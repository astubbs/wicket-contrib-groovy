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


import java.lang.reflect.Method
import wicket.markup.html.form.Form

class ClosureScriptWrapperTest extends GroovyTestCase implements Serializable {

	def classLevelTest = "beforeOnSubmit"

	DataObject dataObject = new DataObject(name:"some name", someValue:1234) 
	
	public DataObject getDataObject()
	{
		println "In getDataObject"
		return dataObject
	}
	
	void testProperThisReference()
	{
		Method method = org.apache.commons.beanutils.MethodUtils.getAccessibleMethod(NodeTree.class, "getValue", new Class[0])
		Method method2 = org.apache.commons.beanutils.MethodUtils.getAccessibleMethod(NodeTree.class, "getSubValue", new Class[0])
		Closure closure = { return {
			this.getTestValue()
			super_getSubValue()
			getTestValue()
			doSomethingElse() 
			println dataObject.name
		}}.call()
		
		Closure closure2 = { assert false }
		
		
		def newTop = BuilderSupport.getDynamicJavaWrapper().wrapClass(NodeTree.class, 
				[method, method2], 
				[closure, closure2]) 
		
		newTop.newInstance().getValue()
	}
	
	void testGroovyLocalSourceObject()
	{
		Closure closure =  {
			println dataObject.name
		}
		Method method = BuilderSupport.matchClosuresToMethods(ClosureScriptWrapperGroovyLocalBasedDataObject.class, "onSomething", closure)
		
		
		def newForm = BuilderSupport.getDynamicJavaWrapper().wrapClass(ClosureScriptWrapperGroovyLocalBasedDataObject.class, 
				[method], 
				[closure]) 
		
		newForm.newInstance().onSomething()
	}
	
	void testGroovySourceObject()
	{	
		Closure closure =  {
			println dataObject.name
		}
		Method method = BuilderSupport.matchClosuresToMethods(ClosureScriptWrapperGroovyBasedDataObject.class, "onSomething", closure)
		
		
		def newForm = BuilderSupport.getDynamicJavaWrapper().wrapClass(ClosureScriptWrapperGroovyBasedDataObject.class, 
				[method], 
				[closure]) 
		
		newForm.newInstance().onSomething()
	}
	
//	void testJavaSourceObject()
//	{		
//		Closure closure =  {
//			println dataObject.name
//		}
//		Class newDO = ClosureScriptWrapperJavaBasedDataObject.generateClassInJava(closure)
//  		ClosureScriptWrapperJavaBasedDataObject javaDataObj = newDO.getConstructor((Class [])[String.class]).newInstance((Object[])['baseForm'] )
//  		javaDataObj.onSomething()
//	}
//	
//	void testFormObject()
//	{
//		Method method = org.apache.commons.beanutils.MethodUtils.getAccessibleMethod(Form.class, "onSubmit", new Class[0])
//		Closure closure =  {
//			println dataObject.name
//		}
//		
//		def newForm = BuilderSupport.getDynamicJavaWrapper().wrapClass(Form.class, 
//				[method], 
//				[closure]) 
//		
//		newForm.getConstructor((Class[])[String.class]).newInstance((Object [])["asdf"]).onSubmit()
//	}
	
	void getValue()
	{
		assert false
	}
	
	 
	void getTestValue()
	{
		assert false;
	}
	
	void doSomethingElse()
	{
		assert true
	}
}

class NodeTree
{
	NodeTree parent
	
	void getValue()
	{
		
	}
	 
	void getTestValue()
	{
		assert true;
	}
	
	void getSubValue()
	{
		println "In getSubValue"
		assert true;
	}
}

class DataObject
{
	String name
	int someValue
}

class ClosureScriptWrapperGroovyLocalBasedDataObject {

	String someVal = "asdf";

	public String getSomeVal()
	{
		return someVal;
	}

	public void setSomeVal(String someVal)
	{
		this.someVal = someVal;
	}
	
	protected void onSomething()
	{
		System.out.println("in onSomething");
	}
}