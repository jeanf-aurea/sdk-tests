import com.actional.lg.interceptor.sdk.ClientInteraction;
import com.actional.lg.interceptor.sdk.ServerInteraction;

public class RemoteInterceptor
{
	public static void main(String[] args) throws Exception
	{
		final ServerInteraction si = ServerInteraction.begin();

		si.setUrl("/a/b/c");
		si.setPeerAddr("www.rds.ca");

		final ClientInteraction ci = ClientInteraction.begin();

		ci.setUrl("/d/e/f");
		ci.setPeerAddr("www.progress.com");

		ci.end();

		si.end();

		System.out.println("Press <ENTER>");
		System.in.read();
	}
}
