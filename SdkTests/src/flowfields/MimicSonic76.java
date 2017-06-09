package flowfields;

import com.actional.lg.interceptor.sdk.ClientInteraction;
import com.actional.lg.interceptor.sdk.ServerInteraction;
import com.actional.lg.interceptor.sdk.SiteStub;

public class MimicSonic76
{
	public static void main(String[] args)
		throws Exception
	{
//		test1();
//		test2();
//		test3();
		test4();
	}

	public static void test1()
		throws Exception
	{
		long now = System.currentTimeMillis();
		ServerInteraction si = ServerInteraction.begin();

		si.setUrl("/a/b/c");
		si.setPeerAddr("localhost");
		si.setMsgField("msgfield1", Long.toString(now), false);
		si.setMsgField("flowfield1", Long.toString(now + 1), false);

		ClientInteraction ci = ClientInteraction.begin();

		ci.setUrl("/x/y/z");
		ci.setPeerAddr("localhost");

		ci.end();

		si.end();
	}

	public static void test2()
		throws Exception
	{
		long now = System.currentTimeMillis();
		ServerInteraction si = ServerInteraction.begin();

		si.setUrl("/a/b/c");
		si.setPeerAddr("localhost");
		si.setMsgField("msgfield1", Long.toString(now), false);
		si.setMsgField("flowfield1", Long.toString(now + 1), false);
		SiteStub stub = si.split();
		si.end();

		si = ServerInteraction.begin(stub);
		ClientInteraction ci = ClientInteraction.begin();

		ci.setUrl("/x/y/z");
		ci.setPeerAddr("localhost");

		ci.end();

		si.end();
	}

	public static void test3()
		throws Exception
	{
		long now = System.currentTimeMillis();
		ServerInteraction si = ServerInteraction.begin();

		si.setUrl("/a/b/c");
		si.setPeerAddr("localhost");
		si.setMsgField("msgfield1", Long.toString(now), false);
		si.setMsgField("flowfield1", Long.toString(now + 1), false);

		ClientInteraction ci = ClientInteraction.begin();

		ci.setUrl("/x/y/z");
		ci.setPeerAddr("localhost");
		ci.setMsgField("msgfield1", Long.toString(now), false);
		ci.setMsgField("msgfield2", Long.toString(now + 2), false);

		ci.end();

		si.end();
	}

	public static void test4()
		throws Exception
	{
		long now = System.currentTimeMillis();
		ServerInteraction si = ServerInteraction.begin();

		si.setUrl("/a/b/c");
		si.setPeerAddr("localhost");
		si.setMsgField("msgfield1", Long.toString(now), false);
		si.setMsgField("flowfield1", Long.toString(now + 1), false);
		SiteStub stub = si.split();
		si.end();

		si = ServerInteraction.begin(stub);
		ClientInteraction ci = ClientInteraction.begin();

		ci.setUrl("/x/y/z");
		ci.setPeerAddr("localhost");
		ci.setMsgField("msgfield1", Long.toString(now), false);
		ci.setMsgField("msgfield2", Long.toString(now + 2), false);

		ci.end();

		si.end();
	}
}
