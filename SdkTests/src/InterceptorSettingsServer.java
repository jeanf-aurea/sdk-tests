import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.actional.lg.interceptor.sdk.SettingsManager;


public class InterceptorSettingsServer
{
	public static void main(String[] args)
	{
		try
		{
			final int port;

			if (args.length > 0)
				port = Integer.parseInt(args[0]);
			else
				port = 12345;

			final ServerSocket ss = new ServerSocket(port);

			while (true)
			{
				final Socket socket = ss.accept();

				new Connection(socket).start();
			}
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static class Connection extends Thread
	{
		private final Socket itsSocket;
		private final LineNumberReader itsReader;
		private final PrintWriter itsWriter;

		Connection(Socket socket)
			throws IOException
		{
			itsSocket = socket;
			itsReader = new LineNumberReader(new InputStreamReader(socket.getInputStream()));
			itsWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
		}

		public void run()
		{
			try
			{
				while (true)
				{
					try
					{
						final String line = itsReader.readLine();

						if (line == null)
							return;

						if ("close".equals(line))
							return;

						if ("quit".equals(line))
							System.exit(0);

						itsWriter.println(SettingsManager.getSettings().getString(line, "<<null>>"));
						itsWriter.flush();
					}
					catch (Throwable e)
					{
						synchronized(System.err)
						{
							System.err.println("Problem communicating with " + itsSocket);
							e.printStackTrace();
						}

						return;
					}
				}
			}
			finally
			{
				try
				{
					itsSocket.close();
				}
				catch (Throwable e1)
				{
				}
			}
		}
	}
}
