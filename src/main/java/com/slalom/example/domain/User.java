package com.slalom.example.domain;

import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@NamedNativeQuery(name="User.findBySearchTermNamedNative", query="select * from user u where user_status = 0", resultClass=User.class)
@NamedQuery(name="User.findBySearchTermNamed",
		query = "select u from User u where " +
				"lower(u.username) like lower(concat('%',:searchTerm,'%'))")
@Table(name="user", uniqueConstraints={@UniqueConstraint(columnNames={"username"})})
public class User {

  	private Long id;

  	private String username;

	private String firstName;

	private String lastName;

	private String email;

	private String password;

	private String phone;

	private int userStatus;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotNull @Size(min=6, max=100)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Size(max=100) @Pattern(regexp = "^[a-zA-Z0-9_ ]*$", message="Invalid First Name")
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Size(max=100) @Pattern(regexp = "^[a-zA-Z0-9_ ]*$", message="Invalid Last Name")
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Email
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@NotNull @Size(min=6, max=100)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Size(max=100)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Min(value=0) @Max(value=10)
	public int getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(int userStatus) {
		this.userStatus = userStatus;
	}
}
