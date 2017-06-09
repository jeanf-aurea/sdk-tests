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

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.actional.lg.interceptor.internal.simulator.Simulator;
import com.actional.lg.interceptor.sdk.ClientInteraction;
import com.actional.lg.interceptor.sdk.ServerInteraction;
import com.actional.util.B64Code;

public class ACTA_26888 extends Simulator
{
	private static final String PRESERVE = "preserve";
	private static final String AUDIT = "audit";
	private static final String ALERT = "alert";
	private static final String NOTHING = "nothing";
	private static final String PRESERVE_AND_AUDIT = "preserve-and-audit";
	private static final String PRESERVE_AND_ALERT = "preserve-and-alert";

	private final LineNumberReader CONSOLE;

	private ACTA_26888() throws UnsupportedEncodingException
	{
		super("ACT26888");

		CONSOLE = new LineNumberReader(new InputStreamReader(System.in, "UTF-8"));
	}

	public static void main(String[] args)
		throws Exception
	{
		new ACTA_26888().generate();

		Thread.sleep(500L);
	}

	protected void generate()
		throws IOException
	{
		final List<String> flows = new ArrayList<String>();

		// Make sure to deploy the file ACT26888-deploy.xml found in this directory.

		flows.add(generate("test01", PRESERVE, AUDIT));
			  generate("test02", PRESERVE, ALERT);
		flows.add(generate("test03", PRESERVE, NOTHING));
		flows.add(generate("test04", AUDIT, PRESERVE));
			  generate("test05", ALERT, PRESERVE);
		flows.add(generate("test06", NOTHING, PRESERVE));
		flows.add(generate("test07", PRESERVE_AND_AUDIT, NOTHING));
			  generate("test08", PRESERVE_AND_ALERT, NOTHING);

		System.out.println("Press <ENTER> when you want to persist the preserved audit records.");
		CONSOLE.readLine();

		flowAuditing(flows);

		System.out.println("Done.");
	}

	private String generate(final String l3name, final String first, final String second)
		throws IOException
	{
		final String node1 = "node1";
		final String node2 = "node2";

		final ServerInteraction si = inbound("client", node1, first, l3name, null);

		final ClientInteraction ci = outbound(si, node2, second, l3name, null);

		// Just to make debugging easier as we do not have to step in the code for this event.
//		ci.setInternal(true);

		ci.end();

		si.end();

		System.out.println(l3name + " => " + si.getFlowID() + "  (" + first + "," + second + ")");

		final ServerInteraction si2 = inbound(ci, node2, second, l3name, null);

		si2.end();

		return si.getFlowID();
	}

	private static final String BASE_URL = System.getProperty("amsUrl", "http://localhost:4040/lgserver");

	private void flowAuditing(final List<String> flows)
		throws IOException
	{
		final URL url = new URL(BASE_URL + "/api/IFlowMapTask.aapi");

		System.out.println("Notifying " + url);

		final HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		try
		{
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setRequestProperty("SOAPAction", "\"\"");
			conn.setRequestProperty("Content-Type", "text/xml");
			conn.setRequestProperty("Authorization", "Basic " + B64Code.encode("User_Admin:security"));

			final OutputStream os = conn.getOutputStream();

			final StringBuilder sb = new StringBuilder();

			for (final String flow : flows)
			{
				sb.append("    <item>\n");

				sb.append("     <flowID>").append(flow).append("</flowID>\n");
				sb.append("     <isPersistingAudit>true</isPersistingAudit>\n");
				sb.append("     <isPersistingFlowMap>true</isPersistingFlowMap>\n");

				sb.append("    </item>\n");
			}

			final String soap = REQUEST_PART1 + sb + REQUEST_PART2;

			os.write(soap.getBytes("UTF-8"));
			os.close();

			System.out.println("[" + conn.getResponseCode() + "][" + conn.getResponseMessage() + "]");
		}
		finally
		{
			conn.disconnect();
		}
	}

	private static final String REQUEST_PART1 =
		"<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:xsd='http://www.w3.org/2001/XMLSchema' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>\n" +
		" <soapenv:Body>\n" +
		"  <ns1:startMonitoring soapenv:encodingStyle='http://schemas.xmlsoap.org/soap/encoding/' xmlns:ns1='http://IFlowMapTask.task.server.lg.actional.com'>\n" +
		"   <input href='#id0'/>\n" +
		"  </ns1:startMonitoring>\n" +
		"  <multiRef id='id0' soapenc:root='0' soapenv:encodingStyle='http://schemas.xmlsoap.org/soap/encoding/' xsi:type='ns2:StartMonitoringInput' xmlns:soapenc='http://schemas.xmlsoap.org/soap/encoding/' xmlns:ns2='http://to.task.server.lg.actional.com'>\n" +
		"   <flows xsi:type='soapenc:Array' soapenc:arrayType='ns2:FlowMonitoringTO[1]' xmlns:ns3='http://task.soapstation.actional.com'>\n";

	private static final String REQUEST_PART2 =
		"   </flows>\n" +
		"  </multiRef>\n" +
		" </soapenv:Body>\n" +
		"</soapenv:Envelope>\n";

}
