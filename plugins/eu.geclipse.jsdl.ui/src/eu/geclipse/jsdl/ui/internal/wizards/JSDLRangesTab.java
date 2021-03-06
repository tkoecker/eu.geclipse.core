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
 *     PSNC - Katarzyna Bylec
 *           
 *****************************************************************************/
package eu.geclipse.jsdl.ui.internal.wizards;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.TableItem;

import eu.geclipse.jsdl.ui.internal.dialogs.MultipleInputDialog;
import eu.geclipse.ui.widgets.TabComponent;

/**
 * Table to keep ranges for JSDL resources
 */
@SuppressWarnings("unchecked")
public class JSDLRangesTab extends TabComponent
{

  /**
   * Creates new instance of this class
   * 
   * @param contentProvider content provider for table viewer
   * @param labelProvider label provider for table viewer
   * @param columnsProperties headers for table
   * @param hight hight of this control
   * @param width width of collumn in table
   */
  public JSDLRangesTab( final IStructuredContentProvider contentProvider,
                        final ITableLabelProvider labelProvider,
                        final List<String> columnsProperties,
                        final int hight,
                        final int width )
  {
    super( contentProvider, labelProvider, columnsProperties, hight, width );
  }

  @Override
  protected void handleAddButtonSelected()
  {
    MultipleInputDialog dialog = new MultipleInputDialog( getShell(),
                                                       Messages.getString( "JSDLRangesTab.new_value" ) ); //$NON-NLS-1$
    dialog.addTextField( Messages.getString( "JSDLRangesTab.range_start" ), null, false ); //$NON-NLS-1$
    dialog.addTextField( Messages.getString( "JSDLRangesTab.range_end" ), null, false ); //$NON-NLS-1$
    if( dialog.open() != Window.OK ) {
      return;
    }
    String start = dialog.getStringValue( Messages.getString( "JSDLRangesTab.range_start" ) ); //$NON-NLS-1$
    String end = dialog.getStringValue( Messages.getString( "JSDLRangesTab.range_end" ) ); //$NON-NLS-1$
    try {
      Range range = new Range( Double.valueOf( start ).doubleValue(),
                               Double.valueOf( end ).doubleValue() );
      addVariable( range );
    } catch( IllegalArgumentException exc ) {
      MessageDialog.openError( getShell(),
                               Messages.getString( "JSDLRangesTab.bad_range_format" ),  //$NON-NLS-1$
                               Messages.getString( "JSDLRangesTab.bad_range_format" ) ); //$NON-NLS-1$
    }
  }

  @Override
  protected void handleEditButtonSelected()
  {
    IStructuredSelection sel = ( IStructuredSelection )this.table.getSelection();
    Range var = ( Range )sel.getFirstElement();
    if( var == null ) {
      // do nothing;
    } else {
      String oldStart = Double.valueOf( var.getStart() ).toString();
      String oldEnd = Double.valueOf( var.getEnd() ).toString();
      MultipleInputDialog dialog = new MultipleInputDialog( getShell(),
                         Messages.getString( "OutputFilesTab.edit_output_file_settings_dialog_title" ) ); //$NON-NLS-1$
      
      ArrayList<String> comboData = new ArrayList<String>();
      for( FileType fileType : FileType.values() ) {
        comboData.add( fileType.toString() );
      }
      dialog.addTextField( "Range start", oldStart, false ); //$NON-NLS-1$
      dialog.addTextField( Messages.getString( "JSDLRangesTab.range_end" ), oldEnd, false ); //$NON-NLS-1$
      if( dialog.open() != Window.OK ) {
        // do nothing;
      } else {
        try {
          String newStart = dialog.getStringValue( Messages.getString( "JSDLRangesTab.range_start" ) ); //$NON-NLS-1$
          String newEnd = dialog.getStringValue( Messages.getString( "JSDLRangesTab.range_end" ) ); //$NON-NLS-1$
          if( !oldStart.equals( newStart ) || !oldEnd.equals( newEnd ) ) {
            Range newRange = new Range( Double.valueOf( newStart )
              .doubleValue(), Double.valueOf( newEnd ).doubleValue() );
            if( addVariable( newRange ) ) {
              this.table.remove( var );
            }
          } else {
            var.setStart( Double.valueOf( newStart ).doubleValue() );
            var.setEnd( Double.valueOf( newEnd ).doubleValue() );
            this.table.update( var, null );
            updateLaunchConfigurationDialog();
          }
        } catch( IllegalArgumentException exc ) {
          MessageDialog.openError( getShell(),
                                   Messages.getString( "JSDLRangesTab.bad_range_format" ), //$NON-NLS-1$
                                  Messages.getString( "JSDLRangesTab.bad_range_format" ) ); //$NON-NLS-1$
        }
      }
    }
  }

  @Override
  protected void handleRemoveButtonSelected()
  {
    IStructuredSelection sel = ( IStructuredSelection )this.table.getSelection();
    this.table.getControl().setRedraw( false );
    for( Iterator<?> i = sel.iterator(); i.hasNext(); ) {
      Range var = ( Range )i.next();
      this.table.remove( var );
    }
    this.table.getControl().setRedraw( true );
    // updateAppendReplace();
    updateLaunchConfigurationDialog();
  }

  @Override
  protected void setLabels()
  {
    this.addButton.setText( Messages.getString( "JSDLRangesTab.add_button" ) ); //$NON-NLS-1$
    this.editButton.setText( Messages.getString( "JSDLRangesTab.edit_button" ) ); //$NON-NLS-1$
    this.removeButton.setText( Messages.getString( "JSDLRangesTab.remove_button" ) ); //$NON-NLS-1$
  }

  protected boolean addVariable( final Range variable ) {
    boolean result = true;
    TableItem[] items = this.table.getTable().getItems();
    for( int i = 0; i < items.length; i++ ) {
      Range existingVariable = ( Range )items[ i ].getData();
      if( existingVariable.equals( variable ) ) {
        // this will be changed to NewProblemDialog
        MessageDialog.openError( getShell(),
                                 Messages.getString( "JSDLRangesTab.rnage_conflict" ), //$NON-NLS-1$
                                 Messages.getString( "JSDLRangesTab.conflict_message" ) //$NON-NLS-1$
                                     + existingVariable.getStart()
                                     + ", " //$NON-NLS-1$
                                     + existingVariable.getEnd()
                                     + ")" ); //$NON-NLS-1$
        result = false;
        break;
      } else if( existingVariable.getStart() >= variable.getStart()
                 && existingVariable.getStart() < variable.getEnd() )
      {
        // this will be changed to NewProblemDialog
        MessageDialog.openError( getShell(),
                                 Messages.getString( "JSDLRangesTab.range_conflict" ), //$NON-NLS-1$
                                 Messages.getString( "JSDLRangesTab.conflict_message_1" ) //$NON-NLS-1$
                                     + existingVariable.getStart()
                                     + ", " //$NON-NLS-1$
                                     + existingVariable.getEnd()
                                     + ")" ); //$NON-NLS-1$
        result = false;
        break;
      } else {
        if( variable.getStart() >= existingVariable.getStart()
            && variable.getStart() < existingVariable.getEnd() )
        {
          // this will be changed to NewProblemDialog
          MessageDialog.openError( getShell(),
                                   Messages.getString( "JSDLRangesTab.range_conflict" ), //$NON-NLS-1$
                                   Messages.getString( "JSDLRangesTab.conflict_message_1" ) //$NON-NLS-1$
                                       + existingVariable.getStart()
                                       + ", " //$NON-NLS-1$
                                       + existingVariable.getEnd()
                                       + ")" ); //$NON-NLS-1$
          result = false;
          break;
        }
      }
    }
    if( result ) {
      this.table.add( variable );
    }
    return result;
  }
}