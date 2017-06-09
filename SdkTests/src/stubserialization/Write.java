package stubserialization;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.OutputStream;

import com.actional.lg.interceptor.sdk.InteractionStub;
import com.actional.lg.interceptor.sdk.ServerInteraction;

public class Write
{
	public static void main(String[] args)
	{
		try
		{
			ServerInteraction si = ServerInteraction.begin();

			si.setUrl("/a/b/c");

			InteractionStub is = si.detach();

			OutputStream os;

			if (args.length > 0)
				os = new FileOutputStream(args[0]);
			else
				os = System.out;

			try
			{
				ObjectOutputStream oos = new ObjectOutputStream(os);

				oos.writeObject(is);
				oos.close();
			}
			finally
			{
				os.close();
			}
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			System.exit(1);
		}

		try
		{
			Test4 is = new Test4();

			// is.itsTest1String = "Test1Value";
			is.itsTest2String = "Test2Value";

			OutputStream os;

			if (args.length > 0)
				os = new FileOutputStream(args[0]);
			else
				os = System.out;

			try
			{
				ObjectOutputStream oos = new ObjectOutputStream(os);

				oos.writeObject(is);
				oos.close();
			}
			finally
			{
				os.close();
			}

			ObjectInputStream ois = new ObjectInputStream(new java.io.FileInputStream(args[0]));

			is = (Test4) ois.readObject();

			System.out.println("==> " + is.itsTest2String);
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}
}

class Test1 implements java.io.Serializable
{
	private final static long serialVersionUID = 0xfafafafafafafafaL;

	String itsTest1String;
}

class Test2 extends Test1 implements java.io.Externalizable
{
	private final static long serialVersionUID = 0xfbfbfbfbfbfbfbfbL;

	String itsTest2String;

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
	{
		itsTest2String = in.readUTF();
	}

	public void writeExternal(ObjectOutput out) throws IOException
	{
		out.writeUTF(itsTest2String);
	}
}

class Test3 extends Test1
{
	private final static long serialVersionUID = 0xfcfcfcfcfcfcfcfcL;

	String itsTest2String;
}

class Test4 implements java.io.Externalizable
{
	private final static long serialVersionUID = 0xfbfbfbfbfbfbfbfbL; // <== this is put in the stream even if only Externalizable

	private transient Object itsDeserializedStub;

	String itsTest2String;

	public Test4() // <== required by Externalizable to be public
	{
	}

	protected Object readResolve() throws ObjectStreamException
	{
		return itsDeserializedStub;
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
	{
		Test4 t = new Test4();
		t.itsTest2String = in.readUTF();
		itsDeserializedStub = t;
	}

	public void writeExternal(ObjectOutput out) throws IOException
	{
		out.writeUTF(itsTest2String);
	}
}