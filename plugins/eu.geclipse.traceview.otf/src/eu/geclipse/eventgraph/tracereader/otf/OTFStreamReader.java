package eu.geclipse.eventgraph.tracereader.otf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import eu.geclipse.traceview.EventType;

class OTFStreamReader extends OTFUtils {
  private BufferedReader input;
  private int timestamp = 0;
  private int processNr;
  private OTFReader trace;

  OTFStreamReader( final File file, final OTFReader trace ) throws IOException {
//System.out.println( "Opening " + file );
    this.trace = trace;
    this.input = new BufferedReader( new FileReader( file ) );
    while( ( this.line = this.input.readLine() ) != null ) {
      this.lineIdx = 0;
      int ch = this.line.charAt( 0 );
      if ( Character.isDigit( ch ) || Character.isLowerCase( ch ) ) {
        readTimestamp();
      } else if ( ch == '*' ) {
        this.lineIdx++; // skip '*'
        readProcessNr();
      } else if ( ch == 'S' ) {
        this.lineIdx++; // skip 'S'
        readSend();
      } else if ( ch == 'R' ) {
        this.lineIdx++; // skip 'R'
        readRecv();
      }
    }
  }

  private void readRecv() throws IOException {
    int otfPartnerId = readNumber();
    int partnerId = this.trace.getProcessIdForOTFIndex( otfPartnerId );
    int processId = this.trace.getProcessIdForOTFIndex( this.processNr );
    ((Process) this.trace.getProcess( processId )).appendEvent( EventType.RECV, partnerId, this.timestamp );
//System.out.println( "Recv " + partnerId  );
  }

  private void readSend() throws IOException {
    int otfPartnerId = readNumber();
    int partnerId = this.trace.getProcessIdForOTFIndex( otfPartnerId );
    int processId = this.trace.getProcessIdForOTFIndex( this.processNr );
    ((Process) this.trace.getProcess( processId )).appendEvent( EventType.SEND, partnerId, this.timestamp );
//System.out.println( "Send " + partnerId  );
  }

  private void readProcessNr() {
    this.processNr = readNumber();
//System.out.println( "proc: " + processNr );
  }

  private void readTimestamp() {
    this.timestamp =  readNumber();
//System.out.println( "time: " + timestamp );
  }
}