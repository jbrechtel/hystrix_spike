import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

public class Main {
    public static void main(String[] args) throws Exception {

        Server server = new Server(8080);
        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);
        handler.addServletWithMapping(HystrixMetricsStreamServlet.class, "/*");
        server.start();

        while (true) {
            Thread.sleep(20);
            recover();
            failing();
            sometimesFailing();
        }
    }

    private static void recover() {
        try {
            RecoveringCommand cmd = new RecoveringCommand(50);
            cmd.execute();
        } catch(Exception ex) {

        }
    }

    private static void failing() {
        try {
            FailingCommand cmd = new FailingCommand();
            cmd.execute();
        } catch(Exception ex) {

        }
    }

    private static void sometimesFailing() {
        try {
            SometimesFailingCommand cmd = new SometimesFailingCommand();
            cmd.execute();
        } catch(Exception ex) {

        }
    }
}
