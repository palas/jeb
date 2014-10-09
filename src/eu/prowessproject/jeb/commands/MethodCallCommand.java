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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.ericsson.otp.erlang.OtpErlangList;
import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangString;

import eu.prowessproject.jeb.environment.Environment;
import eu.prowessproject.jeb.environment.Variable;
import eu.prowessproject.jeb.exceptions.ReflectionException;
import eu.prowessproject.jeb.results.OkMethodCallResult;
import eu.prowessproject.jeb.results.Result;
import eu.prowessproject.jeb.serialisation.ErlSerialisationUtils;
import eu.prowessproject.jeb.types.ObjectType;
import eu.prowessproject.jeb.types.Type;
import eu.prowessproject.jeb.utils.ReflectionUtils;

/**
 * A order to execute a method call with information about how.
 */
public class MethodCallCommand extends Command {

	public static final int TYPE = 2;

	public static final String TYPE_STR = "method_call";

	public static final String CLASS_STR = "class";

	public static final String METHOD_STR = "method";

	private Method method;

	public static final String THIS_STR = "this";

	private Variable thisObject;

	public static final String PARAMS_STR = "params";

	public static final String PARAM_TYPES_STR = "param_types";

	private Variable[] paramObjects;

	public MethodCallCommand(Method method, Variable thisObject,
	    Variable[] paramObjects) {
		super();
		this.method = method;
		this.thisObject = thisObject;
		this.paramObjects = paramObjects;
	}

	protected static Object[] mapGetObjects(Variable[] paramObjects) {
		Object[] objectObjects = new Object[paramObjects.length];
		for (int i = 0; i < paramObjects.length; i++) {
			objectObjects[i] = paramObjects[i].getObject();
		}
		return objectObjects;
	}

	@Override
	public Result execute(Environment env) {
		Object result = ReflectionUtils.safeInvoke(this.method,
		    thisObject.getObject(), mapGetObjects(paramObjects));
		return new OkMethodCallResult(env.storeVariable(result));
	}

	@Override
	public int getType() {
		return MethodCallCommand.TYPE;
	}

	@Override
	public String getTypeStr() {
		return MethodCallCommand.TYPE_STR;
	}

	@Override
	protected OtpErlangObject concreteErlSerialise() {
		Map<String, OtpErlangObject> map = new HashMap<String, OtpErlangObject>();
		map.put(CLASS_STR, new OtpErlangString(method.getClass().getName()));
		map.put(METHOD_STR, new OtpErlangString(method.getName()));
		map.put(THIS_STR, thisObject.erlSerialise());
		map.put(PARAMS_STR,
		    new OtpErlangList(ErlSerialisationUtils.mapSerialise(paramObjects)));
		map.put(
		    PARAM_TYPES_STR,
		    new OtpErlangList(ErlSerialisationUtils.mapSerialise(ObjectType
		        .mapCreateFromClass(method.getParameterTypes()))));
		return ErlSerialisationUtils.serialiseMap(map);
	}

	public static Command concreteErlDeserialise(Environment env,
	    OtpErlangObject object) {
		Map<String, OtpErlangObject> map = ErlSerialisationUtils
		    .deserialiseMap(object);
		try {
			OtpErlangObject[] paramArray = ErlSerialisationUtils.getArrayFromList(map
			    .get(PARAMS_STR));
			OtpErlangObject[] paramTypeArray = ErlSerialisationUtils
			    .getArrayFromList(map.get(PARAM_TYPES_STR));
			Variable[] paramObjects = new Variable[paramArray.length];
			Type[] paramTypes = new Type[paramTypeArray.length];
			for (int i = 0; i < paramObjects.length; i++) {
				paramObjects[i] = Variable.erlDeserialise(env, paramArray[i]);
				paramTypes[i] = Type.erlDeserialise(paramTypeArray[i]);
			}
			Method method = Class.forName(
			    ErlSerialisationUtils.getStringFromString(map.get(CLASS_STR)))
			    .getMethod(
			        ErlSerialisationUtils.getStringFromString(map.get(METHOD_STR)),
			        Type.mapTypesToClass(paramTypes));
			Variable thisObject = Variable.erlDeserialise(env, map.get(THIS_STR));
			for (int i = 0; i < paramObjects.length; i++) {
				paramObjects[i] = Variable.erlDeserialise(env, paramArray[i]);
			}
			return new MethodCallCommand(method, thisObject, paramObjects);
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException e) {
			throw new ReflectionException(e);
		}
	}

}
