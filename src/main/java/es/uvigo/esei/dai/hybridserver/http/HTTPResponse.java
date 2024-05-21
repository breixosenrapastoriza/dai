/**
 *  HybridServer
 *  Copyright (C) 2023 Miguel Reboiro-Jato
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.uvigo.esei.dai.hybridserver.http;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class HTTPResponse {

	private HTTPResponseStatus status;
	private String version;
	private String content;
	private Map<String, String> parameters;

	public HTTPResponse() {
		version = "";
		content = "";
		status = HTTPResponseStatus.S200;
		parameters = new HashMap<String, String>();
	}

	public HTTPResponseStatus getStatus() {
		return status;
	}

	public void setStatus(HTTPResponseStatus status) {
		this.status = status;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public String putParameter(String name, String value) {
		return parameters.put(name, value);
	}

	public boolean containsParameter(String name) {
		return parameters.containsKey(name);
	}

	public String removeParameter(String name) {
		return parameters.remove(name);
	}

	public void clearParameters() {
		parameters.clear();
	}

	public List<String> listParameters() {
		List<String> keysList = new ArrayList<>(parameters.keySet());
		return keysList;
	}

	/*
	 * public void print(Writer writer) throws IOException { BufferedWriter
	 * bufferedWriter = new BufferedWriter(writer); String responseText = "";
	 * responseText += this.getVersion(); bufferedWriter.write("HOLA"); }
	 */

	public void print(Writer writer) throws IOException {
		BufferedWriter bufferedWriter = new BufferedWriter(writer);
		String textResponse = this.getStatus().getStatus();
		bufferedWriter.write(this.getVersion() + " " + this.getStatus().getCode() + " " + textResponse + "\r\n");
		if (this.getContent().length() != 0) {
			this.putParameter("Content-Length", this.getContent().length() + "");
		}
		for (Map.Entry<String, String> entry : this.getParameters().entrySet()) {
			bufferedWriter.write(entry.getKey() + ": " + entry.getValue() + "\r\n");
		}
		bufferedWriter.write("\r\n" + this.getContent());
		bufferedWriter.flush();
	}

	@Override
	public String toString() {
		try (final StringWriter writer = new StringWriter()) {
			this.print(writer);
			// writer.append(getVersion()).append(getContent());
			return writer.toString();
		} catch (IOException e) {
			throw new RuntimeException("Unexpected I/O exception", e);
		}
	}
}
