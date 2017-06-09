import util.FakeEvaluator;
import util.ManualCorrelator;

import com.actional.lg.interceptor.sdk.ClientInteraction;
import com.actional.lg.interceptor.sdk.ServerInteraction;

public class TransportHeaders extends util.Call
{
	static
	{
		ManualCorrelator.init();
	}

	private TransportHeaders()
	{
		super("TransportHeaders");
	}

	@Override
	protected void doRun(int loop) throws Exception
	{
		ServerInteraction si1 = inbound("A", "B", "g", "s", "o");
		si1.setElapsed(1900);
		si1._addMsgFieldEvaluator(new FakeEvaluator("si1", "si1"));

		ClientInteraction ci1 = outbound(si1, "C", "g", "s", "o");
		ci1.setElapsed(2300);
		ci1._addMsgFieldEvaluator(new FakeEvaluator("ci1", "ci1"));
		ci1.end();

		ServerInteraction si2 = inbound(ci1, "C", "g", "s", "o");
		si2.setElapsed(3100);
		si2._addMsgFieldEvaluator(new FakeEvaluator("si2", "si2"));

		ClientInteraction ci2 = outbound(si2, "D", "g", "s", "o");
		ci2.setElapsed(3500);
		ci2._addMsgFieldEvaluator(new FakeEvaluator("ci2", "ci2"));
		ci2.requestAnalyzed();
		ci2.end();

		si2.end();
		si1.end();
	}

	public static void main(String[] args) throws Exception
	{
		new TransportHeaders().run();
	}
}
