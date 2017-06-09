package jms.helpers.tests;

import util.ManualCorrelator;

import com.actional.GeneralUtil;
import com.actional.lg.interceptor.sdk.ClientInteraction;
import com.actional.lg.interceptor.sdk.DisplayType;
import com.actional.lg.interceptor.sdk.IntCorrelator;
import com.actional.lg.interceptor.sdk.ServerInteraction;
import com.actional.lg.interceptor.sdk.helpers.InterHelpBase;

public class Test // extends util.Call
{
	public static void main(String[] args)
	{
		try
		{
//			ManualCorrelator.init();

			useCaseU1();
//			useCaseU2();
//			useCaseU3();
//			useCaseU3_backward_compatible();
//			useCaseU4();
//			useCaseU5();
//
//			useCaseI1();
//			useCaseI3();
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	private Test(String test)
	{
//		super(test);
	}

	private static ServerInteraction startEx(int testNumber)
	{
		ServerInteraction si = ServerInteraction.begin();

		JmsTestClientInteraction.computeSender(si, testNumber);
		JmsTestClientInteraction.computeIDs(si);

		si.setPeerAddr("jira.progress.com");

		return si;
	}

	private static void useCaseU1() throws Exception
	{
		final String prefix = "u1";
		final String queueName = prefix + "-queue";
		JmsFakeMessage msg = new JmsFakeMessage(queueName);

		ServerInteraction siStart = startEx(1);
		Test test = new Test(prefix);

//		ClientInteraction ci = test.toFakeQueue(null, "client", "queue");
//		ServerInteraction si = test.fromFakeQueue(ci, "queue", "backend", "app", "service", "op");

//		si.end();
	}
}
