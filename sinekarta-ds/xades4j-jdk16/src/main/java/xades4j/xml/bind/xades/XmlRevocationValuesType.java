//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.04.09 at 09:56:29 PM BST 
//


package xades4j.xml.bind.xades;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for RevocationValuesType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RevocationValuesType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CRLValues" type="{http://uri.etsi.org/01903/v1.3.2#}CRLValuesType" minOccurs="0"/>
 *         &lt;element name="OCSPValues" type="{http://uri.etsi.org/01903/v1.3.2#}OCSPValuesType" minOccurs="0"/>
 *         &lt;element name="OtherValues" type="{http://uri.etsi.org/01903/v1.3.2#}OtherCertStatusValuesType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="Id" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RevocationValuesType", propOrder = {
    "crlValues",
    "ocspValues",
    "otherValues"
})
public class XmlRevocationValuesType {

    @XmlElement(name = "CRLValues")
    protected XmlCRLValuesType crlValues;
    @XmlElement(name = "OCSPValues")
    protected XmlOCSPValuesType ocspValues;
    @XmlElement(name = "OtherValues")
    protected XmlOtherCertStatusValuesType otherValues;
    @XmlAttribute(name = "Id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;

    /**
     * Gets the value of the crlValues property.
     * 
     * @return
     *     possible object is
     *     {@link XmlCRLValuesType }
     *     
     */
    public XmlCRLValuesType getCRLValues() {
        return crlValues;
    }

    /**
     * Sets the value of the crlValues property.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlCRLValuesType }
     *     
     */
    public void setCRLValues(XmlCRLValuesType value) {
        this.crlValues = value;
    }

    /**
     * Gets the value of the ocspValues property.
     * 
     * @return
     *     possible object is
     *     {@link XmlOCSPValuesType }
     *     
     */
    public XmlOCSPValuesType getOCSPValues() {
        return ocspValues;
    }

    /**
     * Sets the value of the ocspValues property.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlOCSPValuesType }
     *     
     */
    public void setOCSPValues(XmlOCSPValuesType value) {
        this.ocspValues = value;
    }

    /**
     * Gets the value of the otherValues property.
     * 
     * @return
     *     possible object is
     *     {@link XmlOtherCertStatusValuesType }
     *     
     */
    public XmlOtherCertStatusValuesType getOtherValues() {
        return otherValues;
    }

    /**
     * Sets the value of the otherValues property.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlOtherCertStatusValuesType }
     *     
     */
    public void setOtherValues(XmlOtherCertStatusValuesType value) {
        this.otherValues = value;
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

}