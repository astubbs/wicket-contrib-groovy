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
		Method method = BaseComponentBuilder.matchClosuresToMethods(CompClosuresPageTestForm.class, "onTestEvent", closure);
  		
		Class awesome = BaseComponentBuilder.getDynamicJavaWrapper()
				.wrapClass(CompClosuresPageTestForm.class, ListConstructors.newList(method), ListConstructors.newList(closure), null, null);
		
		return awesome;
	}
	

}
