/*****************************************************************************
 * Copyright (c) 2006-2009 g-Eclipse Consortium
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
 *    Ariel Garcia      - updated to new problem reporting
 *****************************************************************************/

package eu.geclipse.core.model.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import eu.geclipse.core.ICoreProblems;
import eu.geclipse.core.auth.AbstractAuthTokenProvider;
import eu.geclipse.core.internal.Activator;
import eu.geclipse.core.internal.model.GridRoot;
import eu.geclipse.core.internal.model.notify.GridModelEvent;
import eu.geclipse.core.internal.model.notify.GridNotificationService;
import eu.geclipse.core.model.IGridContainer;
import eu.geclipse.core.model.IGridElement;
import eu.geclipse.core.model.IGridElementCreator;
import eu.geclipse.core.model.IGridElementManager;
import eu.geclipse.core.model.IGridModelEvent;
import eu.geclipse.core.model.IGridModelListener;
import eu.geclipse.core.model.IManageable;
import eu.geclipse.core.reporting.ProblemException;
import eu.geclipse.core.util.MasterMonitor;


/**
 * Base implementation of the {@link IGridContainer} interface that
 * implements basic functionalities of a grid container.
 */
public abstract class AbstractGridContainer
    extends AbstractGridElement
    implements IGridContainer {

  private static class ChildFetcher extends Job {

    private final AbstractGridContainer container;
    private IProgressMonitor externalMonitor;
    private Throwable exception;

    /**
     * true if this cancel was called for that fetcher but run() hasn't finished
     * yet
     */
    private boolean canceling;

    /**
     * Construct a new child fetcher for the specified container.
     *
     * @param container The container whose children should be fetched.
     */
    public ChildFetcher( final AbstractGridContainer container ) {
      super( "Child Fetcher @ " + container.getName() ); //$NON-NLS-1$
      this.container = container;
    }

    /**
     * Get an exception that occurred during child fetching or <code>null</code>
     * of no such exception occurred.
     *
     * @return The exception of <code>null</code> if either the fetcher did not
     *         yet run or no exception occurred.
     */
    public Throwable getException() {
      return this.exception;
    }

    /**
     * True if this fetcher has not yet run, i.e. it is currently scheduled, or
     * if it currently runs.
     *
     * @return True if the job has not yet finished.
     */
    public boolean isFetching() {
      return getState() != NONE;
    }

    /**
     * Set a progress monitor that is used in the run method in parallel with
     * the monitor provided by the run method parameter.
     *
     * @param monitor The external monitor.
     */
    public void setExternalMonitor( final IProgressMonitor monitor ) {
      this.externalMonitor = monitor;
    }

    /*
     * (non-Javadoc)
     * @seeorg.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.
     * IProgressMonitor)
     */
    @Override
    protected IStatus run( final IProgressMonitor monitor ) {
      IProgressMonitor mon = new MasterMonitor( monitor, this.externalMonitor );
      this.canceling = false;
      this.exception = null;
      this.container.lock();

      try {
        this.container.deleteAll();
        IStatus status = this.container.fetchChildren( mon );
        if( !status.isOK() ) {
          Throwable exc = status.getException();
          if ( ! AbstractAuthTokenProvider.isTokenRequestCanceledException( exc ) ) {
            this.exception = status.getException();
          }
        }
      } catch( Throwable t ) {
        this.exception = t;
      } finally {
        if( !this.canceling ) {
          this.container.setDirty( false );
        }
        this.container.unlock();
        this.canceling = false;
      }
      return Status.OK_STATUS;
    }

    @Override
    protected void canceling() {
      this.canceling = true;
      super.canceling();
    }

    public boolean isCanceling() {
      return this.canceling;
    }
  }

  /**
   * List of currently know children.
   */
  private final List< IGridElement > children
    = new ArrayList< IGridElement >();

  /**
   * Dirty flag of this container.
   */
  private boolean dirty;

  /**
   * Job used internally for fetching the containers children.
   */
  private ChildFetcher fetcher;

  protected AbstractGridContainer() {
    setDirty();
  }

  /* (non-Javadoc)
   * @see eu.geclipse.core.model.IGridContainer#canContain(eu.geclipse.core.model.IGridElement)
   */
  public boolean canContain( final IGridElement element ) {
    return false;
  }

  /* (non-Javadoc)
   * @see eu.geclipse.core.model.IGridContainer#contains(eu.geclipse.core.model.IGridElement)
   */
  public boolean contains( final IGridElement element ) {
    return this.children.contains( element  );
  }

  /* (non-Javadoc)
   * @see eu.geclipse.core.model.IGridContainer#create(eu.geclipse.core.model.IGridElementCreator)
   */
  public IGridElement create( final IGridElementCreator creator )
      throws ProblemException {
    IGridElement element = creator.create( this );
    element = addElement( element );
    return element;
  }

  /* (non-Javadoc)
   * @see eu.geclipse.core.model.IGridContainer#delete(eu.geclipse.core.model.IGridElement)
   */
  public void delete( final IGridElement child )
      throws ProblemException {
    removeElement( child );
    unregisterFromManager( child );
    child.dispose();
  }

  private void unregisterFromManager( final IGridElement child ) {
    if ( child instanceof IManageable ) {
      IGridElementManager manager
        = ( ( IManageable ) child ).getManager();
      manager.removeElement( child );
    }
  }

  /* (non-Javadoc)
   * @see eu.geclipse.core.model.impl.AbstractGridElement#dispose()
   */
  @Override
  public void dispose() {
    deleteAll();
    super.dispose();
  }

  /* (non-Javadoc)
   * @see eu.geclipse.core.model.IGridContainer#getChildCount()
   */
  public int getChildCount() {
    int result;
    if ( isLazy() && isDirty() ) {
      result = 1;
    } else {
      result = this.children.size();
    }
    return result;
  }

  /* (non-Javadoc)
   * @see eu.geclipse.core.model.IGridContainer#getChildren(org.eclipse.core.runtime.IProgressMonitor)
   */
  public IGridElement[] getChildren( final IProgressMonitor monitor )
      throws ProblemException {
    if ( isLazy() && isDirty() ) {
      try {
        startFetch( monitor );
      } catch ( Throwable t ) {
        if ( t instanceof ProblemException ) {
          throw ( ProblemException ) t;
        }
        throw new ProblemException( ICoreProblems.MODEL_FETCH_CHILDREN_FAILED, t, Activator.PLUGIN_ID );
      }
    }
    return this.children.toArray( new IGridElement[ this.children.size() ] );
  }

  /* (non-Javadoc)
   * @see eu.geclipse.core.model.IGridContainer#hasChildren()
   */
  public boolean hasChildren() {
    return isLazy() || !this.children.isEmpty();
  }

  /* (non-Javadoc)
   * @see eu.geclipse.core.model.IGridContainer#findChild(java.lang.String)
   */
  public IGridElement findChild( final String name ) {
    IGridElement result = null;
    for ( IGridElement child : this.children ) {
      if ( child.getName().equals( name ) ) {
        result = child;
        break;
      }
    }
    return result;
  }

  /* (non-Javadoc)
   * @see eu.geclipse.core.model.IGridContainer#findChildWithResource(java.lang.String)
   */
  public IGridElement findChildWithResource( final String resourceName ) {
    IGridElement result = null;
    for ( IGridElement child : this.children ) {
      if ( !child.isVirtual() ) {
        if ( child.getResource().getName().equals( resourceName ) ) {
          result = child;
          break;
        }
      }
    }
    return result;
  }

  /* (non-Javadoc)
   * @see eu.geclipse.core.model.IGridContainer#isDirty()
   */
  public boolean isDirty() {
    return this.dirty;
  }

  public void refresh( final IProgressMonitor monitor )
      throws ProblemException {

    if ( ! isVirtual() ) {
      IContainer container = ( IContainer ) getResource();
      try {
        lock();
        container.refreshLocal( IResource.DEPTH_INFINITE, monitor );
      } catch( CoreException cExc ) {
        throw new ProblemException( ICoreProblems.MODEL_REFRESH_FAILED,
            cExc,
            Activator.PLUGIN_ID );
      } finally {
        unlock();
      }
    }

    else {
      setDirty();
      try {
        startFetch( monitor );
      } catch ( Throwable t ) {
        if ( t instanceof ProblemException ) {
          throw ( ProblemException ) t;
        }
        throw new ProblemException( ICoreProblems.MODEL_REFRESH_FAILED,
            t,
            Activator.PLUGIN_ID );
      }
    }

  }

  /* (non-Javadoc)
   * @see eu.geclipse.core.model.IGridContainer#setDirty()
   */
  public void setDirty() {
    setDirty( true );
  }

  /**
   * Add an element as child to this container. If a child with the same
   * name is already contained in this contained this old child will
   * be deleted.
   *
   * @param element The new child of this container or <code>null</code>
   * if an error occurs.
   * @return The newly added element.
   */
  protected IGridElement addElement( final IGridElement element )
      throws ProblemException {

    if ( element != null ) {

      testCanContain( element );
      IGridElement oldChild = findChild( element.getName() );
      if ( oldChild != null ) {
        delete( oldChild );
      }
      this.children.add( element );
      GridRoot.registerElement( element );
      fireGridModelEvent( IGridModelEvent.ELEMENTS_ADDED, element );

      if ( isLazy() && ! ( element instanceof ContainerMarker ) ) {
        for ( IGridElement child : this.children ) {
          if ( child instanceof ContainerMarker ) {
            removeElement( child );
            break;
          }
        }
      }

    }

    return element;

  }

  /**
   * Remove all children from this container and call their
   * {@link #dispose()} methods.
   */
  public void deleteAll() {
    if ( this.children != null && !this.children.isEmpty() ) {
      for ( IGridElement child : this.children ) {
        unregisterFromManager( child );
        child.dispose();
      }
      IGridElement[] elements
        = this.children.toArray( new IGridElement[ this.children.size() ] );
      fireGridModelEvent( IGridModelEvent.ELEMENTS_REMOVED, elements );
      this.children.clear();
    }
  }

  /**
   * Fetch the children of this container. For a non-lazy container
   * the children are fetched when the container is constructed. For
   * lazy containers the children are fetched by the
   * {@link #getChildren(IProgressMonitor)} method if the container is
   * dirty.
   *
   * @param monitor A progress monitor to monitor the progress of this
   * maybe long running method.
   * @return True if the operation was successful.
   */
  @SuppressWarnings("unused")
  protected IStatus fetchChildren( final IProgressMonitor monitor )
      throws ProblemException {
    return Status.OK_STATUS;
  }

  protected void removeElement( final IGridElement element )
      throws ProblemException {
    boolean result = this.children.remove( element );
    if ( result ) {
      fireGridModelEvent( IGridModelEvent.ELEMENTS_REMOVED, element );
      if ( this.children.isEmpty() && isLazy() && ! ( element instanceof ContainerMarker ) ) {
        ContainerMarker marker = ContainerMarker.getEmptyFolderMarker( this );
        if ( canContain( marker ) ) {
          addElement( marker );
        }
      }
    }
  }

  /**
   * Set the dirty flag of this container. If setting to dirty, the
   * flags of all child containers are also set recursively to dirty.
   *
   * @param d The new value of the container's dirty flag.
   */
  protected void setDirty( final boolean d ) {
    this.dirty = d;
    if ( d ) {
      if ( this.fetcher != null && this.fetcher.isFetching() ) {
        this.fetcher.cancel();
      }
      List< IGridElement > synchronizedList = Collections.synchronizedList( this.children );
      synchronized ( synchronizedList ) {
        for ( IGridElement child : synchronizedList ) {
          if ( child instanceof IGridContainer ) {
            ( ( IGridContainer ) child ).setDirty();
          }
        }
      }
    }
  }

  protected void lock() {
    getGridNotificationService().lock( this );
  }

  protected void unlock() {
    getGridNotificationService().unlock( this );
  }

  protected void fireGridModelEvent( final int type,
                                   final IGridElement element ) {
    fireGridModelEvent( type, new IGridElement[] { element } );
  }

  protected void fireGridModelEvent( final int type,
                                   final IGridElement[] elements ) {
    if ( elements != null && elements.length > 0 ) {
      IGridModelEvent event = new GridModelEvent( type, this, elements );
      getGridNotificationService().queueEvent( event );
    }
  }

  static private GridNotificationService getGridNotificationService() {
    return GridNotificationService.getInstance();
  }

  /**
   * To register IGridModelListener within constructor or static method, I cannot call GridRoot.getInstance().
   * For reason @see bug #209160
   * So instead of GridRoot, this method is used to register IGridModelListener
   * @param listener
   */
  static protected void staticAddGridModelListener( final IGridModelListener listener ) {
    getGridNotificationService().addListener( listener );
  }

  private void startFetch( final IProgressMonitor monitor )
      throws Throwable {

    if ( this.fetcher == null ) {
      this.fetcher = new ChildFetcher( this );
    }

    this.fetcher.setExternalMonitor( monitor );

    // if canceling, then schedule again (don't wait for finish cancelation - scheduler start job again).
    if ( ! this.fetcher.isFetching()
        || this.fetcher.isCanceling() ) {
      this.fetcher.schedule();
    }

    try {
      this.fetcher.join();
    } catch ( InterruptedException intExc ) {
      // Silently ignored
    }

    Throwable exc = this.fetcher.getException();

    if ( exc != null ) {
      throw exc;
    }
  }

  /**
   * Test if this container can contain the specified element
   * and throw a {@link ProblemException} if this is not the
   * case.
   *
   * @param element The element to be tested.
   * @throws ProblemException Thrown if {@link #canContain(IGridElement)}
   * returns false for the specified element.
   */
  private void testCanContain( final IGridElement element )
      throws ProblemException {
    if ( ! canContain( element ) ) {
      throw new ProblemException( ICoreProblems.MODEL_CONTAINER_CAN_NOT_CONTAIN,
          String.format(
              Messages.getString("AbstractGridContainer.can_not_contain_error"), //$NON-NLS-1$
              getClass().getName(), element.getClass().getName() ),
          Activator.PLUGIN_ID );
    }
  }

  /**
   * @return get current children elements. This method do not
   *         fetchChildren, so for dirty container it return dirty (old)
   *         children, for lazy container it may return empty list (even if
   *         children really exists but weren't fetched yet).
   */
  protected List<IGridElement> getCachedChildren() {
    return this.children;
  }
}
