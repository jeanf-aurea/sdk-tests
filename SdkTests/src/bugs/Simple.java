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
import com.actional.lg.interceptor.sdk.ServerInteraction;

/** <!-- ========================================================================================================== -->
 *
 *
 * @lastrev fixXXXXX - new class
 * <!-- -------------------------------------------------------------------------------------------------------- --> */

public class Simple
{
	public static void main(String[] args)
		throws Exception
	{
		final ServerInteraction si = ServerInteraction.begin();

		si.setUrl("/a/b/c");
		si.setPeerAddr(GeneralUtil.DEMO_PREFIX + "client");

		for (int i = 1; i <= 5; i++)
		{
			final ClientInteraction ci = ClientInteraction.begin();

			ci.setUrl("/x/y/z/"+ i);
			ci.setPeerAddr(GeneralUtil.DEMO_PREFIX + "server");
			ci.setElapsed((i * 100) % 30);

			ci.end();
		}

		si.setElapsed(1100);

		si.end();
	}
}
