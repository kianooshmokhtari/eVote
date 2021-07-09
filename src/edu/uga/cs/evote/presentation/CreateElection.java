package edu.uga.cs.evote.presentation;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.logic.LogicLayer;
import edu.uga.cs.evote.session.Session;
import edu.uga.cs.evote.session.SessionManager;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class CreateElection extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	static String templateDir = "WEB-INF/templates";
	static String resultTemplateName = "ElectionCreated.ftl";
	
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
        long	       id = 0;
        LogicLayer     logicLayer = null;
        HttpSession    httpSession;
        Session        session;
        String         ssid;
		String		   electionBallotID = null;
		String		   office = null;
		String		   isPartisan = null;

		// Load templates from the WEB-INF/templates directory of the Web app.
		try {
			resultTemplate = cfg.getTemplate(resultTemplateName);
		} catch (IOException e) {
			throw new ServletException("CreateElection.doGet: Can't load template in: " + templateDir + ": " + e.toString());
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

        // Get the form parameters
        //
        electionBallotID = req.getParameter( "electionBallotID" );
        if( electionBallotID == null || electionBallotID.length() < 0) {
            EvoteError.error( cfg, toClient, "Invalid ballot ID input." );
            return;
        }
        
        office = req.getParameter( "office" );
        if( office == null || office.length() < 0) {
            EvoteError.error( cfg, toClient, "Invalid office input" );
            return;
        }
        
        isPartisan = req.getParameter( "isPartisan" );
        boolean partisan;
        if( isPartisan.equals("yes") ){
        	partisan = true;
        }
        else if ( isPartisan.equals("no") ){
        	partisan = false;
        }
        else{
        	EvoteError.error( cfg, toClient, "Please select if partisan" );
            return;
        }
        
        try{
        	id = logicLayer.createElection(office, partisan, electionBallotID);
        }
        catch ( Exception e ) {
            EvoteError.error( cfg, toClient, e );
            return;
        }
        
        // Setup the data-model
        //
        Map<String,Object> root = new HashMap<String,Object>();

        // Build the data-model
        //
        root.put( "electionBallotID", new Long( electionBallotID ) );
        root.put( "id", new Long( id ) );

        // Merge the data-model and the template
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
