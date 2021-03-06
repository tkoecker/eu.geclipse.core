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
package eu.geclipse.workflow.ui.editor;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.ui.URIEditorInput;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gmf.runtime.common.ui.services.marker.MarkerNavigationService;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDiagramDocument;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDocument;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDocumentProvider;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.parts.DiagramDocumentEditor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorMatchingStrategy;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.part.FileEditorInput;

import eu.geclipse.workflow.ui.internal.WorkflowDiagramEditorPlugin;
import eu.geclipse.workflow.ui.listeners.JSDLTransferDropTargetListener;
import eu.geclipse.workflow.ui.part.WorkflowPaletteFactory;

/**
 * @generated
 */
public class WorkflowDiagramEditor extends DiagramDocumentEditor implements IGotoMarker
{

  /**
   * @generated
   */
  public static final String ID = "eu.geclipse.workflow.ui.part.WorkflowDiagramEditorID"; //$NON-NLS-1$
  /**
   * @generated
   */
  public static final String CONTEXT_ID = "eu.geclipse.workflow.ui.diagramContext"; //$NON-NLS-1$

  /**
   * @generated
   */
  public WorkflowDiagramEditor() {
    super( true );
  }

  /**
   * @generated
   */
  @Override
  protected String getContextID() {
    return CONTEXT_ID;
  }

  /**
   * @generated
   */
  @Override
  protected PaletteRoot createPaletteRoot( final PaletteRoot existingPaletteRoot ) {
    PaletteRoot root = super.createPaletteRoot( existingPaletteRoot );
    new WorkflowPaletteFactory().fillPalette( root );
    return root;
  }
  
  @Override
  protected void initializeGraphicalViewer() {
    getGraphicalViewer().addDropTargetListener( new JSDLTransferDropTargetListener(getGraphicalViewer()));
    super.initializeGraphicalViewer();
    
}


  /**
   * @generated
   */
  @Override
  protected PreferencesHint getPreferencesHint() {
    return WorkflowDiagramEditorPlugin.DIAGRAM_PREFERENCES_HINT;
  }

  /**
   * @generated
   */
  @Override
  public String getContributorId() {
    return WorkflowDiagramEditorPlugin.ID;
  }

  /**
   * @generated
   */
  @Override
  protected IDocumentProvider getDocumentProvider( final IEditorInput input ) {
    if( input instanceof IFileEditorInput || input instanceof URIEditorInput ) {
      return WorkflowDiagramEditorPlugin.getInstance().getDocumentProvider();
    }
    return super.getDocumentProvider( input );
  }

  /**
   * @generated
   */
  @Override
  public TransactionalEditingDomain getEditingDomain() {
    IDocument document = getEditorInput() != null
                                                 ? getDocumentProvider().getDocument( getEditorInput() )
                                                 : null;
    if( document instanceof IDiagramDocument ) {
      return ( ( IDiagramDocument )document ).getEditingDomain();
    }
    return super.getEditingDomain();
  }

  /**
   * @generated
   */
  @Override
  protected void setDocumentProvider( final IEditorInput input ) {
    if( input instanceof IFileEditorInput || input instanceof URIEditorInput ) {
      setDocumentProvider( WorkflowDiagramEditorPlugin.getInstance()
        .getDocumentProvider() );
    } else {
      super.setDocumentProvider( input );
    }
  }

  /**
   * @generated
   */
  public void gotoMarker( final IMarker marker ) {
    MarkerNavigationService.getInstance().gotoMarker( this, marker );
  }

  /**
   * @generated
   */
  @Override
  public boolean isSaveAsAllowed() {
    return true;
  }

  /**
   * @generated
   */
  @Override
  public void doSaveAs() {
    performSaveAs( new NullProgressMonitor() );
  }

  /**
   * @generated
   */
  @Override
  protected void performSaveAs( final IProgressMonitor progressMonitor ) {
    Shell shell = getSite().getShell();
    IEditorInput input = getEditorInput();
    SaveAsDialog dialog = new SaveAsDialog( shell );
    IFile original = input instanceof IFileEditorInput
                                                      ? ( ( IFileEditorInput )input ).getFile()
                                                      : null;
    if( original != null ) {
      dialog.setOriginalFile( original );
    }
    dialog.create();
    IDocumentProvider provider = getDocumentProvider();
    if( provider == null ) {
      // editor has been programmatically closed while the dialog was open
      return;
    }
    if( provider.isDeleted( input ) && original != null ) {
      String message = NLS.bind( "The original file ''{0}'' has been deleted.", //$NON-NLS-1$
                                 original.getName() );
      dialog.setErrorMessage( null );
      dialog.setMessage( message, IMessageProvider.WARNING );
    }
    if( dialog.open() == Window.CANCEL ) {
      if( progressMonitor != null ) {
        progressMonitor.setCanceled( true );
      }
      return;
    }
    IPath filePath = dialog.getResult();
    if( filePath == null ) {
      if( progressMonitor != null ) {
        progressMonitor.setCanceled( true );
      }
      return;
    }
    IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
    IFile file = workspaceRoot.getFile( filePath );
    final IEditorInput newInput = new FileEditorInput( file );
    // Check if the editor is already open
    IEditorMatchingStrategy matchingStrategy = getEditorDescriptor().getEditorMatchingStrategy();
    IEditorReference[] editorRefs = PlatformUI.getWorkbench()
      .getActiveWorkbenchWindow()
      .getActivePage()
      .getEditorReferences();
    for( int i = 0; i < editorRefs.length; i++ ) {
      if( matchingStrategy.matches( editorRefs[ i ], newInput ) ) {
        MessageDialog.openWarning( shell,
                                   "Problem During Save As...", //$NON-NLS-1$
                                   "Save could not be completed. Target file is already open in another editor." ); //$NON-NLS-1$
        return;
      }
    }
    boolean success = false;
    try {
      provider.aboutToChange( newInput );
      getDocumentProvider( newInput ).saveDocument( progressMonitor,
                                                    newInput,
                                                    getDocumentProvider().getDocument( getEditorInput() ),
                                                    true );
      success = true;
    } catch( CoreException x ) {
      IStatus status = x.getStatus();
      if( status == null || status.getSeverity() != IStatus.CANCEL ) {
        ErrorDialog.openError( shell,
                               "Save Problems", //$NON-NLS-1$
                               "Could not save file.", //$NON-NLS-1$
                               x.getStatus() );
      }
    } finally {
      provider.changed( newInput );
      if( success ) {
        setInput( newInput );
      }
    }
    if( progressMonitor != null ) {
      progressMonitor.setCanceled( !success );
    }
  }
}