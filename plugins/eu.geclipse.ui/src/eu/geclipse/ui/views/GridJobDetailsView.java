/******************************************************************************
 * Copyright (c) 2006 g-Eclipse consortium 
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
 *     PSNC - Mariusz Wojtysiak
 *           
 *****************************************************************************/

package eu.geclipse.ui.views;

import java.util.LinkedList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.ViewPart;
import eu.geclipse.core.model.IGridJob;

/**
 * View showing details about submitted job 
 * @author PSNC - Mariusz Wojtysiak
 *
 */
public class GridJobDetailsView extends ViewPart {

  /**
   * View identifier
   */
  static final public String ID = "eu.geclipse.ui.views.GridJobDetailsView"; //$NON-NLS-1$
  
  private final AbstractSection[] sections = new AbstractSection[1];
  private Composite topComposite = null;
  private FormToolkit formToolkit = null;
  private IGridJob inputJob = null;
    
  @Override
  public void createPartControl( final Composite parentComposite )
  {
    int sectionIndex = 0;
    
    this.topComposite = getFormToolkit().createComposite( parentComposite );
    this.topComposite.setLayout( new GridLayout( 1, false ) );
    this.topComposite.setBackground( getFormToolkit().getColors().getBackground() );

    this.sections[ sectionIndex++ ] = new GeneralSection( this.topComposite );
    this.topComposite.layout();
  }

  @Override
  public void setFocus()
  {
    // TODO Auto-generated method stub
  }
  
  /**
   * Set job, which will be displayed by this view.
   * View will be refreshed automatically
   * @param gridJob
   */
  public void setInputJob( final IGridJob gridJob ) {
    this.inputJob = gridJob;
    refresh();
  }
  
  private void refresh() {
    refreshViewTitle( this.inputJob );
    
    for( AbstractSection section : this.sections ) {
      section.clear();
      
      if( this.inputJob != null ){
        section.refresh( this.inputJob );
      }
    }
    
    this.topComposite.layout();
  }  

  /**
   * This method initializes formToolkit
   *
   * @return org.eclipse.ui.forms.widgets.FormToolkit
   */
  protected FormToolkit getFormToolkit() {
    if( this.formToolkit == null ) {
      this.formToolkit = new FormToolkit( Display.getCurrent() );
    }
    return this.formToolkit;
  }
  
  private void refreshViewTitle( final IGridJob gridJob ){
    String nameString = null;
    
    if( this.inputJob != null ){
      nameString = this.inputJob.getName();
    }
    
    if( nameString == null ){
      nameString = Messages.getString( "GridJobDetailsView.ViewName" ); //$NON-NLS-1$
    }

    setPartName( nameString );
  }

  abstract private class AbstractSection {
    private final Composite clientComposite;
    private final Section section;
    private final LinkedList< Label > itemsList = new LinkedList< Label >();
    
    AbstractSection( final Composite parentComposite, final String nameString ) {
      super();
      this.section = getFormToolkit().createSection( parentComposite,
                                                       ExpandableComposite.TWISTIE | ExpandableComposite.TITLE_BAR );
      this.section.setText( nameString );
      this.section.setExpanded( true );
      this.clientComposite = getFormToolkit().createComposite( this.section, SWT.NONE );
      this.clientComposite.setLayout( new GridLayout( 2, false ) );
      this.section.setClient( this.clientComposite );
    }
        
    /**
     * Refresh view section fields during initialization of when job was changed
     * @param gridJob - job source
     */
    abstract protected void refresh( final IGridJob gridJob );
    
    /**
     * Clear (make empty) all values visibled in section.
     * Called before refresh() to avoid showing old values 
     */
    protected void clear(){
      for( Label itemLabel : this.itemsList ){
        itemLabel.setText( "" );  //$NON-NLS-1$
      }
    }
    
    protected Composite getClientComposite(){
      return this.clientComposite;
    }
    
    protected Label addSectionItem( final String nameString ) {
      getFormToolkit().createLabel( getClientComposite(), nameString );
      Label valueLabel = getFormToolkit().createLabel( getClientComposite(), null );
      this.itemsList.add( valueLabel );
      return valueLabel;
    }
    
    protected void safeSetItemValue( final Label itemLabel, final String valueString ){
      itemLabel.setText( valueString == null ? "" : valueString );       //$NON-NLS-1$
    }
    
    }
  
  private class GeneralSection extends AbstractSection {
    private final Label jobNameLabel;
    private final Label jobIdLabel;
    private final Label jobStatusLabel;
    private final Label jobRefreshedAtLabel;

    GeneralSection( final Composite parentComposite ) {
      super( parentComposite, Messages.getString( "GridJobDetailsView.sectionGeneral" ) ); //$NON-NLS-1$
                
      this.jobNameLabel = addSectionItem( Messages.getString( "GridJobDetailsView.labelJobName" ) );  //$NON-NLS-1$
      this.jobIdLabel = addSectionItem( Messages.getString( "GridJobDetailsView.labelJobId" ) );  //$NON-NLS-1$
      this.jobStatusLabel = addSectionItem( Messages.getString( "GridJobDetailsView.labelJobStatus" ) );  //$NON-NLS-1$
      this.jobRefreshedAtLabel = addSectionItem( Messages.getString( "GridJobDetailsView.labelJobStatusDate" ) );  //$NON-NLS-1$      
    }

    @Override
    protected void refresh( final IGridJob gridJob )
    {
      safeSetItemValue( this.jobNameLabel, gridJob.getName() );
      
      if( gridJob.getID() != null ){
        safeSetItemValue( this.jobIdLabel, gridJob.getID().getJobID() );
      }
      
      if( gridJob.getJobStatus() != null ){
        safeSetItemValue( this.jobStatusLabel, gridJob.getJobStatus().getName() );
       }
      
      safeSetItemValue( this.jobRefreshedAtLabel, "TODO" ); //TODO mariusz jobRefreshedAtLabel 
    }    
  }
}
