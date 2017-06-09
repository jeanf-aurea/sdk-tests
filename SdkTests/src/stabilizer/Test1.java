package stabilizer;

import java.io.InputStreamReader;
import java.io.LineNumberReader;

import com.actional.lg.interceptor.sdk.StabilizerSwitch;

public class Test1
{
	public static void main(String[] args)
		throws Throwable
	{
		System.out.println("Enter the name of stabilizer switch to retrieve the value for:");

		LineNumberReader lnr = new LineNumberReader(new InputStreamReader(System.in));

		for (String line = lnr.readLine(); (line != null) && (line.length() > 0); line = lnr.readLine())
		{
			System.out.println(StabilizerSwitch.getBooleanValue(line));
		}
	}
}
