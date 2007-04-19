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
package wicket.contrib.groovy.builder.impl.wicket.link;

import java.util.Map;

import wicket.Component;
import wicket.MarkupContainer;
import wicket.contrib.groovy.builder.impl.wicket.GenericComponentBuilder;
import wicket.markup.html.basic.Label;
import wicket.model.IModel;

/**
 * Base for link component builders.  This is pretty simple, but adds a convenience feature.
 * If you supply "label" in the map of attributes, a Label component with key 'label'
 * will be automatically inserted under the link.  So, you could have the following:
 * 
 * link("linkKey", label:"Click Me", onClick:{println "Clicked"})
 * 
 * <a href="#" wicket:id="linkKey"><span wicket:id="label"></span></a>
 * 
 * @author Kevin Galligan
 *
 */
public class LinkComponentBuilder extends GenericComponentBuilder
{

	public LinkComponentBuilder(Class componentClass)
	{
		super(componentClass);
	}

	protected void writeSimpleViewTagStart(String key)
	{
		writeViewTagText("<a href='#' wicket:id='"+ key +"'>\n");
	}
	
	protected void writeSimpleViewTagEnd()
	{
		writeViewTagText("</a>\n");
	}


	/**
	 * Override this to provide a little shorthand for links.  If 'label' is provided, 
	 * a label component is added under the link, with 'label' as the key.
	 */
	public Component create(String key, Map attributes) throws Exception
	{
		Component component = createComponentInstace(key, attributes);
		setModel(component, attributes);
		
		Object label = attributes.remove("label");
		Label labelComp;
		
		if(label != null)
		{
			if(label instanceof IModel)
				labelComp = new Label("label", (IModel)label);
			else
				labelComp = new Label("label", label.toString());
			
			((MarkupContainer)component).add(labelComp);
			
			if(isViewTagWriter())
				writeViewTagText("<span wicket:id='"+ label +"'></span>");
		}
		
		
		setOtherProperties(component, attributes);
		
		return component;
	}

}
