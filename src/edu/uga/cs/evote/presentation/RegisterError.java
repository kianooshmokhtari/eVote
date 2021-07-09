// Gnu Emacs C++ mode:  -*- Java -*-
//
// Class:	EvoteError
//
// Type:	Servlet utility class
//
// @author Team 9. Adapted from code by K.J. Kochut

package edu.uga.cs.evote.presentation;

import java.io.BufferedWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import freemarker.template.Configuration;
import freemarker.template.Template;


public class RegisterError {

	static String errorTemplateName = "EvoteError-Register.ftl";

	public static void error(Configuration cfg, BufferedWriter toClient, Exception e)
													throws ServletException {
		error(cfg, toClient, e.toString());
	}

	public static void error(Configuration cfg, BufferedWriter toClient, String msg)
													throws ServletException {
		Template errorTemplate = null;
		Map<String, String> root = new HashMap<String, String>();

		// Load the  Registration error template from the WEB-INF/templates directory of the Web app
		try {
			errorTemplate = cfg.getTemplate( errorTemplateName );
		} catch(Exception e) {
			throw new ServletException( "Can't load template: " + errorTemplateName + ": " + e.toString() );
		}

		root.put("reason", msg);
		
		root.put("followup", "<p></p><p>Back to the <a href=\"CreateVoter.html\"> Registration window</a></p>");

		try {
			errorTemplate.process( root, toClient );
			toClient.flush();
			toClient.close();
		} catch( Exception e ) {
			throw new ServletException( "Error while processing FreeMarker template", e);
		}

		return;

	}

}
