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

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


import wicket.Component;
import wicket.contrib.groovy.builder.util.AttributeUtils;
import wicket.markup.html.form.FormComponent;

public class FormComponentComponentBuilder extends GenericComponentBuilder
{

	public FormComponentComponentBuilder(Class componentClass)
	{
		super(componentClass);
	}

	public Component create(String key, Map attributes) throws Exception
	{
		FormComponent formComponent = (FormComponent) createComponentInstace(key, attributes);
		setFormComponentStandards(formComponent, attributes);
		
		return formComponent;
	}

	protected void setFormComponentStandards(FormComponent formComponent, Map attributes)
	{
		setModel(formComponent, attributes);
		
		if(attributes == null)
			attributes = Collections.EMPTY_MAP;
		
		Object label = attributes.remove("label");
//		Object persistent = attributes.remove("persistent");
//		Object required = attributes.remove("required");
//		
		if(label != null)
			formComponent.setLabel(AttributeUtils.modelValue((Serializable) label));
		
//		if(persistent != null)
//			formComponent.setPersistent(AttributeUtils.booleanValue(persistent));
//		
//		if(required != null)
//			formComponent.setRequired(AttributeUtils.booleanValue(required));
//		
		setOtherProperties(formComponent, attributes);
	}

	
}
