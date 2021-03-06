/*******************************************************************************
 * Copyright (c) 2006, 2007 g-Eclipse Consortium 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Initial development of the original code was made for the g-Eclipse project 
 * funded by European Union project number: FP6-IST-034327 
 * http://www.geclipse.eu/
 *  
 * Contributors:
 *     RUR (http://acet.rdg.ac.uk/)
 *     - Ashish Thandavan - initial API and implementation
 ******************************************************************************/
package eu.geclipse.workflow.impl;

import eu.geclipse.workflow.model.ILink;
import eu.geclipse.workflow.model.IWorkflow;
import eu.geclipse.workflow.model.IWorkflowNode;
import eu.geclipse.workflow.model.IWorkflowPackage;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>IWorkflow</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link eu.geclipse.workflow.impl.IWorkflowImpl#getNodes <em>Nodes</em>}</li>
 * <li>{@link eu.geclipse.workflow.impl.IWorkflowImpl#getLinks <em>Links</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class WorkflowImpl extends WorkflowElementImpl implements IWorkflow {

  /**
   * The cached value of the '{@link #getNodes() <em>Nodes</em>}' containment
   * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @see #getNodes()
   * @generated
   * @ordered
   */
  protected EList<IWorkflowNode> nodes;
  /**
   * The cached value of the '{@link #getLinks() <em>Links</em>}' containment
   * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @see #getLinks()
   * @generated
   * @ordered
   */
  protected EList<ILink> links;

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected WorkflowImpl() {
    super();
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  @Override
  protected EClass eStaticClass() {
    return IWorkflowPackage.Literals.IWORKFLOW;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  public EList<IWorkflowNode> getNodes() {
    if( this.nodes == null ) {
      this.nodes = new EObjectContainmentWithInverseEList<IWorkflowNode>( IWorkflowNode.class,
                                                                          this,
                                                                          IWorkflowPackage.IWORKFLOW__NODES,
                                                                          IWorkflowPackage.IWORKFLOW_NODE__WORKFLOW );
    }
    return this.nodes;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  public EList<ILink> getLinks() {
    if( this.links == null ) {
      this.links = new EObjectContainmentWithInverseEList<ILink>( ILink.class,
                                                                  this,
                                                                  IWorkflowPackage.IWORKFLOW__LINKS,
                                                                  IWorkflowPackage.ILINK__WORKFLOW );
    }
    return this.links;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public NotificationChain eInverseAdd( InternalEObject otherEnd,
                                        int featureID,
                                        NotificationChain msgs )
  {
    switch( featureID ) {
      case IWorkflowPackage.IWORKFLOW__NODES:
        return ( ( InternalEList<InternalEObject> )( InternalEList<?> )getNodes() ).basicAdd( otherEnd,
                                                                                              msgs );
      case IWorkflowPackage.IWORKFLOW__LINKS:
        return ( ( InternalEList<InternalEObject> )( InternalEList<?> )getLinks() ).basicAdd( otherEnd,
                                                                                              msgs );
    }
    return super.eInverseAdd( otherEnd, featureID, msgs );
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove( InternalEObject otherEnd,
                                           int featureID,
                                           NotificationChain msgs )
  {
    switch( featureID ) {
      case IWorkflowPackage.IWORKFLOW__NODES:
        return ( ( InternalEList<?> )getNodes() ).basicRemove( otherEnd, msgs );
      case IWorkflowPackage.IWORKFLOW__LINKS:
        return ( ( InternalEList<?> )getLinks() ).basicRemove( otherEnd, msgs );
    }
    return super.eInverseRemove( otherEnd, featureID, msgs );
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  @Override
  public Object eGet( int featureID, boolean resolve, boolean coreType ) {
    switch( featureID ) {
      case IWorkflowPackage.IWORKFLOW__NODES:
        return getNodes();
      case IWorkflowPackage.IWORKFLOW__LINKS:
        return getLinks();
    }
    return super.eGet( featureID, resolve, coreType );
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public void eSet( int featureID, Object newValue ) {
    switch( featureID ) {
      case IWorkflowPackage.IWORKFLOW__NODES:
        getNodes().clear();
        getNodes().addAll( ( Collection<? extends IWorkflowNode> )newValue );
        return;
      case IWorkflowPackage.IWORKFLOW__LINKS:
        getLinks().clear();
        getLinks().addAll( ( Collection<? extends ILink> )newValue );
        return;
    }
    super.eSet( featureID, newValue );
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  @Override
  public void eUnset( int featureID ) {
    switch( featureID ) {
      case IWorkflowPackage.IWORKFLOW__NODES:
        getNodes().clear();
        return;
      case IWorkflowPackage.IWORKFLOW__LINKS:
        getLinks().clear();
        return;
    }
    super.eUnset( featureID );
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet( int featureID ) {
    switch( featureID ) {
      case IWorkflowPackage.IWORKFLOW__NODES:
        return this.nodes != null && !this.nodes.isEmpty();
      case IWorkflowPackage.IWORKFLOW__LINKS:
        return this.links != null && !this.links.isEmpty();
    }
    return super.eIsSet( featureID );
  }
} //IWorkflowImpl
