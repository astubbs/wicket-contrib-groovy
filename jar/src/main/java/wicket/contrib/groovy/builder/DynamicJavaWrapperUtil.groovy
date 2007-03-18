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
 * I can't figure out if this is really smart or really dumb.  The idea is to subclass
 * java objects to allow custom MetaClass proxys.  This will allow method overriding on 
 * arbitrary objects with Closure objects.  Sounds complicated, but its not really.
 */
class DynamicJavaWrapperUtil {

	 public static String buildConstructorOverrides(String className, Class javaClass)
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
	 
	 public static String buildMehodOverrides(Class javaClass, List methods)
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
			code.append("def script = scriptMap.get('${meth.name}').run(this, args)\n")
			code.append("}\n\n")
			code.append("${visibility} ${meth.returnType.name} super_${meth.name}(")
			code.append(methodParams.toString())
			code.append("){\nsuper.${meth.name}("+ callBuffer.toString() +")\n}\n\n")
		}
		
		return code.toString()
	 }
	 static int count = 0
	
	public static Class wrapClass(Class javaClass, List methods, List closures)
	{
	 	return wrapClass(javaClass, methods, closures, null, null)
	}
	 
	public static Class wrapClass(Class javaClass, List methods, List closures, String extraCode, String interfaces)
	{
		 if(extraCode == null)
			 extraCode = ""

		if(interfaces == null)
			interfaces = ""
		else
			interfaces = "implements "+ interfaces
			
		Class baseClass = javaClass
		
		//Not sure the count is needed.  I've generated dynamic classes with the same name without trouble
		//This is a "just in case" type of deal
		String className = "GroovyOverride" + (count++).toString()
		
		String constructors = buildConstructorOverrides(className, javaClass)
		String methodsString = buildMehodOverrides(javaClass, methods)
		
		String classTemplate = """
	import wicket.contrib.groovy.builder.ScriptWrapper
	import java.lang.reflect.Method
	import java.lang.reflect.Modifier
	
	public class ${className} extends ${baseClass.name} ${interfaces}{

	${constructors}

	static Map scriptMap = [:]
	
	public static void addScript(String methodName, ScriptWrapper wrapper)
	{
		scriptMap.put(methodName, wrapper)
	}

	${methodsString}

	${extraCode}
} 

return ${className}.class
"""
		println classTemplate
		
		GroovyShell shell = new GroovyShell()
		Class newClass = shell.evaluate(classTemplate)
		
		Method addScriptMethod = newClass.getMethod("addScript", (Class[])[String.class, ScriptWrapper.class])
		
		Object[] args = new Object[2]
		int count = 0
		for(meth in methods)
		{
			args[0] = meth.name
			args[1] = new ClosureScriptWrapper(closures[count++])
			                   
			addScriptMethod.invoke(null, args)
		}

		return newClass
	}

}