package wicket.contrib.examples.groovy.builder;

import wicket.util.tester.WicketTester


class FormsPageTest extends GroovyTestCase {

	WicketTester tester
	
	void setUp()
	{
		tester = new WicketTester();
	}
	 
	void testPageInit() { 
		tester.startPage(FormsPage.class)
		tester.assertRenderedPage(FormsPage.class)
	}
}
