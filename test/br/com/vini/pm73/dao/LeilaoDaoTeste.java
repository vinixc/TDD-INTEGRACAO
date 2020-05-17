package br.com.vini.pm73.dao;


import static org.junit.Assert.assertEquals;

import org.hibernate.Session;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import br.com.vini.pm73.dominio.Leilao;
import br.com.vini.pm73.dominio.Usuario;

public class LeilaoDaoTeste {
	
	private static Session session;
	private static LeilaoDao leilaoDao;
	private static UsuarioDao usuarioDao;
	private static Usuario mauricio;
	

	@BeforeClass
	public static void init() {
		session = new CriadorDeSessao().getSession();
		leilaoDao = new LeilaoDao(session);
		usuarioDao = new UsuarioDao(session);
		mauricio = new Usuario("mauricio", "mauricio@mauricio.com.br");
		session.beginTransaction();

		usuarioDao.salvar(mauricio);
	}
	
	@AfterClass
	public static void finish() {
		session.getTransaction().rollback();
		session.close();
	}

	
	@Test
	public void deveContarLeiloesNaoEncerrados() {
		Leilao ativo = new Leilao("Geladeira", 1500.0, mauricio, false);
		Leilao encerrado = new Leilao("Xbox", 700.0, mauricio, false);
		
		encerrado.encerra();
		
		leilaoDao.salvar(ativo);
		leilaoDao.salvar(encerrado);
		
		long total = leilaoDao.total();
		
		assertEquals(1l, total);
	}

}
