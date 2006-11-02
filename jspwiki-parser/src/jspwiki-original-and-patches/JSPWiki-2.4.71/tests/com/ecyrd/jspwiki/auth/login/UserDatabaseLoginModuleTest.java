package com.ecyrd.jspwiki.auth.login;

import java.util.Properties;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import junit.framework.TestCase;

import com.ecyrd.jspwiki.NoRequiredPropertyException;
import com.ecyrd.jspwiki.TestEngine;
import com.ecyrd.jspwiki.WikiEngine;
import com.ecyrd.jspwiki.auth.WikiPrincipal;
import com.ecyrd.jspwiki.auth.authorize.Role;
import com.ecyrd.jspwiki.auth.user.UserDatabase;
import com.ecyrd.jspwiki.auth.user.XMLUserDatabase;

/**
 * @author Andrew R. Jaquith
 * @version $Revision: 1.4 $ $Date: 2006/10/01 16:12:10 $
 */
public class UserDatabaseLoginModuleTest extends TestCase
{
    UserDatabase db;

    Subject      subject;

    public final void testLogin()
    {
        try
        {
            // Log in with a user that isn't in the database
            CallbackHandler handler = new WikiCallbackHandler( db, "user", "password" );
            LoginContext context = new LoginContext( "JSPWiki-custom", subject, handler );
            context.login();
            Set principals = subject.getPrincipals();
            assertEquals( 3, principals.size() );
            assertTrue( principals.contains( new PrincipalWrapper( new WikiPrincipal( "user", WikiPrincipal.LOGIN_NAME ) ) ) );
            assertTrue( principals.contains( Role.AUTHENTICATED ) );
            assertTrue( principals.contains( Role.ALL ) );
            
            // Login with a user that IS in the databasse
            subject = new Subject();
            handler = new WikiCallbackHandler( db, "janne", "myP@5sw0rd" );
            context = new LoginContext( "JSPWiki-custom", subject, handler );
            context.login();
            principals = subject.getPrincipals();
            assertEquals( 3, principals.size() );
            assertTrue( principals.contains( new PrincipalWrapper( new WikiPrincipal( "janne", WikiPrincipal.LOGIN_NAME ) ) ) );
            assertTrue( principals.contains( Role.AUTHENTICATED ) );
            assertTrue( principals.contains( Role.ALL ) );            
        }
        catch( LoginException e )
        {
            System.err.println( e.getMessage() );
            assertTrue( false );
        }
    }

    public final void testLogout()
    {
        try
        {
            CallbackHandler handler = new WikiCallbackHandler( db, "user", "password" );
            LoginContext context = new LoginContext( "JSPWiki-custom", subject, handler );
            context.login();
            Set principals = subject.getPrincipals();
            assertEquals( 3, principals.size() );
            assertTrue( principals.contains( new PrincipalWrapper( new WikiPrincipal( "user",  WikiPrincipal.LOGIN_NAME ) ) ) );
            assertTrue( principals.contains( Role.AUTHENTICATED ) );
            assertTrue( principals.contains( Role.ALL ) );
            context.logout();
            assertEquals( 0, principals.size() );
        }
        catch( LoginException e )
        {
            System.err.println( e.getMessage() );
            assertTrue( false );
        }
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        Properties props = new Properties();
        props.load( TestEngine.findTestProperties() );
        props.put(XMLUserDatabase.PROP_USERDATABASE, "tests/etc/userdatabase.xml");
        WikiEngine m_engine  = new TestEngine(props);
        db = new XMLUserDatabase();
        subject = new Subject();
        try
        {
            db.initialize( m_engine, props );
        }
        catch( NoRequiredPropertyException e )
        {
            System.err.println( e.getMessage() );
            assertTrue( false );
        }
    }

}