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

package bugs;

import com.actional.lg.interceptor.sdk.ClientInteraction;
import com.actional.lg.interceptor.sdk.ServerInteraction;

public class ACTA25565_NullServiceNameNPEInAMS
{
	public static void main(final String[] args) throws InterruptedException
	{
		final ServerInteraction si = ServerInteraction.begin();

		si.setElapsed(1);
		si.setSubnode("crm-bck-beta2.feddom.ad.fedins.com");
		si.setUrl("net.tcp://localhost/crmsandboxhost");
		si.setSelfAddr("CRM-BCK-BETA2.feddom.ad.fedins.com");
		si.setPeerAddr("161.250.5.41");
		si.setPlatformType((short)136);
		si.setAppType((short)8);
		si.setSvcType((short)-128);

		si.end();

		final ClientInteraction ci = ClientInteraction.begin();

		ci.setElapsed(1);
		ci.setSubnode("crm-bck-beta2.feddom.ad.fedins.com");
		ci.setUrl("net.tcp://localhost/crmsandboxhost");
		ci.setSelfAddr("CRM-BCK-BETA2.feddom.ad.fedins.com");
		ci.setPeerAddr("161.250.5.41");
		ci.setPlatformType((short)136);
		ci.setAppType((short)8);
		ci.setSvcType((short)-128);

		ci.end();

		Thread.sleep(1000L);
		
		System.out.println("Done");
	}

	public static void main2(final String[] args) throws InterruptedException
	{
		final ServerInteraction si = ServerInteraction.begin();

		si.setElapsed(1);
		si.setSubnode("crm-bck-beta2.feddom.ad.fedins.com");
		si.setGroupName("crmsandboxhost");
		si.setGroupID("crmsandboxhost");
		si.setServiceID("bCnbArEyREWMbPDYRNUCPw==");
		si.setUrl("net.tcp://localhost/crmsandboxhost");
		si.setOpID("bCnbArEyREWMbPDYRNUCPw==");
		si.setSelfAddr("CRM-BCK-BETA2.feddom.ad.fedins.com");
		si.setPeerAddr("161.250.5.41");
		si.setPlatformType((short)136);
		si.setAppType((short)8);
		si.setSvcType((short)-128);

		si.end();

		Thread.sleep(1000L);
	}
}
