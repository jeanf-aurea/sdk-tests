package rf;

import com.actional.lg.interceptor.sdk.ClientInteraction;
import com.actional.lg.interceptor.sdk.ILogEntry;
import com.actional.lg.interceptor.sdk.LogEntryFactory;
import com.actional.lg.interceptor.sdk.ServerInteraction;

import util.ManualCorrelator;

public class Test extends util.Call
{
	private static final byte[] REQUEST = "<request/>".getBytes();
	private static final byte[] REPLY   = "<reply/>".getBytes();

	public static void main(String[] args)
	{
		try
		{
			ManualCorrelator.init();

			Test test = new Test("test1");

			ServerInteraction si = test.inbound("client", "webgui", "purchasing", "buy", null);
//			si.setInternal(true);
//			si.setPayload(REQUEST);
			si.requestAnalyzed();
//			si.setInternal(false);
//			si.setPayload(REPLY);

//			ILogEntry logEv = LogEntryFactory.beginLogEntry();
//
//			logEv.setLogClassName("myLogClassName");
//			logEv.setLoggerName("myLoggerName");
//			logEv.end();

			si.end();

			ClientInteraction ci = test.outbound(si, "bpm-engine1", "app", "service", null);
//			ci.setInternal(true);
			ci.setMsgField("ff1", "geez", false);
//			ci.setPayload(REQUEST);
			ci.requestAnalyzed();
//			ci.setInternal(false);
//			ci.setPayload(REPLY);
			ci.end();

//			System.out.println("Press any key to send 2nd part of the flow.");
//			System.in.read();

			si = test.inbound("bpm-engine2", "windows-machine", ".NET", "service", null);
//			si.setInternal(true);
			si.setMsgField("ff1", "geez", false);
//			si.setPayload(REQUEST);
			si.requestAnalyzed();
//			si.setInternal(false);
//			si.setPayload(REPLY);
			si.end();

			ci = test.outbound(si, "maineframe", "app", "cobol", null);
//			ci.setInternal(true);
//			ci.setPayload(REQUEST);
			ci.requestAnalyzed();
//			ci.setInternal(false);
//			ci.setPayload(REPLY);
			ci.end();
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	Test(String test)
	{
		super(test);
	}
}
