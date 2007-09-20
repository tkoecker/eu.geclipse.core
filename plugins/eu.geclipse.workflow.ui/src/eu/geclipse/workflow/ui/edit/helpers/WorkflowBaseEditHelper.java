/*******************************************************************************
 * Copyright (c) 2006, 2007 g-Eclipse Consortium 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Initial development of the original code was made for the g-Eclipse project 
 * funded by European Union project number: FP6-IST-034327 
 * http://www.geclipse.eu/
 *  
 * Contributors:
 *     RUR (http://acet.rdg.ac.uk/)
 *     - Ashish Thandavan - initial API and implementation
 ******************************************************************************/
package eu.geclipse.workflow.ui.edit.helpers;

import org.eclipse.gmf.runtime.common.core.command.CompositeCommand;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelper;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyReferenceRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;

/**
 * @generated
 */
public class WorkflowBaseEditHelper extends AbstractEditHelper {

  /**
   * @generated
   */
  public static final String EDIT_POLICY_COMMAND = "edit policy command"; //$NON-NLS-1$

  /**
   * @generated
   */
  @Override
  protected ICommand getInsteadCommand( IEditCommandRequest req ) {
    ICommand epCommand = ( ICommand )req.getParameter( EDIT_POLICY_COMMAND );
    req.setParameter( EDIT_POLICY_COMMAND, null );
    ICommand ehCommand = super.getInsteadCommand( req );
    if( epCommand == null ) {
      return ehCommand;
    }
    if( ehCommand == null ) {
      return epCommand;
    }
    CompositeCommand command = new CompositeCommand( null );
    command.add( epCommand );
    command.add( ehCommand );
    return command;
  }

  /**
   * @generated
   */
  @Override
  protected ICommand getCreateCommand( CreateElementRequest req ) {
    return null;
  }

  /**
   * @generated
   */
  @Override
  protected ICommand getCreateRelationshipCommand( CreateRelationshipRequest req )
  {
    return null;
  }

  /**
   * @generated
   */
  @Override
  protected ICommand getDestroyElementCommand( DestroyElementRequest req ) {
    return null;
  }

  /**
   * @generated
   */
  @Override
  protected ICommand getDestroyReferenceCommand( DestroyReferenceRequest req ) {
    return null;
  }
}