"use client";

import { useState, useEffect, useCallback } from "react";
import Header from "@/components/Header";
import ProductControls from "@/components/ProductControls";
import ProductTable from "@/components/ProductTable";
import Sidebar from "@/components/Sidebar";
import Pagination from "@/components/Pagination";
import ProductForm from "@/components/ProductForm";
import { Product, Meta } from "@/types";
import {
  getProducts,
  createProduct,
  updateProduct,
  deleteProduct,
  getProductById,
  ProductCreationData,
  ProductUpdateData,
} from "@/services/api";
import { AxiosError } from "axios";
import Image from 'next/image';


export default function HomePage() {
  const [products, setProducts] = useState<Product[]>([]);
  const [meta, setMeta] = useState<Meta | null>(null);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [view, setView] = useState<"list" | "form">("list");
  const [editingProduct, setEditingProduct] = useState<Product | null>(null);

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
      if (appliedFilters.minPrice)
        params.append("minPrice", appliedFilters.minPrice);
      if (appliedFilters.maxPrice)
        params.append("maxPrice", appliedFilters.maxPrice);

      const response = await getProducts(params);
      setProducts(response.data);
      setMeta(response.meta);
    } catch (err) {
      setError(
        "Falha ao carregar os produtos. A API do backend está em execução?"
      );
      console.error(err);
    } finally {
      setIsLoading(false);
    }
  }, [currentPage, appliedFilters]);

  useEffect(() => {
    // Só busca os produtos se estivermos na vista de lista
    if (view === "list") {
      fetchProducts();
    }
  }, [view, fetchProducts]);

  const handleFilterChange = useCallback(
    (newFilters: Partial<typeof appliedFilters>) => {
      setCurrentPage(0);
      setAppliedFilters((prevFilters) => ({ ...prevFilters, ...newFilters }));
    },
    []
  );

  const handleEdit = async (productId: number) => {
    try {
      const productToEdit = await getProductById(productId);
      setEditingProduct(productToEdit);
      setView("form");
    } catch (error) {
      console.error("Falha ao buscar produto para edição:", error);
    }
  };

  const handleDelete = async (productId: number) => {
    if (window.confirm("Tem a certeza que deseja apagar este produto?")) {
      try {
        await deleteProduct(productId);
        await fetchProducts();
      } catch (error) {
        console.error("Falha ao apagar produto:", error);
      }
    }
  };

  const handleSaveProduct = async (
    formData: ProductCreationData | ProductUpdateData
  ) => {
    try {
      if (editingProduct) {
        await updateProduct(editingProduct.id, formData);
      } else {
        await createProduct(formData as ProductCreationData);
      }
      setView("list");
    } catch (error: unknown) {
      if (error instanceof AxiosError && error.response) {
        throw new Error(error.response.data.message || "Erro do servidor.");
      } else if (error instanceof Error) {
        throw new Error(error.message);
      }
      throw new Error("Erro desconhecido ao salvar produto.");
    }
  };

  const handleCreateClick = () => {
    setEditingProduct(null);
    setView("form");
  };

  const handleCancelForm = () => {
    setView("list");
  };

  // Função para renderizar o conteúdo principal com base na vista atual
  const renderMainContent = () => {
    if (view === "form") {
      return (
        <>
          <div className="flex items-center gap-3 mb-8">
            <Image
              src="/createFile.svg"
              alt="Ícone de criar arquivo"
              width={40}
              height={40}
              className="w-10 h-10"
            />

            <h2 className="text-3xl font-bold">
              {editingProduct ? "Editar Produto" : "Cadastro de Produto"}
            </h2>
          </div>

          <ProductForm
            onSave={handleSaveProduct}
            onCancel={handleCancelForm}
            initialData={editingProduct}
            isEditing={!!editingProduct}
          />
        </>
      );
    }

    return (
      <>
        <div className="flex items-center gap-3 mb-8">
          <Image
            src="/productFile.svg"
            alt="Ícone produtos"
            width={40}
            height={40}
            className="h-10 w-10"
          />
          <h2 className="text-3xl font-bold">Produtos</h2>
        </div>
        <ProductControls
          onCreate={handleCreateClick}
          onFilterChange={handleFilterChange}
        />
        <ProductTable
          products={products}
          isLoading={isLoading}
          error={error}
          onEdit={handleEdit}
          onDelete={handleDelete}
        />
        <Pagination meta={meta} onPageChange={setCurrentPage} />
      </>
    );
  };

  return (
    <div className="flex h-screen bg-slate-50 text-slate-900">
      <Sidebar />
      <div className="flex-1 flex flex-col overflow-hidden">
        <Header />
        <main className="flex-1 overflow-y-auto p-8">
          {renderMainContent()}
        </main>
      </div>
    </div>
  );
}
