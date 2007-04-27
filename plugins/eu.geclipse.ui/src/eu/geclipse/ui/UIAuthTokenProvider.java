/*****************************************************************************
 * Copyright (c) 2006, 2007 g-Eclipse Consortium 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Initial development of the original code was made for the
 * g-Eclipse project founded by European Union
 * project number: FP6-IST-034327  http://www.geclipse.eu/
 *
 * Contributors:
 *    Mathias Stuempert - initial API and implementation
 *****************************************************************************/

package eu.geclipse.ui;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.cheatsheets.CheatSheetListener;
import org.eclipse.ui.cheatsheets.ICheatSheetEvent;
import org.eclipse.ui.cheatsheets.ICheatSheetManager;
import eu.geclipse.core.auth.AuthenticationException;
import eu.geclipse.core.auth.CoreAuthTokenProvider;
import eu.geclipse.core.auth.IAuthTokenProvider;
import eu.geclipse.core.auth.IAuthenticationToken;
import eu.geclipse.core.auth.IAuthenticationTokenDescription;
import eu.geclipse.ui.internal.Activator;
import eu.geclipse.ui.wizards.wizardselection.ExtPointWizardSelectionListPage;

/**
 * The <code>UIAuthTokenProvider</auth> is the main point were Plugins should request their
 * authentication tokens. It should be used instead of the <code>AuthenticationTokenManager</code>
 * whenever possible. It provides methods to request any token or tokens of a special type. It also
 * takes responsibility for the user interactions with respect to new token wizards, question
 * dialogs and error dialogs. Therefore it makes the request for a new token very easy.
 */
public class UIAuthTokenProvider extends CheatSheetListener implements IAuthTokenProvider {

  /**
   * Runnable implementation that is executed in the UI thread in
   * order to retrieve an existing token or to create a new token.
   * This has to run in the UI thread in order to allow the popup
   * of error dialogs or token wizards. 
   */
  private class Runner implements Runnable {
    
    /**
     * The token description for which to retrieve a token.
     */
    IAuthenticationTokenDescription description;
    
    /**
     * The token that was found or created.
     */
    IAuthenticationToken token;
    
    /**
     * Construct a new Runner used to find a token for the specified
     * token description.
     * 
     * @param description The {@link IAuthenticationTokenDescription} for
     * which to find an {@link IAuthenticationToken}.
     */
    public Runner( final IAuthenticationTokenDescription description ) {
      this.description = description;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run() {
      CoreAuthTokenProvider cProvider = new CoreAuthTokenProvider();
      this.token = cProvider.requestToken( this.description );
      if( this.token == null ) {
        // No token could be found, so create one
        boolean result = MessageDialog.openQuestion( UIAuthTokenProvider.this.shell,
                                                     Messages.getString( "UIAuthTokenProvider.req_token_title" ), //$NON-NLS-1$
                                                     Messages.getString( "UIAuthTokenProvider.new_token_question" ) ); //$NON-NLS-1$
        if( result ) {
          String tokenWizardId = this.description.getWizardId();
          if ( showNewTokenWizard( tokenWizardId ) ) {
            this.token = cProvider.requestToken( this.description );
          }
        }
      }
      if( this.token != null ) {
        Throwable thr = null;
        // Check if the token is both valid and active
        try {
          if( !this.token.isValid() ) {
            validateToken( this.token );
          }
          if( !this.token.isActive() ) {
            activateToken( this.token );
          }
        } catch( InvocationTargetException itExc ) {
          thr = itExc.getCause();
          if( thr == null ) {
            thr = itExc;
          }
        } catch( InterruptedException intExc ) {
          thr = intExc;
        }
        if( thr != null ) {
          IStatus status = new Status( IStatus.ERROR,
                                       eu.geclipse.ui.internal.Activator.PLUGIN_ID,
                                       IStatus.OK,
                                       thr.getLocalizedMessage(),
                                       thr );
          ErrorDialog.openError( UIAuthTokenProvider.this.shell,
                                 Messages.getString( "UIAuthTokenProvider.auth_token_error" ), //$NON-NLS-1$
                                 Messages.getString( "UIAuthTokenProvider.token_activation_error" ), //$NON-NLS-1$
                                 status );
          eu.geclipse.ui.internal.Activator.logStatus( status );
        }
      }
    }
    
  }
  
  /**
   * A manager used for cheat sheet automation.
   */
  protected static ICheatSheetManager cheatSheetManager;
  
  /**
   * Key for the auth token wizard.
   */
  private static final String WIZARD_PAGE_NAME = "pagename"; //$NON-NLS-1$

  /**
   * The <code>Shell</code> that is used to create dialogs, error dialogs...
   */
  protected Shell shell;
  
  /**
   * The display used to synchronously run the token creation process.
   */
  protected Display display;
  
  /**
   * Standard constructor for the <code>UIAuthTokenProvider</code>.
   */
  public UIAuthTokenProvider() {
    this( null );
  }

  /**
   * Construct a new <code>UIAuthTokenProvider</code>.
   * 
   * @param shell The shell that is used to create wizards and dialogs.
   */
  public UIAuthTokenProvider( final Shell shell ) {
    IWorkbench workbench = PlatformUI.getWorkbench();
    this.display = workbench.getDisplay();
    this.shell = shell;
    if ( shell == null ) {
      IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
      if ( window != null ) { 
        this.shell = window.getShell();
      }
    }
  }

  /**
   * Request any authentication token. This is the same as
   * <code>requestToken(null)</code>.
   * 
   * @return Any token that could be found.
   * @see #requestToken(IAuthenticationTokenDescription)
   */
  public IAuthenticationToken requestToken() {
    return requestToken( null );
  }

  /**
   * Request an authentication token that fits the specified description. At the
   * moment the token has only to fit the supported type of the description. So
   * if a <code>GridProxy</code> is requested an empty
   * <code>GridProxyDescription</code> should be passed to this method.
   * <p>
   * Internally this method queries the <code>AuthenticationTokenManager</code>.
   * The first step therefore is always to look for the default token. If the
   * default token is of the specified type it is returned. The second step is
   * to look for all currently registered tokens. If one of these fits it is
   * returned. If no token could be found up to here the new token wizard is
   * launched to create a new token that fits the description. The newly created
   * token is afterwards added to the token managers managed tokens.
   * <p>
   * If the found token is not the default token a message box will pop up to
   * ask if the token should be set as default token. If the found token is not
   * valid or not active it is validated and respectively activated. If
   * something wents wrong during this process an error message will pop up.
   * 
   * @param description A <code>IAuthenticationTokenDescription</code> that
   *          describes the token that is requested.
   * @return A token of the type that is described by the specified description
   *         or null if no such token could be found or created.
   */
  public IAuthenticationToken requestToken( final IAuthenticationTokenDescription description ) {
    Runner runner = new Runner( description );
    this.display.syncExec( runner );
    return runner.token;
  }
  
  /**
   * Show the new token wizard. If the specified description is not null the wizard will
   * be started with the wizard page belonging to the specified description. Otherwise it
   * will be started with the token type page as starting page where the user can choose
   * the type of the he wants to create.
   * 
   * @param tokenWizardId The ID of the token type that should be created or null.
   * @return True if the token dialog was closed with status {@link Window#OK}.
   */
  public boolean showNewTokenWizard( final String tokenWizardId ) {
    URL imgUrl = Activator.getDefault().getBundle().getEntry( "icons/wizban/newtoken_wiz.gif" ); //$NON-NLS-1$

    Wizard wizard =  new Wizard() {
      @Override
      public boolean performFinish() {
        return false;
      }
  
      @Override
      public void addPages() {
        ExtPointWizardSelectionListPage page = new ExtPointWizardSelectionListPage(
            WIZARD_PAGE_NAME,
            eu.geclipse.core.Extensions.AUTH_TOKEN_MANAGEMENT_POINT,
            Messages.getString( "UIAuthTokenProvider.wizard_first_page_title" ), //$NON-NLS-1$
            Messages.getString( "UIAuthTokenProvider.wizard_first_page_description" ) ); //$NON-NLS-1$
        page.setPreselectedId( tokenWizardId, true );
        page.setCheatSheetManager(cheatSheetManager);
        addPage( page );
      }
    };

    wizard.setNeedsProgressMonitor( true );
    wizard.setForcePreviousAndNextButtons( true );
    wizard.setWindowTitle( Messages.getString("UIAuthTokenProvider.wizard_title") ); //$NON-NLS-1$
    wizard.setDefaultPageImageDescriptor( ImageDescriptor.createFromURL( imgUrl ) );
    WizardDialog dialog = new WizardDialog( this.shell, wizard );
    return dialog.open() == Window.OK;
  }

  /**
   * Validates the specified token. This method does the validation in a
   * separate thread and provides a progress monitor for the validation process.
   * 
   * @param token The token to be validated.
   * @throws InvocationTargetException Thrown if an exception occures in the
   *           validation thread.
   * @throws InterruptedException Thrown if the validation thread is
   *           interrupted.
   */
  protected void validateToken( final IAuthenticationToken token )
    throws InvocationTargetException, InterruptedException
  {
    ProgressMonitorDialog progMon = new ProgressMonitorDialog( this.shell );
    progMon.run( false, false, new IRunnableWithProgress() {

      public void run( final IProgressMonitor monitor )
        throws InvocationTargetException, InterruptedException
      {
        try {
          token.validate( monitor );
        } catch( AuthenticationException authExc ) {
          throw new InvocationTargetException( authExc );
        }
      }
    } );
  }

  /**
   * Activate the specified token. This method does the activation in a separate
   * thread and provides a progress monitor for the activation process.
   * 
   * @param token The token to be activated.
   * @throws InvocationTargetException Thrown if an exception occures in the
   *           activation thread.
   * @throws InterruptedException Thrown if the activation thread is
   *           interrupted.
   */
  protected void activateToken( final IAuthenticationToken token )
    throws InvocationTargetException, InterruptedException
  {
    ProgressMonitorDialog progMon = new ProgressMonitorDialog( this.shell );
    progMon.run( false, false, new IRunnableWithProgress() {

      public void run( final IProgressMonitor monitor )
        throws InvocationTargetException, InterruptedException
      {
        try {
          token.setActive( true, monitor );
        } catch( AuthenticationException authExc ) {
          throw new InvocationTargetException( authExc );
        }
      }
    } );
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.cheatsheets.CheatSheetListener#cheatSheetEvent(org.eclipse.ui.cheatsheets.ICheatSheetEvent)
   */
  @Override
  public void cheatSheetEvent( final ICheatSheetEvent event ) {
    cheatSheetManager = event.getCheatSheetManager();
    if ( cheatSheetManager.getData( "var1" ) == null ) { //$NON-NLS-1$
      cheatSheetManager.setData( "var1", "null" ); //$NON-NLS-1$ //$NON-NLS-2$
      cheatSheetManager.setData( "var2", "null" ); //$NON-NLS-1$ //$NON-NLS-2$
    }
  }

}
