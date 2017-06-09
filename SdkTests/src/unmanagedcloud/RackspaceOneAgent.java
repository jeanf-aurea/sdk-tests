package unmanagedcloud;

import java.io.InputStreamReader;
import java.io.LineNumberReader;

import util.ManualCorrelator;

import com.actional.lg.interceptor.sdk.ClientInteraction;
import com.actional.lg.interceptor.sdk.ServerInteraction;

public class RackspaceOneAgent extends util.Call
{
	public static void main(String[] args)
		throws Exception
	{
		ManualCorrelator.init();

		useCaseU1();
	}

	private RackspaceOneAgent(String test)
	{
		super(test);
	}

	private static void useCaseU1() throws Exception
	{
		LineNumberReader lnr = new LineNumberReader(new InputStreamReader(System.in));
		String prefix = "u1";

		do
		{
			RackspaceOneAgent test = new RackspaceOneAgent(prefix);

			ServerInteraction si1 = test.inbound("client", "GlassFish1", "app1", "service1", "op1");
			ClientInteraction ci1 = test.outbound(si1, "F5", "sg2", "ap2", "op2");
			ci1.setInternal(true);
			ci1.end();
			si1.end();

			ServerInteraction si2 = test.inbound(ci1, "AI1", "sg2", "ap2", "op2");
			ClientInteraction ci2 = test.outbound(si2, "GlassFish2", "app3", "service3", "op3");
			ci2.end();
			si2.end();

			System.out.println("Enter a prefix or just <ENTER> to exit.");
			prefix = lnr.readLine().trim();
		}
		while (prefix.length() > 0);
	}
}
