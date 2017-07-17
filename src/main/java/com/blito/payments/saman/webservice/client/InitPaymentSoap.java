/**
 * PaymentIFBindingSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.blito.payments.saman.webservice.client;

public interface InitPaymentSoap extends java.rmi.Remote {
    public java.lang.String requestToken(java.lang.String termID, java.lang.String resNum, long totalAmount, long segAmount1, long segAmount2, long segAmount3, long segAmount4, long segAmount5, long segAmount6, java.lang.String additionalData1, java.lang.String additionalData2, long wage) throws java.rmi.RemoteException;
    public java.lang.String requestMultiSettleTypeToken(java.lang.String termID, java.lang.String resNum, java.lang.String amounts, long totalAmount, long segAmount1, long segAmount2, long segAmount3, long segAmount4, long segAmount5, long segAmount6, java.lang.String additionalData1, java.lang.String additionalData2, long wage) throws java.rmi.RemoteException;
}
