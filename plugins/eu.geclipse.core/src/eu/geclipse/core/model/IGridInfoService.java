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
 *    Mathias Stuempert - initial API and implementation
 *****************************************************************************/

package eu.geclipse.core.model;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * An info service is an {@link IGridService} that provides access
 * to a grid-related information database.
 */
public interface IGridInfoService extends IGridService {
  
  /**
   * Fetch the applications from the underlying database that
   * are available for the specified virtual organization.
   * 
   * @param parent The parent of this element.
   * @param vo The virtual organization for which to fetch the
   * computing elements. This may be <code>null</code>. In that
   * case all available applications should be returned.
   * @param computing The computing for which to fetch the applications.
   * This may be <code>null</code>. In that case all available applications
   * should be returned.
   * @param monitor An {@link IProgressMonitor} to monitor the
   * progress of the operation. 
   * @return The available applications for the specified VO and
   * computings. Both Vo and computings are not used for filtering
   * if they are <code>null</code>.
   */
  public IGridApplication[] fetchApplications( final IGridContainer parent,
                                               final IVirtualOrganization vo,
                                               final IGridComputing computing,
                                               final IProgressMonitor monitor );
  
  /**
   * Fetch the computing elements from the underlying database that
   * are available for the specified virtual organization.
   * 
   * @param parent The parent of this element. 
   * @param vo The virtual organization for which to fetch the
   * computing elements. This may be <code>null</code>. In that
   * case all available computing elements should be returned.
   * @param monitor An {@link IProgressMonitor} to monitor the
   * progress of the operation. 
   * @return The available computing elements for the
   * specified VO or all available elements if the specified
   * VO is <code>null</code>.
   */
  public IGridComputing[] fetchComputing( final IGridContainer parent,
                                          final IVirtualOrganization vo,
                                          final IProgressMonitor monitor );
  
  /**
   * Fetch the storage elements from the underlying database that
   * are available for the specified virtual organization.
   * 
   * @param parent The parent of this element.
   * @param vo The virtual organization for which to fetch the
   * storage elements. This may be <code>null</code>. In that
   * case all available storage elements should be returned.
   * @param monitor An {@link IProgressMonitor} to monitor the
   * progress of the operation.
   * @return The available storage elements for the
   * specified VO or all available elements if the specified
   * VO is <code>null</code>.
   */
  public IGridStorage[] fetchStorage( final IGridContainer parent,
                                      final IVirtualOrganization vo,
                                      final IProgressMonitor monitor );
  
  /**
   * Fetch all available services from the underlying database
   * for the specified virtual organization.
   * 
   * @param parent The parent of this element.
   * @param vo The virtual organization for which to fetch the
   * storage elements. This may be <code>null</code>. In that
   * case all available services should be returned.
   * @param monitor An {@link IProgressMonitor} to monitor the
   * progress of the operation.
   * @return The available services for the
   * specified VO or all available services if the specified
   * VO is <code>null</code>.
   */
  public IGridService[] fetchServices( final IGridContainer parent,
                                       final IVirtualOrganization vo,
                                       final IProgressMonitor monitor );

}
