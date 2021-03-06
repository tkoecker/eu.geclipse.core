/*****************************************************************************
 * Copyright (c) 2006, 2008 g-Eclipse Consortium
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
 *    Christof Klausecker GUP, JKU - initial API and implementation
 *****************************************************************************/

package eu.geclipse.traceview.views;

import java.io.IOException;
import java.util.Vector;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.printing.PrintDialog;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.printing.PrinterData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.ViewPart;

import eu.geclipse.traceview.ITrace;
import eu.geclipse.traceview.ITraceReader;
import eu.geclipse.traceview.ITraceView;
import eu.geclipse.traceview.TraceVisualization;
import eu.geclipse.traceview.internal.Activator;
import eu.geclipse.traceview.internal.Messages;
import eu.geclipse.traceview.views.internal.SelectionProvider;
import eu.geclipse.traceview.views.internal.TraceVisPage;

/**
 * View containing TraceVisualizations
 */
public class TraceView extends ViewPart implements ITraceView {

  protected TraceVisPage traceVisPage = null;
  FileDialog fileDialog;
  CTabFolder cTabFolder = null;
  private ISelectionProvider selectionProvider;
  private MenuManager contextMenuMgr;
  private Action visualisationDropDownAction;

  public ITrace openTrace( final String tracePathString, IProgressMonitor monitor ) throws IOException {
    if( tracePathString != null ) {
      final IPath path = new Path( tracePathString );
      String extension = path.getFileExtension();
      // get the trace reader
      for( IConfigurationElement configurationElement : Platform.getExtensionRegistry()
        .getConfigurationElementsFor( "eu.geclipse.traceview.TraceReader" ) ) { //$NON-NLS-1$
        if( configurationElement.getAttribute( "fileextension" ).equals( extension ) ) { //$NON-NLS-1$
          ITraceReader traceReader;
          try {
            traceReader = ( ITraceReader )configurationElement.createExecutableExtension( "class" );
            final ITrace trace = traceReader.openTrace( path, monitor );
            return trace;
          } catch( CoreException e ) {
            Activator.logException( e );
          }
        }
      }
    }
    return null;
  }

  public void addTrace( final ITrace trace ) {
    if( trace != null ) {
      Display.getDefault().syncExec(new Runnable() {
		@Override
		public void run() {
		      try {
		        PlatformUI.getWorkbench().getActiveWorkbenchWindow()
		                  .getActivePage().showView( "eu.geclipse.traceview.views.TraceView" );
		        getViewSite().getActionBars().getToolBarManager().removeAll();
		        TraceView.this.traceVisPage = new TraceVisPage( TraceView.this.cTabFolder,
		                                              SWT.NONE,
		                                              TraceView.this.getViewSite(),
		                                              TraceView.this,
		                                              trace );
		      } catch( PartInitException exception ) {
		        Activator.logException( exception );
		      }
		    }
      	});
    }
  }

  /**
   * Creates the toolbar
   *
   * @param id
   */
  public void createToolbar( final String id ) {
    Action add = new Action( Messages.getString( "TraceView.Open" ), //$NON-NLS-1$
                             Activator.getImageDescriptor( "icons/open.gif" ) ) { //$NON-NLS-1$

      @Override
      public void run() {
        final String tracePathString = TraceView.this.fileDialog.open();
        if (tracePathString != null) {
          Job openTraceJob = new Job("Opening Trace") {
            @Override
            protected IStatus run( final IProgressMonitor monitor ) {
              IStatus status = Status.OK_STATUS;
              try {
                final ITrace trace = openTrace( tracePathString, monitor );
                if ( trace != null ) {
                  Display.getDefault().syncExec( new Runnable() {
                    public void run() {
                      addTrace( trace );
                    }
                  } );
                }
              } catch( IOException exception ) {
                Activator.logException( exception );
                status = Status.CANCEL_STATUS;
              }
              return status;
            }
          };
          openTraceJob.setUser( true );
          openTraceJob.schedule();
        }
      }
    };
    IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();
    this.visualisationDropDownAction = new Action( Messages.getString( "TraceView.switchVisualization" ), //$NON-NLS-1$
                                                   Activator.getImageDescriptor( "icons/changevis.gif" ) ) { //$NON-NLS-1$

      @Override
      public void run() {
        // TODO switch to next visualization type
      }
    };
    IMenuCreator menuCreator = new DropDown( this, id );
    this.visualisationDropDownAction.setMenuCreator( menuCreator );
    this.visualisationDropDownAction.setEnabled( id != null
                                                 && this.cTabFolder != null
                                                 && this.cTabFolder.getItemCount() != 0 );
    mgr.add( this.visualisationDropDownAction );
    mgr.add( new Separator() );
    mgr.add( add );
  }

  @Override
  public void createPartControl( final Composite parent ) {
    this.selectionProvider = new SelectionProvider();
    this.fileDialog = new FileDialog( this.getSite().getShell(), SWT.OPEN );
    this.fileDialog.setText( Messages.getString( "TraceView.openTrace" ) ); //$NON-NLS-1$
    Vector<String> extensions = new Vector<String>();
    Vector<String> names = new Vector<String>();
    for( IConfigurationElement configurationElement : Platform.getExtensionRegistry()
      .getConfigurationElementsFor( "eu.geclipse.traceview.TraceReader" ) ) { //$NON-NLS-1$
      names.add( configurationElement.getAttribute( "label" ) ); //$NON-NLS-1$
      extensions.add( "*." + configurationElement.getAttribute( "fileextension" ) ); //$NON-NLS-1$ //$NON-NLS-2$
    }
    this.fileDialog.setFilterExtensions( extensions.toArray( new String[ 0 ] ) );
    this.fileDialog.setFilterNames( names.toArray( new String[ 0 ] ) );
    createToolbar( null );
    GridData gridData = new GridData();
    gridData.horizontalAlignment = GridData.FILL;
    gridData.grabExcessHorizontalSpace = true;
    gridData.grabExcessVerticalSpace = true;
    gridData.verticalAlignment = GridData.FILL;
    this.cTabFolder = new CTabFolder( parent, SWT.FLAT | SWT.BOTTOM );
    this.cTabFolder.setLayoutData( gridData );
    getSite().setSelectionProvider( this.selectionProvider );
    this.cTabFolder.addCTabFolder2Listener( new CTabFolder2Adapter() {

      @Override
      public void close( final CTabFolderEvent event ) {
        if( TraceView.this.cTabFolder.getItemCount() == 1 ) {
          TraceView.this.getViewSite()
            .getActionBars()
            .getToolBarManager()
            .removeAll();
          TraceView.this.createToolbar( null );
          TraceView.this.getViewSite()
            .getActionBars()
            .getToolBarManager()
            .update( true );
        }
      }
    } );
    this.cTabFolder.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( final SelectionEvent e ) {
        TraceView.this.getViewSite()
          .getActionBars()
          .getToolBarManager()
          .removeAll();
        if( e.item instanceof CTabItem ) {
          CTabItem item = ( CTabItem )e.item;
          TraceView.this.traceVisPage = ( TraceVisPage )item.getControl();
          if( ( ( TraceVisPage )item.getControl() ).getItems() != null ) {
            for( IContributionItem citem : ( ( TraceVisPage )item.getControl() ).getItems() )
            {
              TraceView.this.getViewSite()
                .getActionBars()
                .getToolBarManager()
                .add( citem );
            }
          }
        }
        TraceView.this.createToolbar( TraceView.this.traceVisPage.getVisualisationID() );
        TraceView.this.getViewSite()
          .getActionBars()
          .getToolBarManager()
          .update( true );
        super.widgetSelected( e );
      }
    } );
    this.contextMenuMgr = new MenuManager( "#PopupMenu" ); //$NON-NLS-1$
    this.contextMenuMgr.add( new Separator( IWorkbenchActionConstants.MB_ADDITIONS ) );
    this.contextMenuMgr.add( new Separator( IWorkbenchActionConstants.MB_ADDITIONS
                                            + "-end" ) ); //$NON-NLS-1$
    // Be sure to register it so that other plug-ins can add actions.
    getSite().registerContextMenu( this.contextMenuMgr,
                                   getSite().getSelectionProvider() );
    IActionBars actionBars = getViewSite().getActionBars();
    actionBars.setGlobalActionHandler( ActionFactory.PRINT.getId(), new Action() {
      @Override
      public void run() {
        CTabItem item = TraceView.this.cTabFolder.getSelection();
        if( item != null && item.getControl() != null ) {
          PrintDialog printDialog = new PrintDialog(Display.getDefault().getActiveShell());
          printDialog.setText( "Print Trace" );
          PrinterData printerData = printDialog.open();
          if (printerData != null) {
            printerData.orientation = PrinterData.LANDSCAPE; // requires version 3.5
            Printer printer = new Printer(printerData);
            if(printer.startJob("Trace graph")) {
              GC gc = new GC(printer);
              if(printer.startPage()) {
                ((TraceVisPage)item.getControl()).printTrace(gc);
                printer.endPage();
              }
              gc.dispose();
              printer.endJob();
            }
            printer.dispose();
          }
        }
      }
    } );
  }

  /**
   * Returns the MenuManager
   *
   * @return menuManager
   */
  public MenuManager getContextMenuManager() {
    return this.contextMenuMgr;
  }

  @Override
  public void setFocus() {
    this.cTabFolder.setFocus();
  }

  /**
   * Returns the trace of the active visualization
   *
   * @return trace
   */
  public ITrace getTrace() {
    return ( ( TraceVisPage )this.cTabFolder.getSelection().getControl() ).getTrace();
  }

  public void redraw() {
    Display.getDefault().asyncExec( new Runnable() {

      public void run() {
        CTabItem  item = TraceView.this.cTabFolder.getSelection();
        if (item != null) {
          ( ( TraceVisPage )item.getControl() ).redrawVisualisation();
        }
      }
    } );
  }

  public TraceVisualization getVisualisationForTrace( final ITrace trace ) {
    final TraceVisualization[] vis = new TraceVisualization[1];
    Display.getDefault().syncExec( new Runnable() {
      public void run() {
        CTabItem[] items = TraceView.this.cTabFolder.getItems();
        for ( CTabItem item : items ) {
          TraceVisPage visPage = ( TraceVisPage )item.getControl();
          if (visPage.getTrace() == trace) {
            vis[0] = visPage.getVisualization();
          }
        }
      }
    } );
    return vis[0];
  }
}
