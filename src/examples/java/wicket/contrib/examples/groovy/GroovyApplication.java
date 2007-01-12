/*
 * $Id: GroovyApplication.java 524 2006-01-08 09:30:40Z jdonnerstag $
 * $Revision: 524 $
 * $Date: 2006-01-08 01:30:40 -0800 (Sun, 08 Jan 2006) $
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wicket.contrib.examples.groovy;

import wicket.application.IClassResolver;
import wicket.contrib.groovy.GroovyClassResolver;
import wicket.protocol.http.WebApplication;

/**
 * WicketServlet class for hello world example.
 * @author Jonathan Locke
 */
public class GroovyApplication extends WebApplication
{
    /**
     * Constructor.
     */
    public GroovyApplication()
    {
        final IClassResolver resolver = new GroovyClassResolver(this);
    	getApplicationSettings().setClassResolver(resolver);
    }

    /**
     * @return Class
     */
    public Class getHomePage()
    {
    	return getApplicationSettings().getClassResolver().resolveClass(
    			"wicket.contrib.examples.groovy.Page1");
    }
}
