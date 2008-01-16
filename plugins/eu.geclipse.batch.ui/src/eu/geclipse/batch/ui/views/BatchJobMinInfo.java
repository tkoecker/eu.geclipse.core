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
 *     UCY (http://www.cs.ucy.ac.cy)
 *      - Harald Gjermundrod (harald@cs.ucy.ac.cy)
 *
 *****************************************************************************/
package eu.geclipse.batch.ui.views;

import eu.geclipse.batch.IBatchJobInfo;

/**
 * Class for holding minimal information about a specific Grid job.
 */
public class BatchJobMinInfo implements IBatchJobInfo {
  private String jobId;
  private String jobName;
  private String userAccount;
  private String timeUse;
  private JobState status;
  private String queueName;

  /**
   * Create a new QueueInfo holder, from the arguments.
   *
   * @param jobId The unique id of the job.
   * @param jobName The name of the job.
   * @param userAccount The name of the pool account that are used to execute this job.
   * @param timeUse How much time this job have used.
   * @param status The current status of this job.
   * @param queueName The name of the queue where this job is placed.
   */
  public BatchJobMinInfo ( final String jobId, final String jobName, final String userAccount,
                  final String timeUse, final JobState status, final String queueName ) {
    this.jobId = jobId;
    this.jobName = jobName;
    this.userAccount = userAccount;
    this.timeUse = timeUse;
    this.status = status;
    this.queueName = queueName;
  }

  /**
   * @return the queueName
   */
  public String getQueueName() {
    return this.queueName;
  }

  /**
   * @param queueName the queueName to set
   */
  public void setQueueName( final String queueName ) {
    this.queueName = queueName;
  }

  /**
   * @return the status
   */
  public JobState getStatus() {
    return this.status;
  }

  /**
   * @param status the status to set
   */
  public void setStatus( final JobState status ) {
    this.status = status;
  }

  /**
   * @return the timeUse
   */
  public String getTimeUse() {
    return this.timeUse;
  }

  /**
   * @param timeUse the timeUse to set
   */
  public void setTimeUse( final String timeUse ) {
    this.timeUse = timeUse;
  }

  /**
   * @return the userAccount
   */
  public String getUserAccount() {
    return this.userAccount;
  }
 
  /**
   * @param userAccount the userAccount to set
   */
  public void setUserAccount( final String userAccount ) {
    this.userAccount = userAccount;
  }

  /**
   * @return the jobId
   */
  public String getJobId() {
    return this.jobId;
  }

  /**
   * @return the jobName
   */
  public String getJobName() {
    return this.jobName;
  }

  /**
   * Can this job be deleted.
   * @return Returns <code>true</code> if this job can be deleted, <code>false</code> otherwise.
   */
  public boolean isDeletable() {
    return false;
  }
  
  /**
   * Can this job be moved.
   * @return Returns <code>true</code> if this job can be moved, <code>false</code> otherwise.
   */
  public boolean isMovable() {
    return false;
  }

  /**
   * Can this job be held.
   * @return Returns <code>true<\code> if this job can be held, <code>false</code> otherwise.
   */
  public boolean isHoldable() {
    return false;
  }

  /**
   * Can this job be released.
   * @return Returns <code>true<\code> if this job can be released, <code>false</code> otherwise.
   */
  public boolean isReleasable() {
    return false;
  }

  /**
   * Deletes this job from the batch service.
   */
  public void deleteJob() {
    // No code needed 
  }

  /**
   * Move this job to another worker node or batch service.
   * @param destQueue The destination queue, <code>null</code> if no destination queue.
   * @param destServer The destination server, <code>null</code> if no destination server.
   */
  public void moveJob( final String destQueue, final String destServer ) {
    // No code needed 
  }

  /**
   * Puts a hold on the job in the queue of the batch service.
   */
  public void holdJob() {
    // No code needed 
  }

  /**
   * Release the job with a previous hold in queue of the batch system.
   */
  public void releaseJob() {
    // No code needed 
  }
  
  /**
   * Return the type of batch service type that handles this batch job.
   *
   * @return Returns the batch service type if known, <code>null</code> if unknown.
   */
  public String getServiceTypeName () {
    return null;
  }
}
