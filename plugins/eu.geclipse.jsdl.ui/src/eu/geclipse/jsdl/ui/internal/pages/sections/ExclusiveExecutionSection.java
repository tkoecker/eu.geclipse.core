/******************************************************************************
 * Copyright (c) 2008 g-Eclipse consortium
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
package eu.geclipse.jsdl.ui.internal.pages.sections;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;

import eu.geclipse.jsdl.model.base.JobDefinitionType;
import eu.geclipse.jsdl.model.base.JobDescriptionType;
import eu.geclipse.jsdl.model.base.JsdlFactory;
import eu.geclipse.jsdl.model.base.JsdlPackage;
import eu.geclipse.jsdl.model.base.ResourcesType;
import eu.geclipse.jsdl.ui.internal.pages.FormSectionFactory;
import eu.geclipse.jsdl.ui.internal.pages.Messages;


/**
 * @author nloulloud
 *
 * This class is responsible for displaying the Exclusive Execution section in the 
 * Resources Page of the JSDL editor. It provides widgets to manipulate the 
 * ExclusiveExecutionType element specified in the "Resources Elements" section of the 
 * Job Submission Description Language (JSDL) Specification, Version 1.0.
 */
public class ExclusiveExecutionSection extends JsdlFormPageSection {
  
  protected JobDescriptionType jobDescriptionType = JsdlFactory.eINSTANCE.createJobDescriptionType();
  protected ResourcesType resourcesType = JsdlFactory.eINSTANCE.createResourcesType();
  protected Label lblExclExecution = null;
  protected Combo cmbExclExec = null;
  
    
  /**
   * Class constructor. Creates the section.
   *  
   * @param parent The parent composite.
   * @param toolkit The parent Form Toolkit.
   */
  public ExclusiveExecutionSection(final Composite parent, final FormToolkit toolkit ){
    
   createSection( parent,toolkit );
    
  }
  
  
  /**
   * Set the Input of this section. The input of this section is the 
   * ResourcesType contained in the JobDefinitionType. 
   * 
   * @param jobDefinitionType The Job Definition type of the JSDL Document.
   */
  public void setInput( final JobDefinitionType jobDefinitionType ) { 

    this.adapterRefreshed = true;
    this.jobDescriptionType = jobDefinitionType.getJobDescription();
    
    if ( this.jobDescriptionType.getResources() != null ) {
      this.resourcesType = this.jobDescriptionType.getResources();
    }
    fillFields();
    
  }
    
  
//  protected void contentChanged() {
//    
//    if (this.isNotifyAllowed){
//      fireNotifyChanged( null);
//    }
//    
//  }
  
  
  
  /*
   * Private Method that creates the CPU Architecture Sub-Section
   */
  private void createSection ( final Composite parent,
                                                 final FormToolkit toolkit ) {
    
    String sectionTitle = Messages.getString( "ResourcesPage_ExclExecSection" ); //$NON-NLS-1$
    String sectionDescription = Messages.getString( "ResourcesPage_ExclExecDescr" ); //$NON-NLS-1$

    TableWrapData td;
       
    Composite client = FormSectionFactory.createStaticSection( toolkit,
                                           parent,
                                           sectionTitle,
                                           sectionDescription,
                                           2 );
     
     td = new TableWrapData( TableWrapData.FILL_GRAB );
    
    /*======================= Exclusive Execution Widgets ====================*/
    
    this.lblExclExecution = toolkit.createLabel( client,
                             Messages.getString( "ResourcesPage_ExclExec" ) ); //$NON-NLS-1$
    
    this.cmbExclExec = new Combo( client, SWT.SIMPLE
                                  | SWT.DROP_DOWN 
                                  | SWT.READ_ONLY );
    
    this.cmbExclExec.setData( FormToolkit.KEY_DRAW_BORDER );
    
    
    /* 
     * Add an EMPTY item value so that the user can disable the specific 
     * feature 
     */
    this.cmbExclExec.add(EMPTY_STRING);
    this.cmbExclExec.add( "true" ); //$NON-NLS-1$
    this.cmbExclExec.add( "false" ); //$NON-NLS-1$
        
    this.cmbExclExec.addSelectionListener( new SelectionListener() {
      public void widgetSelected( final SelectionEvent e ) {
        
        /*
         * If the EMPTY item is selected then the ExclusiveExecution
         * element has to be unset.
         */
        if (ExclusiveExecutionSection.this.cmbExclExec.getItem( ExclusiveExecutionSection
                                                      .this.cmbExclExec.getSelectionIndex() ).equals( EMPTY_STRING )) {
          
          ExclusiveExecutionSection.this.resourcesType.unsetExclusiveExecution();
          checkResourcesElement();
                              
        }
        else {

         checkResourcesElement();
         ExclusiveExecutionSection.this.resourcesType
               .setExclusiveExecution( Boolean.parseBoolean( ExclusiveExecutionSection.this.cmbExclExec.getText() ) );
        }
        
         contentChanged();
        
      }

      public void widgetDefaultSelected( final SelectionEvent e ) {
        // Do Nothing
      }
    });

    this.cmbExclExec.setLayoutData( td );
    
    toolkit.paintBordersFor( client);    

  } //End void cPUArch()
  
    
  
  protected void checkResourcesElement() {
    
    EStructuralFeature eStructuralFeature = this.jobDescriptionType.eClass()
          .getEStructuralFeature( JsdlPackage.JOB_DESCRIPTION_TYPE__RESOURCES );
    
    /*
     * Check if the Resources element is not set. If not set then set it to its 
     * container (JobDescriptionType).
     */
    if (!this.jobDescriptionType.eIsSet( eStructuralFeature )) {      
      this.jobDescriptionType.eSet( eStructuralFeature, this.resourcesType );
    }
    /* 
     * If the Resources Element is set, check for any possible contents which may
     * be set. Also check if the Exclusive Execution attribute is set.
     * If none of the above are true, then delete the Resources Element from it's
     * container (JobDescriptionType).
     */
    else {
      if ( !this.resourcesType.isSetExclusiveExecution() && this.resourcesType.eContents().size() == 0) {
        EcoreUtil.remove( this.resourcesType );
      }
    }
    
  } // end void checkResourcesElement()
  
  
  
  private void fillFields() {
    
    this.isNotifyAllowed = false;
    
    if ( null != this.jobDescriptionType.getResources() ) {
      this.resourcesType = this.jobDescriptionType.getResources();
      if (this.resourcesType.isSetExclusiveExecution() == true){
        boolean value = this.resourcesType.isExclusiveExecution();    
        this.cmbExclExec.setText( Boolean.toString( value ) );
      } else {
        this.cmbExclExec.setText( EMPTY_STRING );
      }
    }
    
   
    this.isNotifyAllowed = true;
    
    if ( this.adapterRefreshed ) {
      this.adapterRefreshed = false;
    }
  }
  
  protected boolean isResourceElementEmpty(){
    
    boolean returnValue = true;
    
    if ( this.resourcesType.eContents().size() > 0  ){
      returnValue = false;
    }
    
    return returnValue;
    
  }
  
}
