//=====================================================================================================================
// $HeadURL: http://uxmtlsvn1.americas.progress.com/repos/Actional/branches/act82x/product/src/builtinplugins/com/actional/plugin/msgfield/MsgFieldXpathEvaluator.java $
// Checked in by: $Author: jeanf $
// $Date: 2011-07-13 11:16:54 -0400 (Wed, 13 Jul 2011) $
// $Revision: 51358 $
//---------------------------------------------------------------------------------------------------------------------
// Copyright (c) 2011 Progress Software Corporation and/or its subsidiaries or affiliates. All rights reserved.
//=====================================================================================================================


package consumerside;

import com.actional.lg.interceptor.sdk.DisplayType;
import com.actional.lg.interceptor.sdk.ServerInteraction;

/**
 * @author jeanf
 *
 */
public class Test6MixedSites
{
	public static void main(String[] args)
		throws Exception
	{
		for (int i = 1; i < 6; i++)
			generate("managed_op" + i);

		for (int i = 1; i < 6; i++)
			generate("unmanaged_op" + i);
	}

	public static void generate(String op)
		throws Exception
	{
		ServerInteraction si = ServerInteraction.begin();

		si.setUrl("/a/b");
		si.setPeerType(DisplayType.NO_PEER);
		si.setOpName(op);
		si.end();
	}
}
