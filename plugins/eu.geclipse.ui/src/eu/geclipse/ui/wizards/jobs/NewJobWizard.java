/******************************************************************************
 * Copyright (c) 2006, 2007 g-Eclipse consortium 
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

package eu.geclipse.ui.wizards.jobs;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import eu.geclipse.core.model.GridModel;
import eu.geclipse.core.model.IGridContainer;
import eu.geclipse.core.model.IGridElement;
import eu.geclipse.core.model.IGridProject;
import eu.geclipse.core.model.impl.JSDLJobDescription;
import eu.geclipse.ui.internal.wizards.jobs.FileType;


/**
 * Wizard for creating new jsdl file
 * 
 * @author katis
 */
public class NewJobWizard extends Wizard implements INewWizard {

  private IStructuredSelection selection;
  private WizardNewFileCreationPage firstPage;
  private IFile file;
//  private EnvNewJobWizardPage envPage;
  private FilesInputNewJobWizardPage inputFilesPage;
  private ExecutableNewJobWizardPage executablePage;
  private FilesOutputNewJobWizardPage outputFilesPage;

  
  @Override
  public void addPages()
  {
    this.firstPage = new FirstPage( Messages.getString( "NewJobWizard.first_page_name" ), //$NON-NLS-1$
                                    this.selection );
    this.firstPage.setTitle( Messages.getString( "NewJobWizard.first_page_title" ) ); //$NON-NLS-1$
    this.firstPage.setDescription( Messages.getString( "NewJobWizard.first_page_description" ) ); //$NON-NLS-1$
    this.firstPage.setFileName( Messages.getString( "NewJobWizard.first_page_default_new_file_name" ) ); //$NON-NLS-1$
    addPage( this.firstPage );
    
    ArrayList<WizardPage> internal = new ArrayList<WizardPage>(); 
    this.inputFilesPage = new FilesInputNewJobWizardPage( Messages.getString( "NewJobWizard.files_input_new_job_page_name" ) ); //$NON-NLS-1$
    
//    addPage( this.inputFilesPage );
    internal.add( this.inputFilesPage );
    this.outputFilesPage = new FilesOutputNewJobWizardPage( Messages.getString( "NewJobWizard.files_output_new_job_page_name" ) ); //$NON-NLS-1$;
//    addPage( this.outputFilesPage );
    internal.add( this.outputFilesPage );
    
    this.executablePage = new ExecutableNewJobWizardPage( Messages.getString( "NewJobWizard.executablePageName" ), internal); //$NON-NLS-1$
    addPage( this.executablePage );
//    this.inputFilesPage = new FilesInputNewJobWizardPage( Messages.getString( "NewJobWizard.files_input_new_job_page_name" ) ); //$NON-NLS-1$
//    addPage( this.inputFilesPage );
//    this.outputFilesPage = new FilesOutputNewJobWizardPage( Messages.getString( "NewJobWizard.files_output_new_job_page_name" ) ); //$NON-NLS-1$;
//    addPage( this.outputFilesPage );
    
//    this.envPage = new EnvNewJobWizardPage( Messages.getString( "NewJobWizard.env_page_name" ) ); //$NON-NLS-1$
//    addPage( this.envPage );
    
  }

  public void init( final IWorkbench workbench, final IStructuredSelection sel )
  {
    setWindowTitle( Messages.getString( "NewJobWizard.windowTitle" ) ); //$NON-NLS-1$
    this.selection = sel;
  }

  @Override
  public boolean performFinish()
  {
    boolean result = false;
    IRunnableWithProgress op = new IRunnableWithProgress() {

      public void run( final IProgressMonitor monitor )
        throws InvocationTargetException
      {
        try {
          createFile( monitor );
        } finally {
          monitor.done();
        }
      }
    };
    try {
      getContainer().run( false, true, op );
      result = true;
    } catch( InterruptedException e ) {
      result = false;
    } catch( InvocationTargetException e ) {
      Throwable realException = e.getTargetException();
      MessageDialog.openError( getShell(),
                               Messages.getString( "NewJobWizard.error_title" ), realException.getMessage() ); //$NON-NLS-1$
      result = false;
    }
    return result;
  }

  protected void createFile( final IProgressMonitor monitor )
  {
    monitor.beginTask( Messages.getString( "NewJobWizard.creating_task" ) + this.firstPage.getFileName(), 2 ); //$NON-NLS-1$
    this.file = this.firstPage.createNewFile();
    
    JSDLJobDescription jsdlJobDescription = null;
    IGridElement element = GridModel.getRoot().findElement( this.file );
    if (element instanceof JSDLJobDescription){
      jsdlJobDescription = (JSDLJobDescription) element;
    }
    
    
    monitor.worked( 1 );
    monitor.setTaskName( Messages.getString( "NewJobWizard.setting_contents_task" ) + this.firstPage.getFileName() ); //$NON-NLS-1$
//    this.file.setContents( getInitialStream(), true, true, monitor );
    if (jsdlJobDescription != null){
      setInitialModel( jsdlJobDescription );
      jsdlJobDescription.save(this.file);
    }
    // jsdl -> jdl translation test
//     JSDLJobDescription descr = new JSDLJobDescription(file);
//     try {
//     WMSClient a = new WMSClient(new
//     URL("https://wms1.cyf-kr.edu.pl:7443/glite_wms_wmproxy_server"));
//     System.out.println(a.translateJSDLtoJDL( jsdlJobDescription ));
//     } catch( MalformedURLException e ) {
//     // TODO Auto-generated catch block
//     e.printStackTrace();
//     } catch( ServiceException e ) {
//      // TODO Auto-generated catch block
//      e.printStackTrace();
//    }
    // end of test
    // catch( ServiceException e ) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    monitor.worked( 1 );
  }
  
  
  private void setInitialModel(final JSDLJobDescription jsdl){
    
    this.executablePage.getApplicationSpecificPage();
    jsdl.createRoot();
    jsdl.addJobDescription();
    jsdl.addJobIdentification( this.executablePage.getJobName(),
                               this.executablePage.getJobDescription() );
    if( !this.executablePage.getExecutableFile().equals( "" ) ) { //$NON-NLS-1$
      jsdl.addApplication();
      String in = null;
      String out = null;
      String inName = null;
      if( this.inputFilesPage.isCreated() ) {
        in = this.inputFilesPage.getStdin();
        out = this.inputFilesPage.getStdout();
        if( in.equals( Messages.getString( "FilesInputNewJobWizardPage.stdin_info" ) ) ) { //$NON-NLS-1$
          in = null;
        } else {
          inName = this.inputFilesPage.getStdinName();
        }
        if( out.equals( Messages.getString( "FilesInputNewJobWizardPage.stdin_info" ) ) ) { //$NON-NLS-1$
          out = null;
        }
      }
      jsdl.addPOSIXApplicationDetails( this.executablePage.getApplicationName(),
                                       this.executablePage.getExecutableFile(),
                                       in,
                                       inName,
                                       out );
    }
    if( this.outputFilesPage.isCreated() ) {
      HashMap<String, String> outFiles = this.outputFilesPage.getFiles( FileType.OUTPUT );
      if( !outFiles.isEmpty() ) {
        for( String name : outFiles.keySet() ) {
          jsdl.setOutDataStaging( name, outFiles.get( name ) );
        }
      }
      outFiles = this.outputFilesPage.getFiles( FileType.INPUT );
      if( !outFiles.isEmpty() ) {
        for( String name : outFiles.keySet() ) {
          jsdl.setInDataStaging( name, outFiles.get( name ) );
        }
      }
    }
    // adding specific arguments
    if( this.executablePage.getApplicationSpecificPage() != null ) {
      Map<String, ArrayList<String>> arguments = this.executablePage.getApplicationSpecificPage()
        .getParametersValues();
      for( String argName : arguments.keySet() ) {
        jsdl.addArgumentForPosixApplication( argName, arguments.get( argName ) );
      }
    }
  }

  
  class FirstPage extends WizardNewFileCreationPage {

    private IStructuredSelection iniSelection;

    /**
     * Creates new instance of {@link FirstPage}
     * @param pageName name of this page
     * @param selection selection to be pass to new instance
     */
    public FirstPage( final String pageName, final IStructuredSelection selection ) {
      super( pageName, selection );
      this.iniSelection = selection;
    }
    

    @Override
    protected void initialPopulateContainerNameField()
    {
      {
        Object obj = this.iniSelection.getFirstElement();
        if( obj instanceof IGridContainer ) {
          IGridElement element = ( IGridElement ) obj;
          IGridProject project = element.getProject();
          if ( project != null ) {
            IGridElement descriptions = project.findChild( IGridProject.DIR_JOBDESCRIPTIONS );
            if ( descriptions != null ) {
              IPath cPath = descriptions.getPath();
              IPath ePath = element.getPath();
              if ( !cPath.isPrefixOf( ePath ) ) {
                element = descriptions;
              }
            }
          }
          super.setContainerFullPath( element.getPath() );
        } else {
          super.initialPopulateContainerNameField();
        }
      }
    }
  }
}