package threads;

import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.net.Socket;

public class Writer
{
	public static void main(String[] args)
	{
		try
		{
			final LineNumberReader CONSOLE = new LineNumberReader(new InputStreamReader(System.in));

			final WriterThread writer = new WriterThread();

			Thread hook = new Thread()
			{
				public void run()
				{
					try
					{
						System.out.println("Shutdown hook invoked. Press <ENTER> again to quit.");
						System.out.println("Writer is " + writer.isAlive());
						CONSOLE.readLine();
						System.out.println("Writer is " + writer.isAlive());
					}
					catch (Throwable e)
					{
						e.printStackTrace();
					}
				}
			};

			Runtime.getRuntime().addShutdownHook(hook);

			System.out.println("Press <ENTER> to quit.");

			writer.start();

			CONSOLE.readLine();
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static class WriterThread extends Thread
	{
		WriterThread()
		{
			setDaemon(true);
		}

		public void run()
		{
			try
			{
				final Socket socket = new Socket("localhost", 9283);
				final OutputStream os = socket.getOutputStream();
				int counter = 0;

				while (true)
				{
					if (counter++ == 0)
						System.out.print('.');

					os.write(44);
				}
			}
			catch (Throwable e)
			{
				e.printStackTrace();
			}
		}
	}
}
