package edu.uga.cs.evote.presentation;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.logic.LogicLayer;
import edu.uga.cs.evote.session.Session;
import edu.uga.cs.evote.session.SessionManager;
import edu.uga.cs.evote.entity.User;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class DeleteVoterAccount extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	static String templateDir = "WEB-INF/templates";
	static String resultTemplateName = "VoterAccountDeleted.ftl";
	
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
        long	       district_id = 0;
        LogicLayer     logicLayer = null;
        HttpSession    httpSession;
        Session        session;
        String         ssid;
		String		   name = null;
		User user = null;
		String			userName		= null;
		String			firstName = null;
		String			lastName = null;
		

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
        
        user = session.getUser();
        userName = user.getUserName();
        firstName = user.getFirstName();
        lastName = user.getLastName();
       

	
 	


        Map<String, Object> root = new HashMap <String, Object>();
        
        try{
        	
        	logicLayer.deleteVoterAccount(userName);
        }
        catch(EVException e){
            
            StringWriter error = new StringWriter();
            e.printStackTrace(new PrintWriter(error));
            String errors = error.toString();
            EvoteError.error( cfg, toClient,  errors);
        	
        }
        
        root.put("userName", userName);
        root.put("firstName", firstName);
        root.put("lastName", lastName);
        
        
        try{
        	resultTemplate.process(root, toClient);
        	toClient.flush();
        }
        catch(TemplateException e){
        	throw new ServletException("cannot process freemarker");
        }
        
        try{
            logicLayer.logout(ssid);
            httpSession.removeAttribute("ssid");
            httpSession.invalidate();
            }
    	catch(EVException e){
    		e.printStackTrace();		
    	}
        
        
        
        
        
        
        toClient.close();
	}
	
	
}
