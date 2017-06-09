package jms.interceptor.ngso;

import com.actional.lg.interceptor.sdk.ClientInteraction;
import com.actional.lg.interceptor.sdk.ServerInteraction;

import util.ManualCorrelator;

public class ACT20300 extends util.Call
{
	public static void main(String[] args)
	{
		try
		{
			ManualCorrelator.init();

			useCaseU1();

			Thread.sleep(1000);
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	private ACT20300(String test)
	{
		super(test);
	}

	private static void useCaseU1() throws Exception
	{
		String prefix = "u1";
		ACT20300 test = new ACT20300(prefix);

		ClientInteraction ci = test.toFakeQueue(null, "client", "request-queue");
		ServerInteraction si = test.fromFakeQueue(ci, "reply-queue", "backend", "app", "service", "op");

		si.end();
		ci.end();
	}
}
