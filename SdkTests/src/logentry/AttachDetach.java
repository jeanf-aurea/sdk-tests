package logentry;

import com.actional.lg.interceptor.sdk.ILogEntry;
import com.actional.lg.interceptor.sdk.InteractionStub;
import com.actional.lg.interceptor.sdk.LogEntryFactory;
import com.actional.lg.interceptor.sdk.ServerInteraction;

public class AttachDetach
{
	public static void main(String[] args)
		throws Exception
	{
		ServerInteraction si = ServerInteraction.begin();

		final ILogEntry le = LogEntryFactory.beginLogEntry();

		le.setLogMessage("test");
		le.end();

		// si.itsLogEntries has one entry.

		final byte[] bytes = si.detach().serializeAsBytes();

		si = (ServerInteraction) ServerInteraction.attach((InteractionStub) InteractionStub.deserialize(bytes));

		// si.itsLogEntries does not have any entry.
	}
}
