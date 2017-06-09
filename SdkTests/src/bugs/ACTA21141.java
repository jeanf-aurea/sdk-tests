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
import com.actional.lg.interceptor.sdk.ServerInteraction;

public class ACTA21141
{
	public static void main(String[] args)
	{
		final ServerInteraction si = ServerInteraction.begin();

		si.setSubnode("act-sandeep-01");
		si.setGroupName("REST");
		si.setServiceName("RESTAccessPoint");
		si.setOpName("/echoEntities/echo echoJsonJson");
		si.setGroupID("D000-vqYy_13J1By5XEcB-FC74E1CA");
//		si.setServiceID("qsjouHuaNcMFknj6vayueg==");
		si.setOpID("mK6RWSVojVE3P8MUf+XdtA==");
		si.setUrl("/sst/runtime.asvc/com.actional.intermediary.REST");
		si.setSelfAddr(GeneralUtil.DEMO_PREFIX + "act-sandeep-01");
		si.setPeerAddr("172.29.12.164");
		si.setPlatformType((short) 124);
		si.setAppType((short) 9);
		si.setSvcType((short) 33);
		si.setOpType((short) 24);

		final ClientInteraction ci = ClientInteraction.begin();

		ci.setSubnode("act-sandeep-01");
		ci.setGroupName("/Echo");
		ci.setServiceName("/resources/");
		ci.setOpName("/echoEntities/echo echoJsonJson");
		ci.setGroupID("ka3g6MhF3BEdKXZsbcRa3Q==");
//		ci.setServiceID("MnIlpR2Q/yieRErkMp4G0A==");
		ci.setOpID("utbVcIBnt1afSTnNjhpIzA==");
		ci.setUrl("http://act-restcxf.aurea.local:8080/Echo/resources/");
		ci.setSelfAddr(GeneralUtil.DEMO_PREFIX + "act-sandeep-01");
		ci.setPeerAddr(GeneralUtil.DEMO_PREFIX + "act-restcxf.aurea.local");
		ci.setPlatformType((short) 124);
		ci.setAppType((short) 8);
		ci.setSvcType((short) 33);
		ci.setOpType((short) 24);

		ci.end();

		si.end();
	}
}
