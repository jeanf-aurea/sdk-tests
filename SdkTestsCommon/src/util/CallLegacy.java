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

package util;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.actional.GeneralUtil;
import com.actional.lg.interceptor.sdk.ClientInteraction;
import com.actional.lg.interceptor.sdk.DisplayType;
import com.actional.lg.interceptor.sdk.ILogEntry;
import com.actional.lg.interceptor.sdk.LogLevel;
import com.actional.lg.interceptor.sdk.ServerInteraction;
import com.actional.lg.interceptor.sdk.helpers.InterHelpBase;
import com.actional.util.B64Code;

class CallLegacy
{
	protected static final String PREFIX = GeneralUtil.DEMO_PREFIX;

	protected static final String NULL_STRING = null;

	private static final boolean itMustPopulate = Boolean.getBoolean("populate");

	protected boolean itIsPopulating = itMustPopulate;

	protected final String itsPrefix;

	protected final String itsTestPrefix;

	protected CallLegacy(String test)
	{
		itsPrefix = test + '-';
		itsTestPrefix = PREFIX + test + '-';
	}

	public final void run() throws Exception
	{
		int loop = itIsPopulating ? 1 : Integer.getInteger("loop", 10);

		if (loop <= 0)
			loop = Integer.MAX_VALUE;

		run(loop);
	}

	public final void run(int loop) throws Exception
	{
		if (itIsPopulating)
			loop = 1;

		long start = System.currentTimeMillis();

		doRun(loop);

		long end = System.currentTimeMillis();
		long elapsed = end - start;

		System.out.println(getClass().getName());
		System.out.println("\tTotal: " + elapsed + " ms");
		System.out.println("\tAverage: " + (elapsed/loop) + " ms");
	}

	protected void doRun(int loop) throws Exception
	{
	}

	protected ILogEntry beginLogEntry(ServerInteraction si)
	{
		return ManualCorrelator.beginLogEntry(si);
	}

	protected void logEntry(ServerInteraction si, LogLevel logLevel, Object msg)
	{
		final ILogEntry le = ManualCorrelator.beginLogEntry(si);

		le.setLogLevel(logLevel);
		le.setLogMessage(msg);
		le.end();
	}

	protected ClientInteraction toFakeQueue(ServerInteraction parentSI, String queueName) throws Exception
	{
		return toFakeQueue(parentSI, parentSI.getSelfAddr(), queueName);
	}

	protected ClientInteraction toFakeQueue(ServerInteraction parentSI, String fromNode, String queueName) throws Exception
	{
		return toFakeQueue(parentSI, fromNode, queueName, System.currentTimeMillis(), 3);
	}

	protected ClientInteraction toFakeQueue(ServerInteraction parentSI, String fromNode, String queueName, long start, int duration) throws Exception
	{
		ClientInteraction ci = ClientInteraction.begin(parentSI);

		if (itIsPopulating)
			ci.setInternal(true);

		if (!fromNode.startsWith(PREFIX))
			fromNode = itsTestPrefix + fromNode;

		ci.setSelfAddr(fromNode);
		ci.setPeerAddr(itsPrefix + queueName);
		ci.setPeerType(DisplayType.QUEUE);
		ci.setOneWay(true);
		ci.setUrl("/");
		ci.setBeginTime(start);
		ci.setElapsed(duration);
		ci.end();

		return ci;
	}

	protected ClientInteraction outbound(ServerInteraction parentSI, String toNode, String toApp, String toService, String toOp) throws Exception
	{
		return outbound(parentSI, parentSI.getSelfAddr(), toNode, toApp, toService, toOp);
	}

	protected ClientInteraction outbound(ServerInteraction parentSI, String fromNode, String toNode, String toApp, String toService, String toOp) throws Exception
	{
		ClientInteraction ci = ClientInteraction.begin(parentSI);

		if (itIsPopulating)
			ci.setInternal(true);

		if (!toNode.startsWith(PREFIX))
			toNode = itsTestPrefix + toNode;

		if ((fromNode != null) && !fromNode.startsWith(PREFIX))
			fromNode = itsTestPrefix + fromNode;

		ci.setSelfAddr(fromNode);
		ci.setPeerAddr(toNode);
		ci.setOneWay(true);
		ci.setUrl('/' + toApp + '/' + toService);
		ci.setGroupName(toApp);
		ci.setServiceName(toService);
		ci.setOpName(toOp);

		if (toService.indexOf("queue") >= 0)
			ci.setSvcType(DisplayType.QUEUE);

		return ci;
	}

	protected ServerInteraction inbound(String fromNode, String toNode, String toApp, String toService, String toOp) throws Exception
	{
		return inbound(null, fromNode, toNode, toApp, toService, toOp);
	}

	protected ServerInteraction inbound(ServerInteraction parentSI, String toNode, String toApp, String toService, String toOp) throws Exception
	{
		ClientInteraction ci = outbound(parentSI, toNode, toApp, toService, toOp);
		ServerInteraction si = inbound(ci);

		ci.end();

		return si;
	}

	protected ServerInteraction inbound(ClientInteraction ci) throws Exception
	{
		return inbound(ci, ci.getSelfAddr(), ci.getPeerAddr(), ci.getGroupName(), ci.getServiceName(), ci.getOpName());
	}

	/**
	 * You call this method to mimic a 'cloud' of unmanaged nodes sitting between two managed nodes, but that
	 * cloud having the quality of propagating the correlation headers inside of the unmanaged cloud up until
	 * reentering the managed world.
	 */
	protected ServerInteraction inbound(ClientInteraction ci, String toNode, String toApp, String toService, String toOp) throws Exception
	{
		return inbound(ci, ci.getPeerAddr(), toNode, toApp, toService, toOp);
	}

	protected ServerInteraction inbound(ClientInteraction ci, String fromNode, String toNode, String toApp, String toService, String toOp) throws Exception
	{
		ServerInteraction si = ServerInteraction.begin();

		if (ci != null)
		{
			String corrIDs = InterHelpBase.writeHeader(ci);
//			System.out.println(corrIDs);
			InterHelpBase.readHeader(corrIDs, si);
		}

		if (!toNode.startsWith(PREFIX))
			toNode = itsTestPrefix + toNode;

		if (fromNode == null)
			si.setPeerType(DisplayType.NO_PEER);
		else if (!fromNode.startsWith(PREFIX))
			fromNode = itsTestPrefix + fromNode;

		if (itIsPopulating)
		{
			si.setPeerAddr(null);
			si.setPeerType(DisplayType.NO_PEER);
		}
		else
		{
			si.setPeerAddr(fromNode);
		}
		si.setSelfAddr(toNode);
		si.setOneWay(true);
		si.setUrl('/' + toApp + '/' + toService);
		si.setGroupName(toApp);
		si.setServiceName(toService);
		si.setOpName(toOp);

		if (toService.indexOf("queue") >= 0)
			si.setSvcType(DisplayType.QUEUE);

		return si;
	}
//
//	protected ServerInteraction fromFakeQueue(ServerInteraction si, String queueName, String toNode, String toApp, String toService, String toOp) throws Exception
//	{
//		ClientInteraction ci = ClientInteraction.begin(si);
//
//		try
//		{
//			ci.setInternal(true);
//			ci.setUrl("/"); // just to keep the SDK happy.
//			ci.setPeerAddr(queueName); // just to keep the SDK happy.
//
//			return fromFakeQueue(ci, queueName, toNode, toApp, toService, toOp);
//		}
//		finally
//		{
//			ci.end();
//		}
//	}

	protected ServerInteraction fromFakeQueue(
			ClientInteraction ci,
			String queueName,
			String toNode,
			String toApp,
			String toService,
			String toOp)
		throws Exception
	{
		ServerInteraction si = ServerInteraction.begin();

		if (ci != null)
		{
			String corrIDs = InterHelpBase.writeHeader(ci);
//			System.out.println(corrIDs);
			InterHelpBase.readHeader(corrIDs, si);
		}

		if (!toNode.startsWith(PREFIX))
			toNode = itsTestPrefix + toNode;

		if (itIsPopulating)
		{
			si.setPeerAddr(null);
			si.setPeerType(DisplayType.NO_PEER);
		}
		else
		{
			si.setPeerAddr(itsPrefix + queueName);
			si.setPeerType(DisplayType.QUEUE);
		}
		si.setSelfAddr(toNode);
		si.setOneWay(true);
		si.setUrl('/' + toApp + '/' + toService);
		si.setGroupName(toApp);
		si.setServiceName(toService);
		si.setOpName(toOp);

		if (toService.indexOf("queue") >= 0)
			si.setSvcType(DisplayType.QUEUE);

		return si;
	}

	protected ClientInteraction database(ServerInteraction si, String toNode, String toDB, String toTable, String op)
		throws Exception
	{
		final ClientInteraction ci = outbound(si, toNode, toDB, toTable, op);

		ci.setAppType(DisplayType.DB);
		ci.setSvcType(DisplayType.TABLE);

		return ci;
	}

	protected void raiseAlert(String baseUrl, String flowID, String message)
		throws Exception
	{
		final URL url = new URL(baseUrl + "/api/IServer.aapi");
		final HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		try
		{
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setRequestProperty("SOAPAction", "\"\"");
			conn.setRequestProperty("Content-Type", "text/xml");
			conn.setRequestProperty("Authorization", "Basic " + B64Code.encode("User_Admin:security"));

			final OutputStream os = conn.getOutputStream();

			final String soap =
				"<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:m0=\"http://db.lg.actional.com\" xmlns:m1=\"http://config.actional.com\" xmlns:m2=\"http://db.contact.actional.com\" xmlns:m3=\"http://db.soapstation.actional.com\" xmlns:m4=\"http://classloader.actional.com\" xmlns:m5=\"http://db.actional.com\" xmlns:m6=\"http://management.license.actional.com\" xmlns:m7=\"http://catalog.actional.com\" xmlns:m8=\"http://actional.com\" xmlns:m9=\"http://db.jms.audit.actional.com\">" +
				"	<SOAP-ENV:Body>" +
				"		<m:raiseAlert xmlns:m=\"http://IServer.server.lg.actional.com\" SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">" +
				"			<alert xsi:type=\"m0:LGAlert\">" +
				"				<LTID xsi:type=\"xsd:string\">" + flowID + "</LTID>" +
				"				<message xsi:type=\"xsd:string\">" + message + "</message>" +
				"				<severity>ALARM</severity>" +
				"				<timeOccurred xsi:type=\"xsd:long\">" + System.currentTimeMillis() + "</timeOccurred>" +
				"				<actionMask xsi:type=\"xsd:long\">3</actionMask>" +
				"			</alert>" +
				"		</m:raiseAlert>" +
				"	</SOAP-ENV:Body>" +
				"</SOAP-ENV:Envelope>";

			os.write(soap.getBytes("UTF-8"));
			os.close();

			System.out.println(conn.getResponseCode() + ' ' + conn.getResponseMessage());
		}
		finally
		{
			conn.disconnect();
		}
	}
}