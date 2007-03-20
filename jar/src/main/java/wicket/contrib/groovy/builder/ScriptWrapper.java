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
 * Generic script wrapper interface.  Initally there were two script wrapper types: closure and string
 * expression.  The string type is being removed, so this might be pointless soon.  Should probably collapse
 * this so that only a closure type is used directly (rather than an interface).
 * 
 * @author Kevin Galligan
 *
 */
public interface ScriptWrapper
{

	public abstract Object run(Object context, Object[] args);
 
}