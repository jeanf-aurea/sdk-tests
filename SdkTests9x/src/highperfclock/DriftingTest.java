package highperfclock;

import java.util.Date;
import java.util.Random;

public class DriftingTest
{
	private final static long DELAY = /* 6L * 60L * 60L * */ 60000L;

	private Random rand = new Random();

	public static void main(String[] args)
	{
		try
		{
			DriftingTest test = new DriftingTest();

			long myWait = 3 * 60 * 60 * 1000L;
			test.test();
			System.out.println("Sleeping until " + new Date(System.currentTimeMillis() + myWait));
			Thread.sleep(myWait);

			while (true)
			{
				test.test();
			}
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}
	}

	private void test()
		throws Exception
	{
		long now1 = System.currentTimeMillis();
		long now2 = com.actional.Clock.currentTimeMillis();

		System.out.println(new Date(now1) + " | " + now1);
		System.out.println(new Date(now2) + " | " + now2);

		long skew = Math.abs(now1 - now2);

		System.out.println(Long.toString(skew) + " -- " + com.actional.Clock.getCounter());

		if (skew > 50L)
			System.out.println("********* ERROR *********");

		long sleepTime = 6000L + (Math.abs(rand.nextLong()) % DELAY);

		System.out.println("Sleeping " + (sleepTime/1000L) + " seconds until " +
				new Date(now1 + sleepTime));

		Thread.sleep(sleepTime);
	}
}
