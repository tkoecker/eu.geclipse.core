/*****************************************************************************
 * Copyright (c) 2008 g-Eclipse Consortium 
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
 *    Szymon Mueller - initial API and implementation
 *****************************************************************************/
package eu.geclipse.core.filesystem;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileInfo;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;

import eu.geclipse.core.ExtensionManager;
import eu.geclipse.core.filesystem.internal.Activator;
import eu.geclipse.core.filesystem.internal.filesystem.GEclipseFileStore;
import eu.geclipse.core.model.ITransferManager;
import eu.geclipse.core.model.ITransferService;

/**
 * Manager class for handling all the transfers.
 */
public class TransferManager implements ITransferManager {
  
  private static final int INF = 10;
  private static final int HIGH = 1;
  private static final int MID = 2;
  private static final int LOW = 3;

  private static TransferManager singleton;
  
  int counter = 1;
  
  private List<TransferOperation> pendingTransfers;
  
  private final boolean useRepo = true;
  

  
  /**
   * Constructor of TransferManager class. It checks repository for unfinished
   * transfer operations and tries to resume them.
   */
  public TransferManager() {
    TransferRepository repo = TransferRepository.getTransferRepository();
    this.pendingTransfers = repo.getOperations();
  }
  
  /**
   * Get singleton of the TransferManager
   * 
   * @return singleton of this manager
   */
  public static TransferManager getManager() {
    if( singleton == null ) {
      singleton = new TransferManager();
    }
    return singleton;
  }
  
  public List<TransferOperation> getPendingTransfers() {
    return this.pendingTransfers;
  }

  public boolean resumeTransfer( final TransferOperation op,
                                 final IProgressMonitor monitor )
  {
    boolean status = false;
    ITransferService service = findService( op.getSource().toURI().getScheme(),
                                            op.getDestination()
                                              .toURI()
                                              .getScheme() );
    status = service.transfer( op.getSource(), op.getDestination(), monitor );
    if( status && this.useRepo ) {
      TransferRepository repo = TransferRepository.getTransferRepository();
      repo.delete( op.getId() );
    }
    return status;
  }

  public boolean doTransfer( final IFileStore sourceGecl,
                             final IFileStore destinationGecl,
                             final boolean moveFlag,
                             final IProgressMonitor monitor )
  {
    boolean status = false;
    IFileStore source;
    IFileStore destination;
    
    if ( sourceGecl instanceof GEclipseFileStore ) {
      source = ((GEclipseFileStore) sourceGecl).getSlave();
    } else {
      source = sourceGecl;
    }
    if ( destinationGecl instanceof GEclipseFileStore ) {
      destination = ((GEclipseFileStore) destinationGecl).getSlave();
    } else {
      destination = destinationGecl;
    }
    ITransferService service = findService( source.toURI().getScheme(),
                                            destination.toURI().getScheme() );
    Integer transferId = getNextKey();
    TransferOperation op = new TransferOperation( transferId,
                                                  source,
                                                  destination,
                                                  "",
                                                  source.fetchInfo().getLength() );
    // Should never happen, all transfer types should at least have basic
    // transfer operation available
    Assert.isNotNull( service, "No operation defined for this type of transfer" );
    TransferRepository repo = TransferRepository.getTransferRepository();
    if( this.useRepo ) {
      repo.save( op );
    }
    if ( monitor.isCanceled() ) {
      throw new OperationCanceledException();
    }
    status = service.transfer( source, destination, monitor );
    if( this.useRepo ) {
      repo.delete( transferId );
    }
    return status;
  }
  
  private boolean isTransferPending( final int id ) {
    boolean result = false;
    for( TransferOperation op: this.pendingTransfers ) {
      if( op.getId().intValue() == id ) {
        result = true;
      }
    }
    return result;
  }

  private ITransferService findService( final String scheme1,
                                        final String scheme2 )
  {
    ITransferService operation = null;
    ExtensionManager manager = new ExtensionManager();
    List<IConfigurationElement> transfers = manager.getConfigurationElements( "eu.geclipse.core.filesystem.transferService",
                                                                              "transfer" );
    int priority = TransferManager.INF;
    
    for ( IConfigurationElement provider: transfers ) {
      if ( provider.getAttribute( "source" ).equalsIgnoreCase( scheme1 ) 
          && provider.getAttribute( "target" ).equalsIgnoreCase( scheme2 ) 
          && Integer.valueOf( provider.getAttribute( "priority" ) ).intValue() < priority ) {
        
        try {
          operation = (ITransferService) provider.createExecutableExtension( "class" );
          priority = Integer.valueOf( provider.getAttribute( "priority" ) ).intValue();
        } catch( CoreException e ) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
      
    }
    transfers.isEmpty();
    // TODO search extensions for operation with highest priority
    // FIXME tutaj
    if( operation == null ) {
      operation = new DefaultTransferService();
    }
    return operation;
  }
  private class DefaultTransferService implements ITransferService {

    /**
     * Copy the content of the specified file store to the destination file
     * store. If destination IFileStore exists, then it will be deleted before
     * the operation.
     * 
     * @param from The source of the transfer.
     * @param to The target of the transfer.
     * @param monitor A progress monitor used to track the transfer.
     * @return A status object tracking errors of the transfer.
     */
    public boolean transfer( final IFileStore from,
                             final IFileStore to,
                             final IProgressMonitor monitor )
    {
      boolean transferStatus = true;
      // Prepare copy operation
      monitor.beginTask( Messages.getString( "TransferManager.copying_progress" ) + from.getName(), 100 ); //$NON-NLS-1$
      monitor.setTaskName( Messages.getString( "TransferManager.copying_progress" ) + from.getName() ); //$NON-NLS-1$
      InputStream inStream = null;
      OutputStream outStream = null;
      if( from instanceof GEclipseFileStore ) {
        ((GEclipseFileStore)from).setActive( GEclipseFileStore.FETCH_INFO_ACTIVE_POLICY );
      }
      if( !from.fetchInfo().isDirectory() ) {
      // File
      try {
        // Fetch file info
        IFileInfo sourceFileInfo = from.fetchInfo();
        long length = sourceFileInfo.getLength();
        // Open input stream
        if( transferStatus ) {
          try {
            inStream = from.openInputStream( EFS.NONE,
                                             new SubProgressMonitor( monitor, 8 ) );
          } catch( CoreException cExc ) {
            transferStatus = false;
          }
        }
        // Open output stream
        if( transferStatus ) {
          try {
            IFileStore targetFile = to.getChild( from.getName() );
            //TODO possible solution to possible bug >_<
//            if(  targetFile.fetchInfo().exists() ) {
//              targetFile.delete( EFS.NONE, monitor );
//            }
            outStream = targetFile.openOutputStream( EFS.NONE,
                                                     new SubProgressMonitor( monitor,
                                                                             8 ) );
          } catch( CoreException cExc ) {
            transferStatus = false;
          }
        }
        // Prepare copy operation
        if( transferStatus && ( inStream != null ) && ( outStream != null ) ) {
          byte[] buffer = new byte[ 1024 * 1024 ];
          Integer totalKb = Integer.valueOf( ( int )Math.ceil( ( double )length / 1024 ) );
          SubProgressMonitor subMonitor = new SubProgressMonitor( monitor, 84 );
          subMonitor.beginTask( Messages.getString( "TransferManager.copying_progress" ) + from.getName(), ( int )length ); //$NON-NLS-1$
          subMonitor.subTask( Messages.getString( "TransferManager.copying_progress" ) + from.getName() ); //$NON-NLS-1$
          // Copy the data
          long byteCounter = 0;
          while( !subMonitor.isCanceled() && transferStatus ) {
            int bytesRead = -1;
            // Read data from source
            try {
              bytesRead = inStream.read( buffer );
            } catch( IOException ioExc ) {
              transferStatus = false;
            }
            // Check if there is still data available
            if( bytesRead == -1 ) {
              break;
            }
            // Write data to target
            try {
              outStream.write( buffer, 0, bytesRead );
            } catch( IOException ioExc ) {
              transferStatus = false;
            }
            byteCounter += bytesRead;
            subMonitor.worked( bytesRead );
            subMonitor.subTask( String.format( Messages.getString( "TransferManager.transfer_progress_format" ), //$NON-NLS-1$
                                               sourceFileInfo.getName(),
                                               Integer.valueOf( ( int )( 100. * byteCounter / length ) ),
                                               Integer.valueOf( ( int )( byteCounter / 1024 ) ),
                                               totalKb ) );
          }
          subMonitor.done();
        }
      } finally {
        monitor.subTask( Messages.getString( "TransferManager.closing_streams" ) ); //$NON-NLS-1$
        // Close output stream
        if( outStream != null ) {
          try {
            outStream.close();
          } catch( IOException ioExc ) {
            // just ignore this
          }
        }
        // Close input stream
        if( inStream != null ) {
          try {
            inStream.close();
          } catch( IOException e ) {
            // just ignore this
          }
        }
        monitor.done();
      }
      } else {
        //Directory
        copyDirectory( from, to, monitor );
      }
      return transferStatus;
    }
    
    /**
     * Copies this specified directory file store and its children recursively to the
     * destination file store.
     * 
     * @param from The source of the transfer.
     * @param to The target of the transfer.
     * @param monitor A progress monitor used to track the transfer.
     * @return A status object tracking errors of the transfer.
     */
    private IStatus copyDirectory( final IFileStore from,
                                   final IFileStore to,
                                   final IProgressMonitor monitor ) {
      
      // Prepare operation
      IStatus status = Status.OK_STATUS;
      monitor.beginTask( Messages.getString("TransferManager.copying_progress") + from.getName(), 10 ); //$NON-NLS-1$
      
      try {
        
        IFileStore[] children = null;
        try {
          GEclipseFileSystem.assureFileStoreIsActive( from );
          children = from.childStores( EFS.NONE, new SubProgressMonitor( monitor, 1 ) );
        } catch( CoreException cExc ) {
          status = new Status( IStatus.ERROR,
                               Activator.PLUGIN_ID,
                               IStatus.OK,
                               Messages.getString("TransferManager.fetching_children_error") + from.getName(), //$NON-NLS-1$
                               cExc );
        }
        
        if ( status.isOK() && ( children != null ) ) {
          
          IFileStore newTo = to.getChild( from.getName() );
          IFileInfo newToInfo = newTo.fetchInfo();
          boolean mkdir = !newToInfo.exists();
        
          SubProgressMonitor subMonitor = new SubProgressMonitor( monitor, 9 );
          subMonitor.beginTask( Messages.getString("TransferManager.copying_progress") //$NON-NLS-1$
                                + from.getName(),
                                mkdir ? children.length + 1 : children.length );
        
          if ( mkdir ) {
            try {
              newTo.mkdir( EFS.NONE, new SubProgressMonitor( subMonitor, 1 ) );
            } catch( CoreException cExc ) {
              status = new Status( IStatus.ERROR,
                                   Activator.PLUGIN_ID,
                                   IStatus.OK,
                                   Messages.getString("TransferManager.create_dir_error") + newTo.getName(), //$NON-NLS-1$
                                   cExc );
            }
          }
        
          if ( status.isOK() ) {
          
            MultiStatus mStatus
              = new MultiStatus( Activator.PLUGIN_ID,
                                 IStatus.OK,
                                 Messages.getString("TransferManager.copying_members_status") //$NON-NLS-1$
                                   + from.getName(),
                                 null );
            
            for ( IFileStore child : children ) {
              
//              IStatus tempStatus = copy( child, newTo, new SubProgressMonitor( subMonitor, 1) );
              boolean statusChild = transfer( child, newTo, new SubProgressMonitor( subMonitor, 1 ) );
              
              
              if ( subMonitor.isCanceled() ) {
                break;
              }
              
            }
            
            if ( !mStatus.isOK() ) {
              status = mStatus;
            }
          
          }
          
        }
        
      } finally {
        monitor.done();
      }
      
      return status;
      
    }
  }
  
  

  private synchronized Integer getNextKey() {
    return Integer.valueOf( this.counter++ );
  }

  public int registerTransfer( final IFileStore source,
                               final IFileStore destination )
  {
    // TODO Auto-generated method stub
    TransferRepository repo = TransferRepository.getTransferRepository();
    Integer transferId = getNextKey();
    TransferOperation op = new TransferOperation( transferId,
                                                  source,
                                                  destination,
                                                  "",
                                                  0 );
    repo.save( op );
    return transferId.intValue();
  }

  public void unregisterTransfer( final Integer transferID ) {
    TransferRepository repo = TransferRepository.getTransferRepository();
    repo.delete( transferID );
  }
}
