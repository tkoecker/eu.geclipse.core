package eu.geclipse.info.model;

import java.net.URI;

import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;

import eu.geclipse.core.model.IGridApplication;
import eu.geclipse.core.model.IGridContainer;
import eu.geclipse.core.model.IGridProject;
import eu.geclipse.info.glue.AbstractGlueTable;
import eu.geclipse.info.glue.GlueSubCluster;
import eu.geclipse.info.glue.GlueSubClusterSoftwareRunTimeEnvironment;


public class GridApplication extends GridGlueElement implements IGridApplication {

  private URI script = null;
  
  public GridApplication( final IGridContainer parent, 
                          final GlueSubClusterSoftwareRunTimeEnvironment software ) {
    super( parent, software );
  }

  public URI getScript() {
    return this.script;
  }

  public String getTag() {
    String tag = null;
    if (getGlueElement() != null && getGlueElement() instanceof GlueSubClusterSoftwareRunTimeEnvironment)
      tag = ((GlueSubClusterSoftwareRunTimeEnvironment)getGlueElement()).GlueLocactionPath;
    
    return tag;
  }

  public void setScript( final URI script ) {
    this.script = script;
    
  }

  public String getHostName() {
    return null;
  }

  public URI getURI() {
    // TODO Auto-generated method stub
    return null;
  }

}
