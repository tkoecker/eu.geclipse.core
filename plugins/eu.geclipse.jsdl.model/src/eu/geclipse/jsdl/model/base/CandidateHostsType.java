/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package eu.geclipse.jsdl.model.base;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Candidate Hosts Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link eu.geclipse.jsdl.model.base.CandidateHostsType#getHostName <em>Host Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see eu.geclipse.jsdl.model.base.JsdlPackage#getCandidateHostsType()
 * @model extendedMetaData="name='CandidateHosts_Type' kind='elementOnly'"
 * @generated
 */
public interface CandidateHostsType extends EObject
{
  /**
   * Returns the value of the '<em><b>Host Name</b></em>' attribute list.
   * The list contents are of type {@link java.lang.String}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Host Name</em>' attribute list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Host Name</em>' attribute list.
   * @see eu.geclipse.jsdl.model.base.JsdlPackage#getCandidateHostsType_HostName()
   * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
   *        extendedMetaData="kind='element' name='HostName' namespace='##targetNamespace'"
   * @generated
   */
  EList getHostName();

} // CandidateHostsType
