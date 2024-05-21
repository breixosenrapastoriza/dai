package es.uvigo.esei.dai.hybridserver;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLConfigurationLoader {
	public Configuration load(Reader reader) throws Exception {
		Configuration configuration = new Configuration();
		//poner path generico para que se pueda abrir en cualquier dispositivo
		Document document = loadAndValidateWithExternalXSD(reader, "C:/Users/breix/Documents/DAI/dai-main/dai-main/configuration.xsd");
		Node config = document.getChildNodes().item(0);
		for (int i = 0; i < config.getChildNodes().getLength(); i++) {
			Node config_node = config.getChildNodes().item(i);
			System.out.println("NODO: " + config_node.getNodeName());
			if (config_node.getNodeName().equals("connections")) {
				Node connections = config_node;
				for (int j = 0; j < connections.getChildNodes().getLength(); j++) {
					Node connections_node = connections.getChildNodes().item(j);
					System.out.println(connections_node.getNodeName() + " - " + connections_node.getTextContent());
					if (connections_node.getNodeName().equals("http")) {
						configuration.setHttpPort(Integer.parseInt(connections_node.getTextContent()));
					}
					if (connections_node.getNodeName().equals("webservice")) {
						configuration.setWebServiceURL(connections_node.getTextContent());
					}
					if (connections_node.getNodeName().equals("numClients")) {
						configuration.setNumClients(Integer.parseInt(connections_node.getTextContent()));
					}
				}
			}
			if (config_node.getNodeName().equals("database")) {
				Node database = config_node;
				for (int j = 0; j < database.getChildNodes().getLength(); j++) {
					Node database_node = database.getChildNodes().item(j);
					System.out.println(database_node.getNodeName() + " - " + database_node.getTextContent());
					if (database_node.getNodeName().equals("user")) {
						configuration.setDbUser(database_node.getTextContent());
					}
					if (database_node.getNodeName().equals("password")) {
						configuration.setDbPassword(database_node.getTextContent());
					}
					if (database_node.getNodeName().equals("url")) {
						configuration.setDbURL(database_node.getTextContent());
					}
				}
			}
			if (config_node.getNodeName().equals("servers")) {
				List<ServerConfiguration> listServerConfigurations = new ArrayList<ServerConfiguration>();
				Node connections = config_node;
				for (int j = 0; j < connections.getChildNodes().getLength(); j++) {
					Node connections_node = connections.getChildNodes().item(j);
					System.out.println(connections_node.getNodeName() + " - " + connections_node.getTextContent());
					if (connections_node.getNodeName().equals("server")) {
						NamedNodeMap namedNodeMap = connections_node.getAttributes();
						ServerConfiguration serverConfiguration = new ServerConfiguration();
						for (int k = 0; k < namedNodeMap.getLength(); k++) {
							Node attribute = namedNodeMap.item(k);
							String name = attribute.getNodeName();
							String textContent = attribute.getTextContent();
							System.out.println(attribute.getNodeName() + " - " + attribute.getTextContent());
							if (name.equals("name")) {
								serverConfiguration.setName(textContent);
							}
							if (name.equals("httpAddress")) {
								serverConfiguration.setHttpAddress(textContent);
							}
							if (name.equals("service")) {
								serverConfiguration.setService(textContent);
							}
							if (name.equals("namespace")) {
								serverConfiguration.setNamespace(textContent);
							}
							if (name.equals("wsdl")) {
								serverConfiguration.setWsdl(textContent);
							}
						}
						listServerConfigurations.add(serverConfiguration);
					}
				}
				configuration.setServers(listServerConfigurations);
			}
			
			
		}
		
		return configuration;
	}

	public static Document loadAndValidateWithExternalXSD(Reader reader, String schemaPath)
			throws ParserConfigurationException, SAXException, IOException {
		try {
			// Construcción del schema
			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			File file = new File(schemaPath);
			Schema schema = schemaFactory.newSchema(file);
			// Construcción del parser del documento. Se establece el esquema y se
			// activa la validación y comprobación de namespaces
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setNamespaceAware(true);
			factory.setSchema(schema);
			// Se añade el manejador de errores
			DocumentBuilder builder = factory.newDocumentBuilder();
			builder.setErrorHandler(new SimpleErrorHandler());

			InputSource readerInputSource = new InputSource(reader);
			return builder.parse(readerInputSource);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
