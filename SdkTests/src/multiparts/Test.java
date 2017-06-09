package multiparts;

import java.io.IOException;
import java.util.Collections;

import com.actional.GeneralUtil;
import com.actional.lg.interceptor.sdk.ClientInteraction;
import com.actional.lg.interceptor.sdk.DisplayType;
import com.actional.lg.interceptor.sdk.FlowStub;
import com.actional.lg.interceptor.sdk.Interaction;
import com.actional.lg.interceptor.sdk.InteractionStub;
import com.actional.lg.interceptor.sdk.ObjectXmlInfoSet;
import com.actional.lg.interceptor.sdk.Part;
import com.actional.lg.interceptor.sdk.PartBytes;
import com.actional.lg.interceptor.sdk.ServerInteraction;

/**
 * Unit tests for multi-parts support. <br>
 * <ul>
 * <li>Run a first time from a virgin AMS (i.e. sites not being discovered)</li>
 * <li></li>
 * <li></li>
 * <li></li>
 * <li></li>
 * <li></li>
 * <li></li>
 * <li></li>
 * <li></li>
 * <li></li>
 * <li></li>
 * <li></li>
 * <li></li>
 * <li></li>
 * <li></li>
 * <li></li>
 * <li></li>
 * <li></li>
 * <li></li>
 * <li></li>
 * <li></li>
 * <li></li>
 * <li></li>
 * <li></li>
 * </ul>
 */
public abstract class Test
{
	public static void main(final String[] args) throws Exception
	{
		if (args.length == 0)
		{
			for (int i = 0, iLen = ALL_TESTS.length; i < iLen; i++)
			{
				ALL_TESTS[i].execute(i);
			}
		}
		else
		{
			for (int i = 0, iLen = args.length; i < iLen; i++)
			{
				final int nb;

				try
				{
					nb = Integer.parseInt(args[i]);
				}
				catch (final Throwable e)
				{
					System.err.println("Invalid integer argument: " + args[i] +
						" (" + ALL_TESTS.length + " tests available).");
					continue;
				}

				if (nb >= ALL_TESTS.length)
				{
					System.err.println("Invalid integer argument: " + args[i] +
						" (" + ALL_TESTS.length + " tests available).");
					continue;
				}

				ALL_TESTS[nb].execute(nb);
			}
		}
	}

	private void execute(final int index)
	{
		System.out.println("================= Test#" + index + " =================");

		try
		{
			run(index);
			System.out.println("OK");
		}
		catch (final Throwable e)
		{
			e.printStackTrace(System.out);
		}
	}

	protected abstract void run(int index) throws Exception;

	protected ServerInteraction createSI(final int index)
	{
		final ServerInteraction si = ServerInteraction.begin();
		si.setUrl("/Multi-Parts Tests/Test" + index);
		// si.setPeerType(DisplayType.NO_PEER);

		return si;
	}

	protected ServerInteraction testSerialization(final ServerInteraction si)
		throws IOException
	{
		final InteractionStub iStub1 = si.detach();
		final byte[] bytes = iStub1.serializeAsBytes();
		final InteractionStub iStub2 = (InteractionStub) FlowStub.deserialize(bytes);

		return (ServerInteraction) Interaction.attach(iStub2);
	}

	private static byte[] utf8(final String str)
	{
		try
		{
			return str.getBytes("UTF-8");
		}
		catch (final Throwable e)
		{
			return null;
		}
	}

	private static final byte[]
			XML1 = utf8("<xml1><value>value1</value></xml1>"),
			XML2 = utf8("<xml2><value>value2</value></xml2>"),
			XML3 = utf8("<xml3><value>value3</value></xml3>"),
			XML4 = utf8("<xml4><value>value4</value></xml4>"),
			XML5 = utf8("<xml5><value>value5</value></xml5>"),
			XML6 = utf8("<xml6><value>value6</value></xml6>"),
			XML7 = utf8("<xml7><value>value7</value></xml7>"),
			TEXT1 = utf8("text1"),
			TEXT2 = utf8("text2"),
			TEXT3 = utf8("text3"),
			TEXT4 = utf8("text4"),
			TEXT5 = utf8("text5"),
			TEXT6 = utf8("text6"),
			TEXT7 = utf8("text7"),
			TEXT8 = utf8("text1"),
			BIN1 = {  1,  2,  3,  4,  5 },
			BIN2 = {  6,  7,  8,  9, 10 },
			BIN3 = { 11, 12, 13, 14, 15 },
			BIN4 = { 16, 17, 18, 19, 20 },
			BIN5 = { 21, 22, 23, 24, 25 }
			;

	private static Part newPart(final byte[] bytes, final String ctype, final String cid, final int index)
	{
		final PartBytes part = new PartBytes(bytes, ctype, null, cid);

		part.setMetaData(Collections.<String, Object>singletonMap("index", new Integer(index)));

		return part;
	}

	private static final Test[] ALL_TESTS =
	{
		new Test() // 0
		{
			protected void run(final int index) throws Exception
			{
				final ServerInteraction si = createSI(index);

				si.setPayload(XML1);
				si.setPayload(XML2); // This will automatically call requestAnalyzed.

				si.end();
			}
		}
		,
		new Test() // 1
		{
			protected void run(final int index) throws Exception
			{
				final ServerInteraction si = createSI(index);

				si.setPayload(XML1); // Payload will be ignored because of combo event

				si.end();
			}
		}
		,
		new Test() // 2
		{
			protected void run(final int index) throws Exception
			{
				final ServerInteraction si = createSI(index);

				si.setPayload(XML1);
				si.addRequestPart(newPart(XML2, "text/xml", "part2", 2));
				si.addRequestPart(newPart(BIN1, "octet/stream", "part3", 3));
				si.addRequestPart(newPart(TEXT1, "text/plain", "part4", 4));
				si.addRequestPart(newPart(XML3, "text/xml", "part5", 5));
				si.requestAnalyzed();

				si.end();
			}
		}
		,
		new Test() // 3
		{
			protected void run(final int index) throws Exception
			{
				final ServerInteraction si = createSI(index);

				si.setPayload(XML1);
				si.setPayload(XML2); // This will automatically call requestAnalyzed.
				try
				{
					si.addRequestPart(newPart(XML3, "text/xml", "part3", 3));
					throw new Exception("addRequestPart() should have rejected the part.");
				}
				catch (final RuntimeException e)
				{
					// That's normal; requestAnalyzed() has been called
					// by the second call to setPayload() so it's too
					// late to send request parts.
				}
				finally
				{
					si.end();
				}
			}
		}
		,
		new Test() // 4
		{
			protected void run(final int index) throws Exception
			{
				final ServerInteraction si = createSI(index);

				si.setPayload(XML1);
				si.setPayload(XML2); // This will automatically call requestAnalyzed.
				si.addReplyPart(newPart(XML3, "text/xml", "part3", 3));
				si.addReplyPart(newPart(TEXT1, "text/plain", "part4", 4));
				si.addReplyPart(newPart(BIN1, "octet/stream", "part5", 5));
				si.addReplyPart(newPart(XML4, "text/xml", "part6", 6));

				si.end();
			}
		}
		,
		new Test() // 5
		{
			protected void run(final int index) throws Exception
			{
				final ServerInteraction si = createSI(index);

				si.setPayload(XML1);
				si.addRequestPart(newPart(XML2, "text/xml", "part2", 2));
				si.filterRequestPartsNow();
				si.addRequestPart(newPart(BIN1, "octet/stream", "part3", 3));
				si.addRequestPart(newPart(TEXT1, "text/plain", "part4", 4));
				si.addRequestPart(newPart(XML3, "text/xml", "part5", 5));
				si.requestAnalyzed();

				si.end();
			}
		}
		,
		new Test() // 6
		{
			protected void run(final int index) throws Exception
			{
				final ServerInteraction si = createSI(index);

				si.addRequestPart(newPart(XML1, "text/xml", "part1", 1));
				si.addRequestPart(newPart(XML2, "text/xml", "part2", 2));
				si.filterRequestPartsNow();
				si.addRequestPart(newPart(BIN1, "octet/stream", "part3", 3));
				si.addRequestPart(newPart(TEXT1, "text/plain", "part4", 4));
				si.addRequestPart(newPart(XML3, "text/xml", "part5", 5));
				si.requestAnalyzed();

				si.end();
			}
		}
		,
		new Test() // 7
		{
			protected void run(final int index) throws Exception
			{
				final ServerInteraction si = createSI(index);

				si.addRequestPart(newPart(TEXT1, "text/plain", "part1", 1));
				si.addRequestPart(newPart(XML1, "text/xml", "part2", 2));
				si.setPayload(XML2);
				si.filterRequestPartsNow();
				si.addRequestPart(newPart(BIN1, "octet/stream", "part4", 4));
				si.addRequestPart(newPart(TEXT1, "text/plain", "part5", 5));
				si.addRequestPart(newPart(XML3, "text/xml", "part6", 6));
				si.requestAnalyzed();

				si.end();
			}
		}
		,
		new Test() // 8
		{
			protected void run(final int index) throws Exception
			{
				final ServerInteraction si = createSI(index);

				si.addRequestPart(newPart(TEXT1, "text/plain", "part1", 1));
				si.addRequestPart(newPart(XML1, "text/xml", "part2", 2));
				si.filterRequestPartsNow();
				si.setPayload(XML2);
				si.addRequestPart(newPart(BIN1, "octet/stream", "part4", 4));
				si.addRequestPart(newPart(TEXT1, "text/plain", "part5", 5));
				si.addRequestPart(newPart(XML3, "text/xml", "part6", 6));
				si.requestAnalyzed();

				si.end();
			}
		}
		,
		new Test() // 9
		{
			protected void run(final int index) throws Exception
			{
				ServerInteraction si = createSI(index);

				si.addRequestPart(newPart(TEXT1, "text/plain", "part1", 1));
				si.addRequestPart(newPart(XML1, "text/xml", "part2", 2));
				si = testSerialization(si);
				si.filterRequestPartsNow();
				si.setPayload(XML2);
				si.addRequestPart(newPart(BIN1, "octet/stream", "part4", 4));
				si = testSerialization(si);
				si.addRequestPart(newPart(TEXT1, "text/plain", "part5", 5));
				si.addRequestPart(newPart(XML3, "text/xml", "part6", 6));
				si.requestAnalyzed();

				si.end();
			}
		}
		,
		new Test() // 10
		{
			protected void run(final int index) throws Exception
			{
				ServerInteraction si = createSI(index);

				si.addRequestPart(newPart(TEXT1, "text/plain", "part1", 1));
				si.addRequestPart(newPart(XML1, "text/xml", "part2", 2));
				si = testSerialization(si);
				si.filterRequestPartsNow();
				si.setPayload(XML2);
				// The following is testing that setPayload has precedence
				// over the IXmlInfoSet
				si.setXmlInfoSetPayload(ObjectXmlInfoSet.createFieldsRequest("MyObject", "MyMethod", new Object[] { new Integer(3) }));
				si.addRequestPart(newPart(BIN1, "octet/stream", "part4", 4));
				si = testSerialization(si);
				si.addRequestPart(newPart(TEXT1, "text/plain", "part5", 5));
				si.addRequestPart(newPart(XML3, "text/xml", "part6", 6));
				si.requestAnalyzed();

				si.end();
			}
		}
		,
		new Test() // 11
		{
			protected void run(final int index) throws Exception
			{
				ServerInteraction si = createSI(index);

				si.addRequestPart(newPart(TEXT1, "text/plain", "part1", 1));
				si.addRequestPart(newPart(XML1, "text/xml", "part2", 2));
				si = testSerialization(si);
				si.filterRequestPartsNow();
				si.setXmlInfoSetPayload(ObjectXmlInfoSet.createFieldsRequest("MyObject", "MyMethod", new Object[] { new Integer(3) }));
				si.addRequestPart(newPart(BIN1, "octet/stream", "part4", 4));
				si = testSerialization(si);
				si.addRequestPart(newPart(TEXT1, "text/plain", "part5", 5));
				si.addRequestPart(newPart(XML3, "text/xml", "part6", 6));
				si.requestAnalyzed();

				si.end();
			}
		}
		,
		new Test() // 12
		{
			protected void run(final int index) throws Exception
			{
				final ServerInteraction si = createSI(index);

				si.getIncludeMsg();

				si.setPayload(XML1);
				si.requestAnalyzed();
				si.end();
			}
		}
		,
		new Test() // 13
		{
			protected void run(final int index) throws Exception
			{
				final ServerInteraction si = createSI(index);

				si.setPayload(XML1);
				si.requestAnalyzed();
				si.end();
			}
		}
		,
		new Test() // 14
		{
			protected void run(final int index) throws Exception
			{
				final ServerInteraction si = createSI(index);

				si.requestAnalyzed();
				si.setPayload(XML1);
				si.end();
			}
		}
		,
		new Test() // 15
		{
			protected void run(final int index) throws Exception
			{
				final ServerInteraction si = createSI(index);

				si.addRequestPart(newPart(XML1, "text/xml", "part1", 1));
				si.addRequestPart(newPart(XML2, "text/xml", "part2", 2));
				si.filterRequestPartsNow();
				si.addRequestPart(newPart(XML3, "text/xml", "part3", 3));
				si.addRequestPart(newPart(BIN1, "octet/stream", "part4", 4));
				si.addRequestPart(newPart(TEXT1, "text/plain", "part5", 5));
				si.addRequestPart(newPart(XML3, "text/xml", "part6", 6));
				si.requestAnalyzed();
				si.addReplyPart(newPart(BIN1, "octet/stream", "part1", 1));
				si.addReplyPart(newPart(XML1, "text/xml", "part2", 2));
				si.addReplyPart(newPart(XML2, "text/xml", "part3", 3));
				si.addReplyPart(newPart(TEXT1, "text/plain", "part4", 4));

				si.end();
			}
		}
		,
		new Test() // 16
		{
			protected void run(final int index) throws Exception
			{
				final String rfkey = Long.toString(System.currentTimeMillis());
				final ServerInteraction si = ServerInteraction.begin();

				si.setUrl("/Multi-Parts Tests/Test" + index);
				si.setPeerType(DisplayType.NO_PEER);
				si.setSelfAddr(GeneralUtil.DEMO_PREFIX + "ManagedClient");

				final ClientInteraction ci = ClientInteraction.begin();

				ci.setUrl("/a/b");
				ci.setSelfAddr(GeneralUtil.DEMO_PREFIX + "ManagedClient");
				ci.setPeerAddr(GeneralUtil.DEMO_PREFIX + "LeavingManagedWorld");
				ci.setMsgField("rfkey", rfkey, true);
				ci.end();

				si.end();

				final ServerInteraction si2 = ServerInteraction.begin();

				si2.setPeerAddr(GeneralUtil.DEMO_PREFIX + "BeforeEnteringManagedWorld");
				si2.setSelfAddr(GeneralUtil.DEMO_PREFIX + "ManagedNode");
				si2.setUrl("/ManagedApp/ManagedService");
				si2.setMsgField("rfkey", rfkey, true);
				si2.addRequestPart(newPart(XML1, "text/xml", "part1", 1));
				si2.addRequestPart(newPart(XML2, "text/xml", "part2", 2));

				si2.requestAnalyzed();

				si2.end();
			}
		}
	};
}