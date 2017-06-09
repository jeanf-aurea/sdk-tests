package jms.fake;

import javax.jms.JMSException;
import javax.jms.Queue;

public class QueueImpl implements Queue
{
	private final String itsName;

	public QueueImpl(final String name)
	{
		itsName = name;
	}

	public String getQueueName() throws JMSException
	{
		return itsName;
	}

	@Override
	public String toString()
	{
		return itsName;
	}
}
