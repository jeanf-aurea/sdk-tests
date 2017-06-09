import util.ManualCorrelator;

import com.actional.lg.interceptor.sdk.ClientInteraction;
import com.actional.lg.interceptor.sdk.DisplayType;
import com.actional.lg.interceptor.sdk.ServerInteraction;


public class InterComponentCommunication extends util.Call
{
	public static void main(String[] args)
	{
		try
		{
			ManualCorrelator.init();

			if (args.length == 0)
			{
				useCaseU1();
				useCaseU2();
				useCaseU3();
				useCaseU4();
				useCaseU5();
			}
			else
			{
				for (int i = 0, iLen = args.length; i < iLen; i++)
				{
					final int index = Integer.parseInt(args[i]);

					switch (index)
					{
						case 1: useCaseU1(); break;
						case 2: useCaseU2(); break;
						case 3: useCaseU3(); break;
						case 4: useCaseU4(); break;
						case 5: useCaseU5(); break;
					}
				}
			}
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	private InterComponentCommunication(String test)
	{
		super(test);
	}

	private static void useCaseU1() throws Exception
	{
		String prefix = "u1";
		InterComponentCommunication test = new InterComponentCommunication(prefix);

		ClientInteraction ci = test.toFakeQueue(null, "agent", "ICC-queue1");
		ci.end();

		ServerInteraction si1 = test.fromFakeQueue(ci, "ICC-queue1", "savvion1", "BP1", "Step1", null);
		si1.setAppType(DisplayType.BPM_ENGINE);
		si1.setSvcType(DisplayType.PROCESS);
		si1.setOpType(DisplayType.STEP);
		si1.end();

		ClientInteraction ci1 = test.toFakeQueue(si1, "ICC-queue2");
		ci1.end();

		ServerInteraction si1a = test.fromFakeQueue(ci1, "ICC-queue2", "apama1", "Complex 1", "Processing 1", null);
		si1a.end();

		ServerInteraction si2 = test.inbound(si1, "savvion1", "BP1", "Step2", null);
		si2.setAppType(DisplayType.BPM_ENGINE);
		si2.setSvcType(DisplayType.PROCESS);
		si2.setOpType(DisplayType.STEP);
		si2.end();

		ClientInteraction ci2 = test.database(si2, "oracle-rac", "Database", "Table", "SELECT");
		ci2.end();

		ServerInteraction si3 = test.inbound(si2, "savvion1", "BP1", "Step3", null);
		si3.setAppType(DisplayType.BPM_ENGINE);
		si3.setSvcType(DisplayType.PROCESS);
		si3.setOpType(DisplayType.STEP);
		si3.end();

		ClientInteraction ci3 = test.toFakeQueue(si3, "ICC-queue4");
		ci3.end();

		ServerInteraction si3a = test.fromFakeQueue(ci3, "ICC-queue4", "apama1", "Complex 2", "Processing 2", null);
		si3a.end();

		ClientInteraction ci3b = test.outbound(si3, "external-system", "App1", "Soap Web Service", "PlaceOrder");
		ci3b.end();
	}

	private static void useCaseU2() throws Exception
	{
		String prefix = "u2";
		InterComponentCommunication test = new InterComponentCommunication(prefix);

		ClientInteraction ci = test.toFakeQueue(null, "agent", "ICC-queue1");
		ci.end();

		ServerInteraction si1 = test.fromFakeQueue(ci, "ICC-queue1", "savvion1", "BP1", "Step1", null);
		si1.end();

		ClientInteraction ci1 = test.toFakeQueue(null, "savvion1", "ICC-queue2");
		ci1.end();

		ServerInteraction si1a = test.fromFakeQueue(ci1, "ICC-queue2", "apama1", "Complex 1", "Processing 1", null);
		si1a.end();

//		ServerInteraction si2 = test.inbound(si1, "savvion1", "BP1", "Step2", null);
//		si2.setPeerType(DisplayType.NO_PEER);
//		si2.end();

//		ClientInteraction ci2 = test.database(si2, "oracle-rac", "Database", "Table", "SELECT");
//		ci2.end();

//		ServerInteraction si3 = test.inbound(si2, "savvion1", "BP1", "Step3", null);
//		si3.setPeerType(DisplayType.NO_PEER);
//		si3.end();

		ClientInteraction ci3 = test.toFakeQueue(null, "savvion1", "ICC-queue4");
		ci3.end();

		ServerInteraction si3a = test.fromFakeQueue(ci3, "ICC-queue4", "apama1", "Complex 2", "Processing 2", null);
		si3a.end();

//		ClientInteraction ci3b = test.outbound(si3, "external-system", "App1", "Soap Web Service", "PlaceOrder");
//		ci3b.end();
	}

	private static void useCaseU3() throws Exception
	{
		String prefix = "u3";
		InterComponentCommunication test = new InterComponentCommunication(prefix);

		ClientInteraction ci = test.toFakeQueue(null, "agent", "ICC-queue1");
		ci.end();

		ServerInteraction si1 = test.fromFakeQueue(ci, "ICC-queue1", "savvion1", "BP1", "Step1", null);
		si1.end();

		ClientInteraction ci1 = test.toFakeQueue(si1, "savvion1", "ICC-queue2");
		ci1.end();

		ServerInteraction si1a = test.fromFakeQueue(ci1, "ICC-queue2", "apama1", "Complex 1", "Processing 1", null);
		si1a.end();

//		ServerInteraction si2 = test.inbound(si1, "savvion1", "BP1", "Step2", null);
//		si2.setPeerType(DisplayType.NO_PEER);
//		si2.end();

//		ClientInteraction ci2 = test.database(si2, "oracle-rac", "Database", "Table", "SELECT");
//		ci2.end();

//		ServerInteraction si3 = test.inbound(si2, "savvion1", "BP1", "Step3", null);
//		si3.setPeerType(DisplayType.NO_PEER);
//		si3.end();

		ClientInteraction ci3 = test.toFakeQueue(null, "savvion1", "ICC-queue4");
		ci3.end();

		ServerInteraction si3a = test.fromFakeQueue(ci3, "ICC-queue4", "apama1", "Complex 2", "Processing 2", null);
		si3a.end();

//		ClientInteraction ci3b = test.outbound(si3, "external-system", "App1", "Soap Web Service", "PlaceOrder");
//		ci3b.end();
	}

	private static void useCaseU4() throws Exception
	{
		String prefix = "u4";
		InterComponentCommunication test = new InterComponentCommunication(prefix);

		ClientInteraction ci = test.toFakeQueue(null, "agent", "ICC-queue1");
		ci.end();

		ServerInteraction si1 = test.fromFakeQueue(ci, "ICC-queue1", "savvion1", "BP1", "Step1", null);
		si1.end();

		ClientInteraction ci1 = test.toFakeQueue(si1, "ICC-queue2");
		ci1.end();

		ServerInteraction si1a = test.fromFakeQueue(ci1, "ICC-queue2", "apama1", "Complex 1", "Processing 1", null);
		si1a.end();

//		ServerInteraction si2 = test.inbound(si1, "savvion1", "BP1", "Step2", null);
//		si2.setPeerType(DisplayType.NO_PEER);
//		si2.end();

//		ClientInteraction ci2 = test.database(si2, "oracle-rac", "Database", "Table", "SELECT");
//		ci2.end();

		ServerInteraction si3 = test.inbound(si1, "savvion1", "BP1", "Step3", null);
		si3.setPeerType(DisplayType.NO_PEER);
		si3.end();

		ClientInteraction ci3 = test.toFakeQueue(si3, "ICC-queue4");
		ci3.end();

		ServerInteraction si3a = test.fromFakeQueue(ci3, "ICC-queue4", "apama1", "Complex 2", "Processing 2", null);
		si3a.end();

//		ClientInteraction ci3b = test.outbound(si3, "external-system", "App1", "Soap Web Service", "PlaceOrder");
//		ci3b.end();
	}

	private static void useCaseU5() throws Exception
	{
		String prefix = "u5";
		InterComponentCommunication test = new InterComponentCommunication(prefix);

		ClientInteraction ci = test.toFakeQueue(null, "agent", "ICC-queue1");
		ci.end();

		ServerInteraction si1 = test.fromFakeQueue(ci, "ICC-queue1", "savvion1", "BP1", "Step1", null);
		si1.end();

		ClientInteraction ci1 = test.toFakeQueue(si1, "ICC-queue2");
		ci1.end();

		ServerInteraction si1a = test.fromFakeQueue(ci1, "ICC-queue2", "apama1", "Complex 1", "Processing 1", null);
		si1a.end();

//		ServerInteraction si2 = test.inbound(si1, "savvion1", "BP1", "Step2", null);
//		si2.setPeerType(DisplayType.NO_PEER);
//		si2.end();

//		ClientInteraction ci2 = test.database(si2, "oracle-rac", "Database", "Table", "SELECT");
//		ci2.end();

		ServerInteraction si3 = test.inbound(si1, "savvion1", "BP1", "Step3", null);
		si3.setPeerType(DisplayType.NO_PEER);
		si3.end();

		ClientInteraction ci3 = test.toFakeQueue(si3, "ICC-queue4");
		ci3.end();

		ServerInteraction si3a = test.fromFakeQueue(ci3, "ICC-queue4", "apama1", "Complex 2", "Processing 2", null);
		si3a.end();

//		ClientInteraction ci3b = test.outbound(si3, "external-system", "App1", "Soap Web Service", "PlaceOrder");
//		ci3b.end();
	}
}
