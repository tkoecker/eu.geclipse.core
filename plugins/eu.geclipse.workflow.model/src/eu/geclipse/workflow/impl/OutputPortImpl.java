/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package eu.geclipse.workflow.impl;

import eu.geclipse.workflow.ILink;
import eu.geclipse.workflow.IOutputPort;
import eu.geclipse.workflow.IWorkflowNode;
import eu.geclipse.workflow.WorkflowPackage;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>IOutput Port</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link eu.geclipse.workflow.impl.IOutputPortImpl#getNode <em>Node</em>}</li>
 *   <li>{@link eu.geclipse.workflow.impl.IOutputPortImpl#getLinks <em>Links</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class OutputPortImpl extends PortImpl implements IOutputPort
{
  /**
   * The cached value of the '{@link #getLinks() <em>Links</em>}' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLinks()
   * @generated
   * @ordered
   */
  protected EList<ILink> links;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected OutputPortImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return WorkflowPackage.Literals.IOUTPUT_PORT;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IWorkflowNode getNode()
  {
    if (eContainerFeatureID != WorkflowPackage.IOUTPUT_PORT__NODE) return null;
    return (IWorkflowNode)eContainer();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetNode(IWorkflowNode newNode, NotificationChain msgs)
  {
    msgs = eBasicSetContainer((InternalEObject)newNode, WorkflowPackage.IOUTPUT_PORT__NODE, msgs);
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setNode(IWorkflowNode newNode)
  {
    if (newNode != eInternalContainer() || (eContainerFeatureID != WorkflowPackage.IOUTPUT_PORT__NODE && newNode != null))
    {
      if (EcoreUtil.isAncestor(this, newNode))
        throw new IllegalArgumentException("Recursive containment not allowed for " + toString()); //$NON-NLS-1$
      NotificationChain msgs = null;
      if (eInternalContainer() != null)
        msgs = eBasicRemoveFromContainer(msgs);
      if (newNode != null)
        msgs = ((InternalEObject)newNode).eInverseAdd(this, WorkflowPackage.IWORKFLOW_NODE__OUTPUTS, IWorkflowNode.class, msgs);
      msgs = basicSetNode(newNode, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, WorkflowPackage.IOUTPUT_PORT__NODE, newNode, newNode));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<ILink> getLinks()
  {
    if (links == null)
    {
      links = new EObjectWithInverseResolvingEList<ILink>(ILink.class, this, WorkflowPackage.IOUTPUT_PORT__LINKS, WorkflowPackage.ILINK__SOURCE);
    }
    return links;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case WorkflowPackage.IOUTPUT_PORT__NODE:
        if (eInternalContainer() != null)
          msgs = eBasicRemoveFromContainer(msgs);
        return basicSetNode((IWorkflowNode)otherEnd, msgs);
      case WorkflowPackage.IOUTPUT_PORT__LINKS:
        return ((InternalEList<InternalEObject>)(InternalEList<?>)getLinks()).basicAdd(otherEnd, msgs);
    }
    return super.eInverseAdd(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case WorkflowPackage.IOUTPUT_PORT__NODE:
        return basicSetNode(null, msgs);
      case WorkflowPackage.IOUTPUT_PORT__LINKS:
        return ((InternalEList<?>)getLinks()).basicRemove(otherEnd, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs)
  {
    switch (eContainerFeatureID)
    {
      case WorkflowPackage.IOUTPUT_PORT__NODE:
        return eInternalContainer().eInverseRemove(this, WorkflowPackage.IWORKFLOW_NODE__OUTPUTS, IWorkflowNode.class, msgs);
    }
    return super.eBasicRemoveFromContainerFeature(msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case WorkflowPackage.IOUTPUT_PORT__NODE:
        return getNode();
      case WorkflowPackage.IOUTPUT_PORT__LINKS:
        return getLinks();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case WorkflowPackage.IOUTPUT_PORT__NODE:
        setNode((IWorkflowNode)newValue);
        return;
      case WorkflowPackage.IOUTPUT_PORT__LINKS:
        getLinks().clear();
        getLinks().addAll((Collection<? extends ILink>)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case WorkflowPackage.IOUTPUT_PORT__NODE:
        setNode((IWorkflowNode)null);
        return;
      case WorkflowPackage.IOUTPUT_PORT__LINKS:
        getLinks().clear();
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case WorkflowPackage.IOUTPUT_PORT__NODE:
        return getNode() != null;
      case WorkflowPackage.IOUTPUT_PORT__LINKS:
        return links != null && !links.isEmpty();
    }
    return super.eIsSet(featureID);
  }

} //IOutputPortImpl
