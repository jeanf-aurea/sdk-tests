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

import util.ManualCorrelator;

import com.actional.lg.interceptor.sdk.ClientInteraction;
import com.actional.lg.interceptor.sdk.ServerInteraction;
import com.actional.lg.interceptor.sdk.helpers.InterHelpBase;

/** <!-- ========================================================================================================== -->
 *
 *
 * @lastrev fixXXXXX - new class
 * <!-- -------------------------------------------------------------------------------------------------------- --> */

public class ACTA21137_1 extends util.Call
{
	public static void main(String[] args)
	{
		try
		{
			ManualCorrelator.init();

			new ACTA21137_1("test1").test1();

			new ACTA21137_1("test2").test2();
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	private ACTA21137_1(String test)
	{
		super(test);
	}

	private void test1() throws Exception
	{
		String corrHeader;

		{
			ServerInteraction si = inbound("A", "B", "app_B", "service_B_1", null);

			ClientInteraction ci = outbound(si, "C", "app_C", "service_C", null);

			corrHeader = InterHelpBase.writeHeader(ci);

			ci.end();

			si.end();
		}

		{
			ServerInteraction si = this.inbound("D", "E", "app_E", "service_E", null);

			InterHelpBase.readHeader(corrHeader, si);

			si.end();

			System.out.println(itsPrefix + " = " + si.getFlowID());
		}
	}

	private void test2() throws Exception
	{
		String corrHeader;

		{
			ServerInteraction si = inbound("A", "B", "app_B", "service_B_1", null);

			ClientInteraction ci = outbound(si, "C", "app_C", "service_C", null);

			corrHeader = InterHelpBase.writeHeader(ci);

			ci.end();

			si.end();
		}

		{
			ServerInteraction si = inbound("D", "B", "app_B", "service_B_2", null);

			InterHelpBase.readHeader(corrHeader, si);

			si.end();

			System.out.println(itsPrefix + " = " + si.getFlowID());
		}
	}
}
