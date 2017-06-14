package util;

import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.actional.lg.interceptor.sdk.ILogEntry;
import com.actional.lg.interceptor.sdk.LogLevel;
import com.actional.lg.interceptor.sdk.Part;
import com.actional.lg.interceptor.sdk.PartBytes;
import com.actional.lg.interceptor.sdk.ServerInteraction;
import com.actional.util.B64Code;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

public class Call extends com.actional.lg.interceptor.internal.simulator.Simulator
{
	private static final boolean itMustPopulate = Boolean.getBoolean("populate");

	protected boolean itIsPopulating = itMustPopulate;

	protected Call(final String name)
	{
		super(name);
	}

	public final void run() throws Exception
	{
		int loop = itIsPopulating ? 1 : Integer.getInteger("loop", 10);

		if (loop <= 0)
			loop = Integer.MAX_VALUE;

		run(loop);
	}

	public final void run(int loop) throws Exception
	{
		if (itIsPopulating)
			loop = 1;

		final long start = System.currentTimeMillis();

		doRun(loop);

		final long end = System.currentTimeMillis();
		final long elapsed = end - start;

		System.out.println(getClass().getName());
		System.out.println("\tTotal: " + elapsed + " ms");
		System.out.println("\tAverage: " + elapsed/loop + " ms");
	}

	protected void doRun(final int loop) throws Exception
	{
	}

	protected void raiseAlert(final String baseUrl, final String flowID, final String message)
		throws Exception
	{
		final URL url = new URL(baseUrl + "/api/IServer.aapi");
		final HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		try
		{
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setRequestProperty("SOAPAction", "\"\"");
			conn.setRequestProperty("Content-Type", "text/xml");
			conn.setRequestProperty("Authorization", "Basic " + B64Code.encode("User_Admin:security"));

			final OutputStream os = conn.getOutputStream();

			final String soap =
				"<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:m0=\"http://db.lg.actional.com\" xmlns:m1=\"http://config.actional.com\" xmlns:m2=\"http://db.contact.actional.com\" xmlns:m3=\"http://db.soapstation.actional.com\" xmlns:m4=\"http://classloader.actional.com\" xmlns:m5=\"http://db.actional.com\" xmlns:m6=\"http://management.license.actional.com\" xmlns:m7=\"http://catalog.actional.com\" xmlns:m8=\"http://actional.com\" xmlns:m9=\"http://db.jms.audit.actional.com\">" +
				"	<SOAP-ENV:Body>" +
				"		<m:raiseAlert xmlns:m=\"http://IServer.server.lg.actional.com\" SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">" +
				"			<alert xsi:type=\"m0:LGAlert\">" +
				"				<LTID xsi:type=\"xsd:string\">" + flowID + "</LTID>" +
				"				<message xsi:type=\"xsd:string\">" + message + "</message>" +
				"				<severity>ALARM</severity>" +
				"				<timeOccurred xsi:type=\"xsd:long\">" + System.currentTimeMillis() + "</timeOccurred>" +
				"				<actionMask xsi:type=\"xsd:long\">3</actionMask>" +
				"			</alert>" +
				"		</m:raiseAlert>" +
				"	</SOAP-ENV:Body>" +
				"</SOAP-ENV:Envelope>";

			os.write(soap.getBytes("UTF-8"));
			os.close();

			System.out.println(conn.getResponseCode() + ' ' + conn.getResponseMessage());
		}
		finally
		{
			conn.disconnect();
		}
	}

	protected static Part jsonToPart(final String json)
	{
		final JsonObject jsonPart = new JsonParser().parse(json).getAsJsonObject();
		final String ctype = getAsString(jsonPart, "contenttype");
		final String enc = getAsString(jsonPart, "encoding");
		final String contentStr = getAsString(jsonPart, "content");
		final byte[] content;

		if (contentStr == null)
			throw new IllegalArgumentException("Missing 'content'");

		if (contentStr.startsWith("bas64:"))
			content = B64Code.decode(contentStr.toCharArray());
		else
			content = contentStr.getBytes(StandardCharsets.UTF_8);

		final PartBytes rtrn = new PartBytes(content, ctype, enc);

		final JsonObject jsonMetadata = getAsJsonObject(jsonPart, "metadata");

		if (jsonMetadata != null)
		{
			final Map<String, Object> metadata = new HashMap<>();

			for (final Map.Entry<String, JsonElement> entry : jsonMetadata.entrySet())
			{
				final JsonElement el = entry.getValue();

				if (el.isJsonNull())
					continue;

				if (!el.isJsonPrimitive())
				{
					throw new IllegalArgumentException();
				}

				final JsonPrimitive jsonPrim = el.getAsJsonPrimitive();
				final Object val;

				if (jsonPrim.isString())
					val = jsonPrim.getAsString();
				else if (jsonPrim.isNumber())
					val = jsonPrim.getAsNumber();
				else
					val = Boolean.valueOf(jsonPrim.getAsBoolean());

				metadata.put(entry.getKey(), val);
			}

			rtrn.setMetaData(metadata);
		}

		return rtrn;
	}

	private static String getAsString(final JsonObject jsonObj, final String name)
	{
		final JsonElement el = jsonObj.get(name);

		if (el == null || el.isJsonNull())
			return null;

		return el.getAsString();
	}

	private static JsonObject getAsJsonObject(final JsonObject jsonObj, final String name)
	{
		final JsonElement el = jsonObj.get(name);

		if (el == null || el.isJsonNull())
			return null;

		return el.getAsJsonObject();
	}

	protected void addJsonLogEntry(final ServerInteraction si, final String jsonDoc)
	{
		final ILogEntry le = beginLogEntry(si);

		if (le == null)
			return;

		final JsonObject json = new JsonParser().parse(new StringReader(jsonDoc)).getAsJsonObject();

		le.setLogMessage(getJsonString(json, "message"));
		le.setLoggerName(getJsonString(json, "logger"));
		le.setLogThreadName(getJsonString(json, "threadName"));
		le.setLogThreadID(getJsonString(json, "threadID"));
		le.setLogClassName(getJsonString(json, "className"));
		le.setLogMethodName(getJsonString(json, "methodName"));
		le.setLogTime(getJsonLong(json, "time"));
		le.setLogCategories(getJsonStringList(json, "categories"));

		final String logLevelStr = getJsonString(json, "level");
		final LogLevel logLevel = findLogLevelFromString(logLevelStr);

		le.setLogLevel(logLevel);

		le.end();
	}

	protected static LogLevel findLogLevelFromString(final String logLevelStr)
	{
		if (logLevelStr == null)
			return LogLevel.UNKNOWN;

		final LogLevel[] all = LogLevel.getAllLogLevels();

		for (final LogLevel level : all)
		{
			if (logLevelStr.equals(level.toString()))
				return level;
		}

		return LogLevel.UNKNOWN;
	}

	private long getJsonLong(final JsonObject obj, final String prop)
	{
		final JsonElement elem = getJsonElement(obj, prop);

		if (elem == null)
			return 0L;

		return elem.getAsLong();
	}

	private String getJsonString(final JsonObject obj, final String prop)
	{
		final JsonElement elem = getJsonElement(obj, prop);

		if (elem == null)
			return null;

		return elem.getAsString();
	}

	private List<String> getJsonStringList(final JsonObject obj, final String prop)
	{
		final JsonElement elem = getJsonElement(obj, prop);

		if (elem == null)
			return null;

		final JsonArray arr = elem.getAsJsonArray();
		final List<String> rtrn = new ArrayList<>(arr.size());

		for (final JsonElement cur : arr)
		{
			rtrn.add(cur.getAsString());
		}

		return rtrn;
	}

	private JsonElement getJsonElement(final JsonObject obj, final String prop)
	{
		JsonElement elem = getJsonElement(obj.get(prop));

		if (elem == null)
			elem = getJsonElement(obj.get(prop.toLowerCase()));

		return elem;
	}

	private JsonElement getJsonElement(final JsonElement elem)
	{
		if (elem == null || elem.isJsonNull())
			return null;
		else
			return elem;
	}
}
