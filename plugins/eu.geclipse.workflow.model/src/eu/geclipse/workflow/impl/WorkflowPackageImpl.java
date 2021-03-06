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

import eu.geclipse.workflow.model.IInputPort;
import eu.geclipse.workflow.model.ILink;
import eu.geclipse.workflow.model.IOutputPort;
import eu.geclipse.workflow.model.IPort;
import eu.geclipse.workflow.model.IWorkflow;
import eu.geclipse.workflow.model.IWorkflowElement;
import eu.geclipse.workflow.model.IWorkflowFactory;
import eu.geclipse.workflow.model.IWorkflowJob;
import eu.geclipse.workflow.model.IWorkflowNode;
import eu.geclipse.workflow.model.IWorkflowPackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Package</b>. <!--
 * end-user-doc -->
 * @generated
 */
public class WorkflowPackageImpl extends EPackageImpl
  implements IWorkflowPackage
{

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  private EClass iPortEClass = null;
  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  private EClass iLinkEClass = null;
  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  private EClass iInputPortEClass = null;
  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  private EClass iOutputPortEClass = null;
  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  private EClass iWorkflowEClass = null;
  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  private EClass iWorkflowJobEClass = null;
  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  private EClass iWorkflowElementEClass = null;
  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  private EClass iWorkflowNodeEClass = null;

  /**
   * Creates an instance of the model <b>Package</b>, registered with
   * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the
   * package package URI value.
   * <p>
   * Note: the correct way to create the package is via the static factory
   * method {@link #init init()}, which also performs initialization of the
   * package, or returns the registered package, if one already exists. <!--
   * begin-user-doc --> <!-- end-user-doc -->
   * 
   * @see org.eclipse.emf.ecore.EPackage.Registry
   * @see eu.geclipse.workflow.WorkflowPackage#eNS_URI
   * @see #init()
   * @generated
   */
  private WorkflowPackageImpl() {
    super(eNS_URI, IWorkflowFactory.eINSTANCE);
  }
  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  private static boolean isInited = false;

  /**
   * Creates, registers, and initializes the <b>Package</b> for this
   * model, and for any others upon which it depends.  Simple
   * dependencies are satisfied by calling this method on all
   * dependent packages before doing anything else.  This method drives
   * initialization for interdependent packages directly, in parallel
   * with this package, itself.
   * <p>Of this package and its interdependencies, all packages which
   * have not yet been registered by their URI values are first created
   * and registered.  The packages are then initialized in two steps:
   * meta-model objects for all of the packages are created before any
   * are initialized, since one package's meta-model objects may refer to
   * those of another.
   * <p>Invocation of this method will not affect any packages that have
   * already been initialized.
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @see #eNS_URI
   * @see #createPackageContents()
   * @see #initializePackageContents()
   * @generated
   */
  public static IWorkflowPackage init() {
    if (isInited) return (IWorkflowPackage)EPackage.Registry.INSTANCE.getEPackage(IWorkflowPackage.eNS_URI);

    // Obtain or create and register package
    WorkflowPackageImpl theWorkflowPackage = (WorkflowPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(eNS_URI) instanceof WorkflowPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(eNS_URI) : new WorkflowPackageImpl());

    isInited = true;

    // Create package meta-data objects
    theWorkflowPackage.createPackageContents();

    // Initialize created meta-data
    theWorkflowPackage.initializePackageContents();

    // Mark meta-data to indicate it can't be changed
    theWorkflowPackage.freeze();

    return theWorkflowPackage;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public EClass getIPort() {
    return iPortEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getIPort_FileName()
  {
    return (EAttribute)iPortEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public EClass getILink() {
    return iLinkEClass;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public EReference getILink_Workflow() {
    return (EReference)iLinkEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public EReference getILink_Target() {
    return (EReference)iLinkEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public EReference getILink_Source() {
    return (EReference)iLinkEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public EClass getIInputPort() {
    return iInputPortEClass;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public EReference getIInputPort_Node() {
    return (EReference)iInputPortEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public EReference getIInputPort_Links() {
    return (EReference)iInputPortEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public EClass getIOutputPort() {
    return iOutputPortEClass;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public EReference getIOutputPort_Node() {
    return (EReference)iOutputPortEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public EReference getIOutputPort_Links() {
    return (EReference)iOutputPortEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public EClass getIWorkflow() {
    return iWorkflowEClass;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public EReference getIWorkflow_Nodes() {
    return (EReference)iWorkflowEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public EReference getIWorkflow_Links() {
    return (EReference)iWorkflowEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public EClass getIWorkflowJob() {
    return iWorkflowJobEClass;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getIWorkflowJob_JobDescription() {
    return (EAttribute)iWorkflowJobEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getIWorkflowJob_JobDescriptionFileName() {
    return (EAttribute)iWorkflowJobEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public EClass getIWorkflowElement() {
    return iWorkflowElementEClass;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getIWorkflowElement_Name() {
    return (EAttribute)iWorkflowElementEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getIWorkflowElement_Id() {
    return (EAttribute)iWorkflowElementEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public EClass getIWorkflowNode() {
    return iWorkflowNodeEClass;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public EReference getIWorkflowNode_Workflow() {
    return (EReference)iWorkflowNodeEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public EReference getIWorkflowNode_Outputs() {
    return (EReference)iWorkflowNodeEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public EReference getIWorkflowNode_Inputs() {
    return (EReference)iWorkflowNodeEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getIWorkflowNode_IsStart() {
    return (EAttribute)iWorkflowNodeEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getIWorkflowNode_IsFinish() {
    return (EAttribute)iWorkflowNodeEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public IWorkflowFactory getWorkflowFactory() {
    return (IWorkflowFactory)getEFactoryInstance();
  }
  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  private boolean isCreated = false;

  /**
   * Creates the meta-model objects for the package.  This method is
   * guarded to have no affect on any invocation but its first.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void createPackageContents() {
    if (isCreated) return;
    isCreated = true;

    // Create classes and their features
    iPortEClass = createEClass(IPORT);
    createEAttribute(iPortEClass, IPORT__FILE_NAME);

    iLinkEClass = createEClass(ILINK);
    createEReference(iLinkEClass, ILINK__WORKFLOW);
    createEReference(iLinkEClass, ILINK__TARGET);
    createEReference(iLinkEClass, ILINK__SOURCE);

    iInputPortEClass = createEClass(IINPUT_PORT);
    createEReference(iInputPortEClass, IINPUT_PORT__NODE);
    createEReference(iInputPortEClass, IINPUT_PORT__LINKS);

    iOutputPortEClass = createEClass(IOUTPUT_PORT);
    createEReference(iOutputPortEClass, IOUTPUT_PORT__NODE);
    createEReference(iOutputPortEClass, IOUTPUT_PORT__LINKS);

    iWorkflowEClass = createEClass(IWORKFLOW);
    createEReference(iWorkflowEClass, IWORKFLOW__NODES);
    createEReference(iWorkflowEClass, IWORKFLOW__LINKS);

    iWorkflowJobEClass = createEClass(IWORKFLOW_JOB);
    createEAttribute(iWorkflowJobEClass, IWORKFLOW_JOB__JOB_DESCRIPTION);
    createEAttribute(iWorkflowJobEClass, IWORKFLOW_JOB__JOB_DESCRIPTION_FILE_NAME);

    iWorkflowElementEClass = createEClass(IWORKFLOW_ELEMENT);
    createEAttribute(iWorkflowElementEClass, IWORKFLOW_ELEMENT__NAME);
    createEAttribute(iWorkflowElementEClass, IWORKFLOW_ELEMENT__ID);

    iWorkflowNodeEClass = createEClass(IWORKFLOW_NODE);
    createEReference(iWorkflowNodeEClass, IWORKFLOW_NODE__WORKFLOW);
    createEReference(iWorkflowNodeEClass, IWORKFLOW_NODE__OUTPUTS);
    createEReference(iWorkflowNodeEClass, IWORKFLOW_NODE__INPUTS);
    createEAttribute(iWorkflowNodeEClass, IWORKFLOW_NODE__IS_START);
    createEAttribute(iWorkflowNodeEClass, IWORKFLOW_NODE__IS_FINISH);
  }
  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  private boolean isInitialized = false;

  /**
   * Complete the initialization of the package and its meta-model. This method
   * is guarded to have no affect on any invocation but its first. <!--
   * begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  public void initializePackageContents() {
    if (isInitialized) return;
    isInitialized = true;

    // Initialize package
    setName(eNAME);
    setNsPrefix(eNS_PREFIX);
    setNsURI(eNS_URI);

    // Create type parameters

    // Set bounds for type parameters

    // Add supertypes to classes
    iPortEClass.getESuperTypes().add(this.getIWorkflowElement());
    iLinkEClass.getESuperTypes().add(this.getIWorkflowElement());
    iInputPortEClass.getESuperTypes().add(this.getIPort());
    iOutputPortEClass.getESuperTypes().add(this.getIPort());
    iWorkflowEClass.getESuperTypes().add(this.getIWorkflowElement());
    iWorkflowJobEClass.getESuperTypes().add(this.getIWorkflowNode());
    iWorkflowNodeEClass.getESuperTypes().add(this.getIWorkflowElement());

    // Initialize classes and features; add operations and parameters
    initEClass(iPortEClass, IPort.class, "IPort", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(getIPort_FileName(), ecorePackage.getEString(), "fileName", null, 0, 1, IPort.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

    initEClass(iLinkEClass, ILink.class, "ILink", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEReference(getILink_Workflow(), this.getIWorkflow(), this.getIWorkflow_Links(), "workflow", null, 1, 1, ILink.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEReference(getILink_Target(), this.getIInputPort(), this.getIInputPort_Links(), "target", null, 1, 1, ILink.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEReference(getILink_Source(), this.getIOutputPort(), this.getIOutputPort_Links(), "source", null, 1, 1, ILink.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

    initEClass(iInputPortEClass, IInputPort.class, "IInputPort", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEReference(getIInputPort_Node(), this.getIWorkflowNode(), this.getIWorkflowNode_Inputs(), "node", null, 1, 1, IInputPort.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEReference(getIInputPort_Links(), this.getILink(), this.getILink_Target(), "links", null, 0, -1, IInputPort.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

    initEClass(iOutputPortEClass, IOutputPort.class, "IOutputPort", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEReference(getIOutputPort_Node(), this.getIWorkflowNode(), this.getIWorkflowNode_Outputs(), "node", null, 1, 1, IOutputPort.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEReference(getIOutputPort_Links(), this.getILink(), this.getILink_Source(), "links", null, 0, -1, IOutputPort.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

    initEClass(iWorkflowEClass, IWorkflow.class, "IWorkflow", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEReference(getIWorkflow_Nodes(), this.getIWorkflowNode(), this.getIWorkflowNode_Workflow(), "nodes", null, 0, -1, IWorkflow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEReference(getIWorkflow_Links(), this.getILink(), this.getILink_Workflow(), "links", null, 0, -1, IWorkflow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

    initEClass(iWorkflowJobEClass, IWorkflowJob.class, "IWorkflowJob", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(getIWorkflowJob_JobDescription(), ecorePackage.getEString(), "jobDescription", null, 1, 1, IWorkflowJob.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEAttribute(getIWorkflowJob_JobDescriptionFileName(), ecorePackage.getEString(), "jobDescriptionFileName", null, 1, 1, IWorkflowJob.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

    initEClass(iWorkflowElementEClass, IWorkflowElement.class, "IWorkflowElement", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(getIWorkflowElement_Name(), ecorePackage.getEString(), "name", null, 0, 1, IWorkflowElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEAttribute(getIWorkflowElement_Id(), ecorePackage.getEString(), "id", null, 1, 1, IWorkflowElement.class, !IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

    initEClass(iWorkflowNodeEClass, IWorkflowNode.class, "IWorkflowNode", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEReference(getIWorkflowNode_Workflow(), this.getIWorkflow(), this.getIWorkflow_Nodes(), "workflow", null, 1, 1, IWorkflowNode.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEReference(getIWorkflowNode_Outputs(), this.getIOutputPort(), this.getIOutputPort_Node(), "outputs", null, 1, -1, IWorkflowNode.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEReference(getIWorkflowNode_Inputs(), this.getIInputPort(), this.getIInputPort_Node(), "inputs", null, 1, -1, IWorkflowNode.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEAttribute(getIWorkflowNode_IsStart(), ecorePackage.getEBoolean(), "isStart", "false", 1, 1, IWorkflowNode.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$
    initEAttribute(getIWorkflowNode_IsFinish(), ecorePackage.getEBoolean(), "isFinish", "false", 1, 1, IWorkflowNode.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$

    // Create resource
    createResource(eNS_URI);
  }
} // WorkflowPackageImpl
