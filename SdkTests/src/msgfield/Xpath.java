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

package msgfield;

import static msgfield.Xpath.Animal.CAT;
import static msgfield.Xpath.Animal.DOG;
import static msgfield.Xpath.Animal.FROG;
import static msgfield.Xpath.Animal.ZEBRA;
import static msgfield.Xpath.Payload.CAT_NONS;
import static msgfield.Xpath.Payload.MULTI_ZEBRA_NONS;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.UndeclaredThrowableException;

import com.actional.lg.interceptor.internal.simulator.Simulator;
import com.actional.lg.interceptor.sdk.ClientInteraction;
import com.actional.lg.interceptor.sdk.Interaction;
import com.actional.lg.interceptor.sdk.ServerInteraction;

/** <!-- ========================================================================================================== -->
 *
 *
 * @lastrev fixXXXXX - new class
 * <!-- -------------------------------------------------------------------------------------------------------- --> */

public class Xpath extends Simulator
{
	public static void main(String[] args)
		throws Exception
	{
		new Xpath().generate();

		Thread.sleep(1000L);
	}

	private Xpath()
	{
		super("xpath");
	}

	protected void generate()
	{
		final String node1 = "node1";
		final String node2 = "node2";
		final String itsL2Name = "l2name";
		final String itsL3Name = "l3name";

		final ServerInteraction si = inbound("client", node1, itsL2Name, itsL3Name, null);

		process(si);

		final ClientInteraction ci = outbound(si, node2, itsL2Name, itsL3Name, null);

		process(ci);

		ci.end();

		final ServerInteraction si2 = inbound(ci, node2, itsL2Name, itsL3Name, null);

		si2.end();

		si.end();
	}

	protected void process(final Interaction inter)
	{
		inter.setPayload(CAT_NONS.asBytes());
		inter.requestAnalyzed();
		inter.setPayload(MULTI_ZEBRA_NONS.asBytes());
	}

	enum Animal
	{
		CAT,
		DOG,
		FROG,

		/**
		 * Zebra-based payloads should be reserved for the reply agent events.
		 * Its purpose is detecting if message fields get inadvertently picked up for the reply.
		 * In other words, if your test is expecting to pick up a value from the request only and
		 * that "zebra", then you get a pretty immediate hint that something went wrong.
		 */
		ZEBRA;
	}

	enum Payload
	{
		CAT_NONS("<animal>" + CAT.name() + "</animal>"),
		CAT_NS("<ns:animal xmlns:ns='abc'>" + CAT.name() + "</ns:animal>"),

		DOG_NONS("<animal>" + DOG.name() + "</animal>"),
		DOG_NS("<ns:animal xmlns:ns='abc'>" + DOG.name() + "</ns:animal>"),

		FROG_NONS("<animal>" + FROG.name() + "</animal>"),
		FROG_NS("<ns:animal xmlns:ns='abc'>" + FROG.name() + "</ns:animal>"),

		ZEBRA_NONS("<animal>" + ZEBRA.name() + "</animal>"),
		ZEBRA_NS("<ns:animal xmlns:ns='abc'>" + ZEBRA.name() + "</ns:animal>"),

		MULTI_NONS("<animals><animal>" + CAT.name() + "</animal><animal>" + DOG.name() + "</animal><animal>" + FROG.name() + "</animal></animals>"),
		MULTI_NS("<ns:animals xmlns:ns='abc'><ns:animal>" + CAT.name() + "</ns:animal><ns:animal>" + DOG.name() + "</ns:animal><ns:animal>" + FROG.name() + "</ns:animal></ns:animals>"),

		MULTI_ZEBRA_NONS("<animals><animal>" + ZEBRA.name() + "</animal></animals>"),
		MULTI_ZEBRA_NS("<ns:animals xmlns:ns='abc'><ns:animal>" + ZEBRA.name() + "</ns:animal></ns:animals>");

		private final String itsPayloadAsStr;
		private final byte[] itsPayloadAsBytes;

		private Payload(final String str)
		{
			itsPayloadAsStr = str;
			itsPayloadAsBytes = asUtf8(str);
		}

		public String asStr()
		{
			return itsPayloadAsStr;
		}

		public byte[] asBytes()
		{
			return itsPayloadAsBytes;
		}

		private static byte[] asUtf8(final String xml)
		{
			try
			{
				return xml.getBytes("UTF-8");
			}
			catch (final UnsupportedEncodingException e)
			{
				throw new UndeclaredThrowableException(e);
			}
		}
	}
}
