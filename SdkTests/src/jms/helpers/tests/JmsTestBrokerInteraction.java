package jms.helpers.tests;

import java.util.Iterator;
import java.util.Map;

import com.actional.lg.interceptor.sdk.helpers.jms.IJmsUserPropertyMap;
import com.actional.lg.interceptor.sdk.helpers.jms.JmsBrokerInteraction;

public class JmsTestBrokerInteraction extends JmsBrokerInteraction
{
	private JmsFakeMessage itsMessage;

	private String itsBrokerName;
	private String itsFromBrokerMachine;
	private String itsToBrokerMachine;

	public JmsTestBrokerInteraction(
			final JmsFakeMessage	msg,
			final String		brokerName,
			final String		fromBrokerMachine,
			final String		toBrokerMachine)
	{
		itsMessage = msg;
		itsBrokerName = brokerName;
		itsFromBrokerMachine = fromBrokerMachine;
		itsToBrokerMachine = toBrokerMachine;
	}

	protected void extractJmsUserProperties(IJmsUserPropertyMap map) throws Exception
	{
		for (Iterator iter = itsMessage.enumProperties(); iter.hasNext(); /* noop */)
		{
			Map.Entry entry = (Map.Entry) iter.next();
			String name = (String) entry.getKey();
			String value = (String) entry.getValue();

			map.put(name, value);
		}
	}

	protected String getBrokerName() throws Exception
	{
		return itsBrokerName;
	}

	protected String getDestName() throws Exception
	{
		return itsMessage.getDestName();
	}

	protected String getFromBrokerMachineName() throws Exception
	{
		return itsFromBrokerMachine;
	}

	protected String getJMSCorrelationID() throws Exception
	{
		return null;
	}

	protected int getJMSDeliveryMode() throws Exception
	{
		return 0;
	}

	protected long getJMSExpiration() throws Exception
	{
		return 0;
	}

	protected String getJMSMessageID() throws Exception
	{
		return null;
	}

	protected int getJMSPriority() throws Exception
	{
		return 0;
	}

	protected boolean getJMSRedelivered() throws Exception
	{
		return false;
	}

	protected long getJMSTimestamp() throws Exception
	{
		return 0;
	}

	protected String getJMSType() throws Exception
	{
		return null;
	}

	protected String getReplyToDestName() throws Exception
	{
		return itsMessage.getReplyToName();
	}

	protected String getStringProperty(String name) throws Exception
	{
		return itsMessage.getStringProperty(name);
	}

	protected String getToBrokerMachineName() throws Exception
	{
		return itsToBrokerMachine;
	}

	protected boolean isDestQueue() throws Exception
	{
		return itsMessage.isDestQueue();
	}

	protected boolean isReplyToTempDest() throws Exception
	{
		return itsMessage.isReplyToTemp();
	}

	protected void removeStringProperties(String[] names) throws Exception
	{
	}

	protected void setStringProperty(String name, String val) throws Exception
	{
		itsMessage.setStringProperty(name, val);
	}
}
