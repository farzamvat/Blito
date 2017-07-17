package com.blito.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity(name="artist")
@PrimaryKeyJoinColumn(referencedColumnName="profileId")
public class Artist extends Profile {
	@Column(columnDefinition="TEXT")
	String groupMembers;

	public String getGroupMembers() {
		return groupMembers;
	}

	public void setGroupMembers(String groupMembers) {
		this.groupMembers = groupMembers;
	}
}
