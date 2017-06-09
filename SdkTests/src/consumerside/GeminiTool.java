package consumerside;

import com.actional.lg.interceptor.sdk.ClientInteraction;
import com.actional.lg.interceptor.sdk.DisplayType;
import com.actional.lg.interceptor.sdk.helpers.InterHelpBase;

public class GeminiTool
{
	public static void main(String[] args)
	{
		try
		{
			int requestSize	= (args.length >= 1) ? Integer.parseInt(args[0]) : 403;
			int replySize	= (args.length >= 2) ? Integer.parseInt(args[1]) : 3963012;

			sendCI(requestSize, replySize, false);
			sendCI(requestSize, replySize, true);
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static void sendCI(int requestSize, int replySize, boolean withPayload)
		throws Exception
	{
		ClientInteraction ci = ClientInteraction.begin();

		ci.setOpName("GetAllIssuesForProject");
		ci.setUrl("http://synergy/gemini/geminiws.asmx");
		ci.setPlatformType(DisplayType.MICROSOFT);

		if (withPayload)
			ci.setPayload(createBytes(requestSize));
		else
			ci.setSize(requestSize);

		ci.requestAnalyzed();

		System.out.println(InterHelpBase.writeHeader(ci));

		Thread.sleep(500L);

		if (withPayload)
			ci.setPayload(createBytes(replySize));
		else
			ci.setSize(replySize);

		ci.end();
	}

	private static byte[] createBytes(int count)
	{
		byte[] rtrn = new byte[count];

		for (int i = 0; i < count; i++)
			rtrn[i] = (byte) i;

		return rtrn;
	}
}
