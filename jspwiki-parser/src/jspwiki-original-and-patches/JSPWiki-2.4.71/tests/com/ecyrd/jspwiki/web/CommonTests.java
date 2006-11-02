package com.ecyrd.jspwiki.web;

import java.io.IOException;

import com.ecyrd.jspwiki.auth.login.CookieAssertionLoginModule;
import com.meterware.httpunit.WebResponse;

import junit.framework.TestCase;
import net.sourceforge.jwebunit.WebTester;

public abstract class CommonTests extends TestCase
{
    protected static final String TEST_PASSWORD = "myP@5sw0rd";
    protected static final String TEST_LOGINNAME = "janne";
    protected static final String TEST_FULLNAME = "Janne Jalkanen";
    protected static final String TEST_WIKINAME = "JanneJalkanen";
    protected WebTester t;
    protected final String m_baseURL;

    public CommonTests( String name, String baseURL )
    {
        super( name );
        m_baseURL = baseURL;
        newSession();
    }

    public void setUp()
    {
        newSession();
    }

    public void testAclJanneEdit()
    {
        // Log in as 'janne' and create page with him as editor
        newSession();
        login( TEST_LOGINNAME, TEST_PASSWORD );
        String page = "AclEditOnly" + System.currentTimeMillis();
        t.beginAt( "/Edit.jsp?page=" + page );
        t.setWorkingForm( "editForm" );
        String text = "[{ALLOW edit janne}]\n"
                    + "This page was created with an ACL by janne";
        t.setFormElement( "_editedtext", text );
        t.submit( "ok" );
        
        // Anonymous viewing should NOT succeed
        newSession();
        t.gotoPage( "/Wiki.jsp?page=" + page );
        t.assertTextPresent( "Please sign in" );
        
        // Anonymous editing should fail
        t.gotoPage( "/Edit.jsp?page=" + page );
        t.assertTextPresent( "Please sign in" );
        
        // Now log in as janne again and view/edit it successfully
        login( TEST_LOGINNAME, TEST_PASSWORD );
        t.gotoPage( "/Wiki.jsp?page=" + page );
        t.assertTextPresent( "This page was created with an ACL by janne" );
        t.gotoPage( "/Edit.jsp?page=" + page );
        t.assertFormPresent( "editForm" );
        t.setWorkingForm( "editForm" );
        t.assertSubmitButtonPresent( "ok" );
        t.assertFormElementPresent("_editedtext" );
    }
    
    public void testAclJanneEditAllView()
    {
        /** testCreatePage does all of the form validation tests */
        // Log in as 'janne' and create page with him as editor
        newSession();
        login( TEST_LOGINNAME, TEST_PASSWORD );
        String page = "AclViewAndEdit" + System.currentTimeMillis();
        t.beginAt( "/Edit.jsp?page=" + page );
        t.setWorkingForm( "editForm" );
        String text = "[{ALLOW edit janne}]\n"
                    + "[{ALLOW view All}]\n"
                    + "This page was created with an ACL by janne";
        t.setFormElement( "_editedtext", text );
        t.submit( "ok" );
        
        // Anonymous viewing should succeed
        newSession();
        t.gotoPage( "/Wiki.jsp?page=" + page );
        t.assertTextPresent( "This page was created with an ACL by janne" );
        
        // Anonymous editing should fail
        t.gotoPage( "/Edit.jsp?page=" + page );
        t.assertTextPresent( "Please sign in" );
        
        // Now log in as janne again and view/edit it successfully
        login( TEST_LOGINNAME, TEST_PASSWORD );
        t.gotoPage( "/Wiki.jsp?page=" + page );
        t.assertTextPresent( "This page was created with an ACL by janne" );
        t.gotoPage( "/Edit.jsp?page=" + page );
        t.assertFormPresent( "editForm" );
        t.setWorkingForm( "editForm" );
        t.assertSubmitButtonPresent( "ok" );
        t.assertFormElementPresent("_editedtext" );
    }
    
    public void testAnonymousCreateGroup()
    {
        // Try to create a group; we should get redirected to login page
        t.gotoPage( "/NewGroup.jsp" );
        t.assertTextPresent( "Please sign in" );
    }
    
    public void testAnonymousView()
    {
        // Start at main, then navigate to About; verify user not logged in
        t.gotoPage( "/Wiki.jsp?page=Main" );
        t.assertTextPresent( "You have successfully installed" );
        t.assertLinkPresentWithText( "About" );
        t.clickLinkWithText( "About" );
        t.assertTextPresent( "This Wiki is done using" );
    }
    
    public void testAnonymousViewImage() throws IOException
    {
        // See if we can view the JSPWiki logo
        t.gotoPage( "/images/jspwiki_logo_s.png" );
        WebResponse r = t.getDialog().getResponse();
        assertEquals( 200, r.getResponseCode() );
        assertFalse( r.getText().length() == 0 );
    }
    
    public void testAssertedName()
    {
        // Navigate to Prefs page; set user cookie
        t.gotoPage( "/Wiki.jsp?page=Main" );
        t.assertTextNotPresent( "G'day" );
        t.clickLinkWithText( "My Prefs" );
        t.assertTextPresent( "Set your user preferences here." );
        t.assertFormPresent( "setCookie" );
        t.setWorkingForm( "setCookie" );
        t.assertFormNotPresent( "clearCookie" );
        t.assertFormElementPresent( "assertedName" );
        t.assertSubmitButtonPresent( "ok" );
        t.setFormElement( "assertedName", "Don Quixote" );
        t.submit( "ok" );
        t.assertTextPresent( "G'day" );
        t.assertTextPresent( "Don Quixote" );
        t.assertTextPresent( "(not logged in)" );
        String cookie = getCookie( CookieAssertionLoginModule.PREFS_COOKIE_NAME );
        assertNotNull( cookie );
        assertEquals( "Don Quixote", cookie );

        // Clear user cookie
        t.clickLinkWithText( "My Prefs" );
        t.assertFormNotPresent( "setCookie" );
        t.setWorkingForm( "clearCookie" );
        t.assertFormPresent( "clearCookie" );
        t.assertSubmitButtonPresent( "ok" );
        t.submit( "ok" );
        t.assertTextNotPresent( "G'day" );
        t.assertTextNotPresent( "Don Quixote" );
        t.assertTextNotPresent( "(not logged in)" );
        cookie = getCookie( CookieAssertionLoginModule.PREFS_COOKIE_NAME );
        assertEquals( "", cookie );
    }
    
    public void testAssertedPermissions( ) 
    {
        // Create new group with 'janne' and 'FredFlintstone' as members
        t.gotoPage( "/Wiki.jsp" );
        login( TEST_LOGINNAME, TEST_PASSWORD );
        String group = "AssertedPermissions" + String.valueOf( System.currentTimeMillis() );
        String members = TEST_LOGINNAME + " \n FredFlintstone";

        // First, create the group
        t.gotoPage( "/NewGroup.jsp" );
        t.assertTextPresent( "Create New Group" );
        t.assertFormPresent( "createGroup" );
        t.setWorkingForm( "createGroup" );
        t.assertFormElementPresent("group" );
        t.assertFormElementPresent("members" );
        t.assertSubmitButtonPresent( "ok" );
        t.setFormElement( "group", group );
        t.setFormElement( "members", members );
        t.submit( "ok" );
        
        // Verify the group was created 
        t.assertTextNotPresent( "Could not create group" );
        t.gotoPage("/Group.jsp?group=" + group );
        t.assertTextPresent( "This is the wiki group called" );
        
        // Verifiy that anonymous users can't view the group
        newSession();
        t.gotoPage("/Group.jsp?group=" + group );
        t.assertTextPresent( "Please sign in" );
        
        // Log in again and verify we can read it
        login( TEST_LOGINNAME, TEST_PASSWORD );
        t.gotoPage("/Group.jsp?group=" + group );
        t.assertTextPresent( "This is the wiki group called" );
        
        // Verify that asserted user 'Fred' can view the group but not edit
        newSession();
        t.gotoPage( "/Wiki.jsp?page=Main" );
        t.clickLinkWithText( "My Prefs" );
        t.setWorkingForm( "setCookie" );
        t.setFormElement( "assertedName", "FredFlintstone" );
        t.submit( "ok" );
        t.assertTextPresent( "G'day" );
        t.assertTextPresent( "FredFlintstone" );
        t.assertTextPresent( "(not logged in)" );
        t.gotoPage("/Group.jsp?group=" + group );
        t.assertTextPresent( "This is the wiki group called" );
        
        // Try to edit -- it should not be allowed
        t.gotoPage("/EditGroup.jsp?group=" + group );
        t.assertTextPresent( "Please sign in" );
    }
    
    public void testCreateGroupFullName()
    {
        createGroup( "Janne Jalkanen" );
    }
    
    public void testCreateGroupLoginName()
    {
        createGroup( TEST_LOGINNAME );
    }

    public void testCreateGroupWikiName()
    {
        createGroup( "JanneJalkanen" );
    }
    
    public void testCreatePage()
    {
        login( TEST_LOGINNAME, TEST_PASSWORD );
        String page = "CreatePage" + System.currentTimeMillis();
        t.gotoPage( "/Edit.jsp?page=" + page );
        t.assertFormPresent( "editForm" );
        t.setWorkingForm( "editForm" );
        t.assertSubmitButtonPresent( "ok" );
        t.assertFormElementPresent("_editedtext" );
        t.setFormElement( "_editedtext", "This page was created by the web unit tests." );
        t.submit( "ok" );
        t.assertTextPresent( "This page was created by the web unit tests." );
    }
    
    public void testLogin()
    {
        // Start at front page; try to log in
        t.gotoPage( "/Wiki.jsp?page=Main" );
        t.assertTextNotPresent( "G'day" );
        t.clickLinkWithText( "Log in" );
        t.assertTextPresent( "Please sign in" );
        t.assertFormPresent( "login" );
        t.setWorkingForm( "login" );
        t.assertFormElementPresent( "j_username" );
        t.assertFormElementPresent( "j_password" );
        t.assertSubmitButtonPresent( "action" );
        t.setFormElement( "j_username", TEST_LOGINNAME );
        t.setFormElement( "j_password", TEST_PASSWORD );
        t.submit( "action" );
        t.assertTextNotPresent( "Please sign in" );
        t.assertTextPresent( "G'day" );
        t.assertTextPresent( "Janne" ); // This is a hack: detecting full
                                            // name isn't working (?)
        t.assertTextPresent( "(authenticated)" );
    }

    public void testLogout()
    {
        // Start at front page; try to log in
        t.gotoPage( "/Login.jsp" );
        t.setWorkingForm( "login" );
        t.setFormElement( "j_username", TEST_LOGINNAME );
        t.setFormElement( "j_password", TEST_PASSWORD );
        t.submit( "action" );
        t.assertTextNotPresent( "Please sign in" );
        t.assertTextPresent( "G'day" );
        t.assertTextPresent( "(authenticated)" );
        String cookie = getCookie( CookieAssertionLoginModule.PREFS_COOKIE_NAME );
        assertNotNull( cookie );
        assertEquals( TEST_WIKINAME, cookie );
        
        // Log out; we should NOT see any asserted identities
        t.gotoPage( "/Logout.jsp" );
        t.assertTextNotPresent( "G'day" );
        cookie = getCookie( CookieAssertionLoginModule.PREFS_COOKIE_NAME );
        assertEquals( "", cookie );
    }
    
    public void testRedirectPageAfterLogin()
    {
        // Create new page 
        login( TEST_LOGINNAME, TEST_PASSWORD );
        String page = "CreatePage" + System.currentTimeMillis();
        t.gotoPage( "/Edit.jsp?page=" + page );
        t.setWorkingForm( "editForm" );
        String redirectText = "We created this page to test redirects.";
        t.setFormElement( "_editedtext", "[{ALLOW view Authenticated}]\n" + redirectText );
        t.submit( "ok" );
        t.assertTextPresent( redirectText );
        
        // Now, from an anonymous session, try to view it, fail, then login
        newSession();
        t.gotoPage( "/Wiki.jsp?page=" + page );
        t.assertTextNotPresent( redirectText );
        t.assertFormPresent( "login" );
        t.assertFormElementPresent( "j_username" );
        t.assertFormElementPresent( "j_password" );
        t.setWorkingForm( "login" );
        t.setFormElement( "j_username", TEST_LOGINNAME );
        t.setFormElement( "j_password", TEST_PASSWORD );
        t.submit( "action" );

        // We should be able to see the page now
        t.assertTextPresent( redirectText );
    }
    
    protected void createGroup( String members ) 
    {
        t.gotoPage( "/Wiki.jsp" );
        login( TEST_LOGINNAME, TEST_PASSWORD );
        String group = "Test" + String.valueOf( System.currentTimeMillis() );
        
        // First, name the group
        t.gotoPage( "/NewGroup.jsp" );
        t.assertTextPresent( "Create New Group" );
        t.assertFormPresent( "createGroup" );
        t.setWorkingForm( "createGroup" );
        t.assertFormElementPresent("group" );
        t.assertFormElementPresent("members" );
        t.assertSubmitButtonPresent( "ok" );
        t.setFormElement( "group", group );
        t.setFormElement( "members", members );
        t.submit( "ok" );
        
        // Verify the group was created 
        t.assertTextNotPresent( "Could not create group" );
        t.gotoPage("/Group.jsp?group=" + group );
        t.assertTextPresent( "This is the wiki group called" );
        
        // Verifiy that anonymous users can't view the group
        newSession();
        t.gotoPage("/Group.jsp?group=" + group );
        t.assertTextPresent( "Please sign in" );
        
        // Log in again and verify we can read it
        login( TEST_LOGINNAME, TEST_PASSWORD );
        t.gotoPage("/Group.jsp?group=" + group );
        t.assertTextPresent( "This is the wiki group called" );
        
        // Try to edit -- it should be allowed
        t.gotoPage("/EditGroup.jsp?group=" + group );
        t.assertTextNotPresent( "Please sign in" );
        t.assertFormPresent( "editGroup" );
    }
    
    protected String createProfile( String loginname, String fullname )
    {
        return createProfile( loginname, fullname, true );
    }
    
    
    protected String createProfile( String loginname, String fullname, boolean withPassword )
    {
        // Navigate to profile tab
        t.gotoPage( "/Wiki.jsp?page=Main" );
        t.assertTextNotPresent( "G'day" );
        t.clickLinkWithText( "My Prefs" );
        t.assertFormPresent( "editProfile" );
        t.setWorkingForm( "editProfile" );
        t.assertFormElementPresent( "loginname" );
        t.assertSubmitButtonPresent( "ok" );

        // Create user profile with generated user name
        String suffix = generateSuffix();
        String wikiname = fullname.replaceAll(" ","");
        t.setFormElement( "loginname", loginname + suffix );
        t.setFormElement( "wikiname", wikiname + suffix );
        t.setFormElement( "fullname", fullname + suffix );
        t.setFormElement( "email", loginname + suffix + "@bandito.org" );
        if ( withPassword )
        {
            t.assertFormElementPresent( "password" );
            t.assertFormElementPresent( "password2" );
            t.setFormElement( "password", TEST_PASSWORD + suffix );
            t.setFormElement( "password2", TEST_PASSWORD + suffix );
        }
        t.submit( "ok" );
        return loginname + suffix;
    }
    
    protected String generateSuffix()
    {
        return String.valueOf( System.currentTimeMillis() );
    }
    
    protected void login( String user, String password ) 
    {
        // Start at front page; try to log in
        t.gotoPage( "/Wiki.jsp?page=Main" );
        t.clickLinkWithText( "Log in" );
        t.setWorkingForm( "login" );
        t.setFormElement( "j_username", user );
        t.setFormElement( "j_password", password );
        t.submit( "action" );
    }
    
    protected String getCookie( String cookie )
    {
        return t.getTestContext().getWebClient().getCookieValue( cookie );
    }
    
    protected void newSession()
    {
        t = new WebTester();
        t.getTestContext().getWebClient().getClientProperties().setAutoRedirect( true );
        t.getTestContext().getWebClient().getClientProperties().setAcceptCookies( true );
        t.getTestContext().setBaseUrl( m_baseURL );
        t.beginAt( "/Wiki.jsp" );
    }
    
}