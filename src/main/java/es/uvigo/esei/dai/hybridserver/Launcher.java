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
package es.uvigo.esei.dai.hybridserver;

import static java.util.stream.Collectors.toMap;

import java.util.Map;
import java.util.stream.Stream;

public class Launcher {
	public static void main(String[] args) {
		// TODO Ejecutar el servidor

		String matrixPages[][] = new String[][] {
				// { "uuid", "texto contenido por la página" }
				{ "6df1047e-cf19-4a83-8cf3-38f5e53f7725",
						"This is the html page 6df1047e-cf19-4a83-8cf3-38f5e53f7725." },
				{ "79e01232-5ea4-41c8-9331-1c1880a1d3c2",
						"This is the html page 79e01232-5ea4-41c8-9331-1c1880a1d3c2." },
				{ "a35b6c5e-22d6-4707-98b4-462482e26c9e",
						"This is the html page a35b6c5e-22d6-4707-98b4-462482e26c9e." },
				{ "3aff2f9c-0c7f-4630-99ad-27a0cf1af137",
						"This is the html page 3aff2f9c-0c7f-4630-99ad-27a0cf1af137." },
				{ "77ec1d68-84e1-40f4-be8e-066e02f4e373",
						"This is the html page 77ec1d68-84e1-40f4-be8e-066e02f4e373." },
				{ "8f824126-0bd1-4074-b88e-c0b59d3e67a3",
						"This is the html page 8f824126-0bd1-4074-b88e-c0b59d3e67a3." },
				{ "c6c80c75-b335-4f68-b7a7-59434413ce6c",
						"This is the html page c6c80c75-b335-4f68-b7a7-59434413ce6c." },
				{ "f959ecb3-6382-4ae5-9325-8fcbc068e446",
						"This is the html page f959ecb3-6382-4ae5-9325-8fcbc068e446." },
				{ "2471caa8-e8df-44d6-94f2-7752a74f6819",
						"This is the html page 2471caa8-e8df-44d6-94f2-7752a74f6819." },
				{ "fa0979ca-2734-41f7-84c5-e40e0886e408",
						"This is the html page fa0979ca-2734-41f7-84c5-e40e0886e408." } };

		// Creación del servidor con las páginas ya en memoria.
		final Map<String, String> pages = Stream.of(matrixPages).collect(toMap(entry -> entry[0], entry -> entry[1]));

		HybridServer hybridServer = new HybridServer(pages);
		hybridServer.start();
	}
}
