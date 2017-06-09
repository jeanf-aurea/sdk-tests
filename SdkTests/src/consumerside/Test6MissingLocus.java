package consumerside;

import com.actional.lg.interceptor.sdk.ClientInteraction;
import com.actional.lg.interceptor.sdk.Interaction;
import com.actional.lg.interceptor.sdk.InteractionStub;
import com.actional.lg.interceptor.sdk.ServerInteraction;
import com.actional.lg.interceptor.sdk.helpers.InterHelpBase;

/**
 * This test allows you to test that stats are not lost if the locus happens to be
 * created later than its downstream sites, aka if the locus is created in the next
 * gather. <br>
 * The test assumes that there is a gather every minute instead of the default 5 minutes.
 */
public class Test6MissingLocus
{
	public static void main(String[] args)
		throws Exception
	{
		InteractionStub siStub;

		if (true)
		{
			ServerInteraction si = ServerInteraction.begin();

			si.setGroupName("Test Delayed Locus");
			si.setServiceName("Delayed Locus");
			si.setUrl("/test-delayed-locus/delayed-locus");
			si.setPeerAddr("localhost");

			// Following are a few dirty manipulations that we do
			// so that downstream sites are correlated with this SI
			// yet this SI is not sent to the analyzer.

			// Calling getIncludeMsg() will force it to compute
			// its opID, flowID and such.
			si.getIncludeMsg();
			// Make sure that the flowID was generated.
			si.getFlowID();

			siStub = si.detach();

			// We need to clone the InteractioStub because when it will be
			// re-attached, its internal state may be changed to reflect the
			// fact it was re-attached hence there might be logic that will
			// prevent us from re-attaching it again (at least, we're hacking
			// this way so that we send it later).
			InteractionStub tmpStub = (InteractionStub) util.Misc.deepClone(siStub);

			si = (ServerInteraction) Interaction.attach(tmpStub);

			// Mark as internal so that the agent does not know about this
			// interaction; we'll send it later in the next gather using siStub.
			si.setInternal(true);
		}

		String corrHeaders;

		if (true)
		{
			ClientInteraction ci = ClientInteraction.begin();

			ci.setGroupName("Test Delayed Locus");
			ci.setServiceName("Downstream call");
			ci.setUrl("/managed-node/downstream-service-1");
			ci.setPeerAddr("localhost");

			corrHeaders = InterHelpBase.writeHeader(ci);

			Thread.sleep(1200L);

			ci.end();
		}

		if (true)
		{
			ServerInteraction si = ServerInteraction.begin();

			si.setGroupName("Test Delayed Locus");
			si.setServiceName("Downstream call");
			si.setUrl("/managed-node/downstream-service-1");
			si.setPeerAddr("localhost");

			InterHelpBase.readHeader(corrHeaders, si);

			Thread.sleep(1000L);

			si.end();
		}

		if (true)
		{
			long sleepTime = (1L * 60L) * 1000L;

			System.out.println("Waiting " + sleepTime + "ms before sending locus agent event.");

			Thread.sleep(sleepTime); // Wait 1 minute for the next gather.

			ServerInteraction si = (ServerInteraction) Interaction.attach(siStub);

			si.requestAnalyzed();

			Thread.sleep(2000L);

			si.end();
		}
	}
}
