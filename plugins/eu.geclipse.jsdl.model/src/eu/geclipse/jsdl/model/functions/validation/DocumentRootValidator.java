/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package eu.geclipse.jsdl.model.functions.validation;

import eu.geclipse.jsdl.model.functions.LoopType;
import eu.geclipse.jsdl.model.functions.ValuesType;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * A sample validator interface for {@link eu.geclipse.jsdl.model.functions.DocumentRoot}.
 * This doesn't really do anything, and it's not a real EMF artifact.
 * It was generated by the org.eclipse.emf.examples.generator.validator plug-in to illustrate how EMF's code generator can be extended.
 * This can be disabled with -vmargs -Dorg.eclipse.emf.examples.generator.validator=false.
 */
public interface DocumentRootValidator
{
  boolean validate();

  boolean validateMixed(FeatureMap value);
  boolean validateXMLNSPrefixMap(EMap value);
  boolean validateXSISchemaLocation(EMap value);
  boolean validateLoop(LoopType value);
  boolean validateValues(ValuesType value);
}
