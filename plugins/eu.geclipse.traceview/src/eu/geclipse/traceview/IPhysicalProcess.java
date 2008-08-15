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

package eu.geclipse.traceview;

/**
 * Interface for a process containing physical clock events.
 */
public interface IPhysicalProcess extends IProcess {

  /**
   * Returns the maximum physical clock for this process.
   * 
   * @return the maximum physical clock.
   */
  int getMaximumPhysicalClock();
  
  /**
   * Returns the Events occurring between the start and the stop time
   * 
   * @param startClock
   * @param stopClock
   * @return events
   */
  IPhysicalEvent[] getEventsByPhysicalClock( int startClock, int stopClock );
}
