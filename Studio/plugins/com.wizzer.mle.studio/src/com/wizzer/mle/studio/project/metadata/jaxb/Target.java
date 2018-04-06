//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.12.22 at 03:48:53 PM MST 
//


package com.wizzer.mle.studio.project.metadata.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Target meta data.
 * 
 * <p>Java class for target complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="target">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="verbose" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="digital_playprint" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="tools" type="{http://www.wizzerworks.com/MLE/MetaDataSchema}target_tools"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "target", propOrder = {
    "verbose",
    "type",
    "id",
    "digitalPlayprint",
    "tools"
})
public class Target {

    protected Boolean verbose;
    @XmlElement(required = true)
    protected String type;
    @XmlElement(required = true)
    protected String id;
    @XmlElement(name = "digital_playprint", required = true)
    protected String digitalPlayprint;
    @XmlElement(required = true)
    protected TargetTools tools;

    /**
     * Gets the value of the verbose property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isVerbose() {
        return verbose;
    }

    /**
     * Sets the value of the verbose property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setVerbose(Boolean value) {
        this.verbose = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the digitalPlayprint property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDigitalPlayprint() {
        return digitalPlayprint;
    }

    /**
     * Sets the value of the digitalPlayprint property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDigitalPlayprint(String value) {
        this.digitalPlayprint = value;
    }

    /**
     * Gets the value of the tools property.
     * 
     * @return
     *     possible object is
     *     {@link TargetTools }
     *     
     */
    public TargetTools getTools() {
        return tools;
    }

    /**
     * Sets the value of the tools property.
     * 
     * @param value
     *     allowed object is
     *     {@link TargetTools }
     *     
     */
    public void setTools(TargetTools value) {
        this.tools = value;
    }

}
