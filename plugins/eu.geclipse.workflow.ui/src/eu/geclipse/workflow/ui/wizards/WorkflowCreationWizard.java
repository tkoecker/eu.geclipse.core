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
package eu.geclipse.workflow.ui.wizards;

import java.lang.reflect.InvocationTargetException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

import eu.geclipse.workflow.ui.editor.WorkflowDiagramEditorUtil;
import eu.geclipse.workflow.ui.internal.WorkflowDiagramEditorPlugin;

/**
 * @generated
 */
public class WorkflowCreationWizard extends Wizard implements INewWizard {

  /**
   * @generated
   */
  private IWorkbench workbench;
  /**
   * @generated
   */
  protected IStructuredSelection selection;
  /**
   * @generated
   */
  protected WorkflowCreationWizardPage diagramModelFilePage;
  /**
   * @generated
   */
  protected Resource diagram;
  /**
   * @generated
   */
  private boolean openNewlyCreatedDiagramEditor = true;

  /**
   * @generated
   */
  public IWorkbench getWorkbench() {
    return workbench;
  }

  /**
   * @generated
   */
  public IStructuredSelection getSelection() {
    return selection;
  }

  /**
   * @generated
   */
  public final Resource getDiagram() {
    return diagram;
  }

  /**
   * @generated
   */
  public final boolean isOpenNewlyCreatedDiagramEditor() {
    return openNewlyCreatedDiagramEditor;
  }

  /**
   * @generated
   */
  public void setOpenNewlyCreatedDiagramEditor( boolean openNewlyCreatedDiagramEditor )
  {
    this.openNewlyCreatedDiagramEditor = openNewlyCreatedDiagramEditor;
  }

  /**
   * @generated
   */
  public void init( IWorkbench workbench, IStructuredSelection selection ) {
    this.workbench = workbench;
    this.selection = selection;
    setWindowTitle( "New Workflow Diagram" ); //$NON-NLS-1$
    setDefaultPageImageDescriptor( WorkflowDiagramEditorPlugin.getBundledImageDescriptor( "icons/wizban/NewWorkflowWizard.gif" ) ); //$NON-NLS-1$
    setNeedsProgressMonitor( true );
  }

  /**
   * @generated
   */
  public void addPages() {
    diagramModelFilePage = new WorkflowCreationWizardPage( "DiagramModelFile", getSelection(), "workflow" ); //$NON-NLS-1$ //$NON-NLS-2$
    diagramModelFilePage.setTitle( "Create Workflow Diagram" ); //$NON-NLS-1$
    diagramModelFilePage.setDescription( "Select file that will contain diagram and domain models." ); //$NON-NLS-1$
    addPage( diagramModelFilePage );
  }

  /**
   * @generated
   */
  public boolean performFinish() {
    IRunnableWithProgress op = new WorkspaceModifyOperation( null ) {

      protected void execute( IProgressMonitor monitor )
        throws CoreException, InterruptedException
      {
        diagram = WorkflowDiagramEditorUtil.createDiagram( diagramModelFilePage.getURI(),
                                                           monitor );
        if( isOpenNewlyCreatedDiagramEditor() && diagram != null ) {
          try {
            WorkflowDiagramEditorUtil.openDiagram( diagram );
          } catch( PartInitException e ) {
            ErrorDialog.openError( getContainer().getShell(),
                                   "Error opening diagram editor", //$NON-NLS-1$
                                   null,
                                   e.getStatus() );
          }
        }
      }
    };
    try {
      getContainer().run( false, true, op );
    } catch( InterruptedException e ) {
      return false;
    } catch( InvocationTargetException e ) {
      if( e.getTargetException() instanceof CoreException ) {
        ErrorDialog.openError( getContainer().getShell(),
                               "Creation Problems", //$NON-NLS-1$
                               null,
                               ( ( CoreException )e.getTargetException() ).getStatus() );
      } else {
        WorkflowDiagramEditorPlugin.getInstance()
          .logError( "Error creating diagram", e.getTargetException() ); //$NON-NLS-1$
      }
      return false;
    }
    return diagram != null;
  }
}