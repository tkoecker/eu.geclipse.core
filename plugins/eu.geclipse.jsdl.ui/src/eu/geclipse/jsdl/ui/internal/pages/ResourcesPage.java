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
 *     UCY (http://www.ucy.cs.ac.cy)
 *      - Nicholas Loulloudes (loulloudes.n@cs.ucy.ac.cy)
 *
  *****************************************************************************/

package eu.geclipse.jsdl.ui.internal.pages;


import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.TableWrapData;

import eu.geclipse.core.model.GridModel;
import eu.geclipse.core.model.IGridElement;
import eu.geclipse.core.model.IGridRoot;
import eu.geclipse.jsdl.model.base.JobDefinitionType;
import eu.geclipse.jsdl.ui.adapters.jsdl.JobDefinitionTypeAdapter;
import eu.geclipse.jsdl.ui.adapters.jsdl.JobIdentificationTypeAdapter;
import eu.geclipse.jsdl.ui.adapters.jsdl.ResourcesTypeAdapter;
import eu.geclipse.jsdl.ui.editors.JsdlEditor;
import eu.geclipse.jsdl.ui.internal.Activator;
import eu.geclipse.jsdl.ui.internal.dialogs.CandidateHostsDialog;
import eu.geclipse.jsdl.ui.internal.dialogs.FileSystemsDialog;
import eu.geclipse.jsdl.ui.internal.pages.sections.AdditionalResourceElemetsSection;
import eu.geclipse.jsdl.ui.internal.pages.sections.CandidateHostsSection;
import eu.geclipse.jsdl.ui.internal.pages.sections.CpuArchitectureSection;
import eu.geclipse.jsdl.ui.internal.pages.sections.ExclusiveExecutionSection;
import eu.geclipse.jsdl.ui.internal.pages.sections.FileSystemSection;
import eu.geclipse.jsdl.ui.internal.pages.sections.OperatingSystemSection;


/**
 * This class provides the Resources page that appears in the JSDL editor.
 */
public final class ResourcesPage extends FormPage implements INotifyChangedListener {
  
   
  protected static final String PAGE_ID = "RESOURCES";  //$NON-NLS-1$
  
  protected JobDefinitionType jobDefinitionType;
  protected ResourcesTypeAdapter resourcesTypeAdapter;
  protected Object[] value = null;  
  protected Composite body = null;
  protected Composite jobRescComposite = null;
  protected Composite left = null;
  protected Composite right = null; 
  protected Label lblMountPoint = null;
  protected Label lblMountSource = null;
  protected Label lblDiskSpace = null;
  protected Label lblFileSystemType = null;
  protected Label lblFileSystemDescr = null;
  protected Label lblOperSystType = null;
  protected Label lblOperSystVer = null;
  protected Label lblOSDescr = null;
  protected Label lblCPUArchName = null;
  protected Label lblIndCPUSpl = null;
  protected Label lblIndCPUTime = null;
  protected Label lblIndCPUCount = null;
  protected Label lblIndNetBand = null;
  protected Label lblPhysMem = null;
  protected Label lblVirtMem = null;
  protected Label lblIndDiskSpac = null;
  protected Label lblCPUTime = null;
  protected Label lblCPUCount = null;
  protected Label lblTotPhMem = null;
  protected Label lblTotVirtMem = null;
  protected Label lblTotDiskSp = null;
  protected Label lblTotResCount = null;
  protected Label lblFileSystemName = null;
  protected Label lblExclExecution = null;
  protected Combo cmbIndividualCPUSpeed = null;
  protected Combo cmbIndividualCPUTime = null;
  protected Combo cmbIndividualCPUCount = null;
  protected Combo cmbIndividualNetworkBandwidth = null;
  protected Combo cmbIndividualPhysicalMemory = null;
  protected Combo cmbIndividualVirtualMesmory = null;
  protected Combo cmbIndividualDiskSpace = null;
  protected Combo cmbTotalCPUTime = null;
  protected Combo cmbTotalCPUCount = null;
  protected Combo cmbTotalPhysicalMemory = null;
  protected Combo cmbTotalVirtualMemory = null;
  protected Combo cmbTotalDiskSpace = null;
  protected Combo cmbTotalResourceCount = null;  
  protected Button btnHostsAdd = null;
  protected Button btnHostsDel = null;
  protected Button btnFileSystemAdd = null;
  protected Button btnFileSystemDel = null;
  protected Button btnFileSystemEdit = null;  
  protected TableViewer hostsViewer = null;
  protected TableViewer fileSystemsViewer = null; 

   
  private ImageDescriptor helpDesc = null;
  private boolean contentRefreshed = false;
  private boolean dirtyFlag = false;
  private AdditionalResourceElemetsSection additionalResourceElemetsSection = null;
  private CandidateHostsSection candidateHostsSection = null;
  private OperatingSystemSection operatingSystemSection = null;
  private CpuArchitectureSection cpuArchitectureSection = null;
  private FileSystemSection fileSystemSection = null;
  private ExclusiveExecutionSection exclusiveExecutionSection = null;
  
  
  /**
   * <code>ResourcesPage</code> class constructor. Creates the page by
   * passing as an argument the container JSDL editor.
   * 
   * @param editor The JSDL editor.
   */
  public ResourcesPage( final FormEditor editor ) {
   
    super( editor, PAGE_ID ,
           Messages.getString("ResourcesPage_PageTitle") ); //$NON-NLS-1$
        
  } // End Class Constructor.
  
  
  
  /**
   * Returns the instance of the JSDL Editor that contains this page  
   *
   * @return JSDL editor that contains this page. 
   */
  public JsdlEditor getParentEditor() {
    return ( JsdlEditor )getEditor();
  }

  
  
  /*
   * (non-Javadoc)
   * @see org.eclipse.ui.forms.editor.FormPage#setActive(boolean)
   */
  @Override
  public void setActive(final boolean active) {
    
    if ( active ){
      if ( isContentRefreshed() ) {    
        this.candidateHostsSection.setInput( this.jobDefinitionType );
        this.additionalResourceElemetsSection.setInput( this.jobDefinitionType );
        this.operatingSystemSection.setInput( this.jobDefinitionType );
        this.cpuArchitectureSection.setInput( this.jobDefinitionType );
        this.fileSystemSection.setInput( this.jobDefinitionType );
        this.exclusiveExecutionSection.setInput( this.jobDefinitionType );
        
      } // end_if (isContentRefreshed())
    } //  end_if (active)
    
  } // End void setActive()
  
  
  /*
   * Checks if the Page Content has been refreshed. 
   */
  private boolean isContentRefreshed() {
    
    return this.contentRefreshed;
  }
  
  
  
  /**
   * Method that set's the Resources Page content. The content is the root 
   * JSDL element. Also this method is responsible to initialize the associated 
   * type adapters for the elements of this page. This method must be called only
   * from the JSDL Editor.
   * 
   * Associated Type Adapters for this page are: 
   * @see JobDefinitionTypeAdapter
   * @see JobIdentificationTypeAdapter
   *  
   * @param jobDefinitionRoot
   * 
   * @param refreshStatus
   * Set to TRUE if the original page content is already set, but there is a need
   * to refresh the page because there was a change to this content
   *  from an outside editor.
   * 
   */
  public void setPageContent( final JobDefinitionType jobDefinitionRoot, 
                              final boolean refreshStatus ){

   if ( refreshStatus ) {
      this.contentRefreshed = true;
   }
   this.jobDefinitionType = jobDefinitionRoot;   
          
  } // End void getPageContent() 
  
    
  @Override
  public boolean isDirty() {
    
    return this.dirtyFlag;
    
  }

    
  /**
   * This method set's the dirty status of the page.
   * 
   * @param dirty TRUE when the page is Dirty (content has been changed) and hence a 
   * Save operation is needed.
   * 
   */
  public void setDirty( final boolean dirty ) {
    if ( this.dirtyFlag != dirty ) {
      this.dirtyFlag = dirty;     
      this.getEditor().editorDirtyStateChanged();  
    }
    
  }

 
  @Override
  /*
   * This method is used to create the Forms content by
   * creating the form layout and then creating the form
   * Sub-Sections
   */
  protected void createFormContent( final IManagedForm managedForm ) {
            
    ScrolledForm form = managedForm.getForm();    
    FormToolkit toolkit = managedForm.getToolkit();
    
    form.setText( Messages.getString( "ResourcesPage_ResourcePageTitle") );  //$NON-NLS-1$
    
    this.body = form.getBody();
    this.body.setLayout( FormLayoutFactory.createFormTableWrapLayout( false, 2 ) );
  
  
    
    this.left = toolkit.createComposite( this.body );
    this.left.setLayout( FormLayoutFactory.createFormPaneTableWrapLayout (false, 1) );
    this.left.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
    
    this.right = toolkit.createComposite( this.body );
    this.right.setLayout( FormLayoutFactory.createFormPaneTableWrapLayout( false, 1 ) );
    this.right.setLayoutData( new TableWrapData( TableWrapData.FILL_GRAB ) );
    
    /* Create the Candidate Hosts Section */
    this.candidateHostsSection = new CandidateHostsSection(this, this.left, toolkit);
    this.candidateHostsSection.setInput( this.jobDefinitionType );
    this.candidateHostsSection.addListener( this );

    /* Create the Operating System Section */
    this.operatingSystemSection = new OperatingSystemSection(this.left, toolkit);
    this.operatingSystemSection.setInput( this.jobDefinitionType );
    this.operatingSystemSection.addListener( this );

    /* Create the File System Section */    
    this.fileSystemSection = new FileSystemSection(this.left, toolkit);
    this.fileSystemSection.setInput( this.jobDefinitionType );
    this.fileSystemSection.addListener( this );
   
    /* Create the CPU Architecture Section */
    this.cpuArchitectureSection = new CpuArchitectureSection(this.right, toolkit);
    this.cpuArchitectureSection.setInput( this.jobDefinitionType );
    this.cpuArchitectureSection.addListener( this );

    /* Create the Exclusive Execution Section */
    this.exclusiveExecutionSection = new ExclusiveExecutionSection(this.right, toolkit);
    this.exclusiveExecutionSection.setInput( this.jobDefinitionType );
    this.exclusiveExecutionSection.addListener( this );
      
      
    
    /* Create the Additional Elements Section */
    this.additionalResourceElemetsSection = new AdditionalResourceElemetsSection(this.right, toolkit);
    this.additionalResourceElemetsSection.setInput( this.jobDefinitionType );
    this.additionalResourceElemetsSection.addListener( this );
   
   /* Set Form Background */
   form.setBackgroundImage( Activator.getDefault().
                            getImageRegistry().get( "formsbackground" ) ); //$NON-NLS-1$

   /* Also add the help system */
   addFormPageHelp( form );
   
  }
  
  
  private void addFormPageHelp( final ScrolledForm form ) {
    
    final String href = getHelpResource();
    if ( href != null ) {
        IToolBarManager manager = form.getToolBarManager();
        Action helpAction = new Action( "help" ) { //$NON-NLS-1$
            @Override
            public void run() {
                BusyIndicator.showWhile( form.getDisplay(), new Runnable() {
                    public void run() {
                        PlatformUI.getWorkbench().getHelpSystem().displayHelpResource( href );
                    }
                } );
            }
        };
        helpAction.setToolTipText( Messages.getString( "ResourcesPage_Help" ) );  //$NON-NLS-1$
        URL stageInURL = Activator.getDefault().getBundle().getEntry( "icons/help.gif" ); //$NON-NLS-1$       
        this.helpDesc = ImageDescriptor.createFromURL( stageInURL ) ;   
        helpAction.setImageDescriptor( this.helpDesc);
        manager.add( helpAction );
        form.updateToolBar();
    }
    
  }
    
  
  /*
   * Method which opens a Dialog for selecting Candidate Hosts for Job Submission.
   */
  @SuppressWarnings("unchecked")
  protected void handleAddDialog( final String dialogTitle ) {
    
    this.value = null;
    
    CandidateHostsDialog hostsDialog = new CandidateHostsDialog( this.body.getShell(), dialogTitle );
    
    IFile file = ( (IFileEditorInput) this.getEditor().getEditorInput() ).getFile();
    IGridRoot root = GridModel.getRoot();
    IGridElement element = root.findElement( file );
    hostsDialog.setDialogInput( element );
    hostsDialog.setExistingCandidateHosts( this.hostsViewer.getInput() );
 
    if( hostsDialog.open() != Window.OK ) {
      return;
        
    }    
      this.value = hostsDialog.getValue();
    
  }  
  
  
  /*
   * Method which opens a Dialog for adding new File Systems.
   */
  protected void handleAddFsDialog( final String dialogTitle, final Button button ){
    
    this.value = null;
    
    FileSystemsDialog fileSystemDialog = new FileSystemsDialog( this.body.getShell(), dialogTitle );

    /* Edit Element */ 
    if (button != this.btnFileSystemAdd ) {
       IStructuredSelection structSelection 
                   = ( IStructuredSelection ) this.fileSystemsViewer.getSelection();
       
       fileSystemDialog.setInput( structSelection.getFirstElement() );

    }  
 
    if( fileSystemDialog.open() != Window.OK ) {
      return;        
    }
    
      this.value = fileSystemDialog.getValue();
    
  }
  
  
  public void notifyChanged( final Notification notification ) {
    setDirty( true );    
  }
  
  
  
  protected String getHelpResource() {
    return "/eu.geclipse.doc.user/html/concepts/jobmanagement/editorpages/resources.html"; //$NON-NLS-1$
  }
  
  /*
   * This function updates the status of the buttons related to
   * the respective Stage-In and Stage-Out Table Viewers. The Status of the 
   * buttons is adjusted according to the selection and content of the respective
   * table viewer.
   * 
   */ 
    protected void updateButtons( final TableViewer tableViewer ) {
    
    ISelection selection = tableViewer.getSelection();
    boolean selectionAvailable = !selection.isEmpty();    
    
    if (tableViewer == this.fileSystemsViewer) {
    
      this.btnFileSystemAdd.setEnabled( true );
      this.btnFileSystemEdit.setEnabled( selectionAvailable );
      this.btnFileSystemDel.setEnabled( selectionAvailable );
    }
    else {     
      this.btnHostsAdd.setEnabled( true );
      this.btnHostsDel.setEnabled( selectionAvailable );
    }
    
  } // End updateButtons    


} // end ResourcesPage class
