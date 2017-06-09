package consumerside;

import com.actional.lg.interceptor.sdk.ClientInteraction;
import com.actional.lg.interceptor.sdk.ServerInteraction;

/**
 * Apply a policy to /mtl-jeanf/Consumer Side Tests/Test3/op1
 * This allow to test that if a policy is applied to a specific operation, then that policy won't kick in
 * if there is a call made to the service itself.
 */
public class Test3
{
	public static void main(String[] args)
		throws Exception
	{
		String msgFieldValue;

		if (args.length > 0)
			msgFieldValue = args[0];
		else
			msgFieldValue = new java.util.Date().toString();

		System.out.println("MyMsgField = " + msgFieldValue);

		if (true)
		{
			ServerInteraction si = ServerInteraction.begin();

			si.setGroupName("Consumer Side Tests");
			si.setServiceName("Test3");
			si.setUrl("/consumer-side/test3");
			si.setPeerAddr("mtl-msunde");
			si.setMsgField("MyMsgField", msgFieldValue, true);

			ClientInteraction ci = ClientInteraction.begin();

			ci.setGroupName("Consumer Side Tests");
			ci.setServiceName("Test3 Invocation");
			ci.setUrl("/consumer-side/test3-invocation");
			ci.setPeerAddr("mtl-fileserver");

			ci.end();

			si.end();
		}

		if (true)
		{
			ServerInteraction si = ServerInteraction.begin();

			si.setGroupName("Consumer Side Tests");
			si.setServiceName("Test3");
			si.setOpName("op1"); // <== this is where there is a difference
			si.setUrl("/consumer-side/test3");
			si.setPeerAddr("mtl-msunde");
			si.setMsgField("MyMsgField", msgFieldValue, true);

			ClientInteraction ci = ClientInteraction.begin();

			ci.setGroupName("Consumer Side Tests");
			ci.setServiceName("Test3 Invocation");
			ci.setOpName("op1"); // <== this is where there is a difference
			ci.setUrl("/consumer-side/test3-invocation");
			ci.setPeerAddr("mtl-fileserver");

			ci.end();

			si.end();
		}
	}
}