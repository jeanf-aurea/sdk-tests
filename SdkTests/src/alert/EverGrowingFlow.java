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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.actional.lg.interceptor.sdk.ClientInteraction;
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

	private static final String REQUEST = "request";
	private static final String REPLY = "reply";

	private static final String NEW_FLOW_CMD = "new";
	private static final String EXIT_CMD = "exit";
	private static final String LOG_CMD = "log ";
	private static final String PART_CMD = "part";
	private static final String FIELD_CMD = "field";
	private static final String BEGIN_CMD = "begin";
	private static final String ELAPSED_CMD = "elapsed";
	private static final String BASE_TIME_CMD = "baseTime";

	private static final Pattern PART_PATTERN = Pattern.compile(
			"\\s*" + PART_CMD + "\\s+(" + REQUEST + "|" + REPLY + ")\\s+(.+)");

	private static void printHelp()
	{
		STDOUT.println("Press <ENTER> to generate another step in the flow.");
		STDOUT.println("Enter '" + EXIT_CMD + "' to terminate.");
		STDOUT.println("Enter '" + NEW_FLOW_CMD + "' to start a new flow.");
		STDOUT.println("Enter '" + LOG_CMD + "<message>' to create an application log.");
		STDOUT.println("Enter '" + PART_CMD + " <" + REQUEST + "|" + REPLY + "> <json>' to add a part to the next SI.");
		STDOUT.println("Enter '" + FIELD_CMD + " <" + REQUEST + "|" + REPLY + "> <name> <value>' to add a message field to the next SI.");
		STDOUT.println("Enter '" + BASE_TIME_CMD + " to reset the base begin time to now.");
		STDOUT.println("Enter '" + BEGIN_CMD + " <integer value> to set the begin time of the interaction (considered relative time to base time if before 2017/01/01 00:00:00 UTC).");
		STDOUT.println("Enter '" + ELAPSED_CMD + " <integer value> to set the begin time of the interaction.");
	}

	private static void useCaseU1() throws Exception
	{
		final LineNumberReader lnr = new LineNumberReader(new InputStreamReader(System.in));

		printHelp();

		final long utc2017 = 1483228800000L; // January 1st 2017
		final String prefix = "u1";
		final EverGrowingFlow test = new EverGrowingFlow(prefix);
		final List<Part> requestParts = new ArrayList<>();
		final List<Part> replyParts = new ArrayList<>();
		final Map<String, String> requestMsgFields = new HashMap<>();
		final Map<String, String> replyMsgFields = new HashMap<>();
		long beginTime = -1L;
		int elapsedTime = -1;
		ServerInteraction si = null;
		int index = 1;

		long baseTime = System.currentTimeMillis();

		try
		{
			while (true)
			{
				String line = lnr.readLine();

				if (null == line)
					return;

				line = line.trim();

				if (line.isEmpty() || NEW_FLOW_CMD.equals(line))
				{
					if (line.isEmpty())
					{
						STDOUT.println("Generating step " + index);

						final String l1 = "node" + index;
						final String l2 = "app" + index;
						final String l3 = "service" + index;
						final String l4 = "op" + index;

						if (si == null)
						{
							si = test.inbound("web-client", l1, l2, l3, l4);
						}
						else
						{
							final ClientInteraction ci = test.outbound(si, l1, l2, l3, l4);

							if (beginTime >= 0)
							{
								if (beginTime < utc2017)
									ci.setBeginTime(baseTime + beginTime);
								else
									ci.setBeginTime(beginTime);
							}

							if (elapsedTime < 0)
								ci.setElapsed(index);
							else
								ci.setElapsed(elapsedTime);

							ci.end();

							si = test.inbound(ci, l1, l2, l3, l4);
						}

						si.setSecurityID("jeanf@aurea.com");

						if (beginTime >= 0)
						{
							if (beginTime < utc2017)
								si.setBeginTime(baseTime + beginTime);
							else
								si.setBeginTime(beginTime);
						}

						for (final Part part : requestParts)
							si.addRequestPart(part);

						for (final Map.Entry<String, String> field : requestMsgFields.entrySet())
						{
							si.setMsgField(field.getKey(), field.getValue(), true);
						}

						if (!requestParts.isEmpty() || !replyParts.isEmpty() ||
						    !requestMsgFields.isEmpty() || !replyMsgFields.isEmpty())
						{
							si.requestAnalyzed();
						}

						for (final Part part : replyParts)
							si.addReplyPart(part);

						for (final Map.Entry<String, String> field : replyMsgFields.entrySet())
						{
							si.setMsgField(field.getKey(), field.getValue(), true);
						}

						if (elapsedTime < 0)
							si.setElapsed(index);
						else
							si.setElapsed(elapsedTime);

						si.end();

						index++;
					}
					else
					{
						STDOUT.println("Starting a new flow...");

						si = null;
						index = 1;
					}

					requestParts.clear();
					requestMsgFields.clear();
					replyParts.clear();
					replyMsgFields.clear();
					beginTime = -1L;
					elapsedTime = -1;

					continue;
				}

				if (EXIT_CMD.equals(line))
					return;

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

					if (REQUEST.equals(reqOrRep))
						requestParts.add(part);
					else
						replyParts.add(part);

					continue;
				}

				final String[] cmdLine = line.split("\\s+");

				if (cmdLine.length == 4 && cmdLine[0].equals(FIELD_CMD))
				{
					if (REQUEST.equals(cmdLine[1]))
					{
						requestMsgFields.put(cmdLine[2], cmdLine[3]);
						continue;
					}
					else if (REPLY.equals(cmdLine[1]))
					{
						replyMsgFields.put(cmdLine[2], cmdLine[3]);
						continue;
					}
				}
				else if (cmdLine.length == 2 && BEGIN_CMD.equals(cmdLine[0]))
				{
					beginTime = Long.parseLong(cmdLine[1]);
					continue;
				}
				else if (cmdLine.length == 2 && ELAPSED_CMD.equals(cmdLine[0]))
				{
					elapsedTime = Integer.parseInt(cmdLine[1]);
					continue;
				}
				else if (cmdLine.length == 1 && BASE_TIME_CMD.equals(cmdLine[0]))
				{
					baseTime = System.currentTimeMillis();
					continue;
				}

				STDOUT.println("Unrecognized command : " + Arrays.asList(cmdLine));
				printHelp();
			}
		}
		finally
		{
			STDOUT.println("Exiting...");
		}
	}
}
