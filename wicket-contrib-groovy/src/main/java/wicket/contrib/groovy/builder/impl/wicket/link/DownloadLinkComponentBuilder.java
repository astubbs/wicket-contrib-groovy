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
package wicket.contrib.groovy.builder.impl.wicket.link;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import wicket.contrib.groovy.builder.WicketComponentBuilderException;

/**
 * TODO: This needs to be improved.  It can handle simple file references, but I'd
 * also like to stream files from the war context (exploded or archived).
 * 
 * @author Kevin Galligan
 *
 */
public class DownloadLinkComponentBuilder extends LinkComponentBuilder
{

	public DownloadLinkComponentBuilder(Class componentClass)
	{
		super(componentClass);
	}

	public List getConstructorParameters(String key, Map attributes)
	{
		List args = new ArrayList();
		
		args.add(key);
		
		Object fileObj = attributes.remove("file");
		if(fileObj == null)
			throw new WicketComponentBuilderException("DownloadLink requires 'file'");
		
		if(fileObj instanceof File)
			args.add(fileObj);
		else if (fileObj instanceof String)
			args.add(new File((String)fileObj));
		else
			throw new WicketComponentBuilderException("'file' must be either java.io.File or java.lang.String");
		
		Object fileName = attributes.remove("fileName");
		
		if(fileName != null)
			args.add(fileName);
		
		return args;
	}

}
