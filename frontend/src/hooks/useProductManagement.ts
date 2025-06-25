"use client";

import { useState, useCallback } from "react";
import { Product, Meta, ProductCreationData, ProductUpdateData } from "@/types";
import {
  getProducts,
  createProduct,
  updateProduct,
  deleteProduct,
  getProductById,
} from "@/service/api";
import { AxiosError } from "axios";

export function useProductManagement() {
  const [products, setProducts] = useState<Product[]>([]);
  const [meta, setMeta] = useState<Meta | null>(null);
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);
  const [currentPage, setCurrentPage] = useState<number>(0);
  const [appliedFilters, setAppliedFilters] = useState({
    search: "",
    minPrice: "",
    maxPrice: "",
  });

  const fetchProducts = useCallback(async () => {
    setIsLoading(true);
    setError(null);
    try {
      const params = new URLSearchParams();
      params.append("page", String(currentPage));
      params.append("size", "10");
      params.append("sort", "name,asc");
      if (appliedFilters.search) params.append("search", appliedFilters.search);
      if (appliedFilters.minPrice) params.append("minPrice", appliedFilters.minPrice);
      if (appliedFilters.maxPrice) params.append("maxPrice", appliedFilters.maxPrice);

      const response = await getProducts(params);
      setProducts(response.data);
      setMeta(response.meta);
    } catch (err) {
      setError("Falha ao carregar os produtos. A API do backend está em execução?");
      console.error(err);
    } finally {
      setIsLoading(false);
    }
  }, [currentPage, appliedFilters]);

  const handleFilterChange = useCallback((newFilters: Partial<typeof appliedFilters>) => {
    setCurrentPage(0);
    setAppliedFilters((prevFilters) => ({ ...prevFilters, ...newFilters }));
  }, []);

  const saveProduct = async (
    formData: ProductCreationData | ProductUpdateData,
    editingProductId: number | null
  ) => {
    try {
      if (editingProductId) {
        await updateProduct(editingProductId, formData);
      } else {
        await createProduct(formData as ProductCreationData);
      }
      await fetchProducts(); // Re-fetch para atualizar a lista
    } catch (error: unknown) {
      if (error instanceof AxiosError && error.response) {
        throw new Error(error.response.data.message || "Erro do servidor.");
      } else if (error instanceof Error) {
        throw new Error(error.message);
      }
      throw new Error("Erro desconhecido ao salvar produto.");
    }
  };

  const deleteProductById = async (productId: number) => {
    if (window.confirm("Tem a certeza que deseja apagar este produto?")) {
      try {
        await deleteProduct(productId);
        await fetchProducts();
      } catch (error) {
        console.error("Falha ao apagar produto:", error);
      }
    }
  };

  return {
    products,
    meta,
    isLoading,
    error,
    appliedFilters,
    fetchProducts,
    handleFilterChange,
    setCurrentPage,
    saveProduct,
    deleteProductById,
    getProductById,
  };
}