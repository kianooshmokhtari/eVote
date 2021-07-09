package edu.uga.cs.evote.persistence.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mysql.jdbc.PreparedStatement;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.*;
import edu.uga.cs.evote.object.*;

class PoliticalPartyManager {

	private ObjectLayer objectLayer = null;
	private Connection conn = null;



	public PoliticalPartyManager( Connection conn, ObjectLayer objectLayer){

		this.conn = conn;
		this.objectLayer = objectLayer;

	}

	public void store(PoliticalParty politicalparty ) throws EVException{

		String 				insertPoliticalPartySql = "insert into Parties ( name ) values ( ? )";
		String 				updatePoliticalPartySql = "update Parties set name = ? where id = ?";
		PreparedStatement 	stmt;
		int 				inscnt;
		long 				politicalpartyId;



		try{
			if(!politicalparty.isPersistent())
				stmt = (PreparedStatement) conn.prepareStatement(insertPoliticalPartySql);
			else
				stmt = (PreparedStatement) conn.prepareStatement(updatePoliticalPartySql);

			if(politicalparty.getName() != null)
				stmt.setString(1, politicalparty.getName());
			else
				throw new EVException("PoliticalPartyManager.save: can't save a Political Party: name undefined");
			if(politicalparty.isPersistent())
				stmt.setLong(2,  politicalparty.getId());

			inscnt = stmt.executeUpdate();

			if(!politicalparty.isPersistent()){

				if(inscnt >= 1){
					String sql = "select last_insert_id()";
					if(stmt.execute(sql)){

						ResultSet r = stmt.getResultSet();

							while(r.next()){

								politicalpartyId = r.getLong(1);
								if(politicalpartyId > 0)
									politicalparty.setId(politicalpartyId);
							}

					}


				}

			}

			else{
				if(inscnt < 1)
					throw new EVException("PoliticalPartyManager.save: failed to save a political party");
			}

		}

		catch(SQLException e){

			e.printStackTrace();
			throw new EVException("PoliticalPartyManager.save: failed to save a political party" + e);
		}

	}

	public List <PoliticalParty> restore( PoliticalParty modelParty) throws EVException{

		String 			selectPoliticalPartySql = "select id, name from Parties";
		Statement 		stmt = null;
		StringBuffer	query = new StringBuffer(100);
		StringBuffer 	condition = new StringBuffer(100);
		List<PoliticalParty> politicalparties = new ArrayList<PoliticalParty>();

		condition.setLength(0);

		query.append(selectPoliticalPartySql);

		if(modelParty != null){
			if(modelParty.getId() >= 0)
				query.append(" where id = " + modelParty.getId());
			else if(modelParty.getName() != null)
				query.append(" where name = '" + modelParty.getName() + "'");

			else{
				//might not need
				if(condition.length() > 0){
					query.append(" where ");
					query.append(condition);
				}
			}

		}

	try{

		stmt = conn.createStatement();

		//retrieve PoliticalPartyObject
		if( stmt.execute(query.toString())){

			ResultSet rs = stmt.getResultSet();

			long 	id;
			String name;
			PoliticalParty politicalparty;

			while(rs.next()){

				id = rs.getLong(1);
				name = rs.getString(2);

				politicalparty = objectLayer.createPoliticalParty(name);
				politicalparty.setId(id);

				politicalparties.add(politicalparty);
			}

			return politicalparties;
		}

	}
	catch(Exception e){
		throw new EVException("PoliticalManager.restor: could not restore persistent PoliticalParty Object: "+ e);

	}
	throw new EVException("PiliticalManager.restore: could not restore persistent Party object");
	}




	public void delete(PoliticalParty modelParty) throws EVException{

		String				deletePoliticalPartySql = "delete from Parties where id = ?";
		PreparedStatement	stmt = null;
		int					inscnt;

		//if persistence, no delete
		if(!modelParty.isPersistent())
			return;


		try{

			stmt = (PreparedStatement) conn.prepareStatement(deletePoliticalPartySql);

			stmt.setLong(1, modelParty.getId());

			inscnt = stmt.executeUpdate();

			if(inscnt == 0)
				throw new EVException("PoliticalPartyManager.delete failed to delete a political party");
		}

		catch(Exception e){
			throw new EVException("PoliticalPartyManager.delete failed " + e.getMessage());
		}
	}

	public void storeCandidateIsMemberOfPoliticalParty(Candidate candidate, PoliticalParty party)
			throws EVException {
		String				insertBallotSql = "insert into IsMemberOf ( candidateID, partyID ) values ( ?, ? )";
		String				selectBallotSql = "select id, candidateID, partyID from IsMemberOf"
											 + " where candidateID = " + candidate.getId() + " and partyID = " + party.getId();
		Statement			stmt;
		PreparedStatement	pstmt = null;
		int					inscnt;
		long				candidateId = candidate.getId();
		long				partyId = party.getId();
		StringBuffer		query = new StringBuffer( 100 );

		try {
			if( candidate.isPersistent() && party.isPersistent() ){

				query.append( selectBallotSql );
				stmt = conn.createStatement();
				if( stmt.execute( query.toString() ) ) { // statement returned a result

					ResultSet rs = stmt.getResultSet();

					if(!rs.isBeforeFirst() ) {
						pstmt = (PreparedStatement) conn.prepareStatement( insertBallotSql );
						pstmt.setLong( 1 , candidateId);
						pstmt.setLong( 2 , partyId);

						inscnt = pstmt.executeUpdate();
					}
				}
			} else{
			throw new EVException("PoliticalPartyManager.storeCandidateIsMemberOfPoliticalParty: failed to store a link" );
			}

		} catch(SQLException e) {
			e.printStackTrace();
			throw new EVException("PoliticalPartyManager.storeCandidateIsMemberOfPoliticalParty: failed to store a link: " + e);
		}

	}

	public List<Candidate> restoreCandidateIsMemberOfPoliticalParty(PoliticalParty PP)
			throws EVException {

		String		selectCandidateIsMemberSql = "select c.id, c.name, c.voteCount, m.candidateID, m.partyID, p.id, p.name"
												+" from Candidates c, IsMemberOf m, Parties p where m.candidateID = c.id ";

		Statement stmt;
		StringBuffer query = new StringBuffer(100);
		StringBuffer condition = new StringBuffer(100);
		List<Candidate> candidate  = new ArrayList<Candidate>();

		if(!PP.isPersistent())
			throw new EVException("Could not restore political party object, object is not persistant");

		query.append(selectCandidateIsMemberSql);


		condition.append("and m.PartyID = " + PP.getId());

		query.append(condition);


		try{
			stmt = conn.createStatement();
			if(stmt.execute(query.toString())){
				ResultSet r = stmt.getResultSet();
				String name;
				while (r.next()) {
					name = r.getString(2);
					Candidate can = objectLayer.createCandidate();
					can.setName(name);
					can.setPoliticalParty(PP);
					candidate.add(can);
				}
				return candidate;
			}
		} catch(Exception e) {
			throw new EVException("PoliticalPartyManager.CandidateIsMemberOfPoliticalParty: " + e.getMessage());
		}


		throw new EVException("PoliticalPartyManager.CandidateIsMemberOfPoliticalParty");

	}

	public void deleteCandidateIsMemberOfElection(Candidate candidate, PoliticalParty PP)
				throws EVException {


		String				deleteSql = "delete from IsMemberOf where candidateID = ? and partyID = ?";
		PreparedStatement	stmt = null;
		int 				incsnt;

		if(!candidate.isPersistent() || !PP.isPersistent())
			return;


		try {

			stmt = (PreparedStatement) conn.prepareStatement(deleteSql);
			stmt.setLong(1, candidate.getId());
			stmt.setLong(2, PP.getId());
			incsnt = stmt.executeUpdate();

			if(incsnt >0){
				return;
			} else
				throw new EVException("PoliticalPartyManager.deleteCandidateIsMEmberOfElection failed to delete");



		}
		catch (SQLException e) {
			throw new EVException("PoliticalPartyManager.deleteCandidateIsMEmberOfElection failed to delete" + e.getMessage());
		}

	}

}
