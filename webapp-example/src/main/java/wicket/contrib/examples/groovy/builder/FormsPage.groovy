package wicket.contrib.examples.groovy.builder;


import wicket.markup.html.WebPage
import wicket.contrib.groovy.builder.WicketBuilder
import wicket.markup.html.basic.Label
import wicket.model.Model
import wicket.model.CompoundPropertyModel
import wicket.markup.html.form.TextField

class FormsPage extends WebPage {

	Person dataPerson = new Person()
	 
	public FormsPage()
	{
		def wicketBuilder = new WicketBuilder(this)
		
		def form = wicketBuilder.newForm('mainForm', 
				model:new CompoundPropertyModel(dataPerson),
				page:this,
				onSubmit:{
			
			getDataPerson().getMetaPropertyValues().each({
				if(it.type.primitive || it.type == String.class)
					println("${it.name}: ${it.value}")
			})
			getDataPerson().address.getMetaPropertyValues().each({
				if(it.type.primitive || it.type == String.class)
					println("${it.name}: ${it.value}")
			})
			
//			doSomething()
		})
		{
			textField('firstName')
			textField('lastName') 
			textField('age', type:Integer)
			textField('salary', type:Float)
			
			textField('address.address1')
			textField('address.address2')
			textField('address.city')
			textField('address.state')
			textField('address.zip')
			
			listView("favorites", 
					populateItem:
					{
						label("name", model:it.modelObject.name)
						label("url", model:it.modelObject.url)
					})
		}
		
	}
 
}

class Person implements Serializable
{
	String firstName
	String lastName
	int age
	float salary
	Address address = new Address()
	List favorites = [
	                  new Favorite(name:"Wicket", url:"http://wicket.sourceforge.net/"),
	                  new Favorite(name:"Groovy", url:"http://groovy.codehaus.org/"),
	                  new Favorite(name:"Wicket Stuff", url:"http://wicketstuff.org/")
	                  ]
}

class Address implements Serializable
{
	String address1
	String address2
	String city
	String state
	String zip
}

class Favorite implements Serializable
{
	String name
	String url
}