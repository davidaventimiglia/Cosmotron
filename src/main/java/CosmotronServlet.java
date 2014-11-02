import java.io.InputStream;
import java.io.OutputStream;
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

public class CosmotronServlet extends HttpServlet {
    public static final long serialVersionUID = 1;
    public static final String COSMOTRONDATA = "cosmotronData";

    private ServletConfig config;
    // private HttpClient client;
    // private STGroup group;

    // Public API Methods ------------------------------------------------------

    public void init (ServletConfig config) throws ServletException {
	try {
	    this.config = config;
	    this.loadTemplates();}
	    // this.client = new HttpClient();
	    // this.client.setConnectorType(HttpClient.CONNECTOR_SELECT_CHANNEL);
	    // this.client.start();}
	catch (Exception e) {throw new ServletException(e);}}

    public void doGet (HttpServletRequest request, HttpServletResponse response) 
	throws IOException, ServletException {
	try {
	    this.loadTemplates();}
	    // this.generateResponse(request, response);}
	catch (Exception e) {throw new ServletException(e);}}
    
    private void loadTemplates () {
	String cosmotronData = config.getInitParameter(COSMOTRONDATA);}
	// this.group = new STGroupFile(cosmotronData + "/cosmotron.stg");}

    private static void copyStream (InputStream input, OutputStream output) throws IOException {
	byte[] buffer = new byte[1024];
	int bytesRead;
	while ((bytesRead = input.read(buffer))!=-1) output.write(buffer, 0, bytesRead);}}
