package highperfclock;

import com.actional.Clock;

public class Test4
{
	public static void main(String[] args)
	{
		try
		{
			int loopCount = Integer.parseInt(args[0]);
			int nbThreads = Integer.parseInt(args[1]);

			// WARM UP

			new TestOldClock().run();
			new TestNewClock().run();
			test(new Runnable() { public void run() { } }, loopCount, nbThreads);

			// DO THE TESTS

			test(new TestNewClock(), loopCount, nbThreads);
			test(new TestOldClock(), loopCount, nbThreads);
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}
	}

	private static void test(final Runnable r, final int loopCount, int nbThreads)
		throws Exception
	{
		Thread[] threads = new Thread[nbThreads];

		for (int i = 0; i < nbThreads; i++)
		{
			threads[i] = new Thread()
			{
				public void run()
				{
					for (int i = 0; i < loopCount; i++)
					{
						r.run();
					}
				}
			};
		}

		long start = System.currentTimeMillis();

		for (int i = 0; i < nbThreads; i++)
		{
			threads[i].start();
		}

		for (int i = 0; i < nbThreads; i++)
		{
			threads[i].join();
		}

		long end = System.currentTimeMillis();

		System.out.println(Long.toString(end - start));
	}

	private static class TestOldClock implements Runnable
	{
		public void run()
		{
			long l1 = Clock.currentTimeMillis();
			long l2 = Clock.currentTimeMillis();
			long l3 = l2 - l1;
		}
	}

	private static class TestNewClock implements Runnable
	{
		public void run()
		{
			long cpuCounter = Clock.getCounter();
			long now = System.currentTimeMillis();
			int elapsed = elapsedSinceStart(now, cpuCounter);
		}
	}

	private static int LOW_PRECISION_THRESHOLD = 2000;

	private static int elapsedSinceStart(long itsTime, long itsCpuCounter)
	{
		long now = System.currentTimeMillis();
		int elapsedSystem = (int) (now - itsTime);

		if (elapsedSystem < 0)
		{
			// The clock has gone back in time (either the user
			// changed the clock, or the OS did automatically
			// with a tool like ntpdate).
			// Another possibility is that the interaction was
			// started on machine A, persisted into a stub, and
			// revived on machine B.
			// We can't compute any meaningful elapsed time, so
			// just return zero.
			// TODO Should we return -1 so that the caller knows
			//	that the clock has gone back in time?
			// TODO Should we have a special msg field set to
			//	represent the fact that the clock has gone
			//	back in time? This way, we could raise alerts,
			//	create dimensions, etc.
			return 0;
		}

//		if (Clock.hasMillisPrecision())
//		{
//			// If the clock has millisecond precision, there is
//			// no need for the more complicated logic relying on CPU ticks.
//			return elapsedSystem;
//		}

		if (elapsedSystem > LOW_PRECISION_THRESHOLD)
		{
			// The elapsed time is beyond our threshold. There is
			// not point in computing the elapsed time based on CPU
			// ticks because it may turn out to be even less
			// precise if the CPU had a varying frequency.
			return elapsedSystem;
		}

		if (itsCpuCounter == 0L)
		{
			// Hmmm... this is probably an interaction that was
			// persisted from an older SDK and that got revived.
			// We can't have a more precise calculations of the
			// elapsed time.
			return elapsedSystem;
		}

		int elapsed = (int) Clock.elapsed(itsCpuCounter, Clock.MS);

		if (elapsed < 0)
		{
			// CPU counter has wrapped. Not much we can do about it.
			// We must fallback to the system clock.
			return elapsedSystem;
		}

		if (Math.abs(elapsed - elapsedSystem) > 50)
		{
			// The discrepancy is way too big. We can't trust the
			// elapsed time that was computed based on the CPU ticks.
			return elapsedSystem;
		}

		return elapsed;
	}
}