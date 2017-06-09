package consumerside;

import com.actional.lg.interceptor.sdk.ClientInteraction;
import com.actional.lg.interceptor.sdk.ServerInteraction;
import com.actional.lg.interceptor.sdk.helpers.InterHelpBase;

/**
 * Apply a policy to all nodes
 * This allow to test that Test1 and Test1 Invocation both trigger the policy (provider side and consumer side
 * respectively).
 */
public class Test1
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

		ServerInteraction si = ServerInteraction.begin();

		si.setGroupName("Consumer Side Tests");
		si.setServiceName("Test1");
		si.setUrl("/consumer-side/test1");
		si.setPeerAddr("mtl-msunde");
		si.setPeerAddr("localhost");
		si.setMsgField("MyMsgField", msgFieldValue, true);

		ClientInteraction ci = ClientInteraction.begin();

		ci.setGroupName("Consumer Side Tests");
		ci.setServiceName("Test1 Invocation");
		ci.setUrl("/consumer-side/test1-invocation");
		ci.setPeerAddr("mtl-fileserver");
		ci.setPeerAddr("localhost");

		System.out.println(InterHelpBase.writeHeader(ci));

		ci.end();

		si.end();
	}
}