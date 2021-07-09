package edu.uga.cs.evote.logic.impl;

import java.sql.*;
import java.util.*;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.ElectoralDistrict;
import edu.uga.cs.evote.object.ObjectLayer;

public class DeleteDistrictCtrl {
	private ObjectLayer objectLayer = null;
	
	public DeleteDistrictCtrl(ObjectLayer objectLayer) {
		this.objectLayer = objectLayer;
	}
	
	public long delete(String name) throws EVException{
		
		ElectoralDistrict district = objectLayer.createElectoralDistrict(name);
		List<ElectoralDistrict> districts = objectLayer.findElectoralDistrict(district);
		if(districts.size() == 0){
			throw new EVException("DeleteDistrictCtrl: District does not exist.");
		}
		else{
			district = districts.get(0);
			List<Ballot> ballots = district.getBallots();
			
			if(ballots.size() > 0){
				throw new EVException("DeleteDistrictCtrl: District has linked ballots.");
			}
			else{
				objectLayer.deleteElectoralDistrict(district);
			}
		}
		
		return district.getId();
	}
}
