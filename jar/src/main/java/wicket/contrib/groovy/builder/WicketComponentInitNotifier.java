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
 * If you want your custom component to be nofied when all properties have been set, 
 * impelement this interface.  What's the point?  Well, if you want all properties set before 
 * initialization code is called, you would normally put them all in the constructor.  However,
 * if in the constructor, you need a custom builder implementation.  To make life easier, you could 
 * have a standard constructor and just implement this.
 * 
 *  public class Example extends Component implements WicketComponentInitNotifier
 *  {
 *  	//this is "standard" for Wicket and the WicketBuilder
 *  	public Example(String key)
 *  	{
 *  		super(key);
 *  	}
 *  
 *  	public void init()
 *  	{
 *  		//Other stuff
 *  	}
 *  }
 *  
 * @author Kevin Galligan
 *
 */
public interface WicketComponentInitNotifier
{
	public void init();
}
