//=====================================================================================================================
// $HeadURL: http://uxmtlsvn1.americas.progress.com/repos/Actional/branches/act82x/product/src/builtinplugins/com/actional/plugin/msgfield/MsgFieldXpathEvaluator.java $
// Checked in by: $Author: jeanf $
// $Date: 2011-07-13 11:16:54 -0400 (Wed, 13 Jul 2011) $
// $Revision: 51358 $
//---------------------------------------------------------------------------------------------------------------------
// Copyright (c) 2011 Progress Software Corporation and/or its subsidiaries or affiliates. All rights reserved.
//=====================================================================================================================

import java.io.InputStreamReader;
import java.io.LineNumberReader;

import com.actional.lg.interceptor.sdk.SettingsManager;

/**
 * @author jeanf
 *
 */
public class InterceptorSettings
{
	public static void main(String[] args)
		throws Exception
	{
		final LineNumberReader lnr = new LineNumberReader(new InputStreamReader(System.in));

		System.out.println("Enter the name of an SDK setting. Empty line to quit.");

		for (String line = lnr.readLine(); line != null ; line = lnr.readLine())
		{
			if (line.length() == 0)
				return;

			final String val = SettingsManager.getSettings().getString(line, null);

			if (val == null)
				System.out.println("There is no value.");
			else
				System.out.println("The value is \"" + val + "\".");
		}
	}
}
