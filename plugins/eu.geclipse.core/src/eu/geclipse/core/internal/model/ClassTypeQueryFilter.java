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

package eu.geclipse.core.internal.model;

import java.util.ArrayList;

import eu.geclipse.core.model.IGridElement;
import eu.geclipse.core.model.IWrappedElement;

/**
 * An {@link IQueryFilter} that filters Grid elements according
 * to their class type. Elements are filtered by using
 * {@link Class#isAssignableFrom(Class)}.
 */
public class ClassTypeQueryFilter implements IQueryFilter {
  
  /**
   * The class type to be applied.
   */
  private Class< ? > classType;
  
  /**
   * Flag that determines if elements of the class type
   * are allowed or not.
   */
  private boolean allow;
  
  /**
   * Create a new filter for the specified class type.
   * 
   * @param classType The class type used to filter the elements.
   * @param allow If <code>true</code> elements of the specified class
   * type are not filtered out, if <code>false</code> only elements of
   * the specified class type are filtered out.
   */
  public ClassTypeQueryFilter( final Class< ? > classType,
                               final boolean allow ) {
    this.classType = classType;
    this.allow = allow;
  }

  /* (non-Javadoc)
   * @see eu.geclipse.core.internal.model.IQueryFilter#filter(eu.geclipse.core.model.IGridElement[])
   */
  public IGridElement[] filter( final IGridElement[] input ) {
    
    java.util.List< IGridElement > result = new ArrayList< IGridElement >();
    
    if ( input != null ) {
    
      for ( IGridElement element : input ) {
        IGridElement matcher
          = element instanceof IWrappedElement 
          ? ( ( IWrappedElement ) element ).getWrappedElement()
          : element;
        if ( this.allow
            && this.classType.isAssignableFrom( matcher.getClass() ) ) {
          result.add( element );
        } else if ( ! this.allow
            && ! this.classType.isAssignableFrom( matcher.getClass() ) ) {
          result.add( element );
        }
      }
      
    }
    
    return result.toArray( new IGridElement[ result.size() ] );
    
  }

}
