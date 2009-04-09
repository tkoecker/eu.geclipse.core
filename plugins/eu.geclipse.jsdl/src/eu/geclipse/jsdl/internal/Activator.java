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
package eu.geclipse.jsdl.internal;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends Plugin {

  /**
   * The plug-in ID
   */
  public static final String PLUGIN_ID = "eu.geclipse.jsdl"; //$NON-NLS-1$
  // The shared instance
  private static Activator plugin;

  /**
   * The constructor
   */
  public Activator() {
    plugin = this;
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
   */
  @Override
  public void start( final BundleContext context ) throws Exception {
    super.start(context);
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
   */
  @Override
  public void stop( final BundleContext context ) throws Exception {
    plugin = null;
    super.stop(context);
  }

  /**
   * Returns the shared instance
   *
   * @return the shared instance
   */
  public static Activator getDefault() {
    return plugin;
  }

  /**
   * Logs an exception.
   *
   * @param exception the exception.
   */
  public static void logException( final Exception exception ) {
    String message = exception.getLocalizedMessage();
    if ( message == null )
      message = exception.getClass().getName();
    IStatus status = new Status( IStatus.ERROR,
                                 PLUGIN_ID,
                                 IStatus.OK,
                                 message,
                                 exception );
    getDefault().getLog().log( status );
  }

  /**
   * Logs a message.
   *
   * @param severity severity constant (defined in IStatus).
   * @param message the message to log.
   */
  public static void logMessage( final int severity, final String message ) {
    IStatus status = new Status( severity, PLUGIN_ID, IStatus.OK, message, null );
    getDefault().getLog().log( status );
  }

  /**
   * Logs a status object to the eclipse logger
   * 
   * @param status the status to be logged
   */
  public static void logStatus( final IStatus status ) {
    getDefault().getLog().log( status );
  }
}
