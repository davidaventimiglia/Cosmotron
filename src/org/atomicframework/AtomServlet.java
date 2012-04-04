package org.atomicframework;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;
import org.stringtemplate.v4.AutoIndentWriter;

public class AtomServlet extends HttpServlet {
    public static final long serialVersionUID = 1;
    public static final String JDBCDRIVER = "JDBCDRIVER";
    public static final String JDBCURL = "JDBCURL";

    private ServletConfig config;
    private STGroup serviceDoc;
    private STGroup feed;

    // Public API Methods ------------------------------------------------------

    public void init (ServletConfig config) throws ServletException {
	try {
	    this.config = config;
	    this.loadTemplates();
	    this.setupDB();}
	catch (Exception e) {throw new ServletException(e);}}

    public void doGet (HttpServletRequest request, HttpServletResponse response) 
	throws IOException, ServletException {
	try {
	    this.loadTemplates();
	    String pathInfo = "" + request.getPathInfo();
	    if (pathInfo.split("/").length<=1) generateServiceDocument(request, response);
	    if (pathInfo.split("/").length==3) generateFeed(request, response);}
	catch (Exception e) {throw new ServletException(e);}}
    
    // Private Helper Methods --------------------------------------------------

    private void setupDB () throws ClassNotFoundException {
	Class.forName(this.config.getInitParameter(JDBCDRIVER));}
    
    private void loadTemplates () {
	this.serviceDoc = new STGroupFile("templates/atom_service_document.stg");
	this.feed = new STGroupFile("templates/atom_feed.stg");}

    private void generateServiceDocument (HttpServletRequest request, HttpServletResponse response) 
	throws Exception {
	response.setContentType("application/atomsvc+xml");
	ST t = this.serviceDoc.getInstanceOf("SERVICEDOC");
	t.add("servicedoc", new AtomServiceDocument());
	t.write(new AutoIndentWriter(response.getWriter()));
	response.setStatus(HttpServletResponse.SC_OK);}

    private void generateFeed (HttpServletRequest request, HttpServletResponse response) 
	throws Exception {
	response.setContentType("application/atom+xml");
	ST t = this.feed.getInstanceOf("FEED");
	t.add("feed", new AtomFeed(("" + request.getPathInfo()).split("/")));
	t.write(new AutoIndentWriter(response.getWriter()));
	response.setStatus(HttpServletResponse.SC_OK);}
    
    // Private Helper Inner Classes --------------------------------------------

    private class AtomServiceDocument {
	public List<AtomWorkspace> workspaces = new ArrayList<AtomWorkspace>();
	public AtomServiceDocument () throws Exception {
	    for (ResultSet r = DriverManager
		     .getConnection(config.getInitParameter(JDBCURL))
		     .getMetaData()
		     .getSchemas(); r.next();)
		this.workspaces.add(new AtomWorkspace(r.getString(1)));}}

    private class AtomWorkspace {
	public String title;
	public List<AtomCollection> collections = new ArrayList<AtomCollection>();
	public AtomWorkspace (String workspace) throws Exception {
	    this.title = workspace;
	    for (ResultSet r = DriverManager
		     .getConnection(config.getInitParameter(JDBCURL))
		     .getMetaData()
		     .getTables(null, workspace, null, null); r.next();)
		this.collections.add(new AtomCollection(r.getString(3)));}}

    private class AtomCollection {
	public String title;
	public String href;
	public List<AtomAccept> accepts = new ArrayList<AtomAccept>();
	public List<AtomCategory> categories = new ArrayList<AtomCategory>();
	public AtomCollection (String title) {
	    this.title = title;
	    this.href = title;}}

    private class AtomAccept {
	public String mimetype;
	public AtomAccept (String mimetype) {
	    this.mimetype = mimetype;}}
    
    private class AtomCategory {
	public String schema;
	public String term;
	public AtomCategory (String schema, String term) {
	    this.schema = schema;
	    this.term = term;}}
    
    private class AtomFeed {
	public String title;
	public String href;
	public String updated;
	public String author;
	public String id;
	public List<AtomEntry> entries = new ArrayList<AtomEntry>();
	public AtomFeed (String[] pathInfo) throws Exception {
	    this.title = pathInfo[1];
	    this.href = pathInfo[1];
	    this.id = pathInfo[1];
	    ResultSet r = DriverManager
		.getConnection(config.getInitParameter(JDBCURL))
		.createStatement()
		.executeQuery("select * from $SCHEMA$.$TABLE$"
			      .replace("$SCHEMA$", pathInfo[1])
			      .replace("$TABLE$", pathInfo[2]));
	    ResultSetMetaData rsmd = r.getMetaData();
	    int columnNumber = rsmd.getColumnCount();
	    List<String> columnNames = new ArrayList<String>();
	    for (int i = 1; i<=columnNumber; i++) columnNames.add(rsmd.getColumnName(i));
	    while (r.next()) 
		this.entries.add(new AtomEntry(pathInfo[1]+"."+pathInfo[2], r, columnNames));}}

    private class AtomEntry {
	public String id;
	public String title;
	public String updated;
	public Map<String, String> content;
	public AtomEntry (String id, ResultSet r, List<String> columnNames) throws Exception {
	    this.id = id;
	    this.title = id;
	    this.content = new HashMap<String, String>();
	    for (String name : columnNames) this.content.put(name, r.getString(name));}}}

