//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.06.19 at 09:22:36 PM IRDT 
//


package com.blito.payments.localhost._8085.services;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for request complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="request">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ResNum" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="RefNum" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="State" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="MID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="TraceNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "request", propOrder = {
    "resNum",
    "refNum",
    "state",
    "mid",
    "traceNo"
})
public class Request {

    @XmlElement(name = "ResNum", required = true)
    protected String resNum;
    @XmlElement(name = "RefNum", required = true)
    protected String refNum;
    @XmlElement(name = "State", required = true)
    protected String state;
    @XmlElement(name = "MID", required = true)
    protected String mid;
    @XmlElement(name = "TraceNo", required = true)
    protected String traceNo;

    /**
     * Gets the value of the resNum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResNum() {
        return resNum;
    }

    /**
     * Sets the value of the resNum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResNum(String value) {
        this.resNum = value;
    }

    /**
     * Gets the value of the refNum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRefNum() {
        return refNum;
    }

    /**
     * Sets the value of the refNum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRefNum(String value) {
        this.refNum = value;
    }

    /**
     * Gets the value of the state property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getState() {
        return state;
    }

    /**
     * Sets the value of the state property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setState(String value) {
        this.state = value;
    }

    /**
     * Gets the value of the mid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMID() {
        return mid;
    }

    /**
     * Sets the value of the mid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMID(String value) {
        this.mid = value;
    }

    /**
     * Gets the value of the traceNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTraceNo() {
        return traceNo;
    }

    /**
     * Sets the value of the traceNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTraceNo(String value) {
        this.traceNo = value;
    }

}
