package com.ecyrd.jspwiki.auth;

import java.util.ArrayList;
import java.util.List;

import com.ecyrd.jspwiki.event.WikiEvent;
import com.ecyrd.jspwiki.event.WikiEventListener;
import com.ecyrd.jspwiki.event.WikiSecurityEvent;

/**
 * Traps the most recent WikiEvent so that it can be used in assertions.
 * @author Andrew Jaquith
 * @version $Revision: 1.3 $ $Date: 2006/08/27 14:12:45 $
 * @since 2.3.79
 */
public class SecurityEventTrap implements WikiEventListener
{
    private WikiSecurityEvent m_lastEvent = null;
    private List              m_events    = new ArrayList();

    public void actionPerformed( WikiEvent event )
    {
        if ( event instanceof WikiSecurityEvent )
        {
            m_lastEvent = (WikiSecurityEvent)event;
            m_events.add( event );
        }
        else
        {
            throw new IllegalArgumentException( "Event wasn't a WikiSecurityEvent. Check the unit test code!" );
        }
    }
    
    public WikiSecurityEvent lastEvent()
    {
        return m_lastEvent;
    }
    
    public void clearEvents()
    {
        m_events.clear();
    }
    
    public WikiSecurityEvent[] events() 
    {
        return (WikiSecurityEvent[])m_events.toArray(new WikiSecurityEvent[m_events.size()]);
    }

}
