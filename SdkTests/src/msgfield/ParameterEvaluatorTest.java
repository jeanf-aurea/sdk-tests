//=====================================================================================================================
// $HeadURL: http://uxmtlsvn1.americas.progress.com/repos/Actional/branches/act82x/product/src/builtinplugins/com/actional/plugin/msgfield/MsgFieldXpathEvaluator.java $
// Checked in by: $Author: jeanf $
// $Date: 2011-07-13 11:16:54 -0400 (Wed, 13 Jul 2011) $
// $Revision: 51358 $
//---------------------------------------------------------------------------------------------------------------------
// Copyright (c) 2011 Progress Software Corporation and/or its subsidiaries or affiliates. All rights reserved.
//=====================================================================================================================


package msgfield;

import com.actional.lg.interceptor.sdk.IMsgFieldEvaluator;
import com.actional.lg.interceptor.sdk.ServerInteraction;

/**
 * @author jeanf
 *
 */
public class ParameterEvaluatorTest
{
	/** <!-- ================================================================================================== -->
	 * @param args
	 *
	 * com.actional.lg.interceptor.sdk.Uplink=com.actional.lg.interceptor.internal.AsyncBinaryUplink
	 *
	 * @lastrev fixXXXXX - New method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public static void main(String[] args)
		throws Exception
	{
		final ServerInteraction si = ServerInteraction.begin();

		si._addMsgFieldEvaluator(new FakeRequestMsgFieldEvaluator());

		si.setUrl("/a/b/c");
		si.setPeerAddr("localhost");
		si.requestAnalyzed();

		si._addMsgFieldEvaluator(new FakeReplyMsgFieldEvaluator());

		si.end();
	}

	private static final class FakeRequestMsgFieldEvaluator implements IMsgFieldEvaluator
	{
		public String eval(String msgFieldName, String context)
		{
			return "request(" + context + ")";
		}
	}

	private static final class FakeReplyMsgFieldEvaluator implements IMsgFieldEvaluator
	{
		public String eval(String msgFieldName, String context)
		{
			return "reply(" + context + ")";
		}
	}
}
