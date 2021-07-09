package edu.uga.cs.evote.persistence.impl;

import java.sql.ResultSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Candidate;
import edu.uga.cs.evote.entity.Election;
import edu.uga.cs.evote.entity.PoliticalParty;
import edu.uga.cs.evote.object.ObjectLayer;

public class CandidateIterator implements Iterator<Candidate>{

	private ResultSet	rs = null;
	private ObjectLayer	objectLayer = null;
	private boolean		more = false;

	public CandidateIterator( ResultSet rs, ObjectLayer objectLayer) throws EVException {
		this.rs = rs;
		this.objectLayer = objectLayer;
		try {
			more = rs.next();
		} catch(Exception e) {
			throw new EVException( "CandidateIterator: Cannot create Candidate iterator; root cause: " + e );
		}
	}

	public boolean hasNext() {
		return more;
	}

	public Candidate next() {

		long id;
		String name;
		PoliticalParty politicalParty = null;
		Election election = null;
		int voteCount;

		if( more ){

			try {
				id = rs.getLong( 1 );
				name = rs.getString( 2 );
				voteCount = rs.getInt( 3 );

				more = rs.next();
			}
			catch( Exception e ){
				throw new NoSuchElementException( "CandidateIterator: No next Candidate object; root cause: " + e );
			}

			Candidate candidate = null;
			try {
				candidate = objectLayer.createCandidate(name, politicalParty, election);
				candidate.setId( id );
			} catch (EVException e) {
				e.printStackTrace();
			}

			return candidate;

		} else{
			throw new NoSuchElementException( "CandidateIterator: No next Candidate object" );
		}

	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

}
