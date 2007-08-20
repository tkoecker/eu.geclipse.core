package eu.geclipse.jsdl.ui.internal.pages;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.TableWrapData;
import eu.geclipse.jsdl.ui.adapters.jsdl.DataStageTypeAdapter;
import eu.geclipse.jsdl.ui.providers.DataStageInLabelProvider;
import eu.geclipse.jsdl.ui.providers.DataStageOutLabelProvider;
import eu.geclipse.jsdl.ui.providers.FeatureContentProvider;
import eu.geclipse.jsdl.ui.providers.FeatureLabelProvider;


/**
 * @author nickl
 *
 */
public class DataStagingPage extends FormPage implements INotifyChangedListener {

  protected Composite body = null;
  protected Composite stageInSection = null;
  protected Composite stageOutSection = null;
  
  protected List lstStageInFileName = null;
  protected Text txtStageInFileSystemName = null;
  protected Text txtSource = null;
  protected Text txtTarget = null;
  protected Text txtStageInName = null;
   
  protected Label lblStageInFileName = null;
  protected Label lblStageInFileSystemName = null;
  protected Label lblCreationFlag = null;
  protected Label lblDelOnTerm = null;
  protected Label lblSource = null;
  protected Label lblTarget = null;
  protected Label lblStageInName = null;
  
  protected Button btnStageInAdd = null;
  protected Button btnStageInEdit = null;
  protected Button btnStageInDel = null;
  
  protected Button btnStageOutAdd = null;
  protected Button btnStageOutEdit = null;
  protected Button btnStageOutDel = null;
  
  protected Combo cmbCreationFlag = null;
  protected Combo cmbDelOnTerm = null;
  
  protected Table tblStageIn = null;
  protected Table tblStageOut = null; 
  
  protected TableViewer viewerStageIn = null;  
  protected TableViewer viewerStageOut = null;  
    
  protected FeatureContentProvider featureContentProvider = new FeatureContentProvider();
  protected FeatureLabelProvider featureLabelProvider = new FeatureLabelProvider();
  private TableColumn column;
  
  private DataStageTypeAdapter dataStageTypeAdapter; 
  
  private final int WIDGET_HEIGHT = 100;
  private boolean contentRefreshed = false;
  private boolean dirtyFlag = false;
  
  
  /**
   * @param editor
   */
  public DataStagingPage( final FormEditor editor )
  
  {
   
   super(editor,"", //$NON-NLS-1$
         Messages.getString("DataStagingPage_PageTitle")); //$NON-NLS-1$
  
   }
  
  
  public void notifyChanged( final Notification arg0 ) {
    setDirty( true );
    
  }
  
  
  
  /**
   * This method set's the dirty status of the page.
   * 
   * @param dirty
   * If TRUE then the page is Dirty and a Save operation is needed.
   * 
   */
  public void setDirty(final boolean dirty) {
    
    if (this.dirtyFlag != dirty) {
      this.dirtyFlag = dirty;     
      this.getEditor().editorDirtyStateChanged();  
    }
    
  }
  
  
  
  @Override
  public boolean isDirty() {
    
    return this.dirtyFlag;
    
  }  
  
  
  
  @Override
  public void setActive(final boolean active) {
    
    if ( active ){
      if ( isContentRefreshed() ) {
        this.dataStageTypeAdapter.load();
      }//end_if isContentRefreshed()
    } // end_if active
    
  } //End void setActive()
  
  
  
  private boolean isContentRefreshed(){
    
    return this.contentRefreshed;
    
  } //End boolean isContentRefreshed()
  
  
  
  @Override
  protected void createFormContent(final IManagedForm managedForm) {
    
    
    ScrolledForm form = managedForm.getForm();
    FormToolkit toolkit = managedForm.getToolkit();
    
    form.setText(Messages.getString("DataStagingPage_DataStagingPageTitle"));  //$NON-NLS-1$
    
    this.body = form.getBody();
    this.body.setLayout(FormLayoutFactory.createFormTableWrapLayout(false, 2));
    
    
    this.stageInSection = toolkit.createComposite( this.body );
    this.stageInSection.setLayout(FormLayoutFactory.createFormPaneTableWrapLayout(false, 1));
    this.stageInSection.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

    createStageInSection( this.stageInSection , toolkit );
   
    this.stageOutSection = toolkit.createComposite( this.body );
    this.stageOutSection.setLayout(FormLayoutFactory.createFormPaneTableWrapLayout(false, 1));
    this.stageOutSection.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

    createStageOutSection( this.stageInSection, toolkit );    

    /*Load the Adapters for this page */
    
    this.dataStageTypeAdapter.load();
    
  }
 
 
 
  /**
   * Method that set's the DataStage Page content. The content is the root 
   * JSDL element. Also this method is responsible to initialize the associated 
   * type adapters for the elements of this page.  This method must be called only
   * from the JSDL Editor.
   * 
   * Associated Type Adapters for this page are: 
   * @see DataStageTypeAdapter
   *  
   * @param rootJsdlElement
   * 
   * @param refreshStatus
   * Set to TRUE if the original page content is already set, but there is a need
   * to refresh the page because there was a change to this content
   * from an outside editor.
   * 
   */
  public void setPageContent( final EObject rootJsdlElement, 
                              final boolean refreshStatus ) {

  if ( refreshStatus ) {
     this.contentRefreshed = true;
     this.dataStageTypeAdapter.setContent( rootJsdlElement );
   }
  else {     
     this.dataStageTypeAdapter = new DataStageTypeAdapter( rootJsdlElement );
     this.dataStageTypeAdapter.addListener( this );
   }
         
  } // End void getPageContent()
 
 
 
  /* This function creates the Stage-In Section of the DateStage Page */
  private void createStageInSection( final Composite parent,
                                     final FormToolkit toolkit ) {
   
   String sectionTitle = Messages.getString( "DataStagingPage_StageInSection" ); //$NON-NLS-1$
   String sectionDescription = Messages.getString( "DataStagingPage_StageInDescr" );   //$NON-NLS-1$
   
   GridData gd;
      
   Composite client = FormSectionFactory.createGridStaticSection(toolkit,
                                          parent,
                                          sectionTitle,
                                          sectionDescription,
                                          3);
     
   
   gd = new GridData();
   
   gd.horizontalAlignment = GridData.FILL;
   gd.verticalAlignment = GridData.FILL;
   gd.verticalSpan = 3;
   gd.horizontalSpan = 1;
   gd.widthHint = 600;
   gd.heightHint = this.WIDGET_HEIGHT;
   
   
   this.viewerStageIn = new TableViewer(client, SWT.NONE 
                                        | SWT.VIRTUAL                                        
                                        | SWT.FULL_SELECTION );
   
   this.tblStageIn = this.viewerStageIn.getTable();
   this.tblStageIn .setHeaderVisible( true);
   this.tblStageIn.setLinesVisible( true );
   
   /* Set the common Content Provider  */
   this.viewerStageIn.setContentProvider(new FeatureContentProvider() );
   /* Set the dedicated Label Provider for DataStage-In elements */
   this.viewerStageIn.setLabelProvider(new DataStageInLabelProvider() );   
   
   this.column = new TableColumn(this.tblStageIn, SWT.LEFT);    
   this.column.setText( Messages.getString("DataStagingPage_Source") ); //$NON-NLS-1$
   this.column.setWidth( 200 );
   this.column = new TableColumn(this.tblStageIn, SWT.CENTER);    
   this.column.setText( Messages.getString("DataStagingPage_FileName") ); //$NON-NLS-1$
   this.column.setWidth( 150 );
   this.column = new TableColumn(this.tblStageIn, SWT.CENTER);    
   this.column.setText( Messages.getString("DataStagingPage_CreationFlag") ); //$NON-NLS-1$
   this.column.setWidth( 100 );
   this.column = new TableColumn(this.tblStageIn, SWT.CENTER);    
   this.column.setText( Messages.getString("DataStagingPage_DeleteOnTermination") ); //$NON-NLS-1$
   this.column.setWidth( 100 );
   
   /* Based on the Table Viewer selection, update the status of the respective
    * buttons.
    */
   this.viewerStageIn .addSelectionChangedListener( new ISelectionChangedListener()
   {

     public void selectionChanged( final SelectionChangedEvent event ) {
       updateButtons((TableViewer)event.getSource());
     }
   } );
   
   this.dataStageTypeAdapter.attachToStageIn( this.viewerStageIn  );
   this.tblStageIn.setData(  FormToolkit.KEY_DRAW_BORDER );
   this.tblStageIn.setLayoutData( gd);
   
   /* Create "Add" Button */
   gd = new GridData();
   gd.horizontalSpan = 2;
   gd.verticalSpan = 1;
   gd.widthHint = 60;
   this.btnStageInAdd = toolkit.createButton(client,
                                      Messages.getString("JsdlEditor_AddButton") //$NON-NLS-1$
                                      , SWT.PUSH);
   
   this.btnStageInAdd.addSelectionListener(new SelectionListener() {
     public void widgetSelected(final SelectionEvent event) {
       //         
     }

      public void widgetDefaultSelected(final SelectionEvent event) {
          // Do Nothing - Required method
      }
    });
   
   this.btnStageInAdd.setLayoutData( gd );
   
   /* Create "Edit..." Button */
   gd = new GridData();
   gd.horizontalSpan = 2;
   gd.verticalSpan = 1;
   gd.widthHint = 60;
   this.btnStageInEdit = toolkit.createButton(client,
                                      Messages.getString("JsdlEditor_EditButton") //$NON-NLS-1$
                                      , SWT.PUSH);
   
   this.btnStageInEdit.addSelectionListener(new SelectionListener() {
     public void widgetSelected(final SelectionEvent event) {
       //       
     }

      public void widgetDefaultSelected(final SelectionEvent event) {
          // Do Nothing - Required method
      }
    });
   
   this.btnStageInEdit.setLayoutData( gd );
   
   
   /* Create "Remove" Button */
   gd = new GridData();
   gd.horizontalSpan = 2;
   gd.verticalSpan = 1;
   gd.widthHint = 60;
   gd.verticalAlignment = GridData.BEGINNING;
   
   this.btnStageInDel = toolkit.createButton(client,
                                   Messages.getString("JsdlEditor_RemoveButton") //$NON-NLS-1$
                                   , SWT.PUSH);   
   
//   this.posixApplicationTypeAdapter.attachToDelete( this.btnDel, 
//                                                            this.argumentViewer );

   this.btnStageInDel.setLayoutData( gd );
   
   /* Update Buttons so as to reflect the initial status of the TableViewer */
   updateButtons( this.viewerStageIn  );
   
   toolkit.paintBordersFor( client );  
     
 }
 
 
 /* This function creates the Stage-Out Section of the DateStage Page */
  private void createStageOutSection( final Composite parent,
                                    final FormToolkit toolkit )                                             
  {

   String sectionTitle = Messages.getString("DataStagingPage_StageOutSection"); //$NON-NLS-1$
   String sectionDescription = Messages.getString("DataStagingPage_StageOutDescr");   //$NON-NLS-1$
   
   GridData gd;
      
   Composite client = FormSectionFactory.createGridStaticSection(toolkit,
                                          parent,
                                          sectionTitle,
                                          sectionDescription,
                                          3);
     
   
   gd = new GridData();
   
   gd.horizontalAlignment = GridData.FILL;
   gd.verticalAlignment = GridData.FILL;
   gd.verticalSpan = 3;
   gd.horizontalSpan = 1;
   gd.widthHint = 600;
   gd.heightHint = this.WIDGET_HEIGHT;
   
   
   this.viewerStageOut = new TableViewer(client, SWT.NONE 
                                        | SWT.VIRTUAL                                        
                                        | SWT.FULL_SELECTION );
   
   
   this.tblStageOut = this.viewerStageOut.getTable();
   this.tblStageOut .setHeaderVisible( true);
   this.tblStageOut.setLinesVisible( true );  
   /* Set the common Content Provider  */
   this.viewerStageOut.setContentProvider( new FeatureContentProvider() );
   /* Set the dedicated Label Provider for DataStage-Out elements */
   this.viewerStageOut.setLabelProvider( new DataStageOutLabelProvider() );
   
     
   this.column = new TableColumn(this.tblStageOut, SWT.CENTER);    
   this.column.setText( Messages.getString("DataStagingPage_FileName")); //$NON-NLS-1$
   this.column.setWidth( 150 );
   this.column = new TableColumn(this.tblStageOut, SWT.LEFT);
   this.column.setText( Messages.getString("DataStagingPage_Target") ); //$NON-NLS-1$
   this.column.setWidth( 200 );
   this.column = new TableColumn(this.tblStageOut, SWT.CENTER);    
   this.column.setText( Messages.getString("DataStagingPage_CreationFlag") ); //$NON-NLS-1$
   this.column.setWidth( 100 );
   this.column = new TableColumn(this.tblStageOut, SWT.CENTER);    
   this.column.setText( Messages.getString("DataStagingPage_DeleteOnTermination") ); //$NON-NLS-1$
   this.column.setWidth( 100 );
   
   /* Based on the Table Viewer selection, update the status of the respective
    * buttons.
    */
   this.viewerStageOut .addSelectionChangedListener( new ISelectionChangedListener()
   {

     public void selectionChanged( final SelectionChangedEvent event ) {
       updateButtons((TableViewer)event.getSource());
     }
   } );
   
   this.dataStageTypeAdapter.attachToStageOut( this.viewerStageOut  );
   this.tblStageOut.setData(  FormToolkit.KEY_DRAW_BORDER );
   this.tblStageOut.setLayoutData( gd);
   
   
   /* Create "Add" Button */
   gd = new GridData();
   gd.horizontalSpan = 2;
   gd.verticalSpan = 1;
   gd.widthHint = 60;
   this.btnStageOutAdd = toolkit.createButton(client,
                                      Messages.getString("JsdlEditor_AddButton") //$NON-NLS-1$
                                      , SWT.PUSH);
   
   this.btnStageOutAdd.addSelectionListener(new SelectionListener() {
     public void widgetSelected(final SelectionEvent event) {
       //       
     }

      public void widgetDefaultSelected(final SelectionEvent event) {
          // Do Nothing - Required method
      }
    });
   
   this.btnStageOutAdd.setLayoutData( gd );
   
   /* Create "Edit..." Button */
   gd = new GridData();
   gd.horizontalSpan = 2;
   gd.verticalSpan = 1;
   gd.widthHint = 60;
   this.btnStageOutEdit = toolkit.createButton(client,
                                      Messages.getString("JsdlEditor_EditButton") //$NON-NLS-1$
                                      , SWT.PUSH);
   
   this.btnStageOutEdit.addSelectionListener(new SelectionListener() {
     public void widgetSelected(final SelectionEvent event) {
       // 
     }

      public void widgetDefaultSelected(final SelectionEvent event) {
          // Do Nothing - Required method
      }
    });
   
   this.btnStageOutEdit.setLayoutData( gd );
   
   
   /* Create "Remove" Button */
   gd = new GridData();
   gd.horizontalSpan = 2;
   gd.verticalSpan = 1;
   gd.widthHint = 60;
   gd.verticalAlignment = GridData.BEGINNING;
   
   this.btnStageOutDel = toolkit.createButton(client,
                                   Messages.getString("JsdlEditor_RemoveButton") //$NON-NLS-1$
                                   , SWT.PUSH);
   
//   this.posixApplicationTypeAdapter.attachToDelete( this.btnDel, 
//                                                            this.argumentViewer );

   this.btnStageOutDel.setLayoutData( gd );
   
   /* Update Buttons so as to reflect the initial status of the TableViewer */
   updateButtons( this.viewerStageOut  );
   
   toolkit.paintBordersFor( client );

}
 
 
 
 /*
  * This function updates the status of the buttons related to
  * the respective Stage-In and Stage-Out Table Viewers. The Status of the 
  * buttons is adjusted according to the selection and content of the respective
  * table viewer.
  * 
  */ 
   protected void updateButtons(final TableViewer tableViewer) {
   
   ISelection selection = tableViewer.getSelection();
   boolean selectionAvailable = !selection.isEmpty();
   
   if (tableViewer == this.viewerStageIn){
   
     this.btnStageInAdd.setEnabled( true );
     this.btnStageInDel.setEnabled( selectionAvailable );
     this.btnStageInEdit.setEnabled( selectionAvailable );
   }
   else{
     
     this.btnStageOutAdd.setEnabled( true );
     this.btnStageOutDel.setEnabled( selectionAvailable );
     this.btnStageOutEdit.setEnabled( selectionAvailable );
   }
 } // End updateButtons
  
  
  
} // End Class

