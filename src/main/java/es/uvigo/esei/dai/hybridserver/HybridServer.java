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

import es.uvigo.esei.dai.hybridserver.http.HTTPParseException;

public class HybridServer implements AutoCloseable {
	private static final int SERVICE_PORT = 8888;
	private Thread serverThread;
	private ExecutorService threadPool;
	private int numClients;
	private int port;
	private boolean stop;

	private Map<String, String> pages;
	private Properties properties;
	private Configuration configuration;
	
	
	public HybridServer() {
		// TODO Inicializar con los parámetros por defecto
		this.properties = new Properties();
		this.port = SERVICE_PORT;
		this.numClients = 50;
		this.properties.setProperty("db.url", "jdbc:mysql://localhost:3306/hstestdb");
		this.properties.setProperty("db.user", "hsdb");
		this.properties.setProperty("db.password", "hsdbpass");
	}

	public HybridServer(Configuration configuration) {
		//System.out.println(configuration.getHttpPort());
		this.configuration = configuration;
		this.port = configuration.getHttpPort();
		this.numClients = configuration.getNumClients();
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
					//System.out.println(numClients + " " + port);
					threadPool = Executors.newFixedThreadPool(numClients);
					while (true) {
						Socket socket = serverSocket.accept();
						if (stop)
							break;

						// TODO Responder al cliente
						if (pages == null) {
							threadPool.execute(new ServiceThread(socket, configuration));
							//threadPool.execute(new ServiceThread(socket, properties));
						} else {
							//threadPool.execute(new ServiceThread(socket, pages));
						}

					}
				} catch (IOException | SQLException e) {
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
	
	public String nulo2() {
		return nulo();
	}

	public String nulo() {
		return null;
	}
	
}
