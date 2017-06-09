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

import com.actional.GeneralUtil;
import com.actional.lg.interceptor.sdk.ClientInteraction;
import com.actional.lg.interceptor.sdk.DisplayType;
import com.actional.lg.interceptor.sdk.ServerInteraction;
import com.actional.lg.interceptor.sdk.helpers.InterHelpBase;

public class ACTA20867_3
{
	public static void main(String[] args)
		throws Exception
	{
		final long now = System.currentTimeMillis();

		final ClientInteraction ci = ClientInteraction.begin();

		ci.setPeerAddr("topic");
		ci.setPeerType(DisplayType.TOPIC);
		ci.setUrl("/");
		ci.setSelfAddr(GeneralUtil.DEMO_PREFIX + "client");
		ci.setBeginTime(now);
		ci.setElapsed(123);

		final String corrHeader = InterHelpBase.writeHeader(ci);

		ci.end();

		createSI(corrHeader, 1, now +  50, 500);
		createSI(corrHeader, 2, now + 100, 100);

		System.out.println(ci.getFlowID());
	}

	private static void createSI(String corrHeader, int index, long start, int elapsed)
	{
		ServerInteraction si = ServerInteraction.begin();

		si.setPeerAddr("topic");
		si.setPeerType(DisplayType.TOPIC);
		si.setUrl("/a/b/c");
		si.setSelfAddr(GeneralUtil.DEMO_PREFIX + "server" + index);
		si.setBeginTime(start);
		si.setElapsed(elapsed);

		InterHelpBase.readHeader(corrHeader, si);

		si.end();
	}
}
