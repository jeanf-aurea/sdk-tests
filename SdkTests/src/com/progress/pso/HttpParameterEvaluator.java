//=====================================================================================================================
// $HeadURL: http://uxmtlsvn1.americas.progress.com/repos/Actional/branches/act82x/product/src/builtinplugins/com/actional/plugin/msgfield/MsgFieldXpathEvaluator.java $
// Checked in by: $Author: jeanf $
// $Date: 2011-07-13 11:16:54 -0400 (Wed, 13 Jul 2011) $
// $Revision: 51358 $
//---------------------------------------------------------------------------------------------------------------------
// Copyright (c) 2012 Progress Software Corporation and/or its subsidiaries or affiliates. All rights reserved.
//=====================================================================================================================

package com.progress.pso;

import com.actional.lg.interceptor.sdk.IMsgFieldEvaluator;
import com.actional.lg.interceptor.sdk.ServerInteraction;

/**
 * This class can be used when you want to have a message field get its value from an HTTP request parameter:
 * <ul>
 * <li>From the query string</li>
 * <li>From url encoded payload of an HTTP/POST</li>
 * <li>From a REST parameter if this code is invoked from within a REST servlet</li>
 * </ul>
 *
 * This class should be used with care. Indeed, calling HttpServletRequest.getParameter(String) causes the payload
 * to be consumed if the request is an HTTP/POST. As such, if you use this class during an HTTP request to servlet
 * that expects to consume the HTTP payload by itself, you might <b>break</b> such servlet implementations. <br>
 * <br>
 * This class is useless on its own; it needs to be invoked at strategic points using AOP. For instance, if you want
 * to retrieve the HTTP parameter called "orderid" on an HTTP/GET to servlet class called "com.foo.MyServlet", you
 * would need an AOP similar to the following: <br>
 * <br>
 * <pre>
 * instanceof javax.servlet.ServletRequest implements com.progress.pso.HttpParameterEvaluator$IHttpServletRequest
 * {
 * }
 *
 * class com.foo.MyServlet
 * {
 *	before doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
 *	{
 *		com.progress.pso.HttpParameterEvaluator.addParameters($1);
 *	}
 * }
 * </pre>
 * <br>
 * Package your AOP and the class HttpParameterEvaluator in a JAR then pass them the Actional instrumentation
 * script, i.e.: <br>
 * <b>Windows</b>
 * <pre>CALL path_to/actional-instrument.cmd your_options -aop path/to/my.aop -jar path/to/my.jar</pre>
 * <b>Linux/Unix</b>
 * <pre>. path_to/actional-instrument.sh
 * actional_instrument your_options -aop path/to/my.aop -jar path/to/my.jar</pre>
 * <br>
 * Then define a message field using the Transport Evaluator and specify the following name as the transport header:
 * <pre>{http-param}orderid</pre>
 * <br>
 * Use your message field in a dimension, an alert message, or an audit policy, then reprovision your agent and you
 * should have your expected value next time your run traffic through. <br>
 * <br>
 * Note that all of the above assumes that you have the servlet interceptor turned on.
 */
public class HttpParameterEvaluator implements IMsgFieldEvaluator
{
	private static final String PREFIX = "{http-param}";

	private final IHttpServletRequest itsRequest;

	public static void addParameters(Object httpServletRequest)
	{
		addParameters(ServerInteraction.get(), httpServletRequest);
	}

	public static void addParameters(ServerInteraction si, Object httpServletRequest)
	{
		if (si == null)
			return;

		if (!(httpServletRequest instanceof IHttpServletRequest))
		{
			// Our instanceof rule did not get applied to this type
			// of HttpServletRequest so we can't report the HTTP
			// parameters.
			return;
		}

		si._addMsgFieldEvaluator(new HttpParameterEvaluator((IHttpServletRequest) httpServletRequest));
	}

	private HttpParameterEvaluator(IHttpServletRequest request)
	{
		itsRequest = request;
	}

	public String eval(String msgFieldName, String transportHeaderName)
	{
		if (!transportHeaderName.startsWith(PREFIX))
			return null;

		return itsRequest.getParameter(transportHeaderName.substring(PREFIX.length()));
	}

	public interface IHttpServletRequest
	{
		String getParameter(String name);
	}
}
