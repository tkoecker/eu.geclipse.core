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

package eu.geclipse.core;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * Abstract implementation of the {@link IProblem} interface. 
 */
public abstract class AbstractProblem implements IProblem {
  
  /**
   * The exception that caused the problem or <code>null</code>.
   */
  private Throwable exception;
  
  /**
   * The ID of that problem.
   */
  private int id;
  
  /**
   * List of solutions.
   */
  private List< ISolution > solutions = new ArrayList< ISolution >();
  
  /**
   * List of solution IDs.
   */
  private List< Integer > solutionIDs = new ArrayList< Integer >();
  
  /**
   * A status object representing this problem to Eclipse.
   */
  private IStatus status;
  
  /**
   * The problem text.
   */
  private String text;
  
  /**
   * Additional descriptive text for this problem.
   */
  private String reason;
  
  /**
   * Construct a new problem with the specified ID and text.
   * 
   * @param id The ID of this problem.
   * @param text A descriptive text that describes this problem.
   */
  protected AbstractProblem( final int id,
                             final String text ) {
    this( id, text, null );
  }
  
  /**
   * Construct a new problem with the specified ID and text.
   * 
   * @param id The ID of this problem.
   * @param text A descriptive text that describes this problem.
   * @param exception An exception that caused this problem.
   */
  protected AbstractProblem( final int id,
                             final String text,
                             final Throwable exception ) {
    this.id = id;
    this.text = text;
    this.exception = exception;
  }
  
  /* (non-Javadoc)
   * @see eu.geclipse.core.IProblem#addSolution(int)
   */
  public void addSolution( final int solutionID ) {
    Integer value = new Integer( solutionID );
    if ( !this.solutionIDs.contains( value ) ) {
      this.solutionIDs.add( value );
    }
  }
  
  /* (non-Javadoc)
   * @see eu.geclipse.core.IProblem#addSolution(eu.geclipse.core.ISolution)
   */
  public void addSolution( final ISolution solution ) {
    ISolution oldSolution = findSolution( solution.getID() );
    if ( oldSolution != null ) {
      this.solutions.remove( oldSolution );
    }
    this.solutions.add( solution );
  }
  
  /* (non-Javadoc)
   * @see eu.geclipse.core.IProblem#getException()
   */
  public Throwable getException() {
    return this.exception;
  }

  /* (non-Javadoc)
   * @see eu.geclipse.core.IProblem#getID()
   */
  public int getID() {
    return this.id;
  }

  /* (non-Javadoc)
   * @see eu.geclipse.core.IProblem#getSolutions(eu.geclipse.core.SolutionRegistry)
   */
  public List< ISolution > getSolutions( final SolutionRegistry registry ) {
    List< ISolution > resultList = new ArrayList< ISolution >(); 
    for ( int i = 0 ; i < this.solutionIDs.size() ; i++ ) {
      int solutionID = this.solutionIDs.get( i ).intValue();
      ISolution solution = findSolution( solutionID );
      if ( solution == null ) {
        solution = registry.findSolution( solutionID );
      }
      if ( solution != null ) {
        resultList.add( solution );
      }
    }
    return resultList;
  }
  
  /* (non-Javadoc)
   * @see eu.geclipse.core.IProblem#setReason(java.lang.String)
   */
  public void setReason(final String reason){
    this.reason=reason;
  }
  
  /* (non-Javadoc)
   * @see eu.geclipse.core.IProblem#getStatus()
   */
  public IStatus getStatus() {
    if ( this.status == null ) {
      this.status = new Status(
        IStatus.ERROR,
        getPluginID(),
        getID(),
        getText(),
        getException()
      );
    }
    return this.status;
  }

  /* (non-Javadoc)
   * @see eu.geclipse.core.IProblem#getText()
   */
  public String getText() {
    String message = this.text;
    if( this.reason != null ){
      message += ": " + this.reason; //$NON-NLS-1$
    }
    return message;
  }
  
  /**
   * Get the plugin ID of this problem, i.e. the ID of the plugin where this
   * problem is defined.
   * 
   * @return The plugin ID of this problem.
   */
  protected abstract String getPluginID();
  
  /**
   * Get the solution with the specified ID from the list of solutions of this
   * problem.
   * 
   * @param solutionID The ID of the solution.
   * @return The solution with the specified ID or <code>null</code>.
   */
  private ISolution findSolution( final int solutionID ) {
    ISolution solution = null;
    for ( int i = 0 ; i < this.solutions.size() ; i++ ) {
      if ( this.solutions.get( i ).getID() == solutionID ) {
        solution = this.solutions.get( i );
        break;
      }
    }
    return solution;
  }
  
  
}
