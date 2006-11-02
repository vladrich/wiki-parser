package com.ecyrd.jspwiki.auth.permissions;

import junit.framework.TestCase;


/**
 * @author Andrew Jaquith
 */
public class WikiPermissionTest extends TestCase {

  public void testHashCode() {
  }

  public void testWikiPermission() {
    WikiPermission p = new WikiPermission("*", "createPages");
    assertEquals("*", p.getName());
    assertEquals("*", p.getWiki());
    assertEquals("createpages", p.getActions());
  }

  /*
   * Class under test for boolean equals(Object)
   */
  public void testEqualsObject() {
    WikiPermission p1 = new WikiPermission("*", "createPages");
    WikiPermission p2 = new WikiPermission("*", "createPages");
    WikiPermission p3 = new WikiPermission("*", "createGroups");
    assertTrue(p1.equals(p2));
    assertTrue(p2.equals(p1));
    assertFalse(p1.equals(p3));
    assertFalse(p3.equals(p1));
    WikiPermission p4 = new WikiPermission("*", "createPages,createGroups");
    WikiPermission p5 = new WikiPermission("*", "createGroups,createPages");
    assertTrue(p4.equals(p5));
  }

  /*
   * Class under test for boolean equals(Object)
   */
  public void testEqualsObjectNSi() {
    WikiPermission p1 = new WikiPermission("mywiki", "createPages");
    WikiPermission p2 = new WikiPermission("*",      "createPages");
    WikiPermission p3 = new WikiPermission("mywiki", "createGroups");
    assertFalse(p1.equals(p2));
    assertFalse(p2.equals(p1));
    assertFalse(p1.equals(p3));
    assertFalse(p3.equals(p1));
    WikiPermission p4 = new WikiPermission("mywiki", "createPages,createGroups");
    WikiPermission p5 = new WikiPermission("*",      "createGroups,createPages");
    assertFalse(p4.equals(p5));
  }
  
  /*
   * Class under test for String getActions()
   */
  public void testGetActions() {
    WikiPermission p1 = new WikiPermission("*", "createPages,createGroups,editProfile");
    assertEquals("creategroups,createpages,editprofile", p1.getActions());
    WikiPermission p2 = new WikiPermission("*", "createGroups,editProfile,createPages");
    assertEquals("creategroups,createpages,editprofile", p2.getActions());
  }

  /*
   * Class under test for boolean implies(Permission)
   */
  public void testImpliesPermission() {
    // Superset of actions implies all individual actions
    WikiPermission p1 = new WikiPermission("*", "createPages,createGroups,editProfile");
    WikiPermission p2 = new WikiPermission("*", "createPages");
    WikiPermission p3 = new WikiPermission("*", "createGroups");
    WikiPermission p5 = new WikiPermission("*", "editPreferences");
    WikiPermission p6 = new WikiPermission("*", "editProfile");
    assertTrue(p1.implies(p2));
    assertFalse(p2.implies(p1));
    assertTrue(p1.implies(p3));
    assertFalse(p3.implies(p1));
    assertTrue(p1.implies(p6));
    assertFalse(p6.implies(p1));
    
    // createGroups implies createPages
    assertTrue(p3.implies(p2));
    assertFalse(p2.implies(p3));
    
    // editProfile implies nothing
    assertFalse(p6.implies(p5));
    assertFalse(p6.implies(p3));
    assertFalse(p6.implies(p3));
    assertFalse(p6.implies(p1));
  }

  /*
   * Class under test for boolean implies(Permission)
   */
  public void testImpliesPermissionNS() {
    // Superset of actions implies all individual actions
    WikiPermission p1 = new WikiPermission("*",      "createPages,createGroups,editProfile");
    WikiPermission p2 = new WikiPermission("mywiki", "createPages");
    WikiPermission p3 = new WikiPermission("mywiki", "createGroups");
    WikiPermission p4 = new WikiPermission("urwiki", "editProfile");
    WikiPermission p5 = new WikiPermission("*",      "editPreferences");
    assertTrue(p1.implies(p2));
    assertFalse(p2.implies(p1));
    assertTrue(p1.implies(p3));
    assertFalse(p3.implies(p1));
    assertTrue(p1.implies(p4));
    assertFalse(p4.implies(p1));
    
    // createGroups implies createPages
    assertTrue(p3.implies(p2));
    assertFalse(p2.implies(p3));
    
    // editPreferences does not imply editProfile
    assertFalse(p5.implies(p4));
    assertFalse(p4.implies(p5));
  }
  
  /*
   * Class under test for String toString()
   */
  public void testToString() {
    WikiPermission p1 = new WikiPermission("*", "createPages,createGroups,editProfile");
    String result = "(\"com.ecyrd.jspwiki.auth.permissions.WikiPermission\",\"*\",\"creategroups,createpages,editprofile\")";
    assertEquals(result, p1.toString());
  }

  public void testImpliedMask() {
    assertEquals(3, WikiPermission.impliedMask(1));
    assertEquals(2, WikiPermission.impliedMask(2));
    assertEquals(4, WikiPermission.impliedMask(4));
  }

  public void testCreateMask() {
    assertEquals(1, WikiPermission.createMask("createGroups"));
    assertEquals(2, WikiPermission.createMask("createPages"));
    assertEquals(3, WikiPermission.createMask("createGroups,createPages"));
    assertEquals(4, WikiPermission.createMask("editPreferences"));
    assertEquals(5, WikiPermission.createMask("createGroups,editPreferences"));
    assertEquals(6, WikiPermission.createMask("createPages,editPreferences"));
    assertEquals(7, WikiPermission.createMask("createGroups,createPages,editPreferences"));
    assertEquals(8, WikiPermission.createMask("editProfile"));
    assertEquals(9, WikiPermission.createMask("createGroups,editProfile"));
    assertEquals(16, WikiPermission.createMask("login"));
    assertEquals(24, WikiPermission.createMask("login,editProfile"));
  }

}
