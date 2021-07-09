package edu.uga.cs.evote.persistence.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.PreparedStatement;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Candidate;
import edu.uga.cs.evote.entity.Election;
import edu.uga.cs.evote.entity.PoliticalParty;
import edu.uga.cs.evote.object.ObjectLayer;

class CandidateManager {

	private ObjectLayer objectLayer = null;
    private Connection   conn = null;

	public CandidateManager( Connection conn, ObjectLayer objectLayer ){
		this.conn = conn;
        this.objectLayer = objectLayer;
	} // constructor

	// store methods

	//Store a given Candidate object in the persistent data store.
	public void storeCandidate ( Candidate candidate) throws EVException{
		String               insertCandidateSql = "insert into Candidates ( name, votecount ) values ( ?, ? )";
        String               updateCandidateSql = "update Candidates set name = ?, votecount = ? where id = ?";
        PreparedStatement    stmt = null;
        int                  inscnt;
        long                 candidateId = candidate.getId();


        try{
        	if( !candidate.isPersistent() ){

                stmt = (PreparedStatement) conn.prepareStatement( insertCandidateSql );
        	}
            else{


                stmt = (PreparedStatement) conn.prepareStatement( updateCandidateSql );
            }

        	//if name is unique
        	if( candidate.getName() != null ){



        		stmt.setString( 1, candidate.getName() );
        	}
        	else
        		throw new EVException( "CandidateManager.store: can't store a Candidate: name undefined" );

        	if( candidate.getVoteCount() >= 0){

        		stmt.setInt(2, candidate.getVoteCount() );
        	}
        	else
        		throw new EVException( "CandidateManager.store: can't store a Candidate: vote count below zero" );

        	if( candidate.isPersistent() ){
        		stmt.setLong( 3, candidateId);

        	}

        	inscnt = stmt.executeUpdate();


        	if( inscnt >= 1  && !candidate.isPersistent()) {

        		String sql = "select last_insert_id()";
                if( stmt.execute( sql ) ) { // statement returned a result


                    // retrieve the result
                    ResultSet r = stmt.getResultSet();

                    // we will use only the first row!
                    //
                    while( r.next() ) {

                        // retrieve the last insert auto_increment value
                        candidateId = r.getLong( 1 );
                        if( candidateId > 0 )
                            candidate.setId( candidateId ); // set this membership's db id (proxy object)
                    }
                }
        	}
        	else{
        		if(inscnt < 1)
        		throw new EVException( "CandidateManager.store: can't store ddda Candidate." );
        	}
        }
        catch( SQLException e ){
        	e.printStackTrace();
        	throw new EVException( "CandidateManager.store failed to store a Candidate: " + e );
        }
	}

	//Store a link between a Election and a Candidate included on the Election.
	public void storeCandidateIsCandidateInElection(Candidate candidate, Election election) throws EVException{
		String               insertBallotSql = "insert into IsCandidateIn ( candidateID, electionID ) values ( ?, ? )";
		String       		 selectBallotSql = "select id, candidateID, electionID from IsCandidateIn"
				 							+ " where candidateID = " + candidate.getId() + " and electionID = " + election.getId();
        String               updateBallotSql = "update IsCandidateIn set candidateID = ?, electionID = ? where id = ?";
        Statement			 stmt;
        PreparedStatement    pstmt = null;
        int                  inscnt;
        long                 candidateId = candidate.getId();
        long				 electionId = election.getId();
        StringBuffer 		 query = new StringBuffer( 100 );

        try {
       	 	if( candidate.isPersistent() && election.isPersistent() ){

	       	 	query.append( selectBallotSql );
	   	 		stmt = conn.createStatement();
	   	 		if( stmt.execute( query.toString() ) ) { // statement returned a result

	   	 			ResultSet rs = stmt.getResultSet();

	   	 			if(!rs.isBeforeFirst() ) {
	   	 				pstmt = (PreparedStatement) conn.prepareStatement( insertBallotSql );
	   	 				pstmt.setLong( 1 , candidateId);
	   	 				pstmt.setLong( 2 , electionId);

	   	 				inscnt = pstmt.executeUpdate();
	   	 			}
	   	 		}
       	 	}
       	 	else{

       	 	throw new EVException("CandidateManager.storeCandidateIsCandidateInElection: failed to store a link" );
       	 	}
        }
        catch( SQLException e) {
        	e.printStackTrace();
        	throw new EVException("CandidateManager.storeCandidateIsCandidateInElection: failed to store a link: " + e);
        }


	}// store methods

	// restore methods

	//Restore all Candidate objects that match attributes of the model Candidate.
	public List<Candidate> restoreCandidate (Candidate candidate) throws EVException{
		String       selectCandidateSql = "select id, name, votecount from Candidates";
        Statement    stmt = null;
        StringBuffer query = new StringBuffer( 100 );
        StringBuffer condition = new StringBuffer( 100 );
        List<Candidate>   candidates = new ArrayList<Candidate>();

        condition.setLength( 0 );

        // form the query based on the given Club object instance
        query.append( selectCandidateSql );

        if( candidate!= null ) {
            if( candidate.getId() >= 0 ) // id is unique, so it is sufficient to get a person
                query.append( " where id = " + candidate.getId() );
            else if( candidate.getName() != null ) // userName is unique, so it is sufficient to get a person
                query.append( " where name = '" + candidate.getName() + "'" );
            else
                condition.append( " where votecount = " + candidate.getVoteCount() );
        }

        try{

        	stmt = conn.createStatement();

            // retrieve the persistent Club objects

            if( stmt.execute( query.toString() ) ) { // statement returned a result

                long   id;
                String name;
                int voteCount;
                Candidate   nextCandidate = null;

                ResultSet rs = stmt.getResultSet();

                // retrieve the retrieved clubs
                while( rs.next() ) {

                    id = rs.getLong( 1 );
                    name = rs.getString( 2 );
                    voteCount = rs.getInt( 3 );

                    nextCandidate = objectLayer.createCandidate(); // create a proxy club object
                    // and now set its retrieved attributes
                    nextCandidate.setId( id );
                    nextCandidate.setName( name );
                    nextCandidate.setVoteCount( voteCount );

                    candidates.add( nextCandidate );
                }

                return candidates;
            }
        }
        catch( Exception e ){
        	throw new EVException( "CandidateManager.restore failed to restore persistent Candidate objects; Root cause: " + e);
        }

		throw new EVException( "CandidateManager.restore failed to restore persistent Candidate objects." );
	}


	//Return the Election in which a given Candidate runs.
	public Election restoreCandidateIsCandidateInElection(Candidate candidate) throws EVException{
		String       selectElectionSql = "select x.id, x.votecount, x.office, x.isPartisan"
										+ " from Items x, IsCandidateIn y"
										+ " where y.electionID = x.id and";
		Statement    stmt = null;
		StringBuffer query = new StringBuffer( 100 );

		query.append( selectElectionSql );

		if( candidate != null ){
			query.append( " y.candidateID = " + candidate.getId() );
		}

		try {

            stmt = conn.createStatement();

            // retrieve the Election

            if( stmt.execute( query.toString() ) ) { // statement returned a result
                ResultSet rs = stmt.getResultSet();

                long   id;
                int voteCount;;
                String office;
                boolean isPartisan;

                Election election = null;

                while( rs.next() ) {

                    id = rs.getLong( 1 );
                    voteCount = rs.getInt( 2 );
                    office = rs.getString( 3 );
                    isPartisan = rs.getBoolean( 4 );

                    election = objectLayer.createElection(office, isPartisan, null);
                    election.setId( id );
                    election.setVoteCount(voteCount);
                }

                return election;
            }
        }
        catch( Exception e ){
        	throw new EVException( "CandidateManager.restoreCandidateIsCandidateInElection could not restore persistent Election; root cause: " + e);
        }

        throw new EVException( "CandidateManager.restoreCandidateIsCandidateInElection could not restore persistent Election." );

	}

	//Return Candidates running in a given Election.
	public List<Candidate> restoreCandidateIsCandidateInElection(Election election) throws EVException{
		String       selectCandidateSql = "select x.id, x.name, x.votecount"
											+ " from Candidates x, IsCandidateIn y"
											+ " where y.candidateID = x.id and";
		Statement    stmt = null;
		StringBuffer query = new StringBuffer( 100 );
		List<Candidate> candidates = new ArrayList<Candidate>();

		query.append( selectCandidateSql );

		if( election !=null ){
			query.append( " y.electionID = " + election.getId() );
		}

		try{

			stmt= conn.createStatement();

			// retrieve Candidates

			if( stmt.execute( query.toString() ) ) {
				ResultSet rs = stmt.getResultSet();

				long id;
				String name;
				int voteCount;

				Candidate nextCandidate = null;

				while ( rs.next() ){

					id = rs.getLong( 1 );
					name = rs.getString( 2 );
					voteCount = rs.getInt( 3 );

					nextCandidate = objectLayer.createCandidate(name, null, election);
					nextCandidate.setId(id);
					nextCandidate.setVoteCount(voteCount);

					candidates.add( nextCandidate );
				}

				return candidates;
			}
		}
		catch( Exception e ){
        	throw new EVException( "CandidateManager.restoreCandidateIsCandidateInElection could not restore persistent Candidates; root cause: " + e);
        }

        throw new EVException( "CandidateManager.restoreCandidateIsCandidateInElection could not restore persistent Candidates." );
	}

	//Return the PoliticalParty of a given Candidate.
	public PoliticalParty restoreCandidateIsMemberOfPoliticalParty(Candidate candidate) throws EVException{
		String       selectPartySql = "select x.id, x.name"
										+ " from Parties x, IsMemberOf y"
										+ " where y.partyID = x.id and";
		Statement    stmt = null;
		StringBuffer query = new StringBuffer( 100 );

		query.append( selectPartySql );

		if( candidate != null ){
			query.append( " y.candidateID = " + candidate.getId() );
		}

		try {

            stmt = conn.createStatement();

            // retrieve the Election

            if( stmt.execute( query.toString() ) ) { // statement returned a result
                ResultSet rs = stmt.getResultSet();

                long   id;
                String name;

                PoliticalParty party = null;

                while( rs.next() ) {

                    id = rs.getLong( 1 );
                    name = rs.getString( 2 );

                    party = objectLayer.createPoliticalParty(name);
                    party.setId( id );
                }

                return party;
            }
        }
        catch( Exception e ){
        	throw new EVException( "CandidateManager.restoreCandidateIsMemberOfPoliticalParty could not restore persistent Election; root cause: " + e);
        }

        throw new EVException( "CandidateManager.restoreCandidateIsMemberOfPoliticalParty could not restore persistent Election." );
	}

	//Return Candidates who are members of a given PoliticalParty.
	public List<Candidate> restoreCandidateIsMemberOfPoliticalParty(PoliticalParty politicalParty) throws EVException{
		String       selectCandidateSql = "select x.id, x.name, x.votecount"
										+ " from Candidates x, IsMemberOf y"
										+ " where y.candidateID = x.id and";
		Statement    stmt = null;
		StringBuffer query = new StringBuffer( 100 );
		List<Candidate> candidates = new ArrayList<Candidate>();

		query.append( selectCandidateSql );

		if( politicalParty !=null ){
			query.append( " y.partyID = " + politicalParty.getId() );
		}

		try{

			stmt= conn.createStatement();

			// retrieve Candidates

			if( stmt.execute( query.toString() ) ) {
				ResultSet rs = stmt.getResultSet();

				long id;
				String name;
				int voteCount;

				Candidate nextCandidate = null;

				while ( rs.next() ){

					id = rs.getLong( 1 );
					name = rs.getString( 2 );
					voteCount = rs.getInt( 3 );

					nextCandidate = objectLayer.createCandidate(name, politicalParty, null);
					nextCandidate.setId(id);
					nextCandidate.setVoteCount(voteCount);

					candidates.add( nextCandidate );
				}

				return candidates;
			}
		}
		catch( Exception e ){
        	throw new EVException( "CandidateManager.restoreCandidateIsMemberOfPoliticalParty could not restore persistent Candidates; root cause: " + e);
        }

        throw new EVException( "CandidateManager.restoreCandidateIsMemberOfPoliticalParty could not restore persistent Candidates." );
	} // restore methods

	// delete methods

	//Delete a given Candidate object from the persistent data store.
	public void deleteCandidate (Candidate candidate) throws EVException{

		String               deleteCandidateSql = "delete from Candidates where id = ?";
        PreparedStatement    stmt = null;
        int                  inscnt;

        if( !candidate.isPersistent() ) // is the Candidate object persistent?  If not, nothing to actually delete
            return;

        try{
        	stmt = (PreparedStatement) conn.prepareStatement( deleteCandidateSql );
            stmt.setLong( 1, candidate.getId() );
            inscnt = stmt.executeUpdate();

            if( inscnt == 1 )
                return;
            else
            	throw new EVException( "CandidateManager.delete failed to delete a Candidate." );
        }
        catch( SQLException e){
        	e.printStackTrace();
        	throw new EVException( "CandidateManager.delete failed to delete a Candidate: " + e );
        }
	}

	//Delete a link between a Election and a Candidate included on the Election.
	public void deleteCandidateIsCandidateInElection(Candidate candidate, Election election) throws EVException{
		String	deleteIsCandidateInSql = "delete from IsCandidateIn where";
		PreparedStatement    stmt = null;
        int                  inscnt;
        StringBuffer query = new StringBuffer( 100 );

        if( !candidate.isPersistent() || !election.isPersistent() ){
        	return;
        }

        

        if( candidate != null && election != null ){
        	deleteIsCandidateInSql += " candidateID = " + candidate.getId()
        					+ " and electionID = " + election.getId();
        	query.append( deleteIsCandidateInSql );
        }

        try {
            stmt = (PreparedStatement) conn.prepareStatement( deleteIsCandidateInSql );
            inscnt = stmt.executeUpdate();
            if( inscnt == 1 ) {
                return;
            }
            else
                throw new EVException( "CandidateManager.deleteCandidateIsCandidateInElection: failed to delete an IsCandidateIn relationship" );
        }
        catch( SQLException e ) {
            e.printStackTrace();
            throw new EVException( "CandidateManager.deleteCandidateIsCandidateInElection: failed to delete an IsCandidateIn relationship; root cause: " + e );
        }
	}

	//Delete a link between a Candidate and a PoliticalParty to which the Candidate belongs.
	void deleteCandidateIsMemberOfElection(Candidate candidate, PoliticalParty politicalParty) throws EVException{
		String	deleteIsMemberOfSql = "delete from IsMemberOf where";
		PreparedStatement    stmt = null;
        int                  inscnt;
        StringBuffer query = new StringBuffer( 100 );

        if( !candidate.isPersistent() || !politicalParty.isPersistent() ){
        	return;
        }

        

        if( candidate != null && politicalParty != null ){
        	deleteIsMemberOfSql += " candidateID = " + candidate.getId()
        					+ " and partyID = " + politicalParty.getId();
        	query.append( deleteIsMemberOfSql );
        }

        try {
            stmt = (PreparedStatement) conn.prepareStatement( deleteIsMemberOfSql );
            inscnt = stmt.executeUpdate();
            if( inscnt == 1 ) {
                return;
            }
            else
                throw new EVException( "CandidateManager.deleteCandidateIsMemberOfElection: failed to delete an IsMemberOf relationship" );
        }
        catch( SQLException e ) {
            e.printStackTrace();
            throw new EVException( "CandidateManager.deleteCandidateIsMemberOfElection: failed to delete an IsMemberOf relationship; root cause: " + e );
        }
	}

	// delete methods

}
