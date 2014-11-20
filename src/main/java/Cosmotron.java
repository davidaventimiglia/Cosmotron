import java.io.*;
import java.util.*;
import org.apache.catalina.*;
import org.apache.catalina.deploy.*;
import org.apache.catalina.startup.*;

public class Cosmotron {
    public static void main (String[] args) throws Exception {
	try {
	    String atomicUrl = System.getProperty("atomic-url");
	    String httpPort = System.getProperty("http-port");
	    String contextPath = System.getProperty("context-path");
	    System.out.println("atomic-url = " + atomicUrl);
	    System.out.println("port-number = " + httpPort);
	    System.out.println("context-path = " + contextPath);
	    System.out.println(new Scanner(ClassLoader.getSystemResourceAsStream("cosmotron_splash.txt")).useDelimiter("\\Z").next().replace("$PORT$", httpPort));
	    Tomcat tomcat = new Tomcat();
	    tomcat.setPort(Integer.parseInt(httpPort));
	    Context ctx = tomcat.addContext(contextPath, new File(".").getAbsolutePath());
            ContextResource resource = new ContextResource();
	    Wrapper w = Tomcat.addServlet(ctx, "cosmotron", new CosmotronServlet());
	    ctx.addServletMapping("/*", "cosmotron");
	    tomcat.start();
	    tomcat.getServer().await();}
	catch (Exception t) {
	    t.printStackTrace(System.err);
	    System.out.println("Cosmotron has encountered a fatal error and is shutting down.");
	    System.exit(1);}}}
