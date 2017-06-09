package performance;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class FakeAgent
{
	private static int listenerPort;
	private static int nbConnections;
	private static final Object nbConnectionsMonitor = new Object();
	private static volatile boolean stopReading;
	private static volatile long itsDelay;

	public static void main(String[] args)
	{
		try
		{
			if (args.length == 0)
			{
				System.out.println("You must specify the directory for Uplink.cfg");
				System.exit(1);
			}

			System.out.println(java.util.Arrays.asList(args));

			String uplinkCfgStr = args[0];

			if (args.length > 1)
				listenerPort = Integer.parseInt(args[1]);
			else
				listenerPort = 4848;

			if (uplinkCfgStr.endsWith("\"")) // Workaround stupid Windows x64 bug
				uplinkCfgStr = uplinkCfgStr.substring(uplinkCfgStr.length() - 1);

			final File uplinkCfg = new File(uplinkCfgStr + File.separatorChar + "Uplink.cfg");

			uplinkCfg.delete();

			new TouchUplinkCfgThread(uplinkCfg).start();

			new AcceptConnectionsThread().start();

			LineNumberReader CONSOLE = new LineNumberReader(new InputStreamReader(System.in));

			while (true)
			{
				if (stopReading)
					System.out.println("Press <ENTER> to start reading the agent events.");
				else
					System.out.println("Press <ENTER> to stop reading the agent events (and thus block the SDK).");

				String line = CONSOLE.readLine();

				if ("q".equals(line))
				{
					System.out.println("Quitting...");
					return;
				}
				else
				{
					try
					{
						int delay = Integer.parseInt(line);
						if ((delay > 0) && (delay < 15))
							delay = 15;

						if (delay > 0)
							System.out.println("Throttling the Fake Agent by sleeping " + delay + "ms.");
						else
							System.out.println("Fake Agent no longer throttled.");

						itsDelay = delay;

						continue;
					}
					catch (Throwable e)
					{
						// Fallthrough.
					}
				}

				stopReading = !stopReading;
			}
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static byte[] getUplinkCfgContent()
		throws Exception
	{
		String host = InetAddress.getLocalHost().getCanonicalHostName();

		String rtrn =	host + '\n' +
				listenerPort + "\n" +
				"1609936160\n" +
				// Make it appear as if all NGSOs need payload capture
				"1\n" +
				"\n" +
				"\n" +
				"\n" +
				"0\n" +
				"0\n" +
				"0\n" +
				"0\n" +
				"0\n" +
				"0\n" +
				"inproc://localhost/agent\n" +
				"0\n" +
				"0\n" +
				"0\n" +
				host + '\n' +
				"1\n" +
				host + '\n' +
				"true\n" +
				"1\n" +
				host + '\n' +
				"0\n";

		return rtrn.getBytes("UTF-8");
	}

	private static class TouchUplinkCfgThread extends Thread
	{
		private final File itsUplinkCfg;

		TouchUplinkCfgThread(File uplinkCfg)
		{
			setDaemon(true);
			itsUplinkCfg = uplinkCfg;
		}

		void createUplinkCfg() throws Exception
		{
			final File uplinkCfgDir = itsUplinkCfg.getParentFile();

			if (!uplinkCfgDir.exists())
			{
				uplinkCfgDir.mkdirs();

				if (!uplinkCfgDir.exists())
				{
					System.out.println("Unable to create " + uplinkCfgDir);
					System.exit(1);
				}
			}

			final FileOutputStream fos = new FileOutputStream(itsUplinkCfg);
			fos.write(getUplinkCfgContent());
			fos.close();
		}

		public void run()
		{
			while (true)
			{
				try
				{
					if (itsUplinkCfg.exists())
						itsUplinkCfg.setLastModified(System.currentTimeMillis());
					else
						createUplinkCfg();

					Thread.sleep(30000L);
				}
				catch (Throwable e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	private static class AcceptConnectionsThread extends Thread
	{
		AcceptConnectionsThread()
		{
			setDaemon(true);
		}

		public void run()
		{
			try
			{
				System.out.println("Wait for connections...");

				final ServerSocket ss = new ServerSocket(4848);

				while (true)
				{
					final Socket socket = ss.accept();

					new AgentEventListenerThread(socket).start();
				}

			}
			catch (Throwable e)
			{
				e.printStackTrace();
			}
		}
	}

	private static class AgentEventListenerThread extends Thread
	{
		private final Socket itsSocket;

		AgentEventListenerThread(Socket socket)
		{
			setDaemon(true);
			itsSocket = socket;
		}

		public void run()
		{
			synchronized(nbConnectionsMonitor)
			{
				nbConnections++;

				System.out.println("Accepted connection at " + new Date() +
						" (" + nbConnections + " connections)");
			}

			try
			{
				final InputStream is = itsSocket.getInputStream();
				final byte[] buf = new byte[65535];

				while (true)
				{
					int avail = is.read(buf);

					while (stopReading)
					{
						Thread.sleep(1000L);
					}

					if (avail < 0)
					{
						itsSocket.close();
						return;
					}

					if (itsDelay > 0)
						Thread.sleep(itsDelay);
				}
			}
			catch (Throwable e)
			{
				e.printStackTrace();
				return;
			}
			finally
			{
				synchronized(nbConnectionsMonitor)
				{
					nbConnections--;

					System.out.println("Connection closed at " + new Date() +
							" (" + nbConnections + " connections)");

				}
			}
		}
	}
}
