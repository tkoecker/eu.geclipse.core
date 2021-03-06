/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package eu.geclipse.jsdl.model.base.impl;

import eu.geclipse.jsdl.model.base.CandidateHostsType;
import eu.geclipse.jsdl.model.base.JsdlPackage;

import java.util.Collection;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Candidate Hosts Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link eu.geclipse.jsdl.model.base.impl.CandidateHostsTypeImpl#getHostName <em>Host Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CandidateHostsTypeImpl extends EObjectImpl implements CandidateHostsType
{
  /**
   * The cached value of the '{@link #getHostName() <em>Host Name</em>}' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getHostName()
   * @generated
   * @ordered
   */
  protected EList hostName;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected CandidateHostsTypeImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected EClass eStaticClass()
  {
    return JsdlPackage.Literals.CANDIDATE_HOSTS_TYPE;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList getHostName()
  {
    if (hostName == null)
    {
      hostName = new EDataTypeEList(String.class, this, JsdlPackage.CANDIDATE_HOSTS_TYPE__HOST_NAME);
    }
    return hostName;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case JsdlPackage.CANDIDATE_HOSTS_TYPE__HOST_NAME:
        return getHostName();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case JsdlPackage.CANDIDATE_HOSTS_TYPE__HOST_NAME:
        getHostName().clear();
        getHostName().addAll((Collection)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case JsdlPackage.CANDIDATE_HOSTS_TYPE__HOST_NAME:
        getHostName().clear();
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case JsdlPackage.CANDIDATE_HOSTS_TYPE__HOST_NAME:
        return hostName != null && !hostName.isEmpty();
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String toString()
  {
    if (eIsProxy()) return super.toString();

    StringBuffer result = new StringBuffer(super.toString());
    result.append(" (hostName: ");
    result.append(hostName);
    result.append(')');
    return result.toString();
  }

} //CandidateHostsTypeImpl
