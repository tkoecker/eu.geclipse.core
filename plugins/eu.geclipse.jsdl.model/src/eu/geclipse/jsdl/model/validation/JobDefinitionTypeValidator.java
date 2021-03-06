/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package eu.geclipse.jsdl.model.validation;

import eu.geclipse.jsdl.model.JobDescriptionType;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * A sample validator interface for {@link eu.geclipse.jsdl.model.JobDefinitionType}.
 * This doesn't really do anything, and it's not a real EMF artifact.
 * It was generated by the org.eclipse.emf.examples.generator.validator plug-in to illustrate how EMF's code generator can be extended.
 * This can be disabled with -vmargs -Dorg.eclipse.emf.examples.generator.validator=false.
 * @deprecated This interface is deprecated. Substitute with the respective class in package eu.geclipse.jsdl.model.base.validation
 */
public interface JobDefinitionTypeValidator
{
  boolean validate();

  boolean validateJobDescription(JobDescriptionType value);
  boolean validateAny(FeatureMap value);
  boolean validateId(String value);
  boolean validateAnyAttribute(FeatureMap value);
}
