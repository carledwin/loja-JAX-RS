package br.com.alura.loja.resource;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.thoughtworks.xstream.XStream;

import br.com.alura.loja.dao.CarrinhoDAO;
import br.com.alura.loja.modelo.Carrinho;

@Path("carrinhos")
public class CarrinhoResource {

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
	
	@GET
	@Path("json/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String buscaToJSON(@PathParam("id") long id) {
		Carrinho carrinho = new CarrinhoDAO().busca(id);
		return carrinho.toJSON();
	}
	
	@Path("")
	@POST
	@Produces(MediaType.APPLICATION_XML)
	public String adiciona(String conteudo) {
		
		Carrinho carrinho = (Carrinho) new XStream().fromXML(conteudo);
		
		CarrinhoDAO carrinhoDAO = new CarrinhoDAO();
		
		carrinhoDAO.adiciona(carrinho);
		
		return "<status>sucesso</status>";
	}
}
