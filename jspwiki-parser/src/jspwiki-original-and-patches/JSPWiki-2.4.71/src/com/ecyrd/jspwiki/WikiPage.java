/* 
    JSPWiki - a JSP-based WikiWiki clone.

    Copyright (C) 2001 Janne Jalkanen (Janne.Jalkanen@iki.fi)

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
package com.ecyrd.jspwiki;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.ecyrd.jspwiki.auth.acl.Acl;
import com.ecyrd.jspwiki.providers.WikiPageProvider;

/**
 *  Simple wrapper class for the Wiki page attributes.  The Wiki page
 *  content is moved around in Strings, though.
 */

// FIXME: We need to rethink how metadata is being used - probably the 
//        author, date, etc. should also be part of the metadata.  We also
//        need to figure out the metadata lifecycle.

public class WikiPage
    implements Cloneable,
               Comparable
{
    private static final long serialVersionUID = 1L;

    private       String     m_name;
    private       WikiEngine m_engine;
    private       String     m_wiki;
    private Date             m_lastModified;
    private long             m_fileSize = -1;
    private int              m_version = WikiPageProvider.LATEST_VERSION;
    private String           m_author = null;
    private final HashMap    m_attributes = new HashMap();

    /**
     *  "Summary" is a short summary of the page.  It is a String.
     */
    public static final String DESCRIPTION = "summary";

    public static final String ALIAS = "alias";
    public static final String REDIRECT = "redirect";

    public static final String SIZE = "size";

    public static final String CHANGENOTE = "changenote";
    
    private Acl m_accessList = null;
    
    public WikiPage( WikiEngine engine, String name )
    {
        m_engine = engine;
        m_name = name;
        m_wiki = engine.getApplicationName();
    }

    public String getName()
    {
        return m_name;
    }

    /**
     *  A WikiPage may have a number of attributes, which might or might not be 
     *  available.  Typically attributes are things that do not need to be stored
     *  with the wiki page to the page repository, but are generated
     *  on-the-fly.  A provider is not required to save them, but they
     *  can do that if they really want.
     *
     *  @param key The key using which the attribute is fetched
     *  @return The attribute.  If the attribute has not been set, returns null.
     */
    public Object getAttribute( String key )
    {
        return m_attributes.get( key );
    }

    /**
     *  Sets an metadata attribute.
     */
    public void setAttribute( String key, Object attribute )
    {
        m_attributes.put( key, attribute );
    }

    /**
     * Returns the full attributes Map, in case external code needs
     * to iterate through the attributes.
     */
    public Map getAttributes() 
    {
        return m_attributes;
    }

    /**
     *  Removes an attribute from the page, if it exists.
     *  @return If the attribute existed, returns the object.
     *  @since 2.1.111
     */
    public Object removeAttribute( String key )
    {
        return m_attributes.remove( key );
    }

    /**
     *  Returns the date when this page was last modified.
     */
    public Date getLastModified()
    {
        return m_lastModified;
    }

    public void setLastModified( Date date )
    {
        m_lastModified = date;
    }

    public void setVersion( int version )
    {
        m_version = version;
    }

    /**
     *  Returns the version that this WikiPage instance represents.
     */
    public int getVersion()
    {
        return m_version;
    }

    /**
     *  @since 2.1.109
     */
    public long getSize()
    {
        return( m_fileSize );
    }

    /**
     *  @since 2.1.109
     */
    public void setSize( long size )
    {
        m_fileSize = size;
    }

    /**
     *  Returns the Acl for this page.  May return null, 
     *  in case there is no ACL defined for this page, or it has not
     *  yet been received.
     */
    public Acl getAcl()
    {
        return m_accessList;
    }

    public void setAcl( Acl acl )
    {
        m_accessList = acl;
    }

    public void setAuthor( String author )
    {
        m_author = author;
    }

    /**
     *  Returns author name, or null, if no author has been defined.
     */
    public String getAuthor()
    {
        return m_author;
    }
    
    /**
     *  Returns the wiki nanme for this page
     */
    public String getWiki()
    {
        return m_wiki;
    }

    /**
     *  This method will remove all metadata from the page.
     */
    public void invalidateMetadata()
    {        
        m_hasMetadata = false;
        setAcl( null );
        m_attributes.clear();
    }

    private boolean m_hasMetadata = false;

    /**
     *  Returns <code>true</code> if the page has valid metadata; that is, it has been parsed.
     */
    public boolean hasMetadata()
    {
        return m_hasMetadata;
    }

    public void setHasMetadata()
    {
        m_hasMetadata = true;
    }

    public String toString()
    {
        return "WikiPage ["+m_wiki+":"+m_name+",ver="+m_version+",mod="+m_lastModified+"]";
    }

    /**
     *  Creates a deep clone of a WikiPage.  Strings are not cloned, since
     *  they're immutable.  Attributes are not cloned, only the internal
     *  HashMap (so if you modify the contents of a value of an attribute,
     *  these will reflect back to everyone).
     */
    public Object clone()
    {
        try
        {
            WikiPage p = (WikiPage)super.clone();
       
            p.m_engine = m_engine;
            p.m_name   = m_name;
            p.m_wiki   = m_wiki;
            
            p.m_author       = m_author;
            p.m_version      = m_version;
            p.m_lastModified = m_lastModified != null ? (Date)m_lastModified.clone() : null;

            p.m_fileSize     = m_fileSize;
                            
            p.m_attributes.putAll(m_attributes);
            
            return p;
        }
        catch( CloneNotSupportedException e )
        {}
        
        return null;
    }
    
    public int compareTo( Object o )
    {
        int res = 0;
        if( o instanceof WikiPage )
        {
            WikiPage c = (WikiPage)o;
        
            res = this.getName().compareTo(c.getName());
            
            if( res == 0 ) res = this.getVersion()-c.getVersion();
        }
            
        return res;
    }
}
