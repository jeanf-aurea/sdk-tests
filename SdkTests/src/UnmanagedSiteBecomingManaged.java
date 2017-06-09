//=====================================================================================================================
// $HeadURL: http://uxmtlsvn1.americas.progress.com/repos/Actional/branches/act82x/product/src/builtinplugins/com/actional/plugin/msgfield/MsgFieldXpathEvaluator.java $
// Checked in by: $Author: jeanf $
// $Date: 2011-07-13 11:16:54 -0400 (Wed, 13 Jul 2011) $
// $Revision: 51358 $
//---------------------------------------------------------------------------------------------------------------------
// Copyright (c) 2011 Progress Software Corporation and/or its subsidiaries or affiliates. All rights reserved.
//=====================================================================================================================

import com.actional.lg.interceptor.sdk.ClientInteraction;
import com.actional.lg.interceptor.sdk.ServerInteraction;
import com.actional.lg.interceptor.sdk.helpers.InterHelpBase;

/**
 * @author jeanf
 *
 */
public class UnmanagedSiteBecomingManaged
{
	public static void main(String[] args) throws Exception
	{
		final ServerInteraction si1 = ServerInteraction.begin();

		si1.setUrl("/a/b/c");
		si1.setPeerAddr("localhost");

		final ClientInteraction ci1 = ClientInteraction.begin();

		ci1.setUrl("/x/y/z");
		ci1.setOpName("op1");
		ci1.setPeerAddr("localhost");

		final String writeHeaders = InterHelpBase.writeHeader(ci1);

		ci1.end();

		si1.end();

		if (args.length > 0)
		{
			final ServerInteraction si2 = ServerInteraction.begin();

			InterHelpBase.readHeader(writeHeaders, si2);

			si2.setUrl("/x/y/z");
			si2.setOpName("op1");
			si2.setOpID("abcdefghijklmnop");
			si2.setPeerAddr("localhost");

			si2.end();
		}
	}
}
