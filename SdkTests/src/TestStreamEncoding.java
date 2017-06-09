import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.actional.lg.interceptor.internal.DataInputStream;
import com.actional.lg.interceptor.internal.DataOutputStream;


public class TestStreamEncoding
{
	private static final int MIN = -1000000; // Integer.MIN_VALUE;
	private static final int MAX =  1000000; // Integer.MAX_VALUE;

	public static void main(String[] args)
		throws Throwable
	{
		final Data data = new Data();
		final DataOutputStream dos = new DataOutputStream(data.itsOutput);
		final DataInputStream dis = new DataInputStream(data.itsInput);

//		for (int i = MIN + 1; i < MAX; i++)
//		{
//			dos.writeSignedInt(i);
//			if (dis.readSignedInt() != i)
//				throw new Exception("Failed encoding " + i);
//			if (!data.valid())
//				throw new Exception("Failed encoding " + i);
//		}

		for (int i = 0; true; i++)
		{
			dos.writeUnsignedInt(i);
			if (dis.readUnsignedInt() != i)
				throw new Exception("Failed encoding " + i);
			if (!data.valid())
				throw new Exception("Failed encoding " + i);

			if (i == MAX)
				break;
		}
	}
}

class Data
{
	int itsNextByteToRead;
	int itsNextByteToWrite;
	byte[] itsBytes = new byte[16384];
	final InputStream itsInput = new InputStreamImpl();
	final OutputStream itsOutput = new OutputStreamImpl();

	boolean valid()
	{
		final boolean rtrn = itsNextByteToRead == itsNextByteToWrite;

		itsNextByteToRead = 0;
		itsNextByteToWrite = 0;

		return rtrn;
	}

	private class InputStreamImpl extends InputStream
	{
		@Override
		public int read() throws IOException
		{
			if (itsNextByteToRead < itsNextByteToWrite)
			{
				final int b = itsBytes[itsNextByteToRead++];

				if (b < 0)
					return 256 + b;
				else
					return b;
			}

			throw new EOFException();
		}

		@Override
		public int read(byte[] b) throws IOException
		{
			return read(b, 0, b.length);
		}

		@Override
		public int read(byte[] b, int off, int len) throws IOException
		{
			for (int i = off, last = off + len; i < last; i++)
				b[i] = (byte) read();

			return len;
		}

		@Override
		public long skip(long n) throws IOException
		{
			if (n > available())
				throw new EOFException();

			itsNextByteToRead += n;

			return n;
		}

		@Override
		public int available() throws IOException
		{
			return itsNextByteToWrite - itsNextByteToRead;
		}

		@Override
		public void close() throws IOException
		{
		}

		@Override
		public void mark(int readlimit)
		{
			throw new RuntimeException("Not supported");
		}

		@Override
		public void reset() throws IOException
		{
			throw new RuntimeException("Not supported");
		}

		@Override
		public boolean markSupported()
		{
			throw new RuntimeException("Not supported");
		}

	}

	private class OutputStreamImpl extends OutputStream
	{
		@Override
		public void write(int b) throws IOException
		{
			itsBytes[itsNextByteToWrite++] = (byte) b;
		}

		@Override
		public void write(byte[] b) throws IOException
		{
			write(b, 0, b.length);
		}

		@Override
		public void write(byte[] b, int off, int len)
			throws IOException
		{
			for (int i = off, last = off + len; i < last; i++)
				write(b[i]);
		}

		@Override
		public void flush() throws IOException
		{
		}

		@Override
		public void close() throws IOException
		{
		}
	}
}
