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

import java.util.ArrayList;
import java.util.List;

import eu.geclipse.core.internal.model.ConnectionManager;
import eu.geclipse.core.internal.model.ElementCreatorRegistry;
import eu.geclipse.core.internal.model.GridRoot;
import eu.geclipse.core.internal.model.HiddenProject;
import eu.geclipse.core.internal.model.JobManager;
import eu.geclipse.core.internal.model.ServiceJobManager;
import eu.geclipse.core.internal.model.VoManager;
import eu.geclipse.core.internal.model.notify.GridNotificationService;
import eu.geclipse.core.reporting.ProblemException;


/**
 * The <code>GridModel</code> class is mainly a helper class for dealing
 * with grid model elements. It defines a method the retrieve the root
 * element of the model (@link {@link #getRoot()}) and several other methods
 * to get element creators for specific use cases. 
 */
public class GridModel {
  
  /**
   * Get the manager that is dedicated to the management of
   * {@link IGridConnection}s.
   * 
   * @return The core implementation of the {@link IConnectionManager}
   * interface. 
   */
  public static IConnectionManager getConnectionManager() {
    return ConnectionManager.getManager();
  }
  
  /**
   * Get the element creator registry that holds a list of all registered
   * grid element creators.
   * 
   * @return The model's element creator registry.
   */
  public static IElementCreatorRegistry getCreatorRegistry() {
    return ElementCreatorRegistry.getRegistry();
  }
  
  /**
   * Get the manager that is dedicated to the management of
   * {@link IGridJob}s.
   * 
   * @return The core implementation of the {@link IGridJobManager}
   * interface. 
   */
  public static IGridJobManager getJobManager() {
    return JobManager.getManager();
  }
  
  /**
   * Get the manager that is dedicated to the management of
   * {@link IServiceJob}s.
   * 
   * @return The core implementation of the {@link IServiceJobManager}
   * interface. 
   */
  public static IServiceJobManager getTestManager() {
    return ServiceJobManager.getManager();
  }

  /**
   * Get the root element of the grid model tree. This is an immutable
   * internal implementation of the {@link IGridRoot} interface. It is
   * created as singleton the first time this method is called. In
   * subsequent calls this method returns the singleton instance that
   * was created formerly.
   * 
   * @return The root of all evil, i.e. the root element of the grid model.
   * @see GridRoot#getInstance()
   */
  public static IGridRoot getRoot() {
    return GridRoot.getInstance();
  }
  
  /**
   * Get a list of all element creators that are able to create
   * {@link IGridElement}s of the specified type. Note that this method
   * only searches the extended creators and not the standard creators.
   * 
   * @param elementType The type of the element that should be
   * created.
   * @return A list containing all currently registered
   * {@link IGridElementCreator}s that are able to create elements
   * of the specified type.
   * @see #getElementCreators()
   */
  public static List< IGridElementCreator > getElementCreators( final Class< ? extends IGridElement > elementType ) {
    List< IGridElementCreator > resultList
      = new ArrayList< IGridElementCreator >();
    List<IGridElementCreator> creators
      = getCreatorRegistry().getCreators();
    for ( IGridElementCreator creator : creators ) {
      if ( creator.canCreate( elementType ) ) {
        resultList.add( creator );
      }
    }
    return resultList;
  }
  
  /**
   * Get a list of all element creators that implement the
   * {@link IGridJobCreator} interface. Note that this method
   * only searches the extended creators and not the standard creators.
   * 
   * @return All currently registered job creators.
   * @see #getElementCreators()
   */
  public static List< IGridJobCreator > getJobCreators() {
    List< IGridJobCreator > jobCreators
      = new ArrayList< IGridJobCreator >();
    List< IGridElementCreator > elementCreators
      = getCreatorRegistry().getCreators();
    for ( IGridElementCreator creator : elementCreators ) {
      if ( creator instanceof IGridJobCreator ) {
        jobCreators.add( ( IGridJobCreator ) creator ); 
      }
    }
    return jobCreators;
  }
  
  /**
   * Get a list of all job creators that are able to create a job from
   * the specified {@link IGridJobDescription}. Searches the list returned
   * by {@link #getJobCreators()}.
   * 
   * @param description The job description from which to create a job.
   * @return All currently registered job managers that are able to create
   * a job from the specified description.
   * @see #getJobCreators()
   */
  public static List< IGridJobCreator > getJobCreators( final IGridJobDescription description ) {
    List< IGridJobCreator > resultList
      = new ArrayList< IGridJobCreator >();
    List<IGridJobCreator> creators = getJobCreators();
    for ( IGridJobCreator creator : creators ) {
      if ( creator.canCreate( description ) ) {
        resultList.add( creator );
      }
    }
    return resultList;
  }
  
  /**
   * Get the grid preferences.
   * 
   * @return An instance of {@link IGridPreferences}.
   * @throws ProblemException If the preferences could not be found
   * or the creation of the preferences failed.
   */
  public static IGridPreferences getPreferences() throws ProblemException {
    return HiddenProject.getInstance();
  }

  /**
   * Get all element creators that are able to create
   * {@link IVirtualOrganization}s.
   *  
   * @return A list of all {@link IGridElementCreator}s that
   * are able to create virtual organizations.
   * @see #getElementCreators(Class)
   */
  public static List< IGridElementCreator > getVoCreators() {
    return getElementCreators( IVirtualOrganization.class );
  }
  
  /**
   * Get the manager that is dedicated to the management of
   * {@link IVirtualOrganization}s.
   * 
   * @return The core implementation of the {@link IVoManager}
   * interface.
   */
  public static IVoManager getVoManager() {
    return VoManager.getManager();
  }
 
  /**
   * Add the specified {@link IGridModelListener} to the list of listeners,
   * which be notified about modifications in grid model. This static method is
   * used within constructors and class initializers, where cannot call
   * GridRoot.getInstance().
   * 
   * @see "bug 209160 for reason, why cannot call GridRoot.getInstance() within constructors"
   * @param listener
   */
  public static void addGridModelListener( final IGridModelListener listener ) {
    GridNotificationService.getInstance().addListener( listener );
  }  
}
