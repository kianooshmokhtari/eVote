package edu.uga.cs.evote.persistence.impl;

import java.sql.ResultSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.*;
import edu.uga.cs.evote.object.ObjectLayer;

public class ElectoralDistrictIterator implements Iterator<ElectoralDistrict>{

	private ResultSet	rs = null;
	private ObjectLayer	objectLayer = null;
	private boolean		more = false;

	public ElectoralDistrictIterator(ResultSet rs, ObjectLayer objectLayer) throws EVException {
		this.rs = rs;
		this.objectLayer = objectLayer;
		try {
			more = rs.next();
		}
		catch( Exception e ) {
			throw new EVException( "ElectoralDistrictIterator: Cannot create ElectoralDistrictIterator; root cause: " + e );
		}
	}

	public boolean hasNext(){
		return more;
	}

	public ElectoralDistrict next(){

		long id;
		String name;
		Voter voter = null;
		Ballot ball = null;

		if( more ){

			try {
				id = rs.getLong( 1 );
				name= rs.getString( 2 );
				more = rs.next();
			} catch (Exception e) {
				throw new NoSuchElementException( "ElectoralDistrictIterator: No next ElectoralDistrict object; root cause: " + e );
			}

			ElectoralDistrict ED = null;
			try {
				ED = objectLayer.createElectoralDistrict(name);
				ED.setId( id );
			} catch (EVException e) {
				e.printStackTrace();
			}

			return ED;

		} else {
			throw new NoSuchElementException( "ElectoralDistrictIterator: No next ElectoralDistrict object" );
		}

	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

}
