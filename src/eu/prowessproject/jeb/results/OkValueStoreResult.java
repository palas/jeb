package eu.prowessproject.jeb.results;

import com.ericsson.otp.erlang.OtpErlangObject;

import eu.prowessproject.jeb.environment.Environment;
import eu.prowessproject.jeb.environment.Variable;


public class OkValueStoreResult extends MethodCallResult {

	public static final int TYPE = 3;

	public static final String TYPE_STR = "ok_value_store";
	
	private Variable varResult;

	public OkValueStoreResult(Variable varResult) {
	  this.varResult = varResult;
  }

	@Override
  public int getType() {
		return OkValueStoreResult.TYPE;
  }

	@Override
  public String getTypeStr() {
		return OkValueStoreResult.TYPE_STR;
  }

	@Override
  public OtpErlangObject concreteErlSerialise() {
		return varResult.erlSerialise();
  }

  public static Result concreteErlDeserialise(Environment env, OtpErlangObject object) {
	  return new OkValueStoreResult(Variable.erlDeserialise(env, object));
  }


}
