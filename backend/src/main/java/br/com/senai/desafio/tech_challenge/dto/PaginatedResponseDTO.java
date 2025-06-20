package br.com.senai.desafio.tech_challenge.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
public class PaginatedResponseDTO<T> {
    private List<T> data;
    private MetaDTO meta;
}
