package es.uvigo.esei.dai.hybridserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketIOManager implements IOManager {
	private final Socket socket;
	private final BufferedReader reader;
	private final PrintWriter writer;

	public SocketIOManager(Socket socket) throws IOException {
		this.socket = socket;
		this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.writer = new PrintWriter(socket.getOutputStream());
	}

	public Socket getSocket() {
		return this.socket;
	}

	@Override
	public boolean canRead() throws IOException {
		return !this.socket.isClosed() && this.socket.getInputStream().available() > 0;
	}

	@Override
	public void println(String line) throws IOException {
		this.writer.println(line);
		this.writer.flush();
	}

	@Override
	public String readLine() throws IOException {
		return this.reader.readLine();
	}
}
