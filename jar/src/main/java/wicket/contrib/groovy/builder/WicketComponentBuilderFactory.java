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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import wicket.MarkupContainer;
import wicket.contrib.groovy.builder.impl.wicket.DefaultComponentBuilderFactoryConfigurationProvider;

/**
 * Factory of factories.  Finds the proper implementation of WicketComponentBuilder for the given 
 * 'name'.
 * 
 * To find builders, it first looks at the local package.  If there's a match, it uses that.  Otherwise, it'll
 * recurse through the packages provided.  It'll then attempt to match the found class type with a builder
 * and return it.
 * 
 * To customize the implementation, call addConfiguration(WicketGroovyConfigurationProvider provider).
 * 
 * TODO:  We need some caching for the found builders.  The code is there, but there was some other trouble,
 * so for now I've disabled it. 
 * 
 * @author Kevin Galligan
 *
 */
public class WicketComponentBuilderFactory {
	static Map nameHelperMapping = new HashMap();
	
	static List searchPackageList = new ArrayList(); 
	
	static Map localPackageNameHelperMappingMap = new HashMap();
	
	
	protected static WicketComponentBuilder checkLocalPackage(String name, MarkupContainer context)
	{
		Map localPackageNameHelperMapping = (Map) localPackageNameHelperMappingMap.get(context.getClass().getPackage().getName());
		if(localPackageNameHelperMapping != null && localPackageNameHelperMapping.get(name)!= null)
			return (WicketComponentBuilder) localPackageNameHelperMapping.get(name);
		
		return null;
	}
	
	protected static void putLocalPackage(String name, MarkupContainer context, WicketComponentBuilder builder)
	{
		String packageName = context.getClass().getPackage().getName();
		Map localPackageNameHelperMapping = (Map) localPackageNameHelperMappingMap.get(packageName);
		
		if(localPackageNameHelperMapping == null)
		{
			localPackageNameHelperMapping = new HashMap();
			localPackageNameHelperMappingMap.put(packageName, localPackageNameHelperMapping);
		}
		
		localPackageNameHelperMapping.put(name, builder);
	}
	
	protected static Class checkPackageForComponent(String packageName, String name) throws ClassNotFoundException
	{
		String className = packageName + "." + StringUtils.capitalize(name);
		return Class.forName(className);
	}
	
	public static WicketComponentBuilder generateComponentBuilder(String name, MarkupContainer context)throws Exception
	{
		WicketComponentBuilder localBuilderClass = checkLocalPackage(name, context);
		if(localBuilderClass != null)
			return localBuilderClass;
		
		if(nameHelperMapping.get(name)!= null)
			return (WicketComponentBuilder) nameHelperMapping.get(name);
		
		Class componentClass = null;
		boolean localPackage = false;
		
		try{
		componentClass = checkPackageForComponent(context.getClass().getPackage().getName(), name);
		} catch (ClassNotFoundException e) {}
		
		if(componentClass != null)
			localPackage = true;
		else
		{
			//TODO: Also need a more robust configuration tree.  Based on different levels:
			//Global > Applicaiton > package > Page
			//Right now, a local class in the page would override a global on if it were found
			//first.  Again, this was mostly written in a day, so you get what you pay for
			
			for(int i=0; i<searchPackageList.size(); i++)
			{
				try {
					componentClass = checkPackageForComponent((String)searchPackageList.get(i), name);
					if(componentClass != null)
						break;
				} catch (ClassNotFoundException e) {}
			}
			
			if(componentClass == null)
				return null;
//				throw new UnsupportedOperationException("Can't find type '"+ name +"'");
		}
		
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
		
//		if(localPackage)
//		{
//			putLocalPackage(name, context, helper);
//		}
//		else
//		{
//			nameHelperMapping.put(name, helper);
//		}
		return helper;
	}
	
	static ClassHierarchyTree helperTree = new ClassHierarchyTree();
	static List configurationProviders = new ArrayList();
	static Map componentAccentMap = new HashMap();
	
	public static Object getComponentAccentForName(String name)
	{
		return componentAccentMap.get(name);
	}
	
	static
	{
		addConfiguration(new DefaultComponentBuilderFactoryConfigurationProvider());
	}
	
	public static void addConfiguration(WicketGroovyConfigurationProvider provider)
	{
		configurationProviders.add(provider);
		provider.getConfiguration().augmentHelperClassHierarchy(helperTree);
		provider.getConfiguration().addComponentAccentDefinitions(componentAccentMap);
		
		String[] packageList = provider.getConfiguration().componentPackageSearchList();
		if(packageList != null)
			Collections.addAll(searchPackageList, packageList);
	}
}
