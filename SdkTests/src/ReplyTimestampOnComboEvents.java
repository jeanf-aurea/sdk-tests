import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Date;

import com.actional.lg.interceptor.sdk.ServerInteraction;


public class ReplyTimestampOnComboEvents
{
	public static void main(String[] args) throws Exception
	{
		final LineNumberReader reader = new LineNumberReader(new InputStreamReader(System.in, "UTF-8"));

		do
		{
			ServerInteraction si = ServerInteraction.begin();
			long time = System.currentTimeMillis();

			si.setPeerAddr("localhost");
			si.setUrl("/a/b/c");
			si.setBeginTime(time);
			si.setElapsed(1111);
			si.end();

			System.out.println(time + " ==> " + new Date(time));
			System.out.println("Type <ENTER> to generate another event.");
		}
		while (reader.readLine() != null);
	}
}
