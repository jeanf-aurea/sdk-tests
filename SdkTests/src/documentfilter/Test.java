package documentfilter;

import java.io.InputStreamReader;
import java.io.LineNumberReader;

import util.ManualCorrelator;

import com.actional.lg.interceptor.sdk.ClientInteraction;
import com.actional.lg.interceptor.sdk.ServerInteraction;

public class Test
{
	public static void main(String[] args)
	{
		try
		{
			ManualCorrelator.init();

			// uninstrumented client --> REST service --> SOAP instrumented service

//			new Test1().run();

			// insrumented client --> REST service --> SOAP uninstrumented service

//			new Test2().run();

			int loop = Integer.getInteger("loop", 10);
			int repeats = 4;

			new Test3().run(loop);

			long start = System.currentTimeMillis();

			for (int i = 0; i < repeats; i++)
				new Test3().run(loop);

			long end = System.currentTimeMillis();

			System.out.println("Average = " + (end - start)/repeats + " ms");
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static void main2(String[] args)
	{
		try
		{
			ManualCorrelator.init();

			LineNumberReader lnr = new LineNumberReader(new InputStreamReader(System.in));
			Network network = NETWORK[Integer.parseInt(args[0])];

			for (String line = lnr.readLine(); (line != null) && (line.length() > 0); line = lnr.readLine())
			{
				network.execute(line);
			}
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	static abstract class Network extends util.Call
	{
		Network()
		{
			super("test");
		}

		abstract void execute(String url) throws Exception;
	}

	private static final Network[] NETWORK =
	{
		new Network()
		{
			public void execute(String url)
			{
				ServerInteraction si = ServerInteraction.begin();

				si.setPeerAddr(PREFIX + "client");
				si.setUrl(url);
				si.end();
				System.out.print(com.actional.GeneralUtil.canonicalURL(url));
				System.out.print(" ==> ");
				System.out.println(si.getUrl());
				System.out.println("\tG = " + si.getGroupName() + " >> " + si.getAppType());
				System.out.println("\tS = " + si.getServiceName() + " >> " + si.getSvcType());
				System.out.println("\tO = " + si.getOpName() + " >> " + si.getOpType());
			}
		}
	};
}

class Test1 extends util.Call
{
	Test1()
	{
		super("test1");
	}

	protected void doRun(int loop) throws Exception
	{
		long id = System.currentTimeMillis();

		for (int i = 0; i < loop; i++)
		{
			ServerInteraction siRest = inbound("uninstrument-client", "rest-server", "Ordering Application", "PlaceOrder", null);

			id++;

			siRest.setUrl(siRest.getUrl() + '/' + id);

			ServerInteraction siSoap = inbound(siRest, "soap-server", "Ordering Backend", "PlacerOrder " + id, null);
			siSoap.end();

			siRest.end();
		}
	}
}

class Test2 extends util.Call
{
	Test2()
	{
		super("test2");
	}

	protected void doRun(int loop) throws Exception
	{
		long id = System.currentTimeMillis();

		for (int i = 0; i < loop; i++)
		{
			ServerInteraction siClient = inbound(NULL_STRING, "instrument-client", "App.exe", "PlaceOrder", null);

			id++;

			ClientInteraction ciRest = outbound(siClient, "rest-server", "Ordering Application", "PlacerOrder " + id, null);

			ciRest.setUrl(ciRest.getUrl() + '/' + id);
			ciRest.end();

			ServerInteraction siRest = inbound(ciRest);

			siRest.setUrl(siRest.getUrl() + '/' + id);

			ClientInteraction ciSoap = outbound(siRest, "soap-server", "Ordering Backend", "PlacerOrder", null);

			ciSoap.end();

			siRest.end();

			siClient.end();
		}
	}
}

class Test3 extends util.Call
{
	Test3()
	{
		super("test3");
	}

	protected void doRun(int loop) throws Exception
	{
		for (int i = 0; i < loop; i++)
		{
			ServerInteraction siClient = inbound(NULL_STRING, "instrument-client", "App.exe", "PlaceOrder", null);

			ClientInteraction ciRest = outbound(siClient, "rest-server", "Ordering Application", "PlacerOrder", null);

			ciRest.setUrl(ciRest.getUrl() + '/');
			ciRest.end();

			ServerInteraction siRest = inbound(ciRest);

			siRest.setUrl(siRest.getUrl() + '/');

			ClientInteraction ciSoap = outbound(siRest, "soap-server", "Ordering Backend", "PlacerOrder", null);

			ciSoap.end();

			siRest.end();

			siClient.end();
		}
	}
}