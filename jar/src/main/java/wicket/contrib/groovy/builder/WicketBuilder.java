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

import wicket.Component;
import wicket.MarkupContainer;
import wicket.behavior.IBehavior;
import wicket.markup.html.form.FormComponent;
import wicket.markup.html.form.validation.IValidator;

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

	public WicketBuilder(MarkupContainer context) {
		this.context = context;
		setCurrent(context);
	}

	protected Object createNode(Object name, Map atts, Object value) {
		String sName = (String) name;
		String key = (String)value;
		
		try
		{
			WicketComponentBuilder helper = WicketComponentBuilderFactory.generateComponentBuilder(sName, context);
			
			if(atts == null)
				atts = Collections.EMPTY_MAP;
			
			Component result = helper.create(key, atts);
			
			if(result instanceof WicketComponentInitNotifier)
				((WicketComponentInitNotifier)result).init();
			
			return result;
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

	protected Object createNode(Object name, Map atts) {
		
		return null;
	}

	protected Object createNode(Object name, Object value) {
		return createNode(name, null, value);
	}

	protected Object createNode(Object name) {
		return null;
	}

	protected void setParent(Object parent, Object child) {
			
		if (parent instanceof MarkupContainer && child instanceof Component) {
			MarkupContainer containerParent = (MarkupContainer) parent;
			Component componentChild = (Component) child;

			containerParent.add(componentChild);
		}
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
