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

package eu.geclipse.core.model;

/**
 * Grid element for projects in the grid model. These projects
 * have not necessarily to be Grid projects but can also be any
 * other type of project located in the workspace. If this is
 * really a grid project, i.e. if {@link #hasGridNature()} returns
 * true, it contains a number of standard folders.
 */
public interface IGridProject extends IGridContainer {
  
  /**
   * Get the project folder that is used by default by this project
   * to store elements of the specified type. If no such folder is
   * defined for this project the project itself is returned.
   * 
   * @param elementType The type of the element to be stored.
   * @return The folder that is used by default to store elements of
   * the specified type or the project itself if no such folder is
   * defined for this project.
   */
  public IGridContainer getProjectFolder( final Class< ? extends IGridElement > elementType );
  
  /**
   * Get the project folder that is used by default by this project
   * to store elements of the specified type. If no such folder is
   * defined for this project the project itself is returned.
   * 
   * @param element The element to be stored.
   * @return The folder that is used by default to store elements of
   * the specified type or the project itself if no such folder is
   * defined for this project.
   * @see #getProjectFolder(Class)
   */
  public IGridContainer getProjectFolder( final IGridElement element );
  
  /**
   * Get the project folder ID of the specified {@link IGridContainer}
   * or return <code>null</code> if the specified container is not a
   * project folder.
   * 
   * @param folder The folder for which to return the ID.
   * @return Either the ID or <code>null</code>.
   */
  public String getProjectFolderID( final IGridContainer folder );
  
  /**
   * Get the virtual organization that is associated with this project.
   * 
   * @return The associated {@link IVirtualOrganization}.
   */
  public IVirtualOrganization getVO();
  
  /**
   * Returns true of this is a project with GridNature and false if it is any
   * other type of project.
   * 
   * @return True if this is really a Grid project.
   */
  public boolean hasGridNature();
  
  /**
   * Determine if this project is currently open.
   * 
   * @return True if this project is open, false otherwise.
   */
  public boolean isOpen();
  
}
