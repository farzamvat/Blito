package com.blito.rest.viewmodels;

public class View {
	public interface DefaultView {}
	public interface SimpleUser extends DefaultView {}
	public interface User extends SimpleUser {}
	public interface AdminUser extends User {}
	
	public interface SimpleExchangeBlit extends DefaultView {}
	public interface ExchangeBlit extends SimpleExchangeBlit {}
	
	public interface SimpleEvent extends DefaultView {}
	public interface Event extends SimpleEvent {}
	public interface AdminEvent extends Event {}
	
	public interface SimpleEventHost extends DefaultView {}
	public interface EventHost extends SimpleEventHost {}
	
	public interface IndexBanner extends DefaultView {}
	public interface AdminIndexBanner extends IndexBanner {}
}
