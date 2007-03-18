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
import groovy.lang.MetaClass;
import groovy.lang.MetaClassRegistry;
import groovy.util.BuilderSupport;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import wicket.Component;
import wicket.MarkupContainer;
import wicket.Page;
import wicket.behavior.IBehavior;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.FormComponent;
import wicket.markup.html.form.validation.IValidator;

public class WicketBuilder extends BuilderSupport {

	HashMap objects;
	
	MarkupContainer context;

//	static
//	{
//		//Do some init stuff
//		MetaClassRegistry registry = MetaClassRegistry.getInstance(0);
//		MetaClass metaClass = registry.getMetaClass(Form.class);
//		
//		metaClass.addNewInstanceMethod(WicketBuilderAddMethods.class.getMethods()[0]);
//		
//	}

	public WicketBuilder(MarkupContainer context) {
		System.out.println("Heyo: context == "+ context);
		this.context = context;
		setCurrent(context);
	}

	protected Object createNode(Object name, Map atts, Object value) {
		String sName = (String) name;
		String key = (String)value;
		System.out.println("name: "+ name +" | value: "+ value);
		
		try
		{
			WicketComponentBuilder helper = WicketComponentBuilderFactory.generateComponentBuilder(sName, key, atts, context);
			
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
		System.out.println("name2: "+ name);
		
		return null;
	}

	protected Object createNode(Object name, Object value) {
		return createNode(name, null, value);
	}

	protected Object createNode(Object name) {
		System.out.println("name: "+ name);
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
