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
import javax.xml.bind.annotation.XmlType;


/**
 * Collection of Magic Lantern tools.
 * 
 * <p>Java class for target_tools complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="target_tools">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;group ref="{http://www.wizzerworks.com/MLE/MetaDataSchema}tool_data"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "target_tools", propOrder = {
    "destinationDirectory",
    "codeGeneration",
    "headerPackage",
    "bigEndian",
    "fixedPoint",
    "tags",
    "gengroup",
    "genmedia",
    "genscene",
    "gentables",
    "genppscript",
    "gendpp"
})
public class TargetTools {

    @XmlElement(name = "destination_directory", required = true)
    protected String destinationDirectory;
    @XmlElement(name = "code_generation", required = true)
    protected String codeGeneration;
    @XmlElement(name = "header_package", required = true)
    protected String headerPackage;
    @XmlElement(name = "big_endian")
    protected Boolean bigEndian;
    @XmlElement(name = "fixed_point")
    protected Boolean fixedPoint;
    protected List<String> tags;
    protected TargetTools.Gengroup gengroup;
    protected TargetTools.Genmedia genmedia;
    protected TargetTools.Genscene genscene;
    protected Object gentables;
    protected TargetTools.Genppscript genppscript;
    protected TargetTools.Gendpp gendpp;

    /**
     * Gets the value of the destinationDirectory property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDestinationDirectory() {
        return destinationDirectory;
    }

    /**
     * Sets the value of the destinationDirectory property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDestinationDirectory(String value) {
        this.destinationDirectory = value;
    }

    /**
     * Gets the value of the codeGeneration property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeGeneration() {
        return codeGeneration;
    }

    /**
     * Sets the value of the codeGeneration property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeGeneration(String value) {
        this.codeGeneration = value;
    }

    /**
     * Gets the value of the headerPackage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHeaderPackage() {
        return headerPackage;
    }

    /**
     * Sets the value of the headerPackage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHeaderPackage(String value) {
        this.headerPackage = value;
    }

    /**
     * Gets the value of the bigEndian property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isBigEndian() {
        return bigEndian;
    }

    /**
     * Sets the value of the bigEndian property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setBigEndian(Boolean value) {
        this.bigEndian = value;
    }

    /**
     * Gets the value of the fixedPoint property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isFixedPoint() {
        return fixedPoint;
    }

    /**
     * Sets the value of the fixedPoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setFixedPoint(Boolean value) {
        this.fixedPoint = value;
    }

    /**
     * Gets the value of the tags property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the tags property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTags().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getTags() {
        if (tags == null) {
            tags = new ArrayList<String>();
        }
        return this.tags;
    }

    /**
     * Gets the value of the gengroup property.
     * 
     * @return
     *     possible object is
     *     {@link TargetTools.Gengroup }
     *     
     */
    public TargetTools.Gengroup getGengroup() {
        return gengroup;
    }

    /**
     * Sets the value of the gengroup property.
     * 
     * @param value
     *     allowed object is
     *     {@link TargetTools.Gengroup }
     *     
     */
    public void setGengroup(TargetTools.Gengroup value) {
        this.gengroup = value;
    }

    /**
     * Gets the value of the genmedia property.
     * 
     * @return
     *     possible object is
     *     {@link TargetTools.Genmedia }
     *     
     */
    public TargetTools.Genmedia getGenmedia() {
        return genmedia;
    }

    /**
     * Sets the value of the genmedia property.
     * 
     * @param value
     *     allowed object is
     *     {@link TargetTools.Genmedia }
     *     
     */
    public void setGenmedia(TargetTools.Genmedia value) {
        this.genmedia = value;
    }

    /**
     * Gets the value of the genscene property.
     * 
     * @return
     *     possible object is
     *     {@link TargetTools.Genscene }
     *     
     */
    public TargetTools.Genscene getGenscene() {
        return genscene;
    }

    /**
     * Sets the value of the genscene property.
     * 
     * @param value
     *     allowed object is
     *     {@link TargetTools.Genscene }
     *     
     */
    public void setGenscene(TargetTools.Genscene value) {
        this.genscene = value;
    }

    /**
     * Gets the value of the gentables property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getGentables() {
        return gentables;
    }

    /**
     * Sets the value of the gentables property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setGentables(Object value) {
        this.gentables = value;
    }

    /**
     * Gets the value of the genppscript property.
     * 
     * @return
     *     possible object is
     *     {@link TargetTools.Genppscript }
     *     
     */
    public TargetTools.Genppscript getGenppscript() {
        return genppscript;
    }

    /**
     * Sets the value of the genppscript property.
     * 
     * @param value
     *     allowed object is
     *     {@link TargetTools.Genppscript }
     *     
     */
    public void setGenppscript(TargetTools.Genppscript value) {
        this.genppscript = value;
    }

    /**
     * Gets the value of the gendpp property.
     * 
     * @return
     *     possible object is
     *     {@link TargetTools.Gendpp }
     *     
     */
    public TargetTools.Gendpp getGendpp() {
        return gendpp;
    }

    /**
     * Sets the value of the gendpp property.
     * 
     * @param value
     *     allowed object is
     *     {@link TargetTools.Gendpp }
     *     
     */
    public void setGendpp(TargetTools.Gendpp value) {
        this.gendpp = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="source_directory" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="script_path" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Gendpp {

        @XmlAttribute(name = "source_directory", required = true)
        protected String sourceDirectory;
        @XmlAttribute(name = "script_path", required = true)
        protected String scriptPath;

        /**
         * Gets the value of the sourceDirectory property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSourceDirectory() {
            return sourceDirectory;
        }

        /**
         * Sets the value of the sourceDirectory property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSourceDirectory(String value) {
            this.sourceDirectory = value;
        }

        /**
         * Gets the value of the scriptPath property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getScriptPath() {
            return scriptPath;
        }

        /**
         * Sets the value of the scriptPath property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setScriptPath(String value) {
            this.scriptPath = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="actor_id_file" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="group_id_file" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Gengroup {

        @XmlAttribute(name = "actor_id_file", required = true)
        protected String actorIdFile;
        @XmlAttribute(name = "group_id_file", required = true)
        protected String groupIdFile;

        /**
         * Gets the value of the actorIdFile property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getActorIdFile() {
            return actorIdFile;
        }

        /**
         * Sets the value of the actorIdFile property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setActorIdFile(String value) {
            this.actorIdFile = value;
        }

        /**
         * Gets the value of the groupIdFile property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getGroupIdFile() {
            return groupIdFile;
        }

        /**
         * Sets the value of the groupIdFile property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setGroupIdFile(String value) {
            this.groupIdFile = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="bom_file" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Genmedia {

        @XmlAttribute(name = "bom_file", required = true)
        protected String bomFile;

        /**
         * Gets the value of the bomFile property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getBomFile() {
            return bomFile;
        }

        /**
         * Sets the value of the bomFile property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setBomFile(String value) {
            this.bomFile = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="script_file" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="toc_name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Genppscript {

        @XmlAttribute(name = "script_file", required = true)
        protected String scriptFile;
        @XmlAttribute(name = "toc_name", required = true)
        protected String tocName;

        /**
         * Gets the value of the scriptFile property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getScriptFile() {
            return scriptFile;
        }

        /**
         * Sets the value of the scriptFile property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setScriptFile(String value) {
            this.scriptFile = value;
        }

        /**
         * Gets the value of the tocName property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTocName() {
            return tocName;
        }

        /**
         * Sets the value of the tocName property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTocName(String value) {
            this.tocName = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="scene_id_file" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Genscene {

        @XmlAttribute(name = "scene_id_file", required = true)
        protected String sceneIdFile;

        /**
         * Gets the value of the sceneIdFile property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSceneIdFile() {
            return sceneIdFile;
        }

        /**
         * Sets the value of the sceneIdFile property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSceneIdFile(String value) {
            this.sceneIdFile = value;
        }

    }

}
