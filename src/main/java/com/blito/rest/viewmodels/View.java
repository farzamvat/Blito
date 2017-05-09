package com.blito.rest.viewmodels;

public class View {
	public interface SimpleUser {}
	public interface User extends SimpleUser {}
	public interface AdminUser extends User {}
	
	public interface SimpleExchangeBlit {}
	public interface ExchangeBlit extends SimpleExchangeBlit {}
	
	public interface SimpleEvent {}
	public interface Event extends SimpleEvent {}
	public interface AdminEvent extends Event {}
}
