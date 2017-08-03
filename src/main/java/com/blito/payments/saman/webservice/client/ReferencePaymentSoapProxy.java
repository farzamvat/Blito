package com.blito.payments.saman.webservice.client;

public class ReferencePaymentSoapProxy implements com.blito.payments.saman.webservice.client.ReferencePaymentSoap {
  private String _endpoint = null;
  private com.blito.payments.saman.webservice.client.ReferencePaymentSoap paymentIFBindingSoap = null;
  
  public ReferencePaymentSoapProxy() {
    _initPaymentIFBindingSoapProxy();
  }
  
  public ReferencePaymentSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initPaymentIFBindingSoapProxy();
  }
  
  private void _initPaymentIFBindingSoapProxy() {
    try {
      paymentIFBindingSoap = (new com.blito.payments.saman.webservice.client.ReferencePaymentLocator()).getPaymentIFBindingSoap();
      if (paymentIFBindingSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)paymentIFBindingSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)paymentIFBindingSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (paymentIFBindingSoap != null)
      ((javax.xml.rpc.Stub)paymentIFBindingSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.blito.payments.saman.webservice.client.ReferencePaymentSoap getPaymentIFBindingSoap() {
    if (paymentIFBindingSoap == null)
      _initPaymentIFBindingSoapProxy();
    return paymentIFBindingSoap;
  }
  
  public double verifyTransaction(java.lang.String string_1, java.lang.String string_2) throws java.rmi.RemoteException{
    if (paymentIFBindingSoap == null)
      _initPaymentIFBindingSoapProxy();
    return paymentIFBindingSoap.verifyTransaction(string_1, string_2);
  }
  
  public double verifyTransaction1(java.lang.String string_1, java.lang.String string_2) throws java.rmi.RemoteException{
    if (paymentIFBindingSoap == null)
      _initPaymentIFBindingSoapProxy();
    return paymentIFBindingSoap.verifyTransaction1(string_1, string_2);
  }
  
  public double reverseTransaction(java.lang.String string_1, java.lang.String string_2, java.lang.String username, java.lang.String password) throws java.rmi.RemoteException{
    if (paymentIFBindingSoap == null)
      _initPaymentIFBindingSoapProxy();
    return paymentIFBindingSoap.reverseTransaction(string_1, string_2, username, password);
  }
  
  public double reverseTransaction1(java.lang.String string_1, java.lang.String string_2, java.lang.String password, double amount) throws java.rmi.RemoteException{
    if (paymentIFBindingSoap == null)
      _initPaymentIFBindingSoapProxy();
    return paymentIFBindingSoap.reverseTransaction1(string_1, string_2, password, amount);
  }
  
  
}