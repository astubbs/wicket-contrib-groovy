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
import wicket.contrib.groovy.builder.util.AttributeUtils;

/**
 * 
 * @author Kevin Galligan
 *
 */
public class TextFieldComponentBuilder extends FormComponentComponentBuilder
{
	public TextFieldComponentBuilder(Class componentClass)
	{
		super(componentClass);
	}

	protected void writeSimpleViewTagStart(String key)
	{
		writeViewTagText("<input type='text' wicket:id='"+ key +"'/>\n");
	}

	public List getConstructorParameters(String key, Map attributes)
	{
		List params = new ArrayList();
		params.add(key);
		Object type;
		try
		{
			type = attributes.remove("type");
			if(type != null)
				params.add(AttributeUtils.classValue(type));
			
			return params;
		}
		catch (ClassNotFoundException e)
		{
			throw new WicketComponentBuilderException("Couldn't convert class", e);
		}
	}
	
	
}
