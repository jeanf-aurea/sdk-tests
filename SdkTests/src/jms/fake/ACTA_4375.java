//=====================================================================================================================
// $HeadURL: http://uxmtlsvn1.americas.progress.com/repos/Actional/branches/act82x/product/src/builtinplugins/com/actional/plugin/msgfield/MsgFieldXpathEvaluator.java $
// Checked in by: $Author: jeanf $
// $Date: 2011-07-13 11:16:54 -0400 (Wed, 13 Jul 2011) $
// $Revision: 51358 $
//---------------------------------------------------------------------------------------------------------------------
// Copyright (c) 2012 Progress Software Corporation and/or its subsidiaries or affiliates. All rights reserved.
//=====================================================================================================================

package jms.fake;

import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageListener;

import com.actional.lg.interceptor.sdk.ILogEntry;
import com.actional.lg.interceptor.sdk.LogEntryFactory;
import com.actional.lg.interceptor.sdk.LogLevel;

public class ACTA_4375
{
	public static void main(String[] args)
	{
		final Destination dest1 = new QueueImpl("FMED.AMDOCS.OIS.REQ.A01");

		new Listener1(60).onMessage(new MessageImpl(dest1));

		new Listener2(49).onMessage(new MessageImpl(dest1));

		new Listener3(66).onMessage(new MessageImpl(dest1));
	}

	private static class ListenerBase
	{
		private final int itsLoopCount;

		ListenerBase(int loop)
		{
			itsLoopCount = loop;
		}

		public void onMessage(Message msg)
		{
			for (int i = 0, iLen = itsLoopCount; i < iLen; i++)
			{
				final ILogEntry log = LogEntryFactory.beginLogEntry();

				if (log != null)
				{
					log.setLogLevel(LogLevel.INFO);
					log.setLogClassName(this.getClass().getName());
					log.setLogMessage("Message#" + i);

					log.end();
				}
			}
		}
	}

	private static final class Listener1 extends ListenerBase implements MessageListener
	{
		Listener1(int loop)
		{
			super(loop);
		}
	}

	private static final class Listener2 extends ListenerBase implements MessageListener
	{
		Listener2(int loop)
		{
			super(loop);
		}
	}

	private static final class Listener3 extends ListenerBase implements MessageListener
	{
		Listener3(int loop)
		{
			super(loop);
		}
	}
}
