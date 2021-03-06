/*
 * $Id: GroovyClassResolver.java 548 2006-01-19 23:59:30Z joco01 $
 * $Revision: 548 $ $Date: 2006-01-19 15:59:30 -0800 (Thu, 19 Jan 2006) $
 * 
 * ==============================================================================
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
package wicket.contrib.groovy;

import java.io.InputStream;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.groovy.control.CompilationFailedException;

import wicket.Application;
import wicket.WicketRuntimeException;
import wicket.application.DefaultClassResolver;
import wicket.application.IClassResolver;
import wicket.util.concurrent.ConcurrentReaderHashMap;
import wicket.util.listener.IChangeListener;
import wicket.util.resource.IResourceStream;
import wicket.util.watch.ModificationWatcher;

/**
 * Extends the default Page Factory to allow for Groovy based classes.
 * Modifications to groovy files are tracked and files are reloaded if modified.
 * 
 * @author Juergen Donnerstag
 */
public class GroovyClassResolver implements IClassResolver
{
	/** Logging */
	private static final Log log = LogFactory.getLog(GroovyClassResolver.class);

	/**
	 * Caching map of class name to groovy class; not sure if GroovyClassLoader
	 * does it as well
	 */
	private final Map classCache = new ConcurrentReaderHashMap();

	/** Default class resolver */
	private final IClassResolver defaultClassResolver = new DefaultClassResolver();

	/** Application */
	private final Application application;

	/**
	 * Constructor
	 * 
	 * @param application
	 *            Application
	 */
	public GroovyClassResolver(final Application application)
	{
		this.application = application;
	}

	/**
	 * Resolve the class for the given classname. First try standard java
	 * classes, then groovy files. Groovy file name must be
	 * &lt;classname&gt;.groovy.
	 * 
	 * @param classname
	 *            The object's class name
	 * @return The class
	 * @see wicket.application.IClassResolver#resolveClass(String)
	 */
	public Class resolveClass(final String classname)
	{
		try
		{
			return defaultClassResolver.resolveClass(classname);
		}
		catch (WicketRuntimeException ex)
		{
			; // default resolver failed. Try the groovy specific loader next
		}

		// If definition already loaded, ...
		Class groovyPageClass = (Class) classCache.get(classname);
		if (groovyPageClass != null)
		{
			return groovyPageClass;
		}

		// Else, try Groovy.
		final IResourceStream resource = application.getResourceSettings()
				.getResourceStreamLocator().locate(getClass(),
						classname.replace('.', '/'), null, null, ".groovy");

		if (resource != null)
		{
			try
			{
				// Load the groovy file, get the Class and watch for changes
				groovyPageClass = loadGroovyFileAndWatchForChanges(classname, resource);
				if (groovyPageClass != null)
				{
					return groovyPageClass;
				}
			}
			catch (WicketRuntimeException ex)
			{
				throw new WicketRuntimeException("Unable to load class with name: "
						+ classname, ex);
			}
		}
		else
		{
			throw new WicketRuntimeException("File not found: " + resource);
		}

		throw new WicketRuntimeException("Unable to load class with name: " + classname);
	}

	/**
	 * Load a Groovy file, create a Class object and put it into the cache
	 * 
	 * @param classname
	 *            The class name to be created by the Groovy filename
	 * @param resource
	 *            The Groovy resource
	 * @return the Class object created by the groovy resouce
	 */
	private final Class loadGroovyFile(String classname, final IResourceStream resource)
	{
		// Ensure that we use the correct classloader so that we can find
		// classes in an application server.
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		if (cl == null)
		{
			cl = GroovyClassResolver.class.getClassLoader();
		}

		final GroovyWarClassLoader groovyClassLoader = new GroovyWarClassLoader(cl);
		Class clazz = null;

		try
		{
			final InputStream in = resource.getInputStream();
			if (in != null)
			{
				clazz = groovyClassLoader.parseClass(in);
				if (clazz != null)
				{
					// this is necessary because with groovy the filename can be
					// different from the class definition included.
					if (false == classname.equals(clazz.getName()))
					{
						log.warn("Though it is possible, the Groovy file name and "
								+ "the java class name defined in that file SHOULD "
								+ "match and follow the java rules");
					}
					classname = clazz.getName();
				}
			}
			else
			{
				log.warn("Groovy file not found: " + resource);
			}
		}
		catch (CompilationFailedException e)
		{
			throw new WicketRuntimeException("Error parsing groovy file: " + resource, e);
		}
		catch (Throwable e)
		{
			throw new WicketRuntimeException("Error while reading groovy file: "
					+ resource, e);
		}
		finally
		{
			if (clazz == null)
			{
				// Groovy file not found; error while compiling etc..
				// Remove it from cache
				classCache.remove(classname);
			}
			else
			{
				// Put the new class definition into the cache
				classCache.put(classname, clazz);
			}
		}

		return clazz;
	}

	/**
	 * Load the groovy file and watch for changes. If changes to the groovy
	 * happens, than reload the file.
	 * 
	 * @param classname
	 * @param resource
	 * @return Loaded class
	 */
	private Class loadGroovyFileAndWatchForChanges(final String classname,
			final IResourceStream resource)
	{
		// Watch file in the future
		final ModificationWatcher watcher = application.getResourceSettings()
				.getResourceWatcher(true);

		if (watcher != null)
		{
			watcher.add(resource, new IChangeListener()
			{
				public void onChange()
				{
					try
					{
						log.info("Reloading groovy file from " + resource);

						// Reload file and update cache
						final Class clazz = loadGroovyFile(classname, resource);
						log.debug("Groovy file contained definition for class: "
								+ clazz.getName());
					}
					catch (Exception e)
					{
						log.error("Unable to load groovyy file: " + resource, e);
					}
				}
			});
		}

		log.info("Loading groovy file from " + resource);
		return loadGroovyFile(classname, resource);
	}
}