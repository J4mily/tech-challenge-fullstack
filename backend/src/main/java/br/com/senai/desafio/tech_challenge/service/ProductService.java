package br.com.senai.desafio.tech_challenge.service;

import br.com.senai.desafio.tech_challenge.dto.ProductRequestDTO;
import br.com.senai.desafio.tech_challenge.dto.ProductResponseDTO;
public interface ProductService {
    ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO);
}