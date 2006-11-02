/*
    JSPWiki - a JSP-based WikiWiki clone.

    Copyright (C) 2001-2006 Janne Jalkanen (Janne.Jalkanen@iki.fi)

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

package com.ecyrd.jspwiki.event;

import java.util.EventObject;

/**
 * Abstract parent class for wiki events.
 *
 * @author  Murray Altheim
 * @author  Andrew Jaquith
 * @version $Revision: 1.3 $ $Date: 2006/08/27 14:05:50 $
 * @since 2.3.79
 */
public abstract class WikiEvent extends EventObject
{
    private static final long serialVersionUID = 1829433967558773960L;

    /** Indicates a exception or error state. */
    public static final int ERROR          = -99;

    /** Indicates an undefined state. */
    public static final int UNDEFINED      = -98;

    private int m_type = UNDEFINED;

    // ............


   /**
     * Constructs an instance of this event.
     * @param source the Object that is the source of the event.
     * @param type   the event type.
     */
    public WikiEvent( Object source, int type )
    {
        super( source );
        setType( type );
    }


   /**
     * Sets the type of this event. Validation of acceptable
     * type values is the responsibility of each subclass.
     *
     * @param type      the type of this WikiEvent.
     */
    protected void setType( int type )
    {
        m_type = type;
    }


   /**
     * Returns the type of this event.
     *
     * @return  the type of this WikiEvent. See the enumerated values
     *          defined in {@link com.ecyrd.jspwiki.event.WikiEvent}).
     */
    public int getType()
    {
        return m_type;
    }


   /** Returns a String (human-readable) description of an event type.
     * This should be subclassed as necessary.
     * @return the String description
     */
    public String getTypeDescription()
    {
        switch ( m_type )
        {
            case ERROR:                return "exception or error event";
            case UNDEFINED:            return "undefined event type";
            default:                   return "unknown event type (" + m_type + ")";
        }
    }


   /**
     * Returns true if the int value is a valid WikiEvent type.
     * Because the WikiEvent class does not itself any event types,
     * this method returns true if the event type is anything except
     * {@link #ERROR} or {@link #UNDEFINED}. This method is meant to
     * be subclassed as appropriate.
     */
    public static boolean isValidType( int type )
    {
        return ( type != ERROR && type != UNDEFINED );
    }


    /**
     * Returns a textual representation of an event type.
     * @return the String representation
     */
    public String eventName()
    {
        switch( m_type )
        {
            case ERROR:                return "ERROR";
            case UNDEFINED:            return "UNDEFINED";
            default:                   return "UNKNOWN (" + m_type + ")";
        }
    }

    /**
     * Prints a String (human-readable) representation of this object.
     * This should be subclassed as necessary.
     * @see java.lang.Object#toString()
     * @return the String representation
     */
    public String toString()
    {
        StringBuffer out = new StringBuffer();
        out.append( "WikiEvent." );
        out.append( eventName() );
        out.append( " [source=" );
        out.append( getSource().toString() );
        out.append( "]" );
        return out.toString();
    }

}
