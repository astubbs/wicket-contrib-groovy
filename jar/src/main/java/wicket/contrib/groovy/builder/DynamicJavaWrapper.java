package wicket.contrib.groovy.builder;

import java.util.List;

/**
 * I had some trouble with calling groovy from Java, so I made an interface for this.
 * 
 * NOTE: The trouble was with eclipse and visibility.  Calling groovy from Java would not
 * be a problem with the actual compiled code.
 * 
 * @author Kevin Galligan
 *
 */
public interface DynamicJavaWrapper
{
	public Class wrapClass(Class javaClass, List methods);
	public Class wrapClass(Class javaClass, List methods, String extraCode, String interfaces);
	public void fillMethods(DynamicJavaWrapperScriptable scriptable, List methods, List closures);
}
