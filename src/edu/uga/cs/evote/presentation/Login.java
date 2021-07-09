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

import edu.uga.cs.evote.logic.LogicLayer;
import edu.uga.cs.evote.session.Session;
import edu.uga.cs.evote.session.SessionManager;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import edu.uga.cs.evote.entity.User;
import edu.uga.cs.evote.entity.ElectionsOfficer;


public class Login extends HttpServlet {

	private static final long serialVersionUID = 1L;

	static String templateDir = "WEB-INF/templates";
	static String resultTemplateName = "MainWindow.ftl";

	private Configuration cfg;

	public void init() {
		// Prepare the FreeMarker configuration;
		// - Load templates from the WEB-INF/templates directory of the Web app.
		cfg = new Configuration();
		cfg.setServletContextForTemplateLoading(getServletContext(), "WEB-INF/templates");
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)
										throws ServletException, IOException {

		Template		resultTemplate	= null;
		HttpSession		httpSession		= null;
		BufferedWriter	toClient		= null;
		String			username		= null;
		String			password		= null;
		String			ssid			= null;
		Session			session			= null;
		LogicLayer		logicLayer		= null;

		// Load templates from the WEB-INF/templates directory of the Web app.
		try {
			resultTemplate = cfg.getTemplate(resultTemplateName);
		} catch (IOException e) {
			throw new ServletException("Login.doPost: Can't load template in: " + templateDir + ": " + e.toString());
		}

		httpSession = req.getSession();
		ssid = (String) httpSession.getAttribute("ssid");
		if (ssid != null) {
			System.out.println("Already have ssid: " + ssid);
			session = SessionManager.getSessionById(ssid);
			System.out.println("Connection: " + session.getConnection());
		} else System.out.println("ssid is null");

		// Prepare the HTTP response:
		// - Use the charset of template for the output
		// - Use text/html MIME-type
		toClient = new BufferedWriter(new OutputStreamWriter(res.getOutputStream(), resultTemplate.getEncoding()));
		res.setContentType("text/html; charset=" + resultTemplate.getEncoding());

		if (session == null) {
			try {
				session = SessionManager.createSession();
			} catch (Exception e) {
				EvoteError.error(cfg, toClient, e);
				return;
			}
		}

		logicLayer = session.getLogicLayer();

		// Get the parameters
		username = req.getParameter("username");
		password = req.getParameter("password");

		if (username == null || password == null) {
			EvoteError.error(cfg, toClient, "Missing user name or password");
			return;
		}

		try {
			ssid = logicLayer.login(session, username, password);
			System.out.println("Obtained ssid: " + ssid);
			httpSession.setAttribute("ssid", ssid);
			System.out.println("Connection: " + session.getConnection());
		} catch (Exception e) {
			EvoteError.error(cfg, toClient, e);
			return;
		}

		/*
		// Setup the data-model
		Map<String, String> root = new HashMap<String, String>();

		// Build the data-model
		root.put("username", username);


		// Merge the data-model and the template
		try {
			resultTemplate.process(root, toClient);
			toClient.flush();
		} catch (TemplateException e) {
			throw new ServletException( "Error while processing FreeMarker template", e);
		}

		toClient.close(); */

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
		username = user.getUserName();

		if (user instanceof ElectionsOfficer)
			resultTemplateName = "MainWindowOfficer.ftl";
		else
			resultTemplateName = "MainWindowVoter.ftl";

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

		// Get the parameters
		//
		// no params here

		// Setup the data-model
		Map<String, String> root = new HashMap<String, String>();

		// Build the data-model
		root.put("username", username);

		// Merge the data-model and the template
		try {
			resultTemplate.process(root, toClient);
			toClient.flush();
		} catch (TemplateException e) {
			throw new ServletException( "Error while processing FreeMarker template", e);
		}

		toClient.close();

	}

}
