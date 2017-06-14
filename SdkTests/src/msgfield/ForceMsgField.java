//=====================================================================================================================
// Copyright (c) 2017. Aurea Software, Inc. All Rights Reserved.
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

package msgfield;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.actional.lg.interceptor.internal.simulator.Simulator;
import com.actional.lg.interceptor.sdk.ServerInteraction;

/** <!-- ========================================================================================================== -->
 * Can be useful to test auditing
 *
 * @lastrev fixXXXXX - new class
 * <!-- -------------------------------------------------------------------------------------------------------- --> */

public class ForceMsgField extends Simulator
{
	private static final DateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");

	public static void main(final String[] args)
		throws Exception
	{
		new ForceMsgField().generate();

		Thread.sleep(1000L);

		System.out.println("Done.");
	}

	private ForceMsgField()
	{
		super("Force-Msg-Field");
	}

	protected void generate()
	{
		final String node1 = "node1";
		final String node2 = "node2";
		final String itsL2Name = "l2name";
		final String itsL3Name = "l3name";

		long time = System.currentTimeMillis();

		final ServerInteraction si = inbound("client", node1, itsL2Name, itsL3Name, null);

		si.setMsgField("field1", DATE_FORMATTER.format(new Date(++time)), false);
		si.setMsgField("field2", DATE_FORMATTER.format(new Date(++time)), false);
		si.setMsgField("field3", DATE_FORMATTER.format(new Date(++time)), false);

		si.requestAnalyzed();

		si.setMsgField("field1", DATE_FORMATTER.format(new Date(++time)), false);
		si.setMsgField("field2", DATE_FORMATTER.format(new Date(++time)), false);
		si.setMsgField("field3", DATE_FORMATTER.format(new Date(++time)), false);

		si.end();
	}
}
