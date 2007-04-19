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

/**
 * 
 * @author Kevin Galligan
 *
 */
public class AbstractSingleSelectChoiceComponentBuilder extends GenericComponentBuilder
{
	public AbstractSingleSelectChoiceComponentBuilder(Class componentClass)
	{
		super(componentClass);
	}

	public List getConstructorParameters(String key, Map attributes)
	{
		List retList = new ArrayList(2);
		retList.add(key);
		
		Object choices = attributes.remove("choices");
		
		if(choices != null)
			retList.add(choices);
		
		return retList;
	}
}
