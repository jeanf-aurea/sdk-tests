//=====================================================================================================================
// $HeadURL: http://uxmtlsvn1.americas.progress.com/repos/Actional/branches/act82x/product/src/builtinplugins/com/actional/plugin/msgfield/MsgFieldXpathEvaluator.java $
// Checked in by: $Author: jeanf $
// $Date: 2011-07-13 11:16:54 -0400 (Wed, 13 Jul 2011) $
// $Revision: 51358 $
//---------------------------------------------------------------------------------------------------------------------
// Copyright (c) 2012 Progress Software Corporation and/or its subsidiaries or affiliates. All rights reserved.
//=====================================================================================================================

import com.actional.lg.interceptor.internal.BinarySerializer;
import com.actional.lg.interceptor.internal.IAgentEventSource;
import com.actional.lg.interceptor.internal.IBaseEventSource;
import com.actional.lg.interceptor.internal.Uplink;
import com.actional.lg.interceptor.internal.config.UplinkCfg;
import com.actional.lg.interceptor.sdk.ClientInteraction;
import com.actional.lg.interceptor.sdk.ServerInteraction;
import com.actional.lg.interceptor.sdk.helpers.InterHelpBase;

/**
 *
 */
public class AgentEventSize
{
	static
	{
		System.setProperty("com.actional.lg.interceptor.sdk.Uplink", SizeUplink.class.getName());
	}

	public static void main(String[] args) throws Exception
	{
		final ServerInteraction si = ServerInteraction.begin();

		si.setUrl("/a/b/c");
		si.setPeerAddr("localhost");

		final ClientInteraction ci = ClientInteraction.begin();

		ci.setUrl("/x/y/z");
		ci.setPeerAddr("localhost");

		final String headers = InterHelpBase.writeHeader(ci);

		System.out.println(headers);
		System.out.println(headers.length());

		ci.end();

		si.end();
	}

	public static class SizeUplink implements Uplink
	{
		private final UplinkCfg SINGLETON = new UplinkCfg(UplinkCfg.LOOPBACK_UPLINK);

		public UplinkCfg getConfig()
		{
			return SINGLETON;
		}

		public boolean isAgentAlive()
		{
			return true;
		}

		public void notifyAgent(IBaseEventSource intr)
		{
			if (intr instanceof IAgentEventSource)
			{
				final BinarySerializer.SinglePayload ser = new BinarySerializer.SinglePayload(false);
				final byte[] bytes = ser.serialize((IAgentEventSource) intr, 0);

				System.out.println(intr.toString() + " - " + bytes.length + " bytes");
			}
		}

		public Object copyUplinkData(Object uplinkData)
		{
			// TODO Auto-generated method stub
			return null;
		}

		public void shutdown()
		{
		}
	}
}
