/*****************************************************************************
 * Copyright (c) 2006, 2007, 2008 g-Eclipse Consortium 
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
 *    Jie Tao - initial API and implementation
 *****************************************************************************/
 
package eu.geclipse.ui.internal.actions;

import java.util.Iterator;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.actions.SelectionListenerAction;

import eu.geclipse.core.model.IGridApplication;
import eu.geclipse.core.model.IGridComputing;
import eu.geclipse.core.model.IVirtualOrganization;
import eu.geclipse.ui.wizards.deployment.DeploymentWizard;
import eu.geclipse.ui.wizards.deployment.UninstallWizard;


public class UninstallAction extends SelectionListenerAction {

  /**
   * The workbench site this action belongs to.
   */
  private IWorkbenchSite site;

  
  /**the deployment action
   * @param site
   */
  public UninstallAction( final IWorkbenchSite site ) {
    super( Messages.getString( "UninstallAction.action_name" ) ); //$NON-NLS-1$
    this.setEnabled( false );
    this.site = site;
  }

  @Override
  public void run() {
    //Shell shell = this.site.getWorkbenchWindow().getShell();
    Shell shell = this.site.getShell();
    UninstallWizard wizard = new UninstallWizard( this.getStructuredSelection() );
    WizardDialog dialog = new WizardDialog( shell, wizard );
    dialog.setBlockOnOpen( false );
    dialog.open();
  }

  @Override
  protected boolean updateSelection( final IStructuredSelection structuredSelection ) {
    this.setEnabled( true );
    boolean enabled = super.updateSelection( structuredSelection );
    boolean isGridApplicationFlag = false;
    
 
    Iterator< ? > iter = structuredSelection.iterator();
    while ( iter.hasNext() && enabled ) {
      Object element = iter.next();
    /* if( element instanceof IGridComputing)
        isGridApplicationFlag = true;
      if( element instanceof IVirtualOrganization)
        isGridApplicationFlag = true;*/
      if( element instanceof IGridApplication)
        isGridApplicationFlag = true;
    }
      
    return enabled && isGridApplicationFlag;
  }

}