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

import wicket.contrib.groovy.builder.WicketComponentBuilderException;

/**
 * 
 * @author Kevin Galligan
 *
 */
public class FragmentComponentBuilder extends GenericComponentBuilder
{
	public FragmentComponentBuilder(Class componentClass)
	{
		super(componentClass);
	}

	public List getConstructorParameters(String key, Map attributes)
	{
		List args = new ArrayList();
		
		args.add(key);
		String markupId = (String) attributes.get("markupId");
		
		if(markupId == null)
			throw new WicketComponentBuilderException("markupId required for Fragment");
		
		args.add(markupId);
		
		Object markupProvider = attributes.get("markupProvider");
		
		if(markupProvider != null)
			args.add(markupProvider);
		
		return args;
	}

	
}
