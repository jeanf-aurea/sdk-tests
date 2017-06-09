package bugs;

import com.actional.lg.interceptor.sdk.ServerInteraction;

public class ACT18888
{
	public static void main(String[] args)
	{
		try
		{
			ServerInteraction si = ServerInteraction.begin();

			si.setGroupName("Jakarta interceptor test web application");
			si.setOpName("(POST)");
			si.setUrl("http://www.google.com:8080/test.3.0/EchoPost.jsp ");
			si.setPeerAddr("www.actional.com");
			si.setCallerAddress("www.actional.com");
			si.setCalleeAddress("www.google.com");
			si.setPlatformType((short) 600);
			si.setAppType((short) 8);
			si.setOpType((short) 24);
			si.requestAnalyzed();
			si.end();
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}

		try
		{
			ServerInteraction si = ServerInteraction.begin();

			si.setGroupName("Jakarta interceptor test web application");
			si.setOpName("(GET)");
			si.setUrl("http://www.google.com:8080/test.3.0/EchoGet.jsp ");
			si.setUrlQuery("msg=some+text");
			si.setPeerAddr("www.actional.com");
			si.setCallerAddress("www.actional.com");
			si.setCalleeAddress("www.google.com");
			si.setPlatformType((short) 600);
			si.setAppType((short) 8);
			si.setOpType((short) 24);
			si.requestAnalyzed();
			si.end();
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}
	}
}
