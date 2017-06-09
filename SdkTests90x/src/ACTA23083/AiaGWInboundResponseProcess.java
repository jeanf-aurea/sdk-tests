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

import util.ManualCorrelator;

import com.actional.lg.interceptor.sdk.ClientInteraction;
import com.actional.lg.interceptor.sdk.DisplayType;
import com.actional.lg.interceptor.sdk.Interaction;
import com.actional.lg.interceptor.sdk.ServerInteraction;
import com.actional.lg.interceptor.sdk.helpers.InterHelpBase;

public class AiaGWInboundResponseProcess
{
	private static final long DEST_NB = computeDestNb();

	public static void main(final String[] args)
		throws Exception
	{
		ManualCorrelator.init();

		invalidPeerAddr();

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

	private static void invalidPeerAddr()
		throws Exception
	{
		String corrHeader;

		{
			final ServerInteraction si = ServerInteraction.begin();

			si.setPeerType(DisplayType.NO_PEER);
			si.setSubnode("esb_d1.ct_esb_gw_enterprise_01_ozod6003.esb_gw_sap");
			si.setGroupName("gw.sap.Dispatch");
			si.setServiceName("sapGWOutboundProcess");
			si.setOpName("sapGWoutOnline.StartProcess:sapGWout.Dispatch2DynamicEP");
			si.setUrl("/gw.sap.Dispatch/sapGWOutboundProcess");
			si.setSelfAddr("ozod6003");
			si.setPeerAddr("ozod6003");
			si.setAppType(DisplayType.ESB_SERVICE);
			si.setSvcType(DisplayType.PROCESS);
			si.setOpType(DisplayType.STEP);

			final ClientInteraction ci = ClientInteraction.begin(si);

			final long destNb = DEST_NB;

			System.out.println("Generated destination number " + destNb);

			ci.setSubnode("esb_d1.ct_esb_gw_enterprise_01_ozod6003.esb_gw_sap");
			ci.setGroupName("JMS-Destination");
			ci.setUrl("/JMS-Destination/rn_con_" + destNb + "::gw-aia-response-int-q");
			ci.setSelfAddr("ozod6003");
			ci.setPeerAddr("Unknown-SonicMQ-Broker");
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

			si.setSubnode("esb_d1.ct_esb_gw_crm-sales-mkt-b2b_03_ozod6002.esb_gw_aia");
			si.setGroupName("aiaGWInboundResponseProcess");
			si.setServiceName("ProcessEntry");
			si.setUrl("/aiaGWInboundResponseProcess/ProcessEntry");
			si.setSelfAddr("ozod6002");
			si.setPeerAddr("Unknown-SonicMQ-Broker");
			si.setCalleeAddress("Unknown-SonicMQ-Broker");
			si.setCallerAddress("ozod6003");
			si.setAppType(DisplayType.ESB_SERVICE);
			si.setSvcType(DisplayType.STANDALONE);
			si.setOneWay(true);


			final ClientInteraction ci = ClientInteraction.begin(si);

			ci.setElapsed(1);
			ci.setSubnode("esb_d1.ct_esb_gw_crm-sales-mkt-b2b_03_ozod6002.esb_gw_aia");
			ci.setGroupName("gw.aia.CBR");
			ci.setServiceName("aiaGWInboundResponseProcess");
			ci.setOpName("aiaGWinRes.ErrorTopicRouting");
			ci.setUrl("/gw.aia.CBR/aiaGWInboundResponseProcess");
			ci.setSelfAddr("ozod6002");
			ci.setPeerAddr("ozod6002");
			ci.setAppType(DisplayType.ESB_SERVICE);
			ci.setSvcType(DisplayType.PROCESS);
			ci.setOpType(DisplayType.STEP);
			ci.setOneWay(true);

			ci.end();

			si.setElapsed(11);
			si.end();

			corrHeader = InterHelpBase.writeHeader(ci);
		}

		{
			final ServerInteraction si = ServerInteraction.begin();

			InterHelpBase.readHeader(corrHeader, si);

			si.setSubnode("esb_d1.ct_esb_gw_crm-sales-mkt-b2b_03_ozod6002.esb_gw_aia");
			si.setGroupName("gw.aia.CBR");
			si.setServiceName("aiaGWInboundResponseProcess");
			si.setOpName("aiaGWinRes.ErrorTopicRouting");
			si.setUrl("/gw.aia.CBR/aiaGWInboundResponseProcess");
			si.setSelfAddr("ozod6002");
			si.setPeerAddr("ozod6002");
			si.setCalleeAddress("ozod6002");
			si.setCallerAddress("ozod6002");
			si.setAppType(DisplayType.ESB_SERVICE);
			si.setSvcType(DisplayType.PROCESS);
			si.setOpType(DisplayType.STEP);
			si.setOneWay(true);

			si.setElapsed(12);
			si.end();
		}
	}
}
