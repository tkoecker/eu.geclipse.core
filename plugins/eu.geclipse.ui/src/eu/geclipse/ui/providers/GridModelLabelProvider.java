/*****************************************************************************
 * Copyright (c) 2006, 2007 g-Eclipse Consortium 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Initial development of the original code was made for the
 * g-Eclipse project founded by European Union
 * project number: FP6-IST-034327  http://www.geclipse.eu/
 *
 * Contributors:
 *    Mathias Stuempert - initial API and implementation
 *    Nikolaos Tsioutsias
 *****************************************************************************/

package eu.geclipse.ui.providers;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.model.WorkbenchLabelProvider;

import eu.geclipse.core.model.IGridComputing;
import eu.geclipse.core.model.IGridConnectionElement;
import eu.geclipse.core.model.IGridContainer;
import eu.geclipse.core.model.IGridElement;
import eu.geclipse.core.model.IGridJob;
import eu.geclipse.core.model.IGridJobDescription;
import eu.geclipse.core.model.IGridService;
import eu.geclipse.core.model.IGridStorage;
import eu.geclipse.core.model.IVirtualOrganization;
import eu.geclipse.info.model.GridGlueService;
import eu.geclipse.ui.internal.Activator;

/**
 * Label provider implementation to be used by any Grid model view.
 */
public class GridModelLabelProvider
    extends LabelProvider
    implements ILabelProviderListener {

  private Image computingImage;
  
  private Image jobImage;
  
  private Image jobDescriptionImage;
  
  private Image serviceImage;
  
  private Image serviceUnsupportedImage;
  
  private Image storageImage;
  
  private Image voImage;
  
  private Image virtualContainerImage;
  
  private Image virtualElementImage;
  
  private ILabelProvider workbenchLabelProvider
    = WorkbenchLabelProvider.getDecoratingWorkbenchLabelProvider();
  
  private ILabelDecorator labelDecorator
    = PlatformUI.getWorkbench().getDecoratorManager().getLabelDecorator();

  /**
   * Construct a new <code>GridModelLabelProvider</code>.
   */
  public GridModelLabelProvider() {
    this.workbenchLabelProvider.addListener( this );
  }

  /* (non-Javadoc)
   * @see org.eclipse.jface.viewers.BaseLabelProvider#dispose()
   */
  @Override
  public void dispose() {
    this.workbenchLabelProvider.removeListener( this );
    super.dispose();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
   */
  @Override
  public Image getImage( final Object element ) {
    
    Image result = null;
    
    if ( element instanceof IGridElement ) {
      result = getImage( ( IGridElement ) element );
    } else {
      result = super.getImage( element );
    }
    
    return result;
    
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.jface.viewers.ILabelProviderListener#labelProviderChanged(org.eclipse.jface.viewers.LabelProviderChangedEvent)
   */
  public void labelProviderChanged( final LabelProviderChangedEvent event ) {
    // TODO find the grid element to the IRessource for only updating necessary
    // nodes
    fireLabelProviderChanged( new LabelProviderChangedEvent( this ) );
  }

  /* (non-Javadoc)
   * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
   */
  @Override
  public String getText( final Object element ) {
    
    String resultText = null;
    
    if( element instanceof IGridElement ) {
      resultText = ( ( IGridElement )element ).getName();
      IResource resource = ( ( IGridElement )element ).getResource();
      if( ( resource != null ) && ( resource.getProject() != null ) ) {
        resultText = this.labelDecorator.decorateText( resultText, element );
      }
    } else if( element instanceof ProgressTreeNode ) {
      resultText = element.toString();
    }
    
    if( element instanceof IGridConnectionElement ) {
      IGridConnectionElement connection = ( IGridConnectionElement )element;
      if( !connection.isValid() ) {
        resultText += "(Error: " + connection.getError() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
      }
    }
    
    return resultText;
    
  }
  
  private Image getImage( final IGridElement element ) {

    Image result = null;
    
    if ( element.isVirtual() ) {
      result = getVirtualElementImage( element );
    } else {
      result = getRealElementImage( element );
    }
    
    return result;
    
  }
  
  private Image getRealElementImage( final IGridElement element ) {
    
    Image result = null;
    
    if ( element instanceof IGridJob ) {
      result = getJobImage();
    } else if ( element instanceof IGridJobDescription ) {
      result = getJobDescriptionImage();
    } else {
      IResource resource = element.getResource();
      result = this.workbenchLabelProvider.getImage( resource );
    }
    
    return result;
    
  }
  
  private Image getVirtualElementImage( final IGridElement element ) {
    
    Image result = null;
    
    if( element instanceof IVirtualOrganization ) {
      result = getVoImage();
    } else if( element instanceof IGridComputing ) {
      result = getComputingImage();
    } else if( element instanceof IGridStorage ) {
      result = getStorageImage();
    } else if( element instanceof IGridService ) {
      if ( element instanceof GridGlueService ) {
        boolean isSupported = ( ( GridGlueService ) element ).getGlueService().isSupported().booleanValue();
        if ( isSupported ) {
          result = getServiceImage();
        } else {
          result = getUnsupportedServiceImage();
        }
      } else {
        result = getServiceImage();
      }
    } else if ( element instanceof IGridContainer ) {
      result = getVirtualContainerImage();
    } else {
      result = getVirtualElementImage();
    }
    
    return result;
    
  }
  
  private Image getJobImage() {
    if( this.jobImage == null ) {
      this.jobImage = Activator.getDefault().getImageRegistry().get( "job" ); //$NON-NLS-1$
    }
    return this.jobImage;
  }
  
  private Image getJobDescriptionImage() {
    if( this.jobDescriptionImage == null ) {
      this.jobDescriptionImage = Activator.getDefault().getImageRegistry().get( "jobdescription" ); //$NON-NLS-1$
    }
    return this.jobDescriptionImage;
  }
  
  private Image getVirtualContainerImage() {
    if( this.virtualContainerImage == null ) {
      this.virtualContainerImage = Activator.getDefault().getImageRegistry().get( "virtualfolder" ); //$NON-NLS-1$
    }
    return this.virtualContainerImage;
  }
  
  private Image getVirtualElementImage() {
    if( this.virtualElementImage == null ) {
      this.virtualElementImage = Activator.getDefault().getImageRegistry().get( "virtualfile" ); //$NON-NLS-1$
    }
    return this.virtualElementImage;
  }

  /**
   * Get an image that represents a virtual organisation.
   * 
   * @return The image for a VO.
   */
  private Image getVoImage() {
    if( this.voImage == null ) {
      this.voImage = Activator.getDefault().getImageRegistry().get( "vo" ); //$NON-NLS-1$
    }
    return this.voImage;
  }

  /**
   * Get an image that represents an {@link IGridService}.
   * 
   * @return The image for a Grid service.
   */
  private Image getServiceImage() {
    if( this.serviceImage == null ) {
      this.serviceImage = Activator.getDefault()
        .getImageRegistry()
        .get( "service" ); //$NON-NLS-1$
    }
    return this.serviceImage;
  }
  
  private Image getUnsupportedServiceImage() {
    if( this.serviceUnsupportedImage == null ) {
      this.serviceUnsupportedImage = Activator.getDefault()
        .getImageRegistry()
        .get( "service_unsupported" ); //$NON-NLS-1$
    }
    return this.serviceUnsupportedImage;
  }

  /**
   * Get an image that represents an {@link IGridStorage}.
   * 
   * @return The image for a Grid storage.
   */
  private Image getStorageImage() {
    if( this.storageImage == null ) {
      this.storageImage = Activator.getDefault()
        .getImageRegistry()
        .get( "storage" ); //$NON-NLS-1$
    }
    return this.storageImage;
  }

  /**
   * Get an image that represents an {@link IGridComputing}.
   * 
   * @return The image for a Grid computing.
   */
  private Image getComputingImage() {
    if( this.computingImage == null ) {
      this.computingImage = Activator.getDefault()
        .getImageRegistry()
        .get( "computing" ); //$NON-NLS-1$
    }
    return this.computingImage;
  }

}
