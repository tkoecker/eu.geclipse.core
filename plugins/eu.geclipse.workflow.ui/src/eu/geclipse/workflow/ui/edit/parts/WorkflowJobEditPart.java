/*******************************************************************************
 * Copyright (c) 2006-2008 g-Eclipse Consortium 
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
 *     - David Johnson
 ******************************************************************************/
package eu.geclipse.workflow.ui.edit.parts;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.filesystem.URIUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.FlowLayoutEditPolicy;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CreationEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.gef.ui.figures.DefaultSizeNodeFigure;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import eu.geclipse.core.model.GridModel;
import eu.geclipse.jsdl.JSDLJobDescription;
import eu.geclipse.jsdl.ui.wizards.NewJobWizard;
import eu.geclipse.workflow.model.IWorkflowJob;
import eu.geclipse.workflow.ui.edit.commands.UpdateJobPortsCommand;
import eu.geclipse.workflow.ui.edit.policies.WorkflowJobCanonicalEditPolicy;
import eu.geclipse.workflow.ui.edit.policies.WorkflowJobDragDropEditPolicy;
import eu.geclipse.workflow.ui.edit.policies.WorkflowJobItemSemanticEditPolicy;
import eu.geclipse.workflow.ui.internal.WorkflowDiagramEditorPlugin;
import eu.geclipse.workflow.ui.internal.WorkflowJobFigure;

/**
 * The class that connects the Figure and Model of the WorkflowJob
 */
public class WorkflowJobEditPart extends ShapeNodeEditPart {
  
  
  protected Collection<Resource> savedResources = new ArrayList<Resource>();
  protected Collection<Resource> removedResources = new ArrayList<Resource>();
  protected Collection<Resource> changedResources = new ArrayList<Resource>();
  protected IResourceChangeListener resourceChangeListener = new IResourceChangeListener()
  
  {

    public void resourceChanged( final IResourceChangeEvent event ) {
      IResourceDelta delta = event.getDelta();
      try {
        class ResourceDeltaVisitor implements IResourceDeltaVisitor {
          
          protected Collection<IResource> changedRes = new ArrayList<IResource>();
          protected Collection<IResource> removedRes = new ArrayList<IResource>();
          
          public boolean visit( final IResourceDelta visitDelta ) throws CoreException {
            if( visitDelta.getFlags() != IResourceDelta.MARKERS
                && visitDelta.getResource().getType() == IResource.FILE )
            {
              // TODO implement IResourceDelta.ADDED
              if( ( visitDelta.getKind() & ( IResourceDelta.CHANGED | IResourceDelta.REMOVED ) ) != 0 )
              {
                IResource resource = visitDelta.getResource();
                if( resource != null ) {
                  if( ( visitDelta.getKind() & IResourceDelta.REMOVED ) != 0 )
                  {
                    this.removedRes.add( resource );
                  } else if( !WorkflowJobEditPart.this.savedResources.remove( resource ) )
                  {
                    this.changedRes.add( resource );
                  }
                }
              }
            }
            return true;
          }
          
          public Collection<IResource> getChangedResources() {
            return this.changedRes;
          }

          public Collection<IResource> getRemovedResources() {
            return this.removedRes;
          }                   
        }
        
        ResourceDeltaVisitor visitor = new ResourceDeltaVisitor();
        delta.accept( visitor );
        
        // if a JSDL within the an open .workflow workflow has changed, scan through the
        // workflow and re-sync it??
        EObject o = WorkflowJobEditPart.this.resolveSemanticElement();
        if (o instanceof IWorkflowJob) {
          Collection<IResource> changed = visitor.getChangedResources();
          for (Iterator<IResource> i = changed.iterator(); i.hasNext();) {
            IResource res = i.next();
            String loc = res.getLocation().toString();
            String jobDesc = ( ( IWorkflowJob )o ).getJobDescription();
            
            if (loc.equals( jobDesc )) { 
              // do a JSDLDropCommand??
              java.net.URI jsdlPathUri = URIUtil.toURI( jobDesc );
              IFile jsdlFile = ResourcesPlugin.getWorkspace()
              .getRoot()
              .findFilesForLocationURI( jsdlPathUri )[ 0 ];
              JSDLJobDescription jsdl = new JSDLJobDescription( jsdlFile );

              AbstractTransactionalCommand updatePortsCommand = new UpdateJobPortsCommand(WorkflowJobEditPart.this, jsdl);
              try {
                OperationHistoryFactory.getOperationHistory()
                .execute( updatePortsCommand, new NullProgressMonitor(), null );
              } catch ( ExecutionException eE ) {
                // ignore for now... very naughty!
              }
            }
          }
        } 
        
        // if a JSDL within an open .workflow workflow has been removed, remove it from 
        // the job part property
        EObject o1 = WorkflowJobEditPart.this.resolveSemanticElement();
        if (o1 instanceof IWorkflowJob) {
          Collection<IResource> removed = visitor.getRemovedResources();
          for (Iterator<IResource> i = removed.iterator(); i.hasNext();) {
            IResource res = i.next();
            String loc = res.getLocation().toString();
            String jobDesc = ( ( IWorkflowJob )o1 ).getJobDescription();
            if (loc.equals( jobDesc )) { 
              ( ( IWorkflowJob )o1 ).setJobDescription( "" );               //$NON-NLS-1$
            }
          }          
        }
        
      } catch (CoreException exception ) {
        //Activator.logException( exception );
      }
    }
    
  };  
  

  /**
   * @generated
   */
  public static final int VISUAL_ID = 1001;
  protected IFigure contentPane;
  protected IFigure primaryShape;

  /**
   * Constructor
   */
  public WorkflowJobEditPart( View view ) {
    super( view );
    ResourcesPlugin.getWorkspace()
    .addResourceChangeListener( this.resourceChangeListener,
                                IResourceChangeEvent.POST_CHANGE );     
  }

  /**
   * Creates the edit policies for this EditPart
   */
  @Override
  protected void createDefaultEditPolicies() {
    installEditPolicy( EditPolicyRoles.CREATION_ROLE, new CreationEditPolicy() );
    super.createDefaultEditPolicies();
    installEditPolicy( EditPolicyRoles.SEMANTIC_ROLE,
                       new WorkflowJobItemSemanticEditPolicy() );
    installEditPolicy( EditPolicyRoles.DRAG_DROP_ROLE, 
                       new WorkflowJobDragDropEditPolicy() );
    installEditPolicy( EditPolicyRoles.CANONICAL_ROLE,
                       new WorkflowJobCanonicalEditPolicy() );
    installEditPolicy( EditPolicy.LAYOUT_ROLE, createLayoutEditPolicy() );
    // XXX need an SCR to runtime to have another abstract superclass that would 
    // let children add reasonable editpolicies
    removeEditPolicy( EditPolicyRoles.CONNECTION_HANDLES_ROLE);
  }

  /**
   * @generated
   */
  protected LayoutEditPolicy createLayoutEditPolicy() {
    FlowLayoutEditPolicy lep = new FlowLayoutEditPolicy() {

      @Override
      protected Command createAddCommand( EditPart child, EditPart after ) {
        return null;
      }

      @Override
      protected Command createMoveChildCommand( EditPart child, EditPart after ) {
        return null;
      }

      @Override
      protected Command getCreateCommand( CreateRequest request ) {
        return null;
      }
    };
    return lep;
  }

  /**
   * Creates the shape of the node
   */
  protected IFigure createNodeShape() {
    WorkflowJobFigure figure = new WorkflowJobFigure();
    return this.primaryShape = figure;
  }

  /**
   * Returns the shape of this figure
   */
  public WorkflowJobFigure getPrimaryShape() {
    return ( WorkflowJobFigure )this.primaryShape;
  }

  /**
   * Add children to this edit part 
   */
  protected boolean addFixedChild( EditPart childEditPart ) {
    if( childEditPart instanceof WorkflowJobNameEditPart ) {
      ( ( WorkflowJobNameEditPart )childEditPart ).setLabel( getPrimaryShape().getFigureWorkflowJobNameFigure() );
      return true;
    }
    return false;
  }

  /**
   * Remove a fixed child
   */
  protected boolean removeFixedChild( EditPart childEditPart ) {
    return false;
  }

  /**
   * Adds the child's visual
   */
  @Override
  protected void addChildVisual( EditPart childEditPart, int index ) {
    if( addFixedChild( childEditPart ) ) {
      return;
    }
    super.addChildVisual( childEditPart, 1 );

//    if ( addChild( childEditPart )) {
//      return;
//    }
//    super.addChildVisual( childEditPart, -1 );

  }
/*
 * This does the double-click action on workflow job edit parts
 * (non-Javadoc)
 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart#performRequest(org.eclipse.gef.Request)
 */
  @Override
  public void performRequest( Request request ) {
    
    // on double-click of this edit part
    if (request.getType().equals( RequestConstants.REQ_OPEN )) {
      
      IWorkflowJob job = ( IWorkflowJob )this.resolveSemanticElement();
      String filename = job.getJobDescription();
      if ( (!"".equals( filename ))) {//||(filename != null)
        // if something is present in job description property
        URI fileNameURI = URIUtil.toURI( filename );
        IFile[] file = ResourcesPlugin.getWorkspace().getRoot().findFilesForLocationURI( fileNameURI ); 
        try {
          if( (file.length != 0) && file[ 0 ].exists() ) {
            IDE.openEditor( WorkflowDiagramEditorPlugin.getDefault()
              .getWorkbench()
              .getActiveWorkbenchWindow()
              .getActivePage(), file[ 0 ], true );
          } else {
              // need to handle if file does not exist with problem exception
          }
        } catch( PartInitException partInitException ) {
          WorkflowDiagramEditorPlugin.logException( partInitException );
        }
      } else {
        // if there is no entry in job description property, fire up NewJobWizard
        NewJobWizard newJobWizard = new NewJobWizard();       
        // this bit find the root directory of the workflow
        TransactionalEditingDomain domain = this.getEditingDomain();
        ResourceSet resourceSet = domain.getResourceSet();
        Resource res = resourceSet.getResources().get( 0 );
        org.eclipse.emf.common.util.URI wfRootUri = res.getURI(); 
        String wfRootPath = wfRootUri.path();
        String[] dirs = wfRootPath.split( "/" ); //$NON-NLS-1$
        String projectName = dirs[2];
        IFileStore wfRootFileStore = GridModel.getRoot().getFileStore().getChild( projectName ).getChild( "Workflows" ); //$NON-NLS-1$
        newJobWizard.init( PlatformUI.getWorkbench(), new StructuredSelection(wfRootFileStore) );        
        WizardDialog wizard = new WizardDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), newJobWizard);
        wizard.create();
        wizard.open();
      }
      
    }
    super.performRequest( request );
  }

  /**
   * Removes the child's visual
   */
  @Override
  protected void removeChildVisual( EditPart childEditPart ) {
    if( removeFixedChild( childEditPart ) ) {
      return;
    }
    super.removeChildVisual( childEditPart );
  }

  /**
   * @generated
   */
  @Override
  protected IFigure getContentPaneFor( IGraphicalEditPart editPart ) {
    return super.getContentPaneFor( editPart );
  }

  /**
   * @generated
   */
  protected NodeFigure createNodePlate() {
    DefaultSizeNodeFigure result = new DefaultSizeNodeFigure( getMapMode().DPtoLP( 100 ),
                                                              getMapMode().DPtoLP( 50 ) );
    return result;
  }

  /**
   * Creates figure for this edit part.
   *
   */
  @Override
  protected NodeFigure createNodeFigure() {
    NodeFigure figure = createNodePlate();
    figure.setLayoutManager( new StackLayout() );
    IFigure shape = createNodeShape();
    figure.add( shape );
    this.contentPane = setupContentPane( shape );
    return figure;
  }

  /**
   * Default implementation treats passed figure as content pane.
   * Respects layout one may have set for generated figure.
   * @param nodeShape instance of generated figure class
   * @generated
   */
  protected IFigure setupContentPane( IFigure nodeShape ) {
    if( nodeShape.getLayoutManager() == null ) {
      ConstrainedToolbarLayout layout = new ConstrainedToolbarLayout();
      layout.setSpacing( getMapMode().DPtoLP( 5 ) );
      nodeShape.setLayoutManager( layout );
    }
    return nodeShape; // use nodeShape itself as contentPane
  }

  /**
   * Returns the content pane
   */
  @Override
  public IFigure getContentPane() {
    if( this.contentPane != null ) {
      return this.contentPane;
    }
    return super.getContentPane();
  }


//  @Override
//  public EditPart getPrimaryChildEditPart() {
//    return getChildBySemanticHint( WorkflowVisualIDRegistry.getType( WorkflowJobDescriptionEditPart.VISUAL_ID ) );
//  }
  
}
