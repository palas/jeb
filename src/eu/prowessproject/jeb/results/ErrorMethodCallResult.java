package eu.prowessproject.jeb.results;

import com.ericsson.otp.erlang.OtpErlangAtom;
import com.ericsson.otp.erlang.OtpErlangObject;

import eu.prowessproject.jeb.environment.Environment;
import eu.prowessproject.jeb.serialisation.ErlSerialisationUtils;


public class ErrorMethodCallResult extends MethodCallResult {

	public static final int TYPE = 2;

	public static final String TYPE_STR = "error_method_call";

	public ErrorMethodCallResult() {
  }

	@Override
  public int getType() {
		return ErrorMethodCallResult.TYPE;
  }

	@Override
  public String getTypeStr() {
		return ErrorMethodCallResult.TYPE_STR;
  }

	@Override
  public OtpErlangObject concreteErlSerialise() {
		return new OtpErlangAtom(TYPE_STR);
  }

  public static Result concreteErlDeserialise(Environment env, OtpErlangObject object) {
	  ErlSerialisationUtils.checkIsAtom(object, TYPE_STR);
	  return new ErrorMethodCallResult();
  }

}
