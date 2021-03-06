/*****************************************************************************
 * Copyright (c) 2006-2008 g-Eclipse Consortium 
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
 *    Jie Tao - test class (Plug-in test)
 *****************************************************************************/

package eu.geclipse.core;

import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import eu.geclipse.core.auth.IAuthenticationTokenDescription;
import eu.geclipse.core.model.IGridElementCreator;

/** this is a test class for examining the functionalty of methods in 
 * class Extensions. 
 * @author tao-j
 *
 */

public class Extensions_PDETest {
  
  /**setup
   * @throws Exception
   */
  @Before
  public void setUp() throws Exception
  {
   // currently no setup
  }
  
  /** test the method {@link Extensions#getRegisteredAuthTokenNames()}
   * 
   */

  @Test
  public void testGetRegisteredAuthTokenNames()
  {
    List< String > tokens;
    tokens = Extensions.getRegisteredAuthTokenNames();
    Assert.assertNotNull( tokens );
  }
  
  /** test the method {@link Extensions#getRegisteredAuthTokenDescriptions()}
   * 
   */

  @Test
  public void testGetRegisteredAuthTokenDescriptions()
  {  
    List< IAuthenticationTokenDescription > autokendescriptions;
    autokendescriptions = Extensions.getRegisteredAuthTokenDescriptions();
    Assert.assertNotNull( autokendescriptions );
    // GridProxyDescription description;
    // File certFile = new File( "C:\\Dokumente und Einstellungen\\Tao-j\\Eigene Dateien\\usercert.pem" ); //$NON-NLS-1$
    // File keyFile = new File( "C:\\Dokumente und Einstellungen\\Tao-j\\Eigene Dateien\\private-key.pem" ); //$NON-NLS-1$
    // description = new GridProxyDescription( certFile, keyFile );
    // PasswordManager.registerPassword( keyFile.getPath(), "gamma" ); //$NON-NLS-1$
  }

}
