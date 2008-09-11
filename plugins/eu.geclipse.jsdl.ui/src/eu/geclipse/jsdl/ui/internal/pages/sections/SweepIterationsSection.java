/*****************************************************************************
 * Copyright (c) 2008, g-Eclipse Consortium 
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
 *    Katarzyna Bylec - PSNC - Initial API and implementation
 *****************************************************************************/
package eu.geclipse.jsdl.ui.internal.pages.sections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.TableWrapData;

import eu.geclipse.jsdl.JSDLJobDescription;
import eu.geclipse.jsdl.model.base.JobDefinitionType;
import eu.geclipse.jsdl.model.sweep.AssignmentType;
import eu.geclipse.jsdl.model.sweep.SweepType;
import eu.geclipse.jsdl.parametric.IParametricJsdlGenerator;
import eu.geclipse.jsdl.parametric.ParametricJsdlGeneratorFactory;
import eu.geclipse.jsdl.ui.adapters.jsdl.ParametricJobAdapter;
import eu.geclipse.jsdl.ui.internal.EditorParametricJsdlHandler;
import eu.geclipse.jsdl.ui.internal.pages.FormSectionFactory;
import eu.geclipse.jsdl.ui.providers.parameters.IterationsCProvider;
import eu.geclipse.jsdl.ui.providers.parameters.IterationsLProvider;

/**
 * Section of "Parameters" page in JSDL multi-page editor. This section displays
 * values of each parameter for every iteration of parameters sweep.
 */
public class SweepIterationsSection extends JsdlFormPageSection {

  private ParametricJobAdapter adapter;
  private Shell shell;
  private Table table;
  private JobDefinitionType jobDefinitionType;
  private FormToolkit formToolkit;
  private TableViewer viewer;
  private IterationsLProvider labelProvider;
  private JSDLJobDescription jsdlJobDescription;
  private Composite client;
  private ArrayList<SweepType> sweepType;

  /**
   * Constructor.
   * 
   * @param parent parent composite of this section
   * @param toolkit forms toolkit to use
   * @param adapter adapter handling operations on parametric part of JSDL
   */
  public SweepIterationsSection( final Composite parent,
                                 final FormToolkit toolkit,
                                 final ParametricJobAdapter adapter )
  {
    this.adapter = adapter;
    createSection( parent, toolkit );
  }

  private void createSection( final Composite parent, final FormToolkit toolkit )
  {
    this.shell = parent.getShell();
    String sectionDescription = "Values of each parameter for every iteration of parameters sweep.";
    this.formToolkit = toolkit;
    this.client = FormSectionFactory.createExpandableSection( toolkit,
                                                              parent,
                                                              "Iterations view",
                                                              sectionDescription,
                                                              1,
                                                              false );
    Hyperlink link = toolkit.createHyperlink( client,
                                              "Calculate iterations (refresh)",
                                              SWT.LEAD );
    link.addMouseListener( new MouseListener() {

      public void mouseDoubleClick( final MouseEvent e ) {
        // TODO Auto-generated method stub
      }

      public void mouseDown( final MouseEvent e ) {
        updateTable();
      }

      public void mouseUp( final MouseEvent e ) {
        // TODO Auto-generated method stub
      }
    } );
    renewTableViewer();
  }

  protected void updateTable() {
    renewTableViewer();
    this.viewer.setInput( null );
    final JSDLJobDescription jsdl = this.jsdlJobDescription;
    final SweepIterationsSection section = this;
    Job job = new Job( "Generating sweep for iterations" ) {

      @Override
      protected IStatus run( final IProgressMonitor monitor ) {
        IParametricJsdlGenerator generator = ParametricJsdlGeneratorFactory.getGenerator();
        generator.generate( jsdl,
                            new EditorParametricJsdlHandler( section ),
                            new NullProgressMonitor() );
        return Status.OK_STATUS;
      }
    };
    job.setUser( true );
    job.schedule();
  }

  private void renewTableViewer() {
    if( this.viewer != null ) {
      this.viewer.getTable().dispose();
    }
    this.viewer = null;
    this.viewer = new TableViewer( this.client, SWT.BORDER | SWT.FULL_SELECTION );
    TableWrapData tableData = new TableWrapData( TableWrapData.FILL_GRAB );
    tableData.heightHint = 300;
    this.table = this.viewer.getTable();
    this.table.setLayoutData( tableData );
    this.labelProvider = new IterationsLProvider();
    this.labelProvider.setColumnIndexMap( getColumnNamesForTable() );
    this.viewer.setLabelProvider( this.labelProvider );
    this.viewer.setContentProvider( new IterationsCProvider() );
    this.table.setHeaderVisible( true );
    for( String name : getColumnNamesForTable().values() ) {
      TableColumn column = new TableColumn( this.table, SWT.NONE );
      column.setText( name );
      column.setWidth( 100 );
    }
    this.client.layout();
  }

  public void setInput( final JobDefinitionType jobDefinition,
                        final JSDLJobDescription jsdlJobDescription )
  {
    this.adapterRefreshed = true;
    if( null != jobDefinition ) {
      this.jobDefinitionType = jobDefinition;
    }
    this.jsdlJobDescription = jsdlJobDescription;
  }

  public void iterationGenerated( final Map<String, Properties> map ) {
    final TableViewer viewerF = this.viewer;
    final Map<String, Properties> mapF = map;
    PlatformUI.getWorkbench().getDisplay().syncExec( new Runnable() {

      public void run() {
        // TODO Auto-generated method stub
        viewerF.setInput( mapF );
        viewerF.refresh( true );
      }
    } );
  }

  private Map<Integer, String> getColumnNamesForTable() {
    Map<Integer, String> columnsMap = new HashMap<Integer, String>();
    int index = 0;
    columnsMap.put( Integer.valueOf( index ), "Iteration" );
    index++;
    for( String paramName : getInerSweepNames( getInnerList() ) ) {
      columnsMap.put( Integer.valueOf( index ), paramName );
      index++;
    }
    return columnsMap;
  }

  @Override
  protected void contentChanged() {
    this.labelProvider.setColumnIndexMap( getColumnNamesForTable() );
    super.contentChanged();
  }

  private List<SweepType> getInnerList() {
    List<SweepType> inerSweepList = new ArrayList<SweepType>();
    this.sweepType = new ArrayList<SweepType>();
    if( null != this.jobDefinitionType ) {
      TreeIterator<EObject> iterator = this.jobDefinitionType.eAllContents();
      while( iterator.hasNext() ) {
        EObject testType = iterator.next();
        if( testType instanceof SweepType
            && !( testType.eContainer() instanceof SweepType ) )
        {
          SweepType type = ( ( SweepType )testType );
          type.eAdapters().add( this );
          this.sweepType.add( type );
        }
      }
      if( !this.sweepType.isEmpty() ) {
        for( SweepType type : this.sweepType ) {
          TreeIterator<EObject> it = type.eAllContents();
          while( it.hasNext() ) {
            EObject testObject = it.next();
            if( testObject instanceof SweepType ) {
              inerSweepList.add( ( SweepType )testObject );
            }
          }
        }
        inerSweepList.addAll( this.sweepType );
      }
    }
    return inerSweepList;
  }

  private List<String> getInerSweepNames( final List<SweepType> inerSweepList )
  {
    List<String> result = new ArrayList<String>();
    for( SweepType sweep : inerSweepList ) {
      EList list = sweep.getAssignment();
      for( int i = 0; i < list.size(); i++ ) {
        Object el = list.get( i );
        if( el instanceof AssignmentType ) {
          AssignmentType assignment = ( AssignmentType )el;
          EList paramList = assignment.getParameter();
          for( int j = 0; j < paramList.size(); j++ ) {
            result.add( ( String )paramList.get( j ) );
          }
        }
      }
    }
    return result;
  }
}