package wicket.contrib.groovy.builder;

import groovy.lang.Closure;

import java.lang.reflect.Method;

import wicket.contrib.groovy.builder.util.ListConstructors;
import wicket.markup.html.form.Form;
import wicket.model.IModel;

public class CompClosuresPageTestForm extends Form
{  
	public CompClosuresPageTestForm(String arg0, IModel arg1)
	{
		super(arg0, arg1);
	}

	public CompClosuresPageTestForm(String arg0)
	{ 
		super(arg0);
	}

	protected void onTestEvent()
	{
		throw new UnsupportedOperationException("Shouldn't be in here");
	}
	
	public static Class generateClassInJava(Closure closure)
	{
		Method method = BuilderSupport.matchClosuresToMethods(CompClosuresPageTestForm.class, "onTestEvent", closure);
  		
		Class awesome = BuilderSupport.getDynamicJavaWrapper()
				.wrapClass(CompClosuresPageTestForm.class, ListConstructors.newList(method), null, null);
		
		return awesome;
	}
	
	public static void fillMethodsOnInstance(Closure closure, DynamicJavaWrapperScriptable scriptable)
	{
		Method method = BuilderSupport.matchClosuresToMethods(CompClosuresPageTestForm.class, "onTestEvent", closure);

		BuilderSupport.getDynamicJavaWrapper().fillMethods(scriptable, ListConstructors.newList(method), ListConstructors.newList(closure));
	}
}
