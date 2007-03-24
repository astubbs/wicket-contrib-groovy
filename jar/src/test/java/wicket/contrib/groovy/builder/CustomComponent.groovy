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


import wicket.markup.html.link.Link
import wicket.Component

public class CustomComponent extends Link  {
	Date dummy
	
	public CustomComponent(String key, Date dummy)
	{
		super(key)
		this.dummy = dummy
	}
	
	public void onClick()
	{
		println "Custom link component: $dummy"
	}
	
	public static WicketComponentBuilder localBuilder()
	{
		return new CustomComponentBuilder()
	}
}

public class CustomComponentBuilder implements WicketComponentBuilder
{
	public Component create(String key, Map attributes) throws Exception
	{
		return new CustomComponent(key, new Date());
	}
	
	public void writeViewTagEnd(StringBuilder text)
	{
		
	}

	public void writeViewTagStart(StringBuilder text)
	{
		
	}
}