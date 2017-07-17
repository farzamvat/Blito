/**
 * PaymentIFBindingSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.blito.payments.saman.webservice.client;

public interface ReferencePaymentSoap extends java.rmi.Remote {
    public double verifyTransaction(java.lang.String string_1, java.lang.String string_2) throws java.rmi.RemoteException;
    public double verifyTransaction1(java.lang.String string_1, java.lang.String string_2) throws java.rmi.RemoteException;
    public double reverseTransaction(java.lang.String string_1, java.lang.String string_2, java.lang.String username, java.lang.String password) throws java.rmi.RemoteException;
    public double reverseTransaction1(java.lang.String string_1, java.lang.String string_2, java.lang.String password, double amount) throws java.rmi.RemoteException;
}
