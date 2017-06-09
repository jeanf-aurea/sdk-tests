package util;

import com.actional.lg.interceptor.sdk.ClientInteraction;
import com.actional.lg.interceptor.sdk.ILogEntry;
import com.actional.lg.interceptor.sdk.IntCorrelator;
import com.actional.lg.interceptor.sdk.IntCorrelatorImp;
import com.actional.lg.interceptor.sdk.Interaction;
import com.actional.lg.interceptor.sdk.LogEntryFactory;
import com.actional.lg.interceptor.sdk.ServerInteraction;

public class ManualCorrelator extends IntCorrelatorImp
{
	private static final ThreadLocal SI_TLS = new ThreadLocal();

	private static final String SDK_IMPL = "com.actional.lg.interceptor.internal.ManualCorrelator";

	public static void init()
	{
		try
		{
			Class.forName(SDK_IMPL);
			IntCorrelator.setIntCorrelator(SDK_IMPL);
		}
		catch (final ClassNotFoundException e)
		{
			// This version of the SDK does not have the manual correlator.
			IntCorrelator.setIntCorrelator(ManualCorrelator.class.getName());
		}
	}

	public static ILogEntry beginLogEntry(final ServerInteraction si)
	{
		SI_TLS.set(si);

		try
		{
			return LogEntryFactory.beginLogEntry();
		}
		finally
		{
			SI_TLS.set(null);
		}
	}

	public void dropKey(final Object key)
	{
		// When is this called????
	}

	public void dropValue(final Interaction intObj, final boolean tlsOnly)
	{
		// When is this called????
	}

	public ClientInteraction getClient()
	{
		return null;
	}

	public ClientInteraction getClient(final Object key)
	{
		if (key instanceof ClientInteraction)
			return (ClientInteraction) key;
		else
			return null;
	}

	public Interaction getForBegin(final Object key, final boolean forServer, final int reuseFlag)
	{
		return super.getForBegin(key, forServer, reuseFlag);
	}

	public ServerInteraction getServer()
	{
		return null;
	}

	public ServerInteraction getServer(final Object key)
	{
		if (key == null)
			return (ServerInteraction) SI_TLS.get();

		return (ServerInteraction) key;
	}

	public void putClient(final Interaction intObj)
	{
	}

	public void putClient(final Object key, final Interaction intObj)
	{
	}

	public void putServer(final Interaction intObj)
	{
	}

	public void putServer(final Object key, final Interaction intObj)
	{
	}
}
