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

import eu.geclipse.workflow.IOutputPort;
import eu.geclipse.workflow.IWorkflowPackage;
import java.util.Collection;
import java.util.List;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;

/**
 * This is the item provider adapter for a
 * {@link eu.geclipse.workflow.IOutputPort} object. <!-- begin-user-doc --> <!--
 * end-user-doc -->
 * 
 * @generated
 */
public class OutputPortItemProvider extends PortItemProvider
  implements IEditingDomainItemProvider, IStructuredItemContentProvider,
  ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource
{

  /**
   * This constructs an instance from a factory and a notifier. <!--
   * begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  public OutputPortItemProvider( AdapterFactory adapterFactory ) {
    super( adapterFactory );
  }

  /**
   * This returns the property descriptors for the adapted class. <!--
   * begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  @Override
  public List<IItemPropertyDescriptor> getPropertyDescriptors( Object object )
  {
    if( itemPropertyDescriptors == null ) {
      super.getPropertyDescriptors( object );
      addLinksPropertyDescriptor( object );
    }
    return itemPropertyDescriptors;
  }

  /**
   * This adds a property descriptor for the Links feature. <!-- begin-user-doc
   * --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected void addLinksPropertyDescriptor( Object object ) {
    itemPropertyDescriptors.add( createItemPropertyDescriptor( ( ( ComposeableAdapterFactory )adapterFactory ).getRootAdapterFactory(),
                                                               getResourceLocator(),
                                                               getString( "_UI_IOutputPort_links_feature" ),
                                                               getString( "_UI_PropertyDescriptor_description",
                                                                          "_UI_IOutputPort_links_feature",
                                                                          "_UI_IOutputPort_type" ),
                                                               IWorkflowPackage.Literals.IOUTPUT_PORT__LINKS,
                                                               true,
                                                               false,
                                                               true,
                                                               null,
                                                               null,
                                                               null ) );
  }

  /**
   * This returns IOutputPort.gif. <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  @Override
  public Object getImage( Object object )
  {
    return overlayImage( object,
                         getResourceLocator().getImage( "full/obj16/OutputPort" ) );
  }

  /**
   * This returns the label text for the adapted class. <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * 
   * @generated
   */
  @Override
  public String getText( Object object )
  {
    String label = ( ( IOutputPort )object ).getName();
    return label == null || label.length() == 0
                                               ? getString( "_UI_IOutputPort_type" )
                                               : getString( "_UI_IOutputPort_type" )
                                                 + " "
                                                 + label;
  }

  /**
   * This handles model notifications by calling {@link #updateChildren} to
   * update any cached children and by creating a viewer notification, which it
   * passes to {@link #fireNotifyChanged}. <!-- begin-user-doc --> <!--
   * end-user-doc -->
   * 
   * @generated
   */
  @Override
  public void notifyChanged( Notification notification )
  {
    updateChildren( notification );
    super.notifyChanged( notification );
  }

  /**
   * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
   * that can be created under this object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected void collectNewChildDescriptors( Collection<Object> newChildDescriptors,
                                             Object object )
  {
    super.collectNewChildDescriptors( newChildDescriptors, object );
  }

  /**
   * Return the resource locator for this item provider's resources.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public ResourceLocator getResourceLocator()
  {
    return WorkflowEditPlugin.INSTANCE;
  }
}