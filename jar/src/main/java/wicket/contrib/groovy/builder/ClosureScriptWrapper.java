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
package wicket.contrib.groovy.builder;

import groovy.lang.Closure;
import groovy.lang.Reference;

import java.io.Serializable;
import java.lang.reflect.Constructor;

/**
 * Takes a closure and holds on to the Class that implements the closure.  The original 
 * purpose here was to make something that's Serializable so Wicket could deal with it.  
 * 
 * TODO:  There are some sticky issues where that need to be reviewed.  If somebody is
 * a groovy/closure expert, please take a look.
 * 
 * Also, running this in Jetty, I was getting exceptions that the constructor for the
 * Closure class was not serializable (something like that).  I did not, however, get
 * that problem in tomcat.  this should be examined.
 * 
 * Also, I have a feeling there will be some issues in a clustered environment.  Take 
 * a ride with that sometime.
 * 
 * @author Kevin Galligan
 *
 */
public class ClosureScriptWrapper implements Serializable, ScriptWrapper
{
	static final Class[] CONSTRUCTOR = new Class[]{Object.class, Object.class};
	
	Class closureClass;
	Serializable owner;
	
	//Holding a reference is problematic. TODO: figure out a way to do this
//	transient Closure closure;
	
	public ClosureScriptWrapper(Closure closure)
	{
		this.closureClass = closure.getClass();
		
		//Test for local references (not allowed)
		Constructor[] cons = closureClass.getConstructors();
		for(int i=0; i<cons.length; i++)
		{
			Class[] params = cons[i].getParameterTypes();
			for(int j=0; j<params.length; j++)
			{
				if(params[j].equals(Reference.class))
					throw new WicketComponentBuilderException("Closures cannot have local variable references");
			}
		}
		
		Object tempOwner = closure.getOwner();
		while(tempOwner instanceof Closure && tempOwner != ((Closure)tempOwner).getOwner())
		{
			tempOwner = ((Closure)tempOwner).getOwner();
		}
		
		if(tempOwner instanceof Serializable == false)
			throw new WicketComponentBuilderException("Owning class of closure must be java.io.Serializable");
		
		this.owner = (Serializable) tempOwner;
		//We don't want this to work with the set closure and not one created fromthe class, so don't
		//set here
//		this.closure = closure;
	}
	
	
	/* (non-Javadoc)
	 * @see wicket.contrib.groovy.builder.ScriptWrapper#run(java.lang.Object)
	 */
	public Object run(Object ignore, Object[] args) {
		if(closureClass == null)
			return null;
		
		//TODO: A little ugly.  Clean up.
		Closure closure = getClosure(ignore);
		
//		closure.
		
		return closure.call(args);
	}


	public Closure getClosure(Object newOwner)
	{
		Closure closure;
		try
		{
			Constructor cons = closureClass.getConstructor(CONSTRUCTOR);
			cons.setAccessible(true);
			closure = (Closure) cons.newInstance(new Object[]{newOwner, newOwner});
			closure.setDelegate(owner);
		}
		catch (Exception e)
		{
			throw new WicketComponentBuilderException("Can't call closure", e);
		}
		
		return closure;
	}
	

}
