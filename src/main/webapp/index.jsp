<%@ page import "java.io.*"%>
<%@ page import "java.net.*"%>
<%@ page import "java.util.*"%>
<%@ page import "javax.xml.bind.*"%>
<%@ page import "org.apache.olingo.odata2.api.commons.*"%>
<%@ page import "org.apache.olingo.odata2.api.edm.*"%>
<%@ page import "org.apache.olingo.odata2.api.ep.*"%>
<%@ page import "org.apache.olingo.odata2.api.ep.feed.*"%>
<%@ page import "org.apache.olingo.odata2.api.exception.*"%>
<%@ page import "org.neptunestation.calutron.commands.*"%>
<%@ page import "org.neptunestation.calutron.model.*"%>

<%!
public Edm readEdm (final String serviceUri, 
                    final String username, 
                    final String password) throws IOException, ODataException {
    return EntityProvider.readMetadata(call(serviceUri + "/" + METADATA, 
                                            CONTENT_TYPE, 
                                            HTTP_METHOD_GET, 
                                            username, 
                                            password), false);}
%>
    
<%!
public ODataFeed readFeed (final Edm edm, 
                           final String serviceUri, 
                           final EdmEntityContainer entityContainer, 
                           final EdmEntitySet entitySet, 
                           final String username, 
                           final String password) throws IOException, ODataException {
    return EntityProvider.readFeed(CONTENT_TYPE, 
                                   entitySet, 
                                   call(serviceUri + "/public" + "." + entitySet.getName(), 
                                        CONTENT_TYPE, 
                                        HTTP_METHOD_GET, 
                                        username, 
                                        password), 
                                   EntityProviderReadProperties.init().build());}
%>

<%!
public void setEdm (final Edm edm) {
    this.edm = edm;}
>%

<%!
public Edm getEdm () {
    return this.edm;}
%>

<%!
protected InputStream call (final String relativeUri, 
                            final String contentType, 
                            final String httpMethod, 
                            final String username, 
                            final String password) throws IOException {
    HttpURLConnection connection = initializeConnection(relativeUri, contentType, httpMethod, username, password);
    connection.connect();
    checkStatus(connection);
    InputStream content = connection.getInputStream();
    return content;}
%>

<%!
protected HttpURLConnection connect (final String relativeUri, 
                                     final String contentType, 
                                     final String httpMethod, 
                                     final String username, 
                                     final String password) throws IOException {
    HttpURLConnection connection = initializeConnection(relativeUri, contentType, httpMethod, username, password);
    connection.connect();
    checkStatus(connection);
    return connection;}
%>

<%!
protected HttpURLConnection initializeConnection (final String absoluteUri, 
                                                  final String contentType, 
                                                  final String httpMethod, 
                                                  final String username, 
                                                  final String password) throws MalformedURLException, IOException {
    URL url = new URL(absoluteUri);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod(httpMethod);
    connection.setRequestProperty(HTTP_HEADER_ACCEPT, contentType);
    String creds = String.format("%s:%s", username, password);
    String base64 = DatatypeConverter.printBase64Binary(creds.getBytes());
    connection.setRequestProperty("Authorization", "Basic " + base64);
    if (HTTP_METHOD_POST.equals(httpMethod) || HTTP_METHOD_PUT.equals(httpMethod)) {
        connection.setDoOutput(true);
        connection.setRequestProperty(HTTP_HEADER_CONTENT_TYPE, contentType);}
    return connection;}
%>

<%!
protected HttpStatusCodes checkStatus (final HttpURLConnection connection) throws IOException {
    HttpStatusCodes httpStatusCode = HttpStatusCodes.fromStatusCode(connection.getResponseCode());
    if (400 <= httpStatusCode.getStatusCode() && httpStatusCode.getStatusCode() <= 599) 
        throw new RuntimeException("Http Connection failed with status " + httpStatusCode.getStatusCode() + " " + httpStatusCode.toString());
    return httpStatusCode;}
%>


<%
try {readEdm(
                                                                                            getContext().getSetting("user"),
                                                                                            getContext().getSetting("password")));}
catch (Throwable t) {try {System.console().printf("%s\n", "Error performing operation");} catch (Throwable t2) {}}}}


SortedSet<String> names = new TreeSet<String>();
try {
    for (EdmEntitySet e : ((Calutron)getContext().getState()).getEdm().getEntitySets()) names.add(e.getName());
    for (String name : names) System.console().printf("%s\n", name);}
catch (Throwable t) {try {System.console().printf("%s\n", "Error performing operation");} catch (Throwable t2) {}}}}



%>

<html>
  <head>
    <title>Cosmotron</title>
  </head>
  <body>
    <h1>Cosmotron<h1>
  </body>
</html>
