package wicket.contrib.groovy.builder;

import groovy.lang.Closure;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.commons.beanutils.PropertyUtils;

import wicket.Component;
import wicket.contrib.groovy.builder.util.AttributeUtils;

public abstract class BuilderSupport
{
	private Class targetClass;
	
	public BuilderSupport(Class targetClass) {
		
		this.targetClass = targetClass;
	}

	public Class getTargetClass()
	{
		return targetClass;
	}
	
	
	static DynamicJavaWrapper wrapper;
	
	
	/**
	 * This avoids the compilation issue with java calling groovy.  Normally we'd not want
	 * to work this hard, but we're getting maven issues.
	 * @return
	 */
	public static DynamicJavaWrapper getDynamicJavaWrapper()
	{
		if(wrapper == null)
		{
			try
			{
				wrapper = (DynamicJavaWrapper) Class.forName("wicket.contrib.groovy.builder.DynamicJavaWrapperUtil").newInstance();
			}
			catch (Exception e)
			{
				throw new WicketComponentBuilderException("Can't get wrapper instanc");
			}
		}
		
		return wrapper;
	}
	
	
	protected Object generateInstance(Map attributes, List constructorParameters)throws Exception
	{
//		Check attributes and see if anything is a closure.  Will require sub-class
		List closures = new ArrayList();
		List methods = new ArrayList();
		
		if(attributes != null)
		{
			Iterator attrIterator = attributes.keySet().iterator();
			
			while(attrIterator.hasNext())
			{
				String attrName = (String) attrIterator.next();
				if(attributes.get(attrName) instanceof Closure)
				{
					Closure methodOverride = (Closure) attributes.get(attrName);
					attrIterator.remove();
					closures.add(methodOverride);
					methods.add(matchClosuresToMethods(getTargetClass(), attrName, methodOverride));
				}
			}
		}
		
		Class localComponentClass;
		
		String extraCode = injectExtraCode();
		
		if(closures.size() > 0 || extraCode != null)
		{ 
			localComponentClass = getDynamicJavaWrapper().wrapClass(getTargetClass(), methods, injectExtraCode(), injectInterfaces());
		}
		else
		{
			localComponentClass = getTargetClass();
		}
		
		
		Object generated = ConstructorUtils.invokeConstructor(localComponentClass, constructorParameters.toArray());
		
		if(generated instanceof DynamicJavaWrapperScriptable)
		{
			DynamicJavaWrapperScriptable scriptable = (DynamicJavaWrapperScriptable) generated; 
			getDynamicJavaWrapper().fillMethods(scriptable, methods, closures);
		}
		
		return generated;
	}
	
	/**
	 * Override if you want to inject extra code.  This is clunky, and will probably go away.  Just for 
	 * ListView right now.
	 * 
	 * @return
	 */
	protected String injectExtraCode()
	{
		return null;
	}
	
	/**
	 * Override if you want to inject interfaces.  This is clunky, and will probably go away.  Just for 
	 * ListView right now.
	 * 
	 * @return
	 */
	protected String injectInterfaces()
	{
		return null;
	}
	
	public static Method matchClosuresToMethods(Class componentClass, String methodName, Closure closure)
	{
		//TODO: do a better type matchup.  Leave this alone for now.  Require strict types.
		try
		{
			Method method = null;
			while (componentClass.equals(Object.class) == false && method == null)
			{
				Method[] methods = componentClass.getDeclaredMethods();
				for (int i = 0; i < methods.length; i++)
				{
					Method checkMethod = methods[i];
					if (checkMethod.getName().equals(methodName))
					{
						if ((closure.getParameterTypes().length == 1 && checkMethod.getParameterTypes().length <= 1)||
								closure.getParameterTypes().length == checkMethod.getParameterTypes().length)
						{
							method = checkMethod;
							break;
						}
					}
				}

				componentClass = componentClass.getSuperclass();
			}
			
			if(method == null)
				throw new WicketComponentBuilderException("Can't get method for closure parameter types");
			
			return method;
		}
		catch (SecurityException e)
		{
			throw new WicketComponentBuilderException("Can't get method for closure parameter types", e);
		}
	}
	
	/**
	 * Tries to match up all properties.  Any extra properties will cause it to fail.
	 * 
	 * @param component
	 * @param attrs
	 */
	public static void setOtherProperties(Object target, Map attrs)
	{
		if(attrs == null || attrs.size() == 0)
			return;
		
		Iterator keyIter = attrs.keySet().iterator();
		
		//This is s a little dirty
		List keyStore = new ArrayList();
		
		while(keyIter.hasNext())
		{
			String key = (String) keyIter.next();
			Object value = attrs.get(key);
			
			keyStore.add(key);
			
			PropertyDescriptor propertyDescriptor;
			
			try
			{
				propertyDescriptor = PropertyUtils.getPropertyDescriptor(target, key);
			}
			catch (Exception e)
			{
				throw new WicketComponentBuilderException("Error with property for '"+ key +"'", e);
			}
			
			if(propertyDescriptor == null)
			{
				throw new WicketComponentBuilderException("No property for '"+ key +"'");
			}
			else
			{
				
//				if(propertyDescriptor.getPropertyType().isPrimitive())
//					value = value.toString();
//				
				
				value = AttributeUtils.generalAttributeConversion(propertyDescriptor.getPropertyType(), value);
				
				try
				{
					//Running pretty fast and loose here.  Good luck.
					BeanUtils.setProperty(target, key, value);
				}
				catch (Exception e)
				{
					throw new WicketComponentBuilderException("Error setting property for '"+ key +"'", e);
				}
			}
		}
		
		for(int i=0; i<keyStore.size(); i++)
		{
			attrs.remove(keyStore.get(i));
		}
		
	}

}
