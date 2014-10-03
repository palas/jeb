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
package eu.prowessproject.jeb.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import eu.prowessproject.jeb.exceptions.ReflectionException;

/**
 * Stores static methods that simplify reflection
 */
public abstract class ReflectionUtils {

	private static final String TYPE_STR_FIELD_NAME = "TYPE_STR";

	private static final String CONCRETE_ERL_DESERIALISE_METHOD_NAME = "concreteErlDeserialise";

	public static String getTypeStrFromClass(Class<?> _class) {
		return getStaticStringFieldFromClass(_class, TYPE_STR_FIELD_NAME);
	}

	private static String getStaticStringFieldFromClass(Class<?> _class,
	    String fieldName) {
		try {
			Field field = _class.getField(fieldName);
			Object value;
			value = field.get(null);
			if (value instanceof String) {
				return (String) value;
			} else {
				throw new ReflectionException("The class \"" + _class.getSimpleName()
				    + "\" has a field " + fieldName + " of the wrong type.");
			}
		} catch (IllegalArgumentException | IllegalAccessException
		    | SecurityException e) {
			throw new ReflectionException(e);
		} catch (NoSuchFieldException e) {
			throw new ReflectionException("The class \"" + _class.getSimpleName()
			    + "\" does not have a field " + fieldName + ".", e);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T callConcreteDeserialise(Class<T> retClass,
	    Class<?> _class, Object[] object, Class<?>[] objectClass) {
		try {
			Method method;
			method = _class.getMethod(CONCRETE_ERL_DESERIALISE_METHOD_NAME,
			    objectClass);
			Object result = method.invoke(null, object);
			if (retClass.isInstance(result)) {
				return (T) result;
			} else {
				throw new ReflectionException("The "
				    + CONCRETE_ERL_DESERIALISE_METHOD_NAME + " method of the class "
				    + _class.getSimpleName() + "returns the wrong type of value.");
			}
		} catch (InvocationTargetException e) {
			throw new ReflectionException(e.getTargetException());
		} catch (IllegalArgumentException | IllegalAccessException
		    | SecurityException e) {
			throw new ReflectionException(e);
		} catch (NoSuchMethodException e) {
			throw new ReflectionException("The class \"" + _class.getSimpleName()
			    + "\" does not have a method " + CONCRETE_ERL_DESERIALISE_METHOD_NAME
			    + ".", e);
		}
	}

	public static Class<?>[] getClassMap(Object[] objects) {
		Class<?>[] objectClasses = new Class<?>[objects.length];
		for (int i = 0; i < objects.length; i++) {
			if (objects[i] == null) {
				objectClasses[i] = null;
			} else {
				objectClasses[i] = objects[i].getClass();
			}
		}
		return objectClasses;
	}

	public static Object safeInvoke(Method method, Object thisObject,
	    Object [] paramObjects) {
		try {
			return method.invoke(thisObject, paramObjects);
		} catch (IllegalAccessException | IllegalArgumentException
		    | InvocationTargetException e) {
			throw new ReflectionException(e);
		}
	}

}
