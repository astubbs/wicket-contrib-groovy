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

import org.codehaus.groovy.runtime.InvokerHelper
import java.lang.reflect.Modifier
import java.lang.reflect.Method

/**
 * This is a little complex.  It overrides component classes, if needed.  The overriden methods are
 * given closures to execute when called.
 *
 * @author Kevin Galligan
 */
class DynamicJavaWrapperUtil implements DynamicJavaWrapper {

	 public String buildConstructorOverrides(String className, Class javaClass)
	 {
		StringBuffer code = new StringBuffer();
		
		for(cons in javaClass.getConstructors())
		{
			if(Modifier.isPublic(cons.getModifiers()))
			{
				code.append("public ${className}(")
				StringBuffer callBuffer = new StringBuffer()
				Iterator paramIter = cons.getParameterTypes().toList().iterator()
				int count = 0;
				while(paramIter.hasNext())
				{
					Class param = paramIter.next();
					String paramName = "arg" + count.toString()
					count += 1
					code.append(param.getName() + " " + paramName)
					callBuffer.append(paramName)
					if(paramIter.hasNext())
					{
						code.append(",")
						callBuffer.append(",")
					}
				}
				code.append("){\nsuper(" + callBuffer.toString() + ")\n}\n\n")
			}
		}
		
		return code.toString()
	 }
	 
	 public String buildMehodOverrides(Class javaClass, List methods)
	 {
		StringBuffer code = new StringBuffer();
		
		for(meth in methods)
		{
			String visibility;
			if(Modifier.isPublic(meth.getModifiers()))
			{
				visibility = "public"
			}
			else if(Modifier.isProtected(meth.getModifiers()))
			{
				visibility = "protected"
			}
			else
			{
				continue
			}
			
			StringBuffer methodParams = new StringBuffer()
			StringBuffer callBuffer = new StringBuffer()
			Iterator paramIter = meth.getParameterTypes().toList().iterator()
			int count = 0;
			while(paramIter.hasNext())
			{ 
				Class param = paramIter.next();
				String paramName = "arg" + count.toString()
				count += 1
				methodParams.append(param.getName() + " " + paramName)
				callBuffer.append(paramName)
				if(paramIter.hasNext())
				{
					methodParams.append(",")
					callBuffer.append(",")
				}
			}
			code.append("${visibility} ${meth.returnType.name} ${meth.name}(")
			code.append(methodParams.toString())
			code.append("){\nObject[] args = (Object[])[" + callBuffer.toString() + "]\n")
			code.append("def scriptResult = scriptMap.get('${meth.name}').run(this, args)\n")
			code.append("}\n\n")
			code.append("${visibility} ${meth.returnType.name} super_${meth.name}(")
			code.append(methodParams.toString())
			code.append("){\nsuper.${meth.name}("+ callBuffer.toString() +")\n}\n\n")
		}
		
		return code.toString()
	 }
	 static int count = 0
	
	public Class wrapClass(Class javaClass, List methods)
	{
	 	return wrapClass(javaClass, methods, null, null)
	}
	 
	 Map cachedClasses = [:]
	                      
	public Class wrapClass(Class javaClass, List methods, String extraCode, String interfaces)
	{
		WicketComponentOverrideDescriptor descriptor = 
			new WicketComponentOverrideDescriptor(javaClass:javaClass, methods:methods,
					 extraCode:extraCode, interfaces:interfaces)
		
		Class componentClass = cachedClasses.get(descriptor)
		
		if(componentClass != null)
		{
			println "pulled from cache"
			return componentClass
		}
		
		 if(extraCode == null)
			 extraCode = ""

		if(interfaces == null)
			interfaces = ""
		else
			interfaces = ", "+ interfaces
			
		Class baseClass = javaClass
		
		//Not sure the count is needed.  I've generated dynamic classes with the same name without trouble
		//This is a "just in case" type of deal
		String className = "GroovyOverride" + (count++).toString()
		
		String constructors = buildConstructorOverrides(className, javaClass)
		String methodsString = buildMehodOverrides(javaClass, methods)
		 
		String classTemplate = """
	import wicket.contrib.groovy.builder.ScriptWrapper
	import wicket.contrib.groovy.builder.DynamicJavaWrapperScriptable
	import java.lang.reflect.Method
	import java.lang.reflect.Modifier
	
	public class ${className} extends ${baseClass.name} implements DynamicJavaWrapperScriptable ${interfaces}{

	${constructors}

	Map scriptMap = [:]
	
	public void addScript(String methodName, ScriptWrapper wrapper)
	{
		scriptMap.put(methodName, wrapper)
	}

	${methodsString}

	${extraCode}
} 

return ${className}.class
"""
//		println classTemplate
		
		GroovyShell shell = new GroovyShell()
		Class newClass = shell.evaluate(classTemplate)
		

		//Turn off cache for now.  Causes problems with stale closure owner references
		//TODO: Update caching such that ClosureScriptWrapper instances are added to each object, not
		//at the class level
		cachedClasses.put(descriptor, newClass)
		
		return newClass
	}

	 public void fillMethods(DynamicJavaWrapperScriptable scriptable, List methods, List closures)
	 {
		 for(i in 0..<closures.size())
		{
			Closure closure = (Closure) closures.get(i);
			Method method = (Method) methods.get(i);
			
			scriptable.addScript(method.getName(), new ClosureScriptWrapper(closure));
		}
	 }
}