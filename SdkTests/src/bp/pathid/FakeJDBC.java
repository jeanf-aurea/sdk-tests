package bp.pathid;

import util.ManualCorrelator;

import com.actional.GeneralUtil;
import com.actional.lg.interceptor.sdk.ClientInteraction;
import com.actional.lg.interceptor.sdk.DisplayType;
import com.actional.lg.interceptor.sdk.ServerInteraction;
import com.actional.lg.interceptor.sdk.helpers.InterHelpBase;

public class FakeJDBC
{
	private static final String PREFIX = GeneralUtil.DEMO_PREFIX;

	public static void main(String[] args)
	{
		try
		{
			ManualCorrelator.init();

			FakeJDBC fakeJDBC = new FakeJDBC();

			fakeJDBC.test();
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	private void test()
		throws Exception
	{
		ServerInteraction si;

		si = inbound("A", "B", "MyAppB", "MySvcB", "MyOpB");

		si = inbound(si, "C", "MyAppC", "MySvcC", "MyOpC");

		ClientInteraction ci = ClientInteraction.begin(si);

		ci.setSelfAddr(prefix(si.getSelfAddr()));
		ci.setPeerAddr(prefix("D"));
		ci.setUrl("/MyDatabase/MyTable");
		ci.setOpName("select");
		ci.setAppType(DisplayType.DB);
		ci.setSvcType(DisplayType.TABLE);

		ci.requestAnalyzed();
		ci.end();
	}

	private ServerInteraction inbound(String fromNode, String toNode, String toApp, String toSvc, String toOp)
		throws Exception
	{
		ServerInteraction si = ServerInteraction.begin();

		si.setPeerAddr(prefix(fromNode));
		si.setSelfAddr(prefix(toNode));
		si.setUrl("/" + toApp + '/' + toSvc);
		si.setOpName(toOp);

		si.end();

		return si;
	}

	private ServerInteraction inbound(ServerInteraction parentSI, String toNode, String toApp, String toSvc, String toOp)
		throws Exception
	{
		ClientInteraction ci = ClientInteraction.begin(parentSI);

		ci.setSelfAddr(prefix(parentSI.getSelfAddr()));
		ci.setPeerAddr(prefix(toNode));
		ci.setUrl("/" + toApp + '/' + toSvc);
		ci.setOpName(toOp);

		String corr = InterHelpBase.writeHeader(ci);

		ci.end();

		ServerInteraction si = ServerInteraction.begin();

		InterHelpBase.readHeader(corr, si);

		si.setPeerAddr(prefix(parentSI.getSelfAddr()));
		si.setSelfAddr(prefix(toNode));
		si.setUrl("/" + toApp + '/' + toSvc);
		si.setOpName(toOp);

		si.end();

		return si;
	}

	private String prefix(String addr)
	{
		if (addr.startsWith(PREFIX))
			return addr;
		else
			return PREFIX + addr;
	}
}
