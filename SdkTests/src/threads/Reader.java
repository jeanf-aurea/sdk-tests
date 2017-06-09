package threads;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Reader
{
	public static void main(String[] args)
	{
		try
		{
			final ServerSocket ss = new ServerSocket(9283);

			while (true)
			{
				final Socket socket = ss.accept();

				System.out.println("Accepted connection at " + new Date());

//				new Thread()
//				{
//					public void run()
//					{
//						try
//						{
//							final InputStream is = socket.getInputStream();
//							final byte[] buf = new byte[16384];
//
//							while (true)
//							{
//								int avail = is.read(buf);
//
//								if (avail < 0)
//								{
//									socket.close();
//									return;
//								}
//							}
//						}
//						catch (Throwable e)
//						{
//							e.printStackTrace();
//							return;
//						}
//					}
//				}.start();
			}
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}
}
