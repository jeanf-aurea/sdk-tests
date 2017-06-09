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

import java.util.Date;

import util.ManualCorrelator;

import com.actional.lg.interceptor.sdk.ClientInteraction;
import com.actional.lg.interceptor.sdk.DisplayType;
import com.actional.lg.interceptor.sdk.Interaction;
import com.actional.lg.interceptor.sdk.ServerInteraction;
import com.actional.lg.interceptor.sdk.helpers.InterHelpBase;

public class AiaGWInboundNotifEventProcess
{
	public static void main(final String[] args)
		throws Exception
	{
		ManualCorrelator.init();

		// Wait until the middle of the gather.
		final long now = System.currentTimeMillis();
		final long millisWithinCurrentMinute = now % 60000;

		if (millisWithinCurrentMinute <= 29000L)
		{
			final long sleepFor = 30000L - millisWithinCurrentMinute;

			sleep(sleepFor);
		}
		else if (millisWithinCurrentMinute > 40000L)
		{
			final long sleepFor = 30000L + (60000L - millisWithinCurrentMinute);

			sleep(sleepFor);
		}

		// prepopulate some of the NGSOs
		System.out.println(new Date() + " : Prepopulate NGSO");

		prepopulate(populateEventNotification(ServerInteraction.begin(), "ozod6002"));
		prepopulate(populateEventNotification(ServerInteraction.begin(), "ozod6003"));

		prepopulate(populateProcessEntry(ServerInteraction.begin(), "ozod6002"));
		prepopulate(populateProcessEntry(ServerInteraction.begin(), "ozod6003"));

		prepopulate(populatePrototype(ServerInteraction.begin(), "ozod6002"));
		prepopulate(populatePrototype(ServerInteraction.begin(), "ozod6003"));

		// Wait until next gather
		sleep(60000L);

		invalidPeerAddr("ozod6002", "ozod6003");

		// Wait until next gather
		sleep(60000L);

		invalidPeerAddr("ozod6003", "ozod6002");

		Interaction.getUplink().shutdown();
	}

	private static void sleep(final long sleepFor)
		throws InterruptedException
	{
		final long roundedTo30ms = sleepFor + ((30 - sleepFor) % 30);

		System.out.println(new Date() + " : Sleeping for " + roundedTo30ms + " milliseconds.");

		Thread.sleep(roundedTo30ms);
	}

	private static void invalidPeerAddr(final String ciSelfAddr, final String siSelfAddr)
		throws Exception
	{
		System.out.println(new Date() + " : " + ciSelfAddr + " => " + siSelfAddr);

		String corr;

		{
			final ServerInteraction si = ServerInteraction.begin();

			populateEventNotification(si, ciSelfAddr);
			si.setPeerAddr(ciSelfAddr);

			si.requestAnalyzed();

			si.setElapsed(13);

			si.end();

			final ClientInteraction ci = ClientInteraction.begin(si);

			ci.setSubnode("esb_d1.ct_esb_gw_orderexec-des-conf-acti_03_" + ciSelfAddr + ".esb_gw_mcom");
			ci.setGroupName("JMS-Destination");
			ci.setGroupID("JMS-Destination");
			ci.setUrl("/JMS-Destination/gw-aia-event-int-q");
			ci.setSelfAddr(ciSelfAddr);
			ci.setPeerAddr("ozod6002");
			ci.setAppType(DisplayType.APP);
			ci.setSvcType(DisplayType.ACTIVE_DOCUMENT);
			ci.setOneWay(true);

			ci.requestAnalyzed();

			corr = InterHelpBase.writeHeader(ci);

			ci.setElapsed(11);

			ci.end();
		}

		{
			final ServerInteraction si = ServerInteraction.begin();

			InterHelpBase.readHeader(corr, si);

			populateProcessEntry(si, siSelfAddr);
			si.setPeerAddr("ozod6002");

			si.requestAnalyzed();

			si.setElapsed(13);

			si.end();

			final ClientInteraction ci = ClientInteraction.begin(si);

			populatePrototype(ci, siSelfAddr);
			ci.setPeerAddr(siSelfAddr);

			corr = InterHelpBase.writeHeader(ci);

			ci.setElapsed(7);

			ci.end();
		}

		{
			final ServerInteraction si = ServerInteraction.begin();

			InterHelpBase.readHeader(corr, si);

			populatePrototype(si, siSelfAddr);
			si.setPeerAddr(siSelfAddr);

			si.requestAnalyzed();

			si.setElapsed(17);

			si.end();
		}
	}

	private static void prepopulate(final Interaction intr)
	{
		intr.setPeerType(DisplayType.NO_PEER);
		intr.end();
	}

	private static Interaction populateEventNotification(final Interaction intr, final String selfAddr)
	{
		intr.setSubnode("esb_d1.ct_esb_gw_orderexec-des-conf-acti_03_" + selfAddr + ".esb_gw_mcom");
		intr.setGroupName("gw.mcom.EventNotification");
		intr.setGroupID("gw.mcom.EventNotification");
		intr.setServiceName("mcomGWOutboundProcess");
		intr.setOpName("mcomGWoutEvt.StartProcess:mcomGWout.Dispatch2MultiQ");
		intr.setUrl("/gw.mcom.EventNotification/mcomGWOutboundProcess");
		intr.setAppType(DisplayType.ESB_SERVICE);
		intr.setSvcType(DisplayType.PROCESS);
		intr.setOpType(DisplayType.STEP);
		intr.setOneWay(true);
		intr.setSelfAddr(selfAddr);

		return intr;
	}

	private static Interaction populateProcessEntry(final Interaction intr, final String selfAddr)
	{
		intr.setSubnode("esb_d1.ct_esb_gw_crm-sales-mkt-b2b_03_" + selfAddr + ".esb_gw_aia");
		intr.setGroupName("aiaGWInboundNotifEventProcess");
		intr.setGroupID("aiaGWInboundNotifEventProcess");
		intr.setServiceName("ProcessEntry");
		intr.setUrl("/aiaGWInboundNotifEventProcess/ProcessEntry");
		intr.setAppType(DisplayType.ESB_SERVICE);
		intr.setSvcType(DisplayType.STANDALONE);
		intr.setOneWay(true);
		intr.setSelfAddr(selfAddr);

		return intr;
	}

	private static Interaction populatePrototype(final Interaction intr, final String selfAddr)
	{
		intr.setSubnode("esb_d1.ct_esb_gw_crm-sales-mkt-b2b_03_" + selfAddr + ".esb_gw_aia");
		intr.setGroupName("gw.aia.Prototype");
		intr.setGroupID("gw.aia.Prototype");
		intr.setServiceName("aiaGWInboundNotifEventProcess");
		intr.setOpName("aiaGWInEvtNotif.GetId97FromSetting");
		intr.setUrl("/gw.aia.Prototype/aiaGWInboundNotifEventProcess");
		intr.setAppType(DisplayType.ESB_SERVICE);
		intr.setSvcType(DisplayType.PROCESS);
		intr.setOpType(DisplayType.STEP);
		intr.setOneWay(true);
		intr.setSelfAddr(selfAddr);

		return intr;
	}
}
