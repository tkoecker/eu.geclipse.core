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
 *     PSNC - Katarzyna Bylec
 *           
 *****************************************************************************/

package eu.geclipse.ui.wizards.jobs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.w3c.dom.Element;
import eu.geclipse.ui.internal.wizards.jobs.ApplicationSpecificControlsFactory;
import eu.geclipse.ui.wizards.jobs.wizardnodes.SpecificWizardPart;

/**
 * Wizard page used in {@link SpecificWizardPart} (but not as a last page)
 * 
 * @see ApplicationSpecificLastPage
 */
public class ApplicationSpecificPage extends WizardPage
  implements IApplicationSpecificPage
{

  private Composite parent;
  private ArrayList<Text> textFieldsWithFileChooser = new ArrayList<Text>();
  // holds map with controls as a keys and parameter name as a value
  private HashMap<Control, String> controlsParametersNames = new HashMap<Control, String>();
  private Element pageElement;

  /**
   * Creates new instance of {@link ApplicationSpecificLastPage}
   * 
   * @param pageName name of the new page
   * @param element page element form xml file that describes this page
   */
  public ApplicationSpecificPage( final String pageName, final Element element )
  {
    super( pageName );
    this.pageElement = element;
  }

  public void createControl( final Composite parentP ) {
    Composite mainComp = new Composite( parentP, SWT.NONE );
    GridLayout gLayout = new GridLayout( 3, false );
    mainComp.setLayout( gLayout );
    ApplicationSpecificControlsFactory factory = new ApplicationSpecificControlsFactory();
    factory.createControls( this.pageElement,
                            mainComp,
                            this.textFieldsWithFileChooser,
                            this.controlsParametersNames );
    this.parent = mainComp;
    setControl( mainComp );
    setPageComplete( true );
  }

  Composite getParent() {
    return this.parent;
  }

  public Map<String, ArrayList<String>> getParametersValues() {
    Map<String, ArrayList<String>> result = null;
    if( this.controlsParametersNames != null
        && !this.controlsParametersNames.isEmpty() )
    {
      result = new HashMap<String, ArrayList<String>>();
      for( Control control : this.controlsParametersNames.keySet() ) {
        String controlText = null;
        if( control instanceof Text ) {
          controlText = ( ( Text )control ).getText();
        } else if( control instanceof Combo ) {
          controlText = ( ( Combo )control ).getText();
        }
        if( !result.containsKey( this.controlsParametersNames.get( control ) ) )
        {
          ArrayList<String> values = new ArrayList<String>();
          values.add( controlText );
          result.put( this.controlsParametersNames.get( control ), values );
        } else {
          result.get( this.controlsParametersNames.get( control ) )
            .add( controlText );
        }
      }
    }
    return result;
  }
}
