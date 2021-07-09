package edu.uga.cs.evote.presentation;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.logic.LogicLayer;
import edu.uga.cs.evote.session.Session;
import edu.uga.cs.evote.session.SessionManager;
import edu.uga.cs.evote.entity.*;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FindBallotsToVote extends HttpServlet{

	private static final long serialVersionUID = 1L;
	static String templateDir = "WEB-INF/templates";
	static String resultTemplateName = "ListVoterBallots.ftl";

	private Configuration cfg;

	public void init() {

		// Prepare the FreeMarker configuration;
		// - Load templates from the WEB-INF/templates directory of the Web app.
		cfg = new Configuration();
		cfg.setServletContextForTemplateLoading(getServletContext(), "WEB-INF/templates");
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		Template       resultTemplate = null;
          BufferedWriter toClient = null;
          long	          party_id = 0;
          LogicLayer     logicLayer = null;
          HttpSession    httpSession;
          Session        session;
          String         ssid;
          Calendar current = Calendar.getInstance();
          

     // Load templates from the WEB-INF/templates directory of the Web app.
     		try {
     			resultTemplate = cfg.getTemplate(resultTemplateName);
     		} catch (IOException e) {
     			throw new ServletException("FindAllParties.doGet: Can't load template in: " + templateDir + ": " + e.toString());
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

        String output = "";
       /*
        ElectoralDistrict ED;
        List<Ballot> ballots;
        Ballot singleBallot;
        List<BallotItem>
        */
        String ballotInfo = "";
        boolean printSubmit = true;
        boolean method = true;
        boolean printRight = true;

        try{
        ElectoralDistrict ED = voter.getElectoralDistrict();

        List<VoteRecord> vRecord = logicLayer.findVoteRecord(voter);
        
       

        ballotInfo += "<h4>" + ED.getName() +"<h4>" + "\n";

         List<Ballot> ballots = ED.getBallots();

         String names = "";

        // ballotInfo += "<h4> WE GOT BALLOTS ON BALLOTS </h4> \n\n";
         //ballotInfo += "<h4> more ballots </h4> \n\n";

         //if(ballots.size() < 1)
        	//ballotInfo += "<h4> we aint go no ballots test </h4> \n";
         //else{
        	// ballotInfo+= "<h4>we got ballots</h4>";

        	 //if(ballots.size() < 1)
        		// ballotInfo+= "<h4>THE SIZE OF BALLOT IS 0 </h4>";

        for(int i = 0; i < ballots.size(); i++){
        	//get dates and compare
        	
        	
        	printSubmit = true;
        	method = true;
        	printRight = true;

        	Ballot singleBallot = ballots.get(i);
        	
        	Date openDate = singleBallot.getOpenDate();
        	Date closeDate = singleBallot.getCloseDate();
        	String openD = openDate.toString();
        	String closeD = closeDate.toString();
        	int [] open = new int[3];
        	int [] close = new int[3];
        	open[0] = Integer.parseInt(openD.substring(0, 4));
        	open[1] = Integer.parseInt(openD.substring(5, 7));
        	open[2] = Integer.parseInt(openD.substring(8, 10));
        	close[0] = Integer.parseInt(closeD.substring(0, 4));
        	close[1] = Integer.parseInt(closeD.substring(5, 7));
        	close[2] = Integer.parseInt(closeD.substring(8, 10));
        	Calendar openday = Calendar.getInstance();
            Calendar closeday = Calendar.getInstance();
            openday.set(open[0], open[1], open[2]);
            closeday.set(close[0], close[1], close[2]);
        	
          
        	//output += openday.toString() +"\n"+ closeday.toString() +"\n";
        	
        	
        	
        	//names = names + openD + "???" + closeD;
        	
        	List<BallotItem> items = singleBallot.getBallotItems();


        	boolean post = true;

        	for(int g = 0; g < vRecord.size(); g++){
        		//check ballot ID with all voteRecord voterID to see if voted for or not
        		if(singleBallot.getId() == vRecord.get(g).getBallot().getId()){
        			post = false;
        			printSubmit = false;
        		}
        	}
        		ballotInfo += "<h2> Ballot " + singleBallot.getId() + "<h2> \n";
        		if(current.before(openday) || current.after(closeday) ){
        			
        			ballotInfo += "<h4> Ballot is closed <h4> \n";
        			printSubmit = false;
        			
        			
        		}
        		
        		else{

        		if(post){
        	//ballotInfo += "<h2> Ballot " + singleBallot.getId() + "<h2> \n";
        	//List<BallotItem> items = singleBallot.getBallotItems();

        	//
        	//list all elections
        	for(int a = 0; a < items.size(); a++){
        		if(items.get(a) instanceof Election){
        			method = false;
        			Election election = (Election) items.get(a);
        			ballotInfo += "<h3> Election <h3> \n";
        			ballotInfo +="<h3> Office: "+ election.getOffice() + "</h3> \n";
        			List<Candidate> candidate = election.getCandidates();
        			ballotInfo += "<form method=post action=\"CastBallot\"> \n";
        			for(int b = 0; b < candidate.size(); b++){
        				ballotInfo +="<input type =\"radio\" name = \"election"+ election.getId()+"\""+ " value =\"" + election.getId() + "?"
        			+ candidate.get(b).getId()+"?" + singleBallot.getId()+"\">" + candidate.get(b).getName(); 
        				if(election.getIsPartisan()){
        					
        					PoliticalParty party = candidate.get(b).getPoliticalParty();
        					String pparty = party.getName();
        					ballotInfo += "  " +pparty + "<br>\n";
        				}
        				else{
        					ballotInfo +=  "<br>\n";
        				}


        			}
        		}
        	}
        		//ballotInfo += "<p><input type=submit>  \n";
        		//ballotInfo += "</form> \n";
        		}
        		else{

        			ballotInfo += "<h4> Already Voted on this Ballot</h4>";
        			printSubmit = false;
        			printRight = false;
        		}

        		boolean postIssue = true;

        	ballotInfo += "\n\n";
        	//list all Issues


        	boolean doPrint = true;

        	for(int c = 0; c < vRecord.size(); c++){
        		//check ballot ID with all voteRecord voterID to see if voted for or not
        		if(singleBallot.getId() == vRecord.get(c).getBallot().getId()){
        			postIssue = false;
        		}
        	}
        	if(postIssue){
        	for(int a = 0; a<items.size(); a++){
        		if(items.get(a) instanceof Issue){
        			if(doPrint){
        				ballotInfo += "<h3> Issues:  </h3> \n";
        				doPrint = false;
        			}
        			Issue issue = (Issue) items.get(a);
        			if(method){
        				ballotInfo += "<form method=post action=\"CastBallot\"> \n";
        			}

        			ballotInfo += "<h4> " + issue.getQuestion() + "<h4> \n";
        			ballotInfo +="<input type =\"radio\" name = \"issue" +issue.getId()+"\"" +" value =\"" + issue.getId() + "?"
                			+ "yes" + singleBallot.getId() + "\">" + "yes" + "<br>\n";
        			ballotInfo +="<input type =\"radio\" name = \"issue" +issue.getId()+"\"" +" value =\"" + issue.getId() + "?"
                			+ "no" + singleBallot.getId()+ "\">" + "no" + "<br>\n";

        		}

        	}
        	}
        	else{
        		if(printRight){
        		ballotInfo+= "<h4>Already have voted on this Ballot </h4> \n";
        		}
        	}

        	}
        		if(printSubmit){
        ballotInfo += "<p><input type=submit>  \n";
		ballotInfo += "</form> \n";
        		}
        }
      //  throw new EVException(names);
	}
	


    catch(Exception e){
    	 StringWriter error = new StringWriter();
    		e.printStackTrace(new PrintWriter(error));
    		String errors = error.toString();  
    	  EvoteError.error( cfg, toClient,  errors);
             

    	
    }
/*
        try{

        	ballots = logicLayer.findBallots(ED);

        }
        catch (EVException e){
        	EvoteError.error( cfg, toClient, "Could not create list." );
            return;
        }
        */

     // Setup the data-model
        //
        Map<String,Object> root = new HashMap<String,Object>();

        // Build the data-model
        //
        root.put( "ballots", ballotInfo );

        // Merge the data-model and the template
        //
        try {
            resultTemplate.process( root, toClient );
            toClient.flush();
        }
        catch (TemplateException b) {
            throw new ServletException( "Error while processing FreeMarker template", b);
        }

        toClient.close();

	}
}
