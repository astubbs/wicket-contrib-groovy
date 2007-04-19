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
package wicket.contrib.groovy.builder.impl.wicket;

import groovy.lang.Closure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import wicket.Component;
import wicket.contrib.groovy.builder.ListViewPopulateItemClosure;

/**
 * This one will be complicated.  It will have to support special 'populateItem' handling 
 * in certain cases.
 * 
 * @author Kevin Galligan
 *
 */
public class ListViewComponentBuilder extends GenericComponentBuilder
{
	//This is very, very clunky, but I have other things to do with life than re-architect again
	ThreadLocal populateItemClosureExists = new ThreadLocal();
	
	public ListViewComponentBuilder(Class componentClass)
	{
		super(componentClass);
	}

	/**
	 * Supports adding a list of values directly
	 */
	public List getConstructorParameters(String key, Map attributes)
	{
		List args = new ArrayList();
		args.add(key);
		
		Object list = attributes.remove("list");
		if(list != null)
			args.add(list);
		
		return args;
	}

	public Component create(String key, Map attributes) throws Exception
	{
		Object populateItem = attributes.remove("populateItem");
		if(populateItem == null)
		{
			populateItemClosureExists.set(Boolean.FALSE);
			return super.create(key, attributes);
		}
		
		populateItemClosureExists.set(Boolean.TRUE);
				
		Component component = super.create(key, attributes);
		
		((ListViewPopulateItemClosure)component).setPopulateItemClosure((Closure) populateItem);
		
		return component;
	}

	protected String injectExtraCode()
	{
		if(((Boolean)populateItemClosureExists.get()).booleanValue())
		{
			StringBuffer pic = new StringBuffer();
			pic.append("wicket.contrib.groovy.builder.ClosureScriptWrapper wrapper\n");
			pic.append("public void setPopulateItemClosure(Closure closure){\n");
			pic.append("wrapper = new wicket.contrib.groovy.builder.ClosureScriptWrapper(closure)\n");
			pic.append("}\n\n");
			pic.append("protected void populateItem(wicket.markup.html.list.ListItem item){\n");
			pic.append("wicket.contrib.groovy.builder.WicketBuilder builder = new wicket.contrib.groovy.builder.WicketBuilder(item)\n");
			pic.append("builder.kickStart(item, wrapper.getClosure(this))\n");
			pic.append("}\n");
			
			return pic.toString();
		}
		
		return null;
	}

	protected String injectInterfaces()
	{
		if(((Boolean)populateItemClosureExists.get()).booleanValue())
		{
			return "wicket.contrib.groovy.builder.ListViewPopulateItemClosure";
		}
		
		return null;
	}

	protected void writeSimpleViewTagEnd()
	{
		writeViewTagText("</tr></table>\n");
	}

	protected void writeSimpleViewTagStart(String key)
	{
		writeViewTagText("<table><tr wicket:id='"+ key +"'>\n");
	}

	
}
