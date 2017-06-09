//=====================================================================================================================
// $HeadURL:  $
// Checked in by: $Author: $
// $Date: $
// $Revision: $
//---------------------------------------------------------------------------------------------------------------------
// Copyright (c) 2014 Aurea Software and/or its subsidiaries or affiliates. All rights reserved.
//=====================================================================================================================

package bugs;

import com.actional.lg.interceptor.sdk.ClientInteraction;
import com.actional.lg.interceptor.sdk.DisplayType;
import com.actional.lg.interceptor.sdk.ServerInteraction;
import com.actional.lg.interceptor.sdk.helpers.InterHelpBase;

public class ACTA20137
{
	private static final String QUEUE_PATTERN = "msunde";

	public static void main(String[] args) throws Exception
	{
		flow1();
		flow2();
		flow3();
	}

	private static void flow1() throws Exception
	{
		final ServerInteraction si = ServerInteraction.begin();

		si.setPeerType(DisplayType.NO_PEER);
		si.setUrl("/flow1/service1");

		final ClientInteraction ci = ClientInteraction.begin();

		ci.setPeerType(DisplayType.QUEUE);
		ci.setPeerAddr("queue.flow1." + QUEUE_PATTERN);
		ci.setUrl("/"); // just to keep the SDK happy.

		final String corrHeader = InterHelpBase.writeHeader(ci);

		ci.end();

		si.end();

		final ServerInteraction si2 = ServerInteraction.begin();

		InterHelpBase.readHeader(corrHeader, si2);

		si2.setPeerType(DisplayType.QUEUE);
		si2.setPeerAddr("queue.flow1." + QUEUE_PATTERN);
		si2.setUrl("/flow1/service2");

		si2.end();
	}

	private static void flow2() throws Exception
	{
		final ServerInteraction si = ServerInteraction.begin();

		si.setPeerType(DisplayType.NO_PEER);
		si.setUrl("/flow2/service1");

		final ClientInteraction ci = ClientInteraction.begin();

		ci.setPeerType(DisplayType.QUEUE);
		ci.setPeerAddr("queue.flow2." + QUEUE_PATTERN);
		ci.setUrl("/"); // just to keep the SDK happy.

		ci.end();

		si.end();
	}

	private static void flow3() throws Exception
	{
		final ServerInteraction si = ServerInteraction.begin();

		si.setPeerType(DisplayType.QUEUE);
		si.setPeerAddr("queue.flow3." + QUEUE_PATTERN);
		si.setUrl("/flow3/service2");

		si.end();
	}
}
