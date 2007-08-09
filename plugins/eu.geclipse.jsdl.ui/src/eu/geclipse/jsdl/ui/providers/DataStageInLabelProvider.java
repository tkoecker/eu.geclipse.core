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
 *     UCY (http://www.ucy.cs.ac.cy)
 *      - Nicholas Loulloudes (loulloudes.n@cs.ucy.ac.cy)
 *
 *****************************************************************************/
package eu.geclipse.jsdl.ui.providers;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import eu.geclipse.jsdl.model.DataStagingType;


/**
 * @author nickl
 *
 */
public class DataStageInLabelProvider extends LabelProvider
                                                  implements ITableLabelProvider
{

  public Image getColumnImage( final Object element, final int columnIndex ) {
    // TODO Auto-generated method stub
    return null;
  }

  public String getColumnText( final Object element, final int columnIndex ) {
    String text = null;    
    
    if (element instanceof DataStagingType){
      
      DataStagingType dataStagingType  = (DataStagingType) element;   
      
      if (dataStagingType.getSource() != null ){
        
      switch ( columnIndex ) {
        case 0:
            text = dataStagingType.getSource().getURI();          
          break;
        case 1:
          text = dataStagingType.getFileName();
          break;
        case 2:
          text = dataStagingType.getCreationFlag().getName();
          break;
        case 3:
          text = Boolean.toString(dataStagingType.isDeleteOnTermination());
          break;
        default:
          break;
        } // end switch
      
      } // end_if getSource()

    } // end_if dataStagingType
    
    return text;
    
  } // end_if element
  
}  // End Class


