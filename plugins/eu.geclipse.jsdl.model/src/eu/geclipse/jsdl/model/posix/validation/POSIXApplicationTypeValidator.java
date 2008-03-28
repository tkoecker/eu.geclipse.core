/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package eu.geclipse.jsdl.model.posix.validation;

import eu.geclipse.jsdl.model.posix.DirectoryNameType;
import eu.geclipse.jsdl.model.posix.FileNameType;
import eu.geclipse.jsdl.model.posix.GroupNameType;
import eu.geclipse.jsdl.model.posix.LimitsType;
import eu.geclipse.jsdl.model.posix.UserNameType;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * A sample validator interface for {@link eu.geclipse.jsdl.model.posix.POSIXApplicationType}.
 * This doesn't really do anything, and it's not a real EMF artifact.
 * It was generated by the org.eclipse.emf.examples.generator.validator plug-in to illustrate how EMF's code generator can be extended.
 * This can be disabled with -vmargs -Dorg.eclipse.emf.examples.generator.validator=false.
 */
public interface POSIXApplicationTypeValidator
{
  boolean validate();

  boolean validateExecutable(FileNameType value);
  boolean validateArgument(EList value);
  boolean validateInput(FileNameType value);
  boolean validateOutput(FileNameType value);
  boolean validateError(FileNameType value);
  boolean validateWorkingDirectory(DirectoryNameType value);
  boolean validateEnvironment(EList value);
  boolean validateWallTimeLimit(LimitsType value);
  boolean validateFileSizeLimit(LimitsType value);
  boolean validateCoreDumpLimit(LimitsType value);
  boolean validateDataSegmentLimit(LimitsType value);
  boolean validateLockedMemoryLimit(LimitsType value);
  boolean validateMemoryLimit(LimitsType value);
  boolean validateOpenDescriptorsLimit(LimitsType value);
  boolean validatePipeSizeLimit(LimitsType value);
  boolean validateStackSizeLimit(LimitsType value);
  boolean validateCPUTimeLimit(LimitsType value);
  boolean validateProcessCountLimit(LimitsType value);
  boolean validateVirtualMemoryLimit(LimitsType value);
  boolean validateThreadCountLimit(LimitsType value);
  boolean validateUserName(UserNameType value);
  boolean validateGroupName(GroupNameType value);
  boolean validateName(String value);
  boolean validateAnyAttribute(FeatureMap value);
}
