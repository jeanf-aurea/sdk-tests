//=====================================================================================================================
// $HeadURL: http://uxmtlsvn1.americas.progress.com/repos/Actional/branches/act82x/product/src/builtinplugins/com/actional/plugin/msgfield/MsgFieldXpathEvaluator.java $
// Checked in by: $Author: jeanf $
// $Date: 2011-07-13 11:16:54 -0400 (Wed, 13 Jul 2011) $
// $Revision: 51358 $
//---------------------------------------------------------------------------------------------------------------------
// Copyright (c) 2012 Progress Software Corporation and/or its subsidiaries or affiliates. All rights reserved.
//=====================================================================================================================

import com.actional.sdk.Log;

public class TestLogRollover
{
	public static void main(String[] args) throws Exception
	{
		test();
	}

	private static void test() throws Exception
	{
		while (true)
		{
			for (int i = 0; i < 100; i++)
			{
				Log.ERROR.log("test a bc d e f g h j i k l m n o p q r s t u v w x y z");
			}

			Thread.sleep(500L);
		}
	}
}
