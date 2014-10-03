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
package eu.prowessproject.jeb.messages;

import com.ericsson.otp.erlang.OtpErlangObject;

import eu.prowessproject.jeb.commands.Command;
import eu.prowessproject.jeb.environment.Environment;

/**
 * Represents a message used to send a command.
 */
public class CommandMessage extends Message {

	public static final int TYPE = 2;

	public static final String TYPE_STR = "command";

	private Command command;

	public CommandMessage(Command command) {
		this.command = command;
	}

	public Command getCommand() {
		return command;
	}

	@Override
	public int getType() {
		return CommandMessage.TYPE;
	}

	@Override
	public String getTypeStr() {
		return SynMessage.TYPE_STR;
	}

	@Override
	public OtpErlangObject concreteErlSerialise() {
		return this.command.erlSerialise();
	}

	public static Message concreteErlDeserialise(Environment env, OtpErlangObject object) {
		return new CommandMessage(Command.erlDeserialise(env, object));
	}

}
