package edu.uga.cs.evote.persistence.impl;

import java.sql.ResultSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Candidate;
import edu.uga.cs.evote.entity.Election;
import edu.uga.cs.evote.entity.PoliticalParty;
import edu.uga.cs.evote.object.ObjectLayer;

public class PoliticalPartyIterator implements Iterator<PoliticalParty>{

	private ResultSet	rs = null;
	private ObjectLayer	objectLayer = null;
	private boolean		more = false;

	public PoliticalPartyIterator( ResultSet rs, ObjectLayer objectLayer) throws EVException {
		this.rs = rs;
		this.objectLayer = objectLayer;
		try {
			more = rs.next();
		} catch(Exception e) {
			throw new EVException( "PoliticalPartyIterator: Cannot create PoliticalParty iterator; root cause: " + e );
		}
	}

	public boolean hasNext() {
		return more;
	}

	public PoliticalParty next() {

		long id;
		String name;
		Candidate candidate = null;


		if (more) {

			try {
				id = rs.getLong( 1 );
				name = rs.getString( 2 );


				more = rs.next();
			} catch( Exception e ){
				throw new NoSuchElementException( "PoliticalIterator: No next Political object; root cause: " + e );
			}

			PoliticalParty PP = null;
			try {
				PP = objectLayer.createPoliticalParty(name);
				PP.setId( id );
			} catch (EVException e) {
				e.printStackTrace();
			}

			return PP;

		} else {
			throw new NoSuchElementException( "PoliticalIterator: No next Candidate object" );
		}

	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

}
