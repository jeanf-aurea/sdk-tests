package logentry;

import com.actional.GeneralUtil;
import com.actional.lg.interceptor.sdk.ClientInteraction;
import com.actional.lg.interceptor.sdk.ILogEntry;
import com.actional.lg.interceptor.sdk.LogEntryFactory;
import com.actional.lg.interceptor.sdk.LogLevel;
import com.actional.lg.interceptor.sdk.ServerInteraction;

public class ACT_22314
{
	public static void main(String[] args)
	{
		ServerInteraction si = ServerInteraction.begin();

		si.setUrl("/ACT_22314");
		// si.setPeerType(DisplayType.NO_PEER);
		si.setPeerAddr("www.progress.com");

		final ILogEntry le = LogEntryFactory.beginLogEntry();

		le.setLogClassName("class name");
		le.setLoggerName("log4j");
		le.setLogLevel(LogLevel.ERROR);
		le.setLogThreadID("ThreadID");
		le.setLogThreadName("ThreadName");
		le.setLogTime(System.currentTimeMillis());
		le.setLogMessage("the log message");
		le.end();

		ClientInteraction ci = ClientInteraction.begin();

		ci.setUrl("/a/b");
		ci.setPeerAddr(GeneralUtil.DEMO_PREFIX + "ACT-22314");

		ci.end();

		si.end();
	}
}
