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

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * I'm quite sure this exists somewhere, but I want it done relatively quick and dirty.
 * It works like a tree map.  Classes are the key for the map, but if a particular class 
 * isn't in the tree, it'll find the next highest in terms of parent class.
 * 
 * @author Kevin Galligan
 *
 */
public class ClassHierarchyTree {
	Class currentClass;
	Object data;
	
	
	
	List subClasses = new LinkedList();
	
	
	
	public List getSubClasses()
	{
		return subClasses;
	}

	/**
	 * Start root
	 *
	 */
	public ClassHierarchyTree()
	{
		currentClass = Object.class;
	}
	
	private ClassHierarchyTree(Class classArg, Object data)
	{
		currentClass = classArg;
		this.data = data;
	}
	
	public void addSubClass(Class subClass, Object data)
	{
		
		for (ListIterator subClassIter = subClasses.listIterator(); subClassIter.hasNext();) {
			ClassHierarchyTree subHierarchyTree = (ClassHierarchyTree) subClassIter.next();
			
			if(subHierarchyTree.matches(subClass))
			{
				if(subClass.equals(subHierarchyTree.getCurrentClass()))
					return;
				
				subHierarchyTree.addSubClass(subClass, data);
			}
			
			if(subClass.isAssignableFrom(subHierarchyTree.getCurrentClass()))
			{
				ClassHierarchyTree tree = new ClassHierarchyTree(subClass, data);
				
				subClassIter.set(tree);
				tree.subClasses.add(subHierarchyTree);
				
				return;
			}
		}
		
		subClasses.add(new ClassHierarchyTree(subClass, data));
	}
	
	public Object getBest(Class arg)
	{
//		for (ListIterator subClassIter = subClasses.listIterator(); subClassIter.hasNext();) {
//			ClassHierarchyTree subHierarchyTree = (ClassHierarchyTree) subClassIter.next();
//			
//			if(subHierarchyTree.matches(arg))
//			{
//				Object returnData = subHierarchyTree.getBest(arg);
//				
//				if(returnData == null)
//					return subHierarchyTree.data;
//				else
//					return returnData;
//			}
//		}
//		
//		return null;
		
		ClassHierarchyTree tree = getBestTree(arg);
		if(tree != null)
			return tree.data;
		
		return null;
	}
	
	public ClassHierarchyTree getBestTree(Class arg)
	{
		for (ListIterator subClassIter = subClasses.listIterator(); subClassIter.hasNext();) {
			ClassHierarchyTree subHierarchyTree = (ClassHierarchyTree) subClassIter.next();
			
			if(subHierarchyTree.matches(arg))
			{
				ClassHierarchyTree returnData = subHierarchyTree.getBestTree(arg);
				
				if(returnData == null)
					return subHierarchyTree;
				else
					return returnData;
			}
		}
		
		return null;
	}
	
	public boolean matches(Class testClass)
	{
		return currentClass.isAssignableFrom(testClass);
	}

	public Class getCurrentClass() {
		return currentClass;
	}

	protected void setCurrentClass(Class currentClass) {
		this.currentClass = currentClass;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
}
