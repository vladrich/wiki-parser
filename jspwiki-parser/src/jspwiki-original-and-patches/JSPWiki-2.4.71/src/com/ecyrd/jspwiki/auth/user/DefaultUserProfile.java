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
package com.ecyrd.jspwiki.auth.user;

import java.util.Date;

/**
 * Default implementation for representing wiki user information, such as the
 * login name, full name, wiki name, and e-mail address.
 * @author Janne Jalkanen
 * @author Andrew Jaquith
 * @version $Revision: 1.7 $ $Date: 2006/08/01 11:26:32 $
 * @since 2.3
 */

public class DefaultUserProfile implements UserProfile
{
    private Date     m_created   = null;

    private String   m_email     = null;

    private String   m_fullname  = null;

    private String   m_loginName = null;
    
    private Date     m_modified  = null;

    private String   m_password  = null;

    private String   m_wikiname  = null;

    public boolean equals( Object o )
    {
        if ( ( o != null ) && ( o instanceof UserProfile ) )
        {
            DefaultUserProfile u = (DefaultUserProfile) o;
            return ( same( m_fullname, u.m_fullname ) && same( m_password, u.m_password )
                    && same( m_loginName, u.m_loginName ) && same( m_email, u.m_email ) && same( m_wikiname,
                    u.m_wikiname ) );
        }

        return false;
    }
    
    public int hashCode()
    {
        return (m_fullname  != null ? m_fullname.hashCode()  : 0) ^ 
               (m_password  != null ? m_password.hashCode()  : 0) ^ 
               (m_loginName != null ? m_loginName.hashCode() : 0) ^ 
               (m_wikiname  != null ? m_wikiname.hashCode()  : 0) ^ 
               (m_email     != null ? m_email.hashCode()     : 0);
    }
    
    /**
     * Returns the creation date
     * @return the creation date
     * @see com.ecyrd.jspwiki.auth.user.UserProfile#getCreated()
     */
    public Date getCreated()
    {
        return m_created;
    }

    /**
     * Returns the user's e-mail address.
     * @return the e-mail address
     */
    public String getEmail()
    {
        return m_email;
    }

    /**
     * Returns the user's full name.
     * @return the full name
     */
    public String getFullname()
    {
        return m_fullname;
    }
    
    /**
     * Returns the last-modified date.
     * @return the last-modified date
     * @see com.ecyrd.jspwiki.auth.user.UserProfile#getLastModified()
     */
    public Date getLastModified()
    {
        return m_modified;
    }

    /**
     * Returns the user's login name.
     * @return the login name
     */
    public String getLoginName()
    {
        return m_loginName;
    }

    /**
     * Returns the user password for use with custom authentication. Note that
     * the password field is not meaningful for container authentication; the
     * user's private credentials are generally stored elsewhere. While it
     * depends on the {@link UserDatabase}implementation, in most cases the
     * value returned by this method will be a password hash, not the password
     * itself.
     * @return the password
     */
    public String getPassword()
    {
        return m_password;
    }

    /**
     * Returns the user's wiki name.
     * @return the wiki name.
     */
    public String getWikiName()
    {
        return m_wikiname;
    }

    /**
     * Returns <code>true</code> if the user profile is
     * new. This implementation checks whether 
     * {@link #getLastModified()} returns <code>null</code>
     * to determine the status.
     * @see com.ecyrd.jspwiki.auth.user.UserProfile#isNew()
     */
    public boolean isNew()
    {
        return ( m_modified == null );
    }
    
    /**
     * @param date the creation date
     * @see com.ecyrd.jspwiki.auth.user.UserProfile#setCreated(java.util.Date)
     */
    public void setCreated(Date date) 
    {
        m_created = date;
    }
    
    /**
     * Sets the user's e-mail address.
     * @param email the e-mail address
     */
    public void setEmail( String email )
    {
        m_email = email;
    }

    /**
     * Sets the user's full name. For example, "Janne Jalkanen."
     * @param arg the full name
     */
    public void setFullname( String arg )
    {
        m_fullname = arg;
    }

    /**
     * Sets the last-modified date.
     * @param date the last-modified date
     * @see com.ecyrd.jspwiki.auth.user.UserProfile#setLastModified(java.util.Date)
     */
    public void setLastModified( Date date ) 
    {
        m_modified = date;
    }
    
    /**
     * Sets the name by which the user logs in. The login name is used as the
     * username for custom authentication (see
     * {@link com.ecyrd.jspwiki.auth.AuthenticationManager#login(WikiSession, String, String)}).
     * The login name is typically a short name ("jannej"). In contrast, the
     * wiki name is typically of type FirstnameLastName ("JanneJalkanen").
     * @param name the login name
     */
    public void setLoginName( String name )
    {
        m_loginName = name;
    }

    /**
     * Sets the user's password for use with custom authentication. It is
     * <em>not</em> the responsibility of implementing classes to hash the
     * password; that responsibility is borne by the UserDatabase implementation
     * during save operations (see {@link UserDatabase#save(UserProfile)}).
     * Note that the password field is not meaningful for container
     * authentication; the user's private credentials are generally stored
     * elsewhere.
     * @param arg the password
     */
    public void setPassword( String arg )
    {
        m_password = arg;
    }

    /**
     * Sets the user's wiki name. This is typically of type FirstnameLastName
     * ("JanneJalkanen").
     * @param name the wiki name
     */
    public void setWikiName( String name )
    {
        m_wikiname = name;
    }

    /**
     * Returns a string representation of this user profile.
     * @return the string
     */
    public String toString()
    {
        return "[DefaultUserProfile: '" + getFullname() + "']";
    }

    /**
     * Private method that compares two objects and determines whether they are
     * equal. Two nulls are considered equal.
     * @param arg1 the first object
     * @param arg2 the second object
     * @return the result of the comparison
     */
    private boolean same( Object arg1, Object arg2 )
    {
        if ( arg1 == null && arg2 == null )
        {
            return true;
        }
        if ( arg1 == null || arg2 == null )
        {
            return false;
        }
        return ( arg1.equals( arg2 ) );
    }
}