//=====================================================================================================================
// $HeadURL:  $
// Checked in by: $Author: $
// $Date: $
// $Revision: $
//---------------------------------------------------------------------------------------------------------------------
// Copyright (c) 2015. Aurea Software, Inc. All Rights Reserved.
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

import java.io.Console;
import java.io.UnsupportedEncodingException;

import com.actional.lg.interceptor.sdk.ClientInteraction;
import com.actional.lg.interceptor.sdk.ServerInteraction;

/** <!-- ========================================================================================================== -->
 * @lastrev fixXXXXX - new class
 * <!-- -------------------------------------------------------------------------------------------------------- --> */

public class ACTA22692
{
	public static void main(String[] args)
		throws UnsupportedEncodingException
	{
		final Console console = System.console();

		final byte[] REQUEST = "<request>This is some funky test that you can try to mask with a filter.</request>".getBytes("UTF-8");
		final byte[] REPLY = "<reply>This is some funky test that you can try to mask with a filter.</reply>".getBytes("UTF-8");

		while (true)
		{
			System.out.println("Press <ENTER> to send traffic.");

			console.readLine();

			final ServerInteraction si = ServerInteraction.begin();

			si.setPeerAddr("www.microsoft.com");
			si.setUrl("/service1");
			si.setGroupName("My Application");
			si.setServiceName("My Service");
			si.setOpName("My Operation");
			si.setPayload(REQUEST);

			final ClientInteraction ci = ClientInteraction.begin();

			ci.setPeerAddr("www.google.com");
			ci.setUrl("/service2");
			ci.setGroupName("My Backend Application");
			ci.setServiceName("My Backend Service");
			ci.setOpName("My Backend Operation");
			ci.setPayload(REQUEST);
			ci.requestAnalyzed();

			ci.setElapsed(67);
			ci.setPayload(REPLY);

			ci.end();

			si.setElapsed(151);
			si.setPayload(REPLY);

			si.end();
		}
	}
}