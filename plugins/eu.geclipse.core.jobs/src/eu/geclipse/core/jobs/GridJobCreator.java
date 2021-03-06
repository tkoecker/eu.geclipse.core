/*****************************************************************************
 * Copyright (c) 2006-2008 g-Eclipse Consortium 
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
 *    Pawel Wolniewicz 
 *    Mariusz Wojtysiak
 *    Ariel Garcia      - updated to new problem reporting
 *****************************************************************************/
package eu.geclipse.core.jobs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;

import eu.geclipse.core.ICoreProblems;
import eu.geclipse.core.jobs.internal.Activator;
import eu.geclipse.core.model.GridModel;
import eu.geclipse.core.model.ICreatorSourceMatcher;
import eu.geclipse.core.model.IGridContainer;
import eu.geclipse.core.model.IGridElement;
import eu.geclipse.core.model.IGridJob;
import eu.geclipse.core.model.IGridJobDescription;
import eu.geclipse.core.model.IGridJobID;
import eu.geclipse.core.model.IGridJobService;
import eu.geclipse.core.model.impl.AbstractGridJobCreator;
import eu.geclipse.core.reporting.ProblemException;
import eu.geclipse.jsdl.JSDLJobDescription;
import eu.geclipse.workflow.IGridWorkflowDescription;
import eu.geclipse.workflow.IGridWorkflowJobDescription;
import eu.geclipse.workflow.IGridWorkflowJobID;

/**
 * GridJobCreator creates grid jobs. A GridJob object is middleware independent
 * and contains a GridJobID, IGridJobDescription and GridJobStatus. These last
 * three components can be middleware dependent.
 */
public class GridJobCreator
    extends AbstractGridJobCreator
    implements ICreatorSourceMatcher {

  /*
   * (non-Javadoc)
   * 
   * @see eu.geclipse.core.model.IGridElementCreator#create(eu.geclipse.core.model.IGridContainer)
   */
  public IGridElement create( final IGridContainer parent )
    throws ProblemException
  {
    IResource resource = ( IResource )getSource();
    if( resource == null ) {
      throw new ProblemException( ICoreProblems.MODEL_ELEMENT_CREATE_FAILED,
                                  "Source object is null.",
                                  Activator.PLUGIN_ID );
    }
    if( !( resource instanceof IFolder ) ) {
      throw new ProblemException( ICoreProblems.MODEL_ELEMENT_CREATE_FAILED,
                                  Activator.PLUGIN_ID );
    }
    return new GridJob( ( IFolder )resource );
  }

  /*
   * (non-Javadoc)
   * 
   * @see eu.geclipse.core.model.impl.AbstractGridJobCreator#internalCanCreate(eu.geclipse.core.model.IGridJobDescription)
   */
  @Override
  protected boolean internalCanCreate( final IGridJobDescription description ) {
    // TODO pawelw check if it is possible to find supported job description
    // from wizards
    return true;
    // return ( description instanceof JSDLJobDescription
    // || description instanceof IGridWorkflow);
  }

  public boolean canCreate( final Object source ) {
    return source instanceof IFolder
                                    ? GridJob.canCreate( ( IFolder )source )
                                    : false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see eu.geclipse.core.model.IGridJobCreator#create(eu.geclipse.core.model.IGridContainer,
   *      eu.geclipse.core.model.IGridJobID)
   */
  public IGridJob create( final IGridContainer parent,
                          final IGridJobID id,
                          final IGridJobService jobService,
                          final String jobName ) throws ProblemException
  {
    if( !( id instanceof GridJobID ) ) {
      throw new ProblemException( ICoreProblems.MODEL_ELEMENT_CREATE_FAILED,
                                  Messages.getString( "GridJobCreator.cannotCreateJobFromID" ), //$NON-NLS-1$
                                  Activator.PLUGIN_ID );
    }
    IContainer container = ( IContainer )parent.getResource();
    IGridJobDescription description = getDescription();
    String uniqueJobName = findJobFileName( jobName, container );
    IFolder jobFolder = getJobFolder( container, uniqueJobName );
    try {
      // create temporary job folder, to prevent adding new GridJob to project
      // IPath stateLocation = Activator.getDefault().getStateLocation();
      jobFolder.delete( true, null );
      jobFolder.create( true, true, null );
      GridJob job = GridJob.createJobStructure( jobFolder,
                                                ( GridJobID )id,
                                                jobService,
                                                description,
                                                uniqueJobName );
      if( description instanceof IGridWorkflowDescription
          && id instanceof IGridWorkflowJobID )
      {
        createWorkflowJobStructure( job,
                                    jobFolder,
                                    ( IGridWorkflowJobID )id,
                                    jobService,
                                    ( IGridWorkflowDescription )description );
      }
      setSource( jobFolder );
      parent.create( this );
      IGridElement jobElement = GridModel.getRoot().findElement( jobFolder );
      // TODO mariusz check, why findElement() returns null for workflows
      IGridJob result = null;
      if( jobElement instanceof IGridJob ) {
        result = ( IGridJob )jobElement;
      }
      return result;
    } catch( CoreException cExc ) {
      throw new ProblemException( ICoreProblems.MODEL_ELEMENT_CREATE_FAILED,
                                  Messages.getString( "GridJobCreator.problemCreatingFolder" ) //$NON-NLS-1$
                                      + jobFolder.getName(),
                                  cExc,
                                  Activator.PLUGIN_ID );
    }
  }

  /**
   * @param baseName
   * @param folder
   * @return
   */
  private String findJobFileName( final String jobName,
                                  final IContainer container )
  {
    String baseJobName = jobName != null
                                        ? jobName
                                        : Messages.getString( "GridJobCreator.defaultJobName" ); //$NON-NLS-1$
    String uniqueJobName = baseJobName;
    int jobNr = 0;
    for( IFolder jobFolder = getJobFolder( container, uniqueJobName ); jobFolder.exists(); jobFolder = getJobFolder( container,
                                                                                                                     uniqueJobName ) )
    {
      uniqueJobName = String.format( "%s[%d]", baseJobName, Integer.valueOf( ++jobNr ) ); //$NON-NLS-1$
    }
    return uniqueJobName;
  }

  private IFolder getJobFolder( final IContainer container,
                                final String uniqueJobName )
  {
    String folderName = String.format( ".%s%s", uniqueJobName, GridJob.JOBFILE_EXTENSION ); //$NON-NLS-1$
    return container.getFolder( new Path( folderName ) );
  }

  private void createWorkflowJobStructure( final GridJob workflowJob,
                                           final IFolder jobFolder,
                                           final IGridWorkflowJobID id,
                                           final IGridJobService jobService,
                                           final IGridWorkflowDescription workflowDescription )
    throws ProblemException
  {
    List<IGridWorkflowJobID> childrenIds = id.getChildrenJobs();
    if( childrenIds != null ) {
      for( IGridWorkflowJobID childId : childrenIds ) {
        IGridWorkflowJobDescription childJob = findJob( workflowDescription,
                                             childId.getName() );
        if( childJob != null ) {
          JSDLJobDescription childJobDescription = createChildJobDescription( jobFolder,
                                                                              childJob );
          if( this.canCreate( childJobDescription ) ) {
            this.create( workflowJob,
                         childId,
                         jobService,
                         childJobDescription.getPath()
                           .removeFileExtension()
                           .lastSegment() );
          }
          deleteTmpJobDescription( childJobDescription );
        }
      }
    }
  }

  private void deleteTmpJobDescription( final JSDLJobDescription childJobDescription )
  {
    IResource resource = childJobDescription.getResource();
    try {
      resource.delete( true, null );
    } catch( CoreException exception ) {
      // don't break submission, when error occur here
    }
  }

  private IGridWorkflowJobDescription findJob( final IGridWorkflowDescription workflowDescription,
                                    final String jobName )
  {
    IGridWorkflowJobDescription job = null;
    for( IGridWorkflowJobDescription curJob : workflowDescription.getChildrenJobs() ) {
      if( curJob.getName().equals( jobName ) ) {
        job = curJob;
        break;
      }
    }
    return job;
  }

  private JSDLJobDescription createChildJobDescription( final IFolder jobFolder,
                                                        final IGridWorkflowJobDescription childJob ) throws ProblemException
  {
    JSDLJobDescription description = null;
    FileInputStream inputStream = null;
    Path descriptionPath = childJob.getDescriptionPath();
    String tmpJsdlFileName = String.format( "%s.jsdl", childJob.getName() ); //$NON-NLS-1$
    IFile outputFile = jobFolder.getFile( tmpJsdlFileName );
    try {
      inputStream = new FileInputStream( new File( descriptionPath.toString() ) );
      outputFile.create( inputStream, true, null );
      description = new JSDLJobDescription( outputFile );
    } catch( CoreException exception ) {
      Activator.logException( exception,
                              "Cannot create jsdl job description for child job." ); //$NON-NLS-1$
    } catch( FileNotFoundException exception ) {
      String msg = String.format( "Job description file cannot be found %s", descriptionPath.toString() ); //$NON-NLS-1$
      throw new ProblemException( ICoreProblems.IO_OPERATION_FAILED, msg, exception, Activator.PLUGIN_ID );
    } finally {
      if( inputStream != null ) {
        try {
          inputStream.close();
        } catch( IOException exception ) {
          // ignore errors
        }
      }
    }
    return description;
  }
}
