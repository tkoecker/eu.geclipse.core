/*****************************************************************************
 * Copyright (c) 2008 g-Eclipse Consortium 
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
 * An <code>IGridApplication</code> represents VO- and Computing specific
 * applications that are accessible to the user.
 */
public interface IGridApplication
    extends IGridResource {

  /** get the tag of the installed application
   * must return a string which represents the tag
   * this tag is given by the deployer in the process of install and is published
   * on the info system
   * @return String
   */
  public String getTag ();

}
