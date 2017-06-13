package com.blito.payments.saman.webservice.client;

public class InitPaymentSoapProxy implements com.blito.payments.saman.webservice.client.InitPaymentSoap {
  private String _endpoint = null;
  private com.blito.payments.saman.webservice.client.InitPaymentSoap paymentIFBindingSoap = null;
  
  public InitPaymentSoapProxy() {
    _initPaymentIFBindingSoapProxy();
  }
  
  public InitPaymentSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initPaymentIFBindingSoapProxy();
  }
  
  private void _initPaymentIFBindingSoapProxy() {
    try {
      paymentIFBindingSoap = (new com.blito.payments.saman.webservice.client.InitPaymentLocator()).getPaymentIFBindingSoap();
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
  
  public com.blito.payments.saman.webservice.client.InitPaymentSoap getPaymentIFBindingSoap() {
    if (paymentIFBindingSoap == null)
      _initPaymentIFBindingSoapProxy();
    return paymentIFBindingSoap;
  }
  
  public java.lang.String requestToken(java.lang.String termID, java.lang.String resNum, long totalAmount, long segAmount1, long segAmount2, long segAmount3, long segAmount4, long segAmount5, long segAmount6, java.lang.String additionalData1, java.lang.String additionalData2, long wage) throws java.rmi.RemoteException{
    if (paymentIFBindingSoap == null)
      _initPaymentIFBindingSoapProxy();
    return paymentIFBindingSoap.requestToken(termID, resNum, totalAmount, segAmount1, segAmount2, segAmount3, segAmount4, segAmount5, segAmount6, additionalData1, additionalData2, wage);
  }
  
  public java.lang.String requestMultiSettleTypeToken(java.lang.String termID, java.lang.String resNum, java.lang.String amounts, long totalAmount, long segAmount1, long segAmount2, long segAmount3, long segAmount4, long segAmount5, long segAmount6, java.lang.String additionalData1, java.lang.String additionalData2, long wage) throws java.rmi.RemoteException{
    if (paymentIFBindingSoap == null)
      _initPaymentIFBindingSoapProxy();
    return paymentIFBindingSoap.requestMultiSettleTypeToken(termID, resNum, amounts, totalAmount, segAmount1, segAmount2, segAmount3, segAmount4, segAmount5, segAmount6, additionalData1, additionalData2, wage);
  }
  
  
}