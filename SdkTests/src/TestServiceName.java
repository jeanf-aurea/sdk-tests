import com.actional.GeneralUtil;

public class TestServiceName
{
	public static void main(String[] args)
	{
		test("https://sit1.t-home.de:443/is-bin/INTERSHOP.enfinity/WFS/EKI-KC-Site/de_DE/-/EUR/ViewCSS-UpdateSessionParameter;sid=Tb5HPIMamoBGPM-dN4RL8JAfUzzcyGvQpqNZvhjuUzzcyJ2Y1kpZvhju");
	}

	private static void test(String name)
	{
//		System.out.println(name + " = \"" + NameEncoder.serviceName(name, true, "is-bin") + '"');
		System.out.println(name + " = \"" + GeneralUtil.canonicalURL(name) + '"');
	}
}
