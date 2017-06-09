package consumerside;

import com.actional.lg.interceptor.sdk.ClientInteraction;
import com.actional.lg.interceptor.sdk.DisplayType;
import com.actional.lg.interceptor.sdk.ServerInteraction;

/**
 * Apply a policy to all nodes.
 * Similar to Test1 except that the unmanaged appears as a database running on the same node as the managed provider.
 * This allow to test that Test1 and Test1 Invocation both trigger the policy (provider side and consumer side
 * respectively).
 */
public class Test4
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
		si.setServiceName("Test4");
		si.setUrl("/consumer-side/test4");
		si.setPeerAddr("mtl-msunde");
		si.setMsgField("MyMsgField", msgFieldValue, true);

		if (true)
		{
			ClientInteraction ci = ClientInteraction.begin();

			ci.setGroupName("MyDatabase");
			ci.setServiceName("MyTable");
			ci.setOpName("select");
			ci.setUrl("/jdbc/MyDatabase/MyTable");
			ci.setPeerAddr("mtl-jeanf");

			ci.setOpType(DisplayType.OP);
			ci.setSvcType(DisplayType.TABLE);
			ci.setAppType(DisplayType.DB);
			ci.setPeerType(DisplayType.SYSTEM_OF_RECORD);
			ci.setLeafClient(true);

			if (ci.getIncludeMsg())
				ci.setPayload("<xml>select</xml>".getBytes("UTF-8"));

			ci.requestAnalyzed();

			ci.end();
		}

		if (true)
		{
			ClientInteraction ci = ClientInteraction.begin();

			ci.setGroupName("MyDatabase");
			ci.setServiceName("MyTable");
			ci.setOpName("update");
			ci.setUrl("/jdbc/MyDatabase/MyTable");
			ci.setPeerAddr("mtl-jeanf");

			ci.setOpType(DisplayType.OP);
			ci.setSvcType(DisplayType.TABLE);
			ci.setAppType(DisplayType.DB);
			ci.setPeerType(DisplayType.SYSTEM_OF_RECORD);
			ci.setLeafClient(true);

			if (ci.getIncludeMsg())
				ci.setPayload("<xml>update</xml>".getBytes("UTF-8"));

			ci.requestAnalyzed();

			ci.end();
		}

		si.end();
	}
}