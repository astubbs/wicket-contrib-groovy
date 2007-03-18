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

class ClosureScriptWrapperTest extends GroovyTestCase implements Serializable {

	def classLevelTest = "beforeOnSubmit"

	void testProperThisReference()
	{
		Method method = org.apache.commons.beanutils.MethodUtils.getAccessibleMethod(NodeTree.class, "getValue", new Class[0])
		Method method2 = org.apache.commons.beanutils.MethodUtils.getAccessibleMethod(NodeTree.class, "getSubValue", new Class[0])
		Closure closure = {
			this.getTestValue()
			super_getSubValue()
			getTestValue()
			doSomethingElse()
		}
		
		Closure closure2 = { assert false }
		
		
		def newTop = DynamicJavaWrapperUtil.wrapClass(NodeTree.class, 
				[method, method2], 
				[closure, closure2]) 
		
		newTop.newInstance().getValue()
	}
	
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
