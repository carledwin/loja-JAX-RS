package br.com.alura.loja;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;

import br.com.alura.loja.modelo.Carrinho;
import br.com.alura.loja.modelo.Produto;
import junit.framework.Assert;

public class ClientTest {

	private static final String NOTEBOOK = "Notebook";
	private static final String LOCATION2 = "location";
	private static final String CARRINHOS_XML = "/carrinhos/xml";
	private static final String CARRINHOS_XML_SERIALIZADO = "/carrinhos/xml/serializado";
	private static final String RUA_RUA_VERGUEIRO_3185 = "<rua>Rua Vergueiro 3185";
	private static final String V2_52AAF5DEEE7BA8C70329FB7D = "/v2/52aaf5deee7ba8c70329fb7d";
	private static final String HTTP_WWW_MOCKY_IO = "http://www.mocky.io";
	private static final String CARRINHOS_JSON_1 = "/carrinhos/json/1";
	private static final String CARRINHOS_XML_1 = "/carrinhos/xml/1";
	private static final String CARRINHOS_XML_SERIALIZADO_1 = "/carrinhos/xml/serializado/1";
	private static final String RUA_VERGUEIRO_3185_8_ANDAR = "Rua Vergueiro 3185, 8 andar";
	private static final String HTTP_LOCALHOST_8090 = "http://localhost:8090";
	
	private HttpServer server;
	private Client client;
	private WebTarget target;
	
	@Before
	public void startServidor() {
		
		server = Servidor.inicializaServidor();
		
		client = ClientBuilder.newClient();
		
		target = client.target(HTTP_LOCALHOST_8090);
		
		ClientConfig clientConfig = new ClientConfig();
		
		clientConfig.register(new LoggingFilter());
	}
	
	@After
	public void stopServidor() {
		Servidor.finalizaServidor(server);
	}

   //Funciona somente se o objeto nao estiver serializado
	@Test
	public void testQueBuscaUmCarrinhoTrazOCarrinhoEsperadoToXML() {

		String conteudo = target.path(CARRINHOS_XML_1).request().get(String.class);
		//System.out.println(conteudo);
		
		Carrinho carrinho = (Carrinho) new XStream().fromXML(conteudo);
		
		Assert.assertEquals(RUA_VERGUEIRO_3185_8_ANDAR, carrinho.getRua());
		
	}
	
	@Test
	public void testQueBuscaUmCarrinhoTrazOCarrinhoEsperadoToXMLSerializado() {
		
		Carrinho carrinho = target.path(CARRINHOS_XML_SERIALIZADO_1).request().get(Carrinho.class);
		
		Assert.assertEquals(RUA_VERGUEIRO_3185_8_ANDAR, carrinho.getRua());
		
	}
	
	@Test
	public void testQueBuscaUmCarrinhoTrazOCarrinhoEsperadoToJSON() {
		
		String conteudo = target.path(CARRINHOS_JSON_1).request().get(String.class);
		//System.out.println(conteudo);
		
		Carrinho carrinho = new Gson().fromJson(conteudo, Carrinho.class);
		
		Assert.assertEquals(RUA_VERGUEIRO_3185_8_ANDAR, carrinho.getRua());
	}
	
	
	@Test
	public void testQueAConexaoComOServidorFunciona() {
		
		target =  client.target(HTTP_WWW_MOCKY_IO);
		
		String conteudo = target.path(V2_52AAF5DEEE7BA8C70329FB7D).request().get(String.class);
		//System.out.println(conteudo);
		Assert.assertTrue(conteudo.contains(RUA_RUA_VERGUEIRO_3185));
	}

	@Test
	public void testaQueSuportaNovosCarrinhosXStream() {
		
		Carrinho carrinho = new Carrinho();
		carrinho.adiciona(new Produto(324l, NOTEBOOK, 4565, 1));
		carrinho.setRua("Avenida Nova Onda 34");
		carrinho.setCidade("Guarulhos");
		
		String xml = new XStream().toXML(carrinho);
		
		Entity<String> entity = Entity.entity(xml,  MediaType.APPLICATION_XML);
		
		Response response = target.path(CARRINHOS_XML).request().post(entity);
		
		Assert.assertEquals(201, response.getStatus());
		
		String location = response.getHeaderString(LOCATION2);
		
		Assert.assertEquals("http://localhost:8090/carrinhos/xml/3", location);
		
		String conteudo = client.target(location).request().get(String.class);
		
		Assert.assertTrue(conteudo.contains(NOTEBOOK));
	}
	
	@Test
	public void testaQueSuportaNovosCarrinhosSerializado() {
		
		Carrinho carrinho = new Carrinho();
		carrinho.adiciona(new Produto(5554l, NOTEBOOK, 776, 1));
		carrinho.setRua("Avenida Comendador 332");
		carrinho.setCidade("Americana");
		
		Entity<Carrinho> entity = Entity.entity(carrinho, MediaType.APPLICATION_XML);
		
		Response response = target.path(CARRINHOS_XML_SERIALIZADO).request().post(entity);
		
		Assert.assertEquals(201, response.getStatus());
		
		String location = response.getHeaderString(LOCATION2);
		
		Assert.assertEquals("http://localhost:8090/carrinhos/xml/2", location);
		
		String conteudo = client.target(location).request().get(String.class);
		
		Assert.assertTrue(conteudo.contains("<cidade>Americana"));
	}
	
}
