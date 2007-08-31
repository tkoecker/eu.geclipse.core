/*****************************************************************************
 * Copyright (c) 2006, 2007 g-Eclipse Consortium 
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
 *    Mathias Stuempert - initial API and implementation
 *****************************************************************************/

package eu.geclipse.core.filesystem.internal.filesystem;

import java.net.URI;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileInfo;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.filesystem.IFileSystem;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;

import eu.geclipse.core.filesystem.GEclipseFileSystem;
import eu.geclipse.core.filesystem.internal.Activator;
import eu.geclipse.core.model.GridModel;
import eu.geclipse.core.model.IGridConnectionElement;
import eu.geclipse.core.model.IGridContainer;
import eu.geclipse.core.model.IGridElement;
import eu.geclipse.core.model.impl.AbstractGridContainer;

/**
 * Internal implementation of the {@link IGridConnectionElement}.
 */
public class ConnectionElement
    extends AbstractGridContainer
    implements IGridConnectionElement {
  
  /**
   * The corresponding resource.
   */
  private IResource resource;
  
  /**
   * An error that may have occured during the last fetch operation.
   */
  private Throwable fetchError;
  
  /**
   * Create a new connection element from the specified resource.
   * 
   * @param resource The resource from which to create a new element.
   * This resource has either to be a folder linked to a g-Eclipse URI
   * or has to be a child of such a folder.
   */
  public ConnectionElement( final IResource resource ) {
    Assert.isNotNull( resource );
    this.resource = resource;
  }
  
  /* (non-Javadoc)
   * @see eu.geclipse.core.model.impl.AbstractGridContainer#canContain(eu.geclipse.core.model.IGridElement)
   */
  @Override
  public boolean canContain( final IGridElement element ) {
    return isFolder() && ( element instanceof IGridConnectionElement );
  }

  /* (non-Javadoc)
   * @see eu.geclipse.core.model.IGridConnectionElement#getConnectionFileInfo()
   */
  public IFileInfo getConnectionFileInfo() throws CoreException {
    return getConnectionFileStore().fetchInfo();
  }

  /* (non-Javadoc)
   * @see eu.geclipse.core.model.IGridConnectionElement#getConnectionFileStore()
   */
  public IFileStore getConnectionFileStore() throws CoreException {
    IFileStore result = null;
    IResource res = getResource();
    if ( res != null ) {
      URI uri = res.getLocationURI();
      GEclipseFileSystem fileSystem = new GEclipseFileSystem();
      result = fileSystem.getStore( uri ); 
    }
    return result;
  }
  
  /* (non-Javadoc)
   * @see eu.geclipse.core.model.IGridConnectionElement#getError()
   */
  public String getError() {
    String error = null;
    if ( this.fetchError != null ) {
      error = this.fetchError.getLocalizedMessage();
    }
    return error;
  }
  
  public URI getURI() {
    URI result = null;
    try {
      GEclipseFileStore fileStore = ( GEclipseFileStore ) getConnectionFileStore();
      result = fileStore.getSlave().toURI();
    } catch ( CoreException cExc ) {
      Activator.logException( cExc );
    }
    return result;
  }
  
  /* (non-Javadoc)
   * @see eu.geclipse.core.model.impl.AbstractGridContainer#hasChildren()
   */
  @Override
  public boolean hasChildren() {
    return isFolder() ? super.hasChildren() : false;
  }

  /* (non-Javadoc)
   * @see eu.geclipse.core.model.IGridConnectionElement#isFolder()
   */
  public boolean isFolder() {
    return getResource().getType() == IResource.FOLDER;
  }
  
  /* (non-Javadoc)
   * @see eu.geclipse.core.model.impl.AbstractGridElement#isHidden()
   */
  @Override
  public boolean isHidden() {
    return false;
  }
  
  /* (non-Javadoc)
   * @see eu.geclipse.core.model.IGridContainer#isLazy()
   */
  public boolean isLazy() {
    return true;
  }
  
  /* (non-Javadoc)
   * @see eu.geclipse.core.model.IGridElement#isLocal()
   */
  public boolean isLocal() {
    
    boolean result = false;
    
    try {
      GEclipseFileStore fileStore = ( GEclipseFileStore ) getConnectionFileStore();
      result = fileStore.isLocal();
    } catch ( CoreException cExc ) {
      Activator.logException( cExc );
    }
    
    return result;
    
  }

  /* (non-Javadoc)
   * @see eu.geclipse.core.model.IGridConnectionElement#isValid()
   */
  public boolean isValid() {
    return getError() == null;
  }
  
  /* (non-Javadoc)
   * @see eu.geclipse.core.model.impl.AbstractGridContainer#fetchChildren(org.eclipse.core.runtime.IProgressMonitor)
   */
  @Override
  protected synchronized boolean fetchChildren( final IProgressMonitor monitor ) {
    
    IProgressMonitor lMonitor
      = monitor == null
      ? new NullProgressMonitor()
      : monitor;
    
    this.fetchError = null;
    
    lMonitor.beginTask( String.format( Messages.getString("ConnectionElement.fetching_children_progress"), getName() ), 100 ); //$NON-NLS-1$
    
    try {
    
      IResource res = getResource();
      GEclipseFileStore fileStore = ( GEclipseFileStore ) getConnectionFileStore();
      fileStore.reset();
      res.refreshLocal( IResource.DEPTH_INFINITE, new SubProgressMonitor( lMonitor, 10 ) );
      fileStore.activate();
      res.refreshLocal( IResource.DEPTH_ONE, new SubProgressMonitor( lMonitor, 90 ) );
      
    } catch ( CoreException cExc ) {
      this.fetchError = cExc;
    } finally {
      lMonitor.done();
    }
    
    return this.fetchError == null;
    
  }

  /* (non-Javadoc)
   * @see eu.geclipse.core.model.IGridElement#getFileStore()
   */
  public IFileStore getFileStore() {
    URI uri = getResource().getLocationURI();
    IFileSystem fileSystem = EFS.getLocalFileSystem();
    IFileStore fileStore = fileSystem.getStore( uri );
    return fileStore;
  }

  /* (non-Javadoc)
   * @see eu.geclipse.core.model.IGridElement#getName()
   */
  public String getName() {
    return getResource().getName();
  }

  /* (non-Javadoc)
   * @see eu.geclipse.core.model.IGridElement#getParent()
   */
  public IGridContainer getParent() {
    IGridContainer parent = null;
    IPath parentPath = getPath().removeLastSegments( 1 );
    IGridElement parentElement = GridModel.getRoot().findElement( parentPath );
    if ( parentElement instanceof IGridContainer ) {
      parent = ( IGridContainer ) parentElement;
    }
    return parent;
  }

  /* (non-Javadoc)
   * @see eu.geclipse.core.model.IGridElement#getPath()
   */
  public IPath getPath() {
    return getResource().getFullPath();
  }

  /* (non-Javadoc)
   * @see eu.geclipse.core.model.IGridElement#getResource()
   */
  public IResource getResource() {
    return this.resource;
  }

}
