package com.blito.rest.viewmodels.discount;

import java.sql.Timestamp;
import java.util.List;

import javax.validation.constraints.NotNull;

public class DiscountViewModel {
	
	long discountId;
	
	@NotNull
	String code;
	@NotNull
	int reusability;
	
	int used;
	@NotNull
	Timestamp effectDate;
	@NotNull
	Timestamp expirationDate;
	@NotNull
	boolean isPercent;
	
	double percent;
	
	double amount;
	
	long userId;
	@NotNull
	List<Long> blitTypeIds;
	
	public long getDiscountId() {
		return discountId;
	}
	public void setDiscountId(long discountId) {
		this.discountId = discountId;
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
	public int getUsed() {
		return used;
	}
	public void setUsed(int used) {
		this.used = used;
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
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public List<Long> getBlitTypeIds() {
		return blitTypeIds;
	}
	public void setBlitTypeIds(List<Long> blitTypeIds) {
		this.blitTypeIds = blitTypeIds;
	}
}
