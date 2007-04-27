/******************************************************************************
  * Copyright (c) 2007 g-Eclipse consortium
  * All rights reserved. This program and the accompanying materials
  * are made available under the terms of the Eclipse Public License v1.0
  * which accompanies this distribution, and is available at
  * http://www.eclipse.org/legal/epl-v10.html
  *
  * Initialial development of the original code was made for
  * project g-Eclipse founded by European Union
  * project number: FP6-IST-034327  http://www.geclipse.eu/
  *
  * Contributor(s):
  *     UCY (http://www.ucy.cs.ac.cy)
  *      - Nicholas Loulloudes (loulloudes.n@cs.ucy.ac.cy)
  *      - Emilia Stamou (emstamou@cs.ucy.ac.cy)
  *****************************************************************************/

package eu.geclipse.ui.jsdl.editor.pages;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import eu.geclipse.jsdl.adapters.jsdl.DataStagingAdapter;


public class DataStagingPage extends FormPage{
  
  Composite jobDataStaging = null;
  
  List lstFileName = null;
  Text txtFileSystemName = null;
  Text txtSource = null;
  Text txtTarget = null;
   
  Label lblFileName = null;
  Label lblFileSystemName = null;
  Label lblCreationFlag = null;
  Label lblDelOnTerm = null;
  Label lblSource = null;
  Label lblTarget = null;

  
  private DataStagingAdapter dataStagingAdapter;
  private Boolean contentRefreshed = false;
  
  
  // Constructor
  public DataStagingPage( final FormEditor editor )
                            
   {
    
    super(editor,Messages.DataStagingPage_pageId , 
          Messages.DataStagingPage_PageTitle);
    
   // breakTypes(list);
   
    }
  
  @Override
  
  // This method is used to create the Forms content by
  // creating the form layout and then creating the form
  // sub sections.
  protected void createFormContent(final IManagedForm managedForm) {
    
        
    ScrolledForm form = managedForm.getForm();
    form.setText(Messages.DataStagingPage_DataStagingPageTitle); 
  
    ColumnLayout layout = new ColumnLayout();
    
    // Set Max and Min Columns to be 1...
    layout.maxNumColumns = 1;
    layout.minNumColumns = 1;    
    form.getBody().setLayout(layout);
      

        
   this.jobDataStaging = createDataStagingSection(managedForm,
                                 Messages.DataStagingPage_PageTitle, 
                                 Messages.DataStagingPage_DataStagingDescr);  
   
   
   this.dataStagingAdapter.load();
  }
  
public void setActive(final boolean active) {
    
    if (active){
      if (isContentRefreshed()){    
        this.dataStagingAdapter.load();
      }//endif isContentRefreshed
    } // endif active
  }



  private boolean isContentRefreshed(){          
    return this.contentRefreshed;
  }
  
  
  public void setPageContent(final EObject rootJsdlElement, 
                             final boolean refreshStatus){

   if (refreshStatus) {
      this.contentRefreshed = true;
      this.dataStagingAdapter.setContent( rootJsdlElement );
    }
   else{
      this.dataStagingAdapter = new DataStagingAdapter(rootJsdlElement);     
   }
          
  } // End void getPageContent() 


  // This method is used to create individual subsections
  private Composite createSection(final IManagedForm mform, final String title,
                                final String desc, final int numColumns) {
    
   final ScrolledForm form = mform.getForm();
   FormToolkit toolkit = mform.getToolkit();
       
                   
   Section section = toolkit.createSection(form.getBody(), 
                                                  ExpandableComposite.TITLE_BAR 
                                                  | Section.DESCRIPTION 
                                                  | SWT.WRAP);

   section.clientVerticalSpacing = 5;   
   section.setText(title);
   section.setDescription(desc);
   toolkit.createCompositeSeparator(section);
   Composite client = toolkit.createComposite(section);
          
   GridLayout layout = new GridLayout();
   layout.verticalSpacing = 5;
   layout.marginTop = 10;
   layout.marginWidth = 0;
   layout.marginHeight = 0;
   layout.numColumns = numColumns;
   client.setLayout(layout);
   
   section.setClient(client);
      
   return client;
   
   }
  
  private Composite createSubSection(final Composite composite, 
                                     final IManagedForm mform,
                                     final String title, 
                                     final String desc, 
                                     final int numColumns,
                                     final int width,
                                     final int height ) 
  {
    
    FormToolkit toolkit = mform.getToolkit();
    
    Section subSection = toolkit.createSection(composite, ExpandableComposite.TITLE_BAR 
                                               | Section.DESCRIPTION 
                                               | SWT.WRAP);    
    subSection.setText(title);
    subSection.setDescription(desc);
    toolkit.createCompositeSeparator(subSection);
    GridData gd = new GridData();
    gd.widthHint =  width;
    gd.heightHint = height;
    subSection.setLayoutData( gd );
    
    Composite clientsubSection = toolkit.createComposite(subSection);
    GridLayout gridlayout = new GridLayout();
    gridlayout.numColumns = numColumns;
    clientsubSection.setLayout(gridlayout);
    
    
    subSection.setClient( clientsubSection );
    
    gd = new GridData();    
    this.lblFileName = toolkit.createLabel(clientsubSection, Messages.DataStagingPage_FileName);

    gd.widthHint = 300;
    gd.heightHint = 50;
          
    this.lstFileName = new List(clientsubSection, SWT.MULTI| SWT.BORDER);
    this.lstFileName.setLayoutData( gd );
    
 
   
    //this.txtFileName.setLayoutData(gd);
    
    gd = new GridData();
    //File System Name Label and Text box
    this.lblFileSystemName = toolkit.createLabel(clientsubSection,Messages.DataStagingPage_FileSystemName);
    this.txtFileSystemName = toolkit.createText(clientsubSection, "", SWT.BORDER); //$NON-NLS-1$
    gd.widthHint = 300;
    this.txtFileSystemName.setLayoutData(gd);
    
    
    //Creation Flag Label and Combo box
    this.lblCreationFlag = toolkit.createLabel(clientsubSection,Messages.DataStagingPage_CreationFlag);
    Combo comboCreationFlag = new Combo(clientsubSection,SWT.DROP_DOWN);
    comboCreationFlag.add(""); //$NON-NLS-1$
    comboCreationFlag.add(Messages.DataStagingPage_overwrite);
    comboCreationFlag.add(Messages.DataStagingPage_append);
    comboCreationFlag.add(Messages.DataStagingPage_dontOverwrite);
    gd.widthHint = 300;
    comboCreationFlag.setLayoutData(gd);

    //Delete On Termination Label and Combo box
    this.lblDelOnTerm = toolkit.createLabel(clientsubSection,Messages.DataStagingPage_DeleteOnTermination);
    Combo comboDelOnTerm = new Combo(clientsubSection,SWT.DROP_DOWN);
    comboDelOnTerm.add(""); //$NON-NLS-1$
    comboDelOnTerm.add(Messages.DataStagingPage_true);
    comboDelOnTerm.add(Messages.DataStagingPage_false);
    gd.widthHint = 300;
    comboDelOnTerm.setLayoutData(gd);
    
    //Source Label and Text box
    this.lblSource = toolkit.createLabel(clientsubSection,Messages.DataStagingPage_Source);
    this.txtSource = toolkit.createText(clientsubSection, "", SWT.BORDER); //$NON-NLS-1$
    gd.widthHint = 300;
    this.txtSource.setLayoutData(gd);
    
    //Target Label and Text box
    this.lblTarget = toolkit.createLabel(clientsubSection,Messages.DataStagingPage_Target);
    this.txtTarget = toolkit.createText(clientsubSection, "", SWT.BORDER); //$NON-NLS-1$
    gd.widthHint = 300;
    this.txtTarget.setLayoutData(gd);
    

    return clientsubSection;
  }
  
  

  
  
  
  

  /* 
   * Create the Data Staging Section
   * 
   */
  private Composite createDataStagingSection(final IManagedForm mform, 
                                          final String title, final String desc)
  {
   
    Composite client = createSection(mform, title, desc, 2);
    //FormToolkit toolkit = mform.getToolkit();
        
    this.jobDataStaging = createSubSection (client,mform,
                               Messages.DataStagingPage_Section
                              ,Messages.DataStagingPage_SectionDesc,2,480,300);
    
  
    
    return client;
}


}
