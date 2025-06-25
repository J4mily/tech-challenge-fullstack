package br.com.senai.desafio.tech_challenge.dto;
import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class MetaDTO {
    private int page;
    private int limit;
    private long totalItems;
    private int totalPages;
}