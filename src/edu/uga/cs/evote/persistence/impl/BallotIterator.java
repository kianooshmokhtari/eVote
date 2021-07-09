package edu.uga.cs.evote.persistence.impl;

import java.sql.ResultSet;
import java.util.Date;
import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.ElectoralDistrict;
import edu.uga.cs.evote.object.ObjectLayer;

class BallotIterator implements Iterator<Ballot>{

	private ResultSet   rs = null;
	private ObjectLayer objectLayer = null;
	private boolean     more = false;

	public BallotIterator( ResultSet rs, ObjectLayer objectLayer) throws EVException{
		this.rs = rs;
		this.objectLayer = objectLayer;
		try {
			more = rs.next();
		}
		catch( Exception e ) {
			throw new EVException( "BallotIterator: Cannot create Ballot iterator; root cause: " + e );
		}
	}

	public boolean hasNext(){
		return more;
	}

	public Ballot next(){
		long id;
		Date openDate;
		Date closeDate;
		boolean approved;
		ElectoralDistrict electoralDistrict = null;


		if( more ){

			try {
				id = rs.getLong( 1 );
				openDate = rs.getDate( 2 );
				closeDate = rs.getTime( 3 );
				approved = rs.getBoolean( 4 );

				more = rs.next();
			} catch(Exception e) {
				throw new NoSuchElementException( "BallotIterator: No next Ballot object; root cause: " + e );
			}

			Ballot ballot = null;
			try {
				ballot = objectLayer.createBallot(openDate, closeDate, approved, electoralDistrict);
				ballot.setId( id );
			} catch (EVException e) {
				e.printStackTrace();
			}

			return ballot;
		} else {
			throw new NoSuchElementException( "BallotIterator: No next Ballot object. " );
		}

	}

	public void remove() {
		throw new UnsupportedOperationException();
	}
}
