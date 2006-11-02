package com.ecyrd.jspwiki.auth.login;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;

import org.apache.log4j.Logger;

import com.ecyrd.jspwiki.auth.NoSuchPrincipalException;
import com.ecyrd.jspwiki.auth.WikiPrincipal;
import com.ecyrd.jspwiki.auth.authorize.Role;
import com.ecyrd.jspwiki.auth.user.UserDatabase;
import com.ecyrd.jspwiki.auth.user.UserProfile;

/**
 * <p>
 * Logs in a user based on a username, password, and static password file
 * location. This module must be used with a CallbackHandler (such as
 * {@link WikiCallbackHandler}) that supports the following Callback types:
 * </p>
 * <ol>
 * <li>{@link javax.security.auth.callback.NameCallback}- supplies the
 * username</li>
 * <li>{@link javax.security.auth.callback.PasswordCallback}- supplies the
 * password</li>
 * <li>{@link com.ecyrd.jspwiki.auth.login.UserDatabaseCallback}- supplies the
 * {@link com.ecyrd.jspwiki.auth.user.UserDatabase}</li>
 * </ol>
 * <p>
 * After authentication, a Principals based on the login name will be created 
 * and associated with the Subject.
 * Also, principals {@link com.ecyrd.jspwiki.auth.authorize.Role#ALL} and
 * {@link com.ecyrd.jspwiki.auth.authorize.Role#AUTHENTICATED} will be added to
 * the Subject's principal set.
 * </p>
 * @author Andrew Jaquith
 * @version $Revision: 1.6 $ $Date: 2006/10/01 16:10:28 $
 * @since 2.3
 */
public class UserDatabaseLoginModule extends AbstractLoginModule
{

    protected static final Logger log = Logger.getLogger( UserDatabaseLoginModule.class );
    
    /**
     * @see javax.security.auth.spi.LoginModule#login()
     */
    public boolean login() throws LoginException
    {
        UserDatabaseCallback ucb = new UserDatabaseCallback();
        NameCallback ncb = new NameCallback( "User name" );
        PasswordCallback pcb = new PasswordCallback( "Password", false );
        Callback[] callbacks = new Callback[]
        { ucb, ncb, pcb };
        try
        {
            m_handler.handle( callbacks );
            UserDatabase db = ucb.getUserDatabase();
            String username = ncb.getName();
            String password = new String( pcb.getPassword() );

            // Look up the user and compare the password hash
            if ( db == null )
            {
                throw new FailedLoginException( "No user database: check the callback handler code!" );
            }
            UserProfile profile = db.findByLoginName( username );
            String storedPassword = profile.getPassword();
            if ( storedPassword != null && db.validatePassword( username, password ) )
            {
                if ( log.isDebugEnabled() )
                {
                    log.debug( "Logged in loginName=" + username );
                    log.debug( "Added Principals Role.AUTHENTICATED,Role.ALL" );
                }
                
                // If login succeeds, commit these principals/roles
                m_principals.add( new PrincipalWrapper( new WikiPrincipal( username,  WikiPrincipal.LOGIN_NAME ) ) );
                m_principals.add( Role.AUTHENTICATED );
                m_principals.add( Role.ALL );
                
                // If login succeeds, overwrite these principals/roles
                m_principalsToOverwrite.add( WikiPrincipal.GUEST );
                m_principalsToOverwrite.add( Role.ANONYMOUS );
                m_principalsToOverwrite.add( Role.ASSERTED );
                
                // If login fails, remove these roles
                m_principalsToRemove.add( Role.AUTHENTICATED );
                
                return true;
            }
            throw new FailedLoginException( "The username or password is incorrect." );
        }
        catch( IOException e )
        {
            String message = "IO exception; disallowing login.";
            log.error( message, e );
            throw new LoginException( message );
        }
        catch( UnsupportedCallbackException e )
        {
            String message = "Unable to handle callback; disallowing login.";
            log.error( message, e );
            throw new LoginException( message );
        }
        catch( NoSuchPrincipalException e )
        {
            throw new FailedLoginException( "The username or password is incorrect." );
        }
    }

}