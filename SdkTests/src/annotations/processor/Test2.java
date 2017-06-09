package annotations.processor;

public class Test2
{
	public static void main(String[] args)
	{
		try
		{
			System.out.println("1");
			Class.forName("annotations.tests.Tests$Test1");
			System.out.println("2");
			Class.forName("annotations.tests.Tests$Test2");
			System.out.println("3");
			Class.forName("annotations.tests.Tests$Test3");
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}
}
