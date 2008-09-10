/******************************************************************************
 * Copyright (c) 2007-2008 g-Eclipse consortium 
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
 *     Nikolaos Tsioutsias - University of Cyprus
 *     David Johnson - University of Reading, UK
 *****************************************************************************/

package eu.geclipse.ui.properties;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import eu.geclipse.core.model.IGridConnection;
import eu.geclipse.core.model.IGridConnectionElement;
import eu.geclipse.core.model.IGridJob;
import eu.geclipse.core.model.IGridJobDescription;
import eu.geclipse.core.model.IGridJobService;
import eu.geclipse.core.model.IGridProject;
import eu.geclipse.core.model.IGridStorage;
import eu.geclipse.core.model.IPropertiesProvider;
import eu.geclipse.core.model.IVirtualOrganization;
import eu.geclipse.core.model.impl.AbstractGridInfoService;
import eu.geclipse.info.model.GridApplication;
import eu.geclipse.info.model.GridGlueComputing;
import eu.geclipse.info.model.GridGlueService;
import eu.geclipse.info.model.GridGlueServiceApplication;
import eu.geclipse.info.model.GridGlueStorage;
import eu.geclipse.workflow.IGridWorkflowDescription;


public class PropertiesFactory implements IPropertiesFactory {

  /* (non-Javadoc)
   * @see eu.geclipse.ui.properties.IPropertiesFactory#getPropertySources(java.lang.Object)
   */
  public List< AbstractPropertySource< ? > > getPropertySources( final Object sourceObject )
  {
    List< AbstractPropertySource< ? > > sourcesList
      = new ArrayList< AbstractPropertySource< ? > >();
    
    if ( sourceObject instanceof IVirtualOrganization ) {
      sourcesList.add( new VOPropertySource( ( IVirtualOrganization ) sourceObject ) );
    }
    
    if ( sourceObject instanceof URI ) {
      sourcesList.add( new URIPropertySource( ( URI ) sourceObject ) );
    }
    
    if ( ( sourceObject instanceof IGridJobDescription ) && !( sourceObject instanceof IGridWorkflowDescription ) ) {
      sourcesList.add( new GridJobDescSource( ( IGridJobDescription ) sourceObject ) );
    }
    
    if (sourceObject instanceof IGridWorkflowDescription) {
      sourcesList.add( new GridWorkflowDescSource ( (IGridWorkflowDescription) sourceObject ) );
    }
    
    if ( sourceObject instanceof IGridProject ) {
      sourcesList.add( new GridProjectSource( ( IGridProject ) sourceObject ) );
    }
    
    if ( sourceObject instanceof IGridConnection ) {
      sourcesList.add( new ConnectionPropertySource( ( IGridConnection ) sourceObject ) );
    }
    
    if ( sourceObject instanceof IGridJob ) {
      sourcesList.add( new GridJobSource( ( IGridJob ) sourceObject ) );
    }
    
    if ( sourceObject instanceof IGridConnectionElement ) {
      sourcesList.add( new GridConnectionElementSource( ( IGridConnectionElement ) sourceObject ) );
    }
    
    if ( sourceObject instanceof IPropertiesProvider ) {
      sourcesList.add( new PropertiesProviderSource( ( IPropertiesProvider ) sourceObject ) );
    }
    
    if ( sourceObject instanceof GridGlueComputing ) {
      sourcesList.add( new GridGlueComputingSource( ( GridGlueComputing ) sourceObject ) );
    }
    
    if ( sourceObject instanceof GridGlueStorage ) {
      sourcesList.add( new GridGlueStorageSource( ( GridGlueStorage ) sourceObject ) );
    }
    
    if ( sourceObject instanceof GridGlueService ) {
      sourcesList.add( new GridGlueServiceSource( ( GridGlueService ) sourceObject ) );
    }
    
    if ( sourceObject instanceof GridApplication ) {
      sourcesList.add( new GridApplicationSource( ( GridApplication ) sourceObject ) );
    }
    
    if ( sourceObject instanceof AbstractGridInfoService)
    {
      sourcesList.add( new AbstractGridInfoServiceSource( ( AbstractGridInfoService ) sourceObject ) );
    }
    
    if (sourceObject instanceof IGridJobService)
    {
      sourcesList.add( new IGridJobServiceSource( ( IGridJobService ) sourceObject ) );
    }
    
    if (sourceObject instanceof GridGlueServiceApplication)
    {
      sourcesList.add( new GridGlueServiceApplicationSource( ( GridGlueServiceApplication )sourceObject ) );
    }
    
    if (sourceObject instanceof IGridStorage
        && !(sourceObject instanceof GridGlueStorage))
    {
      sourcesList.add( new IGridStorageSource( ( IGridStorage )sourceObject ) );
    }
    return sourcesList;
  }
}
