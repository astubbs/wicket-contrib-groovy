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
import groovy.util.BuilderSupport;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import wicket.Application;
import wicket.Component;
import wicket.MarkupContainer;
import wicket.behavior.IBehavior;
import wicket.markup.html.form.FormComponent;
import wicket.markup.html.form.validation.IValidator;
import wicket.model.IModel;

/**
 * This is the core class.  It takes a MarkupContainer as a context, and adds anything called to that.
 * Originally this took a page as context, but you can use this with more fine grained components as 
 * needed (like a sub-classed form).
 * 
 * There are several convenience 'add' methods that allow custom components, validators, and behaviors
 * to be added.
 * 
 * @author Kevin Galligan
 *
 */
public class WicketBuilder extends BuilderSupport {

	HashMap objects;
	
	MarkupContainer context;

	boolean developmentMode = false;
	
	public WicketBuilder(MarkupContainer context) {
		this.context = context;
		setCurrent(context);
		
		try
		{
			developmentMode = context.getApplication().getConfigurationType().equals(Application.DEVELOPMENT);
		}
		catch (Exception e)
		{
			//Not a problem.
		}		
	}
	
	public StringBuilder getTagTemplate()
	{
		return tagTemplate;
	}

	StringBuilder tagTemplate = new StringBuilder();
	
	//This is pretty hacky.  Should refactor the general layout at some point
	Map generatedComponentBuilderCache = new HashMap();
	
	protected void nodeCompleted(Object parent, Object node)
	{
		if(node instanceof Component)
		{
			if (parent instanceof MarkupContainer) {
				MarkupContainer containerParent = (MarkupContainer) parent;
				Component componentChild = (Component) node;
	
				containerParent.add(componentChild);
			}
			
			WicketComponentBuilder helper = (WicketComponentBuilder) generatedComponentBuilderCache.get(node);
			
			if(developmentMode)
				helper.writeViewTagEnd(tagTemplate);
		}
		else if(node instanceof IModel)
		{
			((Component)parent).setModel((IModel)node);
		}
		else if(node instanceof IBehavior)
		{
			((Component)parent).add((IBehavior)node);
		}
		else if(node instanceof IValidator && parent instanceof FormComponent)
		{
			((FormComponent)parent).add((IValidator)node);
		}
	}
	
	protected Object createNode(Object name, Map atts, Object value) {
		String sName = (String) name;
		
		//Assume component if of type String
		if(value instanceof String)
		{
			String key = (String)value;
			
			try
			{
				WicketComponentBuilder helper = WicketComponentBuilderFactory.generateComponentBuilder(sName, context);
				
				//If not found, try the other guys
				if(helper != null)
				{
					if(atts == null)
						atts = Collections.EMPTY_MAP;
					
					if(developmentMode)
						helper.writeViewTagStart(tagTemplate);
					
					Component result = helper.create(key, atts);
					
					if(result instanceof WicketComponentInitNotifier)
						((WicketComponentInitNotifier)result).init();
					
					generatedComponentBuilderCache.put(result, helper);
					
					return result;
				}
			}
			catch(RuntimeException e)
			{
				throw e;
			}
			catch(Exception e)
			{
				throw new RuntimeException("Error with the builder", e);
			}
		}
		
		Object builderObj = WicketComponentBuilderFactory.getComponentAccentForName(sName);
		
		if(builderObj == null)
			throw new WicketComponentBuilderException("Builder for name '"+ sName +" not found");
		
		return handleMultiTypeBuilder(builderObj, sName, value, atts);
	}

	protected Object createNode(Object name, Map atts) {
		String sName = (String) name;
		Object builderObj = WicketComponentBuilderFactory.getComponentAccentForName(sName);
		
		if(builderObj == null)
			throw new WicketComponentBuilderException("Builder for name '"+ sName +" not found");
		
		return handleMultiTypeBuilder(builderObj, sName, null, atts);
	}
	
	private Object handleMultiTypeBuilder(Object builder, String name, Object defaultArg, Map attributes)
	{
		if(builder instanceof WicketModelBuilder)
		{
			WicketModelBuilder modelBuilder = (WicketModelBuilder) builder;
			try
			{
				IModel model = modelBuilder.create(defaultArg, attributes);
				return model;
			}
			catch(WicketComponentBuilderException e)
			{
				throw e;
			}
			catch (Exception e)
			{
				throw new WicketComponentBuilderException("Error building model", e);
			}
			
		}
		
		throw new WicketComponentBuilderException("No build type found");
	}

	protected Object createNode(Object name, Object value) {
		return createNode(name, null, value);
	}

	protected Object createNode(Object name) {
		return createNode(name, null);
	}

	protected void setParent(Object parent, Object child) {
			
		
	}
	
	public void kickStart(MarkupContainer container, Closure closure)
	{
		setCurrent(container);

        // lets register the builder as the delegate
        setClosureDelegate(closure, container);
        closure.call(container);
	}

	public void add(Component component)
	{
		((MarkupContainer)getCurrent()).add(component);
	}
	
	public void add(IValidator validator)
	{
		if(getCurrent() instanceof FormComponent)
			((FormComponent)getCurrent()).add(validator);
	}
	
	public void add(IBehavior behavior)
	{
		if(getCurrent() instanceof Component)
			((Component)getCurrent()).add(behavior);
	}

	
}
