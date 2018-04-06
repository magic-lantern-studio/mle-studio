//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.12.22 at 03:48:53 PM MST 
//


package com.wizzer.mle.studio.project.metadata.jaxb;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.wizzer.mle.studio.project.metadata.jaxb package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _UserDataId_QNAME = new QName("http://www.wizzerworks.com/MLE/MetaDataSchema", "id");
    private final static QName _UserDataAuthor_QNAME = new QName("http://www.wizzerworks.com/MLE/MetaDataSchema", "author");
    private final static QName _UserDataName_QNAME = new QName("http://www.wizzerworks.com/MLE/MetaDataSchema", "name");
    private final static QName _UserDataData_QNAME = new QName("http://www.wizzerworks.com/MLE/MetaDataSchema", "data");
    private final static QName _UserDataContact_QNAME = new QName("http://www.wizzerworks.com/MLE/MetaDataSchema", "contact");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.wizzer.mle.studio.project.metadata.jaxb
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link TargetTools.Gengroup }
     * 
     */
    public TargetTools.Gengroup createTargetToolsGengroup() {
        return new TargetTools.Gengroup();
    }

    /**
     * Create an instance of {@link TargetTools.Genmedia }
     * 
     */
    public TargetTools.Genmedia createTargetToolsGenmedia() {
        return new TargetTools.Genmedia();
    }

    /**
     * Create an instance of {@link Project.ProjectType }
     * 
     */
    public Project.ProjectType createProjectProjectType() {
        return new Project.ProjectType();
    }

    /**
     * Create an instance of {@link Target }
     * 
     */
    public Target createTarget() {
        return new Target();
    }

    /**
     * Create an instance of {@link TargetTools.Genscene }
     * 
     */
    public TargetTools.Genscene createTargetToolsGenscene() {
        return new TargetTools.Genscene();
    }

    /**
     * Create an instance of {@link Title }
     * 
     */
    public Title createTitle() {
        return new Title();
    }

    /**
     * Create an instance of {@link Project }
     * 
     */
    public Project createProject() {
        return new Project();
    }

    /**
     * Create an instance of {@link TargetTools.Gendpp }
     * 
     */
    public TargetTools.Gendpp createTargetToolsGendpp() {
        return new TargetTools.Gendpp();
    }

    /**
     * Create an instance of {@link TargetTools.Genppscript }
     * 
     */
    public TargetTools.Genppscript createTargetToolsGenppscript() {
        return new TargetTools.Genppscript();
    }

    /**
     * Create an instance of {@link TargetTools }
     * 
     */
    public TargetTools createTargetTools() {
        return new TargetTools();
    }

    /**
     * Create an instance of {@link UserData }
     * 
     */
    public UserData createUserData() {
        return new UserData();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.wizzerworks.com/MLE/MetaDataSchema", name = "id", scope = UserData.class)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    public JAXBElement<String> createUserDataId(String value) {
        return new JAXBElement<String>(_UserDataId_QNAME, String.class, UserData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.wizzerworks.com/MLE/MetaDataSchema", name = "author", scope = UserData.class)
    public JAXBElement<String> createUserDataAuthor(String value) {
        return new JAXBElement<String>(_UserDataAuthor_QNAME, String.class, UserData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.wizzerworks.com/MLE/MetaDataSchema", name = "name", scope = UserData.class)
    public JAXBElement<String> createUserDataName(String value) {
        return new JAXBElement<String>(_UserDataName_QNAME, String.class, UserData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.wizzerworks.com/MLE/MetaDataSchema", name = "data", scope = UserData.class)
    public JAXBElement<Object> createUserDataData(Object value) {
        return new JAXBElement<Object>(_UserDataData_QNAME, Object.class, UserData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.wizzerworks.com/MLE/MetaDataSchema", name = "contact", scope = UserData.class)
    public JAXBElement<String> createUserDataContact(String value) {
        return new JAXBElement<String>(_UserDataContact_QNAME, String.class, UserData.class, value);
    }

}
