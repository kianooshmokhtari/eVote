package edu.uga.cs.evote.presentation;

import java.io.*;
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

public class CreatePoliticalParty extends HttpServlet{

	private static final long serialVersionUID = 1L;
	static String templateDir = "WEB-INF/templates";
	static String resultTemplateName = "PartyCreated.ftl";

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
        long	       party_id = 0;
        LogicLayer     logicLayer = null;
        HttpSession    httpSession;
        Session        session;
        String         ssid;
		String		   name = null;

		// Load templates from the WEB-INF/templates directory of the Web app.
		try {
			resultTemplate = cfg.getTemplate(resultTemplateName);
		} catch (IOException e) {
			throw new ServletException("CreatePoliticalParty.doGet: Can't load template in: " + templateDir + ": " + e.toString());
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
        name = req.getParameter( "party_name" );

        if( name == null || name.length() < 1) {
            EvoteError.error( cfg, toClient, "Unspecified name" );
            return;
        }

        try {
            party_id = logicLayer.createPoliticalParty(session, name);
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
        root.put( "party_name", name );
        root.put( "party_id", new Long( party_id ) );

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
