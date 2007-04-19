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
package wicket.contrib.groovy.builder.impl.databinder;

import org.apache.commons.beanutils.*
import java.lang.reflect.Modifier

class SimpleCrudGenerator {

	Map acceptableTypes = [(Integer.class):Integer.class, (String.class):String.class, (Float.class):Float.class, (Double.class):Double.class, (Boolean.class):Boolean.class,
	                       (int.class):int.class, (float.class):float.class, (boolean.class):boolean.class]
	
	public void buildCrud(Class domainClass, PrintStream outp)
	{
		def properties = PropertyUtils.getPropertyDescriptors(domainClass)
	
		List includedProps = []
		
		for(prop in properties)
		{
			if(Modifier.isPublic(prop.getReadMethod().getModifiers()) && acceptableTypes.get(prop.getPropertyType()) != null)
			{
				includedProps << prop
			}
		}
		
		StringBuffer wicketBuilderTree = new StringBuffer()
		StringBuffer wicketHtmlTree = new StringBuffer()
		
		includedProps.each
		{prop ->
			wicketBuilderTree.append(
"""textField('${prop.name}', type:${prop.propertyType.name}.class)
"""					
			)
			
			wicketHtmlTree.append(
"""${prop.name}: <input type="text" wicket:id="${prop.name}"/>
"""					
			)
		}
		
		outp.println "Builder string {"
		outp.println wicketBuilderTree.toString()
		outp.println "}\n"
		
		outp.println "Html string {"
		outp.println wicketHtmlTree.toString()
		outp.println "}\n"
	}
	
	static void main(args) {
	    SimpleCrudGenerator gen = new SimpleCrudGenerator()
	    gen.buildCrud(SimpleCrudBean.class, System.out)
	}

}

