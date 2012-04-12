package org.atomicframework;

import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringEscapeUtils;
import org.stringtemplate.v4.AutoIndentWriter;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

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
	AtomServiceDocument d = this.getServiceDocument();
	if (d!=null) {
	    ST t = this.serviceDoc.getInstanceOf("SERVICEDOC");
	    response.setContentType("application/atomsvc+xml");
	    t.add("servicedoc", d);
	    t.write(new AutoIndentWriter(response.getWriter()));
	    response.setStatus(HttpServletResponse.SC_OK);}
	else response.setStatus(HttpServletResponse.SC_NOT_FOUND);}

    private void generateFeed (HttpServletRequest request, HttpServletResponse response) 
	throws Exception {
	AtomFeed f = this.getFeed(("" + request.getPathInfo()).toUpperCase().split("/"));
	if (f!=null) {
	    ST t = this.feed.getInstanceOf("FEED");
	    response.setContentType("application/atom+xml");
	    t.add("feed", f);
	    t.write(new AutoIndentWriter(response.getWriter()));
	    response.setStatus(HttpServletResponse.SC_OK);}
	else response.setStatus(HttpServletResponse.SC_NOT_FOUND);}

    private DatabaseMetaData getDatabaseMetaData () throws SQLException {
	return DriverManager.getConnection(config.getInitParameter(JDBCURL)).getMetaData();}

    private AtomServiceDocument getServiceDocument () throws Exception {
	return new AtomServiceDocument();}
    
    private AtomFeed getFeed (String[] pathInfo) throws Exception {
	return 
	    this.getDatabaseMetaData().getTables(null, pathInfo[1], pathInfo[2], null).next() ?
	    new AtomFeed(pathInfo) : null;}

    private List<String> getColumnNames (String schema, String table) throws SQLException {
	List<String> columnNames = new ArrayList<String>();
	ResultSet r = getDatabaseMetaData().getColumns(null, schema, table, null);
	while (r.next()) columnNames.add(r.getString(4));
	return columnNames;}

    // Private Helper Inner Classes --------------------------------------------

    private class AtomServiceDocument {
	public List<AtomWorkspace> workspaces = new ArrayList<AtomWorkspace>();
	public AtomServiceDocument () throws Exception {
	    for (ResultSet r = getDatabaseMetaData().getSchemas(); r.next();)
		this.workspaces.add(new AtomWorkspace(StringEscapeUtils.escapeXml(r.getString(1))));}}

    private class AtomWorkspace {
	public String title;
	public List<AtomCollection> collections = new ArrayList<AtomCollection>();
	public AtomWorkspace (String workspace) throws Exception {
	    this.title = workspace;
	    for (ResultSet r = getDatabaseMetaData().getTables(null, workspace, null, null); r.next();)
		this.collections.add(new AtomCollection(StringEscapeUtils.escapeXml(r.getString(3))));}}

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
	    while (r.next()) 
		this.entries.add(new AtomEntry(pathInfo[1]+"."+pathInfo[2], 
					       r, getColumnNames(pathInfo[1], pathInfo[2])));}}

    private class AtomEntry {
	public String id;
	public String title;
	public String updated;
	public Map<String, String> content;
	public AtomEntry (String id, ResultSet r, List<String> columnNames) throws Exception {
	    this.id = id;
	    this.title = id;
	    this.content = new HashMap<String, String>();
	    for (String name : columnNames) 
		this.content.put(name, StringEscapeUtils.escapeXml(r.getString(name)));}}}

