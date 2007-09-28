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

package eu.geclipse.ui.providers;

/**
 * Interface for listeners that want to listen to changes of
 * {@link ConfigurableContentProvider}s.
 */
public interface IConfigurationListener {
  
  /**
   * Called whenever the mode of the {@link ConfigurableContentProvider}
   * changes.
   * 
   * @param source The {@link ConfigurableContentProvider} where the change
   * occured.
   */
  public void configurationChanged( final ConfigurableContentProvider source );
  
}