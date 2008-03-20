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

/**
 * Thrown by UnimplementedComponentBuilder.  Might be a bit too specific.  A
 * WicketComponentBuilderException would probably be just as good.
 * 
 * @author Kevin Galligan
 *
 */
public class UnimplementedComponentBuilderException extends WicketComponentBuilderException
{
	public UnimplementedComponentBuilderException(String component)
	{
		super("Component '"+ component +"' unimplemented.  You can manually add components by calling 'add(new [ComponentClass])' or 'getCurrent()' and get a direct reference to your Component parent class.");
	}
}
