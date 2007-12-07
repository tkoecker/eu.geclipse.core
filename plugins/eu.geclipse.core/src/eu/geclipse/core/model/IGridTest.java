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
 *     PSNC: 
 *      - Katarzyna Bylec (katis@man.poznan.pl)
 *      - Szymon Mueller     
 *****************************************************************************/
package eu.geclipse.core.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Interface for structural tests and simple tests.
 */
public interface IGridTest extends IGridElement, IManageable {

  /**
   * Method to access all single tests that are run as a one IGridTest
   * implementation.
   * 
   * @return list of single tests names
   */
  public List<String> getSingleTestsNames();

  /**
   * Method to access names of tested resources.
   * 
   * @return List of names of tested resources
   */
  public List<String> getTestedResourcesNames();

  /**
   * Method to access dates of all submissions of this test
   * 
   * @return List of dates of test submission
   */
  public List<Date> getTestSubmissionDates();

  /**
   * Method to access text interpretation of single test for given single test's
   * name, tested resources and date of this test.
   * 
   * @param testName name of single test
   * @param resourceName name of tested resources
   * @param date date of test submission
   * @return String that is representation of single test result
   */
  public String getTestSingleTestResult( final String testName,
                                         final String resourceName,
                                         final Date date );

  /**
   * Short user friendly description of this test.
   * 
   * @return String with test's description
   */
  public String getTestDescription();

  /**
   * Method returning test name
   * 
   * @return String
   */
  public String getName();

  /**
   * Method returning result of the test - as a summary of single tests.
   * 
   * @return Test's result
   */
  public Object getResult();

  /**
   * Method returns all the data which needs to be viewed in properties view.
   * 
   * @return Map with property name as a key and its value as a value
   */
  public Map<String, String> getDescriptions();

  /**
   * Method to access date of last test submission
   * 
   * @return date of last test submission
   */
  public Date getLastUpdate();
}
