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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import wicket.Component;
import wicket.contrib.groovy.builder.BaseComponentBuilder;
import wicket.contrib.groovy.builder.WicketComponentBuilderException;

/**
 * 
 * @author Kevin Galligan
 *
 */
public class ExternalLinkComponentBuilder extends BaseComponentBuilder
{

	public ExternalLinkComponentBuilder(Class componentClass)
	{
		super(componentClass);
	}

	public Component create(String key, Map attributes) throws Exception
	{
		Component component = createComponentInstace(key, attributes);
		
		setOtherProperties(component, attributes);
		
		return component;
	}

	public List getConstructorParameters(String key, Map attributes)
	{
		if(attributes == null)
			attributes = Collections.EMPTY_MAP;
		
		Object href = attributes.remove("href");
		Object label = attributes.remove("label");
		
		if(href == null)
		{
			throw new WicketComponentBuilderException("ExternalLink requires 'href'");
		}
		
		List args = new ArrayList(3);
		args.add(key);
		
		if(href != null)
		{
			args.add(href);
			if(label != null)
			{
				args.add(label);
			}
		}
		
		return args;
	}

}
