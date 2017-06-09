package openedge;

import com.actional.lg.interceptor.sdk.DisplayType;
import com.actional.lg.interceptor.sdk.ServerInteraction;

public class TestPortal
{
	public static void main(String[] args)
	{
		final java.io.LineNumberReader CONSOLE = new java.io.LineNumberReader(new java.io.InputStreamReader(System.in));
		try
		{
//			AppThread t = new AppThread(1000);
//
//			t.start();
//			t.join();

			long start = System.currentTimeMillis();
			int nbLoops = Integer.parseInt(args[0]);

			AppThread[] threads = new AppThread[8];

			for (int i = 0, iLen = threads.length; i < iLen; i++)
			{
				threads[i] = new AppThread(nbLoops);
				threads[i].start();
			}

			for (int i = 0, iLen = threads.length; i < iLen; i++)
			{
				threads[i].join();
			}

			long end = System.currentTimeMillis();

			System.out.println(Integer.toString(nbLoops) + " loops in " + (end - start) + "ms.");

//			exec("a3", "b3", "c3", 683);
//			exec("a4", "b4", "c4", 684);
//			exec("a5", "b5", "c5", 685);
//			exec("a6", "b6", "c6", 686);
//			exec("a7", "b7", "c7", 687);
//
//			exec("a2", "b2", "cX", 682);
//			exec("aX", "b2", "cX", 682);

//			System.out.println("Press <ENTER> to quit.");
//			CONSOLE.readLine();
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}
	}
}

class AppThread extends Thread
{
	private int itsLoopCount;

	AppThread(int loopCount)
	{
		itsLoopCount = loopCount;
	}

	public void run()
	{
		final int nbLoops = itsLoopCount;

		for (int i = 0; i < nbLoops; i++)
			exec("a2", "b2", "c2", 682);
	}

	private static final byte[] PAYLOAD = computePayload();

	private static void exec(String gName, String sName, String oName, int sType)
	{
		ServerInteraction si = ServerInteraction.begin();

		si.setPeerAddr("mtl-msunde");
		si.setUrl("/" + gName + "/" + sName);
		si.setPlatformType((short)680);
		si.setAppType((short)681);
		si.setSvcType((short)sType);
		si.setPayload(PAYLOAD);

		if (oName != null)
		{
			si.setOpType(DisplayType.OP);
			si.setOpName(oName);
		}

		si.requestAnalyzed();
		si.end();
	}

	private static byte[] computePayload()
	{
		int size = Integer.getInteger("payload.size", 2 * 1024 * 1024).intValue();

		System.out.println("Sending with payload of size " + size + " bytes");

		if (size > 0)
			return new byte[size];
		else
			return null;
	}
}