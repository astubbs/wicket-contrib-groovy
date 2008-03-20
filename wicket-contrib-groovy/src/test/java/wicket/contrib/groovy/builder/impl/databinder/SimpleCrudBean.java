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
package wicket.contrib.groovy.builder.impl.databinder;

public class SimpleCrudBean
{
	String prop1;
	String prop2;
	Integer propInteger;
	Float propFloat;
	int propSimpleInt;
	float propSimpleFloat;
	
	public String getProp1()
	{
		return prop1;
	}
	public void setProp1(String prop1)
	{
		this.prop1 = prop1;
	}
	public String getProp2()
	{
		return prop2;
	}
	public void setProp2(String prop2)
	{
		this.prop2 = prop2;
	}
	public Float getPropFloat()
	{
		return propFloat;
	}
	public void setPropFloat(Float propFloat)
	{
		this.propFloat = propFloat;
	}
	public Integer getPropInteger()
	{
		return propInteger;
	}
	public void setPropInteger(Integer propInteger)
	{
		this.propInteger = propInteger;
	}
	public float getPropSimpleFloat()
	{
		return propSimpleFloat;
	}
	public void setPropSimpleFloat(float propSimpleFloat)
	{
		this.propSimpleFloat = propSimpleFloat;
	}
	public int getPropSimpleInt()
	{
		return propSimpleInt;
	}
	public void setPropSimpleInt(int propSimpleInt)
	{
		this.propSimpleInt = propSimpleInt;
	}
	
	
}
