package rf;

import util.ManualCorrelator;

import com.actional.lg.interceptor.sdk.ServerInteraction;

public class ACT18420 extends util.Call
{
	public static void main(String[] args)
	{
		try
		{
			ManualCorrelator.init();

			new ACT18420("test1").test1();
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	private ACT18420(String test)
	{
		super(test);
	}

	private void test1() throws Exception
	{
		final String ID = Long.toString(System.currentTimeMillis());

		ServerInteraction si = fromFakeQueue(null, "AppJMS!ActivationRequest", "cleanxp1", "ProxyService_default_Business_Act497686", "ejb.jar", "com.bea.wli.sb.transports.jms.JmsInboundMDB");
		si.setMsgField("ID", ID, false);
		toFakeQueue(si, "cleanxp1", "AppJMS!SendQueue");
		si.end();

		si = fromFakeQueue(null, "AppJMS!ReceiveQueue", "cleanxp1", "ProxyService_default_Business_Rec-420084", "ejb.jar", "com.bea.wli.sb.transports.jms.JmsInboundMDB");
		si.setMsgField("ID", ID, false);
		toFakeQueue(si, "cleanxp1", "AppJMS!ThrowAway");
		si.end();
	}
}
