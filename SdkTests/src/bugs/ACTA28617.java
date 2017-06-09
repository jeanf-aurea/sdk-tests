package bugs;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.UnsupportedEncodingException;

import com.actional.lg.interceptor.internal.simulator.Simulator;
import com.actional.lg.interceptor.sdk.ServerInteraction;

public class ACTA28617 extends Simulator
{
	private static final byte[] LARGE_PAYLOAD = createPayload();
	
	public static void main(final String[] args) throws IOException
	{
		new ACTA28617().execute();
	}
	
	private void execute() throws IOException
	{
		final LineNumberReader lnr = new LineNumberReader(new InputStreamReader(System.in));
		
		while (true)
		{
			final ServerInteraction si = inbound("machine1", "machine2", "group2", "service2", "op2");
			
			si.setPayload(LARGE_PAYLOAD);
			si.requestAnalyzed();
			si.setPayload(LARGE_PAYLOAD);
			
			final ServerInteraction si2 = inbound(si, "machine3", "group3", "service3", "op3");
			
			si2.setPayload(LARGE_PAYLOAD);
			si2.requestAnalyzed();
			si2.setPayload(LARGE_PAYLOAD);
			
			si2.end();
			
			si.end();
			
			System.out.println("Correlation succeeded : " + si.getFlowID().equals(si2.getFlowID()));
			
			System.out.println("Press <ENTER> to generate another flow. Type 'exit' to stop the test.");
			
			final String line = lnr.readLine();
			
			if (line == null)
				return;
			
			if ("exit".equals(line))
				return;
		}
	}
	
	private static byte[] createPayload()
	{
		final StringBuilder sb = new StringBuilder();
		
		sb.append("<root>");
		
		for (int i = 0; i < 1000; i++)
		{
			sb.append("<child i='");
			sb.append(i);
			sb.append("'>[");
			sb.append(i);
			sb.append("] ");
			
			for (int k = 0; k < 4; k++)
			{
				for (int j = 65; j < 91; j++) // A-Z
					sb.append((char)j);
				for (int j = 97; j < 123; j++) // a-Z
					sb.append((char)j);
				for (int j = 0; j < 10; j++) // 0-9
					sb.append(j);
			}
			
			sb.append("</child>");
		}
		
		sb.append("</root>");

		try 
		{
			return sb.toString().getBytes("UTF-8");
		} 
		catch (final UnsupportedEncodingException e) 
		{
			throw new RuntimeException(e);
		}
	}
}
