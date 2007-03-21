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
package wicket.contrib.groovy.builder.impl.databinder;

import java.util.List;
import java.util.Map;

import wicket.Component;
import wicket.contrib.groovy.builder.BaseComponentBuilder;
import wicket.contrib.groovy.builder.WicketComponentBuilderException;
import wicket.contrib.groovy.builder.util.AttributeUtils;
import wicket.contrib.groovy.builder.util.ListConstructors;

/**
 * 
 * @author Kevin Galligan
 *
 */
public class StyleLinkComponentBuilder extends BaseComponentBuilder
{

	public StyleLinkComponentBuilder(Class componentClass)
	{
		super(componentClass);
	}

	public List getConstructorParameters(String key, Map attributes)
	{
		Object clazz = attributes.remove("class");
		if(clazz == null)
			throw new WicketComponentBuilderException("class param required for StyleLink");
		
		try
		{
			return ListConstructors.newList(key, AttributeUtils.classValue(clazz));
		}
		catch (ClassNotFoundException e)
		{
			throw new WicketComponentBuilderException("Can't create construction params", e);
		}
	}

	public Component create(String key, Map attributes) throws Exception
	{
		Component component = createComponentInstace(key, attributes);
		setOtherProperties(component, attributes);
		
		return component;
	}

}
