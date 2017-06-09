package multiple_nis;

import com.actional.lg.interceptor.sdk.ClientInteraction;
import com.actional.lg.interceptor.sdk.ServerInteraction;

public class Test
{
	public static void main(String[] args)
	{
		ServerInteraction si = ServerInteraction.begin();

		si.setUrl("/");
		si.setGroupName("app");
		si.setServiceName("service");
		si.setOpName("op");
		si.setSelfAddr(args[0]);
		si.setPeerAddr("mtl-msunde");

		ClientInteraction ci = ClientInteraction.begin();

		ci.setUrl("http://pluto/");
		ci.setGroupName("app");
		ci.setServiceName("service");
		ci.setOpName("op");

		ci.end();

		si.end();
	}
}
