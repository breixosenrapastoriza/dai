package es.uvigo.esei.dai.hybridserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

<<<<<<< HEAD
import es.uvigo.esei.dai.hybridserver.http.HTTPParseException;

=======
>>>>>>> 20f869aeb28713db43b52e8edc4954900e2a0543
public class HybridServer implements AutoCloseable {
	private static final int SERVICE_PORT = 8888;
	private Thread serverThread;
	private ExecutorService threadPool;
	private int numClients;
	private int port;
	private boolean stop;

	private Map<String, String> pages;
	private Properties properties;
<<<<<<< HEAD
	private Configuration configuration;
=======
	
>>>>>>> 20f869aeb28713db43b52e8edc4954900e2a0543
	
	
	public HybridServer() {
		// TODO Inicializar con los parámetros por defecto
		this.properties = new Properties();
		this.port = SERVICE_PORT;
		this.numClients = 50;
		this.properties.setProperty("db.url", "jdbc:mysql://localhost:3306/hstestdb");
		this.properties.setProperty("db.user", "hsdb");
		this.properties.setProperty("db.password", "hsdbpass");
	}

<<<<<<< HEAD
	public HybridServer(Configuration configuration) {
		this.configuration = configuration;
	}

=======
>>>>>>> 20f869aeb28713db43b52e8edc4954900e2a0543
	public HybridServer(Map<String, String> pages) {
		// TODO Inicializar con la base de datos en memoria conteniendo "pages"
		this.pages = pages;
		this.port = SERVICE_PORT;
		this.numClients = 50;
	}

	public HybridServer(Properties properties) {
		// TODO Inicializar con los parámetros recibidos
		this.properties = properties;
		this.port = Integer.parseInt((String) properties.get("port"));
		this.numClients = Integer.parseInt((String) properties.get("numClients"));
	}

	public int getPort() {
		return port;
	}

	public void start() {
		this.serverThread = new Thread() {
			@Override
			public void run() {
				try (final ServerSocket serverSocket = new ServerSocket(port)) {
					threadPool = Executors.newFixedThreadPool(numClients);
					while (true) {
						Socket socket = serverSocket.accept();
						if (stop)
							break;

						// TODO Responder al cliente
						if (pages == null) {
							threadPool.execute(new ServiceThread(socket, properties));
						} else {
							threadPool.execute(new ServiceThread(socket, pages));
						}

					}
<<<<<<< HEAD
				} catch (IOException | SQLException | HTTPParseException e) {
=======
				} catch (IOException | SQLException e) {
>>>>>>> 20f869aeb28713db43b52e8edc4954900e2a0543
					e.printStackTrace();
				}
			}
		};

		this.stop = false;
		this.serverThread.start();
	}

	@Override
	public void close() {
		this.stop = true;

		try (Socket socket = new Socket("localhost", port)) {
			// Esta conexión se hace, simplemente, para "despertar" el hilo servidor
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		try {
			this.serverThread.join();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

		this.serverThread = null;
		threadPool.shutdownNow();

		try {
			threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
