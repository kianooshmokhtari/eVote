/**
 * @file Persistent.java
 * @author Team 9: Joey Bruce. Jack Fisher, Kianoosh Mokhtari, Will Runge
 * Written for Dr. Kochut's CSCI 4050 class, fall 2016.
 */

package edu.uga.cs.evote.persistence.impl;

import edu.uga.cs.evote.persistence.Persistable;
import edu.uga.cs.evote.persistence.PersistenceLayer;


public abstract class Persistent implements Persistable {

	private long id;
	private static PersistenceLayer persistenceLayer;

	public Persistent() {
		this.id = -1;
	}

	public Persistent(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isPersistent() {
		return id >= 0;
	}

	public static PersistenceLayer getPersistenceLayer() {
		return persistenceLayer;
	}

	public static void setPersistenceLayer(PersistenceLayer pLayer) {
		persistenceLayer = pLayer;
	}

}
