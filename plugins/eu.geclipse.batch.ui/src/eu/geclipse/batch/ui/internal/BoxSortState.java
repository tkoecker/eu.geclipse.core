/*******************************************************************************
 * Copyright (c) 2008 g-Eclipse consortium All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Initial development of the original
 * code was made for project g-Eclipse founded by European Union project number:
 * FP6-IST-034327 http://www.geclipse.eu/ 
 *    Contributor(s): UCY (http://www.cs.ucy.ac.cy) 
 *        - Kyriakos Katsaris (kykatsar@gmail.com)
 ******************************************************************************/
package eu.geclipse.batch.ui.internal;

import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;

import eu.geclipse.batch.ui.internal.model.Box;
import eu.geclipse.batch.ui.internal.parts.BoxEditPart;
import eu.geclipse.batch.ui.internal.parts.BoxTreeEditPart;

public class BoxSortState extends SelectionAction {

  public static final String PROPERTY_SORT_BY_STATE = "BoxSortByState";//$NON-NLS-1$
  private boolean flag = false;

  public BoxSortState( final IWorkbenchPart part ) {
    super( part );
    this.setId( BoxSortState.PROPERTY_SORT_BY_STATE );
    this.setToolTipText( Messages.getString( "BoxSortByState" ) ); //$NON-NLS-1$
    this.setImageDescriptor( Activator.getDefault()
      .getImageRegistry()
      .getDescriptor( Activator.IMG_NEWQUEUE ) );
    this.setText( Messages.getString( "BoxSortByState" ) ); //$NON-NLS-1$
  }

  @Override
  protected boolean calculateEnabled() {
    boolean result;
    result = !getSelectedObjects().isEmpty();
    if( result ) {
      // Only one selected at a time
      if( 1 > getSelectedObjects().size() ) {
        result = false;
      } else {
        for( Object o : getSelectedObjects() ) {
          if( !( o instanceof BoxEditPart ) ) {
            result = false;
            break;
          }
          if( ( o instanceof BoxTreeEditPart ) ) {
            BoxTreeEditPart boxpart = ( BoxTreeEditPart )o;
            Box boxtemp = boxpart.getMod();
            this.flag = boxtemp.getIsNodes();
          } else if( ( o instanceof BoxEditPart ) ) {
            BoxEditPart boxpart = ( BoxEditPart )o;
            Box boxtemp = boxpart.getMod();
            this.flag = boxtemp.getIsNodes();
          }
        }
      }
    }
    return result;
  }

  @Override
  public void run() {
    if( !this.flag ) {
      BatchUpdate.sortedQ = 2;
      BatchUpdate.Qbyname = 2;
    } else {
      BatchUpdate.sortedN = 2;
    }
  }
}