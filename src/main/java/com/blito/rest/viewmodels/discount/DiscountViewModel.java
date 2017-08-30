package com.blito.rest.viewmodels.discount;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Set;

public class DiscountViewModel {
	
	private long discountId;
	
	@NotEmpty
	private String code;
	@Min(1)
	private int reusability;
	private int used;
	@NotNull
	private Timestamp effectDate;
	@NotNull
	private Timestamp expirationDate;
	@NotNull
	private boolean isPercent;
	
	private double percentage;
	
	private double amount;

	@NotEmpty
	private Set<Long> blitTypeIds;

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
	@JsonProperty("isPercent")
	public boolean isPercent() {
		return isPercent;
	}

	public void setPercent(boolean percent) {
		isPercent = percent;
	}

	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Set<Long> getBlitTypeIds() {
		return blitTypeIds;
	}

	public void setBlitTypeIds(Set<Long> blitTypeIds) {
		this.blitTypeIds = blitTypeIds;
	}
}
