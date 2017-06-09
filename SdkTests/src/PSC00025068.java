//=====================================================================================================================
// $HeadURL: http://uxmtlsvn1.americas.progress.com/repos/Actional/branches/act82x/product/src/builtinplugins/com/actional/plugin/msgfield/MsgFieldXpathEvaluator.java $
// Checked in by: $Author: jeanf $
// $Date: 2011-07-13 11:16:54 -0400 (Wed, 13 Jul 2011) $
// $Revision: 51358 $
//---------------------------------------------------------------------------------------------------------------------
// Copyright (c) 2011 Progress Software Corporation and/or its subsidiaries or affiliates. All rights reserved.
//=====================================================================================================================

import com.actional.lg.interceptor.sdk.ClientInteraction;
import com.actional.lg.interceptor.sdk.Interaction;
import com.actional.lg.interceptor.sdk.ServerInteraction;
import com.actional.lg.interceptor.sdk.SiteStub;
import com.actional.lg.interceptor.sdk.helpers.InterHelpBase;

/**
 *
 */
public class PSC00025068
{
	public static void main(String[] args)
		throws Exception
	{
		long now = System.currentTimeMillis();

		ServerInteraction si = ServerInteraction.begin();

		si.setBeginTime(now);
		si.setSubnode("nl-fldi-00557");
		si.setUrl("sonic://nl-fldi-00557.wdw.disney.com/rnData$$DataBroker1/$Topic$DREAMS.ACCOMMODATION.CHANGE");
		si.setGroupName("rnData::DataBroker1");
		si.setServiceName("$Topic$DREAMS.ACCOMMODATION.CHANGE");
		si.setGroupID("rnData::DataBroker1");
		si.setPeerAddr("localhost");
		si.setSelfAddr("localhost");
		si.setSvcType((short) -128);
		si.setOneWay(true);
		si.setMsgField("FlowField-CorrelationID", "2FD1E4CBA40F5024B29803B0FE855A8B.node001_1", true);
		si.setMsgField("RoutingNodeName", "rnData", true);
		si.setMsgField("BrokerName", "DataBroker1", true);
		si.getIncludeMsg();
		SiteStub siteStub = si.split();

		si.end();

//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		ObjectOutputStream oos = new ObjectOutputStream(baos);
//		oos.writeObject(siteStub);
//		oos.close();
//
//		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
//		ObjectInputStream ois = new ObjectInputStream(bais);
//		siteStub = (SiteStub) ois.readObject();

		siteStub = (SiteStub) Interaction.deserialize(siteStub.serializeAsBytes());

		si = ServerInteraction.begin(siteStub);

		ClientInteraction ci = ClientInteraction.begin();

		ci.setBeginTime(now + 2L);
		ci.setSubnode("nl-fldi-00557");
		ci.setGroupName("appuser");
		ci.setServiceName("$Topic$DREAMS.ACCOMMODATION.CHANGE");
		ci.setGroupID("appuser");
		ci.setUrl("tcp://nl-fldi-00556.wdw.disney.com/appuser/$Topic$DREAMS.ACCOMMODATION.CHANGE");
		ci.setPeerAddr("localhost");
		ci.setSelfAddr("localhost");
		ci.setSvcType((short) -128);
		ci.setOneWay(true);
		si.setMsgField("RoutingNodeName", "rnData", true);
		si.setMsgField("BrokerName", "DataBroker1", true);

		String corr = InterHelpBase.writeHeader(ci);

		System.out.println(corr);

		ci.requestAnalyzed();

		ci.end();

		si.end();

		si = ServerInteraction.begin();

		InterHelpBase.readHeader(corr, si);

		si.setBeginTime(now + 5L);
		si.setSubnode("dmNGEDev3.StrategicFunctionality.StrategicFunctionality");
		si.setGroupName("ProcessAccommodationEvent");
		si.setServiceName("ProcessEntry");
		si.setGroupID("ProcessAccommodationEvent");
		si.setUrl("/ProcessAccommodationEvent/ProcessEntry");
		si.setPeerAddr("localhost");
		si.setSelfAddr("localhost");
		si.setAppType((short) 21);
		si.setSvcType((short) 20);
		si.setOneWay(true);
		si.setMsgField("ESB-Service", "ProcessAccommodationEvent", true);
		si.setMsgField("ESB-Container", "StrategicFunctionality", true);

		si.requestAnalyzed();

		si.end();
	}
}
