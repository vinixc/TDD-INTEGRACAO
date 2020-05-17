package br.com.vini.pm73.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.hibernate.Session;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import br.com.vini.pm73.dominio.Usuario;

public class UsuarioDaoTest {
	
	private static Session session;
	private static UsuarioDao usuarioDao;

	@BeforeClass
	public static void init() {
		session = new CriadorDeSessao().getSession();
		usuarioDao = new UsuarioDao(session);
		session.beginTransaction();
	}
	
	@AfterClass
	public static void finish() {
		session.getTransaction().rollback();
		session.close();
	}
	
	@Test
	public void deveEncontrarPeloNomeEmail() {
		Usuario novoUsuario = new Usuario("Joao da Silva", "joao@dasilva.com.br");
		
		usuarioDao.salvar(novoUsuario);
		
		Usuario usuario = usuarioDao.porNomeEEmail("Joao da Silva", "joao@dasilva.com.br");
		
		assertEquals("Joao da Silva", usuario.getNome());
		assertEquals("joao@dasilva.com.br", usuario.getEmail());
		
	}
	
	@Test
	public void deveRetornarUsuarioNullo() {
		Usuario usuarioInexistente = usuarioDao.porNomeEEmail("Vinicius de Carvalho", "vinicius@decarvalho.com.br");
		assertNull(usuarioInexistente);
	}

}
