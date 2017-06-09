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

package alert;

import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.actional.lg.interceptor.sdk.LogLevel;
import com.actional.lg.interceptor.sdk.Part;
import com.actional.lg.interceptor.sdk.ServerInteraction;

import util.ManualCorrelator;

/** <!-- ========================================================================================================== -->
 * @lastrev fixXXXXX - new class
 * <!-- -------------------------------------------------------------------------------------------------------- --> */

public class EverGrowingFlow extends util.Call
{
	private static final PrintStream STDOUT = System.out;

	public static void main(final String[] args)
	{
		try
		{
			ManualCorrelator.init();

			useCaseU1();
		}
		catch (final Throwable e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	private EverGrowingFlow(final String test)
	{
		super(test);
	}

	private static final String PART_REQUEST = "request";
	private static final String PART_REPLY = "reply";

	private static final String NEW_FLOW_CMD = "new";
	private static final String EXIT_CMD = "exit";
	private static final String LOG_CMD = "log ";
	private static final String PART_CMD = "part";

	private static final Pattern PART_PATTERN = Pattern.compile(
			"\\s*" + PART_CMD + "\\s+(" + PART_REQUEST + "|" + PART_REPLY + ")\\s+(.+)");

	private static void printHelp()
	{
		STDOUT.println("Press <ENTER> to generate another step in the flow.");
		STDOUT.println("Enter '" + EXIT_CMD + "' to terminate.");
		STDOUT.println("Enter '" + NEW_FLOW_CMD + "' to start a new flow.");
		STDOUT.println("Enter '" + LOG_CMD + "<message>' to create an application log.");
		STDOUT.println("Enter '" + PART_CMD + " <" + PART_REQUEST + "|" + PART_REPLY + "> <json>' to add a part to the next SI.");
	}

	private static void useCaseU1() throws Exception
	{
		final LineNumberReader lnr = new LineNumberReader(new InputStreamReader(System.in));

		printHelp();

		final String prefix = "u1";
		final EverGrowingFlow test = new EverGrowingFlow(prefix);
		final List<Part> requestParts = new ArrayList<>();
		final List<Part> replyParts = new ArrayList<>();

		try
		{
			while (true)
			{
				STDOUT.println("Starting a new flow...");

				ServerInteraction si = test.inbound("web-client", "node1", "app1", "service1", "op1");

				si.setElapsed(1);

				si.end();

				int index = 2;

				while (true)
				{
					String line = lnr.readLine();

					if (null == line)
						return;

					line = line.trim().toLowerCase();

					if (EXIT_CMD.equals(line))
						return;

					if (NEW_FLOW_CMD.equals(line))
						break;

					if (line.startsWith(LOG_CMD))
					{
						final String msg = line.substring(LOG_CMD.length()).trim();

						if (msg.startsWith("{") && msg.endsWith("}"))
							test.addJsonLogEntry(si, msg);
						else
							test.logEntry(si, LogLevel.INFO, msg);

						continue;
					}

					final Matcher matcher = PART_PATTERN.matcher(line);

					if (matcher.matches())
					{
						final String reqOrRep = matcher.group(1);
						final String json = matcher.group(2);
						final Part part = jsonToPart(json);

						if (PART_REQUEST.equals(reqOrRep))
							requestParts.add(part);
						else
							replyParts.add(part);

						continue;
					}

					if (!line.isEmpty())
					{
						STDOUT.println("Unrecognized command : " + line);
						printHelp();
						continue;
					}

					STDOUT.println("Generating step " + index);

					si = test.inbound(si, "node" + index, "app" + index, "service" + index, "op" + index);

					if (!requestParts.isEmpty())
					{
						for (final Part part : requestParts)
							si.addRequestPart(part);

						requestParts.clear();

						si.requestAnalyzed();
					}

					if (!replyParts.isEmpty())
					{
						si.requestAnalyzed();

						for (final Part part : replyParts)
							si.addRequestPart(part);

						replyParts.clear();

						si.requestAnalyzed();
					}

					si.setElapsed(index++);

					si.end();
				}
			}
		}
		finally
		{
			STDOUT.println("Exiting...");
		}
	}
}
