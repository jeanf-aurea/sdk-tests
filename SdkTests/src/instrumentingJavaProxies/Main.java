//=====================================================================================================================
// $HeadURL: http://uxmtlsvn1.americas.progress.com/repos/Actional/branches/act82x/product/src/builtinplugins/com/actional/plugin/msgfield/MsgFieldXpathEvaluator.java $
// Checked in by: $Author: jeanf $
// $Date: 2011-07-13 11:16:54 -0400 (Wed, 13 Jul 2011) $
// $Revision: 51358 $
//---------------------------------------------------------------------------------------------------------------------
// Copyright (c) 2012 Progress Software Corporation and/or its subsidiaries or affiliates. All rights reserved.
//=====================================================================================================================

package instrumentingJavaProxies;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author J-F
 *
 */
public class Main
{
	public static void main(final String[] args)
		throws Exception
	{
		final InvocationHandler handler = new MyHandler();

		final InterfaceToInstrument test = (InterfaceToInstrument) Proxy.newProxyInstance(
			Main.class.getClassLoader(), new Class[] { InterfaceToInstrument.class }, handler);

		test.method("blah");

		((BridgeInterface) test).getClientInteraction__();
	}
}

class MyHandler implements InvocationHandler
{
	public Object invoke(Object proxy, Method method, Object[] args)
		throws Throwable
	{
		System.out.println(method.getName());
		return null;
	}
}
