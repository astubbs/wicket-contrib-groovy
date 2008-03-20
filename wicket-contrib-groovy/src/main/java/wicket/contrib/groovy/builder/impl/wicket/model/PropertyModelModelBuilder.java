package wicket.contrib.groovy.builder.impl.wicket.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import wicket.contrib.groovy.builder.util.AttributeUtils;
import wicket.contrib.groovy.builder.util.ListConstructors;

public class PropertyModelModelBuilder extends GenericModelBuilder
{
	String[] EXPRESSION_NAMES = {"exp", "expression", "path"};
	String[] TYPE_NAME = {"propertyType", "type"};
	
	public PropertyModelModelBuilder(Class targetClass)
	{
		super(targetClass);
	}

	public List getConstructorParameters(Object defaultArg, Map attributes)
	{
		Object expression = AttributeUtils.multiNameRequired(attributes, EXPRESSION_NAMES);
		Object type = AttributeUtils.multiNameOptional(attributes, TYPE_NAME);
		List args = ListConstructors.newList(defaultArg);
		
		args.add(expression);
		
		if(type != null)
			args.add(AttributeUtils.classValueSafe(type));
		
		return args;
	}
}
