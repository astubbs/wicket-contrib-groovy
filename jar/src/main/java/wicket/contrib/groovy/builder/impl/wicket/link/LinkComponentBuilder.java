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

import java.util.List;
import java.util.Map;


import wicket.Component;
import wicket.MarkupContainer;
import wicket.contrib.groovy.builder.impl.wicket.GenericComponentBuilder;
import wicket.markup.html.basic.Label;
import wicket.model.IModel;
import wicket.model.Model;

public class LinkComponentBuilder extends GenericComponentBuilder
{

	public LinkComponentBuilder(Class componentClass)
	{
		super(componentClass);
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
			
		}
		
		
		setOtherProperties(component, attributes);
		
		return component;
	}

}
