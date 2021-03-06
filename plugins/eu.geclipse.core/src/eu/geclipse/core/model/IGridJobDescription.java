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
 *    Pawel Wolniewicz
 *    
 *****************************************************************************/

package eu.geclipse.core.model;

import java.net.URI;
import java.util.List;

import eu.geclipse.core.reporting.ProblemException;

/**
 * A grid element that describes a job on the Grid. Such descriptions are
 * used to create and/or submit jobs to the Grid.
 */
public interface IGridJobDescription extends IGridContainer {
  
  /**
   * @return free text user's description for job 
   */
  String getDescription();
  
  /**
   * @return name of executable file executed by job
   */
  String getExecutable();
  
  /**
   * @return arguments for executable file
   */
  List<String> getExecutableArguments();
  
  /**
   * @return filename on computing element for standard input
   */
  String getStdInputFileName();
  
  /**
   * @return filename on computing element for standard output
   */
  String getStdOutputFileName();
  
  /**
   * @return filename on computing element for standard error
   */
  String getStdErrorFileName();  
  
  /**
   * @return {@link URI} to file containing standard output
   * or <code>null</code> if std output is not staged out
   * @throws ProblemException if URI cannot be created
   */
  URI getStdOutputUri() throws ProblemException;
  
  /**
   * @return {@link URI} to file containing standard input
   * or <code>null</code> if std input is not staged in
   * @throws ProblemException if URI cannot be created
   */
  URI getStdInputUri() throws ProblemException;
  
  /**
   * @return {@link URI} to file containing standard error
   * or <code>null</code> if std error is not staged out
   * @throws ProblemException if URI cannot be created
   */
  URI getStdErrorUri() throws ProblemException;
}
