package wicket.contrib.groovy.builder;

import java.util.List;

/**
 * A data object representing an overridden class.  The idea is that this can be used as a key into a 
 * cache for overridden classes.  So, we only have to generate the class dynamically once.  After that,
 * we can pull it from the cache.
 * 
 * There was some trouble with the cache, so its disabled right now.  Also, this should be checked for 
 * completeness.  If its not right, dire consequences...
 * 
 * @author Kevin Galligan
 *
 */
public class WicketComponentOverrideDescriptor
{
	Class javaClass;
	List methods;
//	List closures;
	String extraCode;
	String interfaces;
	
	List closureClasses;
	
//	protected List getClosureClasses()
//	{
//		if(closureClasses == null)
//		{
//			closureClasses = new ArrayList();
//			for(int i=0; i<closures.size(); i++)
//			{
//				closureClasses.add(
//						((Closure)closures.get(i)).getClass()
//						);
//			}
//		}
//		return closureClasses;
//	}
	
	/*
	 * TODO: Review hashCode and equals to make sure they make sense.
	 *
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		final int PRIME = 31;
		int result = 1;
//		result = PRIME * result + ((closures == null) ? 0 : getClosureClasses().hashCode());
		result = PRIME * result + ((extraCode == null) ? 0 : extraCode.hashCode());
		result = PRIME * result + ((interfaces == null) ? 0 : interfaces.hashCode());
		result = PRIME * result + ((javaClass == null) ? 0 : javaClass.hashCode());
		result = PRIME * result + ((methods == null) ? 0 : methods.hashCode());
		return result;
	}
	
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final WicketComponentOverrideDescriptor other = (WicketComponentOverrideDescriptor) obj;
//		if (closures == null)
//		{
//			if (other.closures != null)
//				return false;
//		}
//		
//		else if (!getClosureClasses().equals(other.getClosureClasses()))
//			return false;
		if (extraCode == null)
		{
			if (other.extraCode != null)
				return false;
		}
		else if (!extraCode.equals(other.extraCode))
			return false;
		if (interfaces == null)
		{
			if (other.interfaces != null)
				return false;
		}
		else if (!interfaces.equals(other.interfaces))
			return false;
		if (javaClass == null)
		{
			if (other.javaClass != null)
				return false;
		}
		else if (!javaClass.equals(other.javaClass))
			return false;
		if (methods == null)
		{
			if (other.methods != null)
				return false;
		}
		else if (!methods.equals(other.methods))
			return false;
		return true;
	}
	
	
	
}
