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

import java.util.EventListener;

/**
 * Definition of an {@link EventListener} listening to {@link IGridModelEvent}s.
 */
public interface IGridModelListener
    extends EventListener {
  
  /**
   * The method is called everytime a change in the model occurred and the
   * event processing is active.
   * 
   * @param event The event that occurred in the model.
   */
  public void gridModelChanged( final IGridModelEvent event );
  
}
