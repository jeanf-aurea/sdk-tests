package jms.helpers.tests;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JmsFakeMessage
{
	final static String TEMP_PATTERN = "$TMP$";

	final private Map	itsProperties;
	final private String	itsDest;
	final private boolean	itsDestIsQueue;
	final private String	itsReplyToDest;
	final private boolean	itsReplyToDestIsQueue;
	final private String	itsJMSCorrelationID;

	JmsFakeMessage(String dest)
	{
		this(dest, null);
	}

	JmsFakeMessage(String dest, String replyTo)
	{
		this(dest, true, replyTo, true);
	}

	JmsFakeMessage(String dest, boolean destIsQueue, String replyTo, boolean replyToIsQueue)
	{
		itsDest = dest;
		itsDestIsQueue = destIsQueue;
		itsReplyToDest = replyTo;
		itsReplyToDestIsQueue = replyToIsQueue;
		itsProperties = new HashMap();
		itsJMSCorrelationID = (replyTo == null) ? null : Long.toString(System.currentTimeMillis());
	}

	String getDestName()		{ return itsDest; }
	boolean isDestQueue()		{ return itsDestIsQueue; }
	String getReplyToName()		{ return itsReplyToDest; }
	boolean isReplyToQueue()	{ return itsReplyToDestIsQueue; }
	boolean isReplyToTemp()		{ return itsReplyToDest.indexOf(TEMP_PATTERN) >= 0; }

	String getStringProperty(String name)
	{
		return (String) itsProperties.get(name);
	}

	void setStringProperty(String name, String value)
	{
		itsProperties.put(name, value);
	}

	Iterator enumProperties()
	{
		return itsProperties.entrySet().iterator();
	}

	String getJMSCorrelationID()
	{
		return itsJMSCorrelationID;
	}
}
