package com.jardoapps.usermodule.hbn.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "um_login_record")
public class LogInRecordEntity {

	@Id
	@GeneratedValue
	private long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	UserEntity user;

	@Column(name = "date_time")
	Date time;

	@Column(name = "ip", length = 45)
	private String ip;

	@Column(name = "successful")
	private boolean successful;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public boolean isSuccessful() {
		return successful;
	}

	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}

}
