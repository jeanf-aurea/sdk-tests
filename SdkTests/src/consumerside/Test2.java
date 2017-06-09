package consumerside;

import com.actional.lg.interceptor.sdk.ClientInteraction;
import com.actional.lg.interceptor.sdk.ServerInteraction;
import com.actional.lg.interceptor.sdk.helpers.InterHelpBase;

/**
 * Apply a policy to all nodes.
 * This allows to test that a policy will _not_ trigger twice (once consumer, and once provider).
 */
public class Test2
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

		String corrIDs;

		if (true)
		{
			ServerInteraction si = ServerInteraction.begin();

			si.setGroupName("Consumer Side Tests");
			si.setServiceName("Test2");
			si.setOpName("step1");
			si.setUrl("/consumer-side/test2");
			si.setPeerAddr("mtl-msunde");
			si.setMsgField("MyMsgField", msgFieldValue, true);

			ClientInteraction ci = ClientInteraction.begin();

			ci.setGroupName("Consumer Side Tests");
			ci.setServiceName("Test2");
			ci.setOpName("step2");
			ci.setUrl("/consumer-side/test2");
			ci.setPeerAddr("localhost");

			corrIDs = InterHelpBase.writeHeader(ci);

			ci.end();

			si.end();
		}

		if (true)
		{
			ServerInteraction si = ServerInteraction.begin();

			si.setGroupName("Consumer Side Tests");
			si.setServiceName("Test2");
			si.setOpName("step2");
			si.setUrl("/consumer-side/test2");
			si.setPeerAddr("localhost");
			si.setMsgField("MyMsgField", msgFieldValue, true);

			InterHelpBase.readHeader(corrIDs, si);

			ClientInteraction ci = ClientInteraction.begin();

			ci.setGroupName("Consumer Side Tests");
			ci.setServiceName("Test2");
			ci.setOpName("step3");
			ci.setUrl("/consumer-side/test2");
			ci.setPeerAddr("mtl-fileserver");

			corrIDs = InterHelpBase.writeHeader(ci);

			ci.end();

			si.end();
		}
	}
}