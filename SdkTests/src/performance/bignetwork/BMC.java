package performance.bignetwork;

import java.util.Date;

import com.actional.lg.interceptor.sdk.ClientInteraction;
import com.actional.lg.interceptor.sdk.ServerInteraction;

public class BMC
{
	private static final String DEMO_PREFIX = "__demo__";

	private static final long GATHER = 5*60*1000;

	private static final int NB_CLIENT = 100;

	private static final int NB_PROVIDER = 100;

	private static final int GROUP = 400;

	private static final int SERVICE = 100;

	private static final int OP = 10;

	public static void main(String[] args) throws Throwable
	{
		final String[] CLIENTS = new String[NB_CLIENT];

		for (int i = 0; i < NB_CLIENT; i++)
		{
			CLIENTS[i] = DEMO_PREFIX + "client" + i;
		}

		final String[] PROVIDERS = new String[NB_PROVIDER];

		for (int i = 0; i < NB_PROVIDER; i++)
		{
			PROVIDERS[i] = DEMO_PREFIX + "provider" + i;
		}

		final String[] GROUP_NAMES = new String[GROUP];

		for (int i = 0; i < GROUP; i++)
		{
			GROUP_NAMES[i] = "group" + i;
		}

		final String[] SERVICE_NAMES = new String[SERVICE];

		for (int j = 0; j < SERVICE; j++)
		{
			SERVICE_NAMES[j] = "service" + j;
		}

		final String[] OP_NAMES = new String[OP];

		for (int k = 0; k < OP; k++)
		{
			OP_NAMES[k] = "op" + k;
		}

		final long MAX_SLEEP = 200;
		final long SLEEP_INC = 25;

		int client = 0;
		int provider = 0;
		long sleep = 0;

		System.out.println(new Date() + " Waiting for next gather...");
		Thread.sleep(GATHER - (System.currentTimeMillis() % GATHER) + 1000);

		while (true)
		{
			System.out.println(new Date() + " Generating traffic...");

			for (int i = 0; i < GROUP; i++)
			{
				for (int j = 0; j < SERVICE; j++)
				{
					final String url = "/" + GROUP_NAMES[i] + '/' + SERVICE_NAMES[j];

					for (int k = 0; k < OP; k++)
					{
						final ServerInteraction si  = ServerInteraction.begin();

						si.setUrl(url);
						si.setOpName(OP_NAMES[k]);
						si.setPeerAddr(CLIENTS[client]);

						final ClientInteraction ci  = ClientInteraction.begin();

						ci.setUrl(url);
						ci.setOpName(OP_NAMES[k]);
						ci.setPeerAddr(PROVIDERS[provider]);
						ci.requestAnalyzed();

						Thread.sleep(50 + sleep);

						ci.end();

						si.end();

						client += 3;
						client = client % NB_CLIENT;

						provider += 7;
						provider = provider % NB_PROVIDER;

						sleep += SLEEP_INC;
						sleep = sleep % MAX_SLEEP;
					}
				}
			}

			System.out.println(new Date() + " Waiting for next gather...");
			Thread.sleep(GATHER - (System.currentTimeMillis() % GATHER) + 1000);
		}
	}
}
