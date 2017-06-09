import com.actional.lg.interceptor.sdk.ClientInteraction;
import com.actional.lg.interceptor.sdk.ServerInteraction;
import com.actional.lg.interceptor.sdk.helpers.InterHelpBase;
import com.actional.lg.interceptor.sdk.helpers.InterHelpTrust;
import com.actional.lg.interceptor.sdk.helpers.TrustAssertionType;


public class TrustZoneViolation
{
	public static void main(String[] args) throws Exception
	{
		test(TrustAssertionType.DEFAULT);
		test(TrustAssertionType.ALWAYS);
	}

	private static void test(TrustAssertionType tat) throws Exception
	{
		final ServerInteraction si = ServerInteraction.begin();

		si.setPeerAddr("localhost");
		si.setUrl("/TrustZoneTest1/service1");

		final ClientInteraction ci = ClientInteraction.begin();

		si.end();

		ci.setPeerAddr("localhost");
		ci.setUrl("/TrustZoneTest1/service2");

		final String corrHeaders = InterHelpBase.writeHeader(ci, tat);

		ci.end();

		final ServerInteraction si2 = ServerInteraction.begin();

		System.out.println(corrHeaders);

		InterHelpBase.readHeader(corrHeaders, si2);

		si2.setPeerAddr("localhost");
		si2.setUrl("/TrustZoneTest1/service2");

		InterHelpTrust.checkTrust(si2);

		if (TrustAssertionType.DEFAULT.equals(tat))
		{
			assert !InterHelpTrust.checkTrust(si2);
			assert si2.getHasSecurityFault();
			assert InterHelpTrust.TRUST_VIOLATION_MSG.equals(si2.getFailure());
		}
		else
		{
			assert InterHelpTrust.checkTrust(si2);
			assert !si2.getHasSecurityFault();
			assert si2.getFailure() == null;
		}

		si2.end();
	}
}
