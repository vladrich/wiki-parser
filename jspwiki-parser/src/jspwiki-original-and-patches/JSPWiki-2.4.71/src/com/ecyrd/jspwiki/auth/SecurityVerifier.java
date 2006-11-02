/* 
 JSPWiki - a JSP-based WikiWiki clone.

 Copyright (C) 2001-2005 Janne Jalkanen (Janne.Jalkanen@iki.fi)

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU Lesser General Public License as published by
 the Free Software Foundation; either version 2.1 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package com.ecyrd.jspwiki.auth;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.KeyStore;
import java.security.Permission;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.freshcookies.security.policy.Grantee;
import org.freshcookies.security.policy.PolicyReader;
import org.jdom.JDOMException;

import com.ecyrd.jspwiki.InternalWikiException;
import com.ecyrd.jspwiki.WikiEngine;
import com.ecyrd.jspwiki.WikiException;
import com.ecyrd.jspwiki.WikiSession;
import com.ecyrd.jspwiki.auth.authorize.Group;
import com.ecyrd.jspwiki.auth.authorize.GroupDatabase;
import com.ecyrd.jspwiki.auth.authorize.GroupManager;
import com.ecyrd.jspwiki.auth.authorize.Role;
import com.ecyrd.jspwiki.auth.authorize.WebContainerAuthorizer;
import com.ecyrd.jspwiki.auth.permissions.AllPermission;
import com.ecyrd.jspwiki.auth.permissions.GroupPermission;
import com.ecyrd.jspwiki.auth.permissions.PagePermission;
import com.ecyrd.jspwiki.auth.permissions.WikiPermission;
import com.ecyrd.jspwiki.auth.user.DefaultUserProfile;
import com.ecyrd.jspwiki.auth.user.UserDatabase;
import com.ecyrd.jspwiki.auth.user.UserProfile;

/**
 * Helper class for verifying JSPWiki's security configuration. Invoked by
 * <code>admin/SecurityConfig.jsp</code>.
 * @author Andrew Jaquith
 * @version $Revision: 1.10 $ $Date: 2006/09/27 05:06:12 $
 * @since 2.4
 */
public final class SecurityVerifier
{
    private static final long     serialVersionUID             = -3859563355089169941L;

    private WikiEngine            m_engine;

    private File                  m_jaasConfig                 = null;

    private boolean               m_isJaasConfigured           = false;

    private File                  m_securityPolicy             = null;

    private boolean               m_isSecurityPolicyConfigured = false;

    private Principal[]           m_policyPrincipals           = new Principal[0];

    private WikiSession           m_session;

    public static final String    ERROR                        = "Error.";

    public static final String    WARNING                      = "Warning.";

    public static final String    INFO                         = "Info.";

    public static final String    ERROR_POLICY                 = "Error.Policy";

    public static final String    WARNING_POLICY               = "Warning.Policy";

    public static final String    INFO_POLICY                  = "Info.Policy";

    public static final String    ERROR_JAAS                   = "Error.Jaas";

    public static final String    WARNING_JAAS                 = "Warning.Jaas";

    public static final String    ERROR_ROLES                  = "Error.Roles";

    public static final String    INFO_ROLES                   = "Info.Roles";
    
    public static final String    ERROR_DB                     = "Error.UserDatabase";
    
    public static final String    WARNING_DB                   = "Warning.UserDatabase";
    
    public static final String    INFO_DB                      = "Info.UserDatabase";
    
    public static final String    ERROR_GROUPS                 = "Error.GroupDatabase";
    
    public static final String    WARNING_GROUPS               = "Warning.GroupDatabase";
    
    public static final String    INFO_GROUPS                  = "Info.GroupDatabase";
    
    public static final String    INFO_JAAS                    = "Info.Jaas";

    private static final String[] CONTAINER_ACTIONS            = new String[]
                                                               { "View pages", "Comment on existing pages",
            "Edit pages", "Upload attachments", "Create a new group", "Rename an existing page", "Delete pages" };

    private static final String[] CONTAINER_JSPS               = new String[]
                                                               { "/Wiki.jsp", "/Comment.jsp", "/Edit.jsp",
            "/Upload.jsp", "/NewGroup.jsp", "/Rename.jsp", "/Delete.jsp" };

    private static final String   BG_GREEN                     = "bgcolor=\"#c0ffc0\"";

    private static final String   BG_RED                       = "bgcolor=\"#ffc0c0\"";

    Logger                        log                          = Logger.getLogger( this.getClass().getName() );

    public SecurityVerifier( WikiEngine engine, WikiSession session )
    {
        super();
        m_engine = engine;
        m_session = session;
        m_session.clearMessages();
        verifyJaas();
        verifyPolicy();
        try 
        {
            verifyPolicyAndContainerRoles();
        }
        catch ( WikiException e )
        {
            m_session.addMessage( ERROR_ROLES, e.getMessage() );
        }
        verifyGroupDatabase();
        verifyUserDatabase();
    }

    /**
     * Returns an array of unique Principals from the JSPWIki security policy
     * file. This array will be zero-length if the policy file was not
     * successfully located, or if the file did not specify any Principals in
     * the policy.
     */
    public final Principal[] policyPrincipals()
    {
        return m_policyPrincipals;
    }

    /**
     * Formats and returns an HTML table containing sample permissions and what
     * roles are allowed to have them.
     * @throws IllegalStateException if the authorizer is not of type
     *             {@link com.ecyrd.jspwiki.auth.authorize.WebContainerAuthorizer}
     */
    public final String policyRoleTable()
    {
        Principal[] roles = m_policyPrincipals;
        String wiki = m_engine.getApplicationName();

        String[] pages = new String[]
        { "Main", "Index", "GroupTest", "GroupAdmin" };
        String[] pageActions = new String[]
        { "view", "edit", "modify", "rename", "delete" };
 
        String[] groups = new String[]
        { "Admin", "TestGroup", "Foo" };
        String[] groupActions = new String[]
        { "view", "edit", null, null, "delete" };
               
        // Calculate column widths
        String colWidth;
        if ( pageActions.length > 0 && roles.length > 0 )
        {
            colWidth = String.valueOf( (double) ( 67 / ( pageActions.length * roles.length ) ) ) + "%";
        }
        else
        {
            colWidth = "67%";
        }

        StringBuffer s = new StringBuffer();

        // Write the table header
        s.append( "<table class=\"wikitable\" border=\"1\">\n" );
        s.append( "  <colgroup span=\"1\" width=\"33%\"/>\n" );
        s.append( "  <colgroup span=\"" + pageActions.length * roles.length + "\" width=\"" + colWidth
                + "\" align=\"center\"/>\n" );
        s.append( "  <tr>\n" );
        s.append( "    <th rowspan=\"2\" valign=\"bottom\">Permission</th>\n" );
        for( int i = 0; i < roles.length; i++ )
        {
            s.append( "    <th colspan=\"" + pageActions.length + "\" title=\"" + roles[i].getClass().getName() + "\">"
                    + roles[i].getName() + "</th>\n" );
        }
        s.append( "  </tr>\n" );

        // Print a column for each role
        s.append( "  <tr>\n" );
        for( int i = 0; i < roles.length; i++ )
        {
            for( int j = 0; j < pageActions.length; j++ )
            {
                String action = pageActions[j].substring( 0, 1 );
                s.append( "    <th title=\"" + pageActions[j] + "\">" + action + "</th>\n" );
            }
        }
        s.append( "  </tr>\n" );

        // Write page permission tests first
        for( int i = 0; i < pages.length; i++ )
        {
            String page = pages[i];
            s.append( "  <tr>\n" );
            s.append( "    <td>PagePermission \"" + wiki + ":" + page + "\"</td>\n" );
            for( int j = 0; j < roles.length; j++ )
            {
                for( int k = 0; k < pageActions.length; k++ )
                {
                    Permission permission = new PagePermission( wiki + ":" + page, pageActions[k] );
                    s.append( printPermissionTest( permission, roles[j], 1 ) );
                }
            }
            s.append( "  </tr>\n" );
        }
        
        // Now do the group tests
        for( int i = 0; i < groups.length; i++ )
        {
            String group = groups[i];
            s.append( "  <tr>\n" );
            s.append( "    <td>GroupPermission \"" + wiki + ":" + group + "\"</td>\n" );
            for( int j = 0; j < roles.length; j++ )
            {
                for( int k = 0; k < groupActions.length; k++ )
                {
                    Permission permission = null;
                    if ( groupActions[k] != null)
                    {
                        permission = new GroupPermission( wiki + ":" + group, groupActions[k] );
                    }
                    s.append( printPermissionTest( permission, roles[j], 1 ) );
                }
            }
            s.append( "  </tr>\n" );
        }
        

        // Now check the wiki-wide permissions
        String[] wikiPerms = new String[]
        { "createGroups", "createPages", "login", "editPreferences", "editProfile" };
        for( int i = 0; i < wikiPerms.length; i++ )
        {
            s.append( "  <tr>\n" );
            s.append( "    <td>WikiPermission \"" + wiki + "\",\"" + wikiPerms[i] + "\"</td>\n" );
            for( int j = 0; j < roles.length; j++ )
            {
                Permission permission = new WikiPermission( wiki, wikiPerms[i] );
                s.append( printPermissionTest( permission, roles[j], pageActions.length ) );
            }
            s.append( "  </tr>\n" );
        }

        // Lastly, check for AllPermission
        s.append( "  <tr>\n" );
        s.append( "    <td>AllPermission \"" + wiki + "\"</td>\n" );
        for( int j = 0; j < roles.length; j++ )
        {
            Permission permission = new AllPermission( wiki );
            s.append( printPermissionTest( permission, roles[j], pageActions.length ) );
        }
        s.append( "  </tr>\n" );

        // We're done!
        s.append( "</table>" );
        return s.toString();
    }

    /**
     * Prints a &lt;td&gt; HTML element with the results of a permission test.
     * @param perm the permission to format
     * @param allowed whether the permission is allowed
     */
    private final String printPermissionTest( Permission permission, Principal principal, int cols )
    {
        StringBuffer s = new StringBuffer();
        if ( permission == null )
        {
            s.append( "    <td colspan=\"" + cols + "\" align=\"center\" title=\"N/A\">" );
            s.append( "&nbsp;</td>\n" );
        }
        else
        {
            boolean allowed = verifyStaticPermission( principal, permission );
            s.append( "    <td colspan=\"" + cols + "\" align=\"center\" title=\"" );
            s.append( allowed ? "ALLOW: " : "DENY: " );
            s.append( permission.getClass().getName() );
            s.append( " &quot;" );
            s.append( permission.getName() );
            s.append( "&quot;" );
            if ( permission.getName() != null )
            {
                s.append( ",&quot;" );
                s.append( permission.getActions() );
                s.append( "&quot;" );
            }
            s.append( " " );
            s.append( principal.getClass().getName() );
            s.append( " &quot;" );
            s.append( principal.getName() );
            s.append( "&quot;" );
            s.append( "\"" );
            s.append( ( allowed ? BG_GREEN + ">" : BG_RED + ">" ) );
            s.append( "&nbsp;</td>\n" );
        }
        return s.toString();
    }

    /**
     * Formats and returns an HTML table containing the roles the web container
     * is aware of, and whether each role maps to particular JSPs.
     * @throws IllegalStateException if the authorizer is not of type
     *             {@link com.ecyrd.jspwiki.auth.authorize.WebContainerAuthorizer}
     */
    public final String containerRoleTable() throws WikiException
    {

        AuthorizationManager authorizationManager = m_engine.getAuthorizationManager();
        Authorizer authorizer = authorizationManager.getAuthorizer();

        // If authorizer not WebContainerAuthorizer, print error message
        if ( !( authorizer instanceof WebContainerAuthorizer ) )
        {
            throw new IllegalStateException( "Authorizer should be WebContainerAuthorizer" );
        }

        // Now, print a table with JSP pages listed on the left, and
        // an evaluation of each pages' constraints for each role
        // we discovered
        StringBuffer s = new StringBuffer();
        Principal[] roles = authorizer.getRoles();
        s.append( "<table class=\"wikitable\" border=\"1\">\n" );
        s.append( "<thead>\n" );
        s.append( "  <tr>\n" );
        s.append( "    <th rowspan=\"2\">Action</th>\n" );
        s.append( "    <th rowspan=\"2\">Page</th>\n" );
        s.append( "    <th colspan=\"" + roles.length + 1 + "\">Roles</th>\n" );
        s.append( "  </tr>\n" );
        s.append( "  <tr>\n" );
        s.append( "    <th>Anonymous</th>\n" );
        for( int i = 0; i < roles.length; i++ )
        {
            s.append( "    <th>" + roles[i].getName() + "</th>\n" );
        }
        s.append( "</tr>\n" );
        s.append( "</thead>\n" );
        s.append( "<tbody>\n" );

        try
        {
            WebContainerAuthorizer wca = (WebContainerAuthorizer) authorizer;
            for( int i = 0; i < CONTAINER_ACTIONS.length; i++ )
            {
                String action = CONTAINER_ACTIONS[i];
                String jsp = CONTAINER_JSPS[i];

                // Print whether the page is constrained for each role
                boolean allowsAnonymous = !wca.isConstrained( jsp, Role.ALL );
                s.append( "  <tr>\n" );
                s.append( "    <td>" + action + "</td>\n" );
                s.append( "    <td>" + jsp + "</td>\n" );
                s.append( "    <td title=\"" );
                s.append( allowsAnonymous ? "ALLOW: " : "DENY: " );
                s.append( jsp );
                s.append( " Anonymous" );
                s.append( "\"" );
                s.append( ( allowsAnonymous ? BG_GREEN + ">" : BG_RED + ">" ) );
                s.append( "&nbsp;</td>\n" );
                for( int j = 0; j < roles.length; j++ )
                {
                    Role role = (Role) roles[j];
                    boolean allowed = allowsAnonymous || wca.isConstrained( jsp, role );
                    s.append( "    <td title=\"" );
                    s.append( allowed ? "ALLOW: " : "DENY: " );
                    s.append( jsp );
                    s.append( " " );
                    s.append( role.getClass().getName() );
                    s.append( " &quot;" );
                    s.append( role.getName() );
                    s.append( "&quot;" );
                    s.append( "\"" );
                    s.append( ( allowed ? BG_GREEN + ">" : BG_RED + ">" ) );
                    s.append( "&nbsp;</td>\n" );
                }
                s.append( "  </tr>\n" );
            }
        }
        catch( JDOMException e )
        {
            // If we couldn't evaluate constraints it means
            // there's some sort of IO mess or parsing issue
            log.error( "Malformed XML in web.xml", e );
            throw new InternalWikiException( e.getClass().getName() + ": " + e.getMessage() );
        }

        s.append( "</tbody>\n" );
        s.append( "</table>\n" );
        return s.toString();
    }

    /**
     * Returns <code>true</code> if JAAS is configured correctly.
     * @return the result of the configuration check
     */
    public final boolean isJaasConfigured()
    {
        return m_isJaasConfigured;
    }

    /**
     * Returns <code>true</code> if the JAAS login configuration was already
     * set when JSPWiki started up. We determine this value by consulting a
     * protected member field of {@link AuthenticationManager}, which was set
     * at in initialization by {@link PolicyLoader}.
     * @return <code>true</code> if {@link PolicyLoader} successfully set the
     *         policy, or <code>false</code> for any other reason.
     */
    public final boolean isJaasConfiguredAtStartup()
    {
        return m_engine.getAuthenticationManager().m_isJaasConfiguredAtStartup;
    }

    /**
     * Returns <code>true</code> if JSPWiki can locate a named JAAS login
     * configuration.
     * @param config the name of the application (e.g.,
     *            <code>JSPWiki-container</code>).
     * @return <code>true</code> if found; <code>false</code> otherwise
     */
    protected final boolean isJaasConfigurationAvailable( String config )
    {
        try
        {
            m_session.addMessage( INFO_JAAS, "We found the '" + config + "' login configuration." );
            new LoginContext( config );
            return true;
        }
        catch( Exception e )
        {
            m_session.addMessage( ERROR_JAAS, "We could not find the '" + config + "' login configuration.</p>" );
            return false;
        }
    }

    /**
     * Returns <code>true</code> if the Java security policy is configured
     * correctly, and it verifies as valid.
     * @return the result of the configuration check
     */
    public final boolean isSecurityPolicyConfigured()
    {
        return m_isSecurityPolicyConfigured;
    }

    /**
     * Returns <code>true</code> if the Java security policy file was already
     * set when JSPWiki started up. We determine this value by consulting a
     * protected member field of {@link AuthenticationManager}, which was set
     * at in initialization by {@link PolicyLoader}.
     * @return <code>true</code> if {@link PolicyLoader} successfully set the
     *         policy, or <code>false</code> for any other reason.
     */
    public final boolean isSecurityPolicyConfiguredAtStartup()
    {
        return m_engine.getAuthenticationManager().m_isJavaPolicyConfiguredAtStartup;
    }

    /**
     * If the active Authorizer is the WebContainerAuthorizer, returns the roles
     * it knows about; otherwise, a zero-length array.
     * @return the roles parsed from <code>web.xml</code>, or a zero-length array
     */
    public final Principal[] webContainerRoles() throws WikiException
    {
        Authorizer authorizer = m_engine.getAuthorizationManager().getAuthorizer();
        if ( authorizer instanceof WebContainerAuthorizer )
        {
            return ( (WebContainerAuthorizer) authorizer ).getRoles();
        }
        return new Principal[0];
    }

    /**
     * Verifies that the roles given in the security policy are reflected by the
     * container <code>web.xml</code> file.
     */
    protected final void verifyPolicyAndContainerRoles() throws WikiException
    {
        Authorizer authorizer = m_engine.getAuthorizationManager().getAuthorizer();
        Principal[] containerRoles = authorizer.getRoles();
        boolean missing = false;
        for( int i = 0; i < m_policyPrincipals.length; i++ )
        {
            Principal principal = m_policyPrincipals[i];
            if ( principal instanceof Role )
            {
                Role role = (Role) principal;
                boolean isContainerRole = ArrayUtils.contains( containerRoles, role );
                if ( !Role.isBuiltInRole( role ) && !isContainerRole )
                {
                    m_session.addMessage( ERROR_ROLES, "Role '" + role.getName() + "' is defined in security policy but not in web.xml." );
                    missing = true;
                }
            }
        }
        if ( !missing )
        {
            m_session.addMessage( INFO_ROLES, "Every non-standard role defined in the security policy was also found in web.xml." );
        }
    }

    /**
     * Verifies that the group datbase was initialized properly, and that 
     * user add and delete operations work as they should.
     */
    protected final void verifyGroupDatabase()
    {
        GroupManager mgr = m_engine.getGroupManager();
        GroupDatabase db = null;
        try 
        {
            db = m_engine.getGroupManager().getGroupDatabase();
        }
        catch ( WikiSecurityException e )
        {
            m_session.addMessage( ERROR_GROUPS, "Could not retrieve GroupManager: " + e.getMessage() );
        }
        
        // Check for obvious error conditions
        if ( mgr == null || db == null )
        {
            if ( mgr == null )
            {
                m_session.addMessage( ERROR_GROUPS, "GroupManager is null; JSPWiki could not " +
                        "initialize it. Check the error logs." );
            }
            if ( db == null )
            {
                m_session.addMessage( ERROR_GROUPS, "GroupDatabase is null; JSPWiki could not " +
                        "initialize it. Check the error logs." );
            }
            return;
        }
        
        // Everything initialized OK...
        
        // Tell user what class of database this is.
        m_session.addMessage( INFO_GROUPS, "GroupDatabase is of type '" + db.getClass().getName() + 
                "'. It appears to be initialized properly." );
        
        // Now, see how many groups we have.
        int oldGroupCount = 0;
        try 
        {
            Group[] groups = db.groups();
            oldGroupCount = groups.length;
            m_session.addMessage( INFO_GROUPS, "The group database contains " + oldGroupCount + " groups." );
        }
        catch ( WikiSecurityException e )
        {
            m_session.addMessage( ERROR_GROUPS, "Could not obtain a list of current groups: " + e.getMessage() );
            return;
        }
        
        // Try adding a bogus group with random name
        String name = "TestGroup" + String.valueOf( System.currentTimeMillis() );
        Group group = null;
        try
        {
            // Create dummy test group
            group = mgr.parseGroup( name, "", true );
            Principal user = new WikiPrincipal( "TestUser" );
            group.add( user );
            db.save( group, new WikiPrincipal("SecurityVerifier") );
            db.commit();
            
            // Make sure the group saved successfully
            if ( db.groups().length == oldGroupCount )
            {
                m_session.addMessage( ERROR_GROUPS, "Could not add a test group to the database." );
                return;
            }
            m_session.addMessage( INFO_GROUPS, "The group database allows new groups to be created, as it should." );
        }
        catch ( WikiSecurityException e )
        {
            m_session.addMessage( ERROR_GROUPS, "Could not add a group to the database: " + e.getMessage() );
            return;
        }

        // Now delete the group; should be back to old count
        if ( group == null )
        {
          m_session.addMessage( ERROR_GROUPS, "Skipped group deletion test." );
          return;
        }
        
        try 
        {
            db.delete( group );
            db.commit();
            if ( db.groups().length != oldGroupCount )
            {
                m_session.addMessage( ERROR_GROUPS, "Could not delete a test group from the database." );
                return;
            }
            m_session.addMessage( INFO_GROUPS, "The group database allows groups to be deleted, as it should." );
        }
        catch ( WikiSecurityException e )
        {
            m_session.addMessage( ERROR_GROUPS, "Could not delete a test group from the database: " + e.getMessage() );
            return;
        }
        
        m_session.addMessage( INFO_GROUPS, "The group database configuration looks fine." );
    }
    
    /**
     * Verfies the JAAS configuration. The configuration is valid if value of
     * the system property <code>java.security.auth.login.config</code>
     * resolves to an existing file, and we can find the JAAS login
     * configurations for <code>JSPWiki-container</code> and
     * <code>JSPWiki-custom</code>.
     */
    protected final void verifyJaas()
    {
        // See if JAAS is on
        AuthorizationManager authMgr = m_engine.getAuthorizationManager();
        if ( !authMgr.isJAASAuthorized() )
        {
            m_session.addMessage( ERROR_JAAS, "JSPWiki's JAAS-based authentication " +
                    "and authorization system is turned off (your <code>jspwiki.properties</code> " +
                    "contains the setting 'jspwiki.security = container'. This " +
                    "setting disables authorization checks and is meant for testing " +
                    "and troubleshooting only. The test results on this page will not " +
                    "be reliable as a result. You should set this to 'jaas' " +
                    "so that security works properly." );
        }
        
        // Validate the property is set correctly
        m_jaasConfig = getFileFromProperty( "java.security.auth.login.config" );

        // Look for the JSPWiki-container config
        boolean foundJaasContainerConfig = isJaasConfigurationAvailable( "JSPWiki-container" );

        // Look for the JSPWiki-custom config
        boolean foundJaasCustomConfig = isJaasConfigurationAvailable( "JSPWiki-custom" );

        m_isJaasConfigured = ( m_jaasConfig != null && foundJaasContainerConfig && foundJaasCustomConfig );
    }

    protected final File getFileFromProperty( String property )
    {
        String propertyValue = null;
        try
        {
            propertyValue = System.getProperty( property );
            if ( propertyValue == null )
            {
                m_session.addMessage( "Error." + property, "The system property '" + property + "' is null." );
                return null;
            }
            
            //
            //  It's also possible to use "==" to mark a property.  We remove that
            //  here so that we can actually find the property file, then.
            //
            if( propertyValue.startsWith("=") )
            {
                propertyValue = propertyValue.substring(1);
            }
            
            try
            {
                m_session.addMessage( "Info." + property, "The system property '" + property + "' is set to: "
                        + propertyValue + "." );
               
                // Prepend a file: prefix if not there already
                if ( !propertyValue.startsWith( "file:" ) )
                {
                  propertyValue = "file:" + propertyValue;
                }
                URL url = new URL( propertyValue );
                File file = new File( url.getPath() );
                if ( file.exists() )
                {
                    m_session.addMessage( "Info." + property, "File '" + propertyValue + "' exists in the filesystem." );
                    return file;
                }
            }
            catch( MalformedURLException e )
            {
                // Swallow exception because we can't find it anyway
            }
            m_session.addMessage( "Error." + property, "File '" + propertyValue
                    + "' doesn't seem to exist. This might be a problem." );
            return null;
        }
        catch( SecurityException e )
        {
            m_session.addMessage( "Error." + property, "We could not read system property '" + property
                    + "'. This is probably because you are running with a security manager." );
            return null;
        }
    }

    /**
     * Verfies the Java security policy configuration. The configuration is
     * valid if value of the system property <code>java.security.policy</code>
     * resolves to an existing file, and the policy file that this file
     * represents a valid policy.
     */
    protected final void verifyPolicy()
    {
        // Look up the policy property and set the status text.
        m_securityPolicy = getFileFromProperty( "java.security.policy" );

        // Next, verify the policy
        if ( m_securityPolicy != null )
        {
            // Get the file
            PolicyReader policy = new PolicyReader( m_securityPolicy );
            m_session.addMessage( INFO_POLICY, "The security policy '" + policy.getFile() + "' exists." );
            try
            {
                // See if there is a keystore that's valid
                KeyStore ks = policy.getKeyStore();
                if ( ks == null )
                {
                    m_session.addMessage( ERROR_POLICY,
                            "Policy file does not have a keystore... at least not one that we can locate." );
                }
                else
                {
                    m_session
                            .addMessage( INFO_POLICY,
                                    "The security policy specifies a keystore, and we were able to locate it in the filesystem." );
                }

                // Verify the file
                policy.read();
                List errors = policy.getMessages();
                if ( errors.size() > 0 )
                {
                    for( Iterator it = errors.iterator(); it.hasNext(); )
                    {
                        Exception e = (Exception) it.next();
                        m_session.addMessage( ERROR_POLICY, e.getMessage() );
                    }
                }
                else
                {
                    m_session.addMessage( INFO_POLICY, "The security policy looks fine." );
                    m_isSecurityPolicyConfigured = true;
                }

                // Stash the unique principals mentioned in the file,
                // plus our standard roles.
                Set principals = new LinkedHashSet();
                principals.add( Role.ALL );
                principals.add( Role.ANONYMOUS );
                principals.add( Role.ASSERTED );
                principals.add( Role.AUTHENTICATED );
                Grantee[] grantees = policy.grantees();
                for( int i = 0; i < grantees.length; i++ )
                {
                    Principal[] granteePrincipals = grantees[i].getPrincipals();
                    for( int j = 0; j < granteePrincipals.length; j++ )
                    {
                        principals.add( granteePrincipals[j] );
                    }
                }
                m_policyPrincipals = (Principal[]) principals.toArray( new Principal[principals.size()] );
            }
            catch( IOException e )
            {
                m_session.addMessage( ERROR_POLICY, e.getMessage() );
            }
        }
    }

    /**
     * Verifies that a particular Principal possesses a Permission, as defined
     * in the security policy file.
     * @param principal the principal
     * @param permission the permission
     * @return the result, based on consultation with the active Java security
     *         policy
     */
    protected final boolean verifyStaticPermission( Principal principal, final Permission permission )
    {
        Subject subject = new Subject();
        subject.getPrincipals().add( principal );
        try 
        {
            Subject.doAsPrivileged( subject, new PrivilegedAction()
            {
                public Object run()
                {
                        AccessController.checkPermission( permission );
                        return null;
                }
            }, null );
            return true;
        }
        catch ( AccessControlException e )
        {
            return false;
        }
    }
    
    /**
     * Verifies that the user datbase was initialized properly, and that 
     * user add and delete operations work as they should.
     */
    protected final void verifyUserDatabase()
    {
        UserDatabase db = m_engine.getUserManager().getUserDatabase();
        
        // Check for obvious error conditions
        if ( db == null )
        {
            m_session.addMessage( ERROR_DB, "UserDatabase is null; JSPWiki could not " +
                    "initialize it. Check the error logs." );
        }
        if ( db instanceof UserManager.DummyUserDatabase )
        {
            m_session.addMessage( ERROR_DB, "UserDatabase is DummyUserDatabase; JSPWiki " +
                    "may not have been able to initialize the database you supplied in " +
                    "jspwiki.properties, or you left the 'jspwiki.userdatabase' property " +
                    "blank. Check the error logs." );
        }
        
        // Tell user what class of database this is.
        m_session.addMessage( INFO_DB, "UserDatabase is of type '" + db.getClass().getName() + 
                "'. It appears to be initialized properly." );
        
        // Now, see how many users we have.
        int oldUserCount = 0;
        try 
        {
            Principal[] users = db.getWikiNames();
            oldUserCount = users.length;
            m_session.addMessage( INFO_DB, "The user database contains " + oldUserCount + " users." );
        }
        catch ( WikiSecurityException e )
        {
            m_session.addMessage( ERROR_DB, "Could not obtain a list of current users: " + e.getMessage() );
            return;
        }
        
        // Try adding a bogus user with random name
        String loginName = "TestUser" + String.valueOf( System.currentTimeMillis() );
        try
        {
            UserProfile profile = new DefaultUserProfile();
            profile.setEmail("testuser@testville.com");
            profile.setLoginName( loginName );
            profile.setWikiName( "WikiName"+loginName );
            profile.setFullname( "FullName"+loginName );
            profile.setPassword("password");
            db.save(profile);
            db.commit();
            
            // Make sure the profile saved successfully
            if ( db.getWikiNames().length == oldUserCount )
            {
                m_session.addMessage( ERROR_DB, "Could not add a test user to the database." );
                return;
            }
            m_session.addMessage( INFO_DB, "The user database allows new users to be created, as it should." );
        }
        catch ( WikiSecurityException e )
        {
            m_session.addMessage( ERROR_DB, "Could not add a test user to the database: " + e.getMessage() );
            return;
        }

        // Now delete the profile; should be back to old count
        try 
        {
            db.deleteByLoginName( loginName );
            db.commit();
            if ( db.getWikiNames().length != oldUserCount )
            {
                m_session.addMessage( ERROR_DB, "Could not delete a test user from the database." );
                return;
            }
            m_session.addMessage( INFO_DB, "The user database allows users to be deleted, as it should." );
        }
        catch ( WikiSecurityException e )
        {
            m_session.addMessage( ERROR_DB, "Could not delete a test user to the database: " + e.getMessage() );
            return;
        }
        
        m_session.addMessage( INFO_DB, "The user database configuration looks fine." );
    }

    /**
     * Returns the location of the JAAS configuration file if and only if the
     * <code>java.security.auth.login.config</code> is set <em>and</em> the
     * file it points to exists in the file system; returns <code>null</code>
     * in all other cases.
     * @return the location of the JAAS configuration file
     */
    public final File jaasConfiguration()
    {
        return m_jaasConfig;
    }

    /**
     * Returns the location of the Java security policy file if and only if the
     * <code>java.security.policy</code> is set <em>and</em> the file it
     * points to exists in the file system; returns <code>null</code> in all
     * other cases.
     * @return the location of the Java security polifile
     */
    public final File securityPolicy()
    {
        return m_securityPolicy;
    }
}
