package simple;

import java.io.InputStreamReader;
import java.io.LineNumberReader;

import com.actional.lg.interceptor.sdk.ClientInteraction;
import com.actional.lg.interceptor.sdk.ILogEntry;
import com.actional.lg.interceptor.sdk.LogEntryFactory;
import com.actional.lg.interceptor.sdk.LogLevel;
import com.actional.lg.interceptor.sdk.ServerInteraction;

public class Test
{
	private static final boolean LARGE_PAYLOAD = Boolean.getBoolean("large-payload");

	public static void main(String[] args)
	{
		try
		{
			final LineNumberReader CONSOLE = new LineNumberReader(new InputStreamReader(System.in));
			long counter = System.currentTimeMillis();

			while (true)
			{
				ServerInteraction si = ServerInteraction.begin();

				si.setUrl("/MyApp/MyService");
				si.setPeerAddr("jira.aurea.local");
				si.setMsgField("Test1", Long.toString(counter++), false);
				si.setMsgField("Test2", Long.toString(counter++), false);
				si.setMsgField("flowfield1", Long.toString(counter++), false);
				
				final byte[] payload = LARGE_PAYLOAD ? generateBigPayload(100000) : generatePayload(counter++);
				si.setPayload(payload);

				si.requestAnalyzed();

				final ILogEntry log = LogEntryFactory.beginLogEntry();

				if (log != null)
				{
					log.setLogLevel(LogLevel.INFO);
					log.setLogMessage("Showcasing log events.");
					log.end();
				}

				ClientInteraction ci = ClientInteraction.begin();
				ci.setUrl("/MyBackendApp/MyBackendService");
				ci.setPeerAddr("wiki.aurea.local");
				ci.setPayload(generatePayload(counter++));
				ci.requestAnalyzed();
				ci.end();

				si.end();

				System.out.print('.');
				System.out.flush();
//				Thread.sleep(10000L);

				System.out.println("Hit <ENTER> to generate another interaction.");
				CONSOLE.readLine();
			}
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static byte[] generateBigPayload(final int size)
		throws Exception
	{
		final byte[] bytes = new byte[size];

		for (int i = 0; i < size; i++)
		{
			final int b = (byte)(i % 16);

			bytes[i] = (byte)(b | (b << 4));
		}

		return bytes;
	}

	private static byte[] generatePayload(final long counter)
		throws Exception
	{
		String payload = "<request><value>" + counter + "</value><time>" + new java.util.Date() + "</time></request>";

		return payload.getBytes("UTF-8");
	}
}