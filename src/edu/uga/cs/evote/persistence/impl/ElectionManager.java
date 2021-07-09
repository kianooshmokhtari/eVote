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
import edu.uga.cs.evote.object.ObjectLayer;

public class ElectionManager {

	private ObjectLayer objectLayer = null;
    private Connection   conn = null;

	public ElectionManager( Connection conn, ObjectLayer objectLayer ){
		this.conn = conn;
        this.objectLayer = objectLayer;
	} // constructor

	//store methods

	//Store a given Election object in the persistent data store.
	public void storeElection ( Election election ) throws EVException{
		String				insertElectionSql = "insert into Items ( type, votecount, office, isPartisan ) values ( ?, ?, ?, ? )";
		String				updateElectionSql = "update Items set type = ?, votecount = ?, office = ?, isPartisan = ? where id = ?";
		PreparedStatement	stmt = null;
		int					inscnt;
		long				electionId;

		try {

			if( !election.isPersistent() )
				stmt = (PreparedStatement) conn.prepareStatement( insertElectionSql );
			else
				stmt = (PreparedStatement) conn.prepareStatement( updateElectionSql );

			stmt.setString( 1, "election" );

			if( election.getVoteCount() >= 0)
				stmt.setInt(2, election.getVoteCount() );
			else
				throw new EVException( "ElectionManager.store: can't store an Election: vote count below zero" );

			if( election.getOffice() != null )
				stmt.setString( 3, election.getOffice() );
			else
				throw new EVException( "ElectionManager.store: can't store an Election: office undefined" );

			stmt.setBoolean( 4, election.getIsPartisan() );

			if( election.isPersistent() ){
				stmt.setLong(5 , election.getId() );
			}

			inscnt = stmt.executeUpdate();

			if( !election.isPersistent() ) {
				if( inscnt >= 1 ) {
					String sql = "select last_insert_id()";
					if( stmt.execute( sql ) ) { // statement returned a result

                        // retrieve the result
                        ResultSet r = stmt.getResultSet();

                        // we will use only the first row!
                        //
                        while( r.next() ) {

                            // retrieve the last insert auto_increment value
                            electionId = r.getLong( 1 );
                            if( electionId > 0 )
                                election.setId( electionId ); // set this person's db id (proxy object)
                        }
                    }
                }
                else
                    throw new EVException( "ElectionManager.store: failed to store an Election" );
            }
            else {
                if( inscnt < 1 )
                    throw new EVException( "ElectionManager.store: failed to store an Election" );
            }
        }
        catch( SQLException e ) {
            e.printStackTrace();
            throw new EVException( "ElectionManager.store: failed to store an Election: " + e );
        }

	} // store methods

	// restore methods

	//Restore all Election objects that match attributes of the model Election.
	public List<Election> restoreElection (Election election) throws EVException{
		String       selectElectionSql = "select id, votecount, office, isPartisan from Items where type = 'election'";
        Statement    stmt = null;
        StringBuffer query = new StringBuffer( 100 );
        StringBuffer condition = new StringBuffer( 100 );
        List<Election>   elections = new ArrayList<Election>();

        condition.setLength( 0 );

        // form the query based on the given Election object instance
        query.append( selectElectionSql );

        if( election != null ) {
            if( election.getId() >= 0 ) // id is unique, so it is sufficient to get an election
                query.append( " and id = " + election.getId() );
            else if( election.getOffice() != null ) // office is unique, so it is sufficient to get a person
                query.append( " and office = '" + election.getOffice() + "'" );
            else {
                condition.append( " and isPartisan = " + election.getIsPartisan() );
            }
        }

        try {

            stmt = conn.createStatement();

            // retrieve the persistent Election objects
            //
            if( stmt.execute( query.toString() ) ) { // statement returned a result

                long   id;
                int votecount;
                String office;
                boolean isPartisan;
                Election  nextElection = null;

                ResultSet rs = stmt.getResultSet();

                // retrieve the retrieved clubs
                while( rs.next() ) {

                    id = rs.getLong( 1 );
                    votecount = rs.getInt( 2 );
                    office = rs.getString( 3 );
                    isPartisan = rs.getBoolean( 4 );

                    nextElection = objectLayer.createElection(); // create a proxy Election object
                    // and now set its retrieved attributes
                    nextElection.setId( id );
                    nextElection.setOffice( office );
                    nextElection.setIsPartisan( isPartisan );
                    // set this to null for the "lazy" association traversal
                    //nextElection.setBallot( null );
                    nextElection.setVoteCount( votecount );

                    elections.add( nextElection );
                }

                return elections;
            }
        }
        catch( Exception e ){
        	throw new EVException( "ElectionManager.restore: Could not restore persisten Election Objects; root cause: " + e);
        }
        throw new EVException( "ElectionManager.restore: Could not restore persistent Election objects." );
	} // restore

	// delete methods

	//Delete a given Election object from the persistent data store.
	public void deleteElection (Election election) throws EVException{

		String               deleteElectionSql = "delete from Items where id = ?";
        PreparedStatement    stmt = null;
        int                  inscnt;

        if( !election.isPersistent() ) // is the Club object persistent?  If not, nothing to actually delete
            return;

        try {
            stmt = (PreparedStatement) conn.prepareStatement( deleteElectionSql );
            stmt.setLong( 1, election.getId() );
            inscnt = stmt.executeUpdate();
            if( inscnt == 1 ) {
                return;
            }
            else
                throw new EVException( "ElectionManager.delete: failed to delete an Election" );
        }
        catch( SQLException e ){
        	e.printStackTrace();
        	throw new EVException( "ElectionManager.delete: failed to delete an Election: " + e );
        }

	} // delete

}
