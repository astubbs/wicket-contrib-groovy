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

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.SingletonMap;

/**
 * Originally this was used because Closures aren't serializable.  Turns out, Closure Classes are.
 * This should be removed, unless there is a good use case for it.
 * 
 * @deprecated
 * 
 * TODO: Figure out if the script shell can be kept around.
 * 
 * @author Kevin Galligan
 *
 */
public class StringScriptWrapper implements Serializable, ScriptWrapper
{
	String scriptString;
	static Map parsedScriptCache = new HashMap();
	
	public StringScriptWrapper(String scriptString)
	{
		this.scriptString = scriptString;
	}
	
	transient Script script;
	
	/* (non-Javadoc)
	 * @see wicket.contrib.groovy.builder.ScriptWrapper#run(java.lang.Object)
	 */
	public Object run(Object context, Object[] args) {
		if(scriptString == null)
			return null;
		
		if(script == null)
		{
			Script script = (Script) parsedScriptCache.get(scriptString);
			if(script == null)
			{
				synchronized(parsedScriptCache)
				{
					GroovyShell shell = new GroovyShell();
					script = shell.parse(scriptString);
					parsedScriptCache.put(scriptString, script);
				}
			}
		}
		
		Binding binding = new Binding(new SingletonMap("this", context));
		
		synchronized(script)
		{
			script.setBinding(binding);
			Object result = script.run();
			script.setBinding(new Binding());
			
			return result;
		}
	}
}
