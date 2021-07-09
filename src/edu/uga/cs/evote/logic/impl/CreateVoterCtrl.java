

package edu.uga.cs.evote.logic.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Voter;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.object.impl.ObjectLayerImpl;
import edu.uga.cs.evote.entity.ElectoralDistrict;





public class CreateVoterCtrl
{
    
    private ObjectLayer objectLayer = null;
    
    
    
    public CreateVoterCtrl( ObjectLayer objectModel )
    {
        this.objectLayer = objectModel;
    }
    
    public long createVoter( String userName, String password, String email, String firstName,
                            String lastName, String address, int age, String district )
    throws EVException
    {
        Voter               		voter  = null;
        Voter               		modelVoter  = objectLayer.createVoter(firstName, lastName, userName
										,password, email, address, age ) ;
        ElectoralDistrict 			ED = null;
        ElectoralDistrict 			modelElectoralDistrict = null;
        List<Voter>         		voters  = null;
        List<ElectoralDistrict>		electoralDistricts = null;
        
          //get electoral district id and name
        modelElectoralDistrict = objectLayer.createElectoralDistrict();
        modelElectoralDistrict.setName(district);

	
	if(modelElectoralDistrict == null)
		throw new EVException("model is null");
	
	if(objectLayer == null)
		throw new EVException("object is null");
	   electoralDistricts = objectLayer.findElectoralDistrict(modelElectoralDistrict);
 	      if(electoralDistricts.size()> 0)
    	        ED = electoralDistricts.get(0);
              else{
	
	throw new EVException("Invalid Electoral District Number, please try again with a correct number");
		}
        
        
        

        
        // check if the userName already exists
        //modelVoter = objectLayer.createVoter();
        //modelVoter.setUserName( userName );
        voters = objectLayer.findVoter( modelVoter );
        if( voters.size() > 0 )
            voter = voters.get( 0 );
        
        // check if the person actually exists, and if so, throw an exception
        if( voter != null )
            throw new EVException( "A person with this user name already exists" );
        
        voter = objectLayer.createVoter( firstName, lastName, userName, password, email, address, age);
        objectLayer.storeVoter( voter );
        voter.setElectoralDistrict(ED);
        
        
        return voter.getId();
    }
}


