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
 *     UCY (http://www.ucy.cs.ac.cy)
 *      - George Tsouloupas (georget@cs.ucy.ac.cy)
 *
 *****************************************************************************/

package eu.geclipse.info.glue;

import java.util.ArrayList;

/**
 * @author George Tsouloupas
 * TODO Write Comments
 */
public class GlueSubCluster extends AbstractGlueTable
  implements java.io.Serializable
{

  private static final long serialVersionUID = 1L;

  /**
   * 
   */
  public GlueIndex glueIndex;

  /**
   * 
   */
  public String UniqueID; // PK

  /**
   * 
   */
  public String keyName = "UniqueID"; //$NON-NLS-1$

  /**
   * 
   */
  public String Name;

  /**
   * 
   */
  public GlueCluster glueCluster; // GlueClusterUniqueID

  /**
   * 
   */
  public Long RAMSize;

  /**
   * 
   */
  public Long RAMAvailable;

  /**
   * 
   */
  public Long VirtualSize;

  /**
   * 
   */
  public Long VirtualAvailable;

  /**
   * 
   */
  public String PlatformType;

  /**
   * 
   */
  public Long SMPSize;

  /**
   * 
   */
  public String OSName;

  /**
   * 
   */
  public String OSRelease;

  /**
   * 
   */
  public String OSVersion;

  /**
   * 
   */
  public String Vendor;

  /**
   * 
   */
  public String Model;

  /**
   * 
   */
  public String Version;

  /**
   * 
   */
  public Long ClockSpeed;

  /**
   * 
   */
  public String InstructionSet;

  /**
   * 
   */
  public String OtherProcessorDescription;

  /**
   * 
   */
  public Long CacheL1;

  /**
   * 
   */
  public Long CacheL1I;

  /**
   * 
   */
  public Long CacheL1D;

  /**
   * 
   */
  public Long CacheL2;

  /**
   * 
   */
  public Long BenchmarkSF00;

  /**
   * 
   */
  public Long BenchmarkSI00;

  /**
   * 
   */
  public String InboundIP;

  /**
   * 
   */
  public String OutboundIP;

  /**
   * 
   */
  public String InformationServiceURL;

  /**
   * 
   */
  public Long PhysicalCPUs;

  /**
   * 
   */
  public Long LogicalCPUs;

  /**
   * 
   */
  public String TmpDir;

  /**
   * 
   */
  public String WNTmpDir;

  /**
   * 
   */
  //public Date MeasurementDate;

  /**
   * 
   */
  //public Date MeasurementTime;

  /**
   * 
   */
  public ArrayList<GlueHostRemoteFileSystem> glueHostRemoteFileSystemList
    = new ArrayList<GlueHostRemoteFileSystem>();

  /**
   * 
   */
  public ArrayList<GlueSubClusterLocation> glueSubClusterLocationList
    = new ArrayList<GlueSubClusterLocation>();

  /**
   * 
   */
  public ArrayList<GlueSubClusterSoftwareRunTimeEnvironment> glueSubClusterSoftwareRunTimeEnvironmentList
    = new ArrayList<GlueSubClusterSoftwareRunTimeEnvironment>();


    /* (non-Javadoc)
   * @see eu.geclipse.info.glue.AbstractGlueTable#getID()
   */
  @Override
  public String getID() {
    return this.UniqueID;
  }

  /**
   * Set this.UniqueID
   * @param id
   */
  public void setID( final String id ) {
    this.UniqueID = id;
  }
}
