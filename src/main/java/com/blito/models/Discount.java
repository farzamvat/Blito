package com.blito.models;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity(name="discount")
public class Discount {
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private Long discountId;
	
	private String code;
	
	private Integer reusability;
	
	private Integer used;
	
	private Timestamp effectDate;
	
	private Timestamp expirationDate;
	
	private Boolean isPercent;
	
	private Double percentage;
	
	private Long amount;
	
	@ManyToOne
	@JoinColumn(name="userId")
	private User user;
	
	@ManyToMany(mappedBy="discounts")
	private Set<BlitType> blitTypes;

	private Boolean isEnabled = true;
	
	public Discount()
	{
		blitTypes = new HashSet<>();
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

	public Boolean getEnabled() {
		return isEnabled;
	}

	public void setEnabled(Boolean enabled) {
		isEnabled = enabled;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<BlitType> getBlitTypes() {
		return blitTypes;
	}

	public void setBlitTypes(Set<BlitType> blitTypes) {
		this.blitTypes = blitTypes;
		blitTypes.forEach(b -> b.getDiscounts().add(this));
	}
}
