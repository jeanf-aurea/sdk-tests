package socket;

import java.net.InetSocketAddress;

public class Loopback
{
	public static void main(String[] args)
	{
		InetSocketAddress inet = new InetSocketAddress(4848);

		System.out.println(inet.getAddress().isLoopbackAddress());

		inet = new InetSocketAddress("127.0.0.1", 4848);

		System.out.println(inet.getAddress().isLoopbackAddress());
	}
}
