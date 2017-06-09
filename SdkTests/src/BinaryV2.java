import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UTFDataFormatException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.MessageDigest;

public class BinaryV2
{
	private static final byte[] V2_STRING = new byte[] { 'A', 'c', 't', 'i', 'o', 'n', 'a', 'l', '-', 'v', '2', 0 };

	/**
	 * @param args
	 */
	public static void main(String[] args)
		throws Throwable
	{
		final Socket socket = new Socket(InetAddress.getLocalHost(), 4848);

		try
		{
			final String password = args.length == 0 ? "password" : args[0];
			final OutputStream os = new BufferedOutputStream(socket.getOutputStream());
			final HexadecimalOutput hexos = new HexadecimalOutput();
			final DataOutputStream dos = new DataOutputStream(new TeeOutputStream(os, hexos));
			final InputStream is = new BufferedInputStream(socket.getInputStream());
			final DataInputStream dis = new DataInputStream(is);

			os.write(V2_STRING);
			os.flush();

			final int connectionIdLen = dis.readInt();
			final byte[] connectionId = new byte[connectionIdLen];

			dis.readFully(connectionId);

			System.out.print("ConnID [" + Integer.toString(connectionId.length) + "] ");
			for (byte b : connectionId)
				System.out.print(Integer.toHexString(b) + ' ');
			System.out.println();

			final MessageDigest md = MessageDigest.getInstance("SHA");
			md.update(connectionId);
			final byte[] passwordHash = md.digest(password.getBytes("UTF-8"));

			dos.writeUTF("jeanf");
			dos.write(0x01); // 0x01 = SHA, 0x02 = MD5
			dos.writeCompactUnsignedInt(passwordHash.length);
			dos.write(passwordHash);
			dos.write(0); // Interceptor could return a session ID so that analyzer can know which connections are related
			// dos.write(sessionIdAsBytes);
			// dos.write(0); // 0 == no compression, 1 == gzip interceptor to agent, 2 == gzip agent to interceptor
			dos.flush();
			System.out.println(hexos.toString());

			final int auth = is.read();

			if (auth == 1)
			{
				System.out.println("Authenticated.");
			}
			else
			{
				assert auth < 0;
				System.out.println("Access refused. (" + auth + ")");
			}
		}
		finally
		{
			socket.close();
		}
	}
}


class DataInputStream extends InputStream
{
	private static final int TEMP_BUFFER_SIZE =
		Integer.getInteger("com.actional.io.DataInputStream.tempBufSize", 256).intValue();

	private char[] itsTempCharBuffer;
	private byte[] itsTempIntBuffer;
	private final byte[] itsLongTempBuffer;
	private final InputStream itsInputStream;

	/** <!-- ================================================================================================== -->
	 * @lastrev fix32489 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public DataInputStream(InputStream is)
	{
		itsInputStream = is;
		itsLongTempBuffer = new byte[8];
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Implementation of com.actional.io.DataInput
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** <!-- ================================================================================================== -->
	 * @lastrev fix35460 - wrong buffer being referenced
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public String readUTF() throws IOException
	{
		int utfLen = readCompactUnsignedInt();

		if (utfLen < 0)
			return null;

		if (utfLen == 0)
			return "";

		char[] str;
		byte[] utfBytes;

		if (utfLen > TEMP_BUFFER_SIZE)
		{
			str = new char[utfLen]; // We may be allocated too much, but that's okay.
			utfBytes = new byte[utfLen];
		}
		else
		{
			if (itsTempIntBuffer == null)
			{
				itsTempIntBuffer = new byte[TEMP_BUFFER_SIZE];
				itsTempCharBuffer = new char[TEMP_BUFFER_SIZE];
			}

			str = itsTempCharBuffer;
			utfBytes = itsTempIntBuffer;
		}

		int strIndex = 0;
		int char1, char2, char3;
		int utfIndex = 0;

		readFully(utfBytes, 0, utfLen);

		while (utfIndex < utfLen)
		{
			char1 = utfBytes[utfIndex] & 0xff;
			switch (char1 >> 4)
			{
				case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7:
				{
					/* 0xxxxxxx*/
					utfIndex++;
					str[strIndex++] = (char) char1;
					break;
				}

				case 12: case 13:
				{
					/* 110x xxxx   10xx xxxx*/
					utfIndex += 2;
					if (utfIndex > utfLen)
						throw new UTFDataFormatException();

					char2 = utfBytes[utfIndex-1];
					if ((char2 & 0xC0) != 0x80)
						throw new UTFDataFormatException();

					char c = (char) (((char1 & 0x1F) << 6) | (char2 & 0x3F));
					str[strIndex++] = c;
					break;
				}

				case 14:
				{
					/* 1110 xxxx  10xx xxxx  10xx xxxx */
					utfIndex += 3;
					if (utfIndex > utfLen)
						throw new UTFDataFormatException();

					char2 = utfBytes[utfIndex-2];
					char3 = utfBytes[utfIndex-1];
					if (((char2 & 0xC0) != 0x80) || ((char3 & 0xC0) != 0x80))
						throw new UTFDataFormatException();

					char c = (char) (((char1 & 0x0F) << 12) | ((char2 & 0x3F) << 6)  |
							((char3 & 0x3F) << 0));
					str[strIndex++] = c;
					break;
				}

				default:
				{
					/* 10xx xxxx,  1111 xxxx */
					throw new UTFDataFormatException();
				}
			}
		}

		// The number of chars produced may be less than utflen
		return new String(str, 0, strIndex);
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix32489 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public boolean readBoolean() throws IOException
	{
		int b = itsInputStream.read();

		if (b < 0)
			throw new EOFException();

		return (b != 0);
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix32489 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public byte readByte() throws IOException
	{
		int b = itsInputStream.read();

		if (b < 0)
			throw new EOFException();

		return (byte) b;
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix32489 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public short readShort() throws IOException
	{
		int ch1 = itsInputStream.read();
		int ch2 = itsInputStream.read();

		if ((ch1 | ch2) < 0)
			throw new EOFException();

		return (short)((ch1 << 8) + (ch2 << 0));
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix32489 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public int readInt() throws IOException
	{
		int ch1 = itsInputStream.read();
		int ch2 = itsInputStream.read();
		int ch3 = itsInputStream.read();
		int ch4 = itsInputStream.read();

		if ((ch1 | ch2 | ch3 | ch4) < 0)
			throw new EOFException();

		return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix32489 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public long readLong() throws IOException
	{
		byte[] readBuffer = itsLongTempBuffer;

		readFully(readBuffer, 0, 8);
		return (((long)readBuffer[0] << 56) +
			((long)(readBuffer[1] & 255) << 48) +
			((long)(readBuffer[2] & 255) << 40) +
			((long)(readBuffer[3] & 255) << 32) +
			((long)(readBuffer[4] & 255) << 24) +
			((readBuffer[5] & 255) << 16) +
			((readBuffer[6] & 255) <<  8) +
			((readBuffer[7] & 255) <<  0));
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix32489 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public float readFloat() throws IOException
	{
		return Float.intBitsToFloat(readInt());
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix32489 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public double readDouble() throws IOException
	{
		return Double.longBitsToDouble(readLong());
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix32489 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public char readChar() throws IOException
	{
		int ch1 = itsInputStream.read();
		int ch2 = itsInputStream.read();

		if ((ch1 | ch2) < 0)
			throw new EOFException();

		return (char)((ch1 << 8) + (ch2 << 0));
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix32489 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public Boolean readBoxedBoolean() throws IOException
	{
		byte b = readByte();

		switch (b)
		{
		case 0: return Boolean.FALSE;
		case 1: return Boolean.TRUE;
		case 2: return null;
		default: throw new IOException("Unexpected boxed boolean value " + b + ".");
		}
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix32489 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public Byte readBoxedByte() throws IOException
	{
		byte b = readByte();

		if (b != 0)
			return null;

		return new Byte(readByte());
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix32489 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public Short readBoxedShort() throws IOException
	{
		byte b = readByte();

		if (b != 0)
			return null;

		return new Short(readShort());
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix32489 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public Integer readBoxedInt() throws IOException
	{
		byte b = readByte();

		if (b != 0)
			return null;

		return new Integer(readInt());
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix32489 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public Long readBoxedLong() throws IOException
	{
		byte b = readByte();

		if (b != 0)
			return null;

		return new Long(readLong());
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix32489 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public Float readBoxedFloat() throws IOException
	{
		byte b = readByte();

		if (b != 0)
			return null;

		return new Float(Float.intBitsToFloat(readInt()));
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix32489 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public Double readBoxedDouble() throws IOException
	{
		byte b = readByte();

		if (b != 0)
			return null;

		return new Double(Double.longBitsToDouble(readLong()));
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix32489 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public Character readBoxedChar() throws IOException
	{
		byte b = readByte();

		if (b != 0)
			return null;

		return new Character(readChar());
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix32489 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public void readFully(byte[] b, int offset, int len) throws IOException
	{
		if (len < 0)
			throw new IndexOutOfBoundsException();

		while (len > 0)
		{
			int count = itsInputStream.read(b, offset, len);

			if (count < 0)
				throw new EOFException();

			offset += count;
			len -= count;
		}
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix32489 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public void readFully(byte[] b) throws IOException
	{
		readFully(b, 0, b.length);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Implementation of java.io.InputStream
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** <!-- ================================================================================================== -->
	 * @lastrev fix32489 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public int available() throws IOException
	{
		return itsInputStream.available();
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix32489 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public void close() throws IOException
	{
		itsInputStream.close();
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix32489 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public void mark(int readlimit)
	{
		itsInputStream.mark(readlimit);
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix32489 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public boolean markSupported()
	{
		return itsInputStream.markSupported();
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix32489 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public int read() throws IOException
	{
		return itsInputStream.read();
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix32489 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public int read(byte[] b, int off, int len) throws IOException
	{
		return itsInputStream.read(b, off, len);
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix32489 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public int read(byte[] b) throws IOException
	{
		return itsInputStream.read(b);
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix32489 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public void reset() throws IOException
	{
		itsInputStream.reset();
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix32489 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public long skip(long n) throws IOException
	{
		return itsInputStream.skip(n);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// PRIVATE HELPER METHODS
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** <!-- ================================================================================================== -->
	 * @lastrev fix35460 - readByte() returns negative values which this logic does not expect
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	private int readCompactUnsignedInt() throws IOException
	{
		int b = readNext();

		if ((b & 0x40) != 0)
		{
			// Return -1 to represent the fact that this is a null value.
			return -1;
		}

		int rtrn = (b & 0x3f);
		int shift = 6;

		while ((b & 0x80) != 0)
		{
			b = readNext();
			rtrn |= ((b & 0x7f) << shift);
			shift += 7;
		}

		return rtrn;
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix35460 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	private int readNext() throws IOException
	{
		int b = itsInputStream.read();

		if (b < 0)
			throw new EOFException();

		return b;
	}
}

class DataOutputStream extends OutputStream
{
	private static final int TEMP_BUFFER_SIZE =
		Integer.getInteger("com.actional.io.DataOutputStream.tempBufSize", 256).intValue();

	private byte[] itsTempIntBuffer;
	private final OutputStream itsOutputStream;

	/** <!-- ================================================================================================== -->
	 * @lastrev fix32489 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public DataOutputStream(OutputStream os)
	{
		itsOutputStream = os;
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix32489 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public OutputStream getOutputStream()
	{
		return itsOutputStream;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Implementation of java.io.InputStream
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** <!-- ================================================================================================== -->
	 * @lastrev fix35460 - wrong buffer being referenced
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public void writeUTF(String str) throws IOException
	{
		if (str == null)
		{
			itsOutputStream.write(0x40);
			return;
		}

		int strlen = str.length();
		byte[] bytearr;

		// Note: the assumption made when allocating the array is that
		//	 it will at worst 3 times more bytes than there are
		//	 characters in the string.

		if (strlen > TEMP_BUFFER_SIZE)
		{
			bytearr = new byte[strlen*3];
		}
		else
		{
			if (itsTempIntBuffer == null)
				itsTempIntBuffer = new byte[TEMP_BUFFER_SIZE*3];

			bytearr = itsTempIntBuffer;
		}

		int count = 0;

		for (int i = 0; i < strlen; i++)
		{
			char c = str.charAt(i);

			if ((c >= 0x0001) && (c <= 0x007F))
			{
				bytearr[count++] = (byte) c;
			}
			else if (c > 0x07FF)
			{
				bytearr[count++] = (byte) (0xE0 | ((c >> 12) & 0x0F));
				bytearr[count++] = (byte) (0x80 | ((c >>  6) & 0x3F));
				bytearr[count++] = (byte) (0x80 | ((c >>  0) & 0x3F));
			}
			else
			{
				bytearr[count++] = (byte) (0xC0 | ((c >>  6) & 0x1F));
				bytearr[count++] = (byte) (0x80 | ((c >>  0) & 0x3F));
			}
		}

		writeCompactUnsignedInt(count);
		itsOutputStream.write(bytearr, 0, count);
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix32489 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public void writeBoolean(boolean val) throws IOException
	{
		if (val)
			itsOutputStream.write(1);
		else
			itsOutputStream.write(0);
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix32489 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public void writeByte(byte val) throws IOException
	{
		itsOutputStream.write(val);
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix32489 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public void writeShort(short val) throws IOException
	{
		itsOutputStream.write((val >>> 8) & 0xFF);
		itsOutputStream.write((val >>> 0) & 0xFF);
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix32489 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public void writeInt(int val) throws IOException
	{
		itsOutputStream.write((val >>> 24) & 0xFF);
		itsOutputStream.write((val >>> 16) & 0xFF);
		itsOutputStream.write((val >>>  8) & 0xFF);
		itsOutputStream.write((val >>>  0) & 0xFF);
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix32489 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public void writeLong(long val) throws IOException
	{
		if (itsTempIntBuffer == null)
		{
			// Let's assume there might be strings written
			// out and thus use the same buffer.
			itsTempIntBuffer = new byte[TEMP_BUFFER_SIZE];
		}

		byte[] buf = itsTempIntBuffer;

		buf[0] = (byte)(val >>> 56);
	        buf[1] = (byte)(val >>> 48);
	        buf[2] = (byte)(val >>> 40);
	        buf[3] = (byte)(val >>> 32);
	        buf[4] = (byte)(val >>> 24);
	        buf[5] = (byte)(val >>> 16);
	        buf[6] = (byte)(val >>>  8);
	        buf[7] = (byte)(val >>>  0);

		itsOutputStream.write(buf, 0, 8);
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix32489 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public void writeFloat(float val) throws IOException
	{
		writeInt(Float.floatToIntBits(val));
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix32489 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public void writeDouble(double val) throws IOException
	{
		writeLong(Double.doubleToLongBits(val));
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix32489 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public void writeChar(char val) throws IOException
	{
		itsOutputStream.write((val >>> 8) & 0xFF);
		itsOutputStream.write((val >>> 0) & 0xFF);
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix32489 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public void writeBoxedBoolean(Boolean val) throws IOException
	{
		if (val == null)
			itsOutputStream.write(2);
		else if (val.booleanValue())
			itsOutputStream.write(1);
		else
			itsOutputStream.write(0);
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix32489 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public void writeBoxedByte(Byte val) throws IOException
	{
		if (val == null)
		{
			itsOutputStream.write(1);
			return;
		}

		itsOutputStream.write(0);
		writeByte(val.byteValue());
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix32489 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public void writeBoxedShort(Short val) throws IOException
	{
		if (val == null)
		{
			itsOutputStream.write(1);
			return;
		}

		itsOutputStream.write(0);
		writeShort(val.shortValue());
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix32489 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public void writeBoxedInt(Integer val) throws IOException
	{
		if (val == null)
		{
			itsOutputStream.write(1);
			return;
		}

		itsOutputStream.write(0);
		writeInt(val.intValue());
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix32489 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public void writeBoxedLong(Long val) throws IOException
	{
		if (val == null)
		{
			itsOutputStream.write(1);
			return;
		}

		itsOutputStream.write(0);
		writeLong(val.longValue());
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix32489 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public void writeBoxedFloat(Float val) throws IOException
	{
		if (val == null)
		{
			itsOutputStream.write(1);
			return;
		}

		itsOutputStream.write(0);
		writeFloat(val.floatValue());
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix32489 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public void writeBoxedDouble(Double val) throws IOException
	{
		if (val == null)
		{
			itsOutputStream.write(1);
			return;
		}

		itsOutputStream.write(0);
		writeDouble(val.doubleValue());
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix32489 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public void writeBoxedChar(Character val) throws IOException
	{
		if (val == null)
		{
			itsOutputStream.write(1);
			return;
		}

		itsOutputStream.write(0);
		writeChar(val.charValue());
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix32489 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public void writeBytes(byte[] b, int offset, int len) throws IOException
	{
		write(b, offset, len);
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix32489 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public void writeBytes(byte[] b) throws IOException
	{
		write(b);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Implementation of java.io.InputStream
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** <!-- ================================================================================================== -->
	 * @lastrev fix35446 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public void flush() throws IOException
	{
		itsOutputStream.flush();
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix35446 - Make sure the stream is flushed prior to closing.
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public void close() throws IOException
	{
		try
		{
			flush();
		}
		finally
		{
			itsOutputStream.close();
		}
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix32489 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public void write(byte[] b, int off, int len) throws IOException
	{
		itsOutputStream.write(b, off, len);
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix32489 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public void write(byte[] b) throws IOException
	{
		itsOutputStream.write(b);
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix32489 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public void write(int b) throws IOException
	{
		itsOutputStream.write(b);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// PRIVATE HELPER METHODS
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** <!-- ================================================================================================== -->
	 * @lastrev fix32489 - new method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public void writeCompactUnsignedInt(int val) throws IOException
	{
		if (val == 0)
		{
			// Optimized path
			itsOutputStream.write(0);
			return;
		}

		// Note: on the first byte written out, the bit 0x40 is
		//	 reserved to represent a null value.

		int cur = (val & 0x3f);
		val >>= 6;

		while (val > 0)
		{
			cur |= 0x80; // Add the 'more data' bit
			itsOutputStream.write(cur);

			cur = (val & 0x7f);
			val >>= 7;
		}

		itsOutputStream.write(cur);

		return;
	}
}

class HexadecimalOutput extends OutputStream
{
	private final static char[] HEXDIGITS =
	{
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
	};

	protected byte[]	itsBuffer;
	protected int		itsBufferLen;
	private StringBuffer	itsStringBuffer;
	private int		itsStringBufferLen;

	/** <!-- ================================================================================================== -->
	* @lastrev fix31713 - Call init().
	* <!-- ------------------------------------------------------------------------------------------------- --> */

	public HexadecimalOutput()
	{
		itsBuffer = new byte[16];
		init();
	}

	/** <!-- ================================================================================================== -->
	* @lastrev fix31713 - new method
	* <!-- ------------------------------------------------------------------------------------------------- --> */

	private void init()
	{
		itsBufferLen = 0;
		itsStringBuffer = new StringBuffer();
		itsStringBufferLen = 0;
	}

	/** <!-- ================================================================================================== -->
	* Write one byte in the hexadecimal output
	*
	* @param b			the byte to write
	*
	* @lastrev fix25836 - new method
	* <!-- ------------------------------------------------------------------------------------------------- --> */

	public void write(byte b)
	{
		if (itsBufferLen == itsBuffer.length)
			outputLine();

		itsBuffer[itsBufferLen++] = b;
	}

	/** <!-- ================================================================================================== -->
	 * Write one byte in the hexadecimal output
	 *
	 * @param b			the byte to write
	 *
	 * @lastrev fix30266 - New method.
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public void write(int b)
	{
		write((byte)b);
	}

	/** <!-- ================================================================================================== -->
	* Write multiple bites in the hexadecimal output.
	*
	* @param b			the bytes to write
	*
	* @lastrev fix25836 - new method
	* <!-- ------------------------------------------------------------------------------------------------- --> */

	public void write(byte[] b)
	{
		_write(b, 0, b.length);
	}

	/** <!-- ================================================================================================== -->
	* Write a subset of the array of bytes in the hexadecimal output.
	*
	* @param b			the bytes to write
	* @param len			the number of bytes to write from the beginning of the array.
	*
	* @lastrev fix27955 - pass the number of bytes that must be written, not the number of bytes of the array
	* <!-- ------------------------------------------------------------------------------------------------- --> */

	public void write(byte[] b, int len)
	{
		_write(b, 0, len);
	}

	/** <!-- ================================================================================================== -->
	* Write a subset of the array of bytes in the hexadecimal output starting from an offset.
	*
	* @param b			the bytes to write
	* @param offset			the number of bytes to write from the beginning of the array.
	* @param len			first byte of the array to write
	*
	* @lastrev fix30266 - Ported to .NET
	* <!-- ------------------------------------------------------------------------------------------------- --> */

	public void write(byte[] b, int offset, int len)
	{
		_write(b, offset, len);
	}

	/** <!-- ================================================================================================== -->
	* Write a subset of the array of bytes in the hexadecimal output starting from an offset.
	*
	* @param b			the bytes to write
	* @param offset			the number of bytes to write from the beginning of the array.
	* @param len			first byte of the array to write
	*
	* @lastrev fix30266 - Ported to .NET
	* <!-- ------------------------------------------------------------------------------------------------- --> */

	private void _write(byte[] b, int offset, int len)
	{
		len += offset;

		for (int i = offset; i < len; i++)
			write(b[i]);
	}

	/** <!-- ================================================================================================== -->
	* This method returns a human readable version of the buffer of bytes.
	*
	* @return the stringified representation of the array of bytes
	*
	* @lastrev fix31713 - call init() after having initializing generated the result.
	* <!-- ------------------------------------------------------------------------------------------------- --> */

	public String toString()
	{
		outputLine();
		String res = itsStringBuffer.toString();
		init();
		return res;
	}

	/** <!-- ================================================================================================== -->
	 * Helper accessor that turns the passed in byte[] into a String in one method call.
	 *
	 * @lastrev fix35512 - New method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public static String toString(byte[] bytes)
	{
		HexadecimalOutput hex = new HexadecimalOutput();

		hex.write(bytes);

		return hex.toString();
	}

	/** <!-- ================================================================================================== -->
	 * Helper accessor that turns the passed in byte[] into a String in one method call.
	 *
	 * @lastrev fix35512 - New method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public static void toString(StringBuffer sb, byte[] bytes)
	{
		HexadecimalOutput hex = new HexadecimalOutput();

		hex.itsStringBuffer = sb;
		hex.write(bytes);
		hex.outputLine(); // Make sure to flush the last line.
	}

	/** <!-- ================================================================================================== -->
	* Adds one line in the hexadecimal buffer output.
	*
	* @lastrev fix31492 - made protected.
	* <!-- ------------------------------------------------------------------------------------------------- --> */

	protected void outputLine()
	{
		synchronized(itsStringBuffer)
		{
			itsStringBuffer.append(HEXDIGITS[(itsStringBufferLen & 0xf0000000) >> 28]);
			itsStringBuffer.append(HEXDIGITS[(itsStringBufferLen & 0x0f000000) >> 24]);
			itsStringBuffer.append(HEXDIGITS[(itsStringBufferLen & 0x00f00000) >> 20]);
			itsStringBuffer.append(HEXDIGITS[(itsStringBufferLen & 0x000f0000) >> 16]);
			itsStringBuffer.append(HEXDIGITS[(itsStringBufferLen & 0x0000f000) >> 12]);
			itsStringBuffer.append(HEXDIGITS[(itsStringBufferLen & 0x00000f00) >> 8]);
			itsStringBuffer.append(HEXDIGITS[(itsStringBufferLen & 0x000000f0) >> 4]);
			itsStringBuffer.append(HEXDIGITS[(itsStringBufferLen & 0x0000000f)]);

			itsStringBuffer.append("   ");

			for (int i = 0; i < itsBufferLen; i++)
			{
				byte b = itsBuffer[i];
				itsStringBuffer.append(HEXDIGITS[(b & 0xf0) >> 4]);
				itsStringBuffer.append(HEXDIGITS[(b & 0x0f)]);
				itsStringBuffer.append(' ');
			}

			for (int i = itsBufferLen; i < itsBuffer.length; i++)
				itsStringBuffer.append("   ");

			itsStringBuffer.append("   ");

			for (int i = 0; i < itsBufferLen; i++)
			{
				char c = (char) itsBuffer[i];

				if (isPrint(c))
					itsStringBuffer.append(c);
				else
					itsStringBuffer.append('.');
			}

			itsStringBuffer.append("\r\n");

			itsStringBufferLen += itsBufferLen;
			itsBufferLen = 0;
		}
	}

	/** <!-- ================================================================================================== -->
	* Checks if a character is 'printable'.
	*
	* @param c			the character to test.
	*
	* @return whether the character can be printed.
	*
	* @lastrev fix25941 - New method
	* <!-- ------------------------------------------------------------------------------------------------- --> */

	public static boolean isPrint(char c)
	{
		if ((c >= '!') && (c <= '~')) /* 33 .. 126 */
			return true;
		else
			return false;
	}
}

class TeeOutputStream extends OutputStream
{
	protected OutputStream		itsStream1;
	protected OutputStream		itsStream2;

	/** <!-- ================================================================================================== -->
	* Constructor.
	*
	* @param stream1		the first output stream.
	* @param stream2		the second output stream
	*
	* @lastrev fix31713 - new method.
	* <!-- ------------------------------------------------------------------------------------------------- --> */
	public TeeOutputStream(OutputStream stream1, OutputStream stream2)
	{
		itsStream1 = stream1;
		itsStream2 = stream2;
	}

	/** <!-- ================================================================================================== -->
	* Writes the specified byte to this output stream.
	*
	* @param b			the data
	*
	* @lastrev fix31713 - new method.
	* <!-- ------------------------------------------------------------------------------------------------- --> */
	public void write(int b) throws IOException
	{
		try
		{
			itsStream1.write(b);
		}
		finally
		{
			itsStream2.write(b);
		}
	}

	/** <!-- ================================================================================================== -->
	* @param b			the data
	*
	* @lastrev fix31713 - new method.
	* <!-- ------------------------------------------------------------------------------------------------- --> */
	public void write(byte b[]) throws IOException
	{
		try
		{
			itsStream1.write(b);
		}
		finally
		{
			itsStream2.write(b);
		}
	}

	/** <!-- ================================================================================================== -->
	 * @param b			the data
	 * @param off			the start offset in the data
	 * @param len			the number of bytes to write
	 *
	 * @lastrev fix31713 - new method.
	 * <!-- ------------------------------------------------------------------------------------------------ --> */
	public void write(byte b[], int off, int len) throws IOException
	{
		try
		{
			itsStream1.write(b, off, len);
		}
		finally
		{
			itsStream2.write(b, off, len);
		}
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix31713 - new method.
	 * <!-- ------------------------------------------------------------------------------------------------ --> */
	public void flush() throws IOException
	{
		try
		{
			itsStream1.flush();
		}
		finally
		{
			itsStream2.flush();
		}
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix31713 - new method.
	* <!-- ------------------------------------------------------------------------------------------------- --> */
	public void close() throws IOException
	{
		try
		{
			itsStream1.close();
		}
		finally
		{
			itsStream2.close();
		}
	}
}
