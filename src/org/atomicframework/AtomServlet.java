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
    public static final String ATOMICDATA = "atomicData";

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
	    if (pathInfo.split("/").length<=1) writeServiceDocument(request, response);
	    if (pathInfo.split("/").length==3) writeFeed(request, response);}
	catch (Exception e) {throw new ServletException(e);}}

    // Private Helper Methods --------------------------------------------------

    private void setupDB () throws ClassNotFoundException {
	Class.forName(this.config.getInitParameter(JDBCDRIVER));}
    
    private void loadTemplates () {
	String atomicData = config.getInitParameter(ATOMICDATA);
	this.serviceDoc = new STGroupFile(atomicData + "/atom_service_document.stg");
	this.feed = new STGroupFile(atomicData + "/atom_feed.stg");}

    private void writeServiceDocument (HttpServletRequest request, HttpServletResponse response) 
	throws Exception {
	Map d = this.getServiceDocument();
	if (d!=null) {
	    ST t = this.serviceDoc.getInstanceOf("SERVICEDOC");
	    response.setContentType("application/atomsvc+xml");
	    t.add("servicedoc", d);
	    t.write(new AutoIndentWriter(response.getWriter()));
	    response.setStatus(HttpServletResponse.SC_OK);}
	else response.setStatus(HttpServletResponse.SC_NOT_FOUND);}

    private void writeFeed (HttpServletRequest request, HttpServletResponse response) 
	throws Exception {
	Map f = this.getFeed(("" + request.getPathInfo()).toUpperCase().split("/"));
	if (f!=null) {
	    ST t = this.feed.getInstanceOf("FEED");
	    response.setContentType("application/atom+xml");
	    t.add("feed", f);
	    t.write(new AutoIndentWriter(response.getWriter()));
	    response.setStatus(HttpServletResponse.SC_OK);}
	else response.setStatus(HttpServletResponse.SC_NOT_FOUND);}

    private DatabaseMetaData getDatabaseMetaData () throws SQLException {
	return DriverManager.getConnection(config.getInitParameter(JDBCURL)).getMetaData();}

    private Map getServiceDocument () throws Exception {
	Map serviceDoc = new HashMap();
	List workspaces = new ArrayList();
	ResultSet r = getDatabaseMetaData().getSchemas();
	while (r.next()) workspaces.add(getWorkSpace(StringEscapeUtils.escapeXml(r.getString(1))));
	serviceDoc.put("workspaces", workspaces);
	return serviceDoc;}

    private Map getWorkSpace (String id) throws Exception {
	Map workspace = new HashMap();
	List collections = new ArrayList();
	ResultSet r = getDatabaseMetaData().getTables(null, id, null, null);
	while (r.next()) collections.add(getCollection(StringEscapeUtils.escapeXml(r.getString(3))));
	workspace.put("title", id);
	workspace.put("collections", collections);
	return workspace;}

    private Map getCollection (String id) {
	Map collection = new HashMap();
	collection.put("title", id);
	collection.put("href", id);
	collection.put("accepts", new ArrayList());
	collection.put("categories", new ArrayList());
	return collection;}
	
    private Map getFeed (String[] pathInfo) throws Exception {
	Map feed = new HashMap();
	List entries = new ArrayList();
	List columnNames = getColumnNames(pathInfo[1], pathInfo[2]);
	ResultSet r = DriverManager
	    .getConnection(config.getInitParameter(JDBCURL))
	    .createStatement()
	    .executeQuery("select * from $SCHEMA$.$TABLE$"
			  .replace("$SCHEMA$", pathInfo[1])
			  .replace("$TABLE$", pathInfo[2]));
	while (r.next()) entries.add(getEntry(pathInfo[1]+"."+pathInfo[2], r, columnNames));
	feed.put("title", pathInfo[1]);
	feed.put("href", pathInfo[2]);
	feed.put("id", pathInfo[1]);
	feed.put("entries", entries);
	return feed;}

    private Map getEntry (String id, ResultSet r, List<String> columnNames) throws Exception {
	Map entry = new HashMap();
	Map content = new HashMap();
	for (String name : columnNames) content.put(name, StringEscapeUtils.escapeXml(r.getString(name)));
	entry.put("id", id);
	entry.put("title", id);
	entry.put("content", content);
	System.out.println(content);
	return entry;}
	
    private List getColumnNames (String schema, String table) throws SQLException {
	List columnNames = new ArrayList();
	ResultSet r = getDatabaseMetaData().getColumns(null, schema, table, null);
	while (r.next()) columnNames.add(r.getString(4));
	return columnNames;}}


