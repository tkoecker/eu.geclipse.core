/**
 * <copyright>
 * </copyright>
 *
 * $Id: PosixXMLProcessor.java,v 1.2 2007/03/01 09:15:18 emstamou Exp $
 */
package eu.geclipse.jsdl.model.posix.util;

import eu.geclipse.jsdl.model.posix.PosixPackage;

import java.util.Map;

import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.xmi.util.XMLProcessor;

/**
 * This class contains helper methods to serialize and deserialize XML documents
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class PosixXMLProcessor extends XMLProcessor 
{
  /**
   * Public constructor to instantiate the helper.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public PosixXMLProcessor()
  {
    super((EPackage.Registry.INSTANCE));
    PosixPackage.eINSTANCE.eClass();
  }
  
  /**
   * Register for "*" and "xml" file extensions the PosixResourceFactoryImpl factory.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	protected Map getRegistrations()
  {
    if (registrations == null)
    {
      super.getRegistrations();
      registrations.put(XML_EXTENSION, new PosixResourceFactoryImpl());
      registrations.put(STAR_EXTENSION, new PosixResourceFactoryImpl());
    }
    return registrations;
  }

} //PosixXMLProcessor