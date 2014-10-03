/**
 * Copyright (c) 2014, Pablo Lamela Seijas
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * Created by: Pablo Lamela on 3/10/2014
 */
package eu.prowessproject.jeb.environment;

import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangString;

import eu.prowessproject.jeb.exceptions.WrongErlangValue;
import eu.prowessproject.jeb.serialisation.ErlSerialisationUtils;


/**
 * Represent a NULL constant with type
 */
public class NullConstant extends Variable {

	public static final int TYPE = 2;

	public static final String TYPE_STR = "null";

	private Class<?> objectType;
	
	public NullConstant(Class<?> _class) {
	  this.objectType = _class;
  }

	@Override
	public Object getObject() {
		return null;
	}

	@Override
	public Class<?> getObjectType() {
		return this.objectType;
	}

	@Override
	public int getType() {
		return NullConstant.TYPE;
	}

	@Override
	public String getTypeStr() {
		return NullConstant.TYPE_STR;
	}

	@Override
	protected OtpErlangObject concreteErlSerialise() {
		return new OtpErlangString(this.objectType.getName());
	}


	public static Variable concreteErlDeserialise(Environment env, OtpErlangObject object) {
		try {
	    Class<?> _class = Class.forName(ErlSerialisationUtils.getStringFromString(object));
	    return new NullConstant(_class);
    } catch (ClassNotFoundException e) {
	    throw new WrongErlangValue(e);
    }
	}

}
