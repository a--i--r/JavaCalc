package calc;

// ０除算例外
public class DivideByZeroException extends Exception{
	public DivideByZeroException()
	{
		super();
	}

	public DivideByZeroException(String s)
	{
		super(s);
	}
}
