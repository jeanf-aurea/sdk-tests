package il;

import java.io.InputStream;

import com.actional.lg.interceptor.internal.config.UplinkCfgParser;
import com.actional.lg.interceptor.internal.config.UplinkType;

public class BackwardCompatibility2
{
	public static void main(String[] args)
		throws Exception
	{
		final InputStream is = BackwardCompatibility.class.getResourceAsStream("BackwardCompatibility2.txt");

		new UplinkCfgParser().parse(is, UplinkType.LOOPBACK_UPLINK);
	}
}
