/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package eu.geclipse.jsdl.model.validation;

import eu.geclipse.jsdl.model.FileSystemTypeEnumeration;
import eu.geclipse.jsdl.model.RangeValueType;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * A sample validator interface for {@link eu.geclipse.jsdl.model.FileSystemType}.
 * This doesn't really do anything, and it's not a real EMF artifact.
 * It was generated by the org.eclipse.emf.examples.generator.validator plug-in to illustrate how EMF's code generator can be extended.
 * This can be disabled with -vmargs -Dorg.eclipse.emf.examples.generator.validator=false.
 */
public interface FileSystemTypeValidator
{
  boolean validate();

  boolean validateFileSystemType(FileSystemTypeEnumeration value);
  boolean validateDescription(String value);
  boolean validateMountPoint(String value);
  boolean validateDiskSpace(RangeValueType value);
  boolean validateAny(FeatureMap value);
  boolean validateName(String value);
  boolean validateAnyAttribute(FeatureMap value);
}