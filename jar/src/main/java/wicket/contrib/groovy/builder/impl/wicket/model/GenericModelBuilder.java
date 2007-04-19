package wicket.contrib.groovy.builder.impl.wicket.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import wicket.Component;
import wicket.contrib.groovy.builder.BaseModelBuilder;
import wicket.contrib.groovy.builder.util.ListConstructors;
import wicket.model.IModel;

public class GenericModelBuilder extends BaseModelBuilder
{

	public GenericModelBuilder(Class targetClass)
	{
		super(targetClass);
		// TODO Auto-generated constructor stub
	}

	public List getConstructorParameters(Object defaultArg, Map attributes)
	{
		return new ArrayList(0);
	}

	public IModel create(Object data, Map attributes) throws Exception
	{
		IModel model = createModelInstace(data, attributes);
		setOtherProperties(model, attributes);

		return model;
	}

}
