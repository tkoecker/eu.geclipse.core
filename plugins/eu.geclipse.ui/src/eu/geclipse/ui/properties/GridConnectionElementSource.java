/******************************************************************************
 * Copyright (c) 2007 g-Eclipse consortium 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Initial development of the original code was made for
 * project g-Eclipse founded by European Union
 * project number: FP6-IST-034327  http://www.geclipse.eu/
 *
 * Contributor(s):
 *     Mariusz Wojtysiak - initial API and implementation
 *     
 *****************************************************************************/
package eu.geclipse.ui.properties;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;

import eu.geclipse.core.model.IGridConnectionElement;
import eu.geclipse.ui.internal.Activator;


/**
 * Property source for IGridConnectionElement
 */
public class GridConnectionElementSource extends AbstractPropertySource<IGridConnectionElement> {
  static private List<IProperty<IGridConnectionElement>> staticProperties;

  /**
   * @param sourceObject connection object, which properties will be displayed
   */
  public GridConnectionElementSource( final IGridConnectionElement sourceObject ) {
    super( sourceObject );
    
    try {
      if( sourceObject.getConnectionFileInfo() != null ) {
        addChildSource( new FileInfoSource( sourceObject.getConnectionFileInfo() ) );
      }
    } catch ( CoreException cExc ) {
      // TODO Mariusz: Have a look if the error handling is ok
      Activator.logException( cExc );
    }
  }

  @Override
  protected Class<? extends AbstractPropertySource<?>> getPropertySourceClass()
  {
    return GridConnectionElementSource.class;
  }

  @Override
  protected List<IProperty<IGridConnectionElement>> getStaticProperties()
  {
    if( staticProperties == null ) {
      staticProperties = createProperties();
    }
    return staticProperties;
  }
  
  static private List<IProperty<IGridConnectionElement>> createProperties() {
    ArrayList<IProperty<IGridConnectionElement>> propertiesList = new ArrayList<IProperty<IGridConnectionElement>>();
    
    // properties for IGridConnectionElement are developed in child properties

    return propertiesList;
  }  
}
