package edu.uga.cs.evote.persistence.impl;

import java.sql.ResultSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.Election;
import edu.uga.cs.evote.object.ObjectLayer;

public class ElectionIterator implements Iterator<Election>{

	private ResultSet	rs = null;
	private ObjectLayer	objectLayer = null;
	private boolean		more = false;

	public ElectionIterator( ResultSet rs, ObjectLayer objectLayer) throws EVException{
		this.rs = rs;
		this.objectLayer = objectLayer;
		try {
			more = rs.next();
		} catch (Exception e) {	// just in case...
			throw new EVException( "ElectionIterator: Cannot create Election iterator; root cause: " + e );
		}
	}

	public boolean hasNext() {
		return more;
	}

	public Election next() {

		long id;
		String office;
		boolean isPartisan;
		Ballot ballot = null;

		if (more) {

			try {
				id = rs.getLong( 1 );
				office = rs.getString( 2 );
				isPartisan = rs.getBoolean( 3 );
				//ballot = null;

				more = rs.next();
			} catch (Exception e) {
				throw new NoSuchElementException( "ElectionIterator: Cannot create Election object; root cause: " + e);
			}

			Election election = null;
			try {
				election = objectLayer.createElection(office, isPartisan, ballot);
				election.setId( id );
			} catch (EVException e) {
				e.printStackTrace();
			}

			return election;
		} else throw new NoSuchElementException( "ElectionIterator: No next person object" );
	}

	public void remove(){
		throw new UnsupportedOperationException();
	}

}
