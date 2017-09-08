package br.com.alura.loja.resource;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.thoughtworks.xstream.XStream;

import br.com.alura.loja.dao.CarrinhoDAO;
import br.com.alura.loja.modelo.Carrinho;
import br.com.alura.loja.modelo.Produto;

@Path("carrinhos")
public class CarrinhoResource {

	@POST
	@Path("xml")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public Response adiciona(String conteudo) {
		
		Carrinho carrinho = (Carrinho) new XStream().fromXML(conteudo);
		
		CarrinhoDAO carrinhoDAO = new CarrinhoDAO();
		
		carrinhoDAO.adiciona(carrinho);
		
		URI uri = URI.create("/carrinhos/xml/"+carrinho.getId());
		
		//return "<status>sucesso</status>";
		return Response.created(uri).build();
	}
	
  /*
    Via QueryParam 
    http://localhost:8090/carrinhos?id=1
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public String busca(@QueryParam("id") long id) {*/
	
	/*
	 Via PathParam
	 http://localhost:8090/carrinhos/1*/	
	@GET
	@Path("xml/{id}")
	@Produces(MediaType.APPLICATION_XML)
	public String buscaToXML(@PathParam("id") long id) {
		Carrinho carrinho = new CarrinhoDAO().busca(id);
		return carrinho.toXML();
	}
	
	@DELETE
	@Path("xml/{id}/produtos/{produtoId}")
	public Response removeProduto(@PathParam("id") long id, @PathParam("produtoId") long produtoId) {
		
		Carrinho carrinho = new CarrinhoDAO().busca(id);
		carrinho.remove(produtoId);
		
		return Response.ok().build();
	}
	
	@PUT
	@Path("xml/{id}/produtos/{produtoId}")
	public Response alteraProduto(String conteudo, @PathParam("id") long id, @PathParam("produtoId") long produtoId) {
		
		Carrinho carrinho = new CarrinhoDAO().busca(id);
		
		Produto produto = (Produto) new XStream().fromXML(conteudo);
		
		carrinho.troca(produto);
		
		return Response.ok().build();
	}
	
	@PUT
	@Path("xml/{id}/produtos/{produtoId}/quantidade")
	public Response alteraQuantidadeProduto(String conteudo, @PathParam("id") long id, @PathParam("produtoId") long produtoId) {
		Carrinho carrinho = new CarrinhoDAO().busca(id);
		
		Produto produto = (Produto) new XStream().fromXML(conteudo);
		
		carrinho.trocaQuantidade(produto);
		
		return Response.ok().build();
	}
	
	@GET
	@Path("json/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String buscaToJSON(@PathParam("id") long id) {
		Carrinho carrinho = new CarrinhoDAO().busca(id);
		return carrinho.toJSON();
	}
	
	
}
