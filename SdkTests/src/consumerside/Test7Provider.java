//=====================================================================================================================
// $HeadURL: http://uxmtlsvn1.americas.progress.com/repos/Actional/branches/act82x/product/src/builtinplugins/com/actional/plugin/msgfield/MsgFieldXpathEvaluator.java $
// Checked in by: $Author: jeanf $
// $Date: 2011-07-13 11:16:54 -0400 (Wed, 13 Jul 2011) $
// $Revision: 51358 $
//---------------------------------------------------------------------------------------------------------------------
// Copyright (c) 2011 Progress Software Corporation and/or its subsidiaries or affiliates. All rights reserved.
//=====================================================================================================================


package consumerside;

import com.actional.lg.interceptor.sdk.ServerInteraction;

/**
 * @author jeanf
 *
 */
public class Test7Provider
{
	public static void main(String[] args)
		throws Exception
	{
		final String peerAddr = args.length == 0 ? "127.0.0.1" : args[0];

		for (int i = 1; i < 6; i++)
			Test7Consumer.generate(ServerInteraction.begin(), peerAddr, "managed_op_" + i);
	}
}
