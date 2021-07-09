package edu.uga.cs.evote.logic.impl;

import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.User;
import edu.uga.cs.evote.entity.ElectoralDistrict;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.session.Session;
import edu.uga.cs.evote.session.SessionManager;

public class CreateElectoralDistrictCtrl {
	
	private ObjectLayer objectLayer = null;
	
	public CreateElectoralDistrictCtrl(ObjectLayer objectLayer) {
		this.objectLayer = objectLayer;
	}
	
	public long create(Session session, String name) throws EVException {
		
		ElectoralDistrict district = objectLayer.createElectoralDistrict(name);
		
		List<ElectoralDistrict> districts = objectLayer.findElectoralDistrict(district);
		if(districts.size() == 0){
			objectLayer.storeElectoralDistrict(district);
		}
		else{
			throw new EVException("CreateElectoralDistrictCtrl: District already exists.");
		}
		
		return district.getId();
		
	}

}
