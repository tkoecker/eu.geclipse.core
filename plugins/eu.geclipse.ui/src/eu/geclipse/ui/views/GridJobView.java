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
 *****************************************************************************/

package eu.geclipse.ui.views;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.XMLMemento;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

import eu.geclipse.core.model.GridModel;
import eu.geclipse.core.model.IGridElementManager;
import eu.geclipse.core.model.IGridJob;
import eu.geclipse.core.model.IGridJobManager;
import eu.geclipse.core.model.IGridJobStatusListener;
import eu.geclipse.ui.internal.Activator;
import eu.geclipse.ui.internal.actions.ActionGroupManager;
import eu.geclipse.ui.internal.actions.FilterActions;
import eu.geclipse.ui.internal.actions.JobViewActions;
import eu.geclipse.ui.providers.JobViewLabelProvider;
import eu.geclipse.ui.views.filters.GridFilterConfigurationsManager;
import eu.geclipse.ui.views.filters.IFilterConfigurationListener;
import eu.geclipse.ui.views.filters.IGridFilterConfiguration;
import eu.geclipse.ui.views.filters.JobViewFilterConfiguration;

/**
 * Job view that shows all jobs that are currently managed by the default
 * implementation of the {@link IGridJobManager} interface
 */
public class GridJobView extends ElementManagerViewPart
  implements IGridJobStatusListener, IFilterConfigurationListener
{
  private static String XML_MEMENTO_FILTERS = "Filters"; //$NON-NLS-1$
  private static String PREFERENCE_NAME_FILTERS = "GridJobViewFilters"; //$NON-NLS-1$
  private JobViewActions jobActions;
  private GridFilterConfigurationsManager filterConfigurationsManager;

  @Override
  public void dispose() {
    GridModel.getJobManager().removeJobStatusListener( this );
    if( this.filterConfigurationsManager != null ) {
      this.filterConfigurationsManager.removeConfigurationListener( this );
    }
    
    saveFilters();    
    super.dispose();
  }

  public void statusChanged( final IGridJob job ) {
    refreshViewer( job );
  }

  /*
   * (non-Javadoc)
   * 
   * @see eu.geclipse.ui.views.GridElementManagerViewPart#getManager()
   */
  @Override
  protected IGridElementManager getManager() {
    return GridModel.getJobManager();
  }

  /*
   * (non-Javadoc)
   * 
   * @see eu.geclipse.ui.views.GridModelViewPart#createLabelProvider()
   */
  @Override
  protected IBaseLabelProvider createLabelProvider() {
    return new JobViewLabelProvider();
  }

  /*
   * (non-Javadoc)
   * 
   * @see eu.geclipse.ui.views.ElementManagerViewPart#createTreeColumns(org.eclipse.swt.widgets.Tree)
   */
  @Override
  protected boolean createTreeColumns( final Tree tree ) {
    super.createTreeColumns( tree );
    TreeColumn idColumn = new TreeColumn( tree, SWT.NONE );
    idColumn.setText( Messages.getString( "GridJobView.id_column" ) ); //$NON-NLS-1$
    idColumn.setAlignment( SWT.LEFT );
    idColumn.setWidth( 200 );
    TreeColumn statusColumn = new TreeColumn( tree, SWT.NONE );
    statusColumn.setText( Messages.getString( "GridJobView.status_column" ) ); //$NON-NLS-1$
    statusColumn.setAlignment( SWT.LEFT );
    statusColumn.setWidth( 100 );
    TreeColumn reasonColumn = new TreeColumn( tree, SWT.NONE );
    reasonColumn.setText( Messages.getString( "GridJobView.reason_column" ) ); //$NON-NLS-1$
    reasonColumn.setAlignment( SWT.LEFT );
    reasonColumn.setWidth( 100 );
    TreeColumn lastUpdateColumn = new TreeColumn( tree, SWT.NONE );
    lastUpdateColumn.setText( Messages.getString( "GridJobView.last_update_column" ) ); //$NON-NLS-1$
    lastUpdateColumn.setAlignment( SWT.LEFT );
    lastUpdateColumn.setWidth( 120 );
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see eu.geclipse.ui.views.ElementManagerViewPart#contributeAdditionalActions(eu.geclipse.ui.internal.actions.ActionGroupManager)
   */
  @Override
  protected void contributeAdditionalActions( final ActionGroupManager groups )
  {
    IWorkbenchSite site = getSite();
    this.jobActions = new JobViewActions( site );
    groups.addGroup( this.jobActions );
    groups.addGroup( new FilterActions( getSite(),
                                        this.filterConfigurationsManager ) );
    super.contributeAdditionalActions( groups );
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.part.ViewPart#init(org.eclipse.ui.IViewSite,
   *      org.eclipse.ui.IMemento)
   */
  @Override
  public void init( final IViewSite site, final IMemento mem )
    throws PartInitException
  {
    super.init( site, mem );
    GridModel.getJobManager().addJobStatusListener( this );
    IPreferenceStore preferenceStore = new ScopedPreferenceStore( new InstanceScope(),
                                                                  "eu.geclipse.core" ); //$NON-NLS-1$
    preferenceStore.addPropertyChangeListener( new IPropertyChangeListener() {

      public void propertyChange( final PropertyChangeEvent event ) {
        if( GridJobView.this.jobActions != null ) {
          GridJobView.this.jobActions.setJobsUpdateStatus( eu.geclipse.core.Preferences.getUpdateJobsStatus() );
        }
      }
    } );
  }

  /*
   * (non-Javadoc)
   * 
   * @see eu.geclipse.ui.views.GridModelViewPart#initViewer(org.eclipse.jface.viewers.StructuredViewer)
   */
  @Override
  protected void initViewer( final StructuredViewer sViewer ) {
    super.initViewer( sViewer );
    initFilters( sViewer );
  }

  private void initFilters( final StructuredViewer sViewer ) {
    createFilterConfigurationsManager( sViewer );
    readFilters();
  }

  private void createFilterConfigurationsManager( final StructuredViewer sViewer )
  {
    this.filterConfigurationsManager = new GridFilterConfigurationsManager( GridFilterConfigurationsManager.ID_JOBVIEW )
    {

      @Override
      public IGridFilterConfiguration createConfiguration( final String name ) {
        return new JobViewFilterConfiguration( name );
      }
    };
    this.filterConfigurationsManager.addConfigurationListener( this );
  }

  public void configurationChanged() {
    // ignore changes in configurations
  }

  public void filterConfigurationSelected( final ViewerFilter[] filters ) {
    if( this.getViewer() != null ) {
      this.getViewer().setFilters( filters );
    }
  }

  private void saveFilters() {
    XMLMemento memento = XMLMemento.createWriteRoot( XML_MEMENTO_FILTERS );
    if( this.filterConfigurationsManager != null ) {
      this.filterConfigurationsManager.saveState( memento );
    }
    StringWriter writer = new StringWriter();
    try {
      memento.save( writer );
    } catch( IOException exc ) {
      Activator.logException( exc );
    }
    
    Preferences preferences = Activator.getDefault().getPluginPreferences();
    preferences.setValue( PREFERENCE_NAME_FILTERS, writer.toString() );
    Activator.getDefault().savePluginPreferences();
  }
  
  private void readFilters() {    
    String preference = Activator.getDefault().getPluginPreferences().getString( PREFERENCE_NAME_FILTERS );

    if( preference != null
        && preference.length() > 0 ) {
      StringReader reader = new StringReader( preference );
      try {
        this.filterConfigurationsManager.readState( XMLMemento.createReadRoot( reader ) );
      } catch (WorkbenchException exc) {
        Activator.logException( exc );
      }
    }
  }  
}