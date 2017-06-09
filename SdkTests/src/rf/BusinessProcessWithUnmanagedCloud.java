package rf;

import util.Call;

import com.actional.lg.interceptor.internal.ManualCorrelator;
import com.actional.lg.interceptor.sdk.ClientInteraction;
import com.actional.lg.interceptor.sdk.ServerInteraction;

// ACT-19747 http://jira.bedford.progress.com/browse/ACT-19747
public class BusinessProcessWithUnmanagedCloud extends Call
{
	public static void main(String[] args)
		throws Throwable
	{
		ManualCorrelator.init();

		new BusinessProcessWithUnmanagedCloud("test1").run();
	}

	BusinessProcessWithUnmanagedCloud(String test)
	{
		super(test);
	}

	protected void doRun(int loop) throws Exception
	{
		for (int i = 0; i < loop; i++)
		{
			ServerInteraction siWebMethods = inbound(NULL_STRING, "webMethods", "wm-group", "wm-app", null);

			ClientInteraction ciQueue = toFakeQueue(siWebMethods, "some-queue");

			siWebMethods.end();

			ServerInteraction siWebSphere = inbound(ciQueue, "DataPower", "WebSphere", "ws-group", "ws-app", null);

			ClientInteraction ciSoap = outbound(siWebSphere, "soap-server", "Ordering Backend", "PlacerOrder", null);

			ciSoap.end();

			siWebSphere.end();
		}
	}
}
