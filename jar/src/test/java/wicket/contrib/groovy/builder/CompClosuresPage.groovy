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

import wicket.markup.html.WebPage
import wicket.markup.html.basic.Label
import wicket.model.*
import java.lang.reflect.Method
import wicket.markup.html.form.Form

class CompClosuresPage extends WebPage {

	SomeData someData = new SomeData(name:"heyo", value:"someval")
	
//	public SomeData getSomeData()
//	{
//		println 'Called getSomeData'
//		return someData
//	}
	
  	public CompClosuresPage()
  	{
  		Closure testClosure =  {
//  				println someData.name
  				println getSomeData().name
  			}
  		
  		WicketBuilder builder = new WicketBuilder(page)
//  		
//		Class newDO = ClosureScriptWrapperJavaBasedDataObject.generateClassInJava(testClosure)
//  		ClosureScriptWrapperJavaBasedDataObject javaDataObj = newDO.getConstructor((Class [])[String.class]).newInstance((Object[])['baseForm'] )
//  		javaDataObj.onSomething()

		Class newFormClass = CompClosuresPageTestForm.generateClassInJava(testClosure)
		Form form = newFormClass.getConstructor((Class [])[String.class]).newInstance((Object[])['baseForm'] )
		
		CompClosuresPageTestForm.fillMethodsOnInstance(testClosure, form)
		
		form.setModel(new CompoundPropertyModel(someData))
  		form.onTestEvent()
  		
  		add(form)
  		
  		println "qwert"
  	}
//  	
//  	private ClosureScriptWrapperJavaBasedDataObject getJavaDataObject(Closure closure)
//  	{
//  		Closure closure =  {
//  				println someData.name
//  			}
//  		Method method = BuilderSupport.matchClosuresToMethods(ClosureScriptWrapperJavaBasedDataObject.class, "onSomething", closure)
//  			
//  			
//  		def newDO = BuilderSupport.getDynamicJavaWrapper().wrapClass(ClosureScriptWrapperJavaBasedDataObject.class, 
//  				[method], 
//  				[closure]) 
//  		
//		Class newDO = ClosureScriptWrapperJavaBasedDataObject.generateClassInJava(closure)
//  		return newDO.newInstance()
//  	}
//  	
}

class SomeData
{
	String name
	String value
}