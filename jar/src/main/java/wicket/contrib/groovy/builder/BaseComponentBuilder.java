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

import java.util.List;
import java.util.Map;

import wicket.Component;
import wicket.model.IModel;
import wicket.model.Model;

/**
 * Core class for most of the wicket component builder factories.  Override this for 
 * highly customized builders (use GenericComponentBuilder for more standard fare).
 * 
 * @author Kevin Galligan
 *
 */
public abstract class BaseComponentBuilder extends BuilderSupport implements WicketComponentBuilder
{
	public BaseComponentBuilder(Class targetClass) {
		super(targetClass);
	}
	
	protected void writeSimpleViewTagStart(String key)
	{}
	
	protected void writeSimpleViewTagEnd()
	{}
	
	/**
	 * Generate a list of constructor arguments.  This should allow for obscure situations
	 * easily with sub-classed component builders.
	 * 
	 * @return
	 */
	public abstract List getConstructorParameters(String key, Map attributes);
	
	protected Component createComponentInstace(String key, Map attributes)
	{
		try
		{ 
			if(isViewTagWriter())
			{
				writeSimpleViewTagStart(key);
			}
			
			List constructorParameters = getConstructorParameters(key, attributes);
			
			Object generated = generateInstance(attributes, constructorParameters);
			
			return (Component) generated;
		}
		catch (Exception e)
		{
			throw new WicketComponentBuilderException("Can't create component instance", e);
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
	
	//This is a little hacky, but until I figure out a better way to organize the code
	//this is how its happening.
	ThreadLocal viewTagStringBuilder = new ThreadLocal();
	
	public void writeViewTagEnd(StringBuilder text)
	{
		viewTagStringBuilder.set(null);
	}

	public void writeViewTagStart(StringBuilder text)
	{
		viewTagStringBuilder.set(text);
	}
	
	protected boolean isViewTagWriter()
	{
		return viewTagStringBuilder.get() != null;
	}
	
	protected void writeViewTagText(String text)
	{
		if(isViewTagWriter())
			((StringBuilder)viewTagStringBuilder.get()).append(text);
	}
	
	protected void writeViewTagText(StringBuilder text)
	{
		writeViewTagText(text.toString());
	}
	
	protected void writeViewTagText(StringBuffer text)
	{
		writeViewTagText(text.toString());
	}
}
