package consumerside;

import com.actional.lg.interceptor.sdk.ClientInteraction;
import com.actional.lg.interceptor.sdk.ServerInteraction;

public class Test5
{
	public static void main(String[] args)
		throws Exception
	{
		ServerInteraction si = ServerInteraction.begin();

		si.setGroupName("Managed Node");
		si.setServiceName("Managed Service 1");
		si.setUrl("/managed-node/Managed-service-1");
		si.setPeerAddr("localhost");
		si.setPayload("<provider.request/>".getBytes("UTF-8"));

		{
			ClientInteraction ci = ClientInteraction.begin();

			ci.setGroupName("Managed Node");
			ci.setServiceName("Unmanaged Service 1");
			ci.setUrl("/managed-node/unmanaged-service-1");
			ci.setPeerAddr("localhost");
			ci.setPayload("<consumer.request/>".getBytes("UTF-8"));
			ci.requestAnalyzed();
			ci.setPayload("<consumer.reply/>".getBytes("UTF-8"));

			ci.end();
		}

		{
			ClientInteraction ci = ClientInteraction.begin();

			ci.setGroupName("Managed Node");
			ci.setServiceName("Managed Service 1");
			ci.setUrl("/managed-node/Managed-service-1");
			ci.setPeerAddr("localhost");
			ci.setPayload("<provider.request2/>".getBytes("UTF-8"));
			ci.requestAnalyzed();
			ci.setPayload("<provider.reply2/>".getBytes("UTF-8"));
			ci.end();
		}

		si.setPayload("<provider.reply/>".getBytes("UTF-8"));
		si.end();
	}
}
