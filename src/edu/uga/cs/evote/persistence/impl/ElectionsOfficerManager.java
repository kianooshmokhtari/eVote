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

public class ElectionsOfficerManager {

	private ObjectLayer objectlayer = null;
	private Connection conn = null;

	public ElectionsOfficerManager(Connection conn, ObjectLayer objectLayer){

		this.conn = conn;
		this.objectlayer = objectLayer;

		}

	public void store(ElectionsOfficer EO) throws EVException{

		String 			insertElectionOfficerSQL = "insert into Users ( type, username, userpass, firstname, lastname, email, address)"
													+ " values ('officer', ?, ?, ?, ?, ?, ?)";

		String 			updateElectionOfficerSQL = "update Users set type = 'officer', username = ?, userpass = ?, firstname = ?, lastname = ?, "
													+ "email = ?, address = ?";

		PreparedStatement stmt;
		int				inscnt;
		long 			userId;


		try{


			if(!EO.isPersistent())
				stmt = (PreparedStatement) conn.prepareStatement(insertElectionOfficerSQL);
			else
				stmt = (PreparedStatement) conn.prepareStatement(updateElectionOfficerSQL);

			if(EO.getUserName() != null)
				stmt.setString(1, EO.getUserName());
			else
				throw new EVException("ElectionOfficerManager.store cant store a Person");

			if(EO.getPassword() != null)
				stmt.setString(2, EO.getPassword());
			else
				throw new EVException("ElectionOfficerManager.store cant store a Person");

			if(EO.getFirstName() != null)
				stmt.setString(3, EO.getFirstName());
			else
				throw new EVException("ElectionOfficerManager.store cant store a Person");

			if(EO.getLastName() != null)
				stmt.setString(4, EO.getLastName());
			else
				throw new EVException("ElectionOfficerManager.store cant store a Person");

			if(EO.getEmailAddress() != null)
				stmt.setString(5, EO.getEmailAddress());
			else
				throw new EVException("ElectionOfficerManager.store cant store a Person");

			if(EO.getAddress()!= null)
				stmt.setString(6, EO.getAddress());
			else
				throw new EVException("ElectionOfficerManager.store cant store a Person");



			if(EO.isPersistent())
				stmt.setLong(7, EO.getId());

			inscnt = stmt.executeUpdate();


			if(!EO.isPersistent()){

				//check to see if object got stored
				if(inscnt > 0){
					String sql = "select last_insert_id()";
					if(stmt.execute(sql)){

						ResultSet rs = stmt.getResultSet();

						while(rs.next()){

							userId = rs.getLong(1);
							if(userId >0)
								EO.setId(userId);


						}


					}
				}

			}

			else{
				if(inscnt < 1)
					throw new EVException("ElectionOfficerManager.save: failed");




			}
		}
		catch(Exception e){
			e.printStackTrace();
			throw new EVException("ElectionOfficerManager.save failed: " +e.getMessage());
		}

	}


public List<ElectionsOfficer> restore(ElectionsOfficer EO) throws EVException{

	String 		selectElectionOfficerSql = "select id, type, username, userpass, firstname, lastname, email, address from Users";
	Statement	stmt = null;
	StringBuffer query = new StringBuffer(100);
	StringBuffer condition = new StringBuffer(100);
	List<ElectionsOfficer> electionofficers = new ArrayList<ElectionsOfficer>();

	condition.setLength(0);

	query.append(selectElectionOfficerSql);


	if(EO != null){
		if(EO.getId() >= 0){
			query.append(" where id = " + EO.getId());

			//grab only officer objects from table
			query.append("and type = 'officer'");
		}
		else if(EO.getUserName() != null){
			query.append(" where username = '" + EO.getUserName() + "'");
			//grab only officer objects from table
			query.append(" and type = 'officer'");
		}
		else{
			//grab only officer objects from table
			condition.append(" type = 'officer'");

			if(EO.getPassword() != null)
				if(condition.length() >0)
					condition.append(" and");
				condition.append(" userpass = '" + EO.getPassword() + "'");

			if(EO.getFirstName() != null){
				if(condition.length() > 0)
					condition.append(" and");
				condition.append(" firstname = '" + EO.getFirstName() + "'" );
			}

			if(EO.getLastName() != null){
				if(condition.length()>0)
					condition.append(" and");
				condition.append(" lastname = '" + EO.getLastName() + "'");

			}

			if(EO.getEmailAddress()!= null){
				if(condition.length()>0)
					condition.append(" and");
				condition.append(" email = '" + EO.getEmailAddress() + "'");
			}

			if(EO.getAddress() != null){
				if(condition.length()>0)
					condition.append(" and");
				condition.append(" address = '" + EO.getAddress() + "'");

			}

			if(condition.length() > 0){
				query.append(" where ");
				query.append(condition);

			}




		}

	}
	//object = null, return all electionOfficer objects
	else{
		query.append(" where type = 'officer'");

	}

	try{
		stmt = conn.createStatement();

		if(stmt.execute(query.toString())){

			ResultSet rs = stmt.getResultSet();

			long id;
			String username;
			String password;
			String email;
			String firstName;
			String lastName;
			String address;


			while(rs.next()){

				id = rs.getLong(1);
				username = rs.getString(3);
				password = rs.getString(4);
				firstName = rs.getString(5);
				lastName = rs.getString(6);
				email = rs.getString(7);
				address = rs.getString(8);

				ElectionsOfficer ElectionOfficerObject = objectlayer.createElectionsOfficer(firstName, lastName, username, password, email, address);
				ElectionOfficerObject.setId(id);

				electionofficers.add(ElectionOfficerObject);
			}

			return electionofficers;

		}




	}

	catch(Exception e){
		throw new EVException("ELECOFFICER.restore did not work cause:" + e.getMessage());

	}

	throw new EVException("ElectionOfficerManager.restore did not work");
}


	public void delete(ElectionsOfficer EO) throws EVException{

		String 				deleteElectionOfficerSql = "delete from Users where id = ?";
		PreparedStatement 	stmt = null;
		int 				inscnt;

		if(!EO.isPersistent())
			return;


		try{


			stmt = (PreparedStatement) conn.prepareStatement(deleteElectionOfficerSql);

			stmt.setLong(1, EO.getId());

			inscnt = stmt.executeUpdate();

			if(inscnt < 0)
				throw new EVException("ElectionOfficerManager.delete failed");


		}
		catch(Exception e){
			throw new EVException("ElectionOfficerManager.delete failed: " + e.getMessage());

		}

	}












}



