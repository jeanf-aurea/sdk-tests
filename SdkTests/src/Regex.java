//=====================================================================================================================
// $HeadURL: http://uxmtlsvn1.americas.progress.com/repos/Actional/branches/act82x/product/src/builtinplugins/com/actional/plugin/msgfield/MsgFieldXpathEvaluator.java $
// Checked in by: $Author: jeanf $
// $Date: 2011-07-13 11:16:54 -0400 (Wed, 13 Jul 2011) $
// $Revision: 51358 $
//---------------------------------------------------------------------------------------------------------------------
// Copyright (c) 2011 Progress Software Corporation and/or its subsidiaries or affiliates. All rights reserved.
//=====================================================================================================================

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jeanf
 *
 */
public class Regex
{
	public static void main(String[] args)
	{
		final Pattern pattern = Pattern.compile("(.*)Clou(.*)");
		final Matcher matcher = pattern.matcher("Jean-Francois Cloutier");
//		System.out.println(matcher.matches());

		if (matcher.find())
		{
			for (int i = 0, iLen = matcher.groupCount(); i <= iLen; i++)
			{
				System.out.println("[" + i + "] " + matcher.group(i));
			}
		}
	}
}
