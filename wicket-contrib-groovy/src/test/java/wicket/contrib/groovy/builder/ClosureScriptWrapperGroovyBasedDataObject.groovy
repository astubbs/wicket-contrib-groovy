package wicket.contrib.groovy.builder;


class ClosureScriptWrapperGroovyBasedDataObject {

	String someVal = "asdf";

	public String getSomeVal()
	{
		return someVal;
	}

	public void setSomeVal(String someVal)
	{
		this.someVal = someVal;
	}
	
	protected void onSomething()
	{
		System.out.println("in onSomething");
	}
}