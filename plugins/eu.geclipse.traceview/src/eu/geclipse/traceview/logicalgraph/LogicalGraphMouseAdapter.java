/*****************************************************************************
 * Copyright (c) 2006, 2008 g-Eclipse Consortium 
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
 *    Christof Klausecker GUP, JKU - initial API and implementation
 *****************************************************************************/

package eu.geclipse.traceview.logicalgraph;

import eu.geclipse.traceview.ILamportProcess;
import eu.geclipse.traceview.internal.AbstractGraphMouseAdapter;

/**
 * A MouseAdapter which handles the mouse events on the LogicalGraph and sets
 * the according selection in the SelectionProvider.
 */
public class LogicalGraphMouseAdapter extends AbstractGraphMouseAdapter {
  /**
   * Creates a new EventGraphMouseAdapter
   *
   * @param eventGraph
   */
  LogicalGraphMouseAdapter( final LogicalGraph logicalGraph ) {
    super(logicalGraph);
  }

  public Object getObjectForPosition( final int xPos, final int yPos ) {
    Object obj = null;
    // horizontal and vertical space between events
    int hSpace = this.graph.getEventGraphPaintListener().getHSpace();
    int vSpace = this.graph.getEventGraphPaintListener().getVSpace();
    // scrollbar values
    int hSelection = this.graph.getHorizontalBar().getSelection();
    int vSelection = this.graph.getVerticalBar().getSelection();
    // first displayed clock and process
    int firstClock = hSelection / hSpace;
    int firstProc = vSelection / vSpace;
    // x and y offset
    int xOffset = hSelection % hSpace - hSpace / 2;
    int yOffset = vSelection % vSpace - vSpace / 2;
    // area get mouse events from
    int graphWidth = this.graph.getClientArea().width;
    int graphHeight = this.graph.getClientArea().height - 30;
    int x = -1;
    int y = -1;
    if( xPos > 30 && yPos > 0 && xPos < graphWidth && yPos < graphHeight ) {
      x = ( xPos
            + xOffset
            - 30
            - this.graph.getEventGraphPaintListener().getEventSize()
            / 2 + hSpace / 2 )
          / hSpace
          + firstClock;
      y = ( yPos
            + yOffset
            - this.graph.getEventGraphPaintListener().getEventSize()
            / 2 + vSpace / 2 )
          / vSpace
          + firstProc;
      if( Math.abs( xPos
                    - ( ( x - firstClock ) * hSpace + 30 - xOffset + this.graph.getEventGraphPaintListener()
                      .getEventSize() / 2 ) ) > this.graph.getEventGraphPaintListener()
        .getEventSize() / 2 )
      {
        x = -1;
      }
      if( Math.abs( yPos
                    - ( ( y - firstProc ) * vSpace - yOffset + this.graph.getEventGraphPaintListener()
                      .getEventSize() / 2 ) ) > this.graph.getEventGraphPaintListener()
        .getEventSize() / 2 )
      {
        y = -1;
      }
    }
    if( y < this.graph.getTrace().getNumberOfProcesses() ) {
      if( x != -1 && y != -1 ) {
        obj = ( ( ILamportProcess )this.graph.getTrace().getProcess( y ) ).getEventByLamportClock( x );
      }
      if( obj == null && y != -1 ) {
        obj = this.graph.getTrace().getProcess( y );
      }
    }
    if( obj == null )
      obj = this.graph.getTrace();
    return obj;
  }
}
