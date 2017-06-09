package unmanagedcloud;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.actional.lg.interceptor.sdk.ClientInteraction;
import com.actional.lg.interceptor.sdk.DisplayType;
import com.actional.lg.interceptor.sdk.ServerInteraction;
import com.actional.lg.interceptor.sdk.helpers.InterHelpBase;

public class Client
{
	private static final Map<Object, Connection> CONNECTIONS = Collections.synchronizedMap(new HashMap<Object, Connection>());

	public static void main(String[] args)
		throws Exception
	{
		final LineNumberReader console = new LineNumberReader(new InputStreamReader(System.in));

		while (true)
		{
			System.out.println("Enter path:");
			final String url = console.readLine();

			System.out.println("Enter host target:");
			final String peerAddr = console.readLine();

			System.out.println("Enter real host target (empty if same as 'host target'):");
			String host = console.readLine().trim();

			if (host.length() == 0)
				host = peerAddr;

			final ServerInteraction si = ServerInteraction.begin();

			si.setPeerType(DisplayType.NO_PEER);
			si.setUrl("/App/Service");

			final ClientInteraction ci = ClientInteraction.begin();

			si.setUrl(url);
			si.setPeerAddr(peerAddr);

			send(host, peerAddr, url, InterHelpBase.writeHeader(ci), true);

			ci.end();

			si.end();
		}
	}

	private static void send(String host, String peerAddr, String url, String corrHeaders, boolean tryAgain)
		throws Exception
	{
		Connection conn = CONNECTIONS.get(host);

		if (conn != null)
		{
			conn = new Connection();

			try
			{
				conn.socket = new Socket(host, 9384);
				conn.out = new DataOutputStream(new BufferedOutputStream(conn.socket.getOutputStream()));
				conn.in = new DataInputStream(new BufferedInputStream(conn.socket.getInputStream()));
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return;
			}

			CONNECTIONS.put(host, conn);
		}

		try
		{
			conn.out.writeUTF(peerAddr);
			conn.out.writeUTF(url);
			conn.out.writeUTF(corrHeaders);
		}
		catch (Exception e)
		{
			CONNECTIONS.remove(host);

			if (tryAgain)
				send(host, peerAddr, url, corrHeaders, false);
		}
	}
}


class Connection
{
	Socket socket;
	DataOutputStream out;
	DataInputStream in;
}