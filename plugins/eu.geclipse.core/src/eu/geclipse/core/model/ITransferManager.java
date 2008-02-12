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
 * First definition of a transfer manager. This is not yet intended
 * for public use and may change in the near future.  
 */
public interface ITransferManager
    extends IGridElementManager, IStorableElementManager {
  
  /**
   * Add the specified transfers to the transfer queue.
   * 
   * @param sources The elements to be transfered.
   * @param target The destination of the transfer.
   * @throws GridModelException If any error occurs.
   */
  public void queueTransfer( final IGridElement[] sources,
                             final IGridContainer target )
    throws GridModelException;
  
}
