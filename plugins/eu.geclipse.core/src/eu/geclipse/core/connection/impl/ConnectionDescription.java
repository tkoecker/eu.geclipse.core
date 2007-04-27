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
 *    Katarzyna Bylec - initial API and implementation
 *****************************************************************************/
package eu.geclipse.core.connection.impl;

import java.net.URI;
import eu.geclipse.core.connection.AbstractConnection;
import eu.geclipse.core.connection.AbstractConnectionDescription;

/**
 * Basic implementation of {@link AbstractConnectionDescription}
 * @author katis
 *
 */
public class ConnectionDescription extends AbstractConnectionDescription {
  
  /**
   * Creates and initializes new Connection description
   * @param fileSystemURI
   * @param project
   */
  public ConnectionDescription(final URI fileSystemURI, final String project)
  {
    this.fileSystemURI = fileSystemURI;
    this.projectName = project;
  }

  @Override
  public AbstractConnection createConnection(){
    return new Connection( this );
  }
  
}
