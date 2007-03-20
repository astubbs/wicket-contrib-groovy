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
	void setUp()
	{
		WicketComponentOverrideDescriptor desc = getDescriptor()
		
		Class groovyFormClass = BaseComponentBuilder.getDynamicJavaWrapper().wrapClass(desc.javaClass, desc.methods, desc.closures)
		
		def newForm = groovyFormClass.getConstructor((Class[])[String.class]).newInstance((Object[])["newForm"])
		
		newForm.onSubmit()
	}
	
	WicketComponentOverrideDescriptor getDescriptor()
	{
		Class form = SomeClass.class
		
		def closure = {classLevelTest = "afterOnSubmit"}
		def method = BaseComponentBuilder.matchClosuresToMethods(form, "onSubmit", closure)
		
		WicketComponentOverrideDescriptor descriptor = 
			new WicketComponentOverrideDescriptor(javaClass:SomeClass.class, methods:[BaseComponentBuilder.matchClosuresToMethods(form, "onSubmit", closure)],
					closures:[{classLevelTest = "afterOnSubmit"}], extraCode:null, interfaces:null)
		
		return descriptor
	}
	
	void testDynamicWrapping() {
		
		
		assertEquals classLevelTest, "afterOnSubmit"
	}
	
	//Cache disabled for now.  Problems with old owner references
//	void testDynamicClassCache()
//	{
//		WicketComponentOverrideDescriptor desc1 = getDescriptor()
//		WicketComponentOverrideDescriptor desc2 = getDescriptor()
//		
//		assert desc1.equals(desc2)
//		assertEquals desc1.hashCode(), desc2.hashCode()
//		
//		Map cachedClasses =  BaseComponentBuilder.getDynamicJavaWrapper().cachedClasses
//		Class componentClass = cachedClasses.get(getDescriptor()) 
//		
//		assert componentClass != null
//	}
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