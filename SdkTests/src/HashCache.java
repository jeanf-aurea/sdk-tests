//=====================================================================================================================
// $HeadURL: http://uxmtlsvn1.americas.progress.com/repos/Actional/branches/act82x/product/src/builtinplugins/com/actional/plugin/msgfield/MsgFieldXpathEvaluator.java $
// Checked in by: $Author: jeanf $
// $Date: 2011-07-13 11:16:54 -0400 (Wed, 13 Jul 2011) $
// $Revision: 51358 $
//---------------------------------------------------------------------------------------------------------------------
// Copyright (c) 2012 Progress Software Corporation and/or its subsidiaries or affiliates. All rights reserved.
//=====================================================================================================================

import com.actional.util.B64Code;
import com.actional.util.ICache;
import com.actional.util.UBCache;

/**
 * @author jeanf
 *
 */
public class HashCache
{
	private final ICache itsCache;

	public HashCache()
	{
		this(true);
	}

	public HashCache(boolean multithreadsafe)
	{
		this(multithreadsafe, 32, 512);
	}

	public HashCache(boolean multithreadsafe, int min, int max)
	{
		itsCache = UBCache.create(multithreadsafe, min, max);
	}

	public String hash(String str)
		throws Exception
	{
		return doHash(str);
	}

	public String hash(Object hashable)
		throws Exception
	{
		return doHash(hashable);
	}

	protected String doHash(Object hashable)
		throws Exception
	{
		final String rtrn = (String) itsCache.get(hashable);

		if (rtrn != null)
			return rtrn;

		final String value = computeHash(hashable);

		itsCache.put(hashable, value);

		return value;
	}

	protected String computeHash(Object hashable)
		throws Exception
	{
		final byte[] bytes = computeBytes(hashable);

		return B64Code.encodeForKeyIDs(bytes);
	}

	protected byte[] computeBytes(Object hashable)
		throws Exception
	{
		return toString(hashable).getBytes("UTF-8");
	}

	protected String toString(Object hashable)
		throws Exception
	{
		return hashable.toString();
	}
}
