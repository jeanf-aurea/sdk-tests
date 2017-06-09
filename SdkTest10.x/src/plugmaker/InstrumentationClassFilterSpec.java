//=====================================================================================================================
// $HeadURL: http://uxmtlsvn1.americas.progress.com/repos/Actional/branches/act82x/product/src/builtinplugins/com/actional/plugin/msgfield/MsgFieldXpathEvaluator.java $
// Checked in by: $Author: jeanf $
// $Date: 2011-07-13 11:16:54 -0400 (Wed, 13 Jul 2011) $
// $Revision: 51358 $
//---------------------------------------------------------------------------------------------------------------------
// Copyright (c) 2012 Aurea Software and/or its subsidiaries or affiliates. All rights reserved.
//=====================================================================================================================

package plugmaker;

import com.actional.plugmaker.classfilter.ClassFilterMatcher;
import com.actional.plugmaker.classfilter.InstrumentationBootClassFilter;

/**
 *
 */
public class InstrumentationClassFilterSpec
{
	public static void main(String[] args)
		throws Exception
	{
		test1();
		test2();
		test3();
		test4();
		test5();
		test6();
		test7();
		test8();
		test9();
		test10();

		System.err.flush();
		System.out.println("All good.");
	}

	public static void test1()
		throws Exception
	{
		final ClassFilterMatcher filter = InstrumentationBootClassFilter.create();

		test(filter, "java.lang.Object", false);
		test(filter, "java.util.List", false);
		test(filter, "java.util.concurrent.SynchronousQueue", true);
		test(filter, "com.actional.Log", true);
		test(filter, "com.sun.Log", true);
	}

	public static void test2()
		throws Exception
	{
		final ClassFilterMatcher filter = InstrumentationBootClassFilter.create(
				"",
				null,
				null);

		test(filter, "java.lang.Object", true);
		test(filter, "java.util.List", true);
		test(filter, "java.util.concurrent.SynchronousQueue", true);
		test(filter, "com.actional.Log", true);
		test(filter, "com.sun.Log", true);
	}

	public static void test3()
		throws Exception
	{
		final ClassFilterMatcher filter = InstrumentationBootClassFilter.create(
				"*",
				null,
				null);

		test(filter, "java.lang.Object", false);
		test(filter, "java.util.List", false);
		test(filter, "java.util.concurrent.SynchronousQueue", false);
		test(filter, "com.actional.Log", false);
		test(filter, "com.sun.Log", false);
	}

	public static void test4()
		throws Exception
	{
		final ClassFilterMatcher filter = InstrumentationBootClassFilter.create(
				null,
				"",
				null);

		test(filter, "java.lang.Object", false);
		test(filter, "java.util.List", false);
		test(filter, "java.util.concurrent.SynchronousQueue", true);
		test(filter, "com.actional.Log", true);
		test(filter, "com.sun.Log", true);
	}

	public static void test5()
		throws Exception
	{
		final ClassFilterMatcher filter = InstrumentationBootClassFilter.create(
				null,
				"-java.util.concurrent.*",
				null);

		test(filter, "java.lang.Object", false);
		test(filter, "java.util.List", false);
		test(filter, "java.util.concurrent.SynchronousQueue", false);
		test(filter, "com.actional.Log", true);
		test(filter, "com.sun.Log", true);
	}

	public static void test6()
		throws Exception
	{
		final ClassFilterMatcher filter = InstrumentationBootClassFilter.create(
				null,
				"-com.actional.*",
				null);

		test(filter, "java.lang.Object", false);
		test(filter, "java.util.List", false);
		test(filter, "java.util.concurrent.SynchronousQueue", true);
		test(filter, "com.actional.Log", false);
		test(filter, "com.sun.Log", true);
	}

	public static void test7()
		throws Exception
	{
		final ClassFilterMatcher filter = InstrumentationBootClassFilter.create(
				"com.actional.",
				null,
				null);

		test(filter, "java.lang.Object", true);
		test(filter, "java.util.List", true);
		test(filter, "java.util.concurrent.SynchronousQueue", true);
		test(filter, "com.actional.Log", false);
		test(filter, "com.sun.Log", true);
	}

	public static void test8()
		throws Exception
	{
		final ClassFilterMatcher filter = InstrumentationBootClassFilter.create(
				"com/actional/",
				"-java.util.concurrent.*",
				null);

		test(filter, "java.lang.Object", false);
		test(filter, "java.util.List", false);
		test(filter, "java.util.concurrent.SynchronousQueue", false);
		test(filter, "com.actional.Log", true);
		test(filter, "com.sun.Log", true);
	}

	public static void test9()
		throws Exception
	{
		final ClassFilterMatcher filter = InstrumentationBootClassFilter.create(
				"com/actional/",
				null,
				"-java.util.concurrent.*");

		test(filter, "java.lang.Object", false);
		test(filter, "java.util.List", false);
		test(filter, "java.util.concurrent.SynchronousQueue", true);
		test(filter, "com.actional.Log", true);
		test(filter, "com.sun.Log", true);
	}

	public static void test10()
		throws Exception
	{
		final ClassFilterMatcher filter = InstrumentationBootClassFilter.create(
				"com/actional/",
				"-java.util.concurrent.*,+java.util.*",
				null);

		test(filter, "java.lang.Object", false);
		test(filter, "java.util.List", true);
		test(filter, "java.util.concurrent.SynchronousQueue", false);
		test(filter, "com.actional.Log", true);
		test(filter, "com.sun.Log", true);
	}

	private static void test(
			final ClassFilterMatcher filter,
			final String classResourceName,
			final boolean expected)
		throws Exception
	{
		if (expected != filter.mustInstrument(classResourceName))
			throw new Exception("Unexpected result for " + classResourceName);
	}
}
