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
class DynamicJavaWrapperTest extends GroovyTestCase implements Serializable {

	def classLevelTest = "beforeOnSubmit"

	private WicketComponentOverrideDescriptor generateDescriptor(closure)
	{
		Class form = SomeClass.class
		
		def method = BuilderSupport.matchClosuresToMethods(form, "onSubmit", closure)
		
		WicketComponentOverrideDescriptor descriptor = 
			new WicketComponentOverrideDescriptor(javaClass:SomeClass.class, methods:[method], extraCode:null, interfaces:null)
		
		return descriptor
	}
	
	void testDynamicWrapping() {
		Closure closure = {classLevelTest = "afterOnSubmit"}
		
		WicketComponentOverrideDescriptor desc = generateDescriptor(closure)
		
		Class groovyFormClass = BuilderSupport.getDynamicJavaWrapper().wrapClass(desc.javaClass, desc.methods)
		
		def newForm = groovyFormClass.getConstructor((Class[])[String.class]).newInstance((Object[])["newForm"])
		
		BuilderSupport.getDynamicJavaWrapper().fillMethods(newForm, desc.methods, [closure])
		
		newForm.onSubmit()
		
		assertEquals classLevelTest, "afterOnSubmit"
	}
	
	void testCacheDifference()
	{
		Closure closure = {classLevelTest = "afterSecond"}
		
		WicketComponentOverrideDescriptor desc = generateDescriptor(closure)
			
		Class groovyFormClass = BuilderSupport.getDynamicJavaWrapper().wrapClass(desc.javaClass, desc.methods)
		
		def newForm = groovyFormClass.getConstructor((Class[])[String.class]).newInstance((Object[])["newForm"])
		
		BuilderSupport.getDynamicJavaWrapper().fillMethods(newForm, desc.methods, [closure])
		
		newForm.onSubmit()
	
		assertEquals classLevelTest, "afterSecond"
	}
	
	//Cache disabled for now.  Problems with old owner references
	void testDynamicClassCache()
	{
		WicketComponentOverrideDescriptor desc1 = generateDescriptor({classLevelTest = "step1"})
		WicketComponentOverrideDescriptor desc2 = generateDescriptor({classLevelTest = "step2"})
		
		assert desc1.equals(desc2)
		assertEquals desc1.hashCode(), desc2.hashCode()
		
		Map cachedClasses =  BuilderSupport.getDynamicJavaWrapper().cachedClasses
		Class componentClass = cachedClasses.get(generateDescriptor({classLevelTest = "somethingElse"})) 
		
		assert componentClass != null
	}
}

class SomeClass
{
	public SomeClass(String key)
	{
		
	}
	public void onSubmit()
	{
		
	}
}