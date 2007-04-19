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


import wicket.util.tester.WicketTester

/**
 * This isn't so much of a "test" yet.  Just building tree.  Not testing its accuracy.
 */
class CompClosuresTest extends GroovyTestCase {

	WicketTester tester
	
	void setUp()
	{
		tester = new WicketTester();
	}
	
	void testSomething() { 
		tester.startPage(CompClosuresPage.class)
		tester.assertRenderedPage(CompClosuresPage.class)
	}

}
