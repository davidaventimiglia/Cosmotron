import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;
import org.atomicframework.CosmotronServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

public class Cosmotron {
    public static void main (String[] args) throws Exception{
	Logger logger = Log.getLogger(Cosmotron.class);
	try {
	    OptionParser parser = new OptionParser();
	    OptionSpec<String> data = parser
		.acceptsAll(Arrays.asList("d", "data-directory"))
		.withRequiredArg()
		.ofType(String.class)
		.defaultsTo(System.getenv("COSMOTRONDATA"));
	    OptionSpec<Integer> port = parser
		.acceptsAll(Arrays.asList("p", "port-number"))
		.withRequiredArg()
		.ofType(Integer.class)
		.defaultsTo(8080);
	    OptionSpec<String> context = parser
		.acceptsAll(Arrays.asList("c", "context-path"))
		.withRequiredArg()
		.ofType(String.class)
		.defaultsTo("cosmotron");
	    OptionSpec help = parser
		.accepts("h", "Help");
	    OptionSet options = parser.parse(args);
	    if (options.has("h")) {parser.printHelpOn(System.out); return;}
	    assert options.has(data);
	    assert options.has(port);
	    assert options.has(context);
	    String cosmotronData = options.valueOf(data);
	    String contextPath = options.valueOf(context);
	    Integer webPort = options.valueOf(port);
	    logger.info("data-directory = " + cosmotronData);
	    logger.info("port-number = " + webPort);
	    logger.info("context-path = " + contextPath);
	    STGroup group = new STGroupFile(cosmotronData + "/templates/cosmotron_splash.stg");
	    Tomcat tomcat = new Tomcat();
	    tomcat.setPort(webPort);
	    Context ctx = tomcat.addContext("/"+contextPath, new File(".").getAbsolutePath());
	    Wrapper w = Tomcat.addServlet(ctx, "cosmotron", new CosmotronServlet());
	    ctx.addServletMapping("/*", "cosmotron");
	    w.addInitParameter(CosmotronServlet.COSMOTRONDATA, cosmotronData + "/templates");
	    tomcat.start();
	    System.out.println(group.getInstanceOf("logo_Standard").render());
	    ST r = group.getInstanceOf("ready");
	    r.add("port", webPort);
	    System.out.println(r.render());
	    tomcat.getServer().await();	}
	catch (Exception t) {
	    t.printStackTrace();
	    logger.warn(t.getLocalizedMessage());
	    logger.warn("Cosmotron has encountered a fatal error and is shutting down.");
	    System.exit(1);}}}
