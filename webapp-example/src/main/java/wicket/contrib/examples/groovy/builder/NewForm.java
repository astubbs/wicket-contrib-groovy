package wicket.contrib.examples.groovy.builder;

import java.io.IOException;

import wicket.markup.html.form.Form;

/**
 * This exists for no particular reason.  You could just use the regular form object.
 * @author Kevin
 *
 */
public class NewForm extends Form
{ 
	public NewForm(String key)
	{
		super(key);
		System.out.println("in new form");
		
	}
	
	public NewForm(String key, wicket.model.CompoundPropertyModel model)
	{
		super(key, model);
		System.out.println("in new form");
		
	}
	
	@Override
	protected void onError()
	{
		System.out.println("onError called");
	}


	@Override
	protected void onSubmit()
	{
		System.out.println("onSubmit called");
	}
	
	public void doSomething() throws IOException
	{
		throw new IOException("Just checking");
	}
}
