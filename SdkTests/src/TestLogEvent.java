import util.Call;
import util.ManualCorrelator;

import com.actional.lg.interceptor.sdk.LogLevel;
import com.actional.lg.interceptor.sdk.ServerInteraction;


public class TestLogEvent extends Call
{
	private static long itsCounter = System.currentTimeMillis();

	public static void main(String[] args)
	{
//		ServerInteraction si = ServerInteraction.begin();
//
//		si.setUrl("/a/b/c/d/e");
//		si.setPeerAddr("www.microsoft.com");
//
//		ILogEntry le = LogEntryFactory.beginLogEntry();
//		le.setLogMessage("test");
//		le.setLogLevel(LogLevel.CRITICAL);
//		le.end();
//		si.requestAnalyzed();
//
//		si.end();

		try
		{
			ManualCorrelator.init();

			new TestLogEvent().run();
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	TestLogEvent()
	{
		super("logevent");
	}

	protected void doRun(int loop) throws Exception
	{
		final ServerInteraction appA = inbound("WebClient", "A", "AppA", "ServiceA", "OpA");
		logEntry(appA, LogLevel.DEBUG, itsCounter++);

		final ServerInteraction appB = inbound(appA, "B", "AppB", "ServiceB", "OpB");
		logEntry(appB, LogLevel.DEBUG, itsCounter++);

		final ServerInteraction appC = inbound(appB, "C", "AppC", "ServiceC", "OpC");
		logEntry(appC, LogLevel.DEBUG, itsCounter++);

		final ServerInteraction appD = inbound(appC, "D", "AppD", "ServiceD", "OpD");
		logEntry(appD, LogLevel.DEBUG, itsCounter++);

		appA.end();
		appB.end();
		appC.end();
		appD.end();
	}
}
