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
package eu.geclipse.batch.model.qdl;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Integer Boundary Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link eu.geclipse.batch.model.qdl.IntegerBoundaryType#getValue <em>Value</em>}</li>
 *   <li>{@link eu.geclipse.batch.model.qdl.IntegerBoundaryType#getExclusiveBound <em>Exclusive Bound</em>}</li>
 *   <li>{@link eu.geclipse.batch.model.qdl.IntegerBoundaryType#getAnyAttribute <em>Any Attribute</em>}</li>
 * </ul>
 * </p>
 *
 * @see eu.geclipse.batch.model.qdl.QdlPackage#getIntegerBoundaryType()
 * @model extendedMetaData="name='Integer_Boundary_Type' kind='simple'"
 * @generated
 */
public interface IntegerBoundaryType extends EObject
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
   * @see #isSetValue()
   * @see #unsetValue()
   * @see #setValue(int)
   * @see eu.geclipse.batch.model.qdl.QdlPackage#getIntegerBoundaryType_Value()
   * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Int"
   *        extendedMetaData="name=':0' kind='simple'"
   * @generated
   */
  int getValue();

  /**
   * Sets the value of the '{@link eu.geclipse.batch.model.qdl.IntegerBoundaryType#getValue <em>Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Value</em>' attribute.
   * @see #isSetValue()
   * @see #unsetValue()
   * @see #getValue()
   * @generated
   */
  void setValue(int value);

  /**
   * Unsets the value of the '{@link eu.geclipse.batch.model.qdl.IntegerBoundaryType#getValue <em>Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isSetValue()
   * @see #getValue()
   * @see #setValue(int)
   * @generated
   */
  void unsetValue();

  /**
   * Returns whether the value of the '{@link eu.geclipse.batch.model.qdl.IntegerBoundaryType#getValue <em>Value</em>}' attribute is set.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return whether the value of the '<em>Value</em>' attribute is set.
   * @see #unsetValue()
   * @see #getValue()
   * @see #setValue(int)
   * @generated
   */
  boolean isSetValue();

  /**
   * Returns the value of the '<em><b>Exclusive Bound</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Exclusive Bound</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Exclusive Bound</em>' attribute.
   * @see #isSetExclusiveBound()
   * @see #unsetExclusiveBound()
   * @see #setExclusiveBound(int)
   * @see eu.geclipse.batch.model.qdl.QdlPackage#getIntegerBoundaryType_ExclusiveBound()
   * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Int"
   *        extendedMetaData="kind='attribute' name='exclusiveBound'"
   * @generated
   */
  int getExclusiveBound();

  /**
   * Sets the value of the '{@link eu.geclipse.batch.model.qdl.IntegerBoundaryType#getExclusiveBound <em>Exclusive Bound</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Exclusive Bound</em>' attribute.
   * @see #isSetExclusiveBound()
   * @see #unsetExclusiveBound()
   * @see #getExclusiveBound()
   * @generated
   */
  void setExclusiveBound(int value);

  /**
   * Unsets the value of the '{@link eu.geclipse.batch.model.qdl.IntegerBoundaryType#getExclusiveBound <em>Exclusive Bound</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isSetExclusiveBound()
   * @see #getExclusiveBound()
   * @see #setExclusiveBound(int)
   * @generated
   */
  void unsetExclusiveBound();

  /**
   * Returns whether the value of the '{@link eu.geclipse.batch.model.qdl.IntegerBoundaryType#getExclusiveBound <em>Exclusive Bound</em>}' attribute is set.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return whether the value of the '<em>Exclusive Bound</em>' attribute is set.
   * @see #unsetExclusiveBound()
   * @see #getExclusiveBound()
   * @see #setExclusiveBound(int)
   * @generated
   */
  boolean isSetExclusiveBound();

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
   * @see eu.geclipse.batch.model.qdl.QdlPackage#getIntegerBoundaryType_AnyAttribute()
   * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
   *        extendedMetaData="kind='attributeWildcard' wildcards='##other' name=':2' processing='lax'"
   * @generated
   */
  FeatureMap getAnyAttribute();

} // IntegerBoundaryType
