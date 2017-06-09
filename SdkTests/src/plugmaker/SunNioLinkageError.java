package plugmaker;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class SunNioLinkageError
{
	public static void main(String[] args)
		throws Exception
	{
		final File f = new File(args[0]);
		final URL[] urls = { f.toURL() };
		final URLClassLoader ucl = new URLClassLoader(urls);

		ucl.loadClass("plugmaker.SunNioLinkageError$MyFakeClass");
	}

	private static class MyFakeClass
	{
		// To reproduce the LinkageError, you must make binary modifications
		// to the class file of MyFakeClass so that "SomeString" contains
		// invalid UTF-8 characters, ZIP it up in a JAR, and pass it in argument
		// to SunNioLinkageError
		public static final String CONSTANT = "SomeString";
	}
}
