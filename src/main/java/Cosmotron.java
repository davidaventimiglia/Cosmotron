import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
// import joptsimple.OptionParser;
// import joptsimple.OptionSet;
// import joptsimple.OptionSpec;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;
// import org.eclipse.jetty.server.Server;
// import org.eclipse.jetty.servlet.DefaultServlet;
// import org.eclipse.jetty.servlet.ServletContextHandler;
// import org.eclipse.jetty.servlet.ServletHolder;
// import org.eclipse.jetty.util.log.Log;
// import org.eclipse.jetty.util.log.Logger;

public class Cosmotron {
    public static void main (String[] args) throws Exception{
	// Logger logger = Log.getLogger(Cosmotron.class);
	try {
	    // String cosmotronData = options.valueOf(data);
	    // String contextPath = options.valueOf(context);
	    // Integer webPort = options.valueOf(port);
	    // logger.info("data-directory = " + cosmotronData);
	    // logger.info("port-number = " + webPort);
	    // logger.info("context-path = " + contextPath);
	    // STGroup group = new STGroupFile(cosmotronData + "/templates/cosmotron_splash.stg");
	    Tomcat tomcat = new Tomcat();
	    // tomcat.setPort(webPort);
	    // Context ctx = tomcat.addContext("/"+contextPath, new File(".").getAbsolutePath());
	    // Wrapper w = Tomcat.addServlet(ctx, "cosmotron", new CosmotronServlet());
	    // ctx.addServletMapping("/*", "cosmotron");
	    // w.addInitParameter(CosmotronServlet.COSMOTRONDATA, cosmotronData + "/templates");
	    tomcat.start();
	    // System.out.println(group.getInstanceOf("logo_Standard").render());
	    // ST r = group.getInstanceOf("ready");
	    // r.add("port", webPort);
	    // System.out.println(r.render());
	    tomcat.getServer().await();	}
	catch (Exception t) {
	    t.printStackTrace();
	    // logger.warn(t.getLocalizedMessage());
	    // logger.warn("Cosmotron has encountered a fatal error and is shutting down.");
	    System.exit(1);}}}
