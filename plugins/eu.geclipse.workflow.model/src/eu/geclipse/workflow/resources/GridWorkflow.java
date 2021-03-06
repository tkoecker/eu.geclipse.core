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
 *     FZK (http://www.fzk.de)
 *     - Mathias Stumpert - initial API and implementation
 *     - David Johnson - added GridModelListener to correctly refresh changes
 ******************************************************************************/
package eu.geclipse.workflow.resources;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;

import eu.geclipse.core.model.GridModel;
import eu.geclipse.core.model.IGridElement;
import eu.geclipse.core.model.IGridJobDescription;
import eu.geclipse.core.model.IGridModelEvent;
import eu.geclipse.core.model.IGridModelListener;
import eu.geclipse.core.model.impl.ResourceGridContainer;
import eu.geclipse.core.model.impl.ResourceGridElement;
import eu.geclipse.core.reporting.ProblemException;
import eu.geclipse.workflow.IGridWorkflowDescription;
import eu.geclipse.workflow.IGridWorkflowJobDescription;
import eu.geclipse.workflow.impl.WorkflowImpl;
import eu.geclipse.workflow.internal.Activator;
import eu.geclipse.workflow.model.IWorkflowJob;
import eu.geclipse.workflow.model.IWorkflowNode;

/**
 * Grid element for workflow description
 */
public class GridWorkflow extends ResourceGridContainer
implements IGridWorkflowDescription
{

  private WorkflowImpl rootImpl;
  private List<IGridWorkflowJobDescription> jobs;
  static {
    GridModel.addGridModelListener( createGridModelListener() );
  }

  protected GridWorkflow( final IResource resource ) {
    super( resource );
  }

  @Override
  public boolean canContain( final IGridElement element ) {
    return ( element instanceof IGridJobDescription )
    || ( element instanceof ResourceGridElement );
  }

  public String getDescription() {
    return null;
  }

  public String getExecutable() {
    return null;
  }

  public List<String> getExecutableArguments() {
    return null;
  }

  public String getStdErrorFileName() {
    return null;
  }

  public URI getStdErrorUri() throws ProblemException {
    return null;
  }

  public String getStdInputFileName() {
    return null;
  }

  public URI getStdInputUri() throws ProblemException {
    return null;
  }

  public String getStdOutputFileName() {
    return null;
  }

  public URI getStdOutputUri() throws ProblemException {
    return null;
  }

  public List<IGridWorkflowJobDescription> getChildrenJobs() {
    if( this.jobs == null ) {
      WorkflowImpl root = getRoot();
      if( root != null ) {
        this.jobs = new ArrayList<IGridWorkflowJobDescription>();
        for( IWorkflowNode node : root.getNodes() ) {
          if( node instanceof IWorkflowJob ) {
            this.jobs.add( new GridWorkflowJobDescription( ( IWorkflowJob )node ) );
          }
        }
      }
    }
    return this.jobs;
  }

  private WorkflowImpl getRoot() {
    if( this.rootImpl == null ) {
      IFile root = null;
      if( getResource() instanceof IFolder ) {
        IFolder rootFolder = ( IFolder )getResource();
        root = rootFolder.getFile( rootFolder.getName() );
      }
      loadModel( root );
    }
    return this.rootImpl;
  }

  private void loadModel( final IFile file ) {
    String filePath = file.getFullPath().toString();
    // System.out.println( filePath );
    org.eclipse.emf.common.util.URI uri = org.eclipse.emf.common.util.URI.createPlatformResourceURI( filePath,
                                                                                                     false );
    ResourceSet resourceSet = new ResourceSetImpl();
    Resource resource = resourceSet.createResource( uri );
    Map<String, Object> options = new HashMap<String, Object>();
    options.put( XMLResource.OPTION_ENCODING, "UTF8" ); //$NON-NLS-1$
    try {
      resource.load( options );
      this.rootImpl = ( WorkflowImpl )resource.getContents().get( 0 );
      this.jobs = new ArrayList<IGridWorkflowJobDescription>();
      for( IWorkflowNode node : this.rootImpl.getNodes() ) {
        if( node instanceof IWorkflowJob ) {
          this.jobs.add( new GridWorkflowJobDescription( ( IWorkflowJob )node ) );
        }
      }
    } catch( IOException ioEx ) {
      Activator.logException( ioEx );
    }
  }

  private static IGridModelListener createGridModelListener() {
    return new IGridModelListener() {

      public void gridModelChanged( final IGridModelEvent event ) {
        switch( event.getType() ) {
          case IGridModelEvent.ELEMENTS_CHANGED:
            if( event.getElements().length > 0 ) {
              IGridElement element = event.getElements()[ 0 ];
              if( element instanceof GridWorkflow ) {
                onWorkflowChanged( event.getElements() );
              }
            }
            break;
        }
      }

      private void onWorkflowChanged( final IGridElement[] elements ) {
        for( IGridElement gridElement : elements ) {
          if( gridElement instanceof GridWorkflow ) {
            GridWorkflow workflowDescription = ( GridWorkflow )gridElement;
            IFolder workflowDescriptionFolder = ( IFolder )workflowDescription.getResource();
            IFile workflowDescriptionFile = workflowDescriptionFolder.getFile( workflowDescriptionFolder.getName() );
            workflowDescription.loadModel( workflowDescriptionFile );
          }
        }
      }
    };
  }

  /**
   * @return
   */
  public File getWorkflowFilePath() {
    File workflowFilePath = new File( this.getResource()
                                      .getLocation()
                                      .toFile()
                                      .toString()
                                      + "/"
                                      + this.getResource()
                                      .getLocation()
                                      .toFile()
                                      .getName() );
    return workflowFilePath;
  }
}
