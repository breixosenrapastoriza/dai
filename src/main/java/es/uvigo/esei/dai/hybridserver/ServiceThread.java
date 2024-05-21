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
import es.uvigo.esei.dai.hybridserver.http.MIME;
import model.dao.Html;
import model.dao.Xml;
import model.dao.Xsd;
import model.dao.Xslt;
import model.dao.controller.HtmlController;
import model.dao.controller.XmlController;
import model.dao.controller.XsdController;
import model.dao.controller.XsltController;
import paquete.XMLValidator;


public class ServiceThread implements Runnable {
	private final Socket socket;
	private HtmlController htmlController;
	private XmlController xmlController;
	private XsdController xsdController;
	private XsltController xsltController;

	public ServiceThread(Socket clientSocket, Properties properties) throws IOException, SQLException {
		this.socket = clientSocket;
		this.htmlController = new HtmlController(properties);
		this.xmlController = new XmlController(properties);
		this.xsdController = new XsdController(properties);
		this.xsltController = new XsltController(properties);
	}

	public ServiceThread(Socket clientSocket, Configuration configuration) throws IOException, SQLException {
		this.socket = clientSocket;
		this.htmlController = new HtmlController(configuration);
		this.xmlController = new XmlController(configuration);
		this.xsdController = new XsdController(configuration);
		this.xsltController = new XsltController(configuration);
	}

	@Override
	public void run() {
		try (this.socket) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			HTTPRequest request = new HTTPRequest(reader);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			HTTPResponse response = new HTTPResponse();
			response.setStatus(HTTPResponseStatus.S200);
			response.setVersion("HTTP/1.1");
			response.putParameter("Content-Type", MIME.TEXT_HTML.getMime());
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
						}
						text += "</ul>";
						response.setContent(text);
					}
				} else if (request.getResourceName().equals("xml")) {
					if (request.getResourceParameters().containsKey("uuid") && request.getResourceParameters().containsKey("xslt")) {
						//System.out.println(request.toString());
						String uuid = request.getResourceParameters().get("uuid");
						String xslt = request.getResourceParameters().get("xslt");
						String xml_content = xmlController.get(uuid).getContent();
						String uuid_xsd = xsltController.get(xslt).getXsd();
						if (uuid_xsd == null) {
							response.setStatus(HTTPResponseStatus.S404);
						}else {
							String xslt_content = xsltController.get(xslt).getContent();
							String xsd_content = xsdController.get(uuid_xsd).getContent();
							if (XMLValidator.validarXMLConXSD(xml_content, xsd_content)) {
								response.putParameter("Content-Type", MIME.TEXT_HTML.getMime());
								String html_content = XMLValidator.transformXMLWithXSLT(xml_content, xslt_content, true);
								response.setContent(html_content.replaceAll("[ \t]", ""));
							} else {
								response.setStatus(HTTPResponseStatus.S400);
							}					
						}
						
					} else if (request.getResourceParameters().containsKey("uuid")) {
						String uuid = request.getResourceParameters().get("uuid");
						if (xmlController.contains(uuid)) {
							response.putParameter("Content-Type", MIME.APPLICATION_XML.getMime());
							response.setContent(xmlController.get(uuid).getContent());
						} else {
							response.setStatus(HTTPResponseStatus.S404);
						}
					} else {
						String text = "<ul>";
						for (Xml xml : xmlController.list()) {
							text += "<li><a href=\"html?uuid=" + xml.getUuid() + "\">" + xml.getUuid() + "</a></li>";
						}
						text += "</ul>";
						response.setContent(text);
					}
				} else if (request.getResourceName().equals("xsd")) {
					if (request.getResourceParameters().containsKey("uuid")) {
						String uuid = request.getResourceParameters().get("uuid");
						if (xsdController.contains(uuid)) {
							response.putParameter("Content-Type", MIME.APPLICATION_XML.getMime());
							response.setContent(xsdController.get(uuid).getContent());
						} else {
							response.setStatus(HTTPResponseStatus.S404);
						}
					} else {
						String text = "<ul>";
						for (Xsd xsd : xsdController.list()) {
							text += "<li><a href=\"xsd?uuid=" + xsd.getUuid() + "\">" + xsd.getUuid() + "</a></li>";
						}
						text += "</ul>";
						response.setContent(text);
					}
				} else if (request.getResourceName().equals("xslt")) {
					if (request.getResourceParameters().containsKey("uuid")) {
						String uuid = request.getResourceParameters().get("uuid");
						if (xsltController.contains(uuid)) {
							response.putParameter("Content-Type", MIME.APPLICATION_XML.getMime());
							response.setContent(xsltController.get(uuid).getContent());
						} else {
							response.setStatus(HTTPResponseStatus.S404);
						}
					} else {
						String text = "<ul>";
						for (Xslt xslt : xsltController.list()) {
							text += "<li><a href=\"xslt?uuid=" + xslt.getUuid() + "\">" + xslt.getUuid() + "</a></li>";
						}
						text += "</ul>";
						response.setContent(text);
					}
				} else {
					response.setContent("<h1>Error: Page Not Found</h1>");
					response.setStatus(HTTPResponseStatus.S400);
				}
			} else if (request.getMethod().name().equals("POST")) {
				if (request.getResourceName().equals("html")) {
					UUID randomUuid = UUID.randomUUID();
					String uuid = randomUuid.toString();
					if (request.getResourceParameters().get("html") != null) {
						htmlController.add(new Html(uuid, request.getResourceParameters().get("html")));
						response.setContent("<a href=\"html?uuid=" + uuid + "\">" + uuid + "</a>");
					} else {
						response.setStatus(HTTPResponseStatus.S400);
					}
				} else if (request.getResourceName().equals("xml")) {
					UUID randomUuid = UUID.randomUUID();
					String uuid = randomUuid.toString();
					xmlController.add(new Xml(uuid, request.getResourceParameters().get("xml")));
					response.setContent("<a href=\"xml?uuid=" + uuid + "\">" + uuid + "</a>");
				} else if (request.getResourceName().equals("xsd")) {
					UUID randomUuid = UUID.randomUUID();
					String uuid = randomUuid.toString();
					xsdController.add(new Xsd(uuid, request.getResourceParameters().get("xsd")));
					response.setContent("<a href=\"xsd?uuid=" + uuid + "\">" + uuid + "</a>");
				} else if (request.getResourceName().equals("xslt")) {
					// System.out.println(request.toString());
					UUID randomUuid = UUID.randomUUID();
					String uuid = randomUuid.toString();
					String xsd = request.getResourceParameters().get("xsd");
					// HAY QUE REVISAR ESTA CONDICIÓN REVISARRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR
					if (xsltController.contains(xsd) || xsdController.contains(xsd)) {
						xsltController.add(new Xslt(uuid, request.getResourceParameters().get("xslt"), xsd));
						response.setContent("<a href=\"xslt?uuid=" + uuid + "\">" + uuid + "</a>");
					} else if (xsd == null) {
						response.setStatus(HTTPResponseStatus.S400);
					} else {
						response.setStatus(HTTPResponseStatus.S404);
					}
				} else {
					response.setStatus(HTTPResponseStatus.S400);
				}
			} else if (request.getMethod().name().equals("DELETE")) {
				if (request.getResourceParameters().containsKey("uuid")) {
					String uuid = request.getResourceParameters().get("uuid");
					if (request.getResourceName().equals("html") && htmlController.contains(uuid)) {
						htmlController.delete(uuid);
					} else if (request.getResourceName().equals("xml") && xmlController.contains(uuid)) {
						xmlController.delete(uuid);
					} else if (request.getResourceName().equals("xsd") && xsdController.contains(uuid)) {
						xsdController.delete(uuid);
					} else if (request.getResourceName().equals("xslt") && xsltController.contains(uuid)) {
						xsltController.delete(uuid);
					} else {
						response.setStatus(HTTPResponseStatus.S404);
					}
				}
			}
			response.print(writer);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
