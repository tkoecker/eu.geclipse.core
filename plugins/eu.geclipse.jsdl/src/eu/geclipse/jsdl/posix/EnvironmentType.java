/**
 * <copyright>
 * </copyright>
 *
 * $Id: EnvironmentType.java,v 1.2 2007/03/01 09:15:16 emstamou Exp $
 */
package eu.geclipse.jsdl.posix;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Environment Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link eu.geclipse.jsdl.posix.EnvironmentType#getValue <em>Value</em>}</li>
 *   <li>{@link eu.geclipse.jsdl.posix.EnvironmentType#getFilesystemName <em>Filesystem Name</em>}</li>
 *   <li>{@link eu.geclipse.jsdl.posix.EnvironmentType#getName <em>Name</em>}</li>
 *   <li>{@link eu.geclipse.jsdl.posix.EnvironmentType#getAnyAttribute <em>Any Attribute</em>}</li>
 * </ul>
 * </p>
 *
 * @see eu.geclipse.jsdl.posix.PosixPackage#getEnvironmentType()
 * @model extendedMetaData="name='Environment_Type' kind='simple'"
 * @generated
 */
public interface EnvironmentType extends EObject
{
  /**
   * Returns the value of the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Value</em>' attribute.
   * @see #setValue(String)
   * @see eu.geclipse.jsdl.posix.PosixPackage#getEnvironmentType_Value()
   * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
   *        extendedMetaData="name=':0' kind='simple'"
   * @generated
   */
	String getValue();

  /**
   * Sets the value of the '{@link eu.geclipse.jsdl.posix.EnvironmentType#getValue <em>Value</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Value</em>' attribute.
   * @see #getValue()
   * @generated
   */
	void setValue(String value);

  /**
   * Returns the value of the '<em><b>Filesystem Name</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Filesystem Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Filesystem Name</em>' attribute.
   * @see #setFilesystemName(String)
   * @see eu.geclipse.jsdl.posix.PosixPackage#getEnvironmentType_FilesystemName()
   * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.NCName"
   *        extendedMetaData="kind='attribute' name='filesystemName'"
   * @generated
   */
	String getFilesystemName();

  /**
   * Sets the value of the '{@link eu.geclipse.jsdl.posix.EnvironmentType#getFilesystemName <em>Filesystem Name</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Filesystem Name</em>' attribute.
   * @see #getFilesystemName()
   * @generated
   */
	void setFilesystemName(String value);

  /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see eu.geclipse.jsdl.posix.PosixPackage#getEnvironmentType_Name()
   * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.NCName" required="true"
   *        extendedMetaData="kind='attribute' name='name'"
   * @generated
   */
	String getName();

  /**
   * Sets the value of the '{@link eu.geclipse.jsdl.posix.EnvironmentType#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
	void setName(String value);

  /**
   * Returns the value of the '<em><b>Any Attribute</b></em>' attribute list.
   * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Any Attribute</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Any Attribute</em>' attribute list.
   * @see eu.geclipse.jsdl.posix.PosixPackage#getEnvironmentType_AnyAttribute()
   * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
   *        extendedMetaData="kind='attributeWildcard' wildcards='##other' name=':3' processing='lax'"
   * @generated
   */
	FeatureMap getAnyAttribute();

} // EnvironmentType