package jms.fake;

import javax.jms.JMSException;
import javax.jms.Topic;

public class TopicImpl implements Topic
{
	private final String itsName;

	public TopicImpl(final String name)
	{
		itsName = name;
	}

	public String getTopicName() throws JMSException
	{
		return itsName;
	}

	@Override
	public String toString()
	{
		return itsName;
	}
}
