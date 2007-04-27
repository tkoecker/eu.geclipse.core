package eu.geclipse.core.internal.model;

import org.eclipse.core.resources.IProject;
import eu.geclipse.core.model.IGridContainer;
import eu.geclipse.core.model.IGridElement;
import eu.geclipse.core.model.IGridElementCreator;
import eu.geclipse.core.model.IGridProject;
import eu.geclipse.core.model.impl.AbstractGridElementCreator;

/**
 * Implementation of the {@link IGridElementCreator} interface
 * for projects in the workspace. These projects have not
 * to be grid projects.
 */
public class GridProjectCreator
    extends AbstractGridElementCreator {

  public boolean canCreate( final Class< ? extends IGridElement > elementType ) {
    return IGridProject.class.isAssignableFrom( elementType );
  }
  
  public IGridElement create( final IGridContainer parent ) {
    IProject project = ( IProject ) getObject();
    return new GridProject( project );
  }
  
  @Override
  public boolean internalCanCreate( final Object object ) {
    return ( object instanceof IProject );
  }

}
