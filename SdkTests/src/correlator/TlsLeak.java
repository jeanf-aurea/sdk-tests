package correlator;

import com.actional.lg.interceptor.sdk.ServerInteraction;

public class TlsLeak
{
	public static void main(String[] args)
	{
		if (args.length == 0)
		{
			System.out.println("You must specify how many interactions you want to be stacked up.");
			System.exit(1);
		}

		int depth = Integer.parseInt(args[0]);

		for (int i = 0; i < depth; i++)
		{
			ServerInteraction si = ServerInteraction.begin();

			si.setUrl("/");
			si.requestAnalyzed();
		}
	}
}
