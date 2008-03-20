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

import java.io.FilterInputStream;
import java.io.InputStream;
import java.io.LineNumberInputStream;
import java.io.PushbackInputStream;
import java.util.Date;

import wicket.contrib.groovy.builder.ClassHierarchyTree;

import junit.framework.TestCase;

public class ClassHierarchyTreeTest extends TestCase {
	public void testBasicTree()
	{
		ClassHierarchyTree tree = new ClassHierarchyTree();
		
		tree.addSubClass(InputStream.class, "InputStream.class");
		
		assertEquals(InputStream.class, getChildTree(tree, 0).currentClass);
		
		tree.addSubClass(Date.class, "Date.class");
		
		assertEquals(Date.class, getChildTree(tree, 1).currentClass);
		
		tree.addSubClass(PushbackInputStream.class, "PushbackInputStream.class");
		
		assertEquals(PushbackInputStream.class, getSecondOrder(tree, 0, 0).currentClass);
		
		tree.addSubClass(FilterInputStream.class, "FilterInputStream.class");
		
		assertEquals(FilterInputStream.class, getSecondOrder(tree, 0, 0).currentClass);
		
		assertEquals(PushbackInputStream.class, getThirdOrder(tree, 0, 0, 0).currentClass);
		
		tree.addSubClass(java.sql.Date.class, "java.sql.Date.class");
		
		assertEquals(java.sql.Date.class, getSecondOrder(tree, 1, 0).currentClass);
		
		assertEquals("FilterInputStream.class", tree.getBest(FilterInputStream.class));
		assertEquals("FilterInputStream.class", tree.getBest(LineNumberInputStream.class));
		
		assertEquals("Date.class", tree.getBest(Date.class));
		assertEquals("java.sql.Date.class", tree.getBest(java.sql.Date.class));
		
	}
	
	private ClassHierarchyTree getChildTree(ClassHierarchyTree tree, int index)
	{
		return (ClassHierarchyTree) tree.subClasses.get(index);
	}
	private ClassHierarchyTree getSecondOrder(ClassHierarchyTree tree, int index0, int index1)
	{
		return getChildTree(getChildTree(tree, index0), index1);
	}
	
	private ClassHierarchyTree getThirdOrder(ClassHierarchyTree tree, int index0, int index1, int index2)
	{
		return getChildTree(getSecondOrder(tree, index0, index1), index2);
	}
}
