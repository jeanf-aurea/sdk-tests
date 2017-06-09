package alert;

import util.ManualCorrelator;

import com.actional.lg.interceptor.sdk.ClientInteraction;
import com.actional.lg.interceptor.sdk.ServerInteraction;

public class AutoRaiseAlert extends util.Call
{
	public static void main(String[] args)
	{
		try
		{
			ManualCorrelator.init();

			useCaseU1();
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	private AutoRaiseAlert(String test)
	{
		super(test);
	}

	private static void useCaseU1() throws Exception
	{
		String prefix = "u1";
		AutoRaiseAlert test = new AutoRaiseAlert(prefix);

		ClientInteraction ci = test.toFakeQueue(null, "client", "queue");
		ServerInteraction si = test.fromFakeQueue(ci, "queue", "backend", "app", "service", "op");

		si.end();

		test.raiseAlert("http://10.10.39.42:4040/lgserver", si.getFlowID(), "My test : " + si.getPeerAddr());
	}
}
