package edu.uga.cs.evote.logic.impl;

import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.ElectoralDistrict;
import edu.uga.cs.evote.object.ObjectLayer;

public class EditElectoralDistrictCtrl {
	
	private ObjectLayer objectLayer = null;
	
	public EditElectoralDistrictCtrl (ObjectLayer objectLayer) {
		this.objectLayer = objectLayer;
	}
	
	public long edit(String oldName, String newName) throws EVException{
		long id = -1;
		
		ElectoralDistrict district = objectLayer.createElectoralDistrict(oldName);
		List<ElectoralDistrict> districts = objectLayer.findElectoralDistrict(district);
		if(!districts.isEmpty()){
			id = districts.get(0).getId();
			district = objectLayer.createElectoralDistrict(newName);
			district.setId(id);
			objectLayer.storeElectoralDistrict(district);
		}
		else{
			throw new EVException("EditElectoralDistrictCtrl: District does not exist.");
		}
		
		return id;
	}

}
