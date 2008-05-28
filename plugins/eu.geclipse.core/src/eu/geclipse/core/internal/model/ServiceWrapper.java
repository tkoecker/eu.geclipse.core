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
 *    Harald Gjermundrod - added the getHostName method
 *****************************************************************************/

package eu.geclipse.core.internal.model;

import java.net.URI;

import eu.geclipse.core.model.IGridContainer;
import eu.geclipse.core.model.IGridElement;
import eu.geclipse.core.model.IGridService;

public class ServiceWrapper
    extends WrappedElement
    implements IGridService {

  /**
   * The argument constructor for this class.
   * @param parent The container for this service.
   * @param wrapped The wrapper that holds this service.
   */
  public ServiceWrapper( final IGridContainer parent,
                         final IGridElement wrapped ) {
    super( parent, wrapped );
  }

  /*
   * (non-Javadoc)
   * 
   * @see eu.geclipse.core.model.getURI#getURI()
   */
  public URI getURI() {
    return ( ( IGridService ) getWrappedElement() ).getURI();
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see eu.geclipse.core.model.IGridResource#getHostName()
   */
  public String getHostName() {
    return ( ( IGridService ) getWrappedElement() ).getHostName();
  }
}