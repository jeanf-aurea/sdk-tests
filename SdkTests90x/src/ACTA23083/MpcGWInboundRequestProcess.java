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

package ACTA23083;

import java.util.Random;

import util.ManualCorrelator;

import com.actional.GeneralUtil;
import com.actional.lg.interceptor.sdk.ClientInteraction;
import com.actional.lg.interceptor.sdk.DisplayType;
import com.actional.lg.interceptor.sdk.Interaction;
import com.actional.lg.interceptor.sdk.ServerInteraction;
import com.actional.lg.interceptor.sdk.helpers.InterHelpBase;

/** <!-- ========================================================================================================== -->
 * @lastrev fixXXXXX - new class
 * <!-- -------------------------------------------------------------------------------------------------------- --> */

public class MpcGWInboundRequestProcess
{
	private static final long DEST_NB = computeDestNb();

	public static void main(final String[] args)
		throws Exception
	{
		ManualCorrelator.init();

		final Random rand = new Random();

		final String OZOD6002 = GeneralUtil.DEMO_PREFIX + "ozod6002";
		final String OZOD6003 = GeneralUtil.DEMO_PREFIX + "ozod6003";

//		while (true)
		{
//			invalidPeerAddr(OZOD6003, OZOD6002);
			invalidPeerAddr(OZOD6002, OZOD6003);
//			invalidPeerAddr(OZOD6002, OZOD6002);
//			invalidPeerAddr(OZOD6003, OZOD6003);

			final long sleep = 500L + (rand.nextInt(15) * 500L);

			System.out.println("Sleeping " + sleep + " milliseconds.");

			Thread.sleep(sleep);
		}

		Thread.sleep(2000L); // Give time to queues to flush their events.

		Interaction.getUplink().shutdown();
	}

	private static long computeDestNb()
	{
		final Long destNb = Long.getLong("destNb");

		if (destNb == null)
			return System.nanoTime() % 100L;
		else
			return destNb.longValue();
	}

	private static void invalidPeerAddr(final String from, final String to)
		throws Exception
	{
		String corrHeader;

		{
			final ServerInteraction si = ServerInteraction.begin();

			si.setSubnode("esb_d1.ct_esb_gw_orderexec-des-conf-acti_03_ozod6003.esb_gw_mcom");
			si.setGroupName("gw.mcom.Dispatch");
			si.setServiceName("mcomGWOutboundProcess");
			si.setOpName("mcomGWoutOnline.StartProcess:mcomGWout.Dispatch2DynamicEP");
			si.setUrl("/gw.mcom.Dispatch/mcomGWOutboundProcess");
			si.setSelfAddr(to);
			si.setPeerType(DisplayType.NO_PEER);
//			si.setPeerAddr(from);
			si.setAppType(DisplayType.ESB_SERVICE);
			si.setSvcType(DisplayType.PROCESS);
			si.setOpType(DisplayType.STEP);
			si.setOneWay(true);
			si.setMsgField("ESB-Service", "gw.mcom.Dispatch", true);
			si.setMsgField("ESB-Top-Process", "mcomGWOnlineThrottleProcess", true);
			si.setMsgField("ESB-Container", "esb_gw_mcom", true);
			si.setMsgField("ESB-Process", "mcomGWOutboundProcess", true);

			final ClientInteraction ci = ClientInteraction.begin(si);

			ci.setSubnode("esb_d1.ct_esb_gw_orderexec-des-conf-acti_03_ozod6003.esb_gw_mcom");
			ci.setGroupName("JMS-Destination");
			ci.setUrl("/JMS-Destination/gw-mpc-request-int-q");
			ci.setSelfAddr(to);
			ci.setPeerAddr(to);
			ci.setAppType(DisplayType.APP);
			ci.setSvcType(DisplayType.ACTIVE_DOCUMENT);
			ci.setOneWay(true);
			ci.requestAnalyzed();

			ci.setElapsed(10);
			ci.end();

			si.setElapsed(12);
			si.end();

			corrHeader = InterHelpBase.writeHeader(ci);
		}

		{
			final ServerInteraction si = ServerInteraction.begin();

			InterHelpBase.readHeader(corrHeader, si);

			si.setSubnode("esb_d1.ct_esb_gw_prod-srvc-res-workforce_01_ozod6003.esb_gw_mpc");
			si.setGroupName("mpcGWInboundRequestProcess");
			si.setServiceName("ProcessEntry");
			si.setUrl("/mpcGWInboundRequestProcess/ProcessEntry");
			si.setSelfAddr(from);
			si.setPeerAddr(to);
			si.setCalleeAddress(to);
			si.setCallerAddress(to);
			si.setAppType(DisplayType.ESB_SERVICE);
			si.setSvcType(DisplayType.STANDALONE);
			si.setOneWay(true);
			si.setMsgField("ESB-Service", "mpcGWInboundRequestProcess", true);
			si.setMsgField("ESB-Container", "esb_gw_mpc", true);

			final ClientInteraction ci = ClientInteraction.begin(si);

			ci.setElapsed(1);
			ci.setSubnode("esb_d1.ct_esb_gw_prod-srvc-res-workforce_01_ozod6003.esb_gw_mpc");
			ci.setGroupName("gw.mpc.Transform");
			ci.setServiceName("mpcGWInboundRequestProcess");
			ci.setOpName("mpcGWinReq.TransformAndPrepDynEP");
			ci.setUrl("/gw.mpc.Transform/mpcGWInboundRequestProcess");
			ci.setSelfAddr(from);
			ci.setPeerAddr(from);
			ci.setAppType(DisplayType.ESB_SERVICE);
			ci.setSvcType(DisplayType.PROCESS);
			ci.setOpType(DisplayType.STEP);
			ci.setOneWay(true);
			ci.setMsgField("ESB-Top-Process", "mpcGWInboundRequestProcess", true);
			ci.setMsgField("ESB-Container", "esb_gw_mpc", true);
			ci.setMsgField("ESB-Process", "mpcGWInboundRequestProcess", true);

			ci.end();

			si.setElapsed(11);
			si.end();

			corrHeader = InterHelpBase.writeHeader(ci);
		}

		{
			final ServerInteraction si = ServerInteraction.begin();

			InterHelpBase.readHeader(corrHeader, si);

			si.setSubnode("esb_d1.ct_esb_gw_prod-srvc-res-workforce_01_ozod6003.esb_gw_mpc");
			si.setGroupName("gw.mpc.Transform");
			si.setServiceName("mpcGWInboundRequestProcess");
			si.setOpName("mpcGWinReq.TransformAndPrepDynEP");
			si.setUrl("/gw.mpc.Transform/mpcGWInboundRequestProcess");
			si.setSelfAddr(from);
			si.setPeerAddr(from);
			si.setCalleeAddress(from);
			si.setCallerAddress(from);
			si.setAppType(DisplayType.ESB_SERVICE);
			si.setSvcType(DisplayType.PROCESS);
			si.setOpType(DisplayType.STEP);
			si.setOneWay(true);
			si.setMsgField("ESB-Service", "gw.mpc.Transform", true);
			si.setMsgField("ESB-Top-Process", "mpcGWInboundRequestProcess", true);
			si.setMsgField("ESB-Container", "esb_gw_mpc", true);
			si.setMsgField("ESB-Process", "mpcGWInboundRequestProcess", true);

			si.setElapsed(12);
			si.end();
		}
	}
}
