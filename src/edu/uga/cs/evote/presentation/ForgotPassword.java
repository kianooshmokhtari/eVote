package edu.uga.cs.evote.presentation;

import java.io.*;
import java.sql.Connection;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.logic.LogicLayer;
import edu.uga.cs.evote.logic.impl.LogicLayerImpl;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.object.impl.ObjectLayerImpl;
import edu.uga.cs.evote.persistence.PersistenceLayer;
import edu.uga.cs.evote.persistence.impl.DbUtils;
import edu.uga.cs.evote.persistence.impl.PersistenceLayerImpl;
import edu.uga.cs.evote.persistence.*;
import edu.uga.cs.evote.session.Session;
import edu.uga.cs.evote.session.SessionManager;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class ForgotPassword extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	static String templateDir = "WEB-INF/templates";
	static String resultTemplateName = "ForgotPassword.ftl";
	ObjectLayer object;
	LogicLayer logicLayer;
	private Configuration  cfg;
	private static Connection conn;

	
	public void init() {

		// Prepare the FreeMarker configuration;
		// - Load templates from the WEB-INF/templates directory of the Web app.
		cfg = new Configuration();
		cfg.setServletContextForTemplateLoading(getServletContext(), "WEB-INF/templates");
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		Template       resultTemplate = null;
        BufferedWriter toClient = null;
        LogicLayer     logicLayer = null;
        HttpSession    httpSession;
        Session        session;
        String         ssid;
		String		   userName = null;
		String		   newPassword= null;
		String 		   newPasswordVerify = null;	
		

		// Load templates from the WEB-INF/templates directory of the Web app.
		try {
			resultTemplate = cfg.getTemplate(resultTemplateName);
		} catch (IOException e) {
			throw new ServletException("ForgotPassword.doGet: Can't load template in: " + templateDir + ": " + e.toString());
		}

		// Prepare the HTTP response:
        // - Use the charset of template for the output
        // - Use text/html MIME-type
        //
        toClient = new BufferedWriter(
                new OutputStreamWriter( res.getOutputStream(), resultTemplate.getEncoding() )
                );

        res.setContentType("text/html; charset=" + resultTemplate.getEncoding());

        try{
    		conn = DbUtils.connect();

    	}
    	catch(Exception e){
    	ForgotPasswordError.error(cfg, toClient, "Connection failed");

    	}

          /*Create Object Layer
           *Create Persistence with connection and objectLayer as its parameters
           *make that object persistence with persistence created earlier
           *Create LogicLayer with object as parameters
           */
    	object = new ObjectLayerImpl();
    	PersistenceLayer persistence = new PersistenceLayerImpl(conn, object);
    	object.setPersistence(persistence);
    	logicLayer = new LogicLayerImpl(object);
    	
    	if(logicLayer == null){
    		RegisterError.error(cfg, toClient, "logicLayer is null");
    		return;
    	}
    	
    	userName = req.getParameter("user_name");
    	newPassword = req.getParameter("password");
    	newPasswordVerify = req.getParameter("vpassword");
    	
    	if(userName.isEmpty() || newPassword.isEmpty() || newPasswordVerify.isEmpty())
    		ForgotPasswordError.error(cfg, toClient, "One of the field is missing, make sure all field are filled in to reset password.");
    	
    	
    	if(!newPassword.equals(newPasswordVerify) ){
    		ForgotPasswordError.error(cfg, toClient, "Passwords do not match.");
    	}
    	
    	
         
        try{
        	
        	logicLayer.resetPassword(userName, newPassword);
        	
        }
        catch(Exception e){
        	ForgotPasswordError.error(cfg, toClient, e.getMessage());
        }
        
        Map<String,Object> root = new HashMap<String,Object>();

        // Build the data-model
        try{
   
        root.put( "User_name", userName );
        root.put( "newPassword", newPassword );
        }
        
  	catch(Exception e){
  	RegisterError.error(cfg, toClient, "error putting markers in ftl");

  }
        
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
