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
import java.util.List;
import java.util.Map;

import wicket.Component;
import wicket.contrib.groovy.builder.BaseComponentBuilder;

/**
 * This handles the vast majority of cases.  Override this to build customs in all
 * but the most complex cases.
 * 
 * @author Kevin Galligan
 *
 */
public class GenericComponentBuilder extends BaseComponentBuilder
{
	public GenericComponentBuilder(Class componentClass)
	{
		super(componentClass);
	}

	public Component create(String key, Map attributes) throws Exception
	{
		Component component = createComponentInstace(key, attributes);
		setModel(component, attributes);
		
		setOtherProperties(component, attributes);
		
		return component;
	}

	public List getConstructorParameters(String key, Map attributes)
	{
		List retList = new ArrayList(1);
		retList.add(key);
		return retList;
	}
}
