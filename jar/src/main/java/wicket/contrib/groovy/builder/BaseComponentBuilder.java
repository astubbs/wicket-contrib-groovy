/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
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
import wicket.model.IModel;
import wicket.model.Model;

/**
 * Core class for most of the wicket component builder factories.  Override this for 
 * highly customized builders (use GenericComponentBuilder for more standard fare).
 * 
 * @author Kevin Galligan
 *
 */
public abstract class BaseComponentBuilder implements WicketComponentBuilder
{
	private Class componentClass;
	
	public BaseComponentBuilder(Class componentClass) {
		
		this.componentClass = componentClass;
	}

	public Class getComponentClass()
	{
		return componentClass;
	}
	
	/**
	 * Generate a list of constructor arguments.  This should allow for obscure situations
	 * easily with sub-classed component builders.
	 * 
	 * @return
	 */
	public abstract List getConstructorParameters(String key, Map attributes);
	
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
	
	protected Component createComponentInstace(String key, Map attributes)
	{
		try
		{ 
			//Check attributes and see if anything is a closure.  Will require sub-class
			Iterator attrIterator = attributes.keySet().iterator();
			List closures = new ArrayList();
			List methods = new ArrayList();
			
			while(attrIterator.hasNext())
			{
				String attrName = (String) attrIterator.next();
				if(attributes.get(attrName) instanceof Closure)
				{
					Closure methodOverride = (Closure) attributes.get(attrName);
					attrIterator.remove();
					closures.add(methodOverride);
					methods.add(matchClosuresToMethods(componentClass, attrName, methodOverride));
				}
			}
			
			Class localComponentClass;
			
			String extraCode = injectExtraCode();
			
			if(closures.size() > 0 || extraCode != null)
			{ 
				localComponentClass = getDynamicJavaWrapper().wrapClass(getComponentClass(), methods, closures, injectExtraCode(), injectInterfaces());
			}
			else
			{
				localComponentClass = getComponentClass();
			}
			
			List constructorParameters = getConstructorParameters(key, attributes);
			
			return (Component) ConstructorUtils.invokeConstructor(localComponentClass, constructorParameters.toArray());
		}
		catch (Exception e)
		{
			throw new WicketComponentBuilderException("Can't create component instance", e);
		}
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
	 * Remove properties that are set so auto-mapping should work without problems
	 */
	public static void setModel(Component component, Map attrs)
	{
		if(attrs == null)
			return;
		Object modelObj = attrs.get("model");
		if(modelObj == null)
			return;
		
		if(modelObj instanceof IModel)
			component.setModel((IModel)modelObj);
		else 
			component.setModel(new Model(modelObj.toString()));
		
		attrs.remove("model");
	}
	
	/**
	 * Tries to match up all properties.  Any extra properties will cause it to fail.
	 * 
	 * @param component
	 * @param attrs
	 */
	public static void setOtherProperties(Component component, Map attrs)
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
				propertyDescriptor = PropertyUtils.getPropertyDescriptor(component, key);
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
					BeanUtils.setProperty(component, key, value);
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
