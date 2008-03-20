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
package wicket.contrib.groovy.builder.util;

import java.util.ArrayList;
import java.util.List;

/**
 * These must exist somewhere, but I can't find them right now, and they're 
 * trivial to write, so...
 * 
 * @author Kevin Galligan
 *
 */
public class ListConstructors
{
	public static List newList(Object arg0)
	{
		List retList = new ArrayList();
		retList.add(arg0);
		return retList;
	}
	
	public static List newList(Object arg0, Object arg1)
	{
		List retList = new ArrayList();
		retList.add(arg0);
		retList.add(arg1);
		return retList;
	}
	public static List newList(Object arg0, Object arg1, Object arg2)
	{
		List retList = new ArrayList();
		retList.add(arg0);
		retList.add(arg1);
		retList.add(arg2);
		return retList;
	}
	public static List newList(Object arg0, Object arg1, Object arg2, Object arg3)
	{
		List retList = new ArrayList();
		retList.add(arg0);
		retList.add(arg1);
		retList.add(arg2);
		retList.add(arg3);
		return retList;
	}
	
}
