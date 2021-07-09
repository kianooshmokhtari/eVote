/**
 * @file UserImpl.java
 * @author Team 9: Joey Bruce. Jack Fisher, Kianoosh Mokhtari, Will Runge
 */

package edu.uga.cs.evote.entity.impl;

import edu.uga.cs.evote.persistence.impl.Persistent;
import edu.uga.cs.evote.entity.User;

/**
 * This is the base class of all registered users of the eVote system. Each user
 * has a name, user name (i.e., a login name), password, and an email address.
 */
public abstract class UserImpl extends Persistent implements User {

	// Class attributes
	private String firstName;
	private String lastName;
	private String username;
	private String password;
	private String email;
	private String address;

	// Default constructor
	public UserImpl() {
		super(-1);
		this.firstName	= null;
		this.lastName	= null;
		this.username	= null;
		this.password	= null;
		this.email		= null;
		this.address	= null;
	}

	// Constructor with all fields specified
	public UserImpl(String firstName, String lastName, String username,
					String password, String email, String address) {
		super(-1);
		this.firstName	= firstName;
		this.lastName	= lastName;
		this.username	= username;
		this.password	= password;
		this.email		= email;
		this.address	= address;
	}

	/** Return the user's first name.
	 * @return the user's first name
	 */
	public String getFirstName() {
		return this.firstName;
	}

	/** Set the user's first name.
	 * @param firstName the new first name of this user
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/** Return the user's last name.
	 * @return the user's last name
	 */
	public String getLastName() {
		return this.lastName;
	}

	/** Set the user's last name.
	 * @param lastName the new last name of this user
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/** Return the user's user name (login name).
	 * @return the user's user name (login name)
	 */
	public String getUserName() {
		return this.username;
	}

	/** Set the user's user name (login name).
	 * @param userName the new user (login name)
	 */
	public void setUserName(String userName) {
		this.username = userName;
	}

	/** Return the user's password.
	 * @return the user's password
	 */
	public String getPassword() {
		return this.password;
	}

	/** Set the user's password.
	 * @param password the new password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/** Return the user's email address.
	 * @return the user's email address
	 */
	public String getEmailAddress() {
		return this.email;
	}

	/** Set the user's email address.
	 * @param emailAddress the new email address
	 */
	public void setEmailAddress(String emailAddress) {
		this.email = emailAddress;
	}

	/** Return the user's address.
	 * @return the user's address
	 */
	public String getAddress() {
		return this.address;
	}

	/** Set the user's address.
	 * @param address the new address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

}
