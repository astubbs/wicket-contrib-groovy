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
package wicket.contrib.groovy.builder.impl.databinder;

import java.util.Map;

import wicket.contrib.groovy.builder.AbstractWicketGroovyConfiguration;
import wicket.contrib.groovy.builder.ClassHierarchyTree;
import net.databinder.components.DataForm;
import net.databinder.components.DataPanel;
import net.databinder.components.StyleLink;

/**
 * 
 * @author Kevin Galligan
 *
 */
public class DatabinderWicketGroovyConfiguration extends AbstractWicketGroovyConfiguration
{
	public String[] componentPackageSearchList()
	{
		return new String[]{"net.databinder.components","net.databinder.auth.components"};
	}

	public void augmentHelperClassHierarchy(ClassHierarchyTree tree)
	{
		tree.addSubClass(StyleLink.class, StyleLinkComponentBuilder.class);
		tree.addSubClass(DataForm.class, DataFormComponentBuilder.class);
		tree.addSubClass(DataPanel.class, DataPanelComponentBuilder.class);
	}

	public void addComponentAccentDefinitions(Map componentAccessMap)
	{
		// TODO Auto-generated method stub
		
	}
}
