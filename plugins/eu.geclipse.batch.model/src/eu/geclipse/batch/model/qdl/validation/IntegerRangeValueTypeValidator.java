/******************************************************************************
 * Copyright (c) 2008 g-Eclipse consortium
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
 *     UCY (http://www.cs.ucy.ac.cy)
 *      - Nicholas Loulloudes (loulloudes.n@cs.ucy.ac.cy)
 *
  *****************************************************************************/
package eu.geclipse.batch.model.qdl.validation;

import eu.geclipse.batch.model.qdl.IntegerBoundaryType;
import eu.geclipse.batch.model.qdl.IntegerExactType;
import eu.geclipse.batch.model.qdl.IntegerRangeType;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * A sample validator interface for {@link eu.geclipse.batch.model.qdl.IntegerRangeValueType}.
 * This doesn't really do anything, and it's not a real EMF artifact.
 * It was generated by the org.eclipse.emf.examples.generator.validator plug-in to illustrate 
 * how EMF's code generator can be extended.
 * This can be disabled with -vmargs -Dorg.eclipse.emf.examples.generator.validator=false.
 */
public interface IntegerRangeValueTypeValidator
{
  boolean validate();

  boolean validateUpperBoundedRange(IntegerBoundaryType value);
  boolean validateLowerBoundedRange(IntegerBoundaryType value);
  boolean validateExact(EList<IntegerExactType> value);
  boolean validateRange(EList<IntegerRangeType> value);
  boolean validateAnyAttribute(FeatureMap value);
}
