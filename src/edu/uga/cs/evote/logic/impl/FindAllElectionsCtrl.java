package edu.uga.cs.evote.logic.impl;

import java.sql.*;
import java.util.*;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Election;
import edu.uga.cs.evote.object.ObjectLayer;

public class FindAllElectionsCtrl {
	
	private ObjectLayer objectLayer = null;
	
	public FindAllElectionsCtrl(ObjectLayer objectLayer) {
		this.objectLayer = objectLayer;
	}
	
	public List<String> find() throws EVException {
		
		List<Election> elections = objectLayer.findElection(null);
		List<String> result = new ArrayList<String>();
		
		for (int i = 0; i < elections.size(); i++) {
			result.add( elections.get(i).toString() );
		}
		
		return result;
		
	}
	
}
