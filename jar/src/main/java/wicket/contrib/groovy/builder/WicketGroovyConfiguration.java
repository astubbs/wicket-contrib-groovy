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

import java.util.Map;

/**
 * Configuration provided to the factory.
 * 
 * @author Kevin Galligan
 *
 */
public interface WicketGroovyConfiguration
{

	/**
	 * Add more packages to the list for component name matching
	 * 
	 * @return
	 */
	public abstract String[] componentPackageSearchList();

	/**
	 * Add component builder implementations for your custom component classes
	 * 
	 * @param tree
	 */
	public abstract void augmentHelperClassHierarchy(ClassHierarchyTree tree);

	/**
	 * Add model, behavior, and validator producers.
	 * 
	 * @param componentAccessMap
	 */
	public abstract void addComponentAccentDefinitions(Map componentAccentMap);
}