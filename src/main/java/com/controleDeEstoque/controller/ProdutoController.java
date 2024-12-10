package com.controleDeEstoque.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.controleDeEstoque.exception.ProdutoException;
import com.controleDeEstoque.model.Produto;
import com.controleDeEstoque.repository.ProdutoRepository;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

	@Autowired
	private ProdutoRepository produtoRepository ; 
	
	// Função responsável por adicionar um produto no banco de dados ; 
	
	@PostMapping 
	public Produto adicionarProduto(@RequestBody Produto produto) {
		if(produto.getQuantidade() < 0 || produto.getPreco() < 0) { // Garantindo que a quantidade ou o preço não seja menor que 0 ; 
			throw new ProdutoException("Falha ao adicionar produto! A quantidade ou o preço não pode ser inferior a 0.");
		}
		produtoRepository.save(produto); // Salvando o produto no banco de dados ; 
		System.out.println("Produto: " + produto.getNome() + " foi adicionado com sucesso!");
		return produto ; 
	}
	
	// Função responsável por deletar um produto no banco de dados ;
	
	@DeleteMapping("/{id}") 
	public void deletarProdutoPorId(@PathVariable Long id) {
		produtoRepository.deleteById(id);
	}
	
	// Função responsável por atualizar um produto no banco de dados ; 
	
	@PutMapping("/{id}")
	public void atualizarProdutoPorId(@PathVariable Long id, @RequestBody Produto produto) {
		if(produto.getQuantidade() < 0 || produto.getPreco() < 0) { // Garantindo que a quantidade ou o preço não seja menor que 0 ; 
			throw new ProdutoException("Falha ao adicionar produto! A quantidade ou o preço não pode ser inferior a 0.");
		}
		produto.setId(id); 
		produtoRepository.save(produto); 
		System.out.println("O produto: " + produto.getNome() + " foi atualizado com sucesso!");
	}
	
	// Função responsável por incrementar um produto no banco de dados ; 
	
	@PutMapping("/incrementar/{id}")
	public Produto incrementarProdutoPorId(@PathVariable Long id, @RequestBody Integer quantidade) {
		Produto produto = produtoRepository.findById(id).orElse(null); // Selecionando os produtos cadastrados no banco de dados com o ID informado, caso não exista nenhum produto cadastrado com o ID será retornado nulo ;  
		
		if(produto != null) { // Caso o produto exista ; 
			if(quantidade <= 0) { // Caso a quantidade informada pelo usuário seja menor ou igual a 0 ; 
				throw new ProdutoException("Falha ao incrementar produto! A quantidade precisa ser maior que 0");
			}
			Integer novaQuantidade = produto.getQuantidade() + quantidade ; // Nova quantidade do produto atribuido a variável após a incrementação ; 
			produto.setQuantidade(novaQuantidade); // Incrementando o produto no banco de dados ; 
			produtoRepository.save(produto);
		}
		
		return produto ;
	}
	
	// Função responsável por decrementar um produto no banco de dados ; 
	
	@PutMapping("/decrementar/{id}")
	public Produto decrementarProdutoPorId(@PathVariable Long id, @RequestBody Integer quantidade) {
		Produto produto = produtoRepository.findById(id).orElse(null); // Selecionando todos os produtos cadastrados no banco de dados com o ID informado, caso não exista nenhum produto cadastrado com o ID será retornado nulo ; 
		
		if(produto != null) { // Caso o produto exista ; 
			if(quantidade < 0) { // Caso a quantidade informada pelo usuário seja menor ou igual a 0 ; 
				throw new ProdutoException("Falha ao decrementar produto! A quantidade precisa ser maior que 0");
			}
			Integer novaQuantidade = produto.getQuantidade() - quantidade ; // Nova quantidade do produto atribuindo a variável após a decrementação ; 
			if(novaQuantidade < 0) { // Caso a quantidade fique menor que 0, não será atualizado no banco de dados e será lançado uma exceção ;
				throw new ProdutoException("Falha ao decrementar produto! A quantidade em estoque não pode ficar inferior a 0");

			}
			produto.setQuantidade(novaQuantidade); // Decrementando o produto no banco de dados ; 
			produtoRepository.save(produto);
		}
		return produto ; 
	}


	// Função responsável por selecionar todos os produtos no banco de dados ; 
	
	@GetMapping
	public List<Produto> selecionarTodosProdutos() {
		return produtoRepository.findAll();
	}
	
	// Função responsável por selecionar todos os produtos no banco de dados de um determinado ID ; 
	
	@GetMapping("/{id}")
	public Produto selecionarProdutoPorId(@PathVariable Long id) {
		return produtoRepository.findById(id).orElse(null);
	}
}

