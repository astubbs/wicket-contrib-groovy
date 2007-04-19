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

/**
 * Used by the dynamic component generator.  The new dynamic class will implement this, and the 
 * builder code can cast the object as this type and set the closure for the populateItem method.
 * (This is for Java's sake.  In groovy code, the duck typing would handle this)
 * 
 * @author Kevin Galligan
 *
 */
public interface ListViewPopulateItemClosure
{
	public void setPopulateItemClosure(Closure closure);
}
