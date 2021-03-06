/*****************************************************************************
 * Copyright (c) 2008 g-Eclipse Consortium 
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
 *    Moritz Post - initial API and implementation
 *****************************************************************************/

package eu.geclipse.aws.ec2.ui.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;

import eu.geclipse.aws.ec2.ui.wizards.CreateKeypairWizard;

/**
 * This action displays the {@link CreateKeypairWizard} to create a new keypair.
 * 
 * @author Moritz Post
 */
public class CreateKeypairAction extends AbstractAWSProjectAction {

  @Override
  public void run( final IAction action ) {
    if( getAwsVo() != null ) {
      CreateKeypairWizard createkeypairWizard = new CreateKeypairWizard( getAwsVo() );

      WizardDialog wizardDialog = new WizardDialog( getWorkbenchPart().getSite()
                                                      .getShell(),
                                                    createkeypairWizard );

      wizardDialog.open();
    }
  }

  public void selectionChanged( final IAction action, final ISelection selection )
  {
    extractAWSVoFromCategory( selection );
  }
}
