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

package sequencemap;

import com.actional.lg.interceptor.internal.simulator.Simulator;
import com.actional.lg.interceptor.sdk.ClientInteraction;
import com.actional.lg.interceptor.sdk.ServerInteraction;

/** <!-- ========================================================================================================== -->
 *
 *
 * @lastrev fixXXXXX - new class
 * <!-- -------------------------------------------------------------------------------------------------------- --> */

public class Usecases
{
	public static void main(final String[] args)
	{
		try
		{
//			new UC1().execute();
//			Thread.sleep(15000L);
//			new UC2().execute();
//			Thread.sleep(15000L);
//			new UC3().execute();
//			Thread.sleep(15000L);
//			new UC4().execute();
//			Thread.sleep(15000L);
//			new UC5().execute();
//			Thread.sleep(15000L);
//			new UC6().execute();
//			Thread.sleep(15000L);
//			new UC7().execute();
//			Thread.sleep(15000L);
			new UC8().execute();

			Thread.sleep(1000L);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static abstract class Base extends Simulator
	{
		Base(final String sandboxName)
		{
			super(sandboxName);
		}

		protected void jms_consumer(
				final ClientInteraction ci,
				final String suffix,
				final long delta,
				final int elapsed)
		{
			final ServerInteraction si2 = fromFakeQueue(ci, "node" + suffix, "app", "service", "op");

			si2.setBeginTime(ci.getBeginTime() + delta);
			si2.setElapsed(elapsed);

			si2.end();
		}

		protected void jms_consumer(
				final ClientInteraction ci,
				final String suffix,
				final String fromDest,
				final long delta,
				final int elapsed)
		{
			final ServerInteraction si2 = fromFakeQueue(ci, fromDest, "node" + suffix, "app", "service", "op");

			si2.setBeginTime(ci.getBeginTime() + delta);
			si2.setElapsed(elapsed);

			si2.end();
		}

		protected void request_response(
				final ServerInteraction si,
				final String suffix,
				final long deltaCI,
				final int elapsedCI,
				final long deltaSI,
				final int elapsedSI)
		{
			final ClientInteraction ci2 = outbound(si, "node" + suffix, "app", "service", "op");


			ci2.setBeginTime(si.getBeginTime() + deltaCI);
			ci2.setElapsed(elapsedCI);

			ci2.end();

			final ServerInteraction si2 = inbound(ci2);

			si2.setBeginTime(ci2.getBeginTime() + deltaSI);
			si2.setElapsed(elapsedSI);

			si2.end();
		}
	}

	private static final class UC1 extends Base
	{
		UC1() { super("uc1"); }

		protected void execute() throws Exception
		{
			final ServerInteraction si = inbound("client", "node1", "app1", "service1", "alarm");

			si.setElapsed(25);

			si.end();

			System.out.println(si.getFlowID());
		}
	}

	private static final class UC2 extends Base
	{
		UC2() { super("uc2"); }

		protected void execute() throws Exception
		{
			final ServerInteraction si = inbound("client", "node1", "app1", "service1", "alarm");

			si.setElapsed(25);

			si.end();

			System.out.println(si.getFlowID());

			request_response(si, "2", 4, 14, 2, 9);
		}
	}

	private static final class UC3 extends Base
	{
		UC3() { super("uc3"); }

		protected void execute() throws Exception
		{
			final ServerInteraction si = inbound("client", "node1", "app1", "service1", "alarm");

			si.setElapsed(7000);

			si.end();

			System.out.println(si.getFlowID());

			request_response(si, "2", 1200, 2000, 400, 1200);
			request_response(si, "3", 4000, 3000, 1000, 900);
		}
	}

	private static final class UC4 extends Base
	{
		UC4() { super("uc4"); }

		protected void execute() throws Exception
		{
			final ServerInteraction si = inbound("client", "node1", "app1", "service1", "alarm");

			si.setElapsed(800);

			si.end();

			System.out.println(si.getFlowID());

			request_response(si, "2", 1200, 2000, 400, 1200);
			request_response(si, "3", 4000, 3000, 1000, 900);
		}
	}

	/**
	 * Queue scenario
	 */
	private static final class UC5 extends Base
	{
		UC5() { super("uc5"); }

		protected void execute() throws Exception
		{
			final ServerInteraction si = inbound("client", "node1", "app1", "service1", "alarm");

			si.setElapsed(800);

			si.end();

			System.out.println(si.getFlowID());

			final ClientInteraction ci = toFakeQueue(si, "queue1", si.getBeginTime() + 300, 33);

			jms_consumer(ci, "2", 400, 1100);
		}
	}

	/**
	 * Topic scenario
	 */
	private static final class UC6 extends Base
	{
		UC6() { super("uc6"); }

		protected void execute() throws Exception
		{
			final ServerInteraction si = inbound("client", "node1", "app1", "service1", "alarm");

			si.setElapsed(800);

			si.end();

			System.out.println(si.getFlowID());

			final ClientInteraction ci = toFakeQueue(si, "topic1", si.getBeginTime() + 300, 33);

			jms_consumer(ci, "2", 400, 1100);
			jms_consumer(ci, "3", 300, 600);
		}
	}

	/**
	 * Producer queue different than consumer queue
	 */
	private static final class UC7 extends Base
	{
		UC7() { super("uc7"); }

		protected void execute() throws Exception
		{
			final ServerInteraction si = inbound("client", "node1", "app1", "service1", "alarm");

			si.setElapsed(800);

			si.end();

			System.out.println(si.getFlowID());

			final ClientInteraction ci = toFakeQueue(si, "queue1", si.getBeginTime() + 300, 33);

			jms_consumer(ci, "2", "queue2", 400, 1100);
		}
	}

	/**
	 * Like UC2 but missing SI reply
	 */
	private static final class UC8 extends Base
	{
		UC8() { super("uc8"); }

		protected void execute() throws Exception
		{
			final ServerInteraction si = inbound("client", "node1", "app1", "service1", "alarm");

			si.setElapsed(25);
			si.requestAnalyzed();
			si.setInternal(true);
			// si.end();

			System.out.println(si.getFlowID());

			request_response(si, "2", 4, 14, 2, 9);
		}
	}
}
