/**
 * @file ElectionsOfficer.java
 * @author Team 9: Joey Bruce. Jack Fisher, Kianoosh Mokhtari, Will Runge
 */

package edu.uga.cs.evote.entity.impl;

import edu.uga.cs.evote.entity.ElectionsOfficer;
//import edu.uga.cs.evote.entity.impl.UserImpl;

/**
 * This class represents an Administrator user. It has no additional attributes;
 * only the ones inherited from User.
 */
public class ElectionsOfficerImpl extends UserImpl implements ElectionsOfficer {

	// Default constructor
	public ElectionsOfficerImpl() {
		super();
	}

	// Constructor with all fields specified
	public ElectionsOfficerImpl(
			String firstName, String lastName, String username,
			String password, String email, String address) {
		super(firstName, lastName, username, password, email, address);
	}

	public String toString() {
		return "ElectionsOfficer[" + this.getId() + "] " + this.getFirstName()
				+ " " + this.getLastName() + " " + this.getUserName() + " "
				+ this.getPassword() + " " + this.getEmailAddress() + " "
				+ this.getAddress();
	}

}
