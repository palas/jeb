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
 * Created by: Pablo Lamela on 2/10/2014
 */
package eu.prowessproject.jeb.environment;

import java.math.BigInteger;

import com.ericsson.otp.erlang.OtpErlangLong;
import com.ericsson.otp.erlang.OtpErlangObject;

import eu.prowessproject.jeb.serialisation.ErlSerialisationUtils;

/**
 * Represents a variable stored in the environment
 */
public class StoredVariable extends Variable {

	public static final int TYPE = 1;

	public static final String TYPE_STR = "var";

	private BigInteger index;

	private Class<?> objectType;

	private Object object;

	/**
	 * @param index
	 * @param objectType
	 */
	StoredVariable(BigInteger index, Class<?> objectType, Object object) {
		super();
		this.index = index;
		this.objectType = objectType;
		this.object = object;
	}

	public BigInteger getIndex() {
		return index;
	}

	public Class<?> getObjectType() {
		return objectType;
	}

	public Object getObject() {
		return object;
	}

	@Override
	public int getType() {
		return StoredVariable.TYPE;
	}

	@Override
	public String getTypeStr() {
		return StoredVariable.TYPE_STR;
	}

	@Override
	public OtpErlangObject concreteErlSerialise() {
		return new OtpErlangLong(this.index);
	}

	public static Variable concreteErlDeserialise(Environment env, OtpErlangObject object) {
		return env.retrieveVariable(ErlSerialisationUtils.getBigIntValue(object));
	}

}