package br.usjt.arqsw.controller;

import java.io.IOException;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.usjt.arqsw.entity.Chamado;
import br.usjt.arqsw.entity.Fila;
import br.usjt.arqsw.service.ChamadoService;
import br.usjt.arqsw.service.FilaService;
/**
 * 
 * @author Jessica
 *
 */
public class ManterChamadosRest {

	private ChamadoService chamadoService;
	private FilaService filaService;

	@Autowired
	public ManterChamadosRest(ChamadoService cs, FilaService fs) {
		chamadoService = cs;
		filaService = fs;
	}
    /**
     * Lista todos os chamados de uma determinada fila
     * @param id Recebe o id da fila como parâmetro
     * @return um JSON Array com todos os chamados da fila
     */
	@RequestMapping(method = RequestMethod.GET, value = "rest/chamados/{id}")
	public @ResponseBody List<Chamado> listarChamados(
			@PathVariable("id") Long id) {
		List<Chamado> chamados = null;
		try {
			Fila fila = filaService.carregar(id.intValue());
			chamados = chamadoService.listarChamados(fila);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return chamados;
	}

	/**
     * Lista todos os chamados abertos de uma fila
     * @param id Recebe o id da fila como parâmetro
     * @return Array com os chamados abertos da fila
     */
	@RequestMapping(method = RequestMethod.GET, value = "rest/chamados/abertos/{id}")
	public @ResponseBody List<Chamado> listarChamadosAbertos(
			@PathVariable("id") Long id) {
		List<Chamado> chamados = null;
		try {
			Fila fila = filaService.carregar(id.intValue());
			chamados = chamadoService.listarChamadosAbertos(fila);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return chamados;
	}

	/**
	 * Fecha chamados.
	 * @param Lista de chamados a serem fechados
	 */
/*	@Transactional
	@RequestMapping(method = RequestMethod.PUT, value = "rest/chamados")
	public void fecharChamados(@RequestBody List<Chamado> chamados) {
		try {
			ArrayList<Integer> lista = new ArrayList<>();
			for(Chamado chamado:chamados){
				lista.add(chamado.getNumero());
			}
			chamadoService.fecharChamados(lista);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/
	
	/**
	 * Inclui um novo chamado
	 * @param Chamado com a descrição e a fila preenchida
	 * @return um JSON do chamado com o id criado, data e status
	 */
	@Transactional
	@RequestMapping(method = RequestMethod.POST, value = "rest/chamados")
	public ResponseEntity<Chamado> criarChamado(@RequestBody Chamado chamado) {
		try {
			System.out.println(chamado);
			chamadoService.novoChamado(chamado);
			return new ResponseEntity<Chamado>(chamado, HttpStatus.OK);
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<Chamado>(chamado,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}