package highperfclock;

import com.actional.Clock;
import com.actional.lg.interceptor.internal.IBaseEventSource;
import com.actional.lg.interceptor.internal.config.UplinkCfg;
import com.actional.lg.interceptor.sdk.ServerInteraction;

public class Test3
{
	public static void main(String[] args)
	{
		try
		{
			int loopCount = 1*1000*1000;

			// System.setProperty("com.actional.lg.interceptor.showDiscardedEvents", "true");
			// System.setProperty("com.actional.lg.interceptor.remoteUplink", "com.actional.lg.interceptor.sdk.SocketUplink");

//			setUplink();
			loopClock(loopCount);
			loopInteraction(loopCount);
			loopInteraction2(loopCount);

			System.out.println();
			System.out.println("****** WARM UP DONE ******");
			System.out.println();

			loopClock(loopCount);
			loopInteraction(loopCount);
			loopInteraction2(loopCount);
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}
	}

//	private static void setUplink()
//	{
//		Uplink uplink = new Uplink()
//		{
//			public boolean isAgentAlive()
//			{
//				return true;
//			}
//
//			public UplinkCfg getConfig()
//			{
//				return null;
//			}
//
//			public void notifyAgent(IBaseEventSource intr)
//			{
//				System.out.println(intr);
//			}
//
//			public Object copyUplinkData(Object uplinkData)
//			{
//				return null;
//			}
//
//			public void shutdown()
//			{
//
//			}
//		};
//
//		ServerInteraction.setUplink(uplink);
//	}

	private static void loopClock(int loopCount)
	{
		long startCounter = Clock.getCounter();

		doLoop(loopCount);

		System.out.println("(1)==========================================================================");
		System.out.println(Clock.toString(Clock.elapsed(startCounter)));
	}

	private static void loopInteraction(int loopCount)
	{
		long startCounter = Clock.getCounter();

		ServerInteraction si = ServerInteraction.begin();

		si.setUrl("/a/b/c");
		si.setPeerAddr("localhost");
		si.markTime();

		long startCounter2 = Clock.getCounter();
		doLoop(loopCount);
		long elapsed2 = Clock.elapsed(startCounter2);

		si.markElapsed();
		si.end();

		long elapsed = Clock.elapsed(startCounter);

		System.out.println("(2)==========================================================================");
		System.out.println("SI.getElapsed() = " + si.getElapsed());
		System.out.println("Overall         = " + Clock.toString(elapsed));
		System.out.println("Loop elapsed    = " + Clock.toString(elapsed2));
		System.out.println("SDK overhead    = " + Clock.toString(elapsed - elapsed2));
	}

	private static void loopInteraction2(int loopCount)
	{
		long startCounter = Clock.getCounter();

		ServerInteraction si = ServerInteraction.begin();

		si.setUrl("/a/b/c");
		si.setPeerAddr("localhost");
		si.markTime();
		si.requestAnalyzed();

		long startCounter2 = Clock.getCounter();
		doLoop(loopCount);
		long elapsed2 = Clock.elapsed(startCounter2);

		si.markElapsed();

		try
		{
			Thread.sleep(70L);
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}

		si.markCoTime();
		si.end();

		long elapsed = Clock.elapsed(startCounter);

		System.out.println("(2)==========================================================================");
		System.out.println("SI.getElapsed() = " + si.getElapsed());
		System.out.println("Overall         = " + Clock.toString(elapsed));
		System.out.println("Loop elapsed    = " + Clock.toString(elapsed2));
		System.out.println("SDK overhead    = " + Clock.toString(elapsed - elapsed2));
	}

	private static void doLoop(int loopCount)
	{
		Object o = new Object();
		int a = 2;

		for (int i = 0; i < loopCount; i++)
		{
			synchronized(o)
			{
				a = a + 2;
			}
		}
	}
}
