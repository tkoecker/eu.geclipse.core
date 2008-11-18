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
package eu.geclipse.workflow.provider;

import eu.geclipse.workflow.util.WorkflowAdapterFactory;
import java.util.ArrayList;
import java.util.Collection;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.edit.provider.ChangeNotifier;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IChangeNotifier;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;

/**
 * This is the factory that is used to provide the interfaces needed to support
 * Viewers. The adapters generated by this factory convert EMF adapter
 * notifications into calls to {@link #fireNotifyChanged fireNotifyChanged}.
 * The adapters also support Eclipse property sheets. Note that most of the
 * adapters are shared among multiple instances.
 * 
 * @generated
 */
public class WorkflowItemProviderAdapterFactory extends WorkflowAdapterFactory
  implements ComposeableAdapterFactory, IChangeNotifier
{

  /**
   * This keeps track of the root adapter factory that delegates to this adapter factory.
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  protected ComposedAdapterFactory parentAdapterFactory;
  /**
   * This is used to implement {@link org.eclipse.emf.edit.provider.IChangeNotifier}.
   * <!-- begin-user-doc
   * --> <!-- end-user-doc -->
   * @generated
   */
  protected IChangeNotifier changeNotifier = new ChangeNotifier();
  /**
   * This keeps track of all the supported types checked by {@link #isFactoryForType isFactoryForType}.
   * <!-- begin-user-doc --> <!--
   * end-user-doc -->
   * @generated
   */
  protected Collection<Object> supportedTypes = new ArrayList<Object>();

  /**
   * This constructs an instance.
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public WorkflowItemProviderAdapterFactory() {
    supportedTypes.add(IEditingDomainItemProvider.class);
    supportedTypes.add(IStructuredItemContentProvider.class);
    supportedTypes.add(ITreeItemContentProvider.class);
    supportedTypes.add(IItemLabelProvider.class);
    supportedTypes.add(IItemPropertySource.class);
  }
  /**
   * This keeps track of the one adapter used for all {@link eu.geclipse.workflow.ILink} instances.
   * <!-- begin-user-doc --> <!--
   * end-user-doc -->
   * @generated
   */
  protected LinkItemProvider iLinkItemProvider;

  /**
   * This creates an adapter for a {@link eu.geclipse.workflow.model.ILink}. <!--
   * begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  @Override
  public Adapter createILinkAdapter() {
    if (iLinkItemProvider == null)
    {
      iLinkItemProvider = new LinkItemProvider(this);
    }

    return iLinkItemProvider;
  }
  /**
   * This keeps track of the one adapter used for all {@link eu.geclipse.workflow.IInputPort} instances.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected InputPortItemProvider iInputPortItemProvider;
  /**
   * This creates an adapter for a {@link eu.geclipse.workflow.IInputPort}.
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Adapter createIInputPortAdapter() {
    if (iInputPortItemProvider == null)
    {
      iInputPortItemProvider = new InputPortItemProvider(this);
    }

    return iInputPortItemProvider;
  }
  /**
   * This keeps track of the one adapter used for all {@link eu.geclipse.workflow.IOutputPort} instances.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected OutputPortItemProvider iOutputPortItemProvider;
  /**
   * This creates an adapter for a {@link eu.geclipse.workflow.IOutputPort}.
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Adapter createIOutputPortAdapter() {
    if (iOutputPortItemProvider == null)
    {
      iOutputPortItemProvider = new OutputPortItemProvider(this);
    }

    return iOutputPortItemProvider;
  }
  /**
   * This keeps track of the one adapter used for all {@link eu.geclipse.workflow.IWorkflow} instances.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected WorkflowItemProvider iWorkflowItemProvider;
  /**
   * This creates an adapter for a {@link eu.geclipse.workflow.model.IWorkflow}. <!--
   * begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  @Override
  public Adapter createIWorkflowAdapter() {
    if (iWorkflowItemProvider == null)
    {
      iWorkflowItemProvider = new WorkflowItemProvider(this);
    }

    return iWorkflowItemProvider;
  }
  /**
   * This keeps track of the one adapter used for all {@link eu.geclipse.workflow.IWorkflowJob} instances.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected WorkflowJobItemProvider iWorkflowJobItemProvider;
  /**
   * This creates an adapter for a {@link eu.geclipse.workflow.IWorkflowJob}.
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Adapter createIWorkflowJobAdapter() {
    if (iWorkflowJobItemProvider == null)
    {
      iWorkflowJobItemProvider = new WorkflowJobItemProvider(this);
    }

    return iWorkflowJobItemProvider;
  }

  /**
   * This returns the root adapter factory that contains this factory. <!--
   * begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  public ComposeableAdapterFactory getRootAdapterFactory() {
    return parentAdapterFactory == null ? this : parentAdapterFactory.getRootAdapterFactory();
  }

  /**
   * This sets the composed adapter factory that contains this factory. <!--
   * begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  public void setParentAdapterFactory( ComposedAdapterFactory parentAdapterFactory )
  {
    this.parentAdapterFactory = parentAdapterFactory;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean isFactoryForType( Object type ) {
    return supportedTypes.contains(type) || super.isFactoryForType(type);
  }

  /**
   * This implementation substitutes the factory itself as the key for the adapter.
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Adapter adapt( Notifier notifier, Object type ) {
    return super.adapt(notifier, this);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object adapt( Object object, Object type ) {
    if (isFactoryForType(type))
    {
      Object adapter = super.adapt(object, type);
      if (!(type instanceof Class) || (((Class<?>)type).isInstance(adapter)))
      {
        return adapter;
      }
    }

    return null;
  }

  /**
   * This adds a listener.
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public void addListener( INotifyChangedListener notifyChangedListener ) {
    changeNotifier.addListener(notifyChangedListener);
  }

  /**
   * This removes a listener.
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public void removeListener( INotifyChangedListener notifyChangedListener ) {
    changeNotifier.removeListener(notifyChangedListener);
  }

  /**
   * This delegates to {@link #changeNotifier} and to
   * {@link #parentAdapterFactory}. <!-- begin-user-doc --> <!-- end-user-doc
   * -->
   * 
   * @generated
   */
  public void fireNotifyChanged( Notification notification ) {
    changeNotifier.fireNotifyChanged(notification);

    if (parentAdapterFactory != null)
    {
      parentAdapterFactory.fireNotifyChanged(notification);
    }
  }
}
