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

	void testDynamicWrapping() {
		Class form = SomeClass.class
		
		def closure = {classLevelTest = "afterOnSubmit"}
		def method = BaseComponentBuilder.matchClosuresToMethods(form, "onSubmit", closure)
		                
		Class groovyFormClass = DynamicJavaWrapperUtil.wrapClass(form, [method], [closure] )
		
		def newForm = groovyFormClass.getConstructor((Class[])[String.class]).newInstance((Object[])["newForm"])
		
		newForm.onSubmit()
		
		assertEquals classLevelTest, "afterOnSubmit"
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