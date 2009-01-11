package eu.geclipse.aws.ec2.ui.internal;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

  /** The plug-in ID. */
  public static final String PLUGIN_ID = "eu.geclipse.aws.ec2.ui"; //$NON-NLS-1$

  /** The shared instance. */
  private static Activator plugin;

  /**
   * The constructor
   */
  public Activator() {
    // nothing to do here
  }

  /**
   * Start the bundle. Store a reference to self.
   */
  @Override
  public void start( final BundleContext context ) throws Exception {
    super.start( context );
    Activator.plugin = this;
  }

  /**
   * Stop the bundle. remove reference to self.
   */
  @Override
  public void stop( final BundleContext context ) throws Exception {
    Activator.plugin = null;
    super.stop( context );
  }

  /**
   * Returns the shared instance
   * 
   * @return the shared instance
   */
  public static Activator getDefault() {
    return Activator.plugin;
  }

  /**
   * Log the exception via an {@link IStatus}.
   * 
   * @param e the exception to log
   */
  public static void log( final Exception e ) {
    Activator.log( e.getLocalizedMessage(), e );
  }

  /**
   * Create a log entry with the given description and exception.
   * 
   * @param description a more descriptive text of the exception
   * @param e the exception to log
   */
  public static void log( String description, final Exception e ) {
    if( description == null ) {
      description = e.getClass().getName();
    }
    IStatus status = new Status( IStatus.ERROR,
                                 Activator.PLUGIN_ID,
                                 IStatus.OK,
                                 description,
                                 e );
    Activator.getDefault().getLog().log( status );
  }
}
