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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.commons.lang.StringUtils;

import wicket.MarkupContainer;
import wicket.Page;
import wicket.contrib.groovy.builder.impl.wicket.DefaultComponentBuilderFactoryConfigurationProvider;

public class WicketComponentBuilderFactory {
	static Map nameHelperMapping = new HashMap();
	
	static List searchPackageList = new ArrayList(); 
	
	public static WicketComponentBuilder generateComponentBuilder(String name, String key, Map attrs, MarkupContainer context)throws Exception
	{
		if(nameHelperMapping.get(name)!= null)
			return (WicketComponentBuilder) nameHelperMapping.get(name);
		
		Class componentClass = null;
		
		//TODO: Create a cache based on Page object so we're not duplicating this every time
		//TODO: Also need a more robust configuration tree.  Based on different levels:
		//Global > Applicaiton > package > Page
		//Right now, a local class in the page would override a global on if it were found
		//first.  Again, this was mostly written in a day, so you get what you pay for
		List allPackages = new ArrayList(searchPackageList);
		allPackages.add(context.getClass().getPackage().getName());
		
		for(int i=0; i<allPackages.size(); i++)
		{
			try {
				String className = allPackages.get(i) + "." + StringUtils.capitalize(name);
				System.out.println(className);
				componentClass = Class.forName(className);
				if(componentClass != null)
					break;
			} catch (ClassNotFoundException e) {}
		}
		
		if(componentClass == null)
			throw new UnsupportedOperationException("Can't find type '"+ name +"'");
		
		Method localBuilder = null;
		
		try
		{
			localBuilder = componentClass.getMethod("localBuilder", new Class[0]);
		}
		catch(Exception e)
		{}
		
		if(localBuilder != null)
		{
			return (WicketComponentBuilder) localBuilder.invoke(null, new Object[0]);
		}
		
		Class wicketNodeHelperClass = (Class)helperTree.getBest(componentClass);
		WicketComponentBuilder helper = (WicketComponentBuilder) wicketNodeHelperClass.getConstructors()[0].newInstance(new Object[]{componentClass});
		
		nameHelperMapping.put(name, helper);
		return helper;
	}
	
	static ClassHierarchyTree helperTree = new ClassHierarchyTree();
	static List configurationProviders = new ArrayList();
	
	static
	{
		addConfiguration(new DefaultComponentBuilderFactoryConfigurationProvider());
	}
	
	public static void addConfiguration(WicketGroovyConfigurationProvider provider)
	{
		configurationProviders.add(provider);
		provider.getConfiguration().augmentHelperClassHierarchy(helperTree);
		String[] packageList = provider.getConfiguration().componentPackageSearchList();
		if(packageList != null)
			Collections.addAll(searchPackageList, packageList);
	}
}
