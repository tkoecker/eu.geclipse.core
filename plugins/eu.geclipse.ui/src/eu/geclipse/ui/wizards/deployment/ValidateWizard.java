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
 *    Jie Tao -- initial API and implementation
 *****************************************************************************/

package eu.geclipse.ui.wizards.deployment;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

import eu.geclipse.core.model.IGridApplication;
import eu.geclipse.core.model.IGridElement;
import eu.geclipse.core.model.IVirtualOrganization;
import eu.geclipse.ui.internal.Activator;



/**the wizard for get information of uninstall a deployed software
 * @author tao-j
 *
 */
public class ValidateWizard extends Wizard {

  private ValidateWizardPage firstpage;
  private IStructuredSelection selection;
  /**
   * @param structuredSelection
   */
  public ValidateWizard( final IStructuredSelection structuredSelection ) {
    super( );
    this.setWindowTitle( Messages.getString( "Validate.wizard_titel" ) ); //$NON-NLS-1$
    URL imgUrl = Activator.getDefault().getBundle().getEntry( "icons/wizban/newtoken_wiz.gif" ); //$NON-NLS-1$
    this.setDefaultPageImageDescriptor( ImageDescriptor.createFromURL( imgUrl ) );
    this.selection = structuredSelection;
  
  }
 

  @Override
  public void addPages()
  {
    this.firstpage = new ValidateWizardPage("Script selection"); //$NON-NLS-1$
    addPage( this.firstpage );
  }
  
  @Override
  public boolean canFinish() {
    boolean finished = false;
    IWizardPage currentPage = this.getContainer().getCurrentPage();
    if ( currentPage == this.firstpage ) {
      finished = true;
    }
    return finished;
  }
  
  @Override
  public boolean performFinish() {

   IGridElement selected = ( IGridElement ) this.selection.getFirstElement();
   IVirtualOrganization vo = selected.getProject().getVO();
   Iterator< ? > iter = this.selection.iterator();
   List< IGridApplication > validateList = new ArrayList< IGridApplication >();
   while ( iter.hasNext() ) {
     Object selectelement = iter.next();
     IGridApplication application = (IGridApplication) selectelement;
     validateList.add( application );
     application.setScript( this.firstpage.getScript() );
   }
   IGridApplication[] target = validateList.toArray( new IGridApplication[ validateList.size() ] );
    ValidateJob job = new ValidateJob( "Validate deployed software", target, vo ); //$NON-NLS-1$
    job.setUser( true );
    job.schedule();
    return true;
  }
}
