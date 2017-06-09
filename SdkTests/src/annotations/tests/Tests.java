package annotations.tests;

public class Tests
{
	interface ToInstrument { }

	@Copyright
	public static class Test1 implements ToInstrument { }

	@Copyright("1998")
	public static class Test2 implements ToInstrument { }

	public static class Test3 implements ToInstrument { }
}
