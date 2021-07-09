

package edu.uga.cs.evote.logic.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Voter;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.object.impl.ObjectLayerImpl;
import edu.uga.cs.evote.entity.Candidate;
import edu.uga.cs.evote.entity.Election;
import edu.uga.cs.evote.entity.ElectoralDistrict;





public class CastElectionVoteCtrl
{
    
    private ObjectLayer objectLayer = null;
    
    
    
    public CastElectionVoteCtrl( ObjectLayer objectModel )
    {
        this.objectLayer = objectModel;
    }
    
    public void Cast( Voter voter, String who ) throws EVException{
    
    	char question = '?';
    	String candidateId = "";
    	String electionId = "";
    	int spaces = 0;
    	Election election = null;
    	Candidate candidate = null;
    /*	
    	for(int z = who.length()-1; z >0; z--){
    		char d = who.charAt(z);
    		if(d != question){
    			spaces++;
    		}
    		else
    			break;
    	}
    	*/
    	
    	
    	for(int i = 0; i < who.length(); i++){
    		char c = who.charAt(i);
    		if(c == question){
    			electionId = electionId + who.substring(0, i);
    			for(int a = i +1; a < who.length(); a++){
    				char b = who.charAt(a);
    				if(b == question){
    					
    					candidateId = candidateId + who.substring(i+1, a);
    					break;
    				}
    			}
    			break;
    		}
    		
    	}
    	
    	Election electionModel = objectLayer.createElection();
    	electionModel.setId(Long.parseLong(electionId));
    	Candidate candidateModel = objectLayer.createCandidate();
    	candidateModel.setId(Long.parseLong(candidateId));
    	
    	List<Election> elections = objectLayer.findElection(electionModel);
    	List<Candidate> candidates = objectLayer.findCandidate(candidateModel);
    	
    	if(!elections.isEmpty() && !candidates.isEmpty()){
    		election = elections.get(0);
    		candidate = candidates.get(0);
    		election.addVote();
    		candidate.addVote();
    		
    		objectLayer.storeCandidate(candidate);
    		objectLayer.storeElection(election);
    		
    		
    		
    	}
    	else{
    		throw new EVException("Could not find election or candidate");
    	}
        
    }
}


