package jms.interceptor.ngso;

import util.ManualCorrelator;

import com.actional.lg.interceptor.sdk.ClientInteraction;
import com.actional.lg.interceptor.sdk.ServerInteraction;

public class Test extends util.Call
{
	public static void main(String[] args)
	{
		try
		{
			ManualCorrelator.init();

			System.out.println("<html><head><base href='http://localhost:4040/lgserver/'></head><body><table>");

			useCaseU1();
			useCaseU1B();
			useCaseU2();
			useCaseU3();
			useCaseU3_backward_compatible();
			useCaseU4();
			useCaseU5();

			useCaseI1();
			useCaseI3();

			// unmanaged client going through load balancer
			useCaseLB1();

			// managed client going through load balancer
			useCaseLB2();

//			test1();
//			test2();
//			test3();
//			test4();
//			test5();
//			test6();
//			test7();
//			test8();

			System.out.println("</table></body></html>");
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	private Test(String test)
	{
		super(test);
	}

	private static void sequenceMapHyperlink(String name, String flowId)
	{
		System.out.print("<tr><td><a href='admin/logging/transactionlookup.jsp?flowids=");
		System.out.print(flowId);
		System.out.print("'>");
		System.out.print(name);
		System.out.println("</a></td></tr>");
	}

	private static void useCaseU1() throws Exception
	{
		String prefix = "u1";
		Test test = new Test(prefix);
		final long baseTime = System.currentTimeMillis();

		ClientInteraction ci = test.toFakeQueue(null, "client", "queue", baseTime, 5);
		ServerInteraction si = test.fromFakeQueue(ci, "queue", "backend", "app", "service", "op");

		si.setBeginTime(baseTime + 8L);
		si.setElapsed(7);

		si.end();

		sequenceMapHyperlink("useCaseU1", ci.getFlowID());
	}

	private static void useCaseU1B() throws Exception
	{
		String prefix = "u1";
		Test test = new Test(prefix);
		final long baseTime = System.currentTimeMillis();

		ServerInteraction si1 = test.inbound(null, null, "client", "JMS", "Sender", "Server");

		si1.setBeginTime(baseTime);
		si1.setElapsed(5);

		si1.end();

		ClientInteraction ci = test.toFakeQueue(si1, "client", "queue", baseTime, 5);
		ServerInteraction si = test.fromFakeQueue(ci, "queue", "backend", "app", "service", "op");

		si.setBeginTime(baseTime + 8L);
		si.setElapsed(7);

		si.end();

		sequenceMapHyperlink("useCaseU1B", ci.getFlowID());
	}

	private static void useCaseU2() throws Exception
	{
		String prefix = "u2";
		Test test = new Test(prefix);
		final long baseTime = System.currentTimeMillis();

		ClientInteraction ci = test.toFakeQueue(null, "client", "queue", baseTime, 11);
		ServerInteraction si = test.inbound(ci, "broker-machine3", "backend", "app", "service", "op");

		si.setBeginTime(baseTime + 2L);
		si.setElapsed(3);

		si.end();

		sequenceMapHyperlink("useCaseU2", ci.getFlowID());
	}

	private static void useCaseU3() throws Exception
	{
		String prefix = "u3";
		Test test = new Test(prefix);
		final long baseTime = System.currentTimeMillis();

		ClientInteraction ci = test.outbound(null, "client", "broker-machine1", "broker1", "queue", null);
		ServerInteraction si = test.inbound(ci, "broker-machine1", "backend", "app", "service", "op");
		ci.end();

		si.end();

		sequenceMapHyperlink("useCaseU3(1)", ci.getFlowID());
	}

	private static void useCaseU3_backward_compatible() throws Exception
	{
		String prefix = "u3";
		Test test = new Test(prefix);
		final long baseTime = System.currentTimeMillis();

		ClientInteraction ci = test.outbound(null, "client", "broker-machine1", "broker1", "queue", null);
		ServerInteraction si = test.fromFakeQueue(ci, "queue", "backend", "app", "service", "op");
		ci.end();

		si.end();

		sequenceMapHyperlink("useCaseU3(2)", ci.getFlowID());
	}

	private static void useCaseU4() throws Exception
	{
		String prefix = "u4";
		Test test = new Test(prefix);
		final long baseTime = System.currentTimeMillis();

		ClientInteraction ci = test.outbound(null, "client", "broker-machine1", "broker1", "queue", null);
		ServerInteraction si = test.inbound(ci, "broker-machine1", "backend", "app", "service", "op");
		ci.end();

		si.end();

		sequenceMapHyperlink("useCaseU4", ci.getFlowID());
	}

	private static void useCaseU5() throws Exception
	{
		String prefix = "u5";
		Test test = new Test(prefix);
		final long baseTime = System.currentTimeMillis();

		ClientInteraction ci = test.outbound(null, "client", "broker-machine1", "broker1", "queue", null);
		ServerInteraction si = test.inbound(ci, "broker-machine3", "backend", "app", "service", "op");
		ci.end();

		si.end();

		sequenceMapHyperlink("useCaseU5", ci.getFlowID());
	}

	private static void useCaseI1() throws Exception
	{
		String prefix = "i1";
		Test test = new Test(prefix);
		final long baseTime = System.currentTimeMillis();

		ClientInteraction ci = test.toFakeQueue(null, "client", "queue");
		ServerInteraction si1 = test.inbound(ci, "queue", "broker-machine1", "broker1", "queue", null);
		ServerInteraction si2 = test.inbound(si1, "broker-machine2", "broker1", "queue", null);
		si1.end();
		ServerInteraction si3 = test.inbound(si2, "broker-machine3", "broker1", "queue", null);
		si2.end();
		ServerInteraction si4 = test.inbound(si3, "backend", "app", "service", "op");
		si3.end();
		si4.end();

		sequenceMapHyperlink("useCaseI1", ci.getFlowID());
	}

	private static void useCaseI3() throws Exception
	{
		String prefix = "i3";
		Test test = new Test(prefix);
		final long baseTime = System.currentTimeMillis();

		ClientInteraction ci = test.outbound(null, "client", "broker-machine1", "broker1", "queue", null);
		ServerInteraction si1 = test.inbound(ci);
		ServerInteraction si2 = test.inbound(si1, "broker-machine2", "broker1", "queue", null);
		si1.end();
		ServerInteraction si3 = test.inbound(si2, "broker-machine3", "broker1", "queue", null);
		si2.end();
		ServerInteraction si4 = test.inbound(si3, "backend", "app", "service", "op");
		si3.end();
		si4.end();

		sequenceMapHyperlink("useCaseI3", ci.getFlowID());
	}

	private static void useCaseLB1() throws Exception
	{
		String prefix = "lb1";
		Test test = new Test(prefix);

		ServerInteraction si = test.inbound("client", "server", "app", "service", "op");

		// These manipulations will eventually be done by the
		// HTTP interceptor (or indirectly through SDK calls).
		si.setCallerAddress(si.getPeerAddr());
		si.setCalleeAddress(test.itsTestPrefix + "load.balancer");
		si.setPeerAddr(si.getCalleeAddress());

		si.end();

		sequenceMapHyperlink("useCaseLB1", si.getFlowID());
	}

	private static void useCaseLB2() throws Exception
	{
		String prefix = "lb2";
		Test test = new Test(prefix);

		{
			ClientInteraction ci = test.outbound(null, "client", "load.balancer", "app1", "service1", null);

			ServerInteraction si = test.inbound(ci, "load.balancer", "server", "app", "service", "op1");

			si.end();

			ci.end();

			sequenceMapHyperlink("useCaseLB2(1)", ci.getFlowID());
		}

		{
			ClientInteraction ci = test.outbound(null, "client", "load.balancer", "app1", "service1", null);

			ServerInteraction si = test.inbound(ci, "load.balancer", "server", "app", "service", "op2");

			si.end();

			ci.end();

			sequenceMapHyperlink("useCaseLB2(2)", ci.getFlowID());
		}
	}

	private static void test1() throws Exception
	{
		Test test = new Test("test1");

		ClientInteraction ci = test.toFakeQueue(null, "client", "queue");
		ServerInteraction si = test.fromFakeQueue(ci, "queue", "backend", "app", "service", "op");

		si.end();
	}

	private static void test2() throws Exception
	{
		String prefix = "test2";
		Test test = new Test(prefix);

		ClientInteraction ci = test.toFakeQueue(null, "client", "queue");
		ServerInteraction si = test.fromFakeQueue(ci, "queue", "broker-machine1", "broker1", prefix + "-queue", null);

		test.inbound(si, "backend", "app", "service", "op").end();

		si.end();
	}

	private static void test3() throws Exception
	{
		String prefix = "test3";
		Test test = new Test(prefix);

		ClientInteraction ci = test.outbound(null, "client", "broker-machine1", "broker1", prefix + "-queue", null);

		test.inbound(ci, "backend", "app", "service", "op").end();

		ci.end();
	}

	private static void test4() throws Exception
	{
		String prefix = "test4";
		Test test = new Test(prefix);

		ClientInteraction ci = test.outbound(null, "client", "broker-machine1", "broker1", prefix + "-queue", null);

		ServerInteraction broker2 = test.inbound(ci, "broker-machine2", "broker1", prefix + "-queue", null);

		ci.end();

		ServerInteraction broker3 = test.inbound(broker2, "broker-machine3", "broker1", prefix + "-queue", null);

		broker2.end();

		ServerInteraction backend = test.inbound(broker3, "backend", "app", "service", "op");

		broker3.end();

		backend.end();
	}

	private static void test5() throws Exception
	{
		String prefix = "test5";
		Test test = new Test(prefix);

		ClientInteraction ci = test.outbound(null, "client", "broker-machine1", "broker1", prefix + "-queue", null);

		ServerInteraction backend = test.inbound(ci, "broker-machine3", "backend", "app", "service", "op");

		ci.end();

		backend.end();
	}

	private static void test6() throws Exception
	{
		String prefix = "test6";
		Test test = new Test(prefix);

		ClientInteraction ci = test.outbound(null, "client", "appserver", "myApp", "myService", "myOp");

		ServerInteraction wsInbound = test.inbound(ci);

		ci.end();

		ClientInteraction wsOutbound = test.outbound(wsInbound, "broker-machine1", "broker1", prefix + "-queue", null);

		wsInbound.end();

		ServerInteraction backend = test.inbound(wsOutbound, "broker-machine3", "backend", "app", "service", "op");

		wsOutbound.end();

		backend.end();
	}

	private static void test7() throws Exception
	{
		String prefix = "test7";
		Test test = new Test(prefix);

		ClientInteraction ci = test.toFakeQueue(null, "client", "queue");
		ServerInteraction si = test.inbound(ci, "broker-machine1", "backend", "app", "service", "op");

		si.end();
	}

	private static void test8() throws Exception
	{
		String prefix = "test8";
		Test test = new Test(prefix);

		ClientInteraction ci = test.outbound(null, "client", "appserver", "myApp", "myService", "myOp");

		ServerInteraction wsInbound = test.inbound(ci);

		ci.end();

		ClientInteraction wsOutbound = test.outbound(wsInbound, "broker-machine1", "broker1", prefix + "-queue", null);

		wsInbound.end();

		ServerInteraction backend = test.fromFakeQueue(wsOutbound, "queue", "backend", "app", "service", "op");

		wsOutbound.end();

		backend.end();
	}
}
