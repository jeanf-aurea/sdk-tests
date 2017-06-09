package jms.fake;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;

public class JMSSenderImpl implements javax.jms.MessageProducer
{
	private Destination itsDest;

	public JMSSenderImpl()
	{
	}

	public JMSSenderImpl(Destination dest)
	{
		itsDest = dest;
	}

	public void close() throws JMSException
	{
		// TODO Auto-generated method stub

	}

	public int getDeliveryMode() throws JMSException
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public Destination getDestination() throws JMSException
	{
		return itsDest;
	}

	public boolean getDisableMessageID() throws JMSException
	{
		// TODO Auto-generated method stub
		return false;
	}

	public boolean getDisableMessageTimestamp() throws JMSException
	{
		// TODO Auto-generated method stub
		return false;
	}

	public int getPriority() throws JMSException
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public long getTimeToLive() throws JMSException
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public void send(Destination arg0, Message arg1, int arg2, int arg3, long arg4) throws JMSException
	{
		// TODO Auto-generated method stub

	}

	public void send(Destination arg0, Message arg1) throws JMSException
	{
		// TODO Auto-generated method stub

	}

	public void send(Message arg0, int arg1, int arg2, long arg3) throws JMSException
	{
		// TODO Auto-generated method stub

	}

	public void send(Message arg0) throws JMSException
	{
		// TODO Auto-generated method stub

	}

	public void setDeliveryMode(int arg0) throws JMSException
	{
		// TODO Auto-generated method stub

	}

	public void setDisableMessageID(boolean arg0) throws JMSException
	{
		// TODO Auto-generated method stub

	}

	public void setDisableMessageTimestamp(boolean arg0) throws JMSException
	{
		// TODO Auto-generated method stub

	}

	public void setPriority(int arg0) throws JMSException
	{
		// TODO Auto-generated method stub

	}

	public void setTimeToLive(long arg0) throws JMSException
	{
		// TODO Auto-generated method stub

	}

}
