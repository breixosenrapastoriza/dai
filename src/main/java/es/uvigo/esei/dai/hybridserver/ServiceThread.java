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

import es.uvigo.esei.dai.hybridserver.http.HTTPRequest;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponse;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponseStatus;
import model.dao.Html;
import model.dao.HtmlController;

public class ServiceThread implements Runnable {
	private final Socket socket;
	private HtmlController htmlController;

	public ServiceThread(Socket clientSocket, Properties properties) throws IOException, SQLException {
		this.socket = clientSocket;
		this.htmlController = new HtmlController(properties);
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
			if (request.getMethod().name().equals("GET")) {
				if (request.getResourceName().equals("")) {
					response.setContent("<h1>Hybrid Server</h1><h2>Breixo Senra Pastoriza</h2>");
				} else if (request.getResourceName().equals("html")) {
					if (request.getResourceParameters().containsKey("uuid")) {
						String uuid = request.getResourceParameters().get("uuid");
						if (htmlController.contains(uuid)) {
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
				if (request.getResourceParameters().containsKey("html")) {
					UUID randomUuid = UUID.randomUUID();
					String uuid = randomUuid.toString();
					// pages.put(uuid, request.getResourceParameters().get("html"));
					htmlController.add(new Html(uuid, request.getResourceParameters().get("html")));
					response.setContent("<a href=\"html?uuid=" + uuid + "\">" + uuid + "</a>");
				} else {
					response.setStatus(HTTPResponseStatus.S400);
				}
			} else if (request.getMethod().name().equals("DELETE")) {
				if (request.getResourceParameters().containsKey("uuid")) {
					String uuid = request.getResourceParameters().get("uuid");
					if (htmlController.contains(uuid)) {
						// pages.remove(uuid);
						htmlController.delete(uuid);
					} else {
						response.setStatus(HTTPResponseStatus.S404);
					}
				}
			}
			response.setVersion("HTTP/1.1");
			response.putParameter("Content-Type", "text/html");
			response.print(writer);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
