package edu.uga.cs.evote.session;

/*******************************************
 * Based on the modified code from Matthew Eavenson
 *
 * @file    Session.java
 * @author  Matthew Everson
 * @see     LICENSE (MIT style license file)
 * @version 0.8
 * @date    Sat May 2 18:00:51 EDT 2011
 */

import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;

//import org.apache.log4j.Logger;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.User;
import edu.uga.cs.evote.entity.ElectionsOfficer;
import edu.uga.cs.evote.logic.LogicLayer;
import edu.uga.cs.evote.logic.impl.LogicLayerImpl;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.object.impl.ObjectLayerImpl;
import edu.uga.cs.evote.persistence.PersistenceLayer;
import edu.uga.cs.evote.persistence.impl.PersistenceLayerImpl;


/***************************************************************
 * This class creates a new session and handle different
 * operations related to session.
 */
public class Session extends Thread {

	private Connection	conn;
	private ObjectLayer	objectLayer;
	private LogicLayer	logicLayer;
	private User		user;
	private String		id;
	private Date		expiration;
	// private static Logger log = SessionManager.getLog();

	/***********************************************************
	 * Constructs a new session for a current connection
	 * @param conn
	 */
	public Session(Connection conn) {
		this.conn = conn;
		objectLayer = new ObjectLayerImpl();
		PersistenceLayer persistence = new PersistenceLayerImpl(conn, objectLayer);
		objectLayer.setPersistence(persistence);
		logicLayer = new LogicLayerImpl(objectLayer);
		extendExpiration();
	}

	/***********************************************************
	 * Gets the JDBC connection information for the current session.
	 * @return current connection
	 */
	public Connection getConnection() {
		extendExpiration();
		return conn;
	}

	/***********************************************************
	 * Gets the GVUser for which the session is created.
	 * @return the loggedIn user
	 */
	public User getUser() {
		extendExpiration();
		return user;
	}

	/***********************************************************
	 * Sets the loggedIn user to the new created session.
	 * @param  user the user to be associated with the session.
	 */
	public void setUser(User user) throws EVException {
		extendExpiration();
		this.user = user;
	}

	/***********************************************************
	 * Gets the id for the current session.
	 * @return the id for the current session
	 */
	public String getSessionId() {
		extendExpiration();
		return id;
	}

	/***********************************************************
	 * Sets the id for the current session.
	 * @param id The String id for the session
	 */
	public void setSessionId(String id) {
		this.id = id;
	}

	/***********************************************************
	 * Gets the Date/time at which the session will expire
	 * @return expiration date of this session
	 */
	public Date getExpiration() {
		return expiration;
	}

	/***********************************************************
	 * Sets the Date/time at which the session will expire
	 * @param expiration the expiration date for this session
	 */
	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}

	/***********************************************************
	 * Extends the expiration time for the session by 10 mins
	 */
	private void extendExpiration() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MINUTE, 10);
		this.expiration = c.getTime();
	}

	public ObjectLayer getObjectLayer() {
		return objectLayer;
	}

	/***********************************************************
	 * Runs the new session as a thread
	 */
	@Override
	public void run() {
		long diff = expiration.getTime() - System.currentTimeMillis();
		while (diff > 0) {
			//System.out.println("Session not expired. Sleeping for "+(diff/1000)+" seconds...");
			try {
				sleep(diff);
			} catch (Exception e) {
				//e.printStackTrace();
				break;
			}
			diff = expiration.getTime() - System.currentTimeMillis();
		}
		//System.out.println("Removing " + usr.name + "'s session");
		try {
			SessionManager.removeSession(this);
		} catch (EVException e) {
			// log.error( e.toString(), e );
			try {
				throw e;
			} catch (EVException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * @return the logicLayer
	 */
	public LogicLayer getLogicLayer() {
		return logicLayer;
	}

	/**
	 * @param logicLayer the logicLayer to set
	 */
	public void setLogicLayer(LogicLayer logicLayer) {
		this.logicLayer = logicLayer;
	}

}
