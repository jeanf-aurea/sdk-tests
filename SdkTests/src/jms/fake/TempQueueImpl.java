package jms.fake;

import javax.jms.JMSException;

public class TempQueueImpl extends QueueImpl implements javax.jms.TemporaryQueue
{
	public TempQueueImpl(final String name)
	{
		super(name);
	}

	public void delete() throws JMSException
	{
	}
}
