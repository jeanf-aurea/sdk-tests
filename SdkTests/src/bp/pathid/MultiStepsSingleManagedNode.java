package bp.pathid;

import com.actional.GeneralUtil;
import com.actional.lg.interceptor.sdk.ClientInteraction;
import com.actional.lg.interceptor.sdk.ServerInteraction;
import com.actional.lg.interceptor.sdk.helpers.InterHelpBase;

public class MultiStepsSingleManagedNode
{
	public static void main(String[] args)
		throws Exception
	{
		for (int g = 0; g < 3; g++)
		{
			for (int s = 0; s < 3; s++)
			{
				for (int o = 0; o < 3; o++)
				{
					inbound("app" + g, "service" + s, "op" + o);
				}
			}
		}

		outbound("backend", "appbe", "servicebe", "opbe");
	}

	private static ServerInteraction inbound(String peerAddr, String g, String s, String o)
	{
		final ServerInteraction si = ServerInteraction.begin();

		si.setPeerAddr(GeneralUtil.DEMO_PREFIX + peerAddr);
		si.setUrl("/" + g + "/" + s);
		si.setOpName(o);

		return si;
	}

	private static ServerInteraction inbound(String g, String s, String o)
		throws Exception
	{
		final ServerInteraction parentSI = ServerInteraction.get();
		final String corr;

		if (parentSI == null)
		{
			corr = null;
		}
		else
		{
			final ClientInteraction ci = ClientInteraction.begin();

			ci.setPeerAddr("localhost");
			ci.setUrl("/" + g + "/" + s);
			ci.setOpName(o);

			corr = InterHelpBase.writeHeader(ci);

			ci.end();

			parentSI.end();
		}

		final ServerInteraction si = ServerInteraction.begin();

		si.setPeerAddr("localhost");
		si.setUrl("/" + g + "/" + s);
		si.setOpName(o);

		InterHelpBase.readHeader(corr, si);

		return si;
	}

	private static void outbound(String n, String g, String s, String o)
	{
		final ClientInteraction ci = ClientInteraction.begin();

		ci.setPeerAddr(GeneralUtil.DEMO_PREFIX + n);
		ci.setUrl("/" + g + "/" + s);
		ci.setOpName(o);
		ci.end();

		ServerInteraction.get().end();
	}
}
