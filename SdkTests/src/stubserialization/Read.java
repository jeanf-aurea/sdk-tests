package stubserialization;

import java.io.ObjectInputStream;

import com.actional.lg.interceptor.sdk.InteractionStub;

public class Read
{
	public static void main(String[] args)
	{
		try
		{
			ObjectInputStream ois = new ObjectInputStream(System.in);

			InteractionStub is = (InteractionStub) ois.readObject();

			System.out.println(is.getUrl());
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}
}
