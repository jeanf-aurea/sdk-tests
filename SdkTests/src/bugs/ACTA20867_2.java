//=====================================================================================================================
// $HeadURL:  $
// Checked in by: $Author: $
// $Date: $
// $Revision: $
//---------------------------------------------------------------------------------------------------------------------
// Copyright (c) 2014. Aurea Software, Inc. All Rights Reserved.
//
// You are hereby placed on notice that the software, its related technology and services may be covered by one or
// more United States ("US") and non-US patents. A listing that associates patented and patent-pending products
// included in the software, software updates, their related technology and services with one or more patent numbers
// is available for you and the general public's access at www.aurea.com/legal/ (the "Patent Notice") without charge.
// The association of products-to-patent numbers at the Patent Notice may not be an exclusive listing of associations,
// and other unlisted patents or pending patents may also be associated with the products. Likewise, the patents or
// pending patents may also be associated with unlisted products. You agree to regularly review the products-to-patent
// number(s) association at the Patent Notice to check for updates.
//=====================================================================================================================

package bugs;

import com.actional.lg.interceptor.sdk.ClientInteraction;
import com.actional.lg.interceptor.sdk.DisplayType;
import com.actional.lg.interceptor.sdk.ServerInteraction;
import com.actional.lg.interceptor.sdk.helpers.InterHelpBase;

public class ACTA20867_2
{
	private static final String
		machine1 = "192.168.1.170",
		machine2 = "192.168.1.180",
		machine3 = "192.168.1.190",
		machine4 = "192.168.1.200";

	private static boolean upstream_reports_special_peer = true;

	public static void main(String[] args)
	{
		try
		{
			final long now = System.currentTimeMillis();

			final ServerInteraction si = ServerInteraction.begin();

			si.setPeerAddr(machine1);

			si.setUrl("/a/b/");
			si.setSelfAddr(machine2);
			si.setBeginTime(now);
			si.setElapsed(123);

			final ClientInteraction ci = ClientInteraction.begin();

			ci.setSelfAddr(machine2);

			ci.setUrl("/a/b/");
			ci.setPeerAddr(machine3);
			ci.setBeginTime(now + 21L);
			ci.setElapsed(456);

			if (upstream_reports_special_peer)
				ci.setPeerType(DisplayType.QUEUE);

			final String corrHeader = InterHelpBase.writeHeader(ci);

			ci.end();

			si.end();

			final ServerInteraction si1 = produceSI(corrHeader);
			final ServerInteraction si2 = produceSI(corrHeader);

			si1.setBeginTime(now + 100L);
			si2.setBeginTime(now + 200L);

			si1.setElapsed(400);
			si2.setElapsed(200);

			si1.requestAnalyzed();
			si2.requestAnalyzed();

			si1.end();
			si2.end();

			System.out.println(si.getFlowID());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private static ServerInteraction produceSI(String corrHeader)
	{
		final ServerInteraction si = ServerInteraction.begin();

		InterHelpBase.readHeader(corrHeader, si);

		if (!upstream_reports_special_peer)
			si.setPeerType(DisplayType.QUEUE);

		si.setPeerAddr(machine3);

		si.setUrl("/a/b/");
		si.setSelfAddr(machine4);

		return si;
	}
}
