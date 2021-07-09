package edu.uga.cs.evote.persistence.impl;

import java.sql.ResultSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.*;
import edu.uga.cs.evote.object.ObjectLayer;

public class ElectionsOfficerIterator implements Iterator<User>{

	private ResultSet	rs = null;
	private ObjectLayer	objectLayer = null;
	private boolean		more = false;

	public ElectionsOfficerIterator( ResultSet rs, ObjectLayer objectLayer) throws EVException {
		this.rs = rs;
		this.objectLayer = objectLayer;
		try {
			more = rs.next();
		}
		catch( Exception e ) {
			throw new EVException( "ElectionOfficerIterator: Cannot create ElectionOfficerIterator; root cause: " + e );
		}
	}

	public boolean hasNext(){
		return more;
	}

	public ElectionsOfficer next(){

		long id;
		String firstName;
		String lastName;
		String userName;
		String password;
		String emailAddress;
		String Address;

		if( more ){

			try {
				id = rs.getLong( 1 );
				firstName= rs.getString( 2 );
				lastName = rs.getString(3);
				userName = rs.getString(4);
				password = rs.getString(5);
				emailAddress = rs.getString(6);
				Address = rs.getString(7);



				more = rs.next();
			} catch(Exception e) {
				throw new NoSuchElementException( "ElectionOfficerIterator: No next ElectionOfficert object; root cause: " + e );
			}

			ElectionsOfficer EO = null;
			try {
				EO = objectLayer.createElectionsOfficer(firstName, lastName, userName, password, emailAddress, Address);
				EO.setId( id );
			} catch (EVException e) {
				e.printStackTrace();
			}

			return EO;

		} else throw new NoSuchElementException( "ElectionsOfficerIterator: No next ElectionsOffice object" );

	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

}
