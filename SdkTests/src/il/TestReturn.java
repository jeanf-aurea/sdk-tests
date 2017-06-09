package il;

import com.actional.il.ILCorePrimitive;
import com.actional.il.ILCorePrimitive.AddInt;
import com.actional.il.ILCorePrimitive.Block;
import com.actional.il.ILCorePrimitive.Equals;
import com.actional.il.ILCorePrimitive.FunctionCall;
import com.actional.il.ILCorePrimitive.GetLocalVar;
import com.actional.il.ILCorePrimitive.If;
import com.actional.il.ILCorePrimitive.IntConstant;
import com.actional.il.ILCorePrimitive.Return;
import com.actional.il.ILCorePrimitive.SetLocalVar;
import com.actional.il.ILCorePrimitive.While;
import com.actional.il.ILFunctionDef;
import com.actional.il.ILPrimitive;
import com.actional.lg.interceptor.internal.BaseFilterPrimitive;
import com.actional.lg.interceptor.internal.ILInteractionEvalCtx;
import com.actional.lg.interceptor.internal.ILInteractionPrimitive.SetInternal;

public class TestReturn
{
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		test1();
		test2();
	}

	private static void test1() throws Exception
	{
		ILPrimitive body = new Block
		(
			new ILPrimitive[]
			{
				new If
				(
					ILCorePrimitive.True,
					new Block
					(
						new ILPrimitive[]
						{
							new SetInternal(ILCorePrimitive.True),
							new Return(ILCorePrimitive.True)
						}
					),
					ILCorePrimitive.Null
				)
			}
		);

		ILFunctionDef function = new ILFunctionDef("special_function", 0, 0, body);

		FunctionCall call = new FunctionCall(function, null);
		FakeCtx ctx = new FakeCtx();
		Object rtrn = call.eval(ctx);
		System.out.println(rtrn);
		assert Boolean.TRUE.equals(rtrn);
	}

	private static void test2() throws Exception
	{
		final ILPrimitive var1 = new GetLocalVar(0);

		ILPrimitive body = new Block
		(
			new ILPrimitive[]
			{
				new SetLocalVar(0, new IntConstant(0)),
				new While
				(
					ILCorePrimitive.True,
					new Block
					(
						new ILPrimitive[]
						{
							new If
							(
								new Equals
								(
									var1,
									new IntConstant(2)
								),
								new Block
								(
									new ILPrimitive[]
									{
										new SetInternal(ILCorePrimitive.True),
										new Return(ILCorePrimitive.True)
									}
								),
								ILCorePrimitive.Null
							),
							new SetLocalVar(0, new AddInt(var1, new IntConstant(1))),
						}
					)
				)
			}
		);

		ILFunctionDef function = new ILFunctionDef("special_function", 0, 1, body);

		FunctionCall call = new FunctionCall(function, null);
		FakeCtx ctx = new FakeCtx();
		Object rtrn = call.eval(ctx);
		System.out.println(rtrn);
		assert Boolean.TRUE.equals(rtrn);
	}
}

class FakeCtx extends ILInteractionEvalCtx
{
	@Override
	protected void handleFilterException(BaseFilterPrimitive filter,
			Throwable e)
	{
		// TODO Auto-generated method stub

	}

	FakeCtx()
	{
		super(null, null);
	}

	@Override
	public boolean isClientInteraction()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String fetchUrlPath()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String fetchProviderRealName()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String fetchProviderAddr()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public short fetchProviderType()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void updateProviderType(short val)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public short fetchConsumerType()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void updateConsumerType(short val)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setMsgField(String name, String val, boolean overwrite)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public Object getMsgField(String name)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMatchedFilterID(String val)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public String getProviderFilterID()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setL2Name(String val)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setL2Type(short val)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setL2ID(String val)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setL3Name(String val)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setL3Type(short val)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setL3ID(String val)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setL4Name(String val)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setL4Type(short val)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setL4ID(String val)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public String getL2Name()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public short getL2Type()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getL2ID()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getL3Name()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public short getL3Type()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getL3ID()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getL4Name()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public short getL4Type()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getL4ID()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUrl()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setUrlPath(String val)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public String getPeerAddr()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPeerAddr(String val)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public String getSelfAddr()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSelfAddr(String val)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setInternal(boolean val)
	{
		// TODO Auto-generated method stub

	}

}