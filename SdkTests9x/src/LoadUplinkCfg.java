import java.io.FileReader;

import com.actional.lg.interceptor.internal.config.UplinkCfg;

//=====================================================================================================================
// $HeadURL: http://uxmtlsvn1.americas.progress.com/repos/Actional/branches/act82x/product/src/builtinplugins/com/actional/plugin/msgfield/MsgFieldXpathEvaluator.java $
// Checked in by: $Author: jeanf $
// $Date: 2011-07-13 11:16:54 -0400 (Wed, 13 Jul 2011) $
// $Revision: 51358 $
//---------------------------------------------------------------------------------------------------------------------
// Copyright (c) 2011 Progress Software Corporation and/or its subsidiaries or affiliates. All rights reserved.
//=====================================================================================================================

/**
 * @author jeanf
 *
 */
public class LoadUplinkCfg
{
	/** <!-- ================================================================================================== -->
	 * @param args
	 *
	 * @lastrev fixXXXXX - New method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public static void main(String[] args)
		throws Exception
	{
		final UplinkCfg uplinkCfg = new UplinkCfg(UplinkCfg.LOOPBACK_UPLINK);

		uplinkCfg.load(new FileReader(args[0]));
	}
}
