package jms.fake;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;

import javax.jms.Destination;
import javax.jms.JMSException;

public class MessageImpl implements javax.jms.Message
{
	private final HashMap<String, Object> itsProperties = new HashMap<String, Object>();
	private final Destination itsDest;

	public MessageImpl(final Destination dest)
	{
		itsDest = dest;
	}

	public void acknowledge() throws JMSException
	{
	}

	public void clearBody() throws JMSException
	{
	}

	public void clearProperties() throws JMSException
	{
		itsProperties.clear();
	}

	public boolean getBooleanProperty(String arg0) throws JMSException
	{
		Boolean rtrn = (Boolean) itsProperties.get(arg0);

		if (rtrn == null)
			return false;
		else
			return rtrn.booleanValue();
	}

	public byte getByteProperty(String arg0) throws JMSException
	{
		Byte rtrn = (Byte) itsProperties.get(arg0);

		if (rtrn == null)
			return 0;
		else
			return rtrn.byteValue();
	}

	public double getDoubleProperty(String arg0) throws JMSException
	{
		Double rtrn = (Double) itsProperties.get(arg0);

		if (rtrn == null)
			return 0;
		else
			return rtrn.doubleValue();
	}

	public float getFloatProperty(String arg0) throws JMSException
	{
		Float rtrn = (Float) itsProperties.get(arg0);

		if (rtrn == null)
			return 0;
		else
			return rtrn.floatValue();
	}

	public int getIntProperty(String arg0) throws JMSException
	{
		Integer rtrn = (Integer) itsProperties.get(arg0);

		if (rtrn == null)
			return 0;
		else
			return rtrn.intValue();
	}

	public String getJMSCorrelationID() throws JMSException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public byte[] getJMSCorrelationIDAsBytes() throws JMSException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public int getJMSDeliveryMode() throws JMSException
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public Destination getJMSDestination() throws JMSException
	{
		return itsDest;
	}

	public long getJMSExpiration() throws JMSException
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public String getJMSMessageID() throws JMSException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public int getJMSPriority() throws JMSException
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean getJMSRedelivered() throws JMSException
	{
		// TODO Auto-generated method stub
		return false;
	}

	public Destination getJMSReplyTo() throws JMSException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public long getJMSTimestamp() throws JMSException
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public String getJMSType() throws JMSException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public long getLongProperty(String arg0) throws JMSException
	{
		Long rtrn = (Long) itsProperties.get(arg0);

		if (rtrn == null)
			return 0;
		else
			return rtrn.longValue();
	}

	public Object getObjectProperty(String arg0) throws JMSException
	{
		return itsProperties.get(arg0);
	}

	public Enumeration getPropertyNames() throws JMSException
	{
		return Collections.enumeration(itsProperties.keySet());
	}

	public short getShortProperty(String arg0) throws JMSException
	{
		Short rtrn = (Short) itsProperties.get(arg0);

		if (rtrn == null)
			return 0;
		else
			return rtrn.shortValue();
	}

	public String getStringProperty(String arg0) throws JMSException
	{
		return (String) itsProperties.get(arg0);
	}

	public boolean propertyExists(String arg0) throws JMSException
	{
		return itsProperties.containsKey(arg0);
	}

	public void setBooleanProperty(String arg0, boolean arg1) throws JMSException
	{
		itsProperties.put(arg0, arg1);
	}

	public void setByteProperty(String arg0, byte arg1) throws JMSException
	{
		itsProperties.put(arg0, arg1);
	}

	public void setDoubleProperty(String arg0, double arg1) throws JMSException
	{
		itsProperties.put(arg0, arg1);
	}

	public void setFloatProperty(String arg0, float arg1) throws JMSException
	{
		itsProperties.put(arg0, arg1);
	}

	public void setIntProperty(String arg0, int arg1) throws JMSException
	{
		itsProperties.put(arg0, arg1);
	}

	public void setJMSCorrelationID(String arg0) throws JMSException
	{
		// TODO Auto-generated method stub

	}

	public void setJMSCorrelationIDAsBytes(byte[] arg0) throws JMSException
	{
		// TODO Auto-generated method stub

	}

	public void setJMSDeliveryMode(int arg0) throws JMSException
	{
		// TODO Auto-generated method stub

	}

	public void setJMSDestination(Destination arg0) throws JMSException
	{
		// TODO Auto-generated method stub

	}

	public void setJMSExpiration(long arg0) throws JMSException
	{
		// TODO Auto-generated method stub

	}

	public void setJMSMessageID(String arg0) throws JMSException
	{
		// TODO Auto-generated method stub

	}

	public void setJMSPriority(int arg0) throws JMSException
	{
		// TODO Auto-generated method stub

	}

	public void setJMSRedelivered(boolean arg0) throws JMSException
	{
		// TODO Auto-generated method stub

	}

	public void setJMSReplyTo(Destination arg0) throws JMSException
	{
		// TODO Auto-generated method stub

	}

	public void setJMSTimestamp(long arg0) throws JMSException
	{
		// TODO Auto-generated method stub

	}

	public void setJMSType(String arg0) throws JMSException
	{
		// TODO Auto-generated method stub

	}

	public void setLongProperty(String arg0, long arg1) throws JMSException
	{
		itsProperties.put(arg0, arg1);
	}

	public void setObjectProperty(String arg0, Object arg1) throws JMSException
	{
		itsProperties.put(arg0, arg1);
	}

	public void setShortProperty(String arg0, short arg1) throws JMSException
	{
		itsProperties.put(arg0, arg1);
	}

	public void setStringProperty(String arg0, String arg1) throws JMSException
	{
		itsProperties.put(arg0, arg1);
	}
}
