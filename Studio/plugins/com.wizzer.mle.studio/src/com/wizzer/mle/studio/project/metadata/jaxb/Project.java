//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.12.22 at 03:48:53 PM MST 
//


package com.wizzer.mle.studio.project.metadata.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="project_type">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="version" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="home_directory_uri" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *         &lt;element name="home_directory_path" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="asset_directory_uri" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="asset_directory_path" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;choice>
 *           &lt;element name="title_data" type="{http://www.wizzerworks.com/MLE/MetaDataSchema}title" maxOccurs="unbounded"/>
 *         &lt;/choice>
 *         &lt;element name="project_user_data" type="{http://www.wizzerworks.com/MLE/MetaDataSchema}user_data" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="major_version" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *             &lt;minInclusive value="0"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="minor_version" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *             &lt;minInclusive value="1"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="date" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "projectType",
    "homeDirectoryUri",
    "homeDirectoryPath",
    "assetDirectoryUri",
    "assetDirectoryPath",
    "titleData",
    "projectUserData"
})
@XmlRootElement(name = "project")
public class Project {

    @XmlElement(name = "project_type", required = true)
    protected Project.ProjectType projectType;
    @XmlElement(name = "home_directory_uri")
    @XmlSchemaType(name = "anyURI")
    protected String homeDirectoryUri;
    @XmlElement(name = "home_directory_path")
    protected String homeDirectoryPath;
    @XmlElement(name = "asset_directory_uri")
    protected String assetDirectoryUri;
    @XmlElement(name = "asset_directory_path")
    protected String assetDirectoryPath;
    @XmlElement(name = "title_data")
    protected List<Title> titleData;
    @XmlElement(name = "project_user_data")
    protected UserData projectUserData;
    @XmlAttribute(name = "major_version", required = true)
    protected int majorVersion;
    @XmlAttribute(name = "minor_version", required = true)
    protected int minorVersion;
    @XmlAttribute
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar date;

    /**
     * Gets the value of the projectType property.
     * 
     * @return
     *     possible object is
     *     {@link Project.ProjectType }
     *     
     */
    public Project.ProjectType getProjectType() {
        return projectType;
    }

    /**
     * Sets the value of the projectType property.
     * 
     * @param value
     *     allowed object is
     *     {@link Project.ProjectType }
     *     
     */
    public void setProjectType(Project.ProjectType value) {
        this.projectType = value;
    }

    /**
     * Gets the value of the homeDirectoryUri property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHomeDirectoryUri() {
        return homeDirectoryUri;
    }

    /**
     * Sets the value of the homeDirectoryUri property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHomeDirectoryUri(String value) {
        this.homeDirectoryUri = value;
    }

    /**
     * Gets the value of the homeDirectoryPath property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHomeDirectoryPath() {
        return homeDirectoryPath;
    }

    /**
     * Sets the value of the homeDirectoryPath property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHomeDirectoryPath(String value) {
        this.homeDirectoryPath = value;
    }

    /**
     * Gets the value of the assetDirectoryUri property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAssetDirectoryUri() {
        return assetDirectoryUri;
    }

    /**
     * Sets the value of the assetDirectoryUri property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAssetDirectoryUri(String value) {
        this.assetDirectoryUri = value;
    }

    /**
     * Gets the value of the assetDirectoryPath property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAssetDirectoryPath() {
        return assetDirectoryPath;
    }

    /**
     * Sets the value of the assetDirectoryPath property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAssetDirectoryPath(String value) {
        this.assetDirectoryPath = value;
    }

    /**
     * Gets the value of the titleData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the titleData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTitleData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Title }
     * 
     * 
     */
    public List<Title> getTitleData() {
        if (titleData == null) {
            titleData = new ArrayList<Title>();
        }
        return this.titleData;
    }

    /**
     * Gets the value of the projectUserData property.
     * 
     * @return
     *     possible object is
     *     {@link UserData }
     *     
     */
    public UserData getProjectUserData() {
        return projectUserData;
    }

    /**
     * Sets the value of the projectUserData property.
     * 
     * @param value
     *     allowed object is
     *     {@link UserData }
     *     
     */
    public void setProjectUserData(UserData value) {
        this.projectUserData = value;
    }

    /**
     * Gets the value of the majorVersion property.
     * 
     */
    public int getMajorVersion() {
        return majorVersion;
    }

    /**
     * Sets the value of the majorVersion property.
     * 
     */
    public void setMajorVersion(int value) {
        this.majorVersion = value;
    }

    /**
     * Gets the value of the minorVersion property.
     * 
     */
    public int getMinorVersion() {
        return minorVersion;
    }

    /**
     * Sets the value of the minorVersion property.
     * 
     */
    public void setMinorVersion(int value) {
        this.minorVersion = value;
    }

    /**
     * Gets the value of the date property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDate() {
        return date;
    }

    /**
     * Sets the value of the date property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDate(XMLGregorianCalendar value) {
        this.date = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
     *       &lt;attribute name="version" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "value"
    })
    public static class ProjectType {

        @XmlValue
        protected String value;
        @XmlAttribute
        protected String version;

        /**
         * Gets the value of the value property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setValue(String value) {
            this.value = value;
        }

        /**
         * Gets the value of the version property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getVersion() {
            return version;
        }

        /**
         * Sets the value of the version property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setVersion(String value) {
            this.version = value;
        }

    }

}
