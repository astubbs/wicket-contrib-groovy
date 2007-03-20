package wicket.contrib.groovy.builder.otherpackage;

import groovy.lang.Closure;

import java.lang.reflect.Method;

import wicket.Component;
import wicket.contrib.groovy.builder.BaseComponentBuilder;
import wicket.contrib.groovy.builder.util.ListConstructors;
import wicket.markup.MarkupStream;
import wicket.markup.html.form.Form;
import wicket.model.IModel;

public class ClosureScriptWrapperJavaBasedDataObject extends wicket.MarkupContainer
{
	public ClosureScriptWrapperJavaBasedDataObject(String arg0, IModel arg1)
	{
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public ClosureScriptWrapperJavaBasedDataObject(String arg0)
	{
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	String someVal = "asdf";

	public String getSomeVal()
	{
		return someVal;
	}

	public void setSomeVal(String someVal)
	{
		this.someVal = someVal;
	}
	
	public static Class generateClassInJava(Closure closure)
	{
		Method method = BaseComponentBuilder.matchClosuresToMethods(ClosureScriptWrapperJavaBasedDataObject.class, "onSomething", closure);
  		
		Class awesome = BaseComponentBuilder.getDynamicJavaWrapper()
				.wrapClass(ClosureScriptWrapperJavaBasedDataObject.class, ListConstructors.newList(method), ListConstructors.newList(closure), null, null);
		
		return awesome;
	}
	
	public void onSomething()
	{
		System.out.println("in onSomething");
	}

	protected void onRender(MarkupStream arg0)
	{
		// TODO Auto-generated method stub
		
	}
	
//	Component get(final String path)
//	{
//		System.out.println("Painful");
//		return null;
//	}
}
