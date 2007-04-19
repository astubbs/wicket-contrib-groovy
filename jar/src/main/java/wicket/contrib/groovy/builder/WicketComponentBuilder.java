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

import java.util.Map;

import wicket.Component;

/**
 * Factory for creating component instances.
 * 
 * @author Kevin Galligan
 *
 */
public interface WicketComponentBuilder
{
	public Component create(String key, Map attributes) throws Exception;
	
	/**
	 * This is for convenience.  Only called in development mode.  It should 
	 * write the code to the console, which can be used for debugging and for
	 * cut/paste of html code.
	 * 
	 * This is the start tag.  If this is not containing other Components, you would 
	 * only need to imeplement one or the other.
	 * 
	 * @param text
	 */
	public void writeViewTagStart(StringBuilder text);
	
	/**
	 * This is for convenience.  Only called in development mode.  It should 
	 * write the code to the console, which can be used for debugging and for
	 * cut/paste of html code.
	 * 
	 * This is the start tag.  If this is not containing other Components, you would 
	 * only need to imeplement one or the other.
	 * 
	 * @param text
	 */
	public void writeViewTagEnd(StringBuilder text);
}
