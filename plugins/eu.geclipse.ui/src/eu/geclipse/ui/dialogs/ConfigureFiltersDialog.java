/******************************************************************************
 * Copyright (c) 2007 g-Eclipse consortium 
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
 *     Mariusz Wojtysiak - initial API and implementation
 *     
 *****************************************************************************/
package eu.geclipse.ui.dialogs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import eu.geclipse.ui.internal.Activator;
import eu.geclipse.ui.internal.dialogs.ConfigureFilters.FilterCompositeFactory;
import eu.geclipse.ui.internal.dialogs.ConfigureFilters.IFilterComposite;
import eu.geclipse.ui.views.filters.GridFilterConfigurationsManager;
import eu.geclipse.ui.views.filters.IGridFilter;
import eu.geclipse.ui.views.filters.IGridFilterConfiguration;

public class ConfigureFiltersDialog extends TrayDialog {  

  private List<IFilterComposite> composites = new ArrayList<IFilterComposite>();
  private CheckboxTableViewer tableViewer;
  private List<IGridFilterConfiguration> configurations;
  private GridFilterConfigurationsManager configurationsManager;

  public ConfigureFiltersDialog( final Shell shell, final GridFilterConfigurationsManager filterConfigurationsManager ) {
    super( shell );
    this.configurationsManager = filterConfigurationsManager;
    this.configurations = copyConfigurations( filterConfigurationsManager.getConfigurations() );
    setShellStyle( getShellStyle() | SWT.RESIZE );
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
   */
  @Override
  protected void configureShell( final Shell newShell )
  {
    newShell.setText( "Configure filters" );
    super.configureShell( newShell );
  } 

  /* (non-Javadoc)
   * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
   */
  @Override
  protected Control createDialogArea( final Composite parent )
  {
    Composite dialogAreaComposite = ( Composite )super.createDialogArea( parent );

    ((GridLayout)dialogAreaComposite.getLayout()).numColumns = 2;
    createConfigurationsComposite( dialogAreaComposite );
    createFiltersComposite( dialogAreaComposite );
        
    return dialogAreaComposite;
  }
  
  private void createConfigurationsComposite( final Composite parent ) {
    Composite configComposite = new Composite( parent, SWT.NONE );
    
    GridLayout layout = new GridLayout( 2, false );
    configComposite.setLayout( layout );
    createTableLabel( configComposite );
    createConfigsTable( configComposite );
    createTableButtons( configComposite );
  }
  
  private void createTableLabel( final Composite parent ) {
    Label label = new Label( parent, SWT.NONE );
    GridData gridData = new GridData();
    gridData.horizontalSpan = 2;
    label.setLayoutData( gridData );
    label.setText( "Defined filters:" );
  }
  
  private void createConfigsTable( final Composite parent ) {
    tableViewer = CheckboxTableViewer.newCheckList( parent, SWT.CHECK | SWT.BORDER );
    tableViewer.setContentProvider( new ArrayContentProvider() );
    tableViewer.setLabelProvider( createTableLabelProvider() );
    tableViewer.addSelectionChangedListener( createTableSelectionListener() );
    tableViewer.addCheckStateListener( createCheckStateListener() );
    
    GridData gridData = new GridData();
    gridData.heightHint = 150;
    gridData.widthHint = 75;
    tableViewer.getTable().setLayoutData( gridData );
    tableViewer.setInput( this.configurations );
    setCheckedConfigs();
    tableViewer.setSelection( new StructuredSelection( this.configurations.get( 0 ) ) );
  }
  
  private ICheckStateListener createCheckStateListener() {
    return new ICheckStateListener() {

      public void checkStateChanged( CheckStateChangedEvent event ) {
        IGridFilterConfiguration configuration = ( IGridFilterConfiguration )event.getElement();
        enableConfiguration( configuration );
      }
    };
  }
  
  private void enableConfiguration( IGridFilterConfiguration configuration ) {
    for( IGridFilterConfiguration curConfiguration : this.configurations ) {
      boolean enable = curConfiguration == configuration;
      tableViewer.setChecked( curConfiguration, enable );
      curConfiguration.setEnabled( enable );
    }
    
  }      


  private void setCheckedConfigs() {
    for( IGridFilterConfiguration configuration : this.configurations ) {
      this.tableViewer.setChecked( configuration, configuration.isEnabled() );
    }
  }

  private ILabelProvider createTableLabelProvider() {
    return new LabelProvider() {

      /* (non-Javadoc)
       * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
       */
      @Override
      public String getText( final Object element )
      {
        return ((IGridFilterConfiguration)element).getName();
      }
    };    
  }
  
  ISelectionChangedListener createTableSelectionListener() {
    return new ISelectionChangedListener() {

      public void selectionChanged( final SelectionChangedEvent event ) {
        StructuredSelection selection = (StructuredSelection)event.getSelection();        
        
        if( selection != null ) {
          IGridFilterConfiguration configuration = (IGridFilterConfiguration)selection.getFirstElement();   
          
          for( IFilterComposite filterComposite : ConfigureFiltersDialog.this.composites ) {
            filterComposite.saveToFilter();
            filterComposite.setFilter( configuration );
          }
        }
        
        setReadOnly( selection == null || selection.isEmpty() );
      }      
    };
  }
  
  private void setReadOnly( boolean readOnly ) {
    for( IFilterComposite filterComposite : ConfigureFiltersDialog.this.composites ) {
      filterComposite.setReadOnly( readOnly );
    }    
  }

  private void createTableButtons( final Composite parent ) {
    Composite buttonsComposite = new Composite( parent, SWT.NONE );
    GridData gridData = new GridData();
    gridData.verticalAlignment = SWT.TOP;
    buttonsComposite.setLayoutData( gridData );
    buttonsComposite.setLayout( new GridLayout( 1, false ) );
    createNewButton( buttonsComposite );
    createDeleteButton( buttonsComposite );
  }
  
  private void createNewButton( final Composite parent ) {
    Button button = createButton( parent, "&New");
    button.addSelectionListener( new SelectionAdapter() {

      /* (non-Javadoc)
       * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
       */
      @Override
      public void widgetSelected( SelectionEvent e )
      {
        InputDialog dialog = new InputDialog( getShell(), "Create New Filter", "Enter Filter Name", "", createNameValidator() );
        
        if( dialog.open() == InputDialog.OK ) {          
          IGridFilterConfiguration newConfiguration = configurationsManager.createConfiguration( dialog.getValue() );
          configurations.add( newConfiguration );
          tableViewer.add( newConfiguration );
          tableViewer.setChecked( newConfiguration, newConfiguration.isEnabled() );
          tableViewer.setSelection( new StructuredSelection( newConfiguration ) );
        }
      }
      
      private IInputValidator createNameValidator() {
        return new IInputValidator() {

          public String isValid( String newText ) {
            String invalidMsg = null;
            if( findConfiguration( newText ) != null ) {
              invalidMsg = "Filter \"" + newText + "\" already exists. Please enter unique Filter Name.";
            }
            return invalidMsg;
          }
        };
      }
      
    } );    
  }
  
  private void createDeleteButton( final Composite parent ) {
    Button button = createButton( parent, "&Delete" );
    
    button.addSelectionListener( new SelectionAdapter() {

      /* (non-Javadoc)
       * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
       */
      @Override
      public void widgetSelected( SelectionEvent e )
      {
        StructuredSelection selection = (StructuredSelection)tableViewer.getSelection();
        if( selection == null || selection.isEmpty() ) {
          MessageDialog.openWarning( getShell(), "Delete filter", "Select Filter, which you want to delete" );
        }
        else {
          IGridFilterConfiguration configuration = (IGridFilterConfiguration)selection.getFirstElement();
          tableViewer.remove( configuration );
          configurations.remove( configuration );          
        }
      }
      
    });
  }
  
  private Button createButton( final Composite parent, final String textString ) {
    Button button = new Button( parent, SWT.NONE );
    GridData gridData = new GridData();
    gridData.widthHint = 80;
    button.setLayoutData( gridData );
    button.setText( textString );    
    return button;   
  }
  
  private void createFiltersComposite( final Composite parent ) {
    Composite filtersComposite = new Composite( parent, SWT.NONE );
    filtersComposite.setLayout( new GridLayout( 1, false ) );
    IGridFilterConfiguration configuration = this.configurations.get( 0 );
    for( IGridFilter filter : configuration.getFilters() ) {
      IFilterComposite composite = FilterCompositeFactory.create( filter, filtersComposite, SWT.NONE );
      if( composite != null ) {
        this.composites.add( composite );
      }
    }
  }
  
  private List<IGridFilterConfiguration> copyConfigurations( final List<IGridFilterConfiguration> sourceConfigurations ) {
    List<IGridFilterConfiguration> newConfigurations = new ArrayList<IGridFilterConfiguration>( 3 + ( sourceConfigurations != null ? sourceConfigurations.size() : 0 ) );

    try {
      for( IGridFilterConfiguration configuration : sourceConfigurations ) {
        newConfigurations.add( configuration.clone() );
      }
    } catch( CloneNotSupportedException exception ) {
      Activator.getDefault().logException( exception );
    }
    
    if( newConfigurations.isEmpty() ) {
      IGridFilterConfiguration defaultConfiguration = this.configurationsManager.createConfiguration( "Default" );      
      newConfigurations.add( defaultConfiguration );
    }
    
    return newConfigurations;
  }
  
  private IGridFilterConfiguration findConfiguration( final String name ) {
    IGridFilterConfiguration foundConfiguration = null;
    
    for( Iterator<IGridFilterConfiguration> iterator = this.configurations.iterator(); iterator.hasNext() && foundConfiguration == null;  )
    {
      IGridFilterConfiguration configuration = iterator.next();
      if( configuration.getName().equals( name ) ) {
        foundConfiguration = configuration;
      }
    }
    return foundConfiguration;
  }
  
  private void deleteConfiguration( final String name ) {
    IGridFilterConfiguration configuration = findConfiguration( name );
    
    if( configuration != null ) {
      this.configurations.remove( configuration );
    }
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.jface.dialogs.Dialog#okPressed()
   */
  @Override
  protected void okPressed()
  {
    saveConfigurationsState();
    for( IFilterComposite composite : this.composites ) {
      composite.saveToFilter();
    }
    super.okPressed();
  }

  public List<IGridFilterConfiguration> getConfigurations() {
    return this.configurations;
  }
  
  private void saveConfigurationsState() {
    for( IGridFilterConfiguration configuration : this.configurations ) {
      configuration.setEnabled( this.tableViewer.getChecked( configuration ) );
    }
  }

}
