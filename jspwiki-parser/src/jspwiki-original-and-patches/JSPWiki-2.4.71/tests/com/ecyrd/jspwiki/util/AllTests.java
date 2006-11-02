
package com.ecyrd.jspwiki.util;

import junit.framework.*;

public class AllTests extends TestCase
{
    public AllTests( String s )
    {
        super( s );
    }

    public static Test suite()
    {
        TestSuite suite = new TestSuite("Utility suite tests");

        suite.addTest( ClassUtilTest.suite() );
        suite.addTest( CommentedPropertiesTest.suite() );
        suite.addTest( PriorityListTest.suite() );
        suite.addTest( TextUtilTest.suite() );
        
        return suite;
    }
}
