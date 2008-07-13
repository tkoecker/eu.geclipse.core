package eu.geclipse.ui.internal.actions;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileInfo;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import eu.geclipse.core.filesystem.GEclipseURI;
import eu.geclipse.core.model.IGridConnection;
import eu.geclipse.core.model.IGridContainer;
import eu.geclipse.core.model.IGridProject;
import eu.geclipse.core.model.IMountable;
import eu.geclipse.core.model.IMountable.MountPoint;
import eu.geclipse.core.model.IMountable.MountPointID;
import eu.geclipse.ui.internal.Activator;
import eu.geclipse.ui.wizards.ConnectionWizard;

public class MountAction extends Action {

  private IMountable[] mountables;
  
  private MountPointID mountID;
  
  private Shell shell;
  
  private boolean mountAs;
  
  /**
   * Construct a new <code>MountAction</code> for the specified
   * {@link IMountable}s using the specified mount ID.
   * 
   * @param shell A shell used to report errors.
   * @param mountables The {@link IMountable}s that should be mounted.
   * @param mountID T
   */
  protected MountAction( final Shell shell,
                         final IMountable[] mountables,
                         final MountPointID mountID,
                         final boolean mountAs ) {
    super( mountID.getUID() );
    this.shell = shell;
    this.mountables = mountables;
    this.mountID = mountID;
    this.mountAs = mountAs;
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.jface.action.Action#run()
   */
  @Override
  public void run() {
    Job mountJob = new Job( "Storage mount job" ) {
      @Override
      protected IStatus run( final IProgressMonitor monitor ) {
        return mountOperation( monitor );
      }
    };
    mountJob.setUser( true );
    mountJob.schedule();
  }
  
  protected IStatus mountOperation( final IProgressMonitor monitor ) {
    
    List< IStatus > errors = new ArrayList< IStatus >();
    
    IProgressMonitor lMonitor
      = monitor == null
      ? new NullProgressMonitor()
      : monitor;
      
    lMonitor.beginTask( "Creating mounts", this.mountables.length );
    
    for ( IMountable mountable : this.mountables ) {
      
      try {
        
        if ( lMonitor.isCanceled() ) {
          break;
        }
        
        lMonitor.subTask( "Mounting " + mountable.getName() );
        createMount( mountable );
        lMonitor.worked( 1 );
        
      } catch ( CoreException cExc ) {
        IStatus status = new Status(
            IStatus.ERROR,
            Activator.PLUGIN_ID,
            String.format( "Failed to mount %s", mountable.getName() ),
            cExc
        );
        errors.add( status );
      }
      
    }
    
    lMonitor.done();
    
    IStatus result = Status.OK_STATUS;
    
    if ( ! errors.isEmpty() ) {
      IStatus[] errorArray = errors.toArray( new IStatus[ errors.size() ] );
      result = new MultiStatus(
          Activator.PLUGIN_ID,
          IStatus.ERROR,
          errorArray,
          "Mount failed",
          null
      );
    }
    
    return result;
    
  }
  
  /**
   * Create a mount file for the specified {@link IMountable}. The mount
   * file is created in the connections folder of the selected project.
   * 
   * @param mountable The {@link IMountable} to be mounted.
   * @throws CoreException If the mount file count not be created.
   */
  protected void createMount( final IMountable mountable )
      throws CoreException {
    
    IGridProject project = mountable.getProject();
    IGridContainer mountFolder = project.getProjectFolder( IGridConnection.class );
    
    if ( mountFolder != null ) {
      
      MountPoint mountPoint = mountable.getMountPoint( this.mountID );
      String mountName = mountPoint.getName();
      
      if ( mountPoint != null ) {
        
        URI uri = mountPoint.getURI();
        
        if ( this.mountAs ) {
          ConnectionWizard wizard = new ConnectionWizard( uri, mountName );
          wizard.init( null, new StructuredSelection( mountFolder.getResource() ) );
          final WizardDialog dialog = new WizardDialog( this.shell, wizard );
          this.shell.getDisplay().syncExec( new Runnable() {
            public void run() {
              dialog.open();
            }
          } );
        }
        
        else {
        
          IContainer mountContainer = ( IContainer ) mountFolder.getResource();
          IPath path = new Path( mountName );
          
          GEclipseURI geclURI = new GEclipseURI( uri );
          URI masterURI = geclURI.toMasterURI();
          IFileStore fileStore = EFS.getStore( masterURI );
          IFileInfo fileInfo = fileStore.fetchInfo();
          
          if ( fileInfo.isDirectory() ) {
            IFolder folder = mountContainer.getFolder( path );
            folder.createLink( masterURI, IResource.ALLOW_MISSING_LOCAL, null );
          } else {
            IFile file = mountContainer.getFile( path );
            file.createLink( masterURI, IResource.ALLOW_MISSING_LOCAL, null );
          }
          
        }
        
      }
      
    }
    
  }
  
}
