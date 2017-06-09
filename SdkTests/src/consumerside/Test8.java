package consumerside;

import java.util.HashMap;
import java.util.Map;

import com.actional.lg.interceptor.sdk.ClientInteraction;
import com.actional.lg.interceptor.sdk.Interaction;
import com.actional.lg.interceptor.sdk.MsgFieldTransportEvaluator;
import com.actional.lg.interceptor.sdk.ServerInteraction;
import com.actional.lg.interceptor.sdk.helpers.InterHelpBase;

public class Test8
{
	public static void main(String[] args) throws Exception
	{
		String msgFieldValue;

		if (args.length > 0)
			msgFieldValue = args[0];
		else
			msgFieldValue = new java.util.Date().toString();

		System.out.println("Date = " + msgFieldValue);

		ServerInteraction si = ServerInteraction.begin();

		si.setGroupName("Consumer Side Tests");
		si.setServiceName("Test1");
		si.setUrl("/consumer-side/test1");
		si.setPeerAddr("webfix.americas.progress.com");
		addEvaluators(si, msgFieldValue);

		ClientInteraction ci = ClientInteraction.begin();

		ci.setGroupName("Consumer Side Tests");
		ci.setServiceName("Test1 Invocation");
		ci.setUrl("/consumer-side/test1-invocation");
		ci.setPeerAddr("uxbedsvn1.bedford.progress.com");
		addEvaluators(ci, msgFieldValue);

		final String corr = InterHelpBase.writeHeader(ci);

		ci.end();

		si.end();





		si = ServerInteraction.begin();

		InterHelpBase.readHeader(corr, si);

		si.setGroupName("Consumer Side Tests");
		si.setServiceName("Test1 Invocation");
		si.setUrl("/consumer-side/test1-invocation");
		si.setPeerAddr("localhost");
		si.setSelfAddr("uxbedsvn1.bedford.progress.com");
		addEvaluators(si, msgFieldValue);

		si.end();

		System.out.println("Done");
	}

	private static void addEvaluators(Interaction intr, String date)
	{
		final Map<String, String> values = new HashMap<String, String>();

		values.put("date", date);
		values.put("interaction", intr.getClass().getName());

		intr._addMsgFieldEvaluator(new MsgFieldTransportEvaluator(values));
//		intr._addMsgFieldEvaluator(new FakeEvaluator("Test8", "Test8"));
	}
}
