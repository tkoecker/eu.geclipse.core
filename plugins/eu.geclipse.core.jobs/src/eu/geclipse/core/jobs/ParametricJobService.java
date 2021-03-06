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
 *     
 *****************************************************************************/
package eu.geclipse.core.jobs;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubMonitor;

import eu.geclipse.core.jobs.internal.Activator;
import eu.geclipse.core.jobs.internal.ParametricJobID;
import eu.geclipse.core.jobs.internal.ParametricJobStatus;
import eu.geclipse.core.model.GridModel;
import eu.geclipse.core.model.IGridContainer;
import eu.geclipse.core.model.IGridElement;
import eu.geclipse.core.model.IGridJob;
import eu.geclipse.core.model.IGridJobDescription;
import eu.geclipse.core.model.IGridJobID;
import eu.geclipse.core.model.IGridJobService;
import eu.geclipse.core.model.IGridJobStatus;
import eu.geclipse.core.model.IGridProject;
import eu.geclipse.core.model.IVirtualOrganization;
import eu.geclipse.core.reporting.ProblemException;
import eu.geclipse.jsdl.JSDLJobDescription;
import eu.geclipse.jsdl.parametric.IParametricJsdlGenerator;
import eu.geclipse.jsdl.parametric.ParametricGenerationCanceled;
import eu.geclipse.jsdl.parametric.ParametricJsdlException;
import eu.geclipse.jsdl.parametric.ParametricJsdlGeneratorFactory;
import eu.geclipse.jsdl.parametric.eclipse.ParametricJsdlSaver;


/**
 * Job service, which handles parametric jobs. Can: submit parametric JSDL, update param job status, delete job
 */
public class ParametricJobService implements IGridJobService {
  private IGridJobService jobService;   // this service is valid only for job submission and may be null. Other methods delegates call to children of parametric job
  private GridJob job; // parametric job, to which children all calls will be delegated 
  
  /**
   * Constructor used only for job submission, when job is not known yet
   * @param jobService
   */
  public ParametricJobService( final IGridJobService jobService ) {
    this.jobService = jobService;
  }

  /**
   * Constructor used for submitted job, when parametric jobs is created and 
   * all actions are delegated to job children
   * @param jobID
   */
  public ParametricJobService( final ParametricJobID jobID ) {
    this.job = jobID.getJob();
  }

  /* (non-Javadoc)
   * @see eu.geclipse.core.model.IGridJobService#canSubmit(eu.geclipse.core.model.IGridJobDescription)
   */
  public boolean canSubmit( final IGridJobDescription desc ) {
    return this.jobService != null && this.jobService.canSubmit( desc );
  }

  /* (non-Javadoc)
   * @see eu.geclipse.core.model.IGridJobService#deleteJob(eu.geclipse.core.model.IGridJob, org.eclipse.core.runtime.IProgressMonitor)
   */
  public void deleteJob( final IGridJobID dummyJobId, final IVirtualOrganization vo, final IProgressMonitor monitor )
    throws ProblemException
  {
    SubMonitor subMonitor = SubMonitor.convert( monitor );
    List<GridJob> childrenJobs = getChildrenJobs();
    subMonitor.setWorkRemaining( childrenJobs.size() );
    
    for( GridJob gridJob : childrenJobs ) {
      gridJob.deleteJob( subMonitor.newChild( 1 ) );
    }
  }

  /* (non-Javadoc)
   * @see eu.geclipse.core.model.IGridJobService#getJobStatus(eu.geclipse.core.model.IGridJobID, org.eclipse.core.runtime.IProgressMonitor)
   */
  public IGridJobStatus getJobStatus( final IGridJobID id, final IVirtualOrganization vo, final boolean fullStatus, final IProgressMonitor progressMonitor ) throws ProblemException {
    SubMonitor subMonitor = SubMonitor.convert( progressMonitor );
    Set<String> statusNames = new HashSet<String>();
    int statusType = IGridJobStatus.DONE;
    List<GridJob> childrenJobs = getChildrenJobs();
    subMonitor.setWorkRemaining( childrenJobs.size() );
    boolean unkOccured = false, abortedOccured = false, runningOccured = false, waitingOccured = false, submittedOccured = false;
    
    for( GridJob gridJob : childrenJobs ) {
      subMonitor.subTask( String.format( Messages.getString("ParametricJobService.taskNameUpdating"), gridJob.getJobName() ) ); //$NON-NLS-1$
      IGridJobStatus oldStatus = gridJob.getJobStatus();
      IGridJobStatus jobStatus = gridJob.updateJobStatus( subMonitor.newChild( 1 ), fullStatus );
      statusNames.add( jobStatus.getName() );
      
      GridModel.getJobManager().jobStatusChanged( gridJob, oldStatus );
      
      switch( jobStatus.getType() ) {
        case IGridJobStatus.DONE:
          break;
          
        case IGridJobStatus.SUBMITTED:
          submittedOccured = true;
          break;
          
        case IGridJobStatus.ABORTED:
          abortedOccured = true;
          break;
        
        case IGridJobStatus.WAITING:
          waitingOccured = true;
          break;
        case IGridJobStatus.RUNNING:
          runningOccured = true;
          break;
          
        case IGridJobStatus.PURGED:
        case IGridJobStatus.UNKNOWN:
        case IGridJobStatus.UNDEF:
          unkOccured = true;
          break;
      }
    }
 
    if( unkOccured ) {
      statusType = IGridJobStatus.UNKNOWN;
    } else if( abortedOccured ) {
      statusType = IGridJobStatus.ABORTED;
    } else if( submittedOccured ) {
      statusType = IGridJobStatus.SUBMITTED;
    } else if( waitingOccured ) {
      statusType = IGridJobStatus.WAITING;
    } else if( runningOccured ) {
      statusType = IGridJobStatus.RUNNING;
    } else {
      statusType = IGridJobStatus.DONE;
    }    
    
    return new ParametricJobStatus( statusNames.toString(), statusType, childrenJobs );
  }

  private List<GridJob> getChildrenJobs() throws ProblemException {
    IGridElement[] children = this.job.getChildren( new NullProgressMonitor() );
    List<GridJob> childrenJobs = new ArrayList<GridJob>( children.length );
    for( IGridElement gridElement : children ) {
      if( gridElement instanceof GridJob ) {
        childrenJobs.add( ( GridJob )gridElement );
      }
    }
    return childrenJobs;
  }

  /**
   * @param jsdl
   * @param parent
   * @param jobName
   * @return created job
   * @throws ProblemException
   */
  public IGridJob createParamJobStructure( final JSDLJobDescription jsdl,
                                           final IGridContainer parent,
                                           final String jobName )
      throws ProblemException
  {
    ParametricJobID jobId = new ParametricJobID(); 
    GridJobCreator creator = new GridJobCreator();
    creator.canCreate( jsdl );
    return creator.create( parent, jobId, this, jobName );
  }

  /* (non-Javadoc)
   * @see eu.geclipse.core.model.IGridJobService#submitJob(eu.geclipse.core.model.IGridJobDescription, org.eclipse.core.runtime.IProgressMonitor)
   */
  /**
   * Specialized method to submit parametric job
   * @param description
   * @param monitor
   * @param parent
   * @param jobName
   * @return submitted job
   * @throws ProblemException
   */
  public IGridJobID submitJob( final IGridJobDescription description,
                               final SubMonitor monitor, final IGridContainer parent, final String jobName )
    throws ProblemException
  {
    IGridJobID jobId = null;
    SubMonitor subMonitor = SubMonitor.convert( monitor );
    
    Assert.isTrue( description instanceof JSDLJobDescription );
    
    JSDLJobDescription jsdl = ( JSDLJobDescription )description;
    
    if( isResumedSubmition( jsdl ) ) {
      jobId = resumeSubmission( jsdl, subMonitor );
    } else {
      Assert.isTrue( jsdl.isParametric() );      
      subMonitor.setWorkRemaining( 10 );
      subMonitor.subTask( Messages.getString("ParametricJobService.taskNameGeneratingJsdl") ); //$NON-NLS-1$
      IGridJob parametricGridJob = createParamJobStructure( jsdl, parent, jobName );
      IParametricJsdlGenerator generator = ParametricJsdlGeneratorFactory.getGenerator( jsdl.getAsString() );
      IFolder generationTargetfolder = ((IFolder)parametricGridJob.getResource()).getFolder( Messages.getString("ParametricJobService.generatedJsdlFolder") ); //$NON-NLS-1$
      ParametricJsdlSaver saver = new ParametricJsdlSaver( jsdl, generationTargetfolder, subMonitor.newChild( 1 ) );
      try {
        generator.generate( saver );
      } catch( ParametricGenerationCanceled exception ) {
        throw new OperationCanceledException();
      } catch( ParametricJsdlException exception ) {
        throw new ProblemException( "eu.geclipse.core.jobs.problem.generateParamJsdlFailed", exception, Activator.PLUGIN_ID ); //$NON-NLS-1$
      }
      List<JSDLJobDescription> generatedJsdls = saver.getGeneratedJsdl();    
      submitGeneratedJsdl( parametricGridJob, generatedJsdls, subMonitor.newChild( 9 ), jobName );
      cleanupSubmission( generationTargetfolder );
      jobId = new ParametricJobID();
    }

    return jobId;
  } 

  private IGridJobID resumeSubmission( final JSDLJobDescription jsdl, final SubMonitor monitor ) throws ProblemException {    
    GridJob parentJob = findParentParamJob( jsdl );
    Assert.isNotNull( parentJob );    
    List<JSDLJobDescription> jsdlList = new ArrayList<JSDLJobDescription>( 1 );
    jsdlList.add( jsdl );
    submitGeneratedJsdl( parentJob, jsdlList, monitor, parentJob.getJobName() );
    cleanupSubmission( ( IFolder )jsdl.getResource().getParent() );
        
    return parentJob.getID();
  }

  private boolean isResumedSubmition( final JSDLJobDescription jsdl ) {
    return findParentParamJob( jsdl ) != null;
  }

  private GridJob findParentParamJob( final JSDLJobDescription jsdl ) {
    GridJob paramJob = null;    
    
    if( !jsdl.isParametric() ) {
      IGridContainer parent = jsdl.getParent();
      
      while( parent != null ) {
        if( parent instanceof GridJob ) {
          GridJob parentJob = ( GridJob )parent;
          IGridJobDescription parentDescription = parentJob.getJobDescription();
          if( parentDescription instanceof JSDLJobDescription
              && (( JSDLJobDescription )parentDescription).isParametric() ) {
            paramJob = parentJob;            
          }
          break;
        }
        parent = parent.getParent();
      }
    }

    
    return paramJob;
  }

  private void cleanupSubmission( final IFolder generationTargetfolder ) throws ProblemException {
    try {
      if( generationTargetfolder.members().length == 0 ) {
        generationTargetfolder.delete( true, null );
      }
    } catch ( CoreException exception ) {
      throw new ProblemException( "eu.geclipse.core.jobs.problem.cleanupSubmissionFailed", exception, Activator.PLUGIN_ID ); //$NON-NLS-1$
    }    
  }

  private List<IGridJobID> submitGeneratedJsdl( final IGridJob parametricJob, final List<JSDLJobDescription> generatedJsdls,
                                    final SubMonitor monitor, final String jobName ) throws ProblemException
  {
    List<IGridJobID> submittedJobs = new ArrayList<IGridJobID>( generatedJsdls.size() );
    GridJobCreator jobCreator = new GridJobCreator();
    monitor.setWorkRemaining( generatedJsdls.size() );
    
    for ( JSDLJobDescription jobDescription : generatedJsdls ) {
      String subjobName = getSubJobName( jobDescription, jobName );      
      
      testCancelled( monitor );
      
      monitor.setTaskName( String.format( Messages.getString("ParametricJobService.taskSubmitting"), jobDescription.getName() ) ); //$NON-NLS-1$
      
      IGridJobID jobID = this.jobService.submitJob( jobDescription, monitor.newChild( 1 ) );
      submittedJobs.add( jobID );
      
      testCancelled( monitor );
      
      jobCreator.canCreate( jobDescription );
      jobCreator.create( parametricJob, jobID, this.jobService, subjobName );
      
      try {
        jobDescription.getResource().delete( true, monitor.newChild( 0 ) );
      } catch( CoreException exception ) {
        throw new ProblemException( "eu.geclipse.core.jobs.problem.deleteGeneratedJsdlFailed", exception, Activator.PLUGIN_ID ); //$NON-NLS-1$
      }      
    }
    
    return submittedJobs;
  }

  private String getSubJobName( final JSDLJobDescription jobDescription,
                                final String jobName )
  {
    String subJobName = jobName;
    String jsdlName = new Path( jobDescription.getName() ).removeFileExtension().toString();    
    int suffixIndex = jsdlName.indexOf( '[' );        
    
    if( suffixIndex > -1 ) {
      subJobName = jobName + jsdlName.substring( suffixIndex );
    }
    
    return subJobName;
  }

  private void testCancelled( final SubMonitor monitor ) {
    if( monitor.isCanceled() ) {
      throw new OperationCanceledException();
    }    
  }

  /* (non-Javadoc)
   * @see eu.geclipse.core.model.IGridResource#getHostName()
   */
  public String getHostName() {
    return this.jobService != null ? this.jobService.getHostName() : Messages.getString("ParametricJobService.unknownHostName"); //$NON-NLS-1$
  }

  /* (non-Javadoc)
   * @see eu.geclipse.core.model.IGridResource#getURI()
   */
  public URI getURI() {
    return this.jobService != null ? this.jobService.getURI() : null;
  }

  /* (non-Javadoc)
   * @see eu.geclipse.core.model.IGridElement#dispose()
   */
  public void dispose() {
    if( this.jobService != null ) {
      this.jobService.dispose();
    }
  }

  /* (non-Javadoc)
   * @see eu.geclipse.core.model.IGridElement#getFileStore()
   */
  public IFileStore getFileStore() {
    return this.jobService.getFileStore();
  }

  /* (non-Javadoc)
   * @see eu.geclipse.core.model.IGridElement#getName()
   */
  public String getName() {
    return this.jobService.getName();
  }

  /* (non-Javadoc)
   * @see eu.geclipse.core.model.IGridElement#getParent()
   */
  public IGridContainer getParent() {
    return this.jobService.getParent();
  }

  /* (non-Javadoc)
   * @see eu.geclipse.core.model.IGridElement#getPath()
   */
  public IPath getPath() {
    return this.jobService.getPath();
  }

  /* (non-Javadoc)
   * @see eu.geclipse.core.model.IGridElement#getProject()
   */
  public IGridProject getProject() {    
    return this.jobService != null ? this.jobService.getProject() : null;
  }

  /* (non-Javadoc)
   * @see eu.geclipse.core.model.IGridElement#getResource()
   */
  public IResource getResource() {
    return this.jobService.getResource();
  }

  /* (non-Javadoc)
   * @see eu.geclipse.core.model.IGridElement#isHidden()
   */
  public boolean isHidden() {
    return this.jobService.isHidden();
  }

  /* (non-Javadoc)
   * @see eu.geclipse.core.model.IGridElement#isLocal()
   */
  public boolean isLocal() {
    return this.jobService.isLocal();
  }

  /* (non-Javadoc)
   * @see eu.geclipse.core.model.IGridElement#isVirtual()
   */
  public boolean isVirtual() {
    return this.jobService.isVirtual();
  }

  /* (non-Javadoc)
   * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
   */
  @SuppressWarnings("unchecked")
  public Object getAdapter( final Class adapter ) {
    return this.jobService != null ? this.jobService.getAdapter( adapter ) : null;
  }

  public IGridJobID submitJob( final IGridJobDescription description,
                               final IProgressMonitor monitor )
    throws ProblemException
  {
    String msg = "ParametricJobService can submit only parametric JSDLs"; //$NON-NLS-1$
    throw new ProblemException( "eu.geclipse.core.jobs.problem.unsupportedOperation", msg, Activator.PLUGIN_ID ); //$NON-NLS-1$
  }

  public IGridJobID submitJob( final IGridJobDescription description,
                               final IVirtualOrganization vo,
                               final IProgressMonitor monitor )
    throws ProblemException
  {
    String msg = "ParametricJobService can submit only parametric JSDLs"; //$NON-NLS-1$
    throw new ProblemException( "eu.geclipse.core.jobs.problem.unsupportedOperation", msg, Activator.PLUGIN_ID ); //$NON-NLS-1$
  }

  public Map<String, URI> getInputFiles( final IGridJobID jobId,
                                         final IGridJobDescription jobDescription,
                                         final IVirtualOrganization vo )
    throws ProblemException
  {
    return null;
  }

  public Map<String, URI> getOutputFiles( final IGridJobID jobId,
                                          final IGridJobDescription jobDescription,
                                          final IVirtualOrganization vo )
    throws ProblemException
  {
    return null;
  }
  
}
