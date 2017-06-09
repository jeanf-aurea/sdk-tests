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

public class ACTA20867_1
{
	public static void main(String[] args)
	{
		try
		{
			String corrHeader = null;
			int i = 0;

			while (i < 10)
			{
				final ServerInteraction si = ServerInteraction.begin();

				if (corrHeader == null)
				{
					si.setPeerType(DisplayType.NO_PEER);
				}
				else
				{
					si.setPeerAddr(GeneralUtil.DEMO_PREFIX + "host_" + (i - 1));
					InterHelpBase.readHeader(corrHeader, si);
				}

				si.setUrl("/a/b/" + i);
				si.setSelfAddr(GeneralUtil.DEMO_PREFIX + "host_" + i);

				final ClientInteraction ci = ClientInteraction.begin();

				ci.setSelfAddr(GeneralUtil.DEMO_PREFIX + "host_" + i);

				i++;

				ci.setUrl("/a/b/" + i);
				ci.setPeerAddr(GeneralUtil.DEMO_PREFIX + "host_" + i);

				corrHeader = InterHelpBase.writeHeader(ci);

				ci.end();

				si.end();
			}

			System.out.println(corrHeader);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
