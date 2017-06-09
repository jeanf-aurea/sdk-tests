//=====================================================================================================================
// $HeadURL: http://uxmtlsvn1.americas.progress.com/repos/Actional/branches/act82x/product/src/builtinplugins/com/actional/plugin/msgfield/MsgFieldXpathEvaluator.java $
// Checked in by: $Author: jeanf $
// $Date: 2011-07-13 11:16:54 -0400 (Wed, 13 Jul 2011) $
// $Revision: 51358 $
//---------------------------------------------------------------------------------------------------------------------
// Copyright (c) 2011 Progress Software Corporation and/or its subsidiaries or affiliates. All rights reserved.
//=====================================================================================================================


package bugs;

import util.ManualCorrelator;

import com.actional.lg.interceptor.sdk.ServerInteraction;

/**
 * @author jeanf
 *
 */
public class PSC00023247 extends util.Call
{
	static
	{
		ManualCorrelator.init();
	}

	PSC00023247()
	{
		super("PSC00023247");
	}

	/** <!-- ================================================================================================== -->
	 * @param args
	 *
	 * @lastrev fixXXXXX - New method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public static void main(String[] args) throws Exception
	{
		new PSC00023247().run();
	}

	@Override
	protected void doRun(int loop) throws Exception
	{
		ServerInteraction si1 = ServerInteraction.begin();

		si1.setSubnode("nbbedbchappel");
		si1.setUrl("sonic://NBBEDBCHAPPEL.bedford.progress.com/NodeA$$edbchappel50010/NodeB$$$Queue$CGQ0");
		si1.setSelfAddr("nbbedbchappel");
		si1.setPeerAddr("");
		si1.setSvcType((short)-128);
		si1.setOneWay(true);
	}
}
