package edu.uga.cs.evote.presentation;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Voter;
import edu.uga.cs.evote.logic.LogicLayer;
import edu.uga.cs.evote.session.Session;
import edu.uga.cs.evote.session.SessionManager;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class CastBallot extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	static String templateDir = "WEB-INF/templates";
	static String resultTemplateName = "VoteCasted.ftl";
	
	private Configuration cfg;
	
	public void init() {

		// Prepare the FreeMarker configuration;
		// - Load templates from the WEB-INF/templates directory of the Web app.
		cfg = new Configuration();
		cfg.setServletContextForTemplateLoading(getServletContext(), "WEB-INF/templates");
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		Template       resultTemplate = null;
        BufferedWriter toClient = null;
        long	       district_id = 0;
        LogicLayer     logicLayer = null;
        HttpSession    httpSession;
        Session        session;
        String         ssid;
		String		   name = null;
		String 			ballot = "";

		// Load templates from the WEB-INF/templates directory of the Web app.
		try {
			resultTemplate = cfg.getTemplate(resultTemplateName);
		} catch (IOException e) {
			throw new ServletException("CreateElectoralDistrict.doGet: Can't load template in: " + templateDir + ": " + e.toString());
		}

		// Prepare the HTTP response:
        // - Use the charset of template for the output
        // - Use text/html MIME-type
        //
        toClient = new BufferedWriter(
                new OutputStreamWriter( res.getOutputStream(), resultTemplate.getEncoding() )
                );

        res.setContentType("text/html; charset=" + resultTemplate.getEncoding());

        httpSession = req.getSession();
        if( httpSession == null ) {       // assume not logged in!
            EvoteError.error( cfg, toClient, "Session expired or illegal; please log in (1)" );
            return;
        }

        ssid = (String) httpSession.getAttribute( "ssid" );
        if( ssid == null ) {       // not logged in!
            EvoteError.error( cfg, toClient, "Session expired or illegal; please log in (2)" );
            return;
        }

        session = SessionManager.getSessionById( ssid );
        if( session == null ) {
            EvoteError.error( cfg, toClient, "Session expired or illegal; please log in (3)" );
            return; 
        }
        
        logicLayer = session.getLogicLayer();
        if( logicLayer == null ) {
            EvoteError.error( cfg, toClient, "Session expired or illegal; please log in (4)" );
            return; 
        }

       Voter voter = (Voter) session.getUser();
       

       

       int number = 0;
       
       List <String> store = new Vector <String>();
       List <String> storeIssue = new Vector <String>();
       
       for(int i = 1; i < 1000; i ++){
    	   String election = req.getParameter("election" + i);
    		   if(election != null){
    			  store.add(election);
    		   }
    		   
    		   
    	   
       }
       
       for(int i = 1; i < 1000; i++){
    	   String issue = req.getParameter("issue" + i);
    	  if(issue != null)
    		  storeIssue.add(issue);
       }
       
       char d = '?';
       int num = 0;
       String place = "";
       
       if(store.isEmpty() && storeIssue.isEmpty())
    	   EvoteError.error(cfg, toClient, "You need to make at least one selection for the ballot");
       
       if(!storeIssue.isEmpty()){
    	   place = place + storeIssue.get(0);
       for(int z = place.length()-1; z >0; z--){
  			Character b = place.charAt(z);
  			if(Character.isDigit(b)){
  				num++;
  			}
  			else
  				break;
  	}
       }
       else{
    	   place = place + store.get(0);
    	   
    	   for(int z = place.length()-1; z >0; z--){
      			char b = place.charAt(z);
      			if(b != d){
      				num++;
      			}
      			else
      				break;
      	}
    	  
       }
    	   ballot = ballot + place.substring(place.length()-num, place.length());
    	   
    	   //EvoteError.error(cfg, toClient, ballot);
    	   
    	   
       boolean keep = true;

        	try{
        		//throw new EVException(storeIssue.get(0) + "    " + storeIssue.get(1) + "    " + storeIssue.get(2) + "    " + storeIssue.get(3));
        		
        		if(keep){
        	logicLayer.createVoterRecord(voter, ballot);
        		
        		
        		
        		}
        	
        		for(int i = 0; i < storeIssue.size(); i++){
        		
        			logicLayer.castIssueVote(voter, storeIssue.get(i));
        		}
        	
        		for(int x = 0 ; x < store.size(); x++){
        			logicLayer.castElectionVote(voter, store.get(x));
        		}
        	
        		
        	
        	}
        	catch(Exception e){

        	     StringWriter error = new StringWriter();
        		e.printStackTrace(new PrintWriter(error));
        		String errors = error.toString();  
        	  EvoteError.error( cfg, toClient,  errors);

        	}
        //
        Map<String,Object> root = new HashMap<String,Object>();

        // Build the data-model
        //
       
        //
        try {
            resultTemplate.process( root, toClient );
            toClient.flush();
        } 
        catch (TemplateException e) {
            throw new ServletException( "Error while processing FreeMarker template", e);
        }

        toClient.close();

  }
}

