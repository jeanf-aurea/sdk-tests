//=====================================================================================================================
// $HeadURL: http://uxmtlsvn1.americas.progress.com/repos/Actional/branches/act82x/product/src/builtinplugins/com/actional/plugin/msgfield/MsgFieldXpathEvaluator.java $
// Checked in by: $Author: jeanf $
// $Date: 2011-07-13 11:16:54 -0400 (Wed, 13 Jul 2011) $
// $Revision: 51358 $
//---------------------------------------------------------------------------------------------------------------------
// Copyright (c) 2012 Progress Software Corporation and/or its subsidiaries or affiliates. All rights reserved.
//=====================================================================================================================

import com.actional.lg.interceptor.internal.IBaseEventSource;
import com.actional.lg.interceptor.internal.Uplink;
import com.actional.lg.interceptor.internal.config.UplinkCfg;
import com.actional.lg.interceptor.sdk.Interaction;
import com.actional.lg.interceptor.sdk.SettingsManager;
import com.actional.lg.interceptor.sdk.UplinkConfig;

/**
 * ACTA-5299
 */
public class SettingsManagerImplNPE
{
	public static void main(String[] args)
		throws Exception
	{
//		test1();
		test2();
//		test3();
//		test4();
	}

	private static void test1()
		throws Exception
	{
		UplinkConfig.getCurrent().getAlive();
	}

	private static void test2()
		throws Exception
	{
		final FakeUplink fake = new FakeUplink();

		Interaction.setUplink(fake);
	}

	private static void test3()
		throws Exception
	{
		SettingsManager.getSettings();
	}

	private static void test4()
		throws Exception
	{
		UplinkConfig.assertsTrust();
	}

	private static class FakeUplink implements Uplink
	{
		public UplinkCfg getConfig()
		{
			return null;
		}

		public boolean isAgentAlive()
		{
			return false;
		}

		public void notifyAgent(IBaseEventSource intr)
		{
		}

		public Object copyUplinkData(Object uplinkData)
		{
			return null;

		}

		public void shutdown()
		{
		}
	}
}
