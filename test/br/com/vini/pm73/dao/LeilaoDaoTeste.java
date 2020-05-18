package br.com.vini.pm73.dao;


import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.vini.pm73.dominio.Leilao;
import br.com.vini.pm73.dominio.Usuario;

public class LeilaoDaoTeste {
	
	private  Session session;
	private  LeilaoDao leilaoDao;
	private  UsuarioDao usuarioDao;
	private  Usuario mauricio;
	
	@Before
	public void init() {
		session = new CriadorDeSessao().getSession();
		leilaoDao = new LeilaoDao(session);
		usuarioDao = new UsuarioDao(session);
		mauricio = new Usuario("mauricio", "mauricio@mauricio.com.br");
		session.beginTransaction();

		usuarioDao.salvar(mauricio);
	}
	
	@After
	public void finish() {
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
	
	@Test
	public void deveRetornarZeroNaoEncerrados() {
		Leilao l1 = new Leilao("Geladeira", 1500.0, mauricio, false);
		Leilao l2 = new Leilao("Xbox", 700.0, mauricio, false);
		l1.encerra();
		l2.encerra();
		leilaoDao.salvar(l1);
		leilaoDao.salvar(l2);
		
		long total = leilaoDao.total();
		
		assertEquals(0l, total);
	}
	
	@Test
	public void deveRetornarLeilaoNovos() {
		
		Leilao l1 = new Leilao("Geladeira", 1500.0, mauricio, true);
		Leilao l2 = new Leilao("Xbox", 1700.0, mauricio, false);
		
		leilaoDao.salvar(l1);
		leilaoDao.salvar(l2);
		
		List<Leilao> novos = leilaoDao.novos();
		
		assertEquals(1, novos.size());
		assertEquals(l2.getNome(), novos.get(0).getNome());
		
	}
	
	@Test
	public void deveRetornarAntigos() {
		Leilao l1 = new Leilao("Geladeira", 1500.0, mauricio, true);
		Calendar dataAntiga = Calendar.getInstance();
		dataAntiga.add(Calendar.DAY_OF_MONTH, -7);
		l1.setDataAbertura(dataAntiga);
		
		Leilao l2 = new Leilao("Xbox", 1700.0, mauricio, false);
		Calendar dataAgora = Calendar.getInstance();
		l2.setDataAbertura(dataAgora);
		
		leilaoDao.salvar(l1);
		leilaoDao.salvar(l2);
		
		List<Leilao> antigos = leilaoDao.antigos();
		
		assertEquals(1, antigos.size());
		assertEquals(l1.getNome(), antigos.get(0).getNome());
	}

}
