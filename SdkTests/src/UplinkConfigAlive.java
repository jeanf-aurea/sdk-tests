import com.actional.lg.interceptor.sdk.UplinkConfig;

//=====================================================================================================================
// $HeadURL: http://uxmtlsvn1.americas.progress.com/repos/Actional/branches/act82x/product/src/builtinplugins/com/actional/plugin/msgfield/MsgFieldXpathEvaluator.java $
// Checked in by: $Author: jeanf $
// $Date: 2011-07-13 11:16:54 -0400 (Wed, 13 Jul 2011) $
// $Revision: 51358 $
//---------------------------------------------------------------------------------------------------------------------
// Copyright (c) 2012 Progress Software Corporation and/or its subsidiaries or affiliates. All rights reserved.
//=====================================================================================================================

/**
 *
 */
public class UplinkConfigAlive
{
	public static void main(String[] args)
	{
		final boolean rtrn = UplinkConfig.getCurrent().getAlive();

		System.out.println(rtrn);
	}
}
