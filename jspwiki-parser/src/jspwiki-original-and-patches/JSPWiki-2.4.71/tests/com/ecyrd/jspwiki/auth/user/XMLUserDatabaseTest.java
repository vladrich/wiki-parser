package com.ecyrd.jspwiki.auth.user;
import java.security.Principal;
import java.util.Properties;

import org.apache.commons.lang.ArrayUtils;

import junit.framework.TestCase;

import com.ecyrd.jspwiki.TestEngine;
import com.ecyrd.jspwiki.WikiEngine;
import com.ecyrd.jspwiki.auth.NoSuchPrincipalException;
import com.ecyrd.jspwiki.auth.Users;
import com.ecyrd.jspwiki.auth.WikiPrincipal;
import com.ecyrd.jspwiki.auth.WikiSecurityException;



/**
 * @author Andrew Jaquith
 */
public class XMLUserDatabaseTest extends TestCase {

  private XMLUserDatabase db;
  
  /**
   * @see junit.framework.TestCase#setUp()
   */
  protected void setUp() throws Exception {
    super.setUp();
    Properties props = new Properties();
    props.load( TestEngine.findTestProperties() );
    props.put(XMLUserDatabase.PROP_USERDATABASE, "tests/etc/userdatabase.xml");
    WikiEngine m_engine  = new TestEngine(props);
    db = new XMLUserDatabase();
    db.initialize(m_engine, props);
  }
  
  public void testDeleteByLoginName() throws WikiSecurityException
  {
      // First, count the number of users in the db now.
      int oldUserCount = db.getWikiNames().length;
      
      // Create a new user with random name
      String loginName = "TestUser" + String.valueOf( System.currentTimeMillis() );
      UserProfile profile = new DefaultUserProfile();
      profile.setEmail("testuser@testville.com");
      profile.setLoginName( loginName );
      profile.setWikiName( "WikiName"+loginName );
      profile.setFullname( "FullName"+loginName );
      profile.setPassword("password");
      db.save(profile);
      db.commit();
      
      // Make sure the profile saved successfully
      profile = db.findByLoginName( loginName );
      assertEquals( loginName, profile.getLoginName() );
      assertEquals( oldUserCount+1, db.getWikiNames().length );

      // Now delete the profile; should be back to old count
      db.deleteByLoginName( loginName );
      db.commit();
      assertEquals( oldUserCount, db.getWikiNames().length );
  }
  
  public void testFindByEmail() {
    try {
        UserProfile profile = db.findByEmail("janne@ecyrd.com");
        assertEquals("janne",           profile.getLoginName());
        assertEquals("Janne Jalkanen",  profile.getFullname());
        assertEquals("JanneJalkanen",   profile.getWikiName());
        assertEquals("{SHA}457b08e825da547c3b77fbc1ff906a1d00a7daee", profile.getPassword());
        assertEquals("janne@ecyrd.com", profile.getEmail());
    }
    catch (NoSuchPrincipalException e) {
        assertTrue(false);
    }
    try {
        db.findByEmail("foo@bar.org");
        // We should never get here
        assertTrue(false);
    }
    catch (NoSuchPrincipalException e) {
        assertTrue(true);
    }
  }
  
  public void testFindByWikiName() {
      try {
          UserProfile profile = db.findByWikiName("JanneJalkanen");
          assertEquals("janne",           profile.getLoginName());
          assertEquals("Janne Jalkanen",  profile.getFullname());
          assertEquals("JanneJalkanen",   profile.getWikiName());
          assertEquals("{SHA}457b08e825da547c3b77fbc1ff906a1d00a7daee", profile.getPassword());
          assertEquals("janne@ecyrd.com", profile.getEmail());
      }
      catch (NoSuchPrincipalException e) {
          assertTrue(false);
      }
      try {
          db.findByEmail("foo");
          // We should never get here
          assertTrue(false);
      }
      catch (NoSuchPrincipalException e) {
          assertTrue(true);
      }
    }

  public void testFindByLoginName() {
      try {
          UserProfile profile = db.findByLoginName("janne");
          assertEquals("janne",           profile.getLoginName());
          assertEquals("Janne Jalkanen",  profile.getFullname());
          assertEquals("JanneJalkanen",   profile.getWikiName());
          assertEquals("{SHA}457b08e825da547c3b77fbc1ff906a1d00a7daee", profile.getPassword());
          assertEquals("janne@ecyrd.com", profile.getEmail());
      }
      catch (NoSuchPrincipalException e) {
          assertTrue(false);
      }
      try {
          db.findByEmail("FooBar");
          // We should never get here
          assertTrue(false);
      }
      catch (NoSuchPrincipalException e) {
          assertTrue(true);
      }
    }
  
  public void testGetWikiNames() throws WikiSecurityException
  {
      // There are 8 test users in the database
      Principal[] p = db.getWikiNames();
      assertEquals( 8, p.length );
      assertTrue( ArrayUtils.contains( p, new WikiPrincipal( "JanneJalkanen", WikiPrincipal.WIKI_NAME ) ) );
      assertTrue( ArrayUtils.contains( p, new WikiPrincipal( "", WikiPrincipal.WIKI_NAME ) ) );
      assertTrue( ArrayUtils.contains( p, new WikiPrincipal( "Administrator", WikiPrincipal.WIKI_NAME ) ) );
      assertTrue( ArrayUtils.contains( p, new WikiPrincipal( Users.ALICE, WikiPrincipal.WIKI_NAME ) ) );
      assertTrue( ArrayUtils.contains( p, new WikiPrincipal( Users.BOB, WikiPrincipal.WIKI_NAME ) ) );
      assertTrue( ArrayUtils.contains( p, new WikiPrincipal( Users.CHARLIE, WikiPrincipal.WIKI_NAME ) ) );
      assertTrue( ArrayUtils.contains( p, new WikiPrincipal( "FredFlintstone", WikiPrincipal.WIKI_NAME ) ) );
      assertTrue( ArrayUtils.contains( p, new WikiPrincipal( Users.BIFF, WikiPrincipal.WIKI_NAME ) ) );
  }

  public void testSave() {
      try {
          UserProfile profile = new DefaultUserProfile();
          profile.setEmail("user@example.com");
          profile.setLoginName("user");
          profile.setPassword("password");
          db.save(profile);
          profile = db.findByEmail("user@example.com");
          assertEquals("user@example.com", profile.getEmail());
          assertEquals("{SHA}5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8", profile.getPassword());
          db.commit();
      }
      catch (NoSuchPrincipalException e) {
          assertTrue(false);
      }
      catch (WikiSecurityException e) {
          assertTrue(false);
      }
  }
  
  public void testValidatePassword() {
      assertFalse(db.validatePassword("janne", "test"));
      assertTrue(db.validatePassword("janne", "myP@5sw0rd"));
      assertTrue(db.validatePassword("user", "password"));
  }

}
