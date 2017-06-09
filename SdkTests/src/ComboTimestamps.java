//=====================================================================================================================
// $HeadURL: http://uxmtlsvn1.americas.progress.com/repos/Actional/branches/act82x/product/src/builtinplugins/com/actional/plugin/msgfield/MsgFieldXpathEvaluator.java $
// Checked in by: $Author: jeanf $
// $Date: 2011-07-13 11:16:54 -0400 (Wed, 13 Jul 2011) $
// $Revision: 51358 $
//---------------------------------------------------------------------------------------------------------------------
// Copyright (c) 2011 Progress Software Corporation and/or its subsidiaries or affiliates. All rights reserved.
//=====================================================================================================================

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import com.actional.lg.interceptor.sdk.ClientInteraction;
import com.actional.lg.interceptor.sdk.Interaction;
import com.actional.lg.interceptor.sdk.ServerInteraction;
import com.actional.lg.interceptor.sdk.helpers.InterHelpBase;

/**
 * @author jeanf
 */
public class ComboTimestamps
{
	private static final Random RANDOM = new Random();

	private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

	public static void main(String[] args) throws Exception
	{
		final ServerInteraction si1 = ServerInteraction.begin();

		setTiming(si1, "si1");

		si1.setUrl("/app/service1");
		si1.setPeerAddr("localhost");

		final ClientInteraction ci1 = ClientInteraction.begin();

		setTiming(ci1, "ci1");

		ci1.setUrl("/app/service2");
		ci1.setPeerAddr("localhost");

		final String corrHeaders = InterHelpBase.writeHeader(ci1);

		ci1.end();

		si1.end();

		final ServerInteraction si2 = ServerInteraction.begin();

		setTiming(si2, "si2");

		InterHelpBase.readHeader(corrHeaders, si2);

		si2.setUrl("/app/service2");
		si2.setPeerAddr("localhost");

		si2.end();
	}

	private static void setTiming(Interaction inter, String interName)
	{
		final long now = System.currentTimeMillis();
		final int elapsed = RANDOM.nextInt(10000) + 5;

		System.out.println();
		System.out.println(interName + ".beginTime = " + now + " (" + DATE_FORMATTER.format(new Date(now)) + ").");
		System.out.println(interName + ".endTime   = " + (now + elapsed) + " (" + DATE_FORMATTER.format(new Date(now + elapsed)) + ").");
		System.out.println(interName + ".elapsed   = " + elapsed + ".");

		inter.setBeginTime(now);
		inter.setElapsed(elapsed);
	}
}
