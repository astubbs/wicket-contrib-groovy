package wicket.contrib.groovy.builder;

import java.util.Map;

import wicket.model.IModel;

public interface WicketModelBuilder
{
	public IModel create(Object data, Map attributes) throws Exception;
}
