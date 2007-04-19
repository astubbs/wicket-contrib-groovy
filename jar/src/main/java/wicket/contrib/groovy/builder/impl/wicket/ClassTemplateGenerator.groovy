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

/**
 * TODO: This is old.  Should probably go away.
 *
 * @author Kevin Galligan
 *
 */
class ClassTemplateGenerator {
	
	static Map formOverrideClasses = new HashMap();
	
	/**
	 * TODO: Should be able to cache based on input class
	 *
	 * Uncomment the 'super' methods when the Inconsistent stack trace is sorted out
	 * (Groovy issue)
	 */
	public static Class createFormOverride(Class form)
	{
		Class foc = formOverrideClasses.get(form)
		
		if(foc == null)
		{
			String classBase = """
import wicket.contrib.groovy.builder.*

public class WicketBuilderFormOverride extends ${form.name} {
	public WicketBuilderFormOverride(String key){super(key)}

	ScriptWrapper onSubmitWrapper;
	ScriptWrapper onErrorWrapper;

	public void onSubmit()
	{
		if(onSubmitWrapper != null)
			onSubmitWrapper.run(this);
		//else
		//	super.onSubmit();
	}

	public void onError()
	{
		if(onErrorWrapper != null)
			onErrorWrapper.run(this);
		//else
		//	super.onError();
	}
}

return WicketBuilderFormOverride.class 
"""
		
			GroovyShell shell = new GroovyShell(new Binding([form:form]))
			foc = shell.evaluate(classBase)
			formOverrideClasses.put(form, foc)
		}
		
		return foc
	}
	
	static Map buttonOverrideClasses = new HashMap();
	
	/**
	 * TODO: Should be able to cache based on input class
	 *
	 * Uncomment the 'super' methods when the Inconsistent stack trace is sorted out
	 * (Groovy issue)
	 */
	public static Class createButtonOverride(Class button)
	{
		Class boc = buttonOverrideClasses.get(button)
		
		if(boc == null)
		{
			String classBase = """
import wicket.contrib.groovy.builder.*

public class WicketBuilderOnSubmitOverride extends ${button.name} {
	public WicketBuilderOnSubmitOverride(String key){super(key)}

	ScriptWrapper onSubmitWrapper;
	
	public void onSubmit()
	{
		if(onSubmitWrapper != null)
			onSubmitWrapper.run(this);
		//else
		//	super.onSubmit();
	}
}

return WicketBuilderOnSubmitOverride.class
"""
		
			GroovyShell shell = new GroovyShell(new Binding([button:button]))
			boc = shell.evaluate(classBase)
			buttonOverrideClasses.put(button, boc)
		}
		
		return boc
	}
}