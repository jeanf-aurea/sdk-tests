import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;


public class InterceptorSettingsClient
{
	public static void main(String[] args)
	{
		try
		{
			String host = "localhost";
			int port = 12345;

			if (args.length > 0)
			{
				host = args[0];

				if (args.length > 1)
					port = Integer.parseInt(args[1]);
			}

			final Socket socket = new Socket(host, port);
			final LineNumberReader socketReader = new LineNumberReader(new InputStreamReader(socket.getInputStream()));
			final PrintWriter socketWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			final LineNumberReader lnr = new LineNumberReader(new InputStreamReader(System.in));

			System.out.println("Enter the name of an SDK setting. Empty line to quit.");

			for (String line = lnr.readLine(); line != null ; line = lnr.readLine())
			{
				socketWriter.println(line);
				socketWriter.flush();

				final String val = socketReader.readLine();

				if (val == null)
					return;

				System.out.println("The value is \"" + val + "\".");
			}

			socket.close();
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}
}
