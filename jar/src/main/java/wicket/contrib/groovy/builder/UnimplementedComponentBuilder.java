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

import java.util.List;
import java.util.Map;

import wicket.Component;

/**
 * For complex components, or ones that simply don't have an implementation yet.  Put this in the tree.
 * Will prevent users from attempting to use the component type (you can still manually create a component
 * with the builder, as always).
 * 
 * @author Kevin Galligan
 *
 */
public class UnimplementedComponentBuilder extends BaseComponentBuilder
{

	public UnimplementedComponentBuilder(Class componentClass)
	{
		super(componentClass);
	}

	public Component create(String key, Map attributes) throws Exception
	{
		throw new UnimplementedComponentBuilderException(getTargetClass().getName());
	}

	/**
	 * Don't care about this, obviously.
	 */
	public List getConstructorParameters(String key, Map attributes)
	{
		return null;
	}

}
