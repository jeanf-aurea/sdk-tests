package util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Misc
{
	public static Object deepClone(Object o) throws IOException, ClassNotFoundException
	{
		return serializationFilter(o);
	}

	public static Object serializationFilter(Object o) throws IOException, ClassNotFoundException
	{
		if (o == null)
			return null;

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		synchronized(baos)
		{
			ObjectOutputStream oos = new ObjectOutputStream(baos);

			oos.writeObject(o);
			oos.flush();
			oos.close();
		}

		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		ObjectInputStream ois = new ObjectInputStream(bais);

		synchronized(bais)
		{
			return ois.readObject();
		}
	}
}
