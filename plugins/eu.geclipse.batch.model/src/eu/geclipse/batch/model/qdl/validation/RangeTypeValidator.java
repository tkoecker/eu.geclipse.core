/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package eu.geclipse.batch.model.qdl.validation;

import eu.geclipse.batch.model.qdl.BoundaryType;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * A sample validator interface for {@link eu.geclipse.batch.model.qdl.RangeType}.
 * This doesn't really do anything, and it's not a real EMF artifact.
 * It was generated by the org.eclipse.emf.examples.generator.validator plug-in to illustrate how EMF's code generator can be extended.
 * This can be disabled with -vmargs -Dorg.eclipse.emf.examples.generator.validator=false.
 */
public interface RangeTypeValidator
{
  boolean validate();

  boolean validateLowerBound(BoundaryType value);
  boolean validateUpperBound(BoundaryType value);
  boolean validateAnyAttribute(FeatureMap value);
}