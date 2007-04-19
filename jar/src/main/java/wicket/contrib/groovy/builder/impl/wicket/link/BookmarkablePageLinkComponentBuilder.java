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

import wicket.PageParameters;
import wicket.contrib.groovy.builder.WicketComponentBuilderException;
import wicket.contrib.groovy.builder.util.AttributeUtils;

/**
 * Parameters can be supplied as a PageParameters object, or a simple name:value map.
 * The key should be either "params", "parameters", "pageParameters".
 * 
 * @author Kevin Galligan
 *
 */
public class BookmarkablePageLinkComponentBuilder extends LinkComponentBuilder
{
	static final String[] PARAMS_NAMES = new String[]{"params", "parameters", "pageParameters"};
	public BookmarkablePageLinkComponentBuilder(Class componentClass)
	{
		super(componentClass);
	}

	public List getConstructorParameters(String key, Map attributes)
	{
		List args = new ArrayList();
		
		args.add(key);
		
		Object classObj = attributes.remove("class");
		if(classObj == null)
			throw new WicketComponentBuilderException("BookmarkablePageLink requires 'class'");
		
		try
		{
			args.add(AttributeUtils.classValue(classObj));
		}
		catch (ClassNotFoundException e)
		{
			throw new WicketComponentBuilderException(e);
		}
		
		Object params = AttributeUtils.multiName(attributes, PARAMS_NAMES);
		
		if(params != null)
		{
			PageParameters pageParams;
			if(params instanceof Map)
				pageParams = new PageParameters((Map)params);
			else if(params instanceof PageParameters)
				pageParams = (PageParameters) params;
			else
				throw new WicketComponentBuilderException("params must be of type Map or PageParameters");
			
			args.add(pageParams);
		}
		
		return args;
	}

}
