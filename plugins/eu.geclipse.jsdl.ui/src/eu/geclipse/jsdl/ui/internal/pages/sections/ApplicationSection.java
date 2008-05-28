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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

import eu.geclipse.jsdl.model.base.ApplicationType;
import eu.geclipse.jsdl.model.base.JobDefinitionType;
import eu.geclipse.jsdl.model.base.JsdlFactory;
import eu.geclipse.jsdl.ui.adapters.jsdl.JsdlAdaptersFactory;
import eu.geclipse.jsdl.ui.internal.pages.FormSectionFactory;
import eu.geclipse.jsdl.ui.internal.pages.Messages;


/**
 * @author nloulloud
 *
 */
public class ApplicationSection extends JsdlAdaptersFactory {
  
  
  private static final int WIDGET_HEIGHT = 100;  
  protected Label lblApplicationName = null;
  protected Label lblApplicationVersion = null;
  protected Label lblApplicationDescription = null;
  protected Text txtApplicationName = null;
  protected Text txtApplicationVersion = null;
  protected Text txtDescription = null;
  protected JobDefinitionType jobDefinitionType = JsdlFactory.eINSTANCE.createJobDefinitionType();  
  protected ApplicationType applicationType = JsdlFactory.eINSTANCE.createApplicationType();
  
  private boolean isNotifyAllowed = true;
  private boolean adapterRefreshed = false;
  
  
  /**
   * @param parent The parent composite.
   * @param toolkit The parent Form Toolkit.
   */
  public ApplicationSection( final Composite parent, final FormToolkit toolkit ){
        
    createSection( parent, toolkit );
    
  }
  
  
  /*
   * Create the Application Section which includes the following: -Application
   * Name (String) -Application Version (String) -Application Description
   * (String)
   */
  private void createSection( final Composite parent, final FormToolkit toolkit ) {
    
    String sectionTitle = Messages.getString( "JobApplicationPage_Applicationtitle" ); //$NON-NLS-1$
    String sectionDescription = Messages.getString( "JobApplicationPage_ApplicationDescription" ); //$NON-NLS-1$
    GridData gd;
    Composite client = FormSectionFactory.createGridStaticSection( toolkit,
                                                                   parent,
                                                                   sectionTitle,
                                                                   sectionDescription,
                                                                   2 );
    
    /* ========================= Application Name ============================ */
    this.lblApplicationName = toolkit
                      .createLabel( client, Messages.getString( "JobApplicationPage_ApplicationName" ) ); //$NON-NLS-1$
    this.txtApplicationName = toolkit.createText( client, "", SWT.NONE ); //$NON-NLS-1$
    gd = new GridData();
    gd.widthHint = 300;
    this.txtApplicationName.setLayoutData( gd );
    this.txtApplicationName.addModifyListener( new ModifyListener(){

      public void modifyText( final ModifyEvent e ) {
        ApplicationSection.this.applicationType
                                          .setApplicationName( ApplicationSection.this.txtApplicationName.getText() );
        contentChanged();
        
      }
      
    });
 
    /* ========================= Application Version ============================ */
    this.lblApplicationVersion = toolkit
                                  .createLabel( client,
                                         Messages.getString( "JobApplicationPage_ApplicationVersion" ) ); //$NON-NLS-1$
    
    this.txtApplicationVersion = toolkit.createText( client, "", SWT.NONE ); //$NON-NLS-1$
    gd = new GridData();
    gd.widthHint = 300;
    this.txtApplicationVersion.setLayoutData( gd );
    this.txtApplicationVersion.addModifyListener( new ModifyListener() {
      
      public void modifyText( final ModifyEvent e ) {
        ApplicationSection.this.applicationType
                                     .setApplicationVersion( ApplicationSection.this.txtApplicationVersion.getText() );
        contentChanged();
        
      }
    } );

    /* ========================= Application Description ============================ */
    this.lblApplicationDescription = toolkit
                                       .createLabel( client,
                                           Messages.getString( "JobApplicationPage_ApplicationDescr" ) ); //$NON-NLS-1$
    
    this.txtDescription = toolkit.createText( client, "", //$NON-NLS-1$
                                              SWT.NONE
                                                  | SWT.V_SCROLL
                                                  | SWT.WRAP );
    gd = new GridData();
    gd.verticalSpan = 3;
    gd.widthHint = 285;
    gd.heightHint = WIDGET_HEIGHT;
    this.txtDescription.setLayoutData( gd );
    this.txtDescription.addModifyListener( new ModifyListener(){

      public void modifyText( final ModifyEvent e ) {
        ApplicationSection.this.applicationType
                                     .setDescription( ApplicationSection.this.txtDescription.getText() );
        contentChanged();
        
      }
      
    });
    
    toolkit.paintBordersFor( client );
    
  } // End void createApplicationSection()
  
  
  protected void contentChanged() {
    
    if ( this.isNotifyAllowed ){
      fireNotifyChanged( null );
    }
    
  }
  
  
  /**
   * @param jobDefinition The JSDL Job Definition element.
   */
  public void setInput( final JobDefinitionType jobDefinition ) {
    
    this.adapterRefreshed = true;
    if( null != jobDefinition ) {
      this.jobDefinitionType = jobDefinition;
      this.applicationType = this.jobDefinitionType.getJobDescription().getApplication();        
      fillFields();
    }
    
  }
  
  
  private void fillFields() {
    
    this.isNotifyAllowed = false;
    
    if( null != this.applicationType ) {
      
      if( null != this.applicationType.getApplicationName() ){
        this.txtApplicationName.setText( this.applicationType.getApplicationName() );
      }
      if ( null != this.applicationType.getApplicationVersion() ){
        this.txtApplicationVersion.setText( this.applicationType.getApplicationVersion() );
      }
      if ( null != this.applicationType.getDescription() ){
        this.txtDescription.setText( this.applicationType.getDescription() );
      }
      
    }
    this.isNotifyAllowed = true;
    if( this.adapterRefreshed ){
      this.adapterRefreshed = false;
    }
    
  }
  
}