package com.blito.models;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity(name="common_blit")
@PrimaryKeyJoinColumn(referencedColumnName="blitId")
public class CommonBlit extends Blit {
	@ManyToOne
	@JoinColumn(name="blitTypeId")
	private BlitType blitType;

	public BlitType getBlitType() {
		return blitType;
	}

	public void setBlitType(BlitType blitType) {
		this.blitType = blitType;
		blitType.getCommonBlits().add(this);
	}
}
