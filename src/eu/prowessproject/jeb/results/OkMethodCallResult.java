package eu.prowessproject.jeb.results;

import com.ericsson.otp.erlang.OtpErlangObject;

import eu.prowessproject.jeb.environment.Environment;
import eu.prowessproject.jeb.environment.Variable;


public class OkMethodCallResult extends MethodCallResult {

	public static final int TYPE = 1;

	public static final String TYPE_STR = "ok_method_call";
	
	private Variable varResult;

	public OkMethodCallResult(Variable varResult) {
	  this.varResult = varResult;
  }

	@Override
  public int getType() {
		return OkMethodCallResult.TYPE;
  }

	@Override
  public String getTypeStr() {
		return OkMethodCallResult.TYPE_STR;
  }

	@Override
  public OtpErlangObject concreteErlSerialise() {
		return varResult.erlSerialise();
  }

  public static Result concreteErlDeserialise(Environment env, OtpErlangObject object) {
	  return new OkMethodCallResult(Variable.erlDeserialise(env, object));
  }


}
