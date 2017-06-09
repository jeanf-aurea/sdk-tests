package util;
import com.actional.lg.interceptor.sdk.IMsgFieldEvaluator;


public class FakeEvaluator implements IMsgFieldEvaluator
{
	private final String itsNamePrefix;
	private final String itsValuePrefix;

	public FakeEvaluator(String namePrefix, String valuePrefix)
	{
		itsNamePrefix = namePrefix;
		itsValuePrefix = valuePrefix;
	}

	public String eval(String msgFieldName, String context)
	{
		return itsValuePrefix + "(" + context + ')';
	}
}