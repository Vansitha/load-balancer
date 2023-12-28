import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.io.OutputStream;

class TestServer {
    private static final String USAGE = "Usage: java TestServer <port-number>";
    private static final int DEFAULT_PORT = 8080;
    public static void main(String[] args) {

      int port = DEFAULT_PORT; 

      if (args.length == 0) {
        System.out.println("Port number can be provided as an optional paramater. " + USAGE); 

      } else if (args.length > 1) {
        System.out.println("Only one optional argument is accepted. " + USAGE);

      } else {
        try {
          port = Integer.parseInt(args[0]);

        } catch (NumberFormatException e) {
          System.out.println("Port number is requied to be an integer. " + USAGE); 
        }
      }

      if (port == DEFAULT_PORT) {
        System.out.println("Using default port: " + DEFAULT_PORT);
      }

      initilizeServer(port);

    }
    private static void initilizeServer(int port) {
        try {
            InetAddress localAddress = InetAddress.getByName("localhost");
            HttpServer server = HttpServer.create(new InetSocketAddress(localAddress, port), 0);
            server.createContext("/", new RequestHandler());
            server.start();

            System.out.println("Server started on port: " + port);

        } catch (UnknownHostException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
class RequestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) {

        try {
            String response = "Hello form the backend server";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
            System.out.println("Hello from the backend.");

        } catch (Exception e) {
            System.err.println("Error handling request");
        }
    }
}
