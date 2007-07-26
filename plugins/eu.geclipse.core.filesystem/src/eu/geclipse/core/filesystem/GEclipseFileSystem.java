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

package eu.geclipse.core.filesystem;

import java.net.URI;

import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.filesystem.IFileSystem;
import org.eclipse.core.filesystem.IFileTree;
import org.eclipse.core.filesystem.provider.FileSystem;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import eu.geclipse.core.filesystem.internal.Activator;
import eu.geclipse.core.filesystem.internal.filesystem.GEclipseFileStore;
import eu.geclipse.core.filesystem.internal.filesystem.FileSystemManager;

/**
 * Implementation of the {@link IFileSystem} interface for the g-Eclipse
 * file system. The g-Eclipse file system is a wrapper for any other
 * file system. It enabled lazy loading within the Grid model views
 * and prevents the Eclipse resource management from fetching the
 * whole file tree from a linked file system.
 * 
 * Since the g-Eclipse file system is a wrapper for other file systems
 * it is called the master file system whereas the wrapped file system
 * is called the slave file system.
 */
public class GEclipseFileSystem
    extends FileSystem
    implements IFileSystem {
  
  /**
   * The slave file system.
   */
  private IFileSystem slave;
  
  /**
   * Create a g-Eclipse URI from the specified URI. If the specified URI
   * is already a g-Eclipse URI it is just returned.
   * 
   * @param slaveURI The slave URI from which to create a master URI. 
   * @return The g-Eclipse URI that corresponds to the specified
   * slave URI. 
   */
  public static URI createMasterURI( final URI slaveURI ) {
    return FileSystemManager.createMasterURI( slaveURI );
  }
  
  /**
   * Create a slave URI from the specified g-Eclipse URI. If the specified
   * URI is not a g-Eclipse URI it is just returned.
   * 
   * @param masterURI The g-Eclipse URI from which to create the slave URI.
   * @return The slave URI that corresponds to the specified g-Eclipse URI.
   */
  public static URI createSlaveURI( final URI masterURI ) {
    return FileSystemManager.createSlaveURI( masterURI );
  }
    
  /* (non-Javadoc)
   * @see org.eclipse.core.filesystem.provider.FileSystem#attributes()
   */
  @Override
  public int attributes() {
    return this.slave != null ? this.slave.attributes() : super.attributes();
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.core.filesystem.provider.FileSystem#canDelete()
   */
  @Override
  public boolean canDelete() {
    return this.slave != null ? this.slave.canDelete() : super.canDelete();
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.core.filesystem.provider.FileSystem#canWrite()
   */
  @Override
  public boolean canWrite() {
    return this.slave != null ? this.slave.canWrite() : super.canWrite();
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.core.filesystem.provider.FileSystem#fetchFileTree(org.eclipse.core.filesystem.IFileStore, org.eclipse.core.runtime.IProgressMonitor)
   */
  @Override
  public IFileTree fetchFileTree( final IFileStore root,
                                  final IProgressMonitor monitor ) {
    
    IFileTree fileTree = null;
    
    if ( ( this.slave != null ) && ( root instanceof GEclipseFileStore ) ) {
      IFileStore slaveStore = ( ( GEclipseFileStore ) root ).getSlave();
      try {
        fileTree = this.slave.fetchFileTree( slaveStore, monitor );
      } catch ( CoreException cExc ) {
        Activator.logException( cExc );
      }
    }
      
    if ( fileTree == null ) {
      fileTree = super.fetchFileTree( root, monitor );
    }
    
    return fileTree;
    
  }

  /* (non-Javadoc)
   * @see org.eclipse.core.filesystem.provider.FileSystem#getStore(java.net.URI)
   */
  @Override
  public IFileStore getStore( final URI uri ) {
    GEclipseFileStore result = null;
    FileSystemManager manager = FileSystemManager.getInstance();
    result = manager.getStore( this, uri );
    this.slave = result.getSlave().getFileSystem();
    return result;
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.core.filesystem.provider.FileSystem#isCaseSensitive()
   */
  @Override
  public boolean isCaseSensitive() {
    return this.slave != null ? this.slave.isCaseSensitive() : super.isCaseSensitive();
  }
  
}
