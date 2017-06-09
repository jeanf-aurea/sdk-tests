//=====================================================================================================================
// $HeadURL: http://uxmtlsvn1.americas.progress.com/repos/Actional/branches/act82x/product/src/builtinplugins/com/actional/plugin/msgfield/MsgFieldXpathEvaluator.java $
// Checked in by: $Author: jeanf $
// $Date: 2011-07-13 11:16:54 -0400 (Wed, 13 Jul 2011) $
// $Revision: 51358 $
//---------------------------------------------------------------------------------------------------------------------
// Copyright (c) 2011 Progress Software Corporation and/or its subsidiaries or affiliates. All rights reserved.
//=====================================================================================================================

import java.io.FileInputStream;
import com.actional.lg.interceptor.internal.config.UplinkCfg;
import com.actional.lg.interceptor.internal.config.UplinkCfgParser;
import com.actional.lg.interceptor.internal.config.UplinkType;

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
		final UplinkCfg uplinkCfg = new UplinkCfgParser().parse(new FileInputStream(args[0]), UplinkType.LOOPBACK_UPLINK);
	}
}
