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
 *****************************************************************************/

package eu.geclipse.core.model.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import eu.geclipse.core.model.IGridContainer;
import eu.geclipse.core.model.IGridInfoService;
import eu.geclipse.core.model.IGridResource;
import eu.geclipse.core.model.IGridResourceCategory;
import eu.geclipse.core.model.IVirtualOrganization;
import eu.geclipse.core.reporting.ProblemException;

/**
 * Abstract core implementation of {@link IGridInfoService}.
 */
public abstract class AbstractGridInfoService
    extends AbstractGridElement
    implements IGridInfoService {
  
  /* (non-Javadoc)
   * @see eu.geclipse.core.model.IGridInfoService#fetchResources(eu.geclipse.core.model.IGridContainer, eu.geclipse.core.model.IVirtualOrganization, eu.geclipse.core.model.IGridResourceCategory, org.eclipse.core.runtime.IProgressMonitor)
   */
  public IGridResource[] fetchResources( final IGridContainer parent,
                                         final IVirtualOrganization vo,
                                         final IGridResourceCategory category,
                                         final IProgressMonitor monitor ) throws ProblemException {
    return fetchResources( parent, vo, category, false, null, monitor );
  }
  
  protected IGridResource[] filterResources( final IGridResource[] resources,
                                             final Class< ? extends IGridResource > type,
                                             final boolean remove ) {

    List< IGridResource > resultList = new ArrayList< IGridResource >();

    for ( IGridResource resource : resources ) {
      boolean isType = type.isAssignableFrom( resource.getClass() );
      if ( ( remove && ! isType ) || ( ! remove && isType ) ) {
        resultList.add( resource );
      }
    }

    return resultList.toArray( new IGridResource[ resultList.size() ] );
  }
}
