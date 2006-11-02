package com.ecyrd.jspwiki.modules;

import com.ecyrd.jspwiki.Release;
import com.ecyrd.jspwiki.WikiEngine;

/**
 *  Superclass for all JSPWiki managers for modules (plugins, etc).
 *  @author jalkanen
 */
public abstract class ModuleManager
{

    /**
     * Location of the property-files of plugins.
     *  (Each plugin should include this property-file in its jar-file)
     */
    public static final String PLUGIN_RESOURCE_LOCATION = "ini/jspwiki_module.xml";
    
    public static final String LOAD_INCOMPATIBLE_MODULES = "jspwiki.loadIncompatibleModules";
    
    protected WikiEngine m_engine;
    
    private boolean m_loadIncompatibleModules = false;
    
    public ModuleManager( WikiEngine engine )
    {
        m_engine = engine;
    }
    
    public boolean checkCompatibility( WikiModuleInfo info )
    {
        if( !m_loadIncompatibleModules )
        {
            String minVersion = info.getMinVersion();
            String maxVersion = info.getMaxVersion();
            
            return Release.isNewerOrEqual( minVersion ) && Release.isOlderOrEqual( maxVersion );
        }
        
        return true;
    }
}
