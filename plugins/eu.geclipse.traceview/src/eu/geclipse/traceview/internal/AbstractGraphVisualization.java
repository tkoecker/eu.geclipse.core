/*****************************************************************************
 * Copyright (c) 2009 g-Eclipse Consortium 
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
 *    Thomas Koeckerbauer - MNM-Team, LMU Munich, code cleanup of logical and physical trace viewers
 *****************************************************************************/

package eu.geclipse.traceview.internal;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.window.DefaultToolTip;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IViewSite;

import eu.geclipse.traceview.IEvent;
import eu.geclipse.traceview.IEventMarker;
import eu.geclipse.traceview.ITrace;
import eu.geclipse.traceview.TraceVisualization;
import eu.geclipse.traceview.views.TraceView;


public abstract class AbstractGraphVisualization extends TraceVisualization {
  protected EventMarkers eventMarkers;
  protected LineType hLines;
  protected LineType vLines;
  protected ITrace trace;
  protected List<SortedSet<Integer>> lineToProcMapping;
  protected int[] procToLineMapping;
  protected boolean[] hideProcess;

  public AbstractGraphVisualization( final Composite parent,
                       final int style,
                       final IViewSite viewSite,
                       final ITrace trace )
  {
    super( parent, style | SWT.V_SCROLL | SWT.H_SCROLL );
    this.trace = trace;
    // layout
    GridData layoutData = new GridData();
    layoutData.horizontalAlignment = SWT.FILL;
    layoutData.grabExcessHorizontalSpace = true;
    layoutData.verticalAlignment = SWT.FILL;
    layoutData.grabExcessVerticalSpace = true;
    this.setLayoutData( layoutData );
    // set defaults
    this.hLines = LineType.Lines_1;
    this.vLines = LineType.Lines_1;
    // create process mapping
    resetOrdering();
    // get the Event Markers
    this.eventMarkers = new EventMarkers( trace );
    TraceView view = ( TraceView )Activator.getDefault()
      .getWorkbench()
      .getActiveWorkbenchWindow()
      .getActivePage()
      .getActivePart();
    Menu menu = view.getContextMenuManager().createContextMenu( this );
    setMenu( menu );
    addListener( SWT.Resize, new Listener() {
      public void handleEvent( final Event e ) {
        handleResize();
      }
    } );
    addDisposeListener( new DisposeListener() {
      public void widgetDisposed( final DisposeEvent e ) {
        IPreferenceStore store = Activator.getDefault().getPreferenceStore();
        store.removePropertyChangeListener( getEventGraphPaintListener().listener );
      }
    } );
  }

  public void registerPaintListener( final AbstractGraphPaintListener eventGraphPaintListener ) {
    eventGraphPaintListener.setHorizontalScrollBar( getHorizontalBar() );
    eventGraphPaintListener.setVerticalScrollBar( getVerticalBar() );
    eventGraphPaintListener.setFont( getFont() );
    eventGraphPaintListener.setTrace( this.trace );
    addPaintListener( eventGraphPaintListener );
  }

  public void registerMouseListener( final AbstractGraphMouseAdapter mouseAdapter ) {
    addMouseListener( mouseAdapter );
    addMouseMoveListener( mouseAdapter );
    @SuppressWarnings("unused")
    DefaultToolTip toolTip = new DefaultToolTip( this ) {
      @Override
      protected boolean shouldCreateToolTip( final Event event ) {
        Object[] objs = mouseAdapter.getObjectsForPosition( event.x, event.y, true );
        return objs.length != 0 && getText( objs ) != null;
      }

      @Override
      protected String getText( final Event event ) {
        Object[] objs = mouseAdapter.getObjectsForPosition( event.x, event.y, true );
        return getText( objs );
      }

      protected String getText( Object[] objs ) {
        String result = null;
        if( objs.length != 0 ) {
          result = ""; //$NON-NLS-1$
          for (Object obj : objs) {
            if (result.length() != 0) result += '\n';
            if (obj.toString() != null) result += obj.toString();
            if (obj instanceof IEvent) {
              for( IEventMarker eventmarker : getEventMarkers() ) {
                eventmarker.mark( (IEvent)obj );
                String markerString = eventmarker.getToolTip();
                if (markerString != null) {
                  result += '\n' + markerString;
                }
              }
            }
          }
          if (result.length() == 0) result = null;
        }
        return result;
      }
    };
  }

  private void setHLines( final LineType lines ) {
    this.hLines = lines;
    redraw();
  }

  private void setVLines( final LineType lines ) {
    this.vLines = lines;
    redraw();
  }

  protected void changeLineStyle( final int direction ) {
    LineType style = null;
    if( direction < 0 ) {
      style = getVLines();
    } else {
      style = getHLines();
    }
    if( style == LineType.Lines_None ) {
      style = LineType.Lines_1;
    } else if( style == LineType.Lines_1 ) {
      style = LineType.Lines_5;
    } else if( style == LineType.Lines_5 ) {
      style = LineType.Lines_10;
    } else if( style == LineType.Lines_10 ) {
      style = LineType.Lines_None;
    }
    if( direction < 0 ) {
      setVLines( style );
    } else {
      setHLines( style );
    }
  }

  public List<IEventMarker> getEventMarkers() {
    return this.eventMarkers.getEventMarkers();
  }

  protected void handleResize() {
    getEventGraphPaintListener().handleResize();
  }

  public LineType getHLines() {
    return this.hLines;
  }

  public LineType getVLines() {
    return this.vLines;
  }

  @Override
  public ITrace getTrace() {
    return this.trace;
  }

  @Override
  public void printTrace( final GC gc ) {
    getEventGraphPaintListener().print( gc );
  }

  void setLineToProcessMapping(List<SortedSet<Integer>> mapping) {
    this.lineToProcMapping = mapping;
    updateProcessToLineMapping();
    redraw();
  }  

  void updateProcessToLineMapping() {
    for(int i = 0; i < this.trace.getNumberOfProcesses(); i++) {
      this.procToLineMapping[i] = -1;
    }
    for(int line = 0; line < this.lineToProcMapping.size(); line++) {
      for(Integer proc : this.lineToProcMapping.get( line )) {
        this.procToLineMapping[proc.intValue()] = line;
      }
    }
  }

  public List<SortedSet<Integer>> getLineToProcessMapping() {
    return this.lineToProcMapping;
  }

  public void setProcessToLineMapping( int[] procToLineMapping ) {
    this.procToLineMapping = procToLineMapping;
    updateLineToProcessMapping();
    redraw();
  }

  void removeEmptyLines() {
    Iterator<SortedSet<Integer>> it = this.lineToProcMapping.iterator();
    while (it.hasNext()) {
      SortedSet<Integer> set = it.next();
      if (set.isEmpty()) {
        it.remove();
      }
    }
    updateProcessToLineMapping();
    redraw();
  }

  void updateLineToProcessMapping() {
    this.lineToProcMapping = new LinkedList<SortedSet<Integer>>();
    for (int proc = 0; proc < this.procToLineMapping.length; proc++) {
      int line = this.procToLineMapping[proc];
      if (line != -1) {
        while(this.lineToProcMapping.size() <= line ) {
          this.lineToProcMapping.add( new TreeSet<Integer>() );
        }
        this.lineToProcMapping.get( line ).add( Integer.valueOf( proc ) );
      }
    }
  }

  public int[] getProcessToLineMapping() {
    return this.procToLineMapping;
  }

  public boolean[] getHideProcess() {
    return this.hideProcess;
  }

  public void setHideProcess( boolean[] hideProcess ) {
    this.hideProcess = hideProcess;
    redraw();
  }

  void resetOrdering() {
    this.procToLineMapping = new int[this.trace.getNumberOfProcesses()];
    this.hideProcess = new boolean[this.trace.getNumberOfProcesses()];
    for (int i = 0; i < this.trace.getNumberOfProcesses(); i++) {
      this.procToLineMapping[i] = i;
    }
    updateLineToProcessMapping();
  }

  @Override
  public IContributionItem[] getToolBarItems() {
    Vector<IContributionItem> items = new Vector<IContributionItem>();
    Action reset = new Action( Messages.getString( "AbstractGraphVisualization.Reset" ), //$NON-NLS-1$
                               Activator.getImageDescriptor( "icons/reset.gif" ) ) { //$NON-NLS-1$
      @Override
      public void run() {
        resetOrdering();
        redraw();
      }
    };
    items.add( new ActionContributionItem( reset ) );
    Action markerSelectionAction = new Action( Messages.getString("AbstractGraphVisualization.toggleMarkers"), //$NON-NLS-1$
                                               Activator.getImageDescriptor( "icons/marker.gif" ) ) { //$NON-NLS-1$
      @Override
      public void run() {
        MarkerOrderDialog dialog = new MarkerOrderDialog( getShell() );
        dialog.setEventMarkerEntries( eventMarkers.getEventMarkerEntries() );
        if (dialog.open() == Window.OK) {
          eventMarkers.eventMarkerEntries = dialog.getEventMarkerEntries();
          eventMarkers.buildEventMarkersList();
          redraw();
        }
      }
    };
    IMenuCreator menuCreator = new MarkerSelectionMenuCreator( this );
    markerSelectionAction.setMenuCreator( menuCreator );
    items.add( new ActionContributionItem( markerSelectionAction ) );

    return items.toArray( new IContributionItem[ items.size() ] );
  }

  public abstract AbstractGraphPaintListener getEventGraphPaintListener();

  public void mergeProcessLines( int fromLine, int toLine ) {
    int[] mapping = getProcessToLineMapping();
    for ( Integer proc : getLineToProcessMapping().get( fromLine ) ) {
      mapping[proc.intValue()] = toLine;
    }
    setProcessToLineMapping( mapping );
    removeEmptyLines();
  }

  public void swapProcessLines( int lineA, int lineB ) {
    int[] mapping = getProcessToLineMapping();
    for (int i = 0; i < mapping.length; i++) {
      if (mapping[i] == lineA ) mapping[i] = lineB;
      else if (mapping[i] == lineB ) mapping[i] = lineA;
    }
    setProcessToLineMapping( mapping );
  }

  public void moveProcessLine( int oldLine, int newLine ) {
    int[] mapping = getProcessToLineMapping();
    for (int i = 0; i < mapping.length; i++) {
      if (oldLine > newLine) {
        if (mapping[i] >= newLine && mapping[i] < oldLine) mapping[i]++;
        else if (mapping[i] == oldLine) mapping[i] = newLine;
      } else {
        if (mapping[i] <= newLine && mapping[i] > oldLine) mapping[i]--;
        else if (mapping[i] == oldLine) mapping[i] = newLine;
      }
    }
    setProcessToLineMapping( mapping );
  }
}
