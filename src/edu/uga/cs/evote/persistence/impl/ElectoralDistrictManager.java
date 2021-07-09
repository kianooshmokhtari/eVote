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
import edu.uga.cs.evote.object.impl.ObjectLayerImpl;

public class ElectoralDistrictManager {

	private ObjectLayer objectLayer = null;
	private Connection conn = null;

	public ElectoralDistrictManager(Connection conn, ObjectLayer objectLayer){

		this.conn = conn;
		this.objectLayer = objectLayer;

	}

	public void store(ElectoralDistrict ED) throws EVException{

		String				insertElectoralDistrictSql = "insert into Districts (name) values (?)";
		String 				updateElectoralDistrictSql = "update Districts set name = ? where id = ?";
		PreparedStatement	stmt ;
		int 				inscnt;
		long				ElectoralDistrictId;


		try{

			if(!ED.isPersistent())
				stmt = (PreparedStatement) conn.prepareStatement(insertElectoralDistrictSql);
			else
				stmt = (PreparedStatement) conn.prepareStatement(updateElectoralDistrictSql);

			if(ED.getName()!= null)
				stmt.setString(1, ED.getName());
			else
				throw new EVException("Could not store Electoral district");

			if(ED.isPersistent())
				stmt.setLong(2, ED.getId());
			inscnt = stmt.executeUpdate();

			if(!ED.isPersistent())
				if(inscnt > 0){
					String sql = "select last_insert_id()";
					if(stmt.execute(sql)){

						ResultSet r = stmt.getResultSet();

						while(r.next()){
							ElectoralDistrictId = r.getLong(1);
							if(ElectoralDistrictId >0)
								ED.setId(ElectoralDistrictId);


						}
					}

				}


		else{
			if(inscnt< 1)
				throw new EVException("ElectoralDistrictManager.store did not work:");


		}
	}

		catch(SQLException e){
			e.printStackTrace();
			throw new EVException("ElectoraDistrictManager,store did not work " + e.getMessage());

		}

	}


	public List<ElectoralDistrict> restore(ElectoralDistrict ED) throws EVException{

		String 			selectElectoralDistrictSql = "select id, name from Districts";
		Statement 	stmt = null;
		StringBuffer query = new StringBuffer(100);
		StringBuffer condition = new StringBuffer(100);
		List<ElectoralDistrict> EDL = new ArrayList<ElectoralDistrict>();

		condition.setLength(0);

		query.append(selectElectoralDistrictSql);


		if(ED != null){
			//id provided in argument
			if(ED.getId() >= 0)
				query.append(" where id = " + ED.getId());
			else if(ED.getName() != null)
				query.append(" where name = '" + ED.getName() + "'");


		}


		try{
			stmt = conn.createStatement();

			if(stmt.execute(query.toString())){

				ResultSet r = stmt.getResultSet();
				long id;
				String name;


				while(r.next()){

					id = r.getLong(1);
					name = r.getString(2);


					ElectoralDistrict electoraldist = objectLayer.createElectoralDistrict(name);
					electoraldist.setId(id);

					EDL.add(electoraldist);


				}

				return EDL;
			}
		}

		catch(Exception e){
			throw new EVException("ElectoralDistrict.restore did not work" + e.getMessage());

		}

		throw new EVException("ElectoralDistrict.restore did not work");
	}




	public void delete(ElectoralDistrict ED) throws EVException{

		String				deleteElectoralDistrict = "delete from Districts where id = ?";
		PreparedStatement	stmt = null;
		int inscnt;

		if(!ED.isPersistent()) //no object exsists to delte
			return;

		try{
			stmt = (PreparedStatement) conn.prepareStatement(deleteElectoralDistrict);

			stmt.setLong(1,  ED.getId());

			inscnt = stmt.executeUpdate();

			if(inscnt <1)
				throw new EVException("ElectoralDistrict.delte method did not work");



		}

		catch(SQLException e){
			throw new EVException("ElectoralDistrict.delete did not work: " + e.getMessage());

		}
	}



	public void storeElectoralDistrictHasBallotBallot(ElectoralDistrict ED, Ballot ballot) throws EVException{

		String 				insertEDhasBallotSql = "insert into HasBallot (districtID, ballotID) values (?, ?)";
		String 				selectCheckSql = "select id, districtID, ballotID from HasBallot where districtID = "
											+ ED.getId() + " and ballotID = " + ballot.getId();

		PreparedStatement	pstmt;
		Statement			stmt;
		int 				inscnt;
		StringBuffer query = new StringBuffer(100);


		if(ED.getId() < 1  || ballot.getId() < 1)
			throw new EVException("Attemping to store a ElectoralDistrict or Ballot object with id <0");
		if(!ED.isPersistent() || !ballot.isPersistent())
			throw new EVException("Attempting to store Electoral District or Ballot object that is not Persistent");

		try{

			query.append(selectCheckSql);

			stmt = conn.createStatement();

			if(stmt.execute(query.toString())){

				ResultSet r = stmt.getResultSet();

				if(!r.isBeforeFirst()){


			pstmt = (PreparedStatement) conn.prepareStatement(insertEDhasBallotSql);

			pstmt.setLong(1, ED.getId());
			pstmt.setLong(2, ballot.getId());

			inscnt = pstmt.executeUpdate();

		if(inscnt <=0)
			throw new EVException("EDManager,store for linked electoral district and ballot failed");

				}

		}
		}
		catch(SQLException e){
			e.printStackTrace();
			throw new EVException("EDManager.store for linked electoral district and ballot failed " + e.getMessage());



		}



	}

	public ElectoralDistrict restoreElectoralDistrictHasBallotBallot(Ballot ballot) throws EVException {
		String 			selectSql = "select d.id, d.name, h.districtID, h.ballotID, b.id from Districts d, HasBallot h, Ballots b"
				+ " where h.districtID = d.id and ";
		Statement 		stmt;
		StringBuffer 		query = new StringBuffer(100);
		query.append(selectSql);

		if(!ballot.isPersistent())
			throw new EVException("ballot object is not persistent");
		else{
			query.append(" b.id = " + ballot.getId() );

		}

		try{

			stmt = conn.createStatement();

			if(stmt.execute(query.toString())){

				ResultSet r = stmt.getResultSet();
				long id;
				String name;
				ElectoralDistrict ED = null;

					while(r.next()){

						id = r.getLong(1);
						name = r.getString(2);

						ED = objectLayer.createElectoralDistrict(name);
						ED.setId(id);
				}

				return ED;

			}


		}
		catch(SQLException e){
			throw new EVException("ElectoralDistrict.restoreElectoralDistrictHasBallotBallot failed; root: " +e.getMessage());

		}
		throw new EVException("ElectoralDistrict.restoreElectoralDistrictHasBallotBallot failed");

	}

	public List<Ballot> restoreElectoralDistrictHasBallotBallot(ElectoralDistrict ED) throws EVException{

		String		selectEDhasBallot = "select d.id, d.name, h.districtID, h.ballotID b.id, b.openDate,"
						+" b.closeDate, b.approved from d Districts, h HasBallot, b Ballots where";

		Statement stmt;
		StringBuffer query = new StringBuffer(100);
		StringBuffer condition = new StringBuffer(100);
		List<Ballot> ballots = new ArrayList<>();

		if(!ED.isPersistent())
			throw new EVException("Could not restore ballot, object undefined");

		query.append(selectEDhasBallot);


			condition.append(" h.districtID = " + ED.getId());
			condition.append(" and h.ballotID = b.id");
			query.append(condition);



		try{
			stmt = conn.createStatement();

			if(stmt.execute(query.toString())){

				ResultSet r = stmt.getResultSet();

				Date openDate;
				Date closeDate;
				boolean approve;


				while(r.next()){

					openDate = r.getDate(6);
					closeDate = r.getDate(7);
					approve = r.getBoolean(8);

					Ballot ball = objectLayer.createBallot(openDate, closeDate, approve, ED);

					ballots.add(ball);



				}

				return ballots;

			}




		}
		catch(Exception e){
			throw new EVException("ElectoralDistrictManager.restore did not work: " + e.getMessage());
		}


		throw new EVException("ElectoralDistrictManager.restore did not work");


}

	public void deleteElectoralDistrictHasBallotBallot(ElectoralDistrict ED, Ballot ballot) throws EVException{

		String 				deleteEDhasBALL = "delete from HasBallots where districtID = ? and ballotID = ?";
		PreparedStatement 	stmt = null;
		int 				inscnt;


		if(!ED.isPersistent() || !ballot.isPersistent()){
			return;

		}

		try{
			stmt = (PreparedStatement) conn.prepareStatement(deleteEDhasBALL);
			stmt.setLong(1, ED.getId());
			stmt.setLong(2, ballot.getId());
			inscnt = stmt.executeUpdate();

			if(inscnt >0){
				return;
			}
			else
				throw new EVException("ElectoralDistrictManager.delete did not work");
		}
		catch(SQLException e){
			throw new EVException ("ElectorlaDistrictManager.delete did not work " + e.getMessage());
		}

	}

	public void storeVoterBelongsToElectoralDistrict(Voter voter, ElectoralDistrict ED )throws EVException{

		String					insertVoterBelongToED = "insert into BelongsTo (voterID, districtID) values (? ,?)";
		String					checkSql = "select id, voterID, districtID from BelongsTo where voterID = " + voter.getId() +
											" and districtID = " + ED.getId();
		PreparedStatement		pstmt = null;
		Statement				stmt;
		StringBuffer query = new StringBuffer(100);
		int 					inscnt;

		//condition to make sure values being passed are not null
		if(!voter.isPersistent() || !ED.isPersistent() || ED.getName() == null)
			throw new EVException("ElectoralDistrictManager..storevoterBelongsToelectoralDistrict failed");

		try{
			if(voter.isPersistent() && ED.isPersistent()){

				query.append(checkSql);
				stmt = conn.createStatement();
				if(stmt.execute(query.toString())){

					ResultSet r = stmt.getResultSet();
					if(!r.isBeforeFirst()){

			pstmt = (PreparedStatement) conn.prepareStatement(insertVoterBelongToED);

			pstmt.setLong(1, voter.getId());
			pstmt.setLong(2, ED.getId());

			inscnt = pstmt.executeUpdate();

			if(inscnt < 1)
				throw new EVException("ElectoralDistrictManager.storeVoterLinkedwithDistrict failed");

					}
			}
			}
		}
		catch(SQLException e){
			throw new EVException("ElectoralDistrictManager.storeVoterLinkedwithDistrict failed" + e.getMessage());
		}

	}

	public List<Voter> restoreVoterBelongsToElectoralDistrict(ElectoralDistrict ED) throws EVException{


		String  selectVoterBelongsToED = "select d.id, d.name, b,voterID, b.districtID, v.id,"
										+ "v.type, v.username, v.userpass, v.firstname, v.lastname,"
										+ "v.email, v.address, v.age, v.voterID from Districts d, "
										+"BelongsTo b, Users v where b.voterID = v.id and v.type = 'voter' ";

		Statement stmt = null;
		StringBuffer query = new StringBuffer(100);
		StringBuffer condition = new StringBuffer(100);
		List<Voter> voters = new ArrayList<Voter>();
		condition.setLength(0);

		query.append(selectVoterBelongsToED);

		if(ED != null){
			if(ED.isPersistent()){
				query.append("and");
				query.append(" b.districtID = "+ED.getId());

			}


			//query.append(condition);



		}


		try{

			stmt = conn.createStatement();


			if(stmt.execute(query.toString())){

				ResultSet r = stmt.getResultSet();

				String userName;
				String userPass;
				String firstName;
				String lastName;
				String email;
				String address;
				int	 age;
				String voterID;
				Voter voter;

				while(r.next()){

					userName = r.getString(7);
					userPass = r.getString(8);
					firstName = r.getString(9);
					lastName = r.getString(10);
					email = r.getString(11);
					address = r.getString(12);
					age = r.getInt(13);
					voterID = r.getString(14);

					voter = objectLayer.createVoter(firstName, lastName, userName, userPass, email, address, age);
					voter.setVoterId(voterID);

					voters.add(voter);

				}

				return voters;



			}


		}
		catch(Exception e){
			throw new EVException("ElectoralDistrictManager.restoreVoterBelongsToElectoralDistrict failed, root: " + e.getMessage());
		}
		throw new EVException("ElectoralDistrictManager.restoreVoterBelongsToElectoralDistrict failed");
	}

	//possibly have to change method name
	public void deleteVoterBelongsToElection(Voter voter, ElectoralDistrict ED) throws EVException {

		String				deleteSql = "delete from BelongsTo where voterID = ? and districtID = ?";
		PreparedStatement	stmt = null;
		int					incsnt;

		if (!voter.isPersistent() || !ED.isPersistent())
			return;


		try {

			stmt = (PreparedStatement) conn.prepareStatement(deleteSql);
			stmt.setLong(1, voter.getId());
			stmt.setLong(2, ED.getId());
			incsnt = stmt.executeUpdate();

			if (incsnt <1) {
				return;
			} else
				throw new EVException("ElectoralDistrictManager.deleteVoterBelongsToElection did not work");

		}
		catch (SQLException e) {
			throw new EVException("ElectoralDistrictManager.deleteVoterBelongsToElection did not work");
		}

	}

}
