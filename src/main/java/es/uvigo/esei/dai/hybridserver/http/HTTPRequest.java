package es.uvigo.esei.dai.hybridserver.http;

import java.io.IOException;
import java.io.Reader;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class HTTPRequest {
	private HTTPRequestMethod method;
	private String resourceChain;
	private String[] resourcePath;
	private String resourceName;
	private Map<String, String> resourceParameters;
	private String httpVersion;
	private Map<String, String> headerParameters;
	private String content;
	private int contentLength;
	private String toString;

	public HTTPRequest(Reader reader) throws IOException, HTTPParseException {
		// TODO Completar. Cualquier error en el procesado debe lanzar una
		// HTTPParseException
		this.headerParameters = new HashMap<String, String>();
		this.resourceParameters = new HashMap<String, String>();

		try {
			char[] buffer = new char[4096];
			int charsRead = reader.read(buffer, 0, 4096);
			String text = new String(buffer, 0, charsRead);
			if (text.substring(text.length() - 2).contains("\r\n"))
				text = text.substring(0, text.length() - 2);
			text = URLDecoder.decode(text, "UTF-8");
			String lines[] = text.split("\r\n");
			setResourceChain(lines[0].split(" ")[1]);
			setResourceName(lines[0].split(" ")[1].substring(1).split("\\?")[0]);
			setResourcePath(lines[0].split(" ")[1].substring(1) == "" ? new String[0]
					: lines[0].split(" ")[1].split("\\?")[0].substring(1).split("/"));
			setToString(text);
			if (lines[0].split(" ").length > 2) {
				setHttpVersion(lines[0].split(" ")[2]);
			} else {
				throw new HTTPParseException();
			}
			if (!lines[0].endsWith(getHttpVersion())) {
				throw new HTTPParseException();
			}
			boolean foundContent = false;
			boolean foundEmptyLine = false;
			for (int i = 1; i < lines.length; i++) {
				if (foundContent && foundEmptyLine) {
					content += lines[i];
				} else if (lines[i].contains(": ")) {
					String[] words = lines[i].split(": ");
					headerParameters.put(words[0], words[1]);
					if (words[0].equals("Content-Length")) {
						setContentLength(Integer.parseInt(words[1]));
						foundContent = true;
						content = "";
					}
				} else if (lines[i].equals("")) {
					foundEmptyLine = true;
				}else {
					throw new HTTPParseException("Invalid header");
				}
			}
			if (foundContent) {
				for (int i = 0; i < content.split("&").length; i++) {
					String equalities = content.split("&")[i];
					String[] words = equalities.split("=");
					resourceParameters.put(words[0], words[1]);
				}
			}
			if (lines[0].split(" ")[1].contains("?")) {
				for (int i = 0; i < lines[0].split(" ")[1].split("\\?")[1].split("&").length; i++) {
					String equalities = lines[0].split(" ")[1].split("\\?")[1].split("&")[i];
					String[] words = equalities.split("=");
					resourceParameters.put(words[0], words[1]);
				}
			}
			switch (lines[0].split(" ")[0]) {
			case "CONNECT":
				setMethod(HTTPRequestMethod.CONNECT);
				break;
			case "DELETE":
				setMethod(HTTPRequestMethod.DELETE);
				break;
			case "HEAD":
				setMethod(HTTPRequestMethod.HEAD);
				break;
			case "POST":
				setMethod(HTTPRequestMethod.POST);
				break;
			case "PUT":
				setMethod(HTTPRequestMethod.PUT);
				break;
			case "GET":
				setMethod(HTTPRequestMethod.GET);
				break;
			}
		} catch (ArrayIndexOutOfBoundsException | IOException e) {
			e.printStackTrace();
			throw new HTTPParseException("Error durante el procesamiento de la solicitud HTTP");
		}
	}

	public HTTPRequestMethod getMethod() {
		return method;
	}

	public void setMethod(HTTPRequestMethod method) {
		this.method = method;
	}

	public String getResourceChain() {
		return resourceChain;
	}

	public void setResourceChain(String resourceChain) {
		this.resourceChain = resourceChain;
	}

	public String[] getResourcePath() {
		return resourcePath;
	}

	public void setResourcePath(String[] resourcePath) {
		this.resourcePath = resourcePath;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public Map<String, String> getResourceParameters() {
		return resourceParameters;
	}

	public void setResourceParameters(Map<String, String> resourceParameters) {
		this.resourceParameters = resourceParameters;
	}

	public String getHttpVersion() {
		return httpVersion;
	}

	public void setHttpVersion(String httpVersion) {
		this.httpVersion = httpVersion;
	}

	public Map<String, String> getHeaderParameters() {
		return headerParameters;
	}

	public void setHeaderParameters(Map<String, String> headerParameters) {
		this.headerParameters = headerParameters;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getContentLength() {
		return contentLength;
	}

	public void setContentLength(int contentLength) {
		this.contentLength = contentLength;
	}

	@Override
	public String toString() {
		return toString;
	}

	private void setToString(String toString) {
		this.toString = toString;
	}
}
