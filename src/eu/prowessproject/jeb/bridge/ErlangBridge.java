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
package eu.prowessproject.jeb.bridge;

import eu.prowessproject.jeb.controller.Controller;

/**
 * Listens for an Erlang client and allows it to execute Java commands remotely.
 */
public class ErlangBridge {

	private static final String NODE_NAME = "java_server";

	private static final String MBOX_NAME = "mbox";

	private static ErlangBridge instance = null;

	private Controller controllerState = null;

	private boolean isRunning = false;

	private ErlangMBox mbox;

	ErlangBridge() {
		mbox = new ErlangMBox(NODE_NAME, MBOX_NAME);
	}

	/**
	 * Returns the only instance of ErlangBridge. If it does not exist it is
	 * created.
	 * 
	 * @return ErlangBridge
	 */
	public static ErlangBridge getInstance() {
		if (instance == null) {
			instance = new ErlangBridge();
		}
		return instance;
	}

	public boolean run() {
		if (isAlreadyRunning()) {
			return false;
		} else {
			do {
				controllerState = controllerState.run(mbox);
			} while (controllerState != null);
			isRunning = false;
			return true;
		}
	}

	private synchronized boolean isAlreadyRunning() {
		if (!isRunning) {
			isRunning = true;
			controllerState = Controller.createController();
			return false;
		} else {
			return true;
		}
	}

}
