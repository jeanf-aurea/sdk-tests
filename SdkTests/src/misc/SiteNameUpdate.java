package misc;

import com.actional.lg.interceptor.internal.IAgentEventSource;
import com.actional.lg.interceptor.sdk.ServerInteraction;

import util.ManualCorrelator;

public class SiteNameUpdate
{
	public static void main(String[] args)
	{
		String suffix = args.length == 0 ? "" : args[0];

		ManualCorrelator.init();

		ServerInteraction si = ServerInteraction.begin();
		String g = "MyAppName";
		String s = "MySvcName";
		String o = "MyOpName";

		si.setGroupName(g + suffix);
		si.setGroupID(com.actional.GeneralUtil.md5hash(g));

		si.setServiceName(s + suffix);
		((IAgentEventSource)si).setServiceID(com.actional.GeneralUtil.md5hash(si.getGroupID() + s));

		si.setOpName(o + suffix);
		si.setOpID(com.actional.GeneralUtil.md5hash(((IAgentEventSource)si).getServiceID() + o));

		si.setUrl("/a/b/c/");
		si.setPeerAddr("localhost");

		si.end();
	}
}
