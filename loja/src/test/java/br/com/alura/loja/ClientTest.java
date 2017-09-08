package br.com.alura.loja;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;

import br.com.alura.loja.modelo.Carrinho;
import junit.framework.Assert;

public class ClientTest {

	private HttpServer server;
	
	@Before
	public void startServidor() {
		server = Servidor.inicializaServidor();
	}
	
	@After
	public void stopServidor() {
		Servidor.finalizaServidor(server);
	}

	@Test
	public void testQueBuscaUmCarrinhoTrazOCarrinhoEsperadoToXML() {

		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8090");
		String conteudo = target.path("/carrinhos/xml/1").request().get(String.class);
		//System.out.println(conteudo);
		
		Carrinho carrinho = (Carrinho) new XStream().fromXML(conteudo);
		
		Assert.assertEquals("Rua Vergueiro 3185, 8 andar", carrinho.getRua());
		
	}
	
	@Test
	public void testQueBuscaUmCarrinhoTrazOCarrinhoEsperadoToJSON() {
		
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8090");
		String conteudo = target.path("/carrinhos/json/1").request().get(String.class);
		//System.out.println(conteudo);
		
		Carrinho carrinho = new Gson().fromJson(conteudo, Carrinho.class);
		
		Assert.assertEquals("Rua Vergueiro 3185, 8 andar", carrinho.getRua());
	}
	
	@Test
	public void testQueAConexaoComOServidorFunciona() {
		
		Client client = ClientBuilder.newClient();
		WebTarget target =  client.target("http://www.mocky.io");
		String conteudo = target.path("/v2/52aaf5deee7ba8c70329fb7d").request().get(String.class);
		//System.out.println(conteudo);
		Assert.assertTrue(conteudo.contains("<rua>Rua Vergueiro 3185"));
	}
	
}
