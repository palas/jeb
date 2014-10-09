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
 * Created by: Pablo Lamela on 8/10/2014
 */
package eu.prowessproject.jeb.types;

import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangString;

import eu.prowessproject.jeb.exceptions.WrongErlangValue;
import eu.prowessproject.jeb.serialisation.ErlSerialisationUtils;


/**
 * Represents the class of an object (not a primitive type)
 */
public class ObjectType extends Type {

	public static final int TYPE = 1;

	public static final String TYPE_STR = "object_type";

	private Class<?> _class;
	
  public ObjectType(Class<?> _class) {
  	this._class = _class;
  }
	
	@Override
  public int getType() {
	  return TYPE;
  }

	@Override
  public String getTypeStr() {
	  return TYPE_STR;
  }

	@Override
	protected OtpErlangObject concreteErlSerialise() {
		return new OtpErlangString(_class.getName());
	}

	public static ObjectType concreteErlDeserialise(OtpErlangObject object) {
		try {
	    Class<?> _class = Class.forName(ErlSerialisationUtils.getStringFromString(object));
	    return new ObjectType(_class);
    } catch (ClassNotFoundException e) {
	    throw new WrongErlangValue(e);
    }
	}

	@Override
  public Class<?> getTypeClass() {
		return this._class;
  }

	public static Type[] mapCreateFromClass(Class<?>[] parameterTypes) {
		Type[] typeObjects = new Type[parameterTypes.length];
		for (int i = 0; i < parameterTypes.length; i++) {
			typeObjects[i] = new ObjectType(parameterTypes[i]);
		}
		return typeObjects;
  }
}
