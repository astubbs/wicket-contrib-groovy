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

import groovy.lang.IntRange;
import groovy.lang.Range;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import wicket.Component;
import wicket.contrib.groovy.builder.WicketComponentBuilderException;
import wicket.contrib.groovy.builder.util.AttributeUtils;
import wicket.markup.html.form.FormComponent;
import wicket.markup.html.form.validation.IValidator;
import wicket.markup.html.form.validation.NumberValidator;
import wicket.markup.html.form.validation.StringValidator;

/**
 * 
 * @author Kevin Galligan
 * 
 */
public class FormComponentComponentBuilder extends GenericComponentBuilder
{
	static final String[] PARAMS_NAMES = new String[] { "valid", "validator", "validators", "validatorList" };

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

		if (attributes == null)
			attributes = Collections.EMPTY_MAP;

		Object label = attributes.remove("label");

		if (label != null)
			formComponent.setLabel(AttributeUtils.modelValue((Serializable) label));

		//***************** VALIDATORS ***************** 
		Object validatorObject = AttributeUtils.multiName(attributes, PARAMS_NAMES);

		if (validatorObject != null)
		{
			if (validatorObject instanceof List)
			{
				List validators = (List) validatorObject;
				for (int i = 0; i < validators.size(); i++)
				{
					formComponent.add((IValidator) validators.get(i));
				}
			}
			else if (validatorObject instanceof IValidator)
			{
				formComponent.add((IValidator) validatorObject);
			}
			else
			{
				throw new WicketComponentBuilderException("validator type not recognized");
			}
		}
		
		Number min = (Number) attributes.remove("min");
		if(min != null)
			formComponent.add(NumberValidator.minimum(min.longValue()));
		
		Number max = (Number) attributes.remove("max");
		if(max != null)
			formComponent.add(NumberValidator.maximum(max.longValue()));
		
		IntRange range = AttributeUtils.intRangeValue(attributes.remove("range"));
		if(range != null)
			formComponent.add(NumberValidator.range(range.getFromInt(), range.getToInt()));
		
		Number minLength = (Number) attributes.remove("minLength");
		if(minLength != null)
			formComponent.add(StringValidator.minimumLength(minLength.intValue()));
		
		Number maxLength = (Number) attributes.remove("maxLength");
		if(maxLength != null)
			formComponent.add(StringValidator.maximumLength(maxLength.intValue()));
		
		setOtherProperties(formComponent, attributes);
	}

}
