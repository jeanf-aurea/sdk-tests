import java.net.HttpURLConnection;
import java.net.URL;

import com.actional.lg.interceptor.sdk.ServerInteraction;


public class ATandT
{
	public static void main(String[] args)
		throws Exception
	{
		while (true)
		{
			for (int i = 0; i < 1000; i++)
			{
				long start, end, elapsed;

				start = System.currentTimeMillis();
				{
					ServerInteraction si = ServerInteraction.begin();

					si.setUrl("/ATT/FakeService");
					si.setPeerAddr("employee.progress.com");
					si.requestAnalyzed();
					si.end();
				}
				end = System.currentTimeMillis();
				elapsed = Math.abs(end - start);

				if (elapsed > 1000L)
					System.out.println("It took more than 1 second to send the agent event.");



			}

			// Mimic things going on in the event log.
			try
			{
				HttpURLConnection conn = (HttpURLConnection) new URL("http://localhost:4040/lgagent/admin/options.jsp?log=true&logtext=bladibladabladibladabladibladabladibladabladibladabladibladabladibladabladiblada").openConnection();
				conn.setRequestProperty("Authorization", "Basic VXNlcl9BZG1pbjpzZWN1cml0eQ==");
				conn.getResponseCode();
				conn.disconnect();
			}
			catch (Throwable e)
			{
				e.printStackTrace();
			}
		}
	}
}
