import com.actional.lg.interceptor.sdk.Interaction;
import com.actional.lg.interceptor.sdk.InteractionStub;
import com.actional.lg.interceptor.sdk.ServerInteraction;


public class TestFlowBrowserWithDupInteractionIDs
{
	public static void main(String[] args)
	{
		test1();
		test2();
		test3();
		test4();
		test5();
		test6();
		test7();
	}

	public static void test1()
	{
		ServerInteraction si = ServerInteraction.begin();

		si.setUrl("/a/b/c");
		si.setPeerAddr("localhost");
		si.getFlowID();

		InteractionStub iStub = si.detach();

		si = (ServerInteraction) Interaction.attach(iStub);

		si.end();

		si = (ServerInteraction) Interaction.attach(iStub);

		si.end();
	}

	public static void test2()
	{
		ServerInteraction si = ServerInteraction.begin();

		si.setUrl("/a/b/c");
		si.setPeerAddr("localhost");
		si.getFlowID();

		InteractionStub iStub = si.detach();

		si = (ServerInteraction) Interaction.attach(iStub);

		si.requestAnalyzed();

		si.end();

		si = (ServerInteraction) Interaction.attach(iStub);

		si.requestAnalyzed();

		si.end();
	}

	public static void test3()
	{
		ServerInteraction si = ServerInteraction.begin();

		si.setUrl("/a/b/c");
		si.setPeerAddr("localhost");
		si.getFlowID();

		InteractionStub iStub = si.detach();

		si = (ServerInteraction) Interaction.attach(iStub);

		si.end();

		si = (ServerInteraction) Interaction.attach(iStub);

		si.requestAnalyzed();

		si.end();
	}

	public static void test4()
	{
		ServerInteraction si = ServerInteraction.begin();

		si.setUrl("/a/b/c");
		si.setPeerAddr("localhost");
		si.getFlowID();

		InteractionStub iStub = si.detach();

		si = (ServerInteraction) Interaction.attach(iStub);

		si.requestAnalyzed();

		si.end();

		si = (ServerInteraction) Interaction.attach(iStub);

		si.end();
	}

	public static void test5()
	{
		ServerInteraction si = ServerInteraction.begin();

		si.setUrl("/a/b/c");
		si.setPeerAddr("localhost");
		si.getFlowID();

		InteractionStub iStub = si.detach();

		si = (ServerInteraction) Interaction.attach(iStub);

		si.requestAnalyzed();

		si.end();

		si = (ServerInteraction) Interaction.attach(iStub);

		si.requestAnalyzed();

		si.end();
	}

	public static void test6()
	{
		ServerInteraction si = ServerInteraction.begin();

		si.setUrl("/a/b/c");
		si.setPeerAddr("localhost");
		si.getFlowID();

		si.requestAnalyzed();

		InteractionStub iStub = si.detach();

		si = (ServerInteraction) Interaction.attach(iStub);

		si.end();

		si = (ServerInteraction) Interaction.attach(iStub);

		si.requestAnalyzed();

		si.end();
	}

	public static void test7()
	{
		ServerInteraction si = ServerInteraction.begin();

		si.setUrl("/a/b/c");
		si.setPeerAddr("localhost");
		si.getFlowID();

		si.requestAnalyzed();

		InteractionStub iStub = si.detach();

		si = (ServerInteraction) Interaction.attach(iStub);

		si.end();

		si = (ServerInteraction) Interaction.attach(iStub);

		si.end();
	}
}
