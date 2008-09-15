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
 *     PSNC: 
 *      - Katarzyna Bylec (katis@man.poznan.pl)
 *           
 *****************************************************************************/
package eu.geclipse.jsdl.ui.internal.pages.sections;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.EStructuralFeatureImpl.ContainmentUpdatingFeatureMapEntry;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.eclipse.emf.ecore.util.FeatureMap.Entry;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.forms.widgets.FormToolkit;

import eu.geclipse.jsdl.model.base.JobDefinitionType;
import eu.geclipse.jsdl.model.functions.ExceptionType;
import eu.geclipse.jsdl.model.functions.FunctionsFactory;
import eu.geclipse.jsdl.model.functions.FunctionsPackage;
import eu.geclipse.jsdl.model.functions.LoopType;
import eu.geclipse.jsdl.model.functions.ValuesType;
import eu.geclipse.jsdl.model.functions.impl.FunctionsPackageImpl;
import eu.geclipse.jsdl.model.sweep.AssignmentType;
import eu.geclipse.jsdl.model.sweep.SweepFactory;
import eu.geclipse.jsdl.model.sweep.SweepPackage;
import eu.geclipse.jsdl.model.sweep.SweepType;
import eu.geclipse.jsdl.model.sweep.impl.SweepPackageImpl;
import eu.geclipse.jsdl.ui.adapters.jsdl.ParametricJobAdapter;
import eu.geclipse.jsdl.ui.internal.pages.FormSectionFactory;
import eu.geclipse.jsdl.ui.providers.parameters.SweepOrderCProvider;
import eu.geclipse.jsdl.ui.providers.parameters.SweepOrderLProvider;
import eu.geclipse.jsdl.ui.widgets.ParametersDialog;
import eu.geclipse.jsdl.ui.widgets.SweepDeleteDialog;
import eu.geclipse.jsdl.ui.widgets.SweepFunctionDialog;

public class SweepOrderSection extends JsdlFormPageSection {

  TreeViewer viewer;
  Combo sweepCombo;
  Text textArea;
  ParametricJobAdapter adapter;
  ArrayList<SweepType> inerSweepList;
  private JobDefinitionType jobDefinitionType;
  private List<SweepType> sweepType = new ArrayList<SweepType>();
  private MenuManager manager;
  private Shell shell;
  private Button newButton;
  private Button independentButton;
  private Button sameLevelButton;
  private Button innerButton;
  private Button deleteButton;
  private Button addFunctionButton;

  /**
   * Constructor.
   * 
   * @param parent parent composite of this section
   * @param toolkit forms toolkit to use
   * @param adapter adapter handling operations on parametric part of JSDL
   */
  public SweepOrderSection( final Composite parent,
                            final FormToolkit toolkit,
                            final ParametricJobAdapter adapter )
  {
    this.adapter = adapter;
    createSection( parent, toolkit );
  }

  public void setInput( final JobDefinitionType jobDefinition ) {
    this.adapterRefreshed = true;
    this.sweepType = new ArrayList<SweepType>();
    if( null != jobDefinition ) {
      this.jobDefinitionType = jobDefinition;
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
      // check all root sweeps' children
      this.inerSweepList = new ArrayList<SweepType>();
      if( !this.sweepType.isEmpty() ) {
        for( SweepType type : this.sweepType ) {
          TreeIterator<EObject> it = type.eAllContents();
          while( it.hasNext() ) {
            EObject testObject = it.next();
            if( testObject instanceof SweepType ) {
              this.inerSweepList.add( ( SweepType )testObject );
            }
          }
        }
        this.inerSweepList.addAll( this.sweepType );
      }
      fillFields();
    }
  }

  private void fillFields() {
    if( this.viewer != null ) {
      this.viewer.setInput( this.sweepType.toArray( new SweepType[ 0 ] ) );
      this.viewer.refresh();
    }
    refreshSweepCombo();
  }

  private void refreshSweepCombo() {
    if( this.sweepCombo != null ) {
      this.sweepCombo.removeAll();
      for( String name : getInerSweepNames() ) {
        this.sweepCombo.add( name );
      }
    }
  }

  private void createSection( final Composite parent, final FormToolkit toolkit )
  {
    this.shell = parent.getShell();
    String sectionTitle = "Sweep order";
    String sectionDescription = "Specify the order of parameters sweep and their dependency from each other";
    Composite client = FormSectionFactory.createGridStaticSection( toolkit,
                                                                   parent,
                                                                   sectionTitle,
                                                                   sectionDescription,
                                                                   3 );
    Composite treeComp = toolkit.createComposite( client );
    GridData gData = new GridData( GridData.FILL_BOTH );
    gData.horizontalSpan = 3;
    treeComp.setLayoutData( gData );
    treeComp.setLayout( new GridLayout( 2, false ) );
    this.viewer = new TreeViewer( treeComp, SWT.BORDER );
    this.viewer.setContentProvider( new SweepOrderCProvider() );
    this.viewer.setLabelProvider( new SweepOrderLProvider() );
    gData = new GridData( GridData.GRAB_HORIZONTAL );
    gData = new GridData();
    gData.horizontalAlignment = GridData.FILL;
    gData.verticalAlignment = GridData.FILL;
    gData.grabExcessHorizontalSpace = true;
    gData.verticalSpan = 6;
    gData.heightHint = 300;
    this.viewer.getTree().setLayoutData( gData );
    this.viewer.addSelectionChangedListener( new ISelectionChangedListener() {

      public void selectionChanged( final SelectionChangedEvent event ) {
        ISelection sel = SweepOrderSection.this.viewer.getSelection();
        if( sel instanceof StructuredSelection ) {
          StructuredSelection sSel = ( StructuredSelection )sel;
          if( sSel.getFirstElement() instanceof SweepType ) {
            SweepType type = ( SweepType )sSel.getFirstElement();
            int i = 0;
            boolean found = false;
            for( String item : SweepOrderSection.this.sweepCombo.getItems() ) {
              if( item.equals( getNameForSweep( type ) ) ) {
                found = true;
                break;
              }
              i++;
            }
            if( found ) {
              SweepOrderSection.this.sweepCombo.select( i );
              setValuesField( SweepOrderSection.this.adapter.getValuesForParameter( SweepOrderSection.this.sweepCombo.getItem( i ),
                                                                                    SweepOrderSection.this.inerSweepList ) );
            }
          }
        }
        updateButtons();
      }
    } );
    this.manager = createMenu();
    // buttons composite
    Composite buttonComp = toolkit.createComposite( treeComp );
    buttonComp.setLayout( new GridLayout( 1, true ) );
    this.newButton = toolkit.createButton( buttonComp, "New sweep...", SWT.PUSH );
    this.newButton.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
    this.independentButton = toolkit.createButton( buttonComp,
                                                   "Independent sweep...",
                                                   SWT.PUSH );
    gData = new GridData( GridData.FILL_HORIZONTAL );
    gData.verticalIndent = 15;
    this.independentButton.setLayoutData( gData );
    this.sameLevelButton = toolkit.createButton( buttonComp,
                                                 "Sweep on the same level...",
                                                 SWT.PUSH );
    this.sameLevelButton.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
    this.innerButton = toolkit.createButton( buttonComp,
                                             "Inner sweep...",
                                             SWT.PUSH );
    this.innerButton.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
    this.deleteButton = toolkit.createButton( buttonComp, "Delete", SWT.PUSH );
    gData = new GridData( GridData.FILL_HORIZONTAL );
    gData.verticalIndent = 15;
    this.deleteButton.setLayoutData( gData );
    updateButtons();
    // values controls
    toolkit.createLabel( client, "Sweep element" );
    this.sweepCombo = new Combo( client, SWT.NONE | SWT.READ_ONLY );
    gData = new GridData( GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL );
    gData.widthHint = 200;
    gData.horizontalSpan = 2;
    this.sweepCombo.setLayoutData( gData );
    this.sweepCombo.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( final SelectionEvent e ) {
        setValuesField( SweepOrderSection.this.adapter.getValuesForParameter( SweepOrderSection.this.sweepCombo.getText(),
                                                                              SweepOrderSection.this.inerSweepList ) );
        updateButtons();
      }
    } );
    toolkit.createLabel( client,
                         "Parameter values\n(put each value in new line)" );
    this.textArea = new Text( client, SWT.MULTI
                                      | SWT.WRAP
                                      | SWT.V_SCROLL
                                      | SWT.BORDER );
    gData = new GridData( GridData.FILL_BOTH
                          | GridData.GRAB_HORIZONTAL
                          | GridData.GRAB_VERTICAL );
    gData.heightHint = 70;
    gData.widthHint = 160;
    this.textArea.setLayoutData( gData );
    this.textArea.addKeyListener( new KeyListener() {

      public void keyPressed( final KeyEvent e ) {
        List<String> val = new ArrayList<String>();
        for( String value : SweepOrderSection.this.textArea.getText()
          .split( System.getProperty( "line.separator" ) ) ) //$NON-NLS-1$
        {
          val.add( value );
        }
        SweepType sweep = SweepOrderSection.this.adapter.findSweepElement( SweepOrderSection.this.sweepCombo.getText(),
                                                                           SweepOrderSection.this.inerSweepList );
        AssignmentType assignment = null;
        if( sweep != null && sweep.getAssignment() != null ) {
          for( int j = 0; j < sweep.getAssignment().size(); j++ ) {
            if( ( ( AssignmentType )sweep.getAssignment().get( j ) ).getParameter()
              .contains( SweepOrderSection.this.sweepCombo.getText() ) )
            {
              assignment = ( AssignmentType )sweep.getAssignment().get( j );
              break;
            }
          }
        }
        setFunctionValues( assignment, val );
        updateButtons();
        contentChanged();
      }

      public void keyReleased( final KeyEvent e ) {
        // do nothing
      }
    } );
    // buttons values composite
    Composite buttonValComp = toolkit.createComposite( client );
    buttonValComp.setLayout( new GridLayout( 1, true ) );
    this.addFunctionButton = toolkit.createButton( buttonValComp,
                                                   "Define loop...",
                                                   SWT.PUSH );
    gData = new GridData();
    this.addFunctionButton.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( final SelectionEvent e ) {
        SweepFunctionDialog dialog = new SweepFunctionDialog( SweepOrderSection.this.shell );
        if( dialog.open() == Window.OK ) {
          List<BigInteger> exceptions = new ArrayList<BigInteger>();
          if( dialog.getExceptionsReturn() != null ) {
            for( String exc : dialog.getExceptionsReturn() ) {
              exceptions.add( new BigInteger( exc ) );
            }
          }
          appendTextToTextArea( SweepOrderSection.this.adapter.createLOOPString( new BigInteger( dialog.getStartReturn() ),
                                                                                 new BigInteger( dialog.getEndReturn() ),
                                                                                 new BigInteger( dialog.getStepReturn() ),
                                                                                 exceptions ) );
          updateButtons();
        }
      }
    } );
    addFunctionButton.setLayoutData( gData );
    updateButtons();
    addButtonsListeners();
  }

  protected void appendTextToTextArea( final String string ) {
    String newText = this.textArea.getText();
    newText = newText.trim() + System.getProperty( "line.separator" ) + string;
    this.textArea.setText( newText.trim() );
    List<String> val = new ArrayList<String>();
    for( String value : SweepOrderSection.this.textArea.getText()
      .split( System.getProperty( "line.separator" ) ) ) //$NON-NLS-1$
    {
      val.add( value );
    }
    SweepType sweep = SweepOrderSection.this.adapter.findSweepElement( sweepCombo.getText(),
                                                                       inerSweepList );
    AssignmentType assignment = null;
    if( sweep.getAssignment() != null ) {
      for( int j = 0; j < sweep.getAssignment().size(); j++ ) {
        if( ( ( AssignmentType )sweep.getAssignment().get( j ) ).getParameter()
          .contains( SweepOrderSection.this.sweepCombo.getText() ) )
        {
          assignment = ( AssignmentType )sweep.getAssignment().get( j );
          break;
        }
      }
    }
    setFunctionValues( assignment, val );
    updateButtons();
    contentChanged();
  }

  private void addButtonsListeners() {
    this.newButton.addSelectionListener( new SelectionAdapter() {

      /*
       * (non-Javadoc)
       * @see
       * org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.
       * swt.events.SelectionEvent)
       */
      @Override
      public void widgetSelected( final SelectionEvent e ) {
        addNew();
      }
    } );
    this.independentButton.addSelectionListener( new SelectionAdapter() {

      /*
       * (non-Javadoc)
       * @see
       * org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.
       * swt.events.SelectionEvent)
       */
      @Override
      public void widgetSelected( final SelectionEvent e ) {
        addIndependent();
      }
    } );
    this.sameLevelButton.addSelectionListener( new SelectionAdapter() {

      /*
       * (non-Javadoc)
       * @see
       * org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.
       * swt.events.SelectionEvent)
       */
      @Override
      public void widgetSelected( final SelectionEvent e ) {
        addChangesWith();
      }
    } );
    this.innerButton.addSelectionListener( new SelectionAdapter() {

      /*
       * (non-Javadoc)
       * @see
       * org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.
       * swt.events.SelectionEvent)
       */
      @Override
      public void widgetSelected( final SelectionEvent e ) {
        addChangesForEachChange();
      }
    } );
    this.deleteButton.addSelectionListener( new SelectionAdapter() {

      /*
       * (non-Javadoc)
       * @see
       * org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.
       * swt.events.SelectionEvent)
       */
      @Override
      public void widgetSelected( final SelectionEvent e ) {
        removeSelected();
      }
    } );
  }

  void updateButtons() {
    boolean enable = !this.viewer.getSelection().isEmpty();
    this.independentButton.setEnabled( enable );
    this.sameLevelButton.setEnabled( enable );
    this.innerButton.setEnabled( enable );
    this.deleteButton.setEnabled( enable );
    if( this.addFunctionButton != null ) {
      this.addFunctionButton.setEnabled( !this.sweepCombo.getText().equals( "" )
                                         && this.textArea.getText()
                                           .trim()
                                           .equals( "" ) );
    }
  }

  void setValuesField( final List<String> values ) {
    if( values != null ) {
      String multiLinesContent = ""; //$NON-NLS-1$
      for( String name : values ) {
        multiLinesContent = multiLinesContent
                            + System.getProperty( "line.separator" ) + name; //$NON-NLS-1$
      }
      if( multiLinesContent.startsWith( System.getProperty( "line.separator" ) ) ) { //$NON-NLS-1$
        multiLinesContent = multiLinesContent.trim();
      }
      this.textArea.setText( multiLinesContent );
      updateButtons();
    }
  }

  Action createDeleteAction() {
    Action action = new Action() {

      @Override
      public void run() {
        removeSelected();
      }
    };
    action.setText( "Delete" );
    return action;
  }

  Action createNewAction() {
    Action action = new Action() {

      @Override
      public void run() {
        addNew();
      }
    };
    action.setText( "New..." );
    return action;
  }

  Action createChangesForEachAction() {
    Action action = new Action() {

      @Override
      public void run() {
        addChangesForEachChange();
      }
    };
    action.setText( "Add inner sweep..." );
    return action;
  }

  private List<String> getInerSweepNames() {
    List<String> result = new ArrayList<String>();
    for( SweepType sweep : this.inerSweepList ) {
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

  protected void addChangesForEachChange() {
    ISelection sel = this.viewer.getSelection();
    if( sel instanceof StructuredSelection ) {
      StructuredSelection sSel = ( StructuredSelection )sel;
      if( sSel.getFirstElement() instanceof SweepType ) {
        SweepType type = ( SweepType )sSel.getFirstElement();
        List<String> adapterList = this.adapter.getElementsList();
        adapterList.removeAll( getInerSweepNames() );
        ParametersDialog dialog = new ParametersDialog( this.shell,
                                                        adapterList,
                                                        getInerSweepNames(),
                                                        getNameForSweep( type ),
                                                        ParametersDialog.EDIT_ELEMENT );
        // dialog.setTitle( "Add inner sweep" );
        if( dialog.open() == Window.OK ) {
          performAddChangesForEachChange( dialog.getElementReturn(),
                                          dialog.getRefElementReturn(),
                                          dialog.getValuesReturn() );
        }
      }
    }
  }

  private void performAddChangesForEachChange( final String element,
                                               final String refElement,
                                               final List<String> values )
  {
    SweepType refSweep = this.adapter.findSweepElement( refElement,
                                                        this.inerSweepList );
    SweepType newSweep = createNewSweepType( element );
    if( refSweep != null ) {
      AssignmentType assignment = ( AssignmentType )newSweep.getAssignment()
        .get( 0 );
      setFunctionValues( assignment, values );
      refSweep.getSweep().add( newSweep );
    }
    this.viewer.refresh( true );
    refreshSweepCombo();
    setInput( this.jobDefinitionType );
    contentChanged();
  }

  void setFunctionValues( final AssignmentType assignment,
                          final List<String> values )
  {
    List<String> loops = new ArrayList<String>();
    for( String value : values ) {
      if( value.startsWith( "LOOP" ) ) {
        loops.add( value );
      }
    }
    values.removeAll( loops );
    boolean valuesAreEmpty = true;
    if( values.size() > 0 ) {
      FunctionsPackage pak = FunctionsPackageImpl.eINSTANCE;
      FunctionsFactory factory = pak.getFunctionsFactory();
      ValuesType valuesType = factory.createValuesType();
      for( String value : values ) {
        if( !value.equals( "" ) ) {
          valuesAreEmpty = false;
          valuesType.getValue().add( value );
        }
      }
      // if( !valuesAreEmpty ) {
      EClass eClass = ExtendedMetaData.INSTANCE.getDocumentRoot( pak );
      Entry e = FeatureMapUtil.createEntry( eClass.getEStructuralFeature( "values" ), //$NON-NLS-1$
                                            valuesType );
      assignment.getFunctionGroup().add( e );
      // } else {
      // assignment.getFunctionGroup().get( 0 ).
      // }
    }
    if( loops.size() > 0 ) {
      FunctionsPackage pak = FunctionsPackageImpl.eINSTANCE;
      FunctionsFactory factory = pak.getFunctionsFactory();
      for( String loopExp : loops ) {
        LoopType loopType = factory.createLoopType();
        loopType.setStart( this.adapter.parseLOOPStringForStart( loopExp ) );
        loopType.setEnd( this.adapter.parseLOOPStringForEnd( loopExp ) );
        loopType.setStep( this.adapter.parseLOOPStringForStep( loopExp ) );
        for( ExceptionType exc : this.adapter.parseLOOPStringForExceptions( loopExp ) )
        {
          loopType.getException().add( exc );
        }
        EClass eClass = ExtendedMetaData.INSTANCE.getDocumentRoot( pak );
        Entry e = FeatureMapUtil.createEntry( eClass.getEStructuralFeature( "loop" ), //$NON-NLS-1$
                                              loopType );
        assignment.getFunctionGroup().add( e );
        // loopType.getException().add( e );
      }
    } else {
      Iterator iterator = assignment.getFunctionGroup().iterator();
      while( iterator.hasNext() ) {
        Object obj = iterator.next();
        if( obj instanceof ContainmentUpdatingFeatureMapEntry ) {
          ContainmentUpdatingFeatureMapEntry loopF = ( ContainmentUpdatingFeatureMapEntry )obj;
          if( loopF.getValue() instanceof LoopType ) {
            assignment.getFunctionGroup().remove( obj );
          }
        }
      }
    }
  }

  Action createIndependentSweepAction() {
    Action action = new Action() {

      @Override
      public void run() {
        addIndependent();
      }
    };
    action.setText( "Add independent sweep..." );
    return action;
  }

  Action createChangesWithSweepAction() {
    Action action = new Action() {

      @Override
      public void run() {
        addChangesWith();
      }
    };
    action.setText( "Add sweep on the same level..." );
    return action;
  }

  String getNameForSweep( final SweepType type ) {
    String result = null;
    EList list = type.getAssignment();
    for( int i = 0; i < list.size(); i++ ) {
      Object el = list.get( i );
      if( el instanceof AssignmentType ) {
        AssignmentType assignment = ( AssignmentType )el;
        EList paramList = assignment.getParameter();
        for( int j = 0; j < paramList.size(); j++ ) {
          result = ( String )paramList.get( j );
          break;
        }
      }
    }
    return result;
  }

  // changing with (on the same level)
  protected void addChangesWith() {
    ISelection sel = this.viewer.getSelection();
    if( sel instanceof StructuredSelection ) {
      StructuredSelection sSel = ( StructuredSelection )sel;
      if( sSel.getFirstElement() instanceof SweepType ) {
        SweepType type = ( SweepType )sSel.getFirstElement();
        List<String> adapterList = this.adapter.getElementsList();
        adapterList.removeAll( getInerSweepNames() );
        ParametersDialog dialog = new ParametersDialog( this.shell,
                                                        adapterList,
                                                        getInerSweepNames(),
                                                        getNameForSweep( type ),
                                                        ParametersDialog.EDIT_ELEMENT );
        // dialog.setTitle( "Add sweep on the same level" );
        if( dialog.open() == Dialog.OK ) {
          performAddChangesWith( dialog.getElementReturn(),
                                 dialog.getRefElementReturn(),
                                 dialog.getValuesReturn() );
        }
      }
    }
  }

  // sweep on the same level
  private void performAddChangesWith( final String sweepElement,
                                      final String refElement,
                                      final List<String> values )
  {
    SweepType refSweep = this.adapter.findSweepElement( refElement,
                                                        this.inerSweepList );
    AssignmentType newSweep = createNewAssignmentType( sweepElement );
    if( refSweep != null ) {
      setFunctionValues( newSweep, values );
      refSweep.getAssignment().add( newSweep );
    }
    this.viewer.refresh( true );
    refreshSweepCombo();
    setInput( this.jobDefinitionType );
    contentChanged();
  }

  private MenuManager createMenu() {
    MenuManager mManager = new MenuManager();
    mManager.setRemoveAllWhenShown( true );
    mManager.addMenuListener( new IMenuListener() {

      public void menuAboutToShow( final IMenuManager mManager ) {
        boolean enable = false;
        if( !SweepOrderSection.this.viewer.getSelection().isEmpty() ) {
          enable = true;
        }
        // Adds a separator
        mManager.add( new Separator( "Zero" ) );
        // Adds a GroupMarker
        GroupMarker marker = new GroupMarker( IWorkbenchActionConstants.MB_ADDITIONS );
        mManager.add( marker );
        mManager.add( createNewAction() );
        Action action = createIndependentSweepAction();
        action.setEnabled( enable );
        mManager.add( new Separator( "First" ) );
        mManager.appendToGroup( "First", action );
        action = createChangesWithSweepAction();
        action.setEnabled( enable );
        mManager.appendToGroup( "First", action );
        action = createChangesForEachAction();
        action.setEnabled( enable );
        mManager.appendToGroup( "First", action );
        mManager.add( new Separator( "Second" ) );
        action = createDeleteAction();
        action.setEnabled( enable );
        mManager.appendToGroup( "Second", action );
      }
    } );
    Menu contextMenu = mManager.createContextMenu( this.viewer.getTree() );
    this.viewer.getTree().setMenu( mManager.getMenu() );
    return mManager;
  }

  protected void removeSelected() {
    ISelection sel = this.viewer.getSelection();
    if( sel instanceof StructuredSelection ) {
      Map<String, AssignmentType> paramsToRemove = new HashMap<String, AssignmentType>();
      StructuredSelection sSel = ( StructuredSelection )sel;
      for( Object object : sSel.toList() ) {
        if( object instanceof SweepType ) {
          SweepType sweep = ( SweepType )object;
          EList<AssignmentType> assignments = sweep.getAssignment();
          for( int i = 0; i < assignments.size(); i++ ) {
            EList<String> params = assignments.get( i ).getParameter();
            for( int j = 0; j < params.size(); j++ ) {
              paramsToRemove.put( params.get( j ), assignments.get( i ) );
            }
          }
        }
      }
      List<String> userDecision = new ArrayList<String>();
      boolean remove = true;
      if( paramsToRemove.size() > 1 ) {
        for( String a : paramsToRemove.keySet() ) {
          userDecision.add( a );
        }
        SweepDeleteDialog dialog = new SweepDeleteDialog( this.shell,
                                                          userDecision );
        if( dialog.open() == Window.OK ) {
          // removing
          userDecision = dialog.getElementsToRemove();
        } else {
          remove = false;
        }
      } else if( paramsToRemove.size() == 1 ) {
        userDecision = new ArrayList<String>();
        for( String param : paramsToRemove.keySet() ) {
          userDecision.add( param );
        }
      }
      if( paramsToRemove.size() > 0 && remove ) {
        for( String param : userDecision ) {
          performRemove( paramsToRemove.get( param ), param );
        }
        this.viewer.refresh();
        contentChanged();
      }
    }
  }

  private void performRemove( final AssignmentType assignment,
                              final String assignmentParam )
  {
    if( assignment.eContainer() instanceof SweepType ) {
      SweepType sweepContainer = ( SweepType )assignment.eContainer();
      if( assignment.getParameter().size() == 1 ) {
        if( sweepContainer.getAssignment().size() == 1 ) {
          EcoreUtil.remove( sweepContainer );
          setInput( this.jobDefinitionType );
        } else {
          EcoreUtil.remove( assignment );
          setInput( this.jobDefinitionType );
        }
      } else {
        assignment.getParameter().remove( assignmentParam );
        setInput( this.jobDefinitionType );
      }
    }
  }

  // changing independently
  protected void addIndependent() {
    ISelection sel = this.viewer.getSelection();
    if( sel instanceof StructuredSelection ) {
      StructuredSelection sSel = ( StructuredSelection )sel;
      if( sSel.getFirstElement() instanceof SweepType ) {
        SweepType type = ( SweepType )sSel.getFirstElement();
        List<String> adapterList = this.adapter.getElementsList();
        adapterList.removeAll( getInerSweepNames() );
        ParametersDialog dialog = new ParametersDialog( this.shell,
                                                        adapterList,
                                                        getInerSweepNames(),
                                                        getNameForSweep( type ),
                                                        ParametersDialog.EDIT_ELEMENT );
        // dialog.setTitle( "Add independent sweep" );
        if( dialog.open() == Window.OK ) {
          performAddIndependent( dialog.getElementReturn(),
                                 dialog.getRefElementReturn(),
                                 dialog.getValuesReturn() );
        }
      }
    }
  }

  protected void addNew() {
    List<String> adapterList = this.adapter.getElementsList();
    adapterList.removeAll( getInerSweepNames() );
    ParametersDialog dialog = new ParametersDialog( this.shell,
                                                    adapterList,
                                                    getInerSweepNames(),
                                                    "",
                                                    ParametersDialog.NEW_ELEMENT );
    // dialog.setTitle( "Add new sweep" );
    if( dialog.open() == Dialog.OK ) {
      performAddIndependent( dialog.getElementReturn(),
                             dialog.getRefElementReturn(),
                             dialog.getValuesReturn() );
    }
  }

  private SweepType createNewSweepType( final String parameter ) {
    SweepType result = null;
    SweepPackage pak = SweepPackageImpl.eINSTANCE;
    SweepFactory factory = pak.getSweepFactory();
    result = factory.createSweepType();
    AssignmentType assignment = factory.createAssignmentType();
    assignment.getParameter().add( parameter );
    result.getAssignment().add( assignment );
    return result;
  }

  private AssignmentType createNewAssignmentType( final String parameter ) {
    AssignmentType result = null;
    SweepPackage pak = SweepPackageImpl.eINSTANCE;
    SweepFactory factory = pak.getSweepFactory();
    result = factory.createAssignmentType();
    result.getParameter().add( parameter );
    return result;
  }

  protected void performAddIndependent( final String sweepElement,
                                        final String refElement,
                                        final List<String> values )
  {
    if( refElement == null ) {
      // there are no sweep elements in JSDL
      SweepType newSweep = createNewSweepType( sweepElement );
      setFunctionValues( ( AssignmentType )newSweep.getAssignment().get( 0 ),
                         values );
      EClass eClass = ExtendedMetaData.INSTANCE.getDocumentRoot( SweepPackageImpl.eINSTANCE );
      Entry e = FeatureMapUtil.createEntry( eClass.getEStructuralFeature( "sweep" ), //$NON-NLS-1$
                                            newSweep );
      this.jobDefinitionType.getAny().add( e );
    } else {
      // there already is sweep element in JSDL
      SweepType refSweep = this.adapter.findSweepElement( refElement,
                                                          this.inerSweepList );
      SweepType newSweep = createNewSweepType( sweepElement );
      setFunctionValues( ( AssignmentType )newSweep.getAssignment().get( 0 ),
                         values );
      if( refSweep.eContainer() instanceof JobDefinitionType ) {
        EClass eClass = ExtendedMetaData.INSTANCE.getDocumentRoot( SweepPackageImpl.eINSTANCE );
        Entry e = FeatureMapUtil.createEntry( eClass.getEStructuralFeature( "sweep" ), //$NON-NLS-1$
                                              newSweep );
        ( ( JobDefinitionType )refSweep.eContainer() ).getAny().add( e );
      } else if( refSweep.eContainer() instanceof SweepType ) {
        ( ( SweepType )refSweep.eContainer() ).getSweep().add( newSweep );
      }
    }
    this.viewer.refresh( true );
    refreshSweepCombo();
    setInput( this.jobDefinitionType );
    contentChanged();
  }
}
