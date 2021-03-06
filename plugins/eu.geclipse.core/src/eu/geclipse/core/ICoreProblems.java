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
 *    Mathias Stuempert - initial API and implementation
 *    Ariel Garcia      - added more strings
 *    Pawel Wolniewicz  - added more strings
 *****************************************************************************/
package eu.geclipse.core;


/**
 * This interface contains the ProblemID strings for accessing the core's
 * problems. These problem-ID's cover the issues expected to be common to
 * different plug-ins or middlewares, which can (and should) therefore be
 * reused by the whole g-Eclipse framework and specific implementations.
 * The middleware dependent or the plug-in specific problems should be
 * declared by the affected plug-in.
 */
public interface ICoreProblems {

  /*
   * Please keep the list sorted by extensionID!
   */
  
  // Access control
  public static final String ACCESSCONTROL_INVALID_ACL
    = "eu.geclipse.core.problem.accesscontrol.invalidACL"; //$NON-NLS-1$
  public static final String ACCESSCONTROL_INVALID_ACTOR
    = "eu.geclipse.core.problem.accesscontrol.invalidActor"; //$NON-NLS-1$
  public static final String ACCESSCONTROL_INVALID_CAPABILITY
    = "eu.geclipse.core.problem.accesscontrol.invalidCapability"; //$NON-NLS-1$
  public static final String ACCESSCONTROL_INVALID_ENTRY
    = "eu.geclipse.core.problem.accesscontrol.invalidEntry"; //$NON-NLS-1$
  public static final String ACCESSCONTROL_INVALID_POLICY
    = "eu.geclipse.core.problem.accesscontrol.invalidPolicy"; //$NON-NLS-1$
  
  // Auth
  public static final String AUTH_CERTIFICATE_LOAD_FAILED
    = "eu.geclipse.core.problem.auth.certificateLoadFailed"; //$NON-NLS-1$
  public static final String AUTH_CREDENTIAL_CREATE_FAILED
    = "eu.geclipse.core.problem.auth.credentialCreateFailed"; //$NON-NLS-1$
  public static final String AUTH_CREDENTIAL_SAVE_FAILED
    = "eu.geclipse.core.problem.auth.credentialSaveFailed"; //$NON-NLS-1$
  public static final String AUTH_INVALID_TOKEN_DESCRIPTION
    = "eu.geclipse.core.problem.auth.invalidTokenDescription"; //$NON-NLS-1$
  public static final String AUTH_KEY_LOAD_FAILED
    = "eu.geclipse.core.problem.auth.keyLoadFailed"; //$NON-NLS-1$
  public static final String AUTH_LOGIN_FAILED
    = "eu.geclipse.core.problem.auth.loginFailed"; //$NON-NLS-1$
  public static final String AUTH_PERMISSION_DENIED
    = "eu.geclipse.core.problem.auth.permissionDenied"; //$NON-NLS-1$  
  public static final String AUTH_TOKEN_ACTIVATE_FAILED
    = "eu.geclipse.core.problem.auth.tokenActivateFailed"; //$NON-NLS-1$
  public static final String AUTH_TOKEN_NOT_YET_VALID
    = "eu.geclipse.core.problem.auth.tokenNotYetValid"; //$NON-NLS-1$
  public static final String AUTH_TOKEN_REQUEST_CANCELED
    = "eu.geclipse.core.problem.auth.tokenRequestCanceled"; //$NON-NLS-1$

  public static final String CONFIG_INVALID_VO_PARAMETER
    = "eu.geclipse.core.problem.config.invalidVoParameter"; //$NON-NLS-1$
  public static final String CONFIG_MISSING_VO_PARAMETER
    = "eu.geclipse.core.problem.config.missingVoParameter"; //$NON-NLS-1$
  public static final String CONFIG_WRONG_VO_CREATOR
    = "eu.geclipse.core.problem.config.wrongVoCreator"; //$NON-NLS-1$
  
  // IO
  public static final String IO_CORRUPTED_FILE
    = "eu.geclipse.core.problem.io.corruptedFile"; //$NON-NLS-1$
  public static final String IO_FILE_EXISTS
    = "eu.geclipse.core.problem.io.fileExists"; //$NON-NLS-1$
  public static final String IO_FILE_NOT_FOUND
    = "eu.geclipse.core.problem.io.fileNotFound"; //$NON-NLS-1$
  public static final String IO_OPERATION_FAILED
    = "eu.geclipse.core.problem.io.operationFailed"; //$NON-NLS-1$
  public static final String IO_UNSPECIFIED_PROBLEM
    = "eu.geclipse.core.problem.io.unspecified"; //$NON-NLS-1$
  
  // Jobs
  public static final String JOB_DESCRIPTION_INVALID
    = "eu.geclipse.core.problem.job.invalidJobDescription"; //$NON-NLS-1$
  public static final String JOB_SUBMISSION_FAILED
    = "eu.geclipse.core.problem.job.submissionFailed"; //$NON-NLS-1$
  
  // Model
  public static final String MODEL_CONTAINER_CAN_NOT_CONTAIN
    = "eu.geclipse.core.problem.model.containerCanNotContain"; //$NON-NLS-1$
  public static final String MODEL_ELEMENT_CREATE_FAILED
    = "eu.geclipse.core.problem.model.elementCreateFailed"; //$NON-NLS-1$
  public static final String MODEL_ELEMENT_DELETE_FAILED
    = "eu.geclipse.core.problem.model.elementDeleteFailed"; //$NON-NLS-1$
  public static final String MODEL_ELEMENT_LOAD_FAILED
    = "eu.geclipse.core.problem.model.elementLoadFailed"; //$NON-NLS-1$
  public static final String MODEL_ELEMENT_NOT_MANAGEABLE
    = "eu.geclipse.core.problem.model.elementNotManageable"; //$NON-NLS-1$
  public static final String MODEL_ELEMENT_SAVE_FAILED
    = "eu.geclipse.core.problem.model.elementSaveFailed"; //$NON-NLS-1$
  public static final String MODEL_FETCH_CHILDREN_FAILED
    = "eu.geclipse.core.problem.model.fetchChildrenFailed"; //$NON-NLS-1$
  public static final String MODEL_PREFERENCE_CREATION_FAILED
    = "eu.geclipse.core.problem.model.preferenceCreationFailed"; //$NON-NLS-1$
  public static final String MODEL_REFRESH_FAILED
    = "eu.geclipse.core.problem.model.refreshFailed"; //$NON-NLS-1$
  
  // Net
  public static final String NET_BIND_FAILED
    = "eu.geclipse.core.problem.net.bindFailed"; //$NON-NLS-1$
  public static final String NET_CONNECTION_FAILED
    = "eu.geclipse.core.problem.net.connectionFailed"; //$NON-NLS-1$
  public static final String NET_CONNECTION_TIMEOUT
    = "eu.geclipse.core.problem.net.connectionTimeout"; //$NON-NLS-1$
  public static final String NET_MALFORMED_URL
    = "eu.geclipse.core.problem.net.malformedURL"; //$NON-NLS-1$
  public static final String NET_UNKNOWN_HOST
    = "eu.geclipse.core.problem.net.unknownHost"; //$NON-NLS-1$
  
  //public static final String SECURITY_CERT_CREATION_FAILED
    //= "eu.geclipse.core.problem.security.certCreationFailed";
  public static final String SECURITY_CERT_LOAD_FAILED
    = "eu.geclipse.core.problem.security.certLoadFailed";
  public static final String SECURITY_CERT_SAVE_FAILED
    = "eu.geclipse.core.problem.security.certSaveFailed";
  
  // System
  public static final String SYS_SYSTEM_TIME_CHECK_FAILED
    = "eu.geclipse.core.problem.sys.systemTimeCheckFailed"; //$NON-NLS-1$
  
  // Tar
  public static final String TAR_BAD_HEADER_CHECKSUM
    = "eu.geclipse.core.problem.tar.badHeaderChecksum"; //$NON-NLS-1$
  public static final String TAR_INVALID_ENTRY_SIZE
    = "eu.geclipse.core.problem.tar.invalidEntrySize"; //$NON-NLS-1$
  public static final String TAR_INVALID_ENTRY_TYPE
    = "eu.geclipse.core.problem.tar.invalidEntryType"; //$NON-NLS-1$
  public static final String TAR_UNSUPPORTED_ENTRY_TYPE
    = "eu.geclipse.core.problem.tar.unsupportedEntryType"; //$NON-NLS-1$
  public static final String TAR_WRONG_HEADER_SIZE
    = "eu.geclipse.core.problem.tar.wrongHeaderSize"; //$NON-NLS-1$
  
  // VO
  public static final String VO_IMPORT_INCOMPLETE
    = "eu.geclipse.core.problem.vo.importIncomplete"; //$NON-NLS-1$
  public static final String VO_IMPORT_FAILED
    = "eu.geclipse.core.problem.vo.importFailed"; //$NON-NLS-1$

}
