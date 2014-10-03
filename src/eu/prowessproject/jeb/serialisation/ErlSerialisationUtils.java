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
package eu.prowessproject.jeb.serialisation;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import com.ericsson.otp.erlang.OtpErlangAtom;
import com.ericsson.otp.erlang.OtpErlangList;
import com.ericsson.otp.erlang.OtpErlangLong;
import com.ericsson.otp.erlang.OtpErlangMap;
import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangString;
import com.ericsson.otp.erlang.OtpErlangTuple;

import eu.prowessproject.jeb.exceptions.WrongErlangValue;
import eu.prowessproject.jeb.exceptions.WrongTypeOfErlangValue;

/**
 * Class that stores common procedures for serialisation and deserialisation
 * from and to Erlang terms.
 */
public abstract class ErlSerialisationUtils {

	public static OtpErlangObject[] tupleOfSizeToArray(OtpErlangObject object,
	    int size) {
		if (object instanceof OtpErlangTuple) {
			OtpErlangTuple tuple = (OtpErlangTuple) object;
			if (tuple.arity() == size) {
				return tuple.elements();
			} else {
				throw new WrongErlangValue("Wrong size of tuple, expected " + size
				    + ", but found: " + tuple.arity());
			}
		} else {
			throw new WrongTypeOfErlangValue(OtpErlangTuple.class, object.getClass());
		}
	}

	public static void checkIsAtom(OtpErlangObject object, String atomText) {
		String atomStr = getStringFromAtom(object);
		if (!atomStr.equals(atomText)) {
			throw new WrongErlangValue("Wrong atom, expected " + atomText
			    + ", but found: " + atomStr);
		}
	}

	public static String getStringFromAtom(OtpErlangObject object) {
		if (object instanceof OtpErlangAtom) {
			OtpErlangAtom atom = (OtpErlangAtom) object;
			return atom.atomValue();
		} else {
			throw new WrongTypeOfErlangValue(OtpErlangAtom.class, object.getClass());
		}
	}

	public static String getStringFromString(OtpErlangObject object) {
		if (object instanceof OtpErlangString) {
			OtpErlangString atom = (OtpErlangString) object;
			return atom.stringValue();
		} else {
			throw new WrongTypeOfErlangValue(OtpErlangString.class, object.getClass());
		}
	}

	public static BigInteger getBigIntValue(OtpErlangObject object) {
		if (object instanceof OtpErlangLong) {
			OtpErlangLong value = (OtpErlangLong) object;
			return value.bigIntegerValue();
		} else {
			throw new WrongTypeOfErlangValue(OtpErlangLong.class, object.getClass());
		}
	}

	public static OtpErlangObject[] getArrayFromList(
	    OtpErlangObject object) {
		if (object instanceof OtpErlangList) {
			OtpErlangList value = (OtpErlangList) object;
			return value.elements();
		} else {
			throw new WrongTypeOfErlangValue(OtpErlangList.class, object.getClass());
		}
	}

	public static OtpErlangMap serialiseMap(Map<String, OtpErlangObject> map) {
		int i = 0;
		OtpErlangObject[] mapKeys = new OtpErlangObject[map.size()];
		OtpErlangObject[] mapValues = new OtpErlangObject[map.size()];
		for (String key : map.keySet()) {
			mapKeys[i] = new OtpErlangAtom(key);
			mapValues[i] = map.get(key);
			i++;
		}
		return new OtpErlangMap(mapKeys, mapValues);
	}

	public static Map<String, OtpErlangObject> deserialiseMap(
	    OtpErlangObject object) {
		if (object instanceof OtpErlangMap) {
			OtpErlangMap oriMap = (OtpErlangMap) object;
			Map<String, OtpErlangObject> destMap = new HashMap<String, OtpErlangObject>();
			OtpErlangObject[] mapKeys = oriMap.keys();
			OtpErlangObject[] mapValues = oriMap.values();
			for (int i = 0; i < mapValues.length; i++) {
				destMap.put(getStringFromAtom(mapKeys[i]), mapValues[i]);
			}
			return destMap;
		} else {
			throw new WrongTypeOfErlangValue(OtpErlangMap.class, object.getClass());
		}
	}

	public static OtpErlangObject[] mapSerialise(ErlSerialisable[] paramObjects) {
		OtpErlangObject[] objectObjects = new OtpErlangObject[paramObjects.length];
		for (int i = 0; i < paramObjects.length; i++) {
			objectObjects[i] = paramObjects[i].erlSerialise();
		}
		return objectObjects;
	}

}
