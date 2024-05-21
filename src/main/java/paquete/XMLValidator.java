package paquete;

import javax.xml.XMLConstants;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.SAXException;
import java.io.StringReader;
import java.io.StringWriter;

public class XMLValidator {
	public static boolean validarXMLConXSD(String xmlString, String xsdString) {
		try {
			// Crear una instancia de fábrica de esquema
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

			// Compilar el esquema XSD
			Schema schema = factory.newSchema(new StreamSource(new StringReader(xsdString)));

			// Crear un validador
			Validator validator = schema.newValidator();

			// Validar el documento XML
			validator.validate(new StreamSource(new StringReader(xmlString)));

			return true; // XML es válido para el esquema
		} catch (SAXException | java.io.IOException e) {
			// El XML no es válido para el esquema
			return false;
		}
	}

	public static String transformXMLWithXSLT(String xmlString, String xsltString, boolean indent) {
        try {
            // Crear una instancia del transformador
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(new StringReader(xsltString)));

            // Configurar la salida para que se indente si se solicita
            if (indent) {
                transformer.setOutputProperty("indent", "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            }

            // Realizar la transformación del XML
            StringWriter writer = new StringWriter();
            transformer.transform(new StreamSource(new StringReader(xmlString)), new StreamResult(writer));
            return writer.toString(); // HTML resultante
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}