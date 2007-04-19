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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import wicket.contrib.groovy.builder.WicketComponentBuilderException;
import wicket.contrib.groovy.builder.util.AttributeUtils;
import wicket.model.IModel;
import wicket.model.Model;

/**
 * 
 * @author Kevin Galligan
 *
 */
public class LoopComponentBuilder extends GenericComponentBuilder
{
	static final String[] INTERATIONS_NAMES = new String[]{"iterations", "times", "loops"};
	public LoopComponentBuilder(Class componentClass)
	{
		super(componentClass);
	}

	public List getConstructorParameters(String key, Map attributes)
	{
		List args = new ArrayList(2);
		args.add(key);
		Object iterations = AttributeUtils.multiName(attributes, INTERATIONS_NAMES);
		if(iterations != null)
		{
			if(iterations instanceof Number)
				args.add(new Model((Serializable) iterations));
			else if(iterations instanceof String)
				args.add(new Model(new Integer((String)iterations)));
			else if(iterations instanceof IModel)
				args.add((IModel)iterations);
			else
				throw new WicketComponentBuilderException("iterations type not known");
		}
		
		throw new WicketComponentBuilderException("iterations param required for Loop"); 
	}

	
}
