import org.apache.derby.drda.NetworkServerControl;
import org.atomicframework.CosmotronServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

public class Cosmotron {
    public static void main (String[] args) throws Exception{
	STGroup group = new STGroupFile("atomic_templates/cosmotron_splash.stg");
	Logger log = Log.getLogger(Cosmotron.class);
	System.out.println(group.getInstanceOf("logo_Standard").render());
	Server s1 = new Server(8080);
 	ServletContextHandler ctx1 = new ServletContextHandler(ServletContextHandler.SESSIONS);
	ctx1.setContextPath("/");
	s1.setHandler(ctx1);
	ServletHolder h1 = new ServletHolder(new CosmotronServlet());
	ServletHolder h2 = new ServletHolder(new DefaultServlet());
        ctx1.addServlet(h1, "/cosmotron/*");
        ctx1.addServlet(h2, "/*");
	h2.setInitParameter("dirAllowed", "true");
	h2.setInitParameter("resourceBase", System.getProperty("user.dir"));
	s1.start();
	log.info(group.getInstanceOf("ready").render());
        s1.join();}}
