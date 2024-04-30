package es.uvigo.esei.dai.hybridserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

<<<<<<< HEAD
import es.uvigo.esei.dai.hybridserver.http.HTTPParseException;
=======
>>>>>>> 20f869aeb28713db43b52e8edc4954900e2a0543
import es.uvigo.esei.dai.hybridserver.http.HTTPRequest;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponse;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponseStatus;
import model.dao.Html;
import model.dao.HtmlController;

public class ServiceThread implements Runnable {
	private final Socket socket;
	private HtmlController htmlController;
<<<<<<< HEAD
	private Properties properties;

	public ServiceThread(Socket clientSocket, Properties properties) throws IOException, SQLException, HTTPParseException {
		this.socket = clientSocket;
		this.properties = properties;
=======

	public ServiceThread(Socket clientSocket, Properties properties) throws IOException, SQLException {
		this.socket = clientSocket;
		this.htmlController = new HtmlController(properties);
>>>>>>> 20f869aeb28713db43b52e8edc4954900e2a0543
	}

	public ServiceThread(Socket clientSocket, Map<String, String> pages) throws IOException {
		this.socket = clientSocket;
		this.htmlController = new HtmlController(pages);
	}

	@Override
	public void run() {
		try (this.socket) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			HTTPRequest request = new HTTPRequest(reader);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			HTTPResponse response = new HTTPResponse();
			response.setStatus(HTTPResponseStatus.S200);
<<<<<<< HEAD
			response.putParameter("Content-Type", "text/html");
			
			this.htmlController = new HtmlController(properties, request.getResourceName());
			
			
			if (request.getMethod().name().equals("GET")) {
				if (request.getResourceName().equals("")) {
					response.setContent("<h1>Hybrid Server</h1><h2>Breixo Senra Pastoriza</h2>");
				} else if (request.getResourceName().equals("html") || request.getResourceName().equals("xsd") || request.getResourceName().equals("xml") || request.getResourceName().equals("xslt")) {
					if (request.getResourceParameters().containsKey("uuid")) {
						String uuid = request.getResourceParameters().get("uuid");
						if (htmlController.contains(uuid)) {
							if (request.getResourceName().equals("xsd") || request.getResourceName().equals("xml")  || request.getResourceName().equals("xslt")) {
								response.putParameter("Content-Type", "application/xml");
							}
=======
			if (request.getMethod().name().equals("GET")) {
				if (request.getResourceName().equals("")) {
					response.setContent("<h1>Hybrid Server</h1><h2>Breixo Senra Pastoriza</h2>");
				} else if (request.getResourceName().equals("html")) {
					if (request.getResourceParameters().containsKey("uuid")) {
						String uuid = request.getResourceParameters().get("uuid");
						if (htmlController.contains(uuid)) {
>>>>>>> 20f869aeb28713db43b52e8edc4954900e2a0543
							response.setContent(htmlController.get(uuid).getContent());
						} else {
							response.setStatus(HTTPResponseStatus.S404);
						}
					} else {
						String text = "<ul>";
						for (Html html : htmlController.list()) {
							text += "<li><a href=\"html?uuid=" + html.getUuid() + "\">" + html.getUuid() + "</a></li>";
							// text += "<li><a>" + html.getUuid() + "</a></li>";
						}
						text += "</ul>";
						response.setContent(text);
					}
				} else {
					response.setContent("<h1>Error: Page Not Found</h1>");
					response.setStatus(HTTPResponseStatus.S400);
				}
			} else if (request.getMethod().name().equals("POST")) {
<<<<<<< HEAD
				if (request.getResourceParameters().containsKey("html") || request.getResourceParameters().containsKey("xsd") || request.getResourceParameters().containsKey("xml") || request.getResourceParameters().containsKey("xslt")) {
					UUID randomUuid = UUID.randomUUID();
					String uuid = randomUuid.toString();
					// pages.put(uuid, request.getResourceParameters().get("html"));
					
					/*El problema de los archivos HSLT proviene del HtmlDBDAO que
					no está adaptado para obtener 1 tupla con 3 atributos*/
					htmlController.add(new Html(uuid, request.getResourceParameters().get(request.getResourceName())));
					//request.getResourceParameters().get("xsd");
					response.setContent("<a href=\"" + request.getResourceName() + "?uuid=" + uuid + "\">" + uuid + "</a>");
=======
				if (request.getResourceParameters().containsKey("html")) {
					UUID randomUuid = UUID.randomUUID();
					String uuid = randomUuid.toString();
					// pages.put(uuid, request.getResourceParameters().get("html"));
					htmlController.add(new Html(uuid, request.getResourceParameters().get("html")));
					response.setContent("<a href=\"html?uuid=" + uuid + "\">" + uuid + "</a>");
>>>>>>> 20f869aeb28713db43b52e8edc4954900e2a0543
				} else {
					response.setStatus(HTTPResponseStatus.S400);
				}
			} else if (request.getMethod().name().equals("DELETE")) {
				if (request.getResourceParameters().containsKey("uuid")) {
					String uuid = request.getResourceParameters().get("uuid");
					if (htmlController.contains(uuid)) {
<<<<<<< HEAD
=======
						// pages.remove(uuid);
>>>>>>> 20f869aeb28713db43b52e8edc4954900e2a0543
						htmlController.delete(uuid);
					} else {
						response.setStatus(HTTPResponseStatus.S404);
					}
				}
			}
			response.setVersion("HTTP/1.1");
<<<<<<< HEAD
=======
			response.putParameter("Content-Type", "text/html");
>>>>>>> 20f869aeb28713db43b52e8edc4954900e2a0543
			response.print(writer);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
