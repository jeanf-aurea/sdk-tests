package il;

import java.io.InputStreamReader;
import java.util.Map;

import com.actional.il.ILCorePrimitive;
import com.actional.il.ILEvalCtx;
import com.actional.il.ILPrimitiveFactory;

public class BackwardCompatibility
{
	public static void main(String[] args)
		throws Exception
	{
		final Map<String, ILPrimitiveFactory> functions = ILPrimitiveFactory.parseFunctions(
				ILCorePrimitive.CORE_PRIMITIVES, new InputStreamReader(BackwardCompatibility.class.getResourceAsStream("BackwardCompatibility.txt")));
		final ILEvalCtx ctx = new ILEvalCtx(functions);
		final Object rtrn = ctx.eval("match", new Object[] { "bladi blada", "Bladi Blada" });

		System.out.println(rtrn);
	}
}
