package wicket.contrib.groovy.builder;

import java.util.List;
import java.util.Map;

import wicket.model.IModel;

public abstract class BaseModelBuilder extends BuilderSupport implements WicketModelBuilder
{

	public BaseModelBuilder(Class targetClass)
	{
		super(targetClass);
	}

	/**
	 * Generate a list of constructor arguments.  This should allow for obscure situations
	 * easily with sub-classed component builders.
	 * 
	 * @return
	 */
	public abstract List getConstructorParameters(Object defaultArg, Map attributes);
	
	protected IModel createModelInstace(Object defaultArg, Map attributes)
	{
		try
		{ 
			List constructorParameters = getConstructorParameters(defaultArg, attributes);
			
			Object generated = generateInstance(attributes, constructorParameters);
			
			return (IModel) generated;
		}
		catch (Exception e)
		{
			throw new WicketComponentBuilderException("Can't create model instance", e);
		}
	}
}
