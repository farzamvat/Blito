package com.blito.rest.viewmodels;

public class View {
	public interface DefaultView {}
	public interface SimpleUser extends DefaultView {}
	public interface User extends SimpleUser {}
	public interface AdminUser extends User {}
	
	public interface SimpleExchangeBlit extends DefaultView {}
	public interface ExchangeBlit extends SimpleExchangeBlit {}
	public interface AdminExchangeBlit extends ExchangeBlit {}
	
	public interface SimpleEvent extends DefaultView,EventDateFlat {}
	public interface Event extends SimpleEvent,BlitType,EventDateFlat {}
	public interface AdminEvent extends Event {}
	
	public interface EventDateFlat extends DefaultView {}
	
	public interface BlitType extends DefaultView {}

	public interface SimpleEventHost extends DefaultView {}
	public interface EventHost extends SimpleEventHost {}
	public interface OwnerEventHost extends EventHost {}
	public interface AdminEventHost extends OwnerEventHost {}
	
	public interface IndexBanner extends DefaultView {}
	public interface AdminIndexBanner extends IndexBanner {}
	
	public interface SimpleBlit extends DefaultView {}
	public interface CommonBlit extends SimpleBlit {}
	public interface SeatBlit extends SimpleBlit {}
	public interface Blit extends SeatBlit,CommonBlit {}
	public interface AdminBlit extends Blit {}

	public interface SimpleSalon extends DefaultView {}
	public interface SalonSchema extends SimpleSalon {}
	public interface IncludingCustomerNameSalonSchema extends SalonSchema {}

}
