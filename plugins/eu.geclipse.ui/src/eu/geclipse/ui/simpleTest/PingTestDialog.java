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
 *     UCY (http://www.cs.ucy.ac.cy)
 *      - Harald Gjermundrod (harald@cs.ucy.ac.cy)
 *
 *****************************************************************************/
package eu.geclipse.ui.simpleTest;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.List;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Button;

import eu.geclipse.core.model.IGridResource;
import eu.geclipse.core.simpleTest.ISimpleTest;
import eu.geclipse.core.simpleTest.PingTest;
import eu.geclipse.ui.dialogs.AbstractSimpleTestDialog;


/**
 * A dialog that allows the user to ping selected resources. 
 * @author hgjermund
 *
 */
public class PingTestDialog extends AbstractSimpleTestDialog  {
  private static DecimalFormat df = new DecimalFormat( "0.000" ); //$NON-NLS-1$

  protected boolean running = false;

  private Text outPut = null;
  private Spinner numberSpn = null;
  private Spinner delaySpn = null;
  /**
   * Construct a new dialog from the specified test.
   * 
   * @param test The <code>ISimpleTest</code> for which to create the dialog for.
   * @param resources The resources that this test should be applied to.
   * @param parentShell  The parent shell of this dialog.
   */
  public PingTestDialog( final ISimpleTest test, final List< IGridResource > resources, final Shell parentShell ) {
    super( test, resources, parentShell );
  }
  
  @Override
  protected void configureShell( final Shell newShell ) {
    super.configureShell( newShell );
    newShell.setMinimumSize( 500, 400 );
    newShell.setText( Messages.getString( "PingTestDialog.dialogTitle" ) ); //$NON-NLS-1$
  }

  /* (non-Javadoc)
   * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
   */
  @Override
  protected Control createDialogArea( final Composite parent ) {
    GridData gData;
    
    Composite mainComp = new Composite( parent, SWT.NONE );
    mainComp.setLayout( new GridLayout( 1, false ) );
    gData = new GridData( SWT.FILL, SWT.FILL, true, true);
    mainComp.setLayoutData( gData );
    
    Group settingsGroup = new Group( mainComp, SWT.NONE );
    settingsGroup.setLayout( new GridLayout( 3, false ) );
    settingsGroup.setText( Messages.getString( "PingTestDialog.settings_group" ) ); //$NON-NLS-1$
    gData = new GridData( GridData.FILL_HORIZONTAL );
    gData.grabExcessHorizontalSpace = true;
    settingsGroup.setLayoutData( gData );

    Label numPingsLabel = new Label( settingsGroup, SWT.LEFT  );
    numPingsLabel.setText( Messages.getString( "PingTestDialog.nPingsLabel" ) ); //$NON-NLS-1$
    gData = new GridData();
    numPingsLabel.setLayoutData( gData );
    
    this.numberSpn = new Spinner( settingsGroup, SWT.LEFT | SWT.SINGLE | SWT.BORDER );
    this.numberSpn.setValues( 2, 1, 10, 0, 1, 2 );
    gData = new GridData( GridData.FILL_HORIZONTAL );
    gData.horizontalSpan = 2;
    gData.grabExcessHorizontalSpace = true;
    this.numberSpn.setLayoutData( gData );    

    Label delayLabel = new Label( settingsGroup, SWT.LEFT );
    delayLabel.setText( Messages.getString( "PingTestDialog.delayLabel" ) ); //$NON-NLS-1$
    gData = new GridData();
    delayLabel.setLayoutData( gData );

    this.delaySpn = new Spinner( settingsGroup, SWT.LEFT | SWT.SINGLE | SWT.BORDER );
    this.delaySpn.setValues( 1, 1, 10, 0, 1, 10 );
    gData = new GridData( GridData.FILL_HORIZONTAL );
    gData.horizontalSpan = 2;
    gData.grabExcessHorizontalSpace = true;
    this.delaySpn.setLayoutData( gData );    
    
    Group outPutGroup = new Group( mainComp, SWT.NONE );
    outPutGroup.setLayout( new GridLayout( 3, false ) );
    outPutGroup.setText( Messages.getString( "PingTestDialog.output_group" ) ); //$NON-NLS-1$
    gData = new GridData( GridData.FILL_BOTH );
    gData.grabExcessHorizontalSpace = true;
    gData.grabExcessVerticalSpace = true;
    outPutGroup.setLayoutData( gData );
    
    Label outPutLabel = new Label( outPutGroup, SWT.LEFT );
    outPutLabel.setText( Messages.getString( "PingTestDialog.outPutLabel" ) ); //$NON-NLS-1$
    gData = new GridData();
    gData.horizontalSpan = 3;
    outPutLabel.setLayoutData( gData );
    
    this.outPut = new Text( outPutGroup, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI );
    this.outPut.setEditable( false );
    gData = new GridData( SWT.FILL, SWT.FILL, true, true );
//    gData.horizontalSpan = 2;
    gData.grabExcessHorizontalSpace = true;
    gData.grabExcessVerticalSpace = true;
    this.outPut.setLayoutData( gData );

    Composite outControls = new Composite( outPutGroup, SWT.NONE );
    GridLayout gLayout = new GridLayout( 1, false );
    gLayout.marginHeight = 0;
    gLayout.marginWidth = 0;
    outControls.setLayout( gLayout );
    gData = new GridData( GridData.VERTICAL_ALIGN_BEGINNING );
    outControls.setLayoutData( gData );    
    
    Button pingButton = new Button( outControls, SWT.PUSH );
    pingButton.setText( Messages.getString( "PingTestDialog.pingButton" ) ); //$NON-NLS-1$
    gData = new GridData( GridData.FILL_HORIZONTAL );
    gData.verticalAlignment = GridData.BEGINNING;
    pingButton.setLayoutData( gData );

    Button stopButton = new Button( outControls, SWT.PUSH );
    stopButton.setText( Messages.getString( "PingTestDialog.stopButton" ) ); //$NON-NLS-1$
    gData = new GridData( GridData.FILL_HORIZONTAL );
    gData.verticalAlignment = GridData.BEGINNING;
    stopButton.setLayoutData( gData );
    
    pingButton.addSelectionListener( new SelectionAdapter() {
      @Override
      public void widgetSelected( final SelectionEvent e) {

        if ( !PingTestDialog.this.running ) { 
          PingTestDialog.this.running = true;
          PingTestDialog.this.runPing();
        }
      }
    });
    
    stopButton.addSelectionListener( new SelectionAdapter() {
      @Override
      public void widgetSelected( final SelectionEvent e) {
        PingTestDialog.this.running = false;
      }
    });
    return mainComp;
  }

  /*  
  64 bytes from 194.42.27.239: icmp_seq=2 ttl=63 time=0.409 ms
  ^C
  --- 194.42.27.239 ping statistics ---
  3 packets transmitted, 3 packets received, 0% packet loss
  round-trip min/avg/max/stddev = 0.390/0.444/0.532/0.063 ms
*/
  protected void runPing() {
    String host;
    InetAddress adr = null;
    int number = this.numberSpn.getSelection();
    boolean exception = false;

    if ( null != this.resources ) {
      // Clear the text field 
      this.outPut.selectAll();
      this.outPut.clearSelection();

      // For each of the hosts to test
      for ( int i = 0; i < this.resources.size() && this.running; ++i ) {
        // Initialize the counters
        host = this.resources.get( i ).getHostName();

        if ( null != host ) {
          try {
            adr = InetAddress.getByName( host );
          } catch( UnknownHostException e ) {
            exception = true;
          }

          if ( exception )
            // Print out which host we ping
            this.outPut.append( Messages.getString( "PingTestDialog.UnknownHostException" )  //$NON-NLS-1$
                                + host + this.outPut.getLineDelimiter() );
          else
            pingHost( adr, number );
        } else
          this.outPut.append( Messages.getString( "PingTestDialog.thePlusSpace" ) + i  //$NON-NLS-1$
                              + Messages.getString( "PingTestDialog.notResolved" )  //$NON-NLS-1$
                              + this.outPut.getLineDelimiter() );
      }
    }
    
    this.running = false;
  }
  
  private void pingHost( final InetAddress host, final int number ) {
    double pingDelay;
    double min = Long.MAX_VALUE; 
    double max = Long.MIN_VALUE;
    double avg = 0;
    int nOk = 0;
    int nFailed = 0;

    // Print out which host we ping
    this.outPut.append( Messages.getString( "PingTestDialog.pingMsg" )  //$NON-NLS-1$
                        + host + this.outPut.getLineDelimiter() );

    for ( int j = 0; j < number && this.running; ++j ) {
      pingDelay = ( ( PingTest )this.test ).ping( host );

      if ( -1 == pingDelay ) {
        ++nFailed;
       this.outPut.append( "Ping " + j + ": "   //$NON-NLS-1$//$NON-NLS-2$
                            + Messages.getString( "PingTestDialog.notReachable" )  //$NON-NLS-1$
                            + this.outPut.getLineDelimiter() );
      }
      else {
        ++nOk;
        if ( pingDelay < min )
          min = pingDelay;
        if ( pingDelay > max )
          max = pingDelay;
        avg += pingDelay;

        this.outPut.append( "Ping " + j + ": " //$NON-NLS-1$ //$NON-NLS-2$
                            + Messages.getString( "PingTestDialog.time" )  //$NON-NLS-1$
                            + df.format( pingDelay ) + " ms"  //$NON-NLS-1$
                            + this.outPut.getLineDelimiter() );
      }
    }
    
    // Write the summary
    this.outPut.append( number + Messages.getString( "PingTestDialog.transmitted" )  //$NON-NLS-1$
                        + nOk + Messages.getString( "PingTestDialog.receivedPlusSpace" )  //$NON-NLS-1$
                        + df.format( ( ( ( double )nFailed ) / number ) * 100 ) 
                        + Messages.getString( "PingTestDialog.packetLoss" )  //$NON-NLS-1$
                        + this.outPut.getLineDelimiter() ); 
    
    if ( nOk > 0 )
      this.outPut.append( Messages.getString( "PingTestDialog.summaryPlusSpace" )  //$NON-NLS-1$
                          + df.format( min ) + "/"  //$NON-NLS-1$
                          + df.format( avg/nOk ) + "/"  //$NON-NLS-1$
                          + df.format( max ) + " ms"  //$NON-NLS-1$
                          + this.outPut.getLineDelimiter() + this.outPut.getLineDelimiter() );
    else
      this.outPut.append( Messages.getString( "PingTestDialog.summaryFailed" )  //$NON-NLS-1$
                          + this.outPut.getLineDelimiter() + this.outPut.getLineDelimiter() );
  }
}
