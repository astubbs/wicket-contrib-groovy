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

import groovy.lang.Closure;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.commons.collections.MapUtils;

import wicket.Component;
import wicket.ajax.AjaxRequestTarget;
import wicket.ajax.markup.html.form.AjaxSubmitLink;
import wicket.contrib.groovy.builder.BaseComponentBuilder;
import wicket.contrib.groovy.builder.ClosureScriptWrapper;
import wicket.contrib.groovy.builder.ScriptWrapper;
import wicket.contrib.groovy.builder.StringScriptWrapper;
import wicket.contrib.groovy.builder.WicketComponentBuilderException;
import wicket.contrib.groovy.builder.util.ListConstructors;
import wicket.markup.html.form.Form;

public class AjaxSubmitLinkComponentBuilder extends GenericComponentBuilder
{
	public AjaxSubmitLinkComponentBuilder(Class componentClass)
	{
		super(componentClass);
	}

	/**
	 * This is very non-standard.  Skip the auto-building
	 */
	public List getConstructorParameters(String key, Map attributes)
	{
		Object form = attributes.remove("form");
		
		if(form == null)
			throw new WicketComponentBuilderException("AjaxSubmitLink requires 'form'");
		
		return ListConstructors.newList(key, form);
	}
}
