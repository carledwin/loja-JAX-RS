package br.com.alura.loja;

import java.io.IOException;
import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

public class Servidor {

	public static void main(String... strings) throws IOException {
		
		HttpServer server = inicializaServidor();
		
		System.out.println("Servidor iniciando...");
		System.out.println("Servidor rodando...");
		
		System.in.read();
		
		finalizaServidor(server);
		
		System.out.println("Desligando Servidor...");
		System.out.println("Servidor encerrado.");
	}

	public static void finalizaServidor(HttpServer server) {
		server.stop();
	}

	public static HttpServer inicializaServidor() {
		ResourceConfig config = new ResourceConfig().packages("br.com.alura.loja");
		URI uri = URI.create("http://localhost:8090/");
		HttpServer server = GrizzlyHttpServerFactory.createHttpServer(uri, config );
		return server;
	}
}
