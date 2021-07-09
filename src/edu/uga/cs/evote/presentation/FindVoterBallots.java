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

public class FindVoterBallots extends HttpServlet{

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
       /*
        ElectoralDistrict ED;
        List<Ballot> ballots;
        Ballot singleBallot;
        List<BallotItem>
        */
        String ballotInfo = null;

        try{
        ElectoralDistrict ED = voter.getElectoralDistrict();

        ballotInfo += "<h4>" + ED.getName() +"<h4>" + "\n";

         List<Ballot> ballots = ED.getBallots();



        // ballotInfo += "<h4> WE GOT BALLOTS ON BALLOTS </h4> \n\n";
         //ballotInfo += "<h4> more ballots </h4> \n\n";

         //if(ballots.size() < 1)
        	//ballotInfo += "<h4> we aint go no ballots test </h4> \n";
         //else{
        	// ballotInfo+= "<h4>we got ballots</h4>";

        	 //if(ballots.size() < 1)
        		// ballotInfo+= "<h4>THE SIZE OF BALLOT IS 0 </h4>";

        for(int i = 0; i < ballots.size(); i++){

        	Ballot singleBallot = ballots.get(i);
        	ballotInfo += "<h2> Ballot " + singleBallot.getId() + "<h2> \n";
        	List<BallotItem> items = singleBallot.getBallotItems();

        	//
        	//list all elections
        	for(int a = 0; a < items.size(); a++){
        		if(items.get(a) instanceof Election){
        			Election election = (Election) items.get(a);
        			ballotInfo += "<h3> Election: <h3> \n";
        			ballotInfo +="<h3> Office: "+ election.getOffice() + "<h3> \n";
        			List<Candidate> candidate = election.getCandidates();
        			for(int b = 0; b < candidate.size(); b++){
        				ballotInfo +="<h4>"+ candidate.get(b).getName()+"<h4> \n";


        			}
        		}
        	}

        	ballotInfo += "\n\n";
        	//list all Issues


        	boolean doPrint = true;

        	for(int a = 0; a<items.size(); a++){
        		if(items.get(a) instanceof Issue){
        			if(doPrint){
        				ballotInfo += "<h3> Issues:  <h3> \n";
        				doPrint = false;
        			}
        			Issue issue = (Issue) items.get(a);

        			ballotInfo += "<h4> " + issue.getQuestion() + "<h4> \n";

        		}

        	}



        }
	}
        // throw new EVException(ballotInfo);


    catch(Exception e){
    	EvoteError.error(cfg, toClient, e.getMessage());
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
