/**
 * <copyright>
 * </copyright>
 *
 * $Id: UserNameTypeImpl.java,v 1.2 2007/03/01 09:15:17 emstamou Exp $
 */
package eu.geclipse.jsdl.model.posix.impl;

import eu.geclipse.jsdl.model.posix.PosixPackage;
import eu.geclipse.jsdl.model.posix.UserNameType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>User Name Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link eu.geclipse.jsdl.model.posix.impl.UserNameTypeImpl#getValue <em>Value</em>}</li>
 *   <li>{@link eu.geclipse.jsdl.model.posix.impl.UserNameTypeImpl#getAnyAttribute <em>Any Attribute</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class UserNameTypeImpl extends EObjectImpl implements UserNameType 
{
  /**
   * The default value of the '{@link #getValue() <em>Value</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getValue()
   * @generated
   * @ordered
   */
	protected static final String VALUE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getValue() <em>Value</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getValue()
   * @generated
   * @ordered
   */
	protected String value = VALUE_EDEFAULT;

  /**
   * The cached value of the '{@link #getAnyAttribute() <em>Any Attribute</em>}' attribute list.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getAnyAttribute()
   * @generated
   * @ordered
   */
	protected FeatureMap anyAttribute = null;

  /**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	protected UserNameTypeImpl()
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
    return PosixPackage.Literals.USER_NAME_TYPE;
  }

  /**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getValue()
  {
    return value;
  }

  /**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setValue(String newValue)
  {
    String oldValue = value;
    value = newValue;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, PosixPackage.USER_NAME_TYPE__VALUE, oldValue, value));
  }

  /**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public FeatureMap getAnyAttribute()
  {
    if (anyAttribute == null)
    {
      anyAttribute = new BasicFeatureMap(this, PosixPackage.USER_NAME_TYPE__ANY_ATTRIBUTE);
    }
    return anyAttribute;
  }

  /**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case PosixPackage.USER_NAME_TYPE__ANY_ATTRIBUTE:
        return ((InternalEList)getAnyAttribute()).basicRemove(otherEnd, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
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
      case PosixPackage.USER_NAME_TYPE__VALUE:
        return getValue();
      case PosixPackage.USER_NAME_TYPE__ANY_ATTRIBUTE:
        if (coreType) return getAnyAttribute();
        return ((FeatureMap.Internal)getAnyAttribute()).getWrapper();
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
      case PosixPackage.USER_NAME_TYPE__VALUE:
        setValue((String)newValue);
        return;
      case PosixPackage.USER_NAME_TYPE__ANY_ATTRIBUTE:
        ((FeatureMap.Internal)getAnyAttribute()).set(newValue);
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
      case PosixPackage.USER_NAME_TYPE__VALUE:
        setValue(VALUE_EDEFAULT);
        return;
      case PosixPackage.USER_NAME_TYPE__ANY_ATTRIBUTE:
        getAnyAttribute().clear();
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
      case PosixPackage.USER_NAME_TYPE__VALUE:
        return VALUE_EDEFAULT == null ? value != null : !VALUE_EDEFAULT.equals(value);
      case PosixPackage.USER_NAME_TYPE__ANY_ATTRIBUTE:
        return anyAttribute != null && !anyAttribute.isEmpty();
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
    result.append(" (value: ");
    result.append(value);
    result.append(", anyAttribute: ");
    result.append(anyAttribute);
    result.append(')');
    return result.toString();
  }

} //UserNameTypeImpl