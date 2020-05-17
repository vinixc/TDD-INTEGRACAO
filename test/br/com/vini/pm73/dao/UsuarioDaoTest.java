package br.com.vini.pm73.dao;

import static org.junit.Assert.assertEquals;

import org.hibernate.Session;
import org.junit.Test;

import br.com.vini.pm73.dominio.Usuario;

public class UsuarioDaoTest {
	
	@Test
	public void deveEncontrarPeloNomeEmail() {
		
		Session session = new CriadorDeSessao().getSession();
		UsuarioDao usuarioDao = new UsuarioDao(session);
		
		Usuario novoUsuario = new Usuario("Joao da Silva", "joao@dasilva.com.br");
		
		usuarioDao.salvar(novoUsuario);
		
		Usuario usuario = usuarioDao.porNomeEEmail("Joao da Silva", "joao@dasilva.com.br");
		
		assertEquals("Joao da Silva", usuario.getNome());
		assertEquals("joao@dasilva.com.br", usuario.getEmail());
		
		session.close();
	}

}
