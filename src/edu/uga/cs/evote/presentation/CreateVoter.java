package edu.uga.cs.evote.presentation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import edu.uga.cs.evote.object.*;
import edu.uga.cs.evote.logic.*;
import edu.uga.cs.evote.presentation.RegisterError;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.*;
import edu.uga.cs.evote.logic.impl.*;
import edu.uga.cs.evote.logic.LogicLayer;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.object.impl.ObjectLayerImpl;
import edu.uga.cs.evote.persistence.impl.PersistenceLayerImpl;
import edu.uga.cs.evote.persistence.impl.DbUtils;
import java.sql.Connection;
import edu.uga.cs.evote.persistence.*;

public class CreateVoter
    extends HttpServlet
{
    private static final long serialVersionUID = 1L;
    static  String         templateDir = "WEB-INF/templates";
    static  String         resultTemplateName = "VoterRegistration.ftl";
	ObjectLayer object;
	LogicLayer logicLayer;
  private Configuration  cfg;
  private static Connection conn;

  public void init()
  {

      cfg = new Configuration();
      cfg.setServletContextForTemplateLoading( getServletContext(), "WEB-INF/templates" );
  }

  public void doPost( HttpServletRequest req, HttpServletResponse res )
          throws ServletException, IOException
  {
//	ObjectLayerImpl obj = new ObjectLayerImpl();
      Template       resultTemplate = null;
      BufferedWriter toClient = null;
      String         user_name = null;
      String         password = null;
      String		 vpassword = null;
      String         first_name = null;
      String	     last_name = null;
      String	     address = null;
      String	     email = null;
      long	     	 person_id = 0;
      String 		 age = null;
      String 		 district = "District ";
      String         dNum = null;
	try{
		conn = DbUtils.connect();

	}
	catch(Exception e){
	RegisterError.error(cfg, toClient, "Connection failed");

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


      try {
          resultTemplate = cfg.getTemplate( resultTemplateName );
      }
      catch (IOException e) {
          throw new ServletException(
                  "Can't load template in: " + templateDir + ": " + e.toString());
      }


      toClient = new BufferedWriter(  new OutputStreamWriter( res.getOutputStream(), resultTemplate.getEncoding() )
              );

      res.setContentType("text/html; charset=" + resultTemplate.getEncoding());


	if(logicLayer == null){
		RegisterError.error(cfg, toClient, "logicLayer is null");
		return;
	}



      // get parameters
      user_name = req.getParameter( "user_name" );
      password = req.getParameter( "password" );
      vpassword = req.getParameter("vpassword");
      email = req.getParameter( "email" );
      first_name = req.getParameter( "first_name" );
      last_name = req.getParameter( "last_name" );
      address = req.getParameter( "address" );
      
      
      dNum = req.getParameter("district");
      dNum = dNum.trim();
      district = district.concat(dNum);
   

      String num = req.getParameter("age");
      int  ageNum = Integer.parseInt(req.getParameter("age"));
      if(district == null)
          RegisterError.error(cfg, toClient, "unspecified district");




      // validate the parameters
      if( user_name == null ) {
          RegisterError.error( cfg, toClient, "Unspecified user name" );
          return;
      }

      if( password == null ) {
          RegisterError.error( cfg, toClient, "Unspecified password" );
          return;
      }

      if(!password.equals(vpassword)){

    	  RegisterError.error(cfg, toClient, "Passwords do not match");
      }

      if( first_name == null ) {
    	  RegisterError.error( cfg, toClient, "Unspecified first name" );
          return;
      }

      if( last_name == null ) {
    	  RegisterError.error( cfg, toClient, "Unspecified last name" );
          return;
      }

      if( address == null )
          address = "";


      if( email == null ) {
    	  RegisterError.error( cfg, toClient, "Unspecified email" );
          return;
      }

      //check email has @ and .
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
    	  RegisterError.error(cfg, toClient, "Email address is not in the proper format");
      }

      try {
	if(logicLayer == null){
	RegisterError.error(cfg, toClient, "Logic Layer is null");

}


     		 person_id = logicLayer.createVoter( user_name, password, email, first_name, last_name, address,ageNum ,district );



		 System.out.println("value of person_id is "+person_id);
      }
      catch ( Exception e ) {
          RegisterError.error(cfg, toClient, e.getMessage());
      }



	String id = Long.toString(person_id);

      // Setup the data-model
      Map<String,Object> root = new HashMap<String,Object>();

      // Build the data-model
      try{
 
      root.put( "first_name", first_name );
      root.put( "last_name", last_name );
      root.put( "person_id",  id  );
      root.put("district", district);
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
