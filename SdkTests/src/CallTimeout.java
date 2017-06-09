//=====================================================================================================================
// $HeadURL:  $
// Checked in by: $Author: $
// $Date: $
// $Revision: $
//---------------------------------------------------------------------------------------------------------------------
// Copyright (c) 2016. Aurea Software, Inc. All Rights Reserved.
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

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.UnsupportedEncodingException;

import com.actional.lg.interceptor.internal.simulator.Simulator;
import com.actional.lg.interceptor.sdk.ServerInteraction;

/** <!-- ========================================================================================================== -->
 *
 *
 * @lastrev fixXXXXX - new class
 * <!-- -------------------------------------------------------------------------------------------------------- --> */

public class CallTimeout extends Simulator
{
	private final LineNumberReader CONSOLE;

	private CallTimeout() throws UnsupportedEncodingException
	{
		super("CallTimeout");

		CONSOLE = new LineNumberReader(new InputStreamReader(System.in, "UTF-8"));
	}

	public static void main(String[] args)
		throws Exception
	{
		new CallTimeout().generate();

		Thread.sleep(500L);
	}

	protected void generate()
		throws IOException
	{
		while (true)
		{
			final ServerInteraction si = inbound("client", "server", "app", "service", "op");

			si.requestAnalyzed();

			System.out.println("Press <ENTER> to complete " + si.getInteractionID());
			CONSOLE.readLine();

			si.end();
		}
	}
}
