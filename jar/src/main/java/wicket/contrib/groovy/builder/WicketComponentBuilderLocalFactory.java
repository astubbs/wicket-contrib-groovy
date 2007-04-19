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
 * This is an odd construct.  You can't have a static method as an interface method, 
 * but you also can't have a factory method that requires the object to exist, if its
 * to create itself.  This interface is just a marker.  Make sure the class has a static
 * 'Component create(String key, Map attributes)' method.
 * 
 * This is a weird construct.  I'm reviewing.  In my experience, if it feels weird early on, it won't be 
 * there long.  Something, however, will take its place.  Custom components should have the option of
 * generating their own instances.
 * 
 * @author Kevin Galligan
 *
 */
public interface WicketComponentBuilderLocalFactory
{
//	public static Component create(String key, Map attributes)throws Exception
}
