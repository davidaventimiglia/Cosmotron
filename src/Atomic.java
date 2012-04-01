import org.apache.derby.drda.NetworkServerControl;
import org.atomicframework.AtomServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

public class Atomic {
    public static void main (String[] args) throws Exception{
	STGroup group = new STGroupFile("atomic_splash.stg");
	System.out.println(group.getInstanceOf("logo_Standard").render());
	System.out.println(group.getInstanceOf("starting").render());
	try {(new NetworkServerControl()).start(null);}	catch (Exception e) {System.exit(1);}
	Server s1 = new Server(8080);
	ServletContextHandler ctx1 = new ServletContextHandler(ServletContextHandler.SESSIONS);
	ctx1.setContextPath("/");
	s1.setHandler(ctx1);
	ServletHolder h1 = new ServletHolder(new AtomServlet());
	h1.setInitParameter(AtomServlet.JDBCDRIVER, args[0]);
	h1.setInitParameter(AtomServlet.JDBCURL, args[1]);
        ctx1.addServlet(h1, "/atomic/*");
	s1.start();
	System.out.println(group.getInstanceOf("ready").render());
        s1.join();}}
