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
package wicket.contrib.groovy.builder.util;

import groovy.lang.IntRange;

import java.io.Serializable;
import java.util.Map;


import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.StringUtils;

import wicket.contrib.groovy.builder.WicketComponentBuilderException;
import wicket.model.IModel;
import wicket.model.Model;

/**
 * Some general utilities for pulling values from the attributes map.
 * 
 * @author Kevin Galligan
 *
 */
public class AttributeUtils
{
	public static boolean booleanValue(Object arg) 
	{
		if(arg instanceof Boolean)
			return ((Boolean)arg).booleanValue();
		else
			return Boolean.parseBoolean(arg.toString());
	}
	
	public static IModel modelValue(Serializable arg)
	{
		if(arg instanceof IModel)
			return (IModel)arg;
		else
			return new Model(arg);
	}
	
	public static IntRange intRangeValue(Object arg)
	{
		if(arg == null)
			return null;
		if(arg instanceof IntRange)
			return (IntRange)arg;
		else
			throw new WicketComponentBuilderException("Range must be of type IntRange");
	}
	
	public static Class classValue(Object arg) throws ClassNotFoundException
	{
		if(arg instanceof Class)
			return (Class)arg;
		else if(arg instanceof String)
			return Class.forName((String)arg);
		else
			throw new WicketComponentBuilderException("Can't figure out Class type");
	}
	
	public static Class classValueSafe(Object arg)
	{
		try
		{
			return classValue(arg);
		}
		catch (ClassNotFoundException e)
		{
			throw new WicketComponentBuilderException("Can't get Class value", e);
		}
	}
	
	public static Object multiName(Map attrs, String[] names)
	{
		for(int i=0; i<names.length; i++)
		{
			String name = names[i];
			Object value = attrs.remove(name);
			if(value != null)
				return value;
		}
		
		return null;
	}
	
	public static Object multiNameOptional(Map attrs, String[] names)
	{
		return multiName(attrs, names);
	}
	
	public static Object multiNameRequired(Map attrs, String[] names)
	{
		Object value = multiName(attrs, names);
		if(value == null)
			throw new WicketComponentBuilderException("One of "+ StringUtils.join(names, ',') +" required");
		
		return value;
	}
	
	public static Object generalAttributeConversion(Class expectedType, Object value)
	{
		if(value instanceof String)
		{
			if (expectedType.equals(Class.class))
			{
				try
				{
					value = AttributeUtils.classValue(value);
				}
				catch (ClassNotFoundException e)
				{
					throw new WicketComponentBuilderException("Attribute conversion error.  Could not find class '"+ value.toString() +"'", e);
				}
			}
			if(expectedType.equals(String.class) == false)
				value = ConvertUtils.convert((String) value, expectedType);
		}
		
		return value;
	}
}
