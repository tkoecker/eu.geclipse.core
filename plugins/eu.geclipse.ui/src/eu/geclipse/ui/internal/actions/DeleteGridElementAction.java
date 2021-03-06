/******************************************************************************
 * Copyright (c) 2008 g-Eclipse consortium 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Initial development of the original code was made for
 * project g-Eclipse founded by European Union
 * project number: FP6-IST-034327  http://www.geclipse.eu/
 *
 * Contributor(s):
 *     Mariusz Wojtysiak - initial API and implementation
 *     David Johnson (UoR) - added support for deleting JSDLs linked to 
 *                           workflows
 *****************************************************************************/
package eu.geclipse.ui.internal.actions;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.DeleteResourceAction;
import org.eclipse.ui.actions.SelectionListenerAction;

import eu.geclipse.core.filesystem.GEclipseFileSystem;
import eu.geclipse.core.model.GridModel;
import eu.geclipse.core.model.IGridConnectionElement;
import eu.geclipse.core.model.IGridContainer;
import eu.geclipse.core.model.IGridElement;
import eu.geclipse.core.model.IGridJob;
import eu.geclipse.core.model.IGridJobDescription;
import eu.geclipse.core.reporting.IProblem;
import eu.geclipse.core.reporting.ISolution;
import eu.geclipse.core.reporting.ProblemException;
import eu.geclipse.ui.dialogs.ProblemDialog;
import eu.geclipse.ui.internal.Activator;
import eu.geclipse.workflow.IGridWorkflowDescription;
import eu.geclipse.workflow.IGridWorkflowJobDescription;


/**
 *
 */
public class DeleteGridElementAction extends SelectionListenerAction {
  Shell shell;
  private DeleteResourceAction eclipseAction;

  protected DeleteGridElementAction( final Shell shell ) {
    super( Messages.getString("DeleteGridElementAction.actionNameDelete") ); //$NON-NLS-1$
    
    this.shell = shell;
    this.eclipseAction = new DeleteResourceAction( new IShellProvider() {
      public Shell getShell() {
        return shell;
      }
    } );
    
    ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
    ImageDescriptor deleteImage 
        = sharedImages.getImageDescriptor( ISharedImages.IMG_TOOL_DELETE );
    setImageDescriptor( deleteImage );
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.action.Action#run()
   */
  @Override
  public void run() {
    List<IGridJob> selectedJobs = new ArrayList<IGridJob>( getSelectedResources().size() );
    List<IGridJobDescription> selectedWorkflowJobDescriptions = new ArrayList<IGridJobDescription>( getSelectedResources().size() );
    List<IResource> selectedResources = new ArrayList<IResource>( getSelectedResources().size() );
    dispatchSelectedElements( selectedJobs, selectedWorkflowJobDescriptions, selectedResources );
    deactivateConnections( selectedResources.toArray( new IResource[ selectedResources.size() ] ) );
    if( !selectedJobs.isEmpty() ) {
      deleteJobs( selectedJobs );
    }
    if( !selectedResources.isEmpty() ) {
      deleteOtherResources( selectedResources );
    }  
    if (!selectedWorkflowJobDescriptions.isEmpty()) {
      deleteWorkflowJobDescriptions( selectedWorkflowJobDescriptions );
    }  
  }
  
  @Override
  protected boolean updateSelection( final IStructuredSelection selection ) {
    return ! getSelectedResources().isEmpty();
  }
  
  private void deactivateConnections( final IResource[] resources ) {
    
    if ( resources != null ) {
    
      for ( IResource resource : resources ) {
        
        IGridElement element = GridModel.getRoot().findElement( resource );
        
        if ( element instanceof IGridConnectionElement ) {
          
          try {
            
            IGridConnectionElement connection = ( IGridConnectionElement ) element;
            IFileStore fileStore = connection.getConnectionFileStore();
          
            // Deactivate the connection before deleting it in order to avoid
            // info or child fetching
            GEclipseFileSystem.setFileStoreActive( fileStore, false, false, false );
            
            // Refresh the resource tree in order to remove all cached child
            // resources that would also cause info fetching
            resource.refreshLocal( IResource.DEPTH_INFINITE, null );
            
          } catch( CoreException cExc ) {
            Activator.logException( cExc );
          }
          
        }
        
        if ( resource instanceof IContainer ) {
          try {
            IResource[] members = ( ( IContainer ) resource ).members();
            deactivateConnections( members );
          } catch ( CoreException cExc ) {
            Activator.logException( cExc );
          }
        }
        
      }
      
    }
    
  }
  
  private void dispatchSelectedElements( final List<IGridJob> selectedJobs, final List<IGridJobDescription> selectedWorkflowJobDescriptions, final List<IResource> otherSelectedResources ) {
    
    for( Object obj : getSelectedResources() ) {
      
      if( obj instanceof IResource ) {
      
        IResource resource = (IResource)obj;
        IGridElement element = GridModel.getRoot().findElement( resource );
        
        if( element instanceof IGridJob ) {
          selectedJobs.add( (IGridJob)element );
        }
        
        else if (element instanceof IGridJobDescription ) {
          // find out if the parent resource is a workflow
          IGridContainer parent = element.getParent();
          if (parent instanceof IGridWorkflowDescription) {
            boolean inWorkflow = false;
            List<IGridWorkflowJobDescription> childrenJobs = ( ( IGridWorkflowDescription )parent ).getChildrenJobs();
            for (Iterator<IGridWorkflowJobDescription> i = childrenJobs.iterator(); i.hasNext();) {
              IGridWorkflowJobDescription child = i.next();
              String childUri = child.getDescriptionPath().toString();
              String fullPath = element.getResource().getLocation().toString();
              if ( childUri.equals( fullPath ) ) {
                inWorkflow = true;
              }
            }   
            if (inWorkflow) {
              selectedWorkflowJobDescriptions.add( ( IGridJobDescription )element );
            } else {
              otherSelectedResources.add( resource );
            }
          } else {
            otherSelectedResources.add( resource );
          }
        }
        
        else {
          otherSelectedResources.add( resource );
        }
        
      }
      
    }
  }
  
  private void deleteOtherResources( final List<IResource> selectedResources ) {
    this.eclipseAction.selectionChanged( new StructuredSelection( selectedResources ) );
    this.eclipseAction.run();    
  }
  
  /*
   * At the moment it only works if workflow is not dirty (i.e. workflow must be saved for
   * it to behave correctly). There is not yet any Problem reporting or Progress monitoring.
   */
  private void deleteWorkflowJobDescriptions( final List<IGridJobDescription> selectedJobDescriptions ) {
    MessageDialog dialog = null;
    String dialogMessage = ""; //$NON-NLS-1$
    if (selectedJobDescriptions.size()==1) {
      IGridJobDescription selectedJobDesc = selectedJobDescriptions.get( 0 );
      String jsdl = selectedJobDesc.getResource().getName();
      dialogMessage = String.format( Messages.getString( "DeleteGridElementAction.confirmJobDescDeleteOne" ), jsdl); //$NON-NLS-1$
    } else {      
      String jsdlList = ""; //$NON-NLS-1$
      for (Iterator<IGridJobDescription> i = selectedJobDescriptions.iterator(); i.hasNext();) {
        jsdlList = jsdlList + " " + i.next().getResource().getName(); //$NON-NLS-1$
      }
      dialogMessage = String.format(Messages.getString( "DeleteGridElementAction.confirmJobDescDeleteMany" ), jsdlList); //$NON-NLS-1$
    }
    dialog = new MessageDialog(DeleteGridElementAction.this.shell, 
                               Messages.getString("DeleteGridElementAction.confirmationTitle"), //$NON-NLS-1$
                               null,
                               dialogMessage,
                               MessageDialog.WARNING,
                               new String[] { IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL },
                               0 );

    if ( dialog.open() == Window.OK ) {
      try {
        for (Iterator<IGridJobDescription> i1 = selectedJobDescriptions.iterator(); i1.hasNext();) {
          IResource r = i1.next().getResource();
          r.delete( true, new NullProgressMonitor() ); // TODO add a proper progress monitor to use           
        }
        
      } catch( CoreException e ) {
        // TODO Auto-generated catch block, add proper problem reporting
        e.printStackTrace();
      }
    }
  }
  
  private enum ConfirmChoice {
    /**
     * 
     */
    deleteFromGrid,
    /**
     * 
     */
    deleteOnlyFromWorkspace,
    /**
     * 
     */
    cancel
  }

  private void deleteJobs( final List<IGridJob> selectedJobs ) {
    ConfirmChoice choice = confirmDeleteJobs( selectedJobs );
    
    if( choice != ConfirmChoice.cancel ) {
      DeleteJobsJob job = new DeleteJobsJob( selectedJobs, choice );
      
      job.setUser( true );
      job.schedule();
    }
  }

  private ConfirmChoice confirmDeleteJobs( final List<IGridJob> selectedJobs ) {
    ConfirmChoice choice = ConfirmChoice.cancel;
    String question = null, warning = null;
    
    if( selectedJobs.size() == 1 ) {
      IGridJob job = selectedJobs.iterator().next();
      question = String.format( Messages.getString("DeleteGridElementAction.confirmationOne"), job.getJobName() ); //$NON-NLS-1$
      if( job.getJobStatus().canChange() ) {
        warning = String.format( Messages.getString("DeleteGridElementAction.warningOne"), job.getJobName() ); //$NON-NLS-1$
      }
    } else {
      question = String.format( Messages.getString("DeleteGridElementAction.confirmationMany"), Integer.valueOf( selectedJobs.size() ) ); //$NON-NLS-1$
      
      for( IGridJob job : selectedJobs ) {
        if( job.getJobStatus().canChange() ) {
          warning = Messages.getString("DeleteGridElementAction.warningMany"); //$NON-NLS-1$
          break;
        }
      }
    }
    
    String msg = question;
    
    if( warning != null ) {
      msg += "\n\n" + warning; //$NON-NLS-1$
    }
    
    ConfirmDeleteJobsDialog dialog = new ConfirmDeleteJobsDialog( msg, warning == null ? MessageDialog.QUESTION : MessageDialog.WARNING );
    
    if( dialog.open() == 0 ) {
      if( dialog.isDeleteFromGrid() ) {
        choice = ConfirmChoice.deleteFromGrid;
      } else {
        choice = ConfirmChoice.deleteOnlyFromWorkspace;
      }              
    }
 
    return choice;
  }
  
  private class ConfirmDeleteJobsDialog extends MessageDialog {
    private Button deleteFromGridCheckbox;
    private boolean deleteFromGrid;

    ConfirmDeleteJobsDialog( final String dialogMessage,
                          final int dialogImageType                          
                           )
    {
      super( DeleteGridElementAction.this.shell,
             Messages.getString("DeleteGridElementAction.confirmationTitle"), //$NON-NLS-1$
             null,
             dialogMessage,
             dialogImageType,
             new String[] { IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL },
             0 );
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.dialogs.MessageDialog#createCustomArea(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createCustomArea( final Composite parent ) {
      this.deleteFromGridCheckbox = new Button( parent, SWT.CHECK );
      this.deleteFromGridCheckbox.setText( Messages.getString("DeleteGridElementAction.alsoDeleteFromGrid") ); //$NON-NLS-1$
      this.deleteFromGridCheckbox.setSelection( true );      
      
      return super.createCustomArea( parent );
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.dialogs.Dialog#close()
     */
    @Override
    public boolean close() {
      this.deleteFromGrid = this.deleteFromGridCheckbox.getSelection();
      return super.close();
    }

    boolean isDeleteFromGrid() {
      return this.deleteFromGrid;
    }
    
  }

  
 
  private class DeleteJobsJob extends Job {
    boolean forceDeleteLocal = false;
    private List<IGridJob> selectedJobs;
    private ConfirmChoice userChoice;    

    /**
     * @param selectedJobs
     * @param userChoice
     */
    public DeleteJobsJob( final List<IGridJob> selectedJobs,
                          final ConfirmChoice userChoice )
    {
      super( Messages.getString("DeleteGridElementAction.deleteJobName") ); //$NON-NLS-1$
      this.selectedJobs = selectedJobs;
      this.userChoice = userChoice;
    }


    @Override
    protected IStatus run( final IProgressMonitor monitor ) {
      IStatus status = Status.OK_STATUS;
      SubMonitor submonitor = SubMonitor.convert( monitor );
      submonitor.setWorkRemaining( this.selectedJobs.size() );
      
      try {
        Iterator<IGridJob> iterator = this.selectedJobs.iterator();
        IGridJob job = null;
        while( iterator.hasNext() ) {
          try {            
            testCancel( submonitor );
            job = iterator.next();
            
            deleteJob( submonitor.newChild( 1 ), job );
            iterator.remove();  // if succesful deleted, then don't delete it again during eventually next try
          } catch( ProblemException exception ) {
            addSolutionOnlyLocalDel( exception );
            ProblemDialog.openProblem( DeleteGridElementAction.this.shell,
                                       Messages.getString("DeleteGridElementAction.deleteProblemTitle"), //$NON-NLS-1$
                                       String.format( Messages.getString("DeleteGridElementAction.problemDescription"),  //$NON-NLS-1$
                                                      job != null ? job.getJobName() : "unknown" ), //$NON-NLS-1$
                                       exception );
            break;
          }      
        }
      } finally {
        submonitor.done();
      }
      
      return status;
    }

    private void deleteJob( final SubMonitor monitor, final IGridJob job )
      throws ProblemException
    {
      monitor.setTaskName( String.format( Messages.getString("DeleteGridElementAction.taskNameDeleting"), job.getJobName() ) ); //$NON-NLS-1$
      monitor.setWorkRemaining( this.userChoice == ConfirmChoice.deleteFromGrid ? 3 : 2 );
      
      stopJobStatusUpdater( job, monitor.newChild( 1 ) );
      
      if( this.userChoice == ConfirmChoice.deleteFromGrid ) {
        try {
          job.deleteJob( monitor.newChild( 1 ) );
        } catch( ProblemException exception ) {
          if( this.forceDeleteLocal ) {
            Activator.logException( exception );
          }
          else {
            throw exception;
          }
        }
      }

      try {
        testCancel( monitor );
        job.getResource().delete( true, monitor.newChild( 1 ) );
      } catch( CoreException exception ) {
        throw new ProblemException( "eu.geclipse.problem.deleteGridElementAction.cannotDeleteResource", //$NON-NLS-1$
                                    exception,
                                    Activator.PLUGIN_ID );
      }
    }
    
    private void stopJobStatusUpdater( final IGridJob job, final SubMonitor monitor ) {
      monitor.subTask( Messages.getString("DeleteGridElementAction.taskStoppingUpdater") ); //$NON-NLS-1$
      GridModel.getJobManager().removeJobStatusUpdater( job, true, monitor );      
    }


    private void testCancel( final IProgressMonitor monitor ) {
      if( monitor.isCanceled() ) {
        throw new OperationCanceledException();
      }      
    }

    void addSolutionOnlyLocalDel( final ProblemException exception ) {
      if( this.userChoice == ConfirmChoice.deleteFromGrid
          && !this.forceDeleteLocal ) {
        IProblem problem = exception.getProblem();
        problem.addSolution( new ISolution() {

          public String getDescription() {
            return Messages.getString("DeleteGridElementAction.forceDeleteLocal"); //$NON-NLS-1$
          }

          public String getID() {
            return null;
          }

          public boolean isActive() {
            return true;
          }

          public void solve() throws InvocationTargetException {
            DeleteJobsJob.this.forceDeleteLocal = true;
            DeleteJobsJob.this.schedule();            
          }} );
      }
      
    }
    
  }



  
}
