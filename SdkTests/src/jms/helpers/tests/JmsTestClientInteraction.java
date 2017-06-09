package jms.helpers.tests;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.Map;

import com.actional.GeneralUtil;
import com.actional.lg.interceptor.internal.IAgentEventSource;
import com.actional.lg.interceptor.sdk.DisplayType;
import com.actional.lg.interceptor.sdk.Interaction;
import com.actional.lg.interceptor.sdk.helpers.jms.IJmsConsumer;
import com.actional.lg.interceptor.sdk.helpers.jms.IJmsUserPropertyMap;
import com.actional.lg.interceptor.sdk.helpers.jms.IReqRespCorr;
import com.actional.lg.interceptor.sdk.helpers.jms.JmsClientInteraction;

public class JmsTestClientInteraction extends JmsClientInteraction
{
	private JmsFakeMessage itsMessage;

	private String itsBrokerName;
	private String itsBrokerMachine;

	private String itsL2Name;
	private String itsL3Name;
	private String itsL4Name;

	/**
	 * This constructor is used to mimic a generic JMS interceptor.
	 */
	public JmsTestClientInteraction(final JmsFakeMessage msg)
	{
		itsMessage = msg;
	}

	/**
	 * This constructor is used to mimic a generic JMS interceptor.
	 */
	public JmsTestClientInteraction(final JmsFakeMessage msg, final byte[] corrReqResp)
		throws IOException, ClassNotFoundException
	{
		super(deserializeCorrReqResp(corrReqResp));
		itsMessage = msg;
	}

	/**
	 * This constructor is used to mimic a broker-aware JMS interceptor.
	 */
	public JmsTestClientInteraction(
			final JmsFakeMessage	msg,
			final String		brokerName,
			final String		brokerMachine)
	{
		itsMessage = msg;
		itsBrokerName = brokerName;
		itsBrokerMachine = brokerMachine;
	}

	private static IReqRespCorr deserializeCorrReqResp(byte[] bytes)
		throws IOException, ClassNotFoundException
	{
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));

		return (IReqRespCorr) ois.readObject();
	}

	public byte[] getCorrReqRespBytes()
		throws IOException
	{
		Object corrReqResp = getReqRespCorr();

		if (corrReqResp == null)
			return null;

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);

		oos.writeObject(corrReqResp);
		oos.close();

		return baos.toByteArray();
	}

	public void setConsumer(int testNumber)
	{
		itsL2Name = APPNAME;
		itsL3Name = "Test #" + testNumber;
		itsL4Name = "Consumer";
	}

	public void setGSO(String l2name, String l3name, String l4name)
	{
		itsL2Name = l2name;
		itsL3Name = l3name;
		itsL4Name = l4name;
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

	protected short getBrokerType() throws Exception
	{
		return DisplayType.SONIC;
	}

	protected String getDestName() throws Exception
	{
		return itsMessage.getDestName();
	}

	protected String getBrokerMachineName() throws Exception
	{
		return itsBrokerMachine;
	}

	protected String getJMSCorrelationID() throws Exception
	{
		return itsMessage.getJMSCorrelationID();
	}

	protected int getJMSDeliveryMode() throws Exception
	{
		return 0;
	}

	protected String getJMSType() throws Exception
	{
		return null;
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

	protected String getReplyToDestName() throws Exception
	{
		return itsMessage.getReplyToName();
	}

	protected String getStringProperty(String name) throws Exception
	{
		return itsMessage.getStringProperty(name);
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
//		if (names == null)
//			return;
//
//		for (int i = 0, iLen = names.length; i < iLen; i++)
//			itsUserProperties.remove(names[i]);
	}

	protected void setStringProperty(String name, String val) throws Exception
	{
		itsMessage.setStringProperty(name, val);
	}

	protected boolean computeTarget(IJmsConsumer target)
	{
		target.setL2Name(itsL2Name);
		target.setL3Name(itsL3Name);
		target.setL4Name(itsL4Name);

		return true;
	}

	private static final String APPNAME = "JMS helper tests";

	public static void computeSender(Interaction intr, int testNumber)
	{
		computeNames(intr, testNumber, "Sender");
	}

	public static void computeConsumer(Interaction intr, int testNumber)
	{
		computeNames(intr, testNumber, "Consumer");
	}

	private static void computeNames(Interaction intr, int testNumber, String opName)
	{
		intr.setGroupName(APPNAME);
		intr.setServiceName("Test #" + testNumber);
		intr.setOpName(opName);
	}

	public static void computeIDs(Interaction intr)
	{
		String name;

		name = intr.getGroupName();

		if (name != null)
			intr.setGroupID(GeneralUtil.md5hash(name));

		name = intr.getServiceName();

		IAgentEventSource aes = (IAgentEventSource) intr;

		if (name != null)
		{
			assert intr.getGroupID() != null;
			aes.setServiceID(GeneralUtil.md5hash(intr.getGroupID() + '@' + name));
		}

		name = intr.getOpName();

		if (name != null)
		{
			assert aes.getServiceID() != null;
			intr.setOpID(GeneralUtil.md5hash(aes.getServiceID() + '@' + name));
		}

		intr.setUrl("/jms-test/"); // dummy stuff; should not be used since we specified a serviceID
	}

	@Override
	protected byte[] getMessageBytes() throws Exception
	{
		final String msg = "<current.time>" + new java.util.Date() + "</current.time>";

		return msg.getBytes("UTF-8");
	}
}
