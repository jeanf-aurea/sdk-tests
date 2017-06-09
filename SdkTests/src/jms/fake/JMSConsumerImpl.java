package jms.fake;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;

public class JMSConsumerImpl implements MessageConsumer
{
	private final Message itsMessage;

	public JMSConsumerImpl(final Message msg)
	{
		itsMessage = msg;
	}

	public void close() throws JMSException
	{
		// TODO Auto-generated method stub

	}

	public MessageListener getMessageListener() throws JMSException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String getMessageSelector() throws JMSException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Message receive() throws JMSException
	{
		return itsMessage;
	}

	public Message receive(long arg0) throws JMSException
	{
		return itsMessage;
	}

	public Message receiveNoWait() throws JMSException
	{
		return itsMessage;
	}

	public void setMessageListener(MessageListener arg0) throws JMSException
	{
		// TODO Auto-generated method stub

	}
}
