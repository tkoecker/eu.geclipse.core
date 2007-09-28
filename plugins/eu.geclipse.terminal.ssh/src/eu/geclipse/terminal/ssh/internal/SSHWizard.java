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
 *    Thomas Koeckerbauer GUP, JKU - initial API and implementation
 *****************************************************************************/

package eu.geclipse.terminal.ssh.internal;

import java.net.URL;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.Wizard;
import eu.geclipse.terminal.ITerminalView;
import eu.geclipse.ui.wizards.portforward.PortForwardOptionsWizardPage;
import eu.geclipse.ui.wizards.wizardselection.IInitalizableWizard;

/**
 * A "new terminal wizard" for creating SSH terminal sessions.
 */
public class SSHWizard extends Wizard implements IInitalizableWizard {
  private SSHConnectionWizardPage mainPage;
  private SSHConnectionInfo info = null;
  private ITerminalView termView;
  private PortForwardOptionsWizardPage portForwardPage;
  
  @Override
  public boolean performFinish() {
    this.info = this.mainPage.getConnectionInfo();
    SshShell sshShell = new SshShell();
    sshShell.createTerminal( this.termView, this.mainPage.getConnectionInfo(),
                             this.portForwardPage.getForwards() );
    return true;
  }

  @Override
  public void addPages() {
    this.mainPage = new SSHConnectionWizardPage( Messages.getString( "SSHWizard.ssh" ) ); //$NON-NLS-1$
    this.portForwardPage = new PortForwardOptionsWizardPage( Messages.getString( "SSHWizard.ssh" ) ); //$NON-NLS-1$
    addPage( this.mainPage );
    addPage( this.portForwardPage );
  }
  
  @Override
  public String getWindowTitle() {
    return Messages.getString( "SSHWizard.ssh" ); //$NON-NLS-1$
  }
  
  @Override
  public String toString() {
    return Messages.getString( "SSHWizard.ssh" ); //$NON-NLS-1$
  }
  
  SSHConnectionInfo getConnectionInfo() {
    return this.info;  
  }

  public boolean init( final Object data ) {
    boolean result = false;
    if ( data instanceof ITerminalView ) {
      this.termView = ( ITerminalView ) data;
      URL imgUrl = Activator.getDefault().getBundle().getEntry( "icons/wizban/newconn_wiz.gif" ); //$NON-NLS-1$
      setDefaultPageImageDescriptor( ImageDescriptor.createFromURL( imgUrl ) );
      result = true;
    }
    return result;
  }
}
