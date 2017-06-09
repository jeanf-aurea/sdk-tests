//=====================================================================================================================
// $HeadURL: http://uxmtlsvn1.americas.progress.com/repos/Actional/branches/act82x/product/src/builtinplugins/com/actional/plugin/msgfield/MsgFieldXpathEvaluator.java $
// Checked in by: $Author: jeanf $
// $Date: 2011-07-13 11:16:54 -0400 (Wed, 13 Jul 2011) $
// $Revision: 51358 $
//---------------------------------------------------------------------------------------------------------------------
// Copyright (c) 2011 Progress Software Corporation and/or its subsidiaries or affiliates. All rights reserved.
//=====================================================================================================================
package simple;

import com.actional.lg.interceptor.sdk.DisplayType;
import com.actional.lg.interceptor.sdk.ServerInteraction;

public class TimeoutInteraction
{
	public static void main(String[] args)
		throws Exception
	{
		final byte[] bin1 = { 4,8,3,5,9 };
		final byte[] xml1 = "<xml1/>".getBytes("UTF-8");
		final byte[] xml2 = "<xml2/>".getBytes("UTF-8");

		for (int i = 0; i < 1; i++)
		{
			ServerInteraction si;

			si = ServerInteraction.begin();
			si.setPeerType(DisplayType.NO_PEER);
			si.setUrl("/a/b/c");
			si.setOpName("1");
			si.setPayload(xml1);
			si.requestAnalyzed();
			si.setInternal(true);
			si.end();

			si = ServerInteraction.begin();
			si.setPeerType(DisplayType.NO_PEER);
			si.setUrl("/a/b/c");
			si.setOpName("2");
			si.setPayload(bin1);
			si.requestAnalyzed();
			si.setInternal(true);
			si.end();

			si = ServerInteraction.begin();
			si.setPeerType(DisplayType.NO_PEER);
			si.setUrl("/a/b/c");
			si.setOpName("3");
			si.setPayload(xml2);
			si.requestAnalyzed();
			si.end();
		}
	}
}
