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

public class EditVoterProfile extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	static String templateDir = "WEB-INF/templates";
	static String resultTemplateName = "EditProfile.ftl";
	
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
		User user = null;
		String			userName		= null;
		String			firstName 		= null;
		String			lastName		= null;
		String 			age				=null;
		String			address			= null;
		String 			email 			= null;
		String			electoralDistrictName = null;
		String			district			= null;
		String			districtNum = null;
		String			password = null;
		String 			vpassword = null;

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
        
        firstName = req.getParameter("editFirstName");
        lastName = req.getParameter("editLastName");
        age 	= req.getParameter("editAge");
        address = req.getParameter("editAddress");
        districtNum = req.getParameter("editDistrict");
        email 		= req.getParameter("editEmail");
        password 	= req.getParameter("editPassword");
        vpassword 	= req.getParameter("editPasswordVerify");
        user = session.getUser();
        userName = user.getUserName();
        
        Map<String,Object> root = new HashMap<String,Object>();
        
        
        if(firstName != null ){
        	
        	try{
        		
        		logicLayer.editVoterFirstName(userName, firstName);
        		
        		
        	}
        	catch(Exception e){
        		EvoteError.error(cfg, toClient, "could not change first name");
        	}
        	
        	root.put("editItem", "First Name");
        	root.put("name", firstName);
        	
        	try {
                resultTemplate.process( root, toClient );
                toClient.flush();
            } 
            catch (TemplateException e) {
                throw new ServletException( "Error while processing FreeMarker template", e);
            }

            toClient.close();
        	
        }
        
        else if(lastName != null ){
        	
        	try{
        		
        		logicLayer.editVoterLastName(userName, lastName);
        		
        		
        	}
        	catch(Exception e){
        		EvoteError.error(cfg, toClient, "could not change last name");
        	}
        	
        	root.put("editItem", "Last Name");
        	root.put("name", lastName);
        	
        	try {
                resultTemplate.process( root, toClient );
                toClient.flush();
            } 
            catch (TemplateException e) {
                throw new ServletException( "Error while processing FreeMarker template", e);
            }

            toClient.close();
        	
        }
        
        else if(age != null ){
        	
        	try{
        		
        		logicLayer.editVoterAge(userName, age);
        		
        		
        	}
        	catch(Exception e){
        		EvoteError.error(cfg, toClient, "could not change age");
        	}
        	
        	root.put("editItem", "Age");
        	root.put("name", age);
        	
        	try {
                resultTemplate.process( root, toClient );
                toClient.flush();
            } 
            catch (TemplateException e) {
                throw new ServletException( "Error while processing FreeMarker template", e);
            }

            toClient.close();
        	
        }

else if(address != null ){
	
	try{
		
		logicLayer.editVoterAddress(userName, address);
		
		
	}
	catch(Exception e){
		EvoteError.error(cfg, toClient, "could not change address");
	}
	
	root.put("editItem", "Address");
	root.put("name", address);
	
	try {
        resultTemplate.process( root, toClient );
        toClient.flush();
    } 
    catch (TemplateException e) {
        throw new ServletException( "Error while processing FreeMarker template", e);
    }

    toClient.close();
	
}
        
else if(email != null ){
        	
	 boolean hasAt = false;
     boolean hasDot = false;

     for(int i = 0; i < email.length(); i++){
   	  if(email.charAt(i) == '@'){
   		  hasAt = true;
   		  for(int g = i; g <email.length() ; g++){

   			  if(email.charAt(g) == '.'){
   				  hasDot = true;
   			  }

   		  }
   	  }

     }
     //email check
     if(!hasDot || !hasAt){
   	  EvoteError.error(cfg, toClient, "Email address is not in the proper format");
     }
	
	
	
        	try{
        		
        		logicLayer.editVoterEmail(userName, email);
        		
        		
        	}
        	catch(Exception e){
        		EvoteError.error(cfg, toClient, "could not change email");
        	}
        	
        	root.put("editItem", "Email");
        	root.put("name", email);
        	
        	try {
                resultTemplate.process( root, toClient );
                toClient.flush();
            } 
            catch (TemplateException e) {
                throw new ServletException( "Error while processing FreeMarker template", e);
            }

            toClient.close();
        	
        }

else if(districtNum != null ){
	
	//districtNum = districtNum.trim();
	
	district = "District " + districtNum;
	
	
	try{
		
		logicLayer.editVoterDistrict(userName, district);
		
		
	}
	catch(Exception e){
		StringWriter error = new StringWriter();
		e.printStackTrace(new PrintWriter(error));
		String errors = error.toString();  
	  EvoteError.error( cfg, toClient,  errors + district);
	          

	}
	
	root.put("editItem", "District");
	root.put("name", district);
	
	try {
        resultTemplate.process( root, toClient );
        toClient.flush();
    } 
    catch (TemplateException e) {
        throw new ServletException( "Error while processing FreeMarker template", e);
    }

    toClient.close();
	
}

else if(password != null && vpassword != null){
	
	if(password.isEmpty() || vpassword.isEmpty())
		EvoteError.error(cfg, toClient, "Password or Veirfy Password not entered");
	
	if(!password.equals(vpassword)){
		
		EvoteError.error(cfg, toClient, "Passwords do not match");
	}
	
	try{
		
		logicLayer.resetPassword(userName, password);
	}
	catch(Exception e){
		EvoteError.error(cfg, toClient, "Could not reset Password");
		
	}
	
	root.put("editItem", "Password");
	root.put("name", password);
	
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
}