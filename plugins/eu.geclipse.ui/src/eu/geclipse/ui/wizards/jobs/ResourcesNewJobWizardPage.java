package eu.geclipse.ui.wizards.jobs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import eu.geclipse.ui.internal.wizards.jobs.JSDLExactValueTab;
import eu.geclipse.ui.internal.wizards.jobs.JSDLRangesTab;
import eu.geclipse.ui.internal.wizards.jobs.Range;
import eu.geclipse.ui.internal.wizards.jobs.ValueWithEpsilon;

public class ResourcesNewJobWizardPage extends WizardPage {

  
  private JSDLRangesTab tab;
  private JSDLRangesTab tabCPUCount;
  private JSDLRangesTab totalPhysicalMemory;
  private JSDLExactValueTab tabVal;
  private JSDLExactValueTab tabCPUCountValues;
  private JSDLExactValueTab totalPhysicalMemoryValues;

  protected ResourcesNewJobWizardPage( final String pageName
                                        )
  {
    super( pageName );
    
    this.setTitle( "Resources" );
    this.setDescription( "Set details of resources required by your job to execute." );
  }

  public void createControl( Composite parent ) {
    Composite mainComp = new Composite( parent, SWT.NONE );
    GridLayout gLayout = new GridLayout( 3, false );
    gLayout.horizontalSpacing = 10;
    gLayout.verticalSpacing = 12;
    mainComp.setLayout( gLayout );
    GridData layout = new GridData();
    // Label cpuLabel = new Label( mainComp, GridData.HORIZONTAL_ALIGN_BEGINNING
    // | GridData.VERTICAL_ALIGN_CENTER | SWT.BOLD );
    // cpuLabel.setText( "CPU" );
    // layout.horizontalAlignment = GridData.FILL;
    // layout.horizontalSpan = 3;
    // cpuLabel.setLayoutData( layout );
    //    
    // Label cpuArch = new Label(mainComp, GridData.HORIZONTAL_ALIGN_BEGINNING
    // | GridData.VERTICAL_ALIGN_CENTER );
    // cpuArch.setText( "Architecture" );
    // layout = new GridData();
    // layout.horizontalAlignment = GridData.FILL;
    // layout.horizontalIndent = 20;
    // cpuArch.setLayoutData( layout );
    //    
    // this.cpuList = new Combo ( mainComp, SWT.SINGLE );
    // for (String cpu: this.cpuArchs){
    // this.cpuList.add( cpu );
    // }
    // layout = new GridData();
    // layout.horizontalAlignment = GridData.FILL;
    // layout.horizontalSpan = 2;
    // this.cpuList.setLayoutData( layout );
    Label cpuSpeedLabel = new Label( mainComp,
                                     GridData.HORIZONTAL_ALIGN_BEGINNING
                                         | GridData.VERTICAL_ALIGN_CENTER );
    cpuSpeedLabel.setText( "Individual CPU speed" );
    layout = new GridData();
    layout.horizontalAlignment = GridData.FILL;
    layout.horizontalIndent = 20;
    
    cpuSpeedLabel.setLayoutData( layout );
    HashMap<String, String> temp = new HashMap<String, String>();
    temp.put( "Start", "Start" );
    temp.put( "End", "End" );
    this.tab = new JSDLRangesTab( new RangeContentProvider(),
                                  new RangeLabelProvider(),
                                  temp,
                                  30,
                                  50 );
    tab.createControl( mainComp );
    
    HashMap<String, String> temp1 = new HashMap<String, String>();
    temp1.put( "Value", "Value" );
    temp1.put( "Epsilon", "Epsilon" );
    this.tabVal = new JSDLExactValueTab( new ValueWithEpsilonContentProvider(),
                                  new ValueWithEpsilonLabelProvider(),
                                  temp1,
                                  30,
                                  50 );
    tabVal.createControl( mainComp );
    
    Label totalCPUCountLabel = new Label( mainComp,
                                          GridData.HORIZONTAL_ALIGN_BEGINNING
                                              | GridData.VERTICAL_ALIGN_CENTER );
    totalCPUCountLabel.setText( "Total CPU Count" );
    layout = new GridData();
    layout.horizontalAlignment = GridData.FILL;
    layout.horizontalIndent = 20;
    totalCPUCountLabel.setLayoutData( layout );
    this.tabCPUCount = new JSDLRangesTab( new RangeContentProvider(),
                                          new RangeLabelProvider(),
                                          temp,
                                          30,
                                          50 );
    this.tabCPUCount.createControl( mainComp );
    
    this.tabCPUCountValues = new JSDLExactValueTab( new ValueWithEpsilonContentProvider(),
                                                    new ValueWithEpsilonLabelProvider(),
                                                    temp1,
                                                    30,
                                                    50 );
    this.tabCPUCountValues.createControl( mainComp );
    
    Label totalPhysicalMemoryLabel = new Label( mainComp,
                                          GridData.HORIZONTAL_ALIGN_BEGINNING
                                              | GridData.VERTICAL_ALIGN_CENTER );
    totalPhysicalMemoryLabel.setText( "Total physical memory" );
    layout = new GridData();
    layout.horizontalAlignment = GridData.FILL;
    layout.horizontalIndent = 20;
    totalPhysicalMemoryLabel.setLayoutData( layout );
    
    this.totalPhysicalMemory = new JSDLRangesTab( new RangeContentProvider(),
                                          new RangeLabelProvider(),
                                          temp,
                                          30,
                                          50 );
    this.totalPhysicalMemory.createControl( mainComp );
    
    this.totalPhysicalMemoryValues = new JSDLExactValueTab( new ValueWithEpsilonContentProvider(),
                                                    new ValueWithEpsilonLabelProvider(),
                                                    temp1,
                                                    30,
                                                    50 );
    this.totalPhysicalMemoryValues.createControl( mainComp );
    
    
    setControl( mainComp );
  }

  @SuppressWarnings("unchecked")
  public ArrayList<Range> getIndividualCPUSpeedRanges() {
    return this.tab.getInput();
  }
  
  @SuppressWarnings("unchecked")
  public ArrayList<Range> getTotalCPUCount(){
    return this.tabCPUCount.getInput();
  }
  
  @SuppressWarnings("unchecked")
  public ArrayList<Range> getTotalPhysicalMemory(){
    return this.totalPhysicalMemory.getInput();
  }
  
  @SuppressWarnings("unchecked")
  public ArrayList<ValueWithEpsilon> getIndividualCPUSValues(){
    return this.tabVal.getInput();
  }
  
  @SuppressWarnings("unchecked")
  public ArrayList<ValueWithEpsilon> getTotalCPUCountValues(){
    return this.tabCPUCountValues.getInput();
  }
  
  @SuppressWarnings("unchecked")
  public ArrayList<ValueWithEpsilon> getTotalPhysicalMemoryValues(){
    return this.totalPhysicalMemoryValues.getInput();
  }
  
  protected class RangeContentProvider implements IStructuredContentProvider {

    public Object[] getElements( final Object inputElement ) {
      Range[] elements = new Range[ 0 ];
      Map<String, String> m = new HashMap<String, String>();
      if( !m.isEmpty() ) {
        elements = new Range[ m.size() ];
        String[] varStarts = new String[ m.size() ];
        m.keySet().toArray( varStarts );
        for( int i = 0; i < m.size(); i++ ) {
          // elements[ i ] = new OutputFile( varNames[ i ],
          // m.get( varNames[ i ] ) );
        }
      }
      return elements;
    }

    public void dispose() {
      // nothing
    }

    public void inputChanged( final Viewer viewer,
                              final Object oldInput,
                              final Object newInput )
    {
      if( newInput == null ) {
        // do nothing
      } else {
        if( viewer instanceof TableViewer ) {
          TableViewer tableViewer = ( TableViewer )viewer;
          if( tableViewer.getTable().isDisposed() ) {
            // do nothing
          } else {
            tableViewer.setSorter( new ViewerSorter() {

              @Override
              public int compare( final Viewer iviewer,
                                  final Object element1,
                                  final Object element2 )
              {
                int result = -1;
                if( element1 == null ) {
                  result = -1;
                } else if( element2 == null ) {
                  result = 1;
                } // else {
                // result = ( ( Range )element1 ).getName()
                // .compareToIgnoreCase( ( ( Range )element2 ).getName() );
                // }
                return result;
              }
            } );
          }
        }
      }
    }
  }
  class RangeLabelProvider extends LabelProvider implements ITableLabelProvider
  {

    public String getColumnText( final Object element, final int columnIndex ) {
      String result = null;
      if( element != null ) {
        Range var = ( Range )element;
        switch( columnIndex ) {
          case 0: // type
            result = Double.valueOf( var.getStart() ).toString();
          break;
          case 1: // name
            result = Double.valueOf( var.getEnd() ).toString();
          break;
        }
      }
      return result;
    }

    public Image getColumnImage( final Object element, final int columnIndex ) {
      if( columnIndex == 0 ) {
        // return
        // DebugPluginImages.getImage(IDebugUIConstants.IMG_OBJS_ENV_VAR);
      }
      return null;
    }
  }
  
  //////providewrs for ValueWithEpsilon
  protected class ValueWithEpsilonContentProvider implements IStructuredContentProvider {

    public Object[] getElements( final Object inputElement ) {
      ValueWithEpsilon[] elements = new ValueWithEpsilon[ 0 ];
      Map<String, String> m = new HashMap<String, String>();
      if( !m.isEmpty() ) {
        elements = new ValueWithEpsilon[ m.size() ];
        String[] varStarts = new String[ m.size() ];
        m.keySet().toArray( varStarts );
        for( int i = 0; i < m.size(); i++ ) {
          // elements[ i ] = new OutputFile( varNames[ i ],
          // m.get( varNames[ i ] ) );
        }
      }
      return elements;
    }

    public void dispose() {
      // nothing
    }

    public void inputChanged( final Viewer viewer,
                              final Object oldInput,
                              final Object newInput )
    {
      if( newInput == null ) {
        // do nothing
      } else {
        if( viewer instanceof TableViewer ) {
          TableViewer tableViewer = ( TableViewer )viewer;
          if( tableViewer.getTable().isDisposed() ) {
            // do nothing
          } else {
            tableViewer.setSorter( new ViewerSorter() {

              @Override
              public int compare( final Viewer iviewer,
                                  final Object element1,
                                  final Object element2 )
              {
                int result = -1;
                if( element1 == null ) {
                  result = -1;
                } else if( element2 == null ) {
                  result = 1;
                } // else {
                // result = ( ( Range )element1 ).getName()
                // .compareToIgnoreCase( ( ( Range )element2 ).getName() );
                // }
                return result;
              }
            } );
          }
        }
      }
    }
  }
  class ValueWithEpsilonLabelProvider extends LabelProvider implements ITableLabelProvider
  {

    public String getColumnText( final Object element, final int columnIndex ) {
      String result = null;
      if( element != null ) {
        ValueWithEpsilon var = ( ValueWithEpsilon )element;
        switch( columnIndex ) {
          case 0: // type
            result = Double.valueOf( var.getValue() ).toString();
          break;
          case 1: // name
            result = Double.valueOf( var.getEpsilon() ).toString();
          break;
        }
      }
      return result;
    }

    public Image getColumnImage( final Object element, final int columnIndex ) {
      if( columnIndex == 0 ) {
        // return
        // DebugPluginImages.getImage(IDebugUIConstants.IMG_OBJS_ENV_VAR);
      }
      return null;
    }
  }
  
}
