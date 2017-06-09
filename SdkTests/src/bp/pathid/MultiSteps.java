package bp.pathid;

import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Collections;
import java.util.Random;

import com.actional.lg.interceptor.sdk.Interaction;
import com.actional.lg.interceptor.sdk.MsgFieldTransportEvaluator;
import com.actional.lg.interceptor.sdk.PartBytes;
import com.actional.lg.interceptor.sdk.ServerInteraction;

public class MultiSteps extends util.Call
{
	private static final Random RAND = new Random();

	public static void main(final String[] args)
		throws Exception
	{
		util.ManualCorrelator.init();

		new MultiSteps().run();
	}

	private MultiSteps()
	{
		super("bp");
	}

	protected void doRun2(final int loop) throws Exception
	{
		final LineNumberReader lnr = new LineNumberReader(new InputStreamReader(System.in));

		while (true)
		{
			final ServerInteraction si1 = inbound("client", "machine1", "app1", "service1", "op1");
			final String value = defineTransportHeader(si1);
			final ServerInteraction si2 = inbound(si1, "machine2", "app2", "service2", "op2");
			defineTransportHeader(si2);
			final ServerInteraction si3 = inbound(si2, "machine3", "app3", "service3", "op3");
			defineTransportHeader(si3);
			final ServerInteraction si4 = inbound(si3, "machine4", "app4", "service4", "op4");
			defineTransportHeader(si4, value);
			final ServerInteraction si5 = inbound(si4, "machine5", "app5", "service5", "op5");
			defineTransportHeader(si5);
			si5.setPayload("<test>payload5</test>".getBytes("UTF-8"));
			final ServerInteraction si6 = inbound(si5, "machine6", "app6", "service6", "op6");
			defineTransportHeader(si6);
			PartBytes part = new PartBytes("<test2>payload6-2</test2>".getBytes("UTF-8"), "text/xml", null);
			part.setMetaData(Collections.<String, Object>singletonMap("index", "67"));
			si6.addRequestPart(part);
			part = new PartBytes("<test>payload6</test>".getBytes("UTF-8"), "text/xml", null);
			part.setMetaData(Collections.<String, Object>singletonMap("index", "67"));
			si6.addRequestPart(part);
			final ServerInteraction si7 = inbound(si6, "machine7", "app7", "service7", "op7");
			defineTransportHeader(si7);
			final ServerInteraction si8 = inbound(si7, "machine8", "app8", "service8", "op8");
			defineTransportHeader(si8);
			final ServerInteraction si9 = inbound(si8, "machine9", "app9", "service9", "op9");
			defineTransportHeader(si9);

			outbound(si9, "backend", "appbe", "servicebe", "opbe").end();

			si9.end();
			si8.end();
			si7.end();
			si6.end();
			si5.end();
			si4.end();
			si3.end();
			si2.end();
			si1.end();

			System.out.println("Flow: " + si1.getFlowID());

			final String line = lnr.readLine();

			if (line == null || "exit".equals(line) || "quit".equals(line))
				return;
		}
	}

	protected void doRun(final int loop) throws Exception
	{
		final LineNumberReader lnr = new LineNumberReader(new InputStreamReader(System.in));

		while (true)
		{
			final ServerInteraction si6 = inbound("client", "machine6", "app6", "service6", "op6");
			defineTransportHeader(si6);
			PartBytes part = new PartBytes("<test2>pay@@load@@6-2</test2>".getBytes("UTF-8"), "text/xml", null);
			part.setMetaData(Collections.<String, Object>singletonMap("index", "67"));
			si6.addRequestPart(part);
			part = new PartBytes("<test>pay@@load@@6</test>".getBytes("UTF-8"), "text/xml", null);
			part.setMetaData(Collections.<String, Object>singletonMap("index", "67"));
			si6.addRequestPart(part);
			si6.requestAnalyzed();
			si6.end();

			System.out.println("Flow: " + si6.getFlowID());

			final String line = lnr.readLine();

			if (line == null || "exit".equals(line) || "quit".equals(line))
				return;
		}
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix41341 - write new sanity test for Context.* messsage fields
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	private static String defineTransportHeader(final Interaction intr)
	{
		final String value = Long.toString(Math.abs(RAND.nextLong()));

		defineTransportHeader(intr, value);

		return value;
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix41341 - write new sanity test for Context.* messsage fields
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	private static void defineTransportHeader(final Interaction intr, final String value)
	{
		intr._addMsgFieldEvaluator(new MsgFieldTransportEvaluator(Collections.singletonMap("id", value)));
	}
}
