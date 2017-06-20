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

import com.ericsson.otp.erlang.OtpErlangAtom;
import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangTuple;

import eu.prowessproject.jeb.environment.Environment;
import eu.prowessproject.jeb.results.Result;
import eu.prowessproject.jeb.serialisation.ErlSerialisable;
import eu.prowessproject.jeb.serialisation.ErlSerialisationUtils;
import eu.prowessproject.jeb.utils.ReflectionUtils;

/**
 * Represents information describing a command to execute in the JVM using an
 * environment.
 */
public abstract class Command implements ErlSerialisable {

	private static final String CMD_ATOM = "cmd";

	public static final Class<?>[] COMMAND_CLASSES = {
	    ClearEnvironmentCommand.class, MethodCallCommand.class, ValueStoreCommand.class, NewCallCommand.class };

	private static final Map<String, Class<?>> CMD_TYPE_MAP;

	static {
		CMD_TYPE_MAP = new HashMap<String, Class<?>>(COMMAND_CLASSES.length);
		for (Class<?> cmdClass : COMMAND_CLASSES) {
			CMD_TYPE_MAP.put(ReflectionUtils.getTypeStrFromClass(cmdClass), cmdClass);
		}
	}

	public abstract int getType();

	public abstract String getTypeStr();

	@Override
	public OtpErlangObject erlSerialise() {
		return new OtpErlangTuple(new OtpErlangObject[] {
		    new OtpErlangAtom(CMD_ATOM), new OtpErlangAtom(this.getTypeStr()),
		    this.concreteErlSerialise() });
	}

	protected abstract OtpErlangObject concreteErlSerialise();

	public static Command erlDeserialise(Environment env, OtpErlangObject object) {
		OtpErlangObject[] tuple = ErlSerialisationUtils.tupleOfSizeToArray(object,
		    3);
		ErlSerialisationUtils.checkIsAtom(tuple[0], CMD_ATOM);
		String typeStr = ErlSerialisationUtils.getStringFromAtom(tuple[1]);
		Class<?> cmdClass = CMD_TYPE_MAP.get(typeStr);
		return ReflectionUtils.callConcreteDeserialise(Command.class, cmdClass,
		    new Object[] { env, tuple[2] }, new Class<?>[] { Environment.class,
		        OtpErlangObject.class });
	}

	public abstract Result execute(Environment env);

}
