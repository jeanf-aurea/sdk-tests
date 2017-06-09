package jms.fake;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;


import com.actional.lg.interceptor.sdk.ServerInteraction;

public class ACT18689
{
	public static void main(String[] args)
	{
		try
		{
			ServerInteraction si = ServerInteraction.begin();

			si.setUrl("/a/b/c");
			si.setOpName("POST");
			si.setPeerAddr("mtl-msunde");
			si.requestAnalyzed();

			final Destination dest0 = new QueueImpl("funky.queue");

			new JMSConsumerImpl(new MessageImpl(dest0)).receive();

			if (si != ServerInteraction.get())
				System.out.println(">>>>>>>>>>>>>>>>> Not our SI");

			si.end();
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}
	}

	public static void main2(String[] args)
	{
		try
		{
			final Destination dest0 = new QueueImpl("funky.queue");

			// This will leave an SI 'dangling' in the TLS.
			// We want to see if the call to onMessage afterwards
			// will (rightfully) end() this dangling SI.
			new JMSConsumerImpl(new MessageImpl(dest0)).receive();

			final Destination dest1 = new QueueImpl("FMED.AMDOCS.OIS.REQ.A01");

			new Listener1().onMessage(new MessageImpl(dest1));
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}
	}
}

class Listener1 implements MessageListener
{
	public void onMessage(Message arg0)
	{
		try
		{
			final Destination dest2 = new QueueImpl("FMED.CCB.CO.ASYNC.REQ.A01");
			final Destination dest3 = new QueueImpl("$sys.lookup");
			final Destination dest4 = new TempQueueImpl("$TMP$.EMS-SERVER.14AC4B4C62E620.1");

			new JMSSenderImpl(dest3).send(new MessageImpl(dest3));

			new JMSConsumerImpl(new MessageImpl(dest4)).receive();

			new JMSSenderImpl().send(new MessageImpl(dest2));
		}
		catch (JMSException e)
		{
			e.printStackTrace();
		}
	}
}