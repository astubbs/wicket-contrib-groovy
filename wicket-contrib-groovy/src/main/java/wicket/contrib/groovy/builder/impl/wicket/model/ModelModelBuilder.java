package wicket.contrib.groovy.builder.impl.wicket.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import wicket.contrib.groovy.builder.util.ListConstructors;

public class ModelModelBuilder extends GenericModelBuilder
{

	public ModelModelBuilder(Class targetClass)
	{
		super(targetClass);
	}

	public List getConstructorParameters(Object defaultArg, Map attributes)
	{
		if(defaultArg != null)
			return ListConstructors.newList(defaultArg);
		else
			return new ArrayList(0);
	}

}
