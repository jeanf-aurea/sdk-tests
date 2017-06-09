package flowfields;

import java.util.HashMap;

import com.actional.lg.interceptor.sdk.ClientInteraction;
import com.actional.lg.interceptor.sdk.MsgFieldTransportEvaluator;
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
		String flowFields = "FlowFields=name1:value1,name2:value2";

		ServerInteraction si = ServerInteraction.begin();

		InterHelpBase.readHeader(flowFields, si);

		si.setGroupName("Flow Fields");
		si.setServiceName("Test1");
		si.setUrl("/flow-fields/test1");
		si.setPeerAddr("mtl-msunde");

		HashMap m = new HashMap();

		m.put("header2", "newvalue2");
		m.put("header3", "newvalue3");
		m.put("header4", "newvalue4");

		MsgFieldTransportEvaluator transportEval = new MsgFieldTransportEvaluator(m);
		si._addMsgFieldEvaluator(transportEval);

		si.requestAnalyzed();

		System.out.println(si.getFlowFieldsToPropagate());
		System.out.println(si._getMsgFieldValues());

		ClientInteraction ci = ClientInteraction.begin();

		ci.setGroupName("Flow Fields");
		ci.setServiceName("Test1 Invocation");
		ci.setUrl("/flow-fields/test1-invocation");
		ci.setPeerAddr("mtl-fileserver");

		System.out.println(InterHelpBase.writeHeader(ci));
		System.out.println(ci._getMsgFieldValues());

		ci.end();

		si.end();
	}
}