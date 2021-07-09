package edu.uga.cs.evote.presentation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.uga.cs.evote.entity.User;
import edu.uga.cs.evote.entity.Voter;
import edu.uga.cs.evote.entity.ElectoralDistrict;
import edu.uga.cs.evote.entity.ElectionsOfficer;
import edu.uga.cs.evote.logic.LogicLayer;
import edu.uga.cs.evote.session.Session;
import edu.uga.cs.evote.session.SessionManager;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;


public class GetProfile extends HttpServlet {

	private static final long serialVersionUID = 1L;

	static String templateDir = "WEB-INF/templates";
	static String resultTemplateName = "Profile.ftl";

	private Configuration cfg;

	public void init() {
		// Prepare the FreeMarker configuration;
		// - Load templates from the WEB-INF/templates directory of the Web app.
		cfg = new Configuration();
		cfg.setServletContextForTemplateLoading(getServletContext(), "WEB-INF/templates");
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res)
										throws ServletException, IOException {

		Template		resultTemplate	= null;
		HttpSession		httpSession		= null;
		BufferedWriter	toClient		= null;
		LogicLayer		logicLayer		= null;
		Voter			voter		 	= null;
		ElectoralDistrict ED			= null;
		String			username		= null;
		String			ssid			= null;
		Session			session			= null;
		String			userName 		= null;
		String			firstName 		= null;
		String			lastName		= null;
		String 			age				=null;
		String			address			= null;
		String 			email 			= null;
		String			electoralDistrictName = null;
		
		

		// Load templates from the WEB-INF/templates directory of the Web app.
		try {
			resultTemplate = cfg.getTemplate( resultTemplateName );
		} catch (IOException e) {
			throw new ServletException("Login.doPost: Can't load template in: " + templateDir + ": " + e.toString());
		}

		// Prepare the HTTP response:
		// - Use the charset of template for the output
		// - Use text/html MIME-type
		toClient = new BufferedWriter(new OutputStreamWriter(res.getOutputStream(), resultTemplate.getEncoding()));
		res.setContentType("text/html; charset=" + resultTemplate.getEncoding());

		httpSession = req.getSession();
		if (httpSession == null) {	// not logged in!
			EvoteError.error(cfg, toClient, "Session expired or illegal; please log in");
			return;
		}

		ssid = (String) httpSession.getAttribute("ssid");
		if (ssid == null) {			// assume not logged in!
			EvoteError.error( cfg, toClient, "Session expired or illegal; please log in" );
			return;
		}

		session = SessionManager.getSessionById(ssid);
		if (session == null) {
			EvoteError.error(cfg, toClient, "Session expired or illegal; please log in");
			return;
		}

		User user = session.getUser();
		if (user == null) {
			EvoteError.error(cfg, toClient, "Session expired or illegal; please log in");
			return;
		}
		
		 logicLayer = session.getLogicLayer();
	        if( logicLayer == null ) {
	            EvoteError.error( cfg, toClient, "Session expired or illegal; please log in (4)" );
	            return; 
	        }
		
		
		
		
		
		userName = user.getUserName();
		
		try{
			
			voter = logicLayer.getProfile(userName);
			
		}
		catch(Exception e){
			EvoteError.error(cfg, toClient, "could not retrieve profile");
			
		}
		
		firstName = voter.getFirstName();
		lastName = voter.getLastName();
		address  = voter.getAddress();
		email 	= voter.getEmailAddress();
		age 	= Integer.toString(voter.getAge());
		
		try{
		ED 		= voter.getElectoralDistrict();
		}
		catch(Exception e){
			EvoteError.error(cfg, toClient, e.getMessage());
		}
		electoralDistrictName = ED.getName();
		
		Map <String, String> root = new HashMap<String, String>();
		
		
		
		
		root.put("userName", userName);
		root.put("firstName", firstName);
		root.put("lastName", lastName);
		root.put("age", age);
		root.put("address", address);
		root.put("email", email);
		root.put("district", electoralDistrictName);
		
		
		try{
			resultTemplate.process(root, toClient);
			toClient.flush();
		}
		catch (TemplateException e) {
            throw new ServletException( "Error while processing FreeMarker template", e);
        }

        toClient.close();
        
	}
		
		
		
	}
	
