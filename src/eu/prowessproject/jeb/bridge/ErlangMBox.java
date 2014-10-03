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
package eu.prowessproject.jeb.bridge;

import java.io.IOException;

import com.ericsson.otp.erlang.OtpErlangDecodeException;
import com.ericsson.otp.erlang.OtpErlangExit;
import com.ericsson.otp.erlang.OtpErlangPid;
import com.ericsson.otp.erlang.OtpMbox;
import com.ericsson.otp.erlang.OtpMsg;
import com.ericsson.otp.erlang.OtpNode;

import eu.prowessproject.jeb.environment.Environment;
import eu.prowessproject.jeb.exceptions.ErrorCreatingMBox;
import eu.prowessproject.jeb.exceptions.ErrorReceivingMessage;
import eu.prowessproject.jeb.exceptions.NotConnectedException;
import eu.prowessproject.jeb.messages.Message;

/**
 * Is in charge of sending and receiving messages
 */
public class ErlangMBox {

	private OtpNode node;

	private OtpMbox mbox;

	private OtpErlangPid destPid = null;

	/**
	 * @param nodeName
	 *          Name given to the Erlang node created
	 * @param mailBoxName
	 *          Name given to the mail box in the Erlang node
	 * @throws IOException
	 */
	ErlangMBox(String nodeName, String mailBoxName) {
		try {
			node = new OtpNode(nodeName);
			mbox = node.createMbox(mailBoxName);
		} catch (IOException e) {
			throw new ErrorCreatingMBox(e);
		}
	}

	public Message recvMessage(Environment env) {
		try {
			OtpMsg msg = mbox.receiveMsg();
			setRecipientPid(msg.getSenderPid());
			return Message.erlDeserialise(msg.getMsg(), env);
		} catch (OtpErlangExit | OtpErlangDecodeException e) {
			throw new ErrorReceivingMessage(e);
		}
	}

	private synchronized void setRecipientPid(OtpErlangPid destPid) {
		if (this.destPid == null) {
			this.destPid = destPid;
		}
	}

	public void sendMessage(Message msg) {
		if (destPid != null) {
			mbox.send(destPid, msg.erlSerialise());
		} else {
			throw new NotConnectedException();
		}
	}

}
