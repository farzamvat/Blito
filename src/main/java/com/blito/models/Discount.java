package com.blito.models;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity(name="discount")
public class Discount {
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private long discountId;
	
	private String code;
	
	private int reusability;
	
	private int used;
	
	private Timestamp effectDate;
	
	private Timestamp expirationDate;
	
	private boolean isPercent;
	
	private double percent;
	
	private double amount;
	
	@ManyToOne
	@JoinColumn(name="userId")
	private User user;
	
	@ManyToMany(mappedBy="discounts")
	private List<BlitType> blitTypes;

	public long getDiscountId() {
		return discountId;
	}
	public void setDiscountId(long discountId) {
		this.discountId = discountId;
	}
	public int getUsed() {
		return used;
	}

	public void setUsed(int used) {
		this.used = used;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getReusability() {
		return reusability;
	}

	public void setReusability(int reusability) {
		this.reusability = reusability;
	}

	public Timestamp getEffectDate() {
		return effectDate;
	}

	public void setEffectDate(Timestamp effectDate) {
		this.effectDate = effectDate;
	}

	public Timestamp getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Timestamp expirationDate) {
		this.expirationDate = expirationDate;
	}

	public boolean isPercent() {
		return isPercent;
	}

	public void setPercent(boolean isPercent) {
		this.isPercent = isPercent;
	}

	public double getPercent() {
		return percent;
	}

	public void setPercent(double percent) {
		this.percent = percent;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<BlitType> getBlitTypes() {
		return blitTypes;
	}

	public void setBlitTypes(List<BlitType> blitTypes) {
		this.blitTypes = blitTypes;
		blitTypes.forEach(b -> b.getDiscounts().add(this));
	}
}
