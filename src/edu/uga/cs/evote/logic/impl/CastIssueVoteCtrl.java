

package edu.uga.cs.evote.logic.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Date;


import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Voter;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.object.impl.ObjectLayerImpl;
import edu.uga.cs.evote.entity.ElectoralDistrict;
import edu.uga.cs.evote.entity.Issue;





public class CastIssueVoteCtrl
{
    
    private ObjectLayer objectLayer = null;
    
    boolean yesB = false;
    boolean noB = false;
    
    
    public CastIssueVoteCtrl( ObjectLayer objectModel )
    {
        this.objectLayer = objectModel;
    }
    
    public void Cast( Voter voter, String who )
    throws EVException
    {
    	
    	//String test = "d";
    	char question = '?';
    	char yes = 'y';
    	char no = 'n';
    	String loop = "";
    	String issueId = "";
    	Issue actualIssue = null;
    	
    	if(who.charAt(1) == '?'){
    		
    		//test += "d";
    		issueId = issueId +  who.substring(0,1);
    	}
    	if(who.charAt(2) == '?'){
    		
    		//test += "d";
    		issueId= issueId+ who.substring(0,2);
    	}
    	if(who.charAt(3) == '?'){
    		issueId = issueId + who.substring(0, 3);
    		//test += "d";	
    	}
    	/*
       for(int i = 0; i < who.length(); i++){
    	   
    	   char real = who.charAt(i);
    	   if(real == question){
    		  issueId = who.substring(0, i);
    		  loop = loop + "fkjsdlfjsdljfdsalk";
    		   break;
    	   }
    	   */
    	   
    	   for(int a = 0; a < who.length(); a++){
    		   char bool = who.charAt(a);
    		   if(bool == yes){
    			   yesB = true;
    			   break;
    		   }
    		   if(bool == no){
    				   noB = true;
    				   break;
    		   }
    	   }
    	   
    	  // throw new EVException("?" +issueId + "?" + test);
    	  
    	   Issue modelIssue = objectLayer.createIssue();
    	  Long id= Long.parseLong(issueId);
    	  modelIssue.setId(id);
    	  
    	  List<Issue> issues = objectLayer.findIssue(modelIssue);
    	  
    	  if(!issues.isEmpty()){
    		  actualIssue = issues.get(0);
    		  if(yesB){
    			  actualIssue.addYesVote();
    			  
    		  }
    		  else{
    			 
    			  actualIssue.addVote();
    		  }
    		  
    	  }
    	  else
    		  throw new EVException("could not find issue");  
    	
    	  objectLayer.storeIssue(actualIssue);
    	  
       }
    	

        
    
}


