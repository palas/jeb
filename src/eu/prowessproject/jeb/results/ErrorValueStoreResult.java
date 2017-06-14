package eu.prowessproject.jeb.results;

import com.ericsson.otp.erlang.OtpErlangAtom;
import com.ericsson.otp.erlang.OtpErlangObject;

import eu.prowessproject.jeb.environment.Environment;
import eu.prowessproject.jeb.serialisation.ErlSerialisationUtils;


public class ErrorValueStoreResult extends MethodCallResult {

	public static final int TYPE = 4;

	public static final String TYPE_STR = "error_value_store";

	public ErrorValueStoreResult() {
  }

	@Override
  public int getType() {
		return ErrorValueStoreResult.TYPE;
  }

	@Override
  public String getTypeStr() {
		return ErrorValueStoreResult.TYPE_STR;
  }

	@Override
  public OtpErlangObject concreteErlSerialise() {
		return new OtpErlangAtom(TYPE_STR);
  }

  public static Result concreteErlDeserialise(Environment env, OtpErlangObject object) {
	  ErlSerialisationUtils.checkIsAtom(object, TYPE_STR);
	  return new ErrorValueStoreResult();
  }

}
