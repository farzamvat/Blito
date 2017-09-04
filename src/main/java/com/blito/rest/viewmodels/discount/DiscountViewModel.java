package com.blito.rest.viewmodels.discount;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;
import java.util.Set;

public class DiscountViewModel {
	
	private Long discountId;
	
	@NotEmpty
	@Pattern(regexp ="(?=\\S+$).+" , message="{discount.code.pattern.validation}")
	private String code;
	@Min(1)
	private Integer reusability;
	private Integer used;
	@NotNull
	private Timestamp effectDate;
	@NotNull
	private Timestamp expirationDate;
	@NotNull
	private Boolean isPercent;
	@NotNull
	private Double percentage;
	@NotNull
	private Long amount;

	private Boolean isEnabled;

	@NotEmpty
	private Set<Long> blitTypeIds;

	@JsonProperty("isEnabled")
	public Boolean getEnabled() {
		return isEnabled;
	}

	public void setEnabled(Boolean enabled) {
		isEnabled = enabled;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getDiscountId() {
		return discountId;
	}

	public void setDiscountId(Long discountId) {
		this.discountId = discountId;
	}

	public Integer getReusability() {
		return reusability;
	}

	public void setReusability(Integer reusability) {
		this.reusability = reusability;
	}

	public Integer getUsed() {
		return used;
	}

	public void setUsed(Integer used) {
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
	public Boolean getPercent() {
		return isPercent;
	}

	public void setPercent(Boolean percent) {
		isPercent = percent;
	}

	public Double getPercentage() {
		return percentage;
	}

	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}



	public Set<Long> getBlitTypeIds() {
		return blitTypeIds;
	}

	public void setBlitTypeIds(Set<Long> blitTypeIds) {
		this.blitTypeIds = blitTypeIds;
	}
}
