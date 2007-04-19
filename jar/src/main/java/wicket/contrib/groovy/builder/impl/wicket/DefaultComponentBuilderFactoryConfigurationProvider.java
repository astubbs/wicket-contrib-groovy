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
package wicket.contrib.groovy.builder.impl.wicket;

import java.util.Map;

import wicket.Component;
import wicket.ajax.markup.html.form.AjaxSubmitButton;
import wicket.ajax.markup.html.form.AjaxSubmitLink;
import wicket.contrib.groovy.builder.AbstractWicketGroovyConfiguration;
import wicket.contrib.groovy.builder.ClassHierarchyTree;
import wicket.contrib.groovy.builder.UnimplementedComponentBuilder;
import wicket.contrib.groovy.builder.WicketGroovyConfiguration;
import wicket.contrib.groovy.builder.WicketGroovyConfigurationProvider;
import wicket.contrib.groovy.builder.impl.wicket.link.BookmarkablePageLinkComponentBuilder;
import wicket.contrib.groovy.builder.impl.wicket.link.DownloadLinkComponentBuilder;
import wicket.contrib.groovy.builder.impl.wicket.link.LinkComponentBuilder;
import wicket.contrib.groovy.builder.impl.wicket.link.PageLinkComponentBuilder;
import wicket.contrib.groovy.builder.impl.wicket.model.GenericModelBuilder;
import wicket.contrib.groovy.builder.impl.wicket.model.ModelModelBuilder;
import wicket.contrib.groovy.builder.impl.wicket.model.PropertyModelModelBuilder;
import wicket.markup.html.form.CheckGroup;
import wicket.markup.html.form.DropDownChoice;
import wicket.markup.html.form.FormComponent;
import wicket.markup.html.form.FormComponentLabel;
import wicket.markup.html.form.PasswordTextField;
import wicket.markup.html.form.RadioChoice;
import wicket.markup.html.form.SubmitLink;
import wicket.markup.html.form.TextField;
import wicket.markup.html.image.Image;
import wicket.markup.html.include.Include;
import wicket.markup.html.link.BookmarkablePageLink;
import wicket.markup.html.link.DownloadLink;
import wicket.markup.html.link.ExternalLink;
import wicket.markup.html.link.InlineFrame;
import wicket.markup.html.link.InternalFrame;
import wicket.markup.html.link.Link;
import wicket.markup.html.link.PageLink;
import wicket.markup.html.link.ResourceLink;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.ListView;
import wicket.markup.html.list.Loop;
import wicket.markup.html.list.Loop.LoopItem;
import wicket.markup.html.navigation.paging.PagingNavigation;
import wicket.markup.html.navigation.paging.PagingNavigationIncrementLink;
import wicket.markup.html.navigation.paging.PagingNavigationLink;
import wicket.markup.html.panel.Fragment;
import wicket.markup.html.resources.PackagedResourceReference;
import wicket.model.AbstractModel;
import wicket.model.AbstractReadOnlyModel;
import wicket.model.Model;
import wicket.model.PropertyModel;

/**
 * This is the big registry.  In here, the standard builder factories are registered.  
 * This will need a major review.
 * 
 * @author Kevin Galligan
 *
 */
public class DefaultComponentBuilderFactoryConfigurationProvider extends AbstractWicketGroovyConfiguration implements WicketGroovyConfigurationProvider{

	public void augmentHelperClassHierarchy(ClassHierarchyTree tree) {
		
		tree.addSubClass(Component.class, GenericComponentBuilder.class);
		
		tree.addSubClass(wicket.Page.class, UnimplementedComponentBuilder.class);
		
		tree.addSubClass(wicket.markup.transformer.AbstractOutputTransformerContainer.class, UnimplementedComponentBuilder.class);
		
		tree.addSubClass(AjaxSubmitLink.class, AjaxSubmitLinkComponentBuilder.class);
		
		tree.addSubClass(CheckGroup.class, CheckGroupComponentBuilder.class);
		
		tree.addSubClass(ExternalLink.class, ExternalLinkComponentBuilder.class);
		
		tree.addSubClass(FormComponent.class, FormComponentComponentBuilder.class);
		
		tree.addSubClass(SubmitLink.class, SubmitLinkComponentBuilder.class);
		
		tree.addSubClass(TextField.class, TextFieldComponentBuilder.class);
		
		tree.addSubClass(PasswordTextField.class, FormComponentComponentBuilder.class);
		
		tree.addSubClass(FormComponentLabel.class, FormComponentLabelComponentBuilder.class);
		
		tree.addSubClass(Fragment.class, FragmentComponentBuilder.class);
		
		tree.addSubClass(InlineFrame.class, UnimplementedComponentBuilder.class);
		
		tree.addSubClass(InternalFrame.class, UnimplementedComponentBuilder.class);
		
		tree.addSubClass(Link.class, LinkComponentBuilder.class);
		
		tree.addSubClass(BookmarkablePageLink.class, BookmarkablePageLinkComponentBuilder.class);
		
		tree.addSubClass(DownloadLink.class, DownloadLinkComponentBuilder.class);
		
		tree.addSubClass(PageLink.class, PageLinkComponentBuilder.class);
		
		//Not sure what these do.  Get back to them...
		tree.addSubClass(PagingNavigationIncrementLink.class, UnimplementedComponentBuilder.class);
		tree.addSubClass(PagingNavigationLink.class, UnimplementedComponentBuilder.class);
		tree.addSubClass(ResourceLink.class, UnimplementedComponentBuilder.class);
		
		//This should be set from ListView
		tree.addSubClass(ListItem.class, UnimplementedComponentBuilder.class);
		
		tree.addSubClass(ListView.class, ListViewComponentBuilder.class);
		
		tree.addSubClass(Loop.class, LoopComponentBuilder.class);
		
		tree.addSubClass(PagingNavigation.class, UnimplementedComponentBuilder.class);
		
		//I think this is set by the Loop?
		tree.addSubClass(LoopItem.class, UnimplementedComponentBuilder.class);
		
		//No idea what this is
		tree.addSubClass(PackagedResourceReference.class, UnimplementedComponentBuilder.class);
		
		tree.addSubClass(DropDownChoice.class, AbstractSingleSelectChoiceComponentBuilder.class);
		
		tree.addSubClass(RadioChoice.class, AbstractSingleSelectChoiceComponentBuilder.class);
		
		tree.addSubClass(AjaxSubmitButton.class, AjaxSubmitLinkComponentBuilder.class);
		
		tree.addSubClass(Image.class, ImageComponentBuilder.class);
		
		tree.addSubClass(Include.class, IncludeComponentBuilder.class);
	}

	public String[] componentPackageSearchList()
	{
		return new String[]{"wicket.markup","wicket.markup.html","wicket.markup.html.basic","wicket.markup.html.body","wicket.markup.html.border","wicket.markup.html.debug","wicket.markup.html.form","wicket.markup.html.form.persistence","wicket.markup.html.form.upload","wicket.markup.html.form.validation","wicket.markup.html.image","wicket.markup.html.image.resource","wicket.markup.html.include","wicket.markup.html.internal","wicket.markup.html.link","wicket.markup.html.list","wicket.markup.html.navigation.paging","wicket.markup.html.pages","wicket.markup.html.panel","wicket.markup.html.resources","wicket.markup.html.tree","wicket.ajax.markup.html.form"};
	}

	public WicketGroovyConfiguration getConfiguration()
	{
		return this;
	}

	public void addComponentAccentDefinitions(Map componentAccentMap)
	{
		componentAccentMap.put("model", new ModelModelBuilder(Model.class));
		componentAccentMap.put("propertyModel", new PropertyModelModelBuilder(PropertyModel.class));
		componentAccentMap.put("abstractModel", new GenericModelBuilder(AbstractModel.class));
		componentAccentMap.put("abstractReadOnlyModel", new GenericModelBuilder(AbstractReadOnlyModel.class));
		
	}

}
