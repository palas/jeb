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
 * Created by: Pablo Lamela on 1/10/2014
 */
package eu.prowessproject.jeb.commands;

import java.util.HashMap;
import java.util.Map;

import com.ericsson.otp.erlang.OtpErlangObject;

import eu.prowessproject.jeb.environment.Environment;
import eu.prowessproject.jeb.environment.Variable;
import eu.prowessproject.jeb.exceptions.ReflectionException;
import eu.prowessproject.jeb.results.ErrorValueStoreResult;
import eu.prowessproject.jeb.results.OkValueStoreResult;
import eu.prowessproject.jeb.results.Result;
import eu.prowessproject.jeb.serialisation.ErlSerialisationUtils;

/**
 * A order to execute a method call with information about how.
 */
public class ValueStoreCommand extends Command {

	public static final int TYPE = 3;

	public static final String TYPE_STR = "value_store";

	public static final String VALUE_STR = "value";

	private Variable valueVar;

	public ValueStoreCommand(Variable valueVar) {
		super();
		this.valueVar = valueVar;
	}

	protected static Object mapGetObjects(Variable valueObject) {
		Object value = valueObject.getObject();
		return value;
	}

	@Override
	public Result execute(Environment env) {
		try {
			Object result = mapGetObjects(valueVar);
			return new OkValueStoreResult(env.storeVariable(result));
		} catch (Throwable a) {
			return new ErrorValueStoreResult();
		}
	}

	@Override
	public int getType() {
		return ValueStoreCommand.TYPE;
	}

	@Override
	public String getTypeStr() {
		return ValueStoreCommand.TYPE_STR;
	}

	@Override
	protected OtpErlangObject concreteErlSerialise() {
		Map<String, OtpErlangObject> map = new HashMap<String, OtpErlangObject>();
		map.put(VALUE_STR, valueVar.erlSerialise());
		return ErlSerialisationUtils.serialiseMap(map);
	}

	public static Command concreteErlDeserialise(Environment env,
			OtpErlangObject object) {
		Map<String, OtpErlangObject> map = ErlSerialisationUtils
				.deserialiseMap(object);
		try {
			Variable valueVar = Variable.erlDeserialise(env, map.get(VALUE_STR));
			return new ValueStoreCommand(valueVar);
		} catch (SecurityException e) {
			throw new ReflectionException(e);
		}
	}

}
