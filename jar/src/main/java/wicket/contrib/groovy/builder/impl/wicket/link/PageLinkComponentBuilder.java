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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import wicket.Page;
import wicket.contrib.groovy.builder.WicketComponentBuilderException;
import wicket.contrib.groovy.builder.util.AttributeUtils;

/**
 * 
 * @author Kevin Galligan
 *
 */
public class PageLinkComponentBuilder extends LinkComponentBuilder
{

	public PageLinkComponentBuilder(Class componentClass)
	{
		super(componentClass);
	}

	public List getConstructorParameters(String key, Map attributes)
	{
		List args = new ArrayList();
		
		args.add(key);
		
		Object classObj = attributes.remove("class");
		
		if(classObj != null)
		{
			try
			{
				args.add(AttributeUtils.classValue(classObj));
			}
			catch (ClassNotFoundException e)
			{
				throw new WicketComponentBuilderException(e);
			}
			
			return args;
		}
		
		Object pageObj = attributes.remove("page");
		
		if(pageObj != null && pageObj instanceof Page)
		{
			args.add(pageObj);
			return args;
		}
		
		throw new WicketComponentBuilderException("'page' or 'class' required");
	}
}
