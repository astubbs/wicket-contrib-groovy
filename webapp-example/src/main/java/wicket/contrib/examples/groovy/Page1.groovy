/*
 * $Id: Page1.groovy 7 2005-03-25 13:26:02Z eelco12 $
 * $Revision: 7 $
 * $Date: 2005-03-25 05:26:02 -0800 (Fri, 25 Mar 2005) $
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

import wicket.PageParameters;
import wicket.contrib.examples.WicketExamplePage;

/**
 * Trivial bookmarkable page.
 */
public class Page1 extends WicketExamplePage
{
    /**
     * Constructor
     * @param parameters
     */
    public Page1(PageParameters parameters)
    {
        add(new NavomaticBorder("navomaticBorder"));
    }
}
