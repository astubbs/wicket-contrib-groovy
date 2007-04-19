package wicket.contrib.groovy.builder;

import groovy.lang.DelegatingMetaClass;

/**
 * Wicket components have a method called 'get(String path)'.  In a strange twist of fate, 
 * Groovy assumes that is a custom generic accessor method.  It doesn't bode well for us.  As a consequence,
 * you can't call properties on your parent object directly from a closure unless you call the actual
 * accessor explicitly.  This breaks some Wicket semantics.  I think we can do this with a MetaClass 
 * implementation, but its not complete yet.  This is the start.  
 * 
 * @author Kevin Galligan
 *
 */
public class WicketComponentMetaClass extends DelegatingMetaClass
{

	WicketComponentMetaClass(final Class a_class)
	{
		super(a_class);
		initialize();
	}

	
	public Object getProperty(Object object, String property)
	{
			try
			{
				return super.getProperty(object, property);
			}
			catch(Throwable e)
			{
//				throw new MissingPropertyException("path", object.getClass(), e);
			}
			//This is how I understand it now.  I'm sure this is wrong, but it looks like
			//if the property is local, well get it before hitting this method.  Otherwise,
			//it'll head up to the parent object of the closure
			
//		}
//		
//        return super.invokeMethod(a_object, methodName, a_arguments);
//		// TODO Auto-generated method stub
//		return super.getProperty(object, property);
			
			return null;
	}


//	public Object invokeMethod(Object a_object, String methodName, Object[] a_arguments)
//    {
//		if(methodName.equals("get"))
//		{
//			try
//			{
//				return super.invokeMethod(a_object, methodName, a_arguments);
//			}
//			catch(IllegalArgumentException e)
//			{
//				//Fell out
//			}
//			//This is how I understand it now.  I'm sure this is wrong, but it looks like
//			//if the property is local, well get it before hitting this method.  Otherwise,
//			//it'll head up to the parent object of the closure
//			throw new MissingPropertyException("path", a_object.getClass());   
//		}
//		
//        return super.invokeMethod(a_object, methodName, a_arguments);
//    }
}
