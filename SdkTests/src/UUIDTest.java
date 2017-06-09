import java.net.InetAddress;
import java.security.SecureRandom;
import java.util.HashMap;

import sun.misc.BASE64Encoder;

import com.actional.util.B64Code;
import com.actional.util.UUID;


public class UUIDTest
{
	public static void main(String[] args)
	{
		int val;

		val = 0;

		print(val);

		val = 40000;

		print(val);

		val = 65535;

		print(val);

		val = 65536;

		print(val);

		HashMap map = new HashMap();

		for (int i = 0; i < 66666; i++)
		{
			String id = UUID.create();

			if (map.put(id, id) != null)
				throw new RuntimeException(id);
		}

		map.clear();

		for (int i = 0; i < 66666; i++)
		{
			String id = UUID_revision_5020.create();

			if (map.put(id, id) != null)
				throw new RuntimeException(id);
		}

		map.clear();

		for (int i = 0; i < 66666; i++)
		{
			String id = UUID_revision_30860.create();

			if (map.put(id, id) != null)
				throw new RuntimeException(id);
		}

		map.clear();

		for (int i = 0; i < 66666; i++)
		{
			String id = UUID_revision_40942.create();

			if (map.put(id, id) != null)
				throw new RuntimeException(id);
		}

		System.out.println("Ok");
	}

	private static void print(int val)
	{
		System.out.print(val);
		System.out.print(" - ");
		System.out.print((byte)(val & 0xff));
		System.out.print(" - ");
		System.out.println((byte)((val >> 8) & 0xff));
	}
}

class UUID_revision_5020
{
	protected static byte[]	itsBase = _getBase();
	protected static Object	itsLock = new Object();
	protected static int	itsCounter = 0;

	protected static synchronized byte[] _getBase()
	{
		byte[] base = new byte[16];

		// Fill in the IP address

		try
		{
			InetAddress id = InetAddress.getLocalHost();
			byte[] ip = id.getAddress();

			base[0] = ip[0];
			base[1] = ip[1];
			base[2] = ip[2];
			base[3] = ip[3];
		}
		catch( Exception e )
		{
		}

		// Fill in the process ID (since we can't get a PID in java
		// generate as random a number as we can)

		SecureRandom	gen = new SecureRandom();
		byte[]		seed = gen.generateSeed(2);

		base[4] = seed[0];
		base[5] = seed[1];

		// Fill in the timestamp of the base UUID

		long now = System.currentTimeMillis();

		base[6] = (byte)(now & 0xff);
		base[7] = (byte)((now >> 8) & 0xff);
		base[8] = (byte)((now >> 16) & 0xff);
		base[9] = (byte)((now >> 24) & 0xff);
		base[10] = (byte)((now >> 32) & 0xff);
		base[11] = (byte)((now >> 40) & 0xff);

		// Return this base ID

		return base;
	}

	/** <!-- ================================================================================================== -->
	* Create a new 16 byte UUID that is base64 encoded to fit into a string.
	*
	* @return String UUID
	*
	* @lastrev fixXXXXX - New method
	* <!-- ------------------------------------------------------------------------------------------------- --> */

	public static String create()
	{
		BASE64Encoder	encoder = new BASE64Encoder();

		return encoder.encode(createBytes());
	}

	/** <!-- ================================================================================================== -->
	* Create a new 16 byte UUID.
	*
	* @return UUID byte array
	*
	* @lastrev fixXXXXX - New method
	* <!-- ------------------------------------------------------------------------------------------------- --> */

	public static byte[] createBytes()
	{
		byte[]		rc = new byte[16];
		int		id;

		synchronized(itsLock)
		{
			id = itsCounter++;

			if(itsCounter == 0)
			{
				// We've wrapped around: regenerate a new base array
				_getBase();
			}
		}

		System.arraycopy(itsBase, 0, rc, 0, 12);
		rc[12] = (byte)(id & 0xff);
		rc[13] = (byte)((id >> 8) & 0xff);
		rc[14] = (byte)((id >> 16) & 0xff);
		rc[15] = (byte)((id >> 24) & 0xff);

		return rc;
	}
}

class UUID_revision_30860
{
	protected static byte[]	itsBase = computeBase();
	protected static Object	itsLock = new Object();
	protected static int	itsCounter = 0;

	protected static final int WRAP_AT = 50000;

	protected static synchronized byte[] computeBase()
	{
		byte[] base = new byte[16];

		// Fill in the IP address

		try
		{
			InetAddress id = InetAddress.getLocalHost();
			byte[] ip = id.getAddress();

			base[0] = ip[0];
			base[1] = ip[1];
			base[2] = ip[2];
			base[3] = ip[3];
		}
		catch( Exception e )
		{
		}

		// Fill in the process ID (since we can't get a PID in java
		// generate as random a number as we can)
		SecureRandom	gen = new SecureRandom();
		byte[]		seed = gen.generateSeed(2);

		base[4] = seed[0];
		base[5] = seed[1];
		// Fill in the timestamp of the base UUID

		long now = System.currentTimeMillis();

		base[6] = (byte)(now & 0xff);
		base[7] = (byte)((now >> 8) & 0xff);
		base[8] = (byte)((now >> 16) & 0xff);
		base[9] = (byte)((now >> 24) & 0xff);
		base[10] = (byte)((now >> 32) & 0xff);
		base[11] = (byte)((now >> 40) & 0xff);

		// Return this base ID

		return base;
	}

	/** <!-- ================================================================================================== -->
	* Create a new 16 byte UUID that is base64 encoded to fit into a string.
	*
	* @return String UUID
	*
	* @lastrev fix28948 - Use B64Code.
	* <!-- ------------------------------------------------------------------------------------------------- --> */

	public static String create()
	{
		return new String(B64Code.encode(createBytes()));
	}

	/** <!-- ================================================================================================== -->
	* Create a new 16 byte UUID.
	*
	* @return UUID byte array
	*
	* @lastrev fix33709 - Reset the base to generate more evenly hashed entries
	* <!-- ------------------------------------------------------------------------------------------------- --> */

	public static byte[] createBytes()
	{
		byte[]		rc = new byte[16];
		byte[]		base;
		int		id;

		synchronized(itsLock)
		{
			id = itsCounter++;

			if (itsCounter > WRAP_AT)
			{
				// Periodically change the base so that we
				// we give other code the best chance of
				// generating an evenly distributed hash.
				itsBase = computeBase();
				itsCounter = 0;
			}

			base = itsBase;
		}

		System.arraycopy(base, 0, rc, 0, 12);
		rc[12] = (byte)(id & 0xff);
		rc[13] = (byte)((id >> 8) & 0xff);
		rc[14] = (byte)((id >> 16) & 0xff);
		rc[15] = (byte)((id >> 24) & 0xff);

		return rc;
	}
}

class UUID_revision_40942
{
	private static final int WRAP_AT = 65535;

	private static final ThreadLocal TLS = new ThreadLocal();

	private static final byte[] THIS_MACHINEIP = computeMachineIP();

	private final byte[]	itsBase;
	private int		itsCounter;

	/** <!-- ================================================================================================== -->
	* Create a new 18 bytes UUID that is base64 encoded to fit into a string of 24 characters.
	*
	* @return String UUID
	*
	* @lastrev fix36039 - (optimization) rewrite our UUID generator to avoid lock contention
	* <!-- ------------------------------------------------------------------------------------------------- --> */

	public static String create()
	{
		final byte[] bytes = getTLS().compute();

		return new String(B64Code.encode(bytes));
	}

	/** <!-- ================================================================================================== -->
	* Create a new 16 byte UUID.
	*
	* @return UUID byte array
	*
	* @lastrev fix36039 - (optimization) rewrite our UUID generator to avoid lock contention
	* <!-- ------------------------------------------------------------------------------------------------- --> */

	public static byte[] createBytes()
	{
		final byte[] uuid = getTLS().compute();
		final byte[] rtrn = new byte[16];

		// In other to ensure backward compatibility with code
		// that might expect our logic to return only 16 bytes
		// we must strip off two bytes from 'uuid' (since it
		// contains 18 bytes). We'll take the first 16 bytes
		// since we know that the bytes that changes the most
		// are put first (see computeBase).

		System.arraycopy(uuid, 0, rtrn, 0, 16);

		return rtrn;
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix36039 - New method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	private UUID_revision_40942()
	{
		itsBase = new byte[18];
		itsBase[14] = THIS_MACHINEIP[3];
		itsBase[15] = THIS_MACHINEIP[2];
		itsBase[16] = THIS_MACHINEIP[1];
		itsBase[17] = THIS_MACHINEIP[0];
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix36039 - New method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	private byte[] compute()
	{
		final int id = itsCounter++;

		if (id > WRAP_AT)
		{
			// Periodically change the base so that we
			// we give other code the best chance of
			// generating an evenly distributed hash.
			computeBase();
		}

		final byte[] rtrn = itsBase;

		rtrn[0] = (byte)(id & 0xff);
		rtrn[1] = (byte)((id >> 8) & 0xff);

		return rtrn;
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix36039 - New method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	private void computeBase()
	{
		final byte[] seed = new SecureRandom().generateSeed(6);
		final byte[] base = itsBase;

		base[2] = seed[0];
		base[3] = seed[1];
		base[4] = seed[2];
		base[5] = seed[3];
		base[6] = seed[4];
		base[7] = seed[5];

		long now = System.currentTimeMillis();

		base[8] = (byte)(now & 0xff);
		base[9] = (byte)((now >> 8) & 0xff);
		base[10] = (byte)((now >> 16) & 0xff);
		base[11] = (byte)((now >> 24) & 0xff);
		base[12] = (byte)((now >> 32) & 0xff);
		base[13] = (byte)((now >> 40) & 0xff);

		itsCounter = 0;
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix36039 - New method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	private static byte[] computeMachineIP()
	{
		try
		{
			InetAddress id = InetAddress.getLocalHost();

			return id.getAddress();
		}
		catch(Exception e)
		{
			return new SecureRandom().generateSeed(4);
		}
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix36039 - New method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	private static UUID_revision_40942 getTLS()
	{
		UUID_revision_40942 rtrn = (UUID_revision_40942) TLS.get();

		if (rtrn == null)
		{
			rtrn = new UUID_revision_40942();
			rtrn.computeBase();

			TLS.set(rtrn);
		}

		return rtrn;
	}

	/** <!-- ================================================================================================== -->
	 * @lastrev fix36039 - New method
	 * <!-- ------------------------------------------------------------------------------------------------ --> */

	public static void main(String[] args)
	{
		for (int i = 0; i < 1000000; i++)
		{
			System.out.println(UUID.create());
		}
	}
}