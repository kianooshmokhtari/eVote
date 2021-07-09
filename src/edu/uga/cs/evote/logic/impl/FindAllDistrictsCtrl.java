package edu.uga.cs.evote.logic.impl;

import java.sql.*;
import java.util.*;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.ElectoralDistrict;
import edu.uga.cs.evote.object.ObjectLayer;

public class FindAllDistrictsCtrl {
	
	private ObjectLayer objectLayer = null;
	
	public FindAllDistrictsCtrl(ObjectLayer objectLayer) {
		this.objectLayer = objectLayer;
	}

	public List<String> find() throws EVException {
		
		List<ElectoralDistrict> districts = objectLayer.findElectoralDistrict(null);
		List<String> result = new ArrayList<String>();
		
		for (int i = 0; i < districts.size(); i++) {
			result.add( districts.get(i).toString() );
		}
		
		return result;
		
	}
	
}
