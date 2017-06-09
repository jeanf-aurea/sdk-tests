package unmanagedcloud;

import com.actional.GeneralUtil;
import com.actional.lg.interceptor.sdk.ClientInteraction;
import com.actional.lg.interceptor.sdk.ServerInteraction;
import com.actional.lg.interceptor.sdk.helpers.InterHelpBase;

public class LoadBalancer
{
	protected static final String PREFIX = GeneralUtil.DEMO_PREFIX;

	public static void main(String[] args)
		throws Exception
	{
		if ((args == null) || (args.length == 0))
		{
			useCase1_invisible_lb();
			useCase2();
			useCase3_invisible_lb();
			useCase4();
		}

		for (int i = 0, iLen = args.length; i < iLen; i++)
		{
			switch (Integer.parseInt(args[i]))
			{
				case 1:
					useCase1_invisible_lb();
					break;

				case 2:
					useCase2();
					break;

				case 3:
					useCase3_invisible_lb();
					break;
				case 4:
					useCase4();
					break;

				default:
					System.out.println("Unknown use case: " + args[i]);
			}
		}
	}

	private static void useCase1_invisible_lb()
	{
		final ServerInteraction si = ServerInteraction.begin();

		si.setUrl("/MyApp/MyService");
		si.setPeerAddr(nodeName("uc1-unmanaged-consumer"));
		si.setSelfAddr(nodeName("uc1-backend"));

		si.end();
	}

	private static void useCase2()
	{
		final ServerInteraction si = ServerInteraction.begin();

		si.setUrl("/MyApp/MyService");
		si.setPeerAddr(nodeName("uc2-load-balancer"));
		si.setSelfAddr(nodeName("uc2-backend"));

		si.end();
	}

	private static void useCase3_invisible_lb()
		throws Exception
	{
		final ClientInteraction ci = ClientInteraction.begin();

		ci.setUrl("/MyApp/MyService");
		ci.setSelfAddr(nodeName("uc3-managed-consumer"));
		ci.setPeerAddr(nodeName("uc3-load-balancer"));

		final String corrHeaders = InterHelpBase.writeHeader(ci);

		final ServerInteraction si = ServerInteraction.begin();

		InterHelpBase.readHeader(corrHeaders, si);

		si.setUrl("/MyApp/MyService");
		si.setPeerAddr(nodeName("uc3-managed-consumer"));
		si.setSelfAddr(nodeName("uc3-backend"));

		si.end();

		ci.end();
	}

	private static void useCase4()
		throws Exception
	{
		final ClientInteraction ci = ClientInteraction.begin();

		ci.setUrl("/MyApp/MyService");
		ci.setSelfAddr(nodeName("uc4-managed-consumer"));
		ci.setPeerAddr(nodeName("uc4-load-balancer"));

		final String corrHeaders = InterHelpBase.writeHeader(ci);

		final ServerInteraction si = ServerInteraction.begin();

		InterHelpBase.readHeader(corrHeaders, si);

		si.setUrl("/MyApp/MyService");
		si.setPeerAddr(nodeName("uc4-load-balancer"));
		si.setSelfAddr(nodeName("uc4-backend"));

		si.end();

		ci.end();
	}

	private static String nodeName(String nodeName)
	{
		if (nodeName.startsWith(PREFIX))
			return nodeName;
		else
			return PREFIX + nodeName;
	}
}
