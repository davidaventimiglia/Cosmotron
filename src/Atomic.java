import org.apache.derby.drda.NetworkServerControl;
import org.atomicframework.AtomServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

public class Atomic {
    public static void main (String[] args) throws Exception{
	String atomicData = System.getenv("ATOMICDATA");
	STGroup group = new STGroupFile(atomicData + "/atomic_splash.stg");
	Logger log = Log.getLogger(Atomic.class);
	try {(new NetworkServerControl()).start(null);}	catch (Exception e) {System.exit(1);}
	Server s1 = new Server(8180);
	ServletContextHandler ctx1 = new ServletContextHandler(ServletContextHandler.SESSIONS);
	ctx1.setContextPath("/");
	s1.setHandler(ctx1);
	ServletHolder h1 = new ServletHolder(new AtomServlet());
	h1.setInitParameter(AtomServlet.JDBCDRIVER, args[0]);
	h1.setInitParameter(AtomServlet.JDBCURL, args[1]);
	System.out.println(atomicData);
	h1.setInitParameter(AtomServlet.ATOMICDATA, atomicData);
        ctx1.addServlet(h1, "/atomic/*");
	s1.start();
	log.info(group.getInstanceOf("ready").render());
        s1.join();}}
