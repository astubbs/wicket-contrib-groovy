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

import java.util.Collections;
import java.util.Map;

import wicket.Component;
import wicket.contrib.groovy.builder.WicketComponentBuilderException;
import wicket.markup.html.form.FormComponent;
import wicket.markup.html.form.FormComponentLabel;

/**
 * This one is lame.  The builder could grab the current component from the context, but 
 * that would mess with the current plumbing.
 * 
 * @author Kevin Galligan
 *
 */
public class FormComponentLabelComponentBuilder extends GenericComponentBuilder
{

	public FormComponentLabelComponentBuilder(Class componentClass)
	{
		super(componentClass);
	}

	private static final Class[] CONSTRUCTOR_FC = new Class[]{String.class, FormComponent.class};
	public Component create(String key, Map attributes) throws Exception
	{
		if(attributes == null)
			attributes = Collections.EMPTY_MAP;
		
		if(attributes.get("formComponent") == null)
		{
			throw new WicketComponentBuilderException("FormComponentLabel requires a formComponent argument");
		}
		
		FormComponentLabel label = (FormComponentLabel) getTargetClass().getConstructor(CONSTRUCTOR_FC).newInstance(new Object[]{key, attributes.get("formComponent")});		
		
		return label;
	}
	
	
}
