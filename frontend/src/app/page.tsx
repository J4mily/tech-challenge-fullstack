'use client';

import { useState, useEffect, useCallback } from 'react';
import Header from '@/components/Header';
import ProductControls from '@/components/ProductControls';
import ProductTable from '@/components/ProductTable';
import Sidebar from '@/components/Sidebar';
import Pagination from '@/components/Pagination';
import Modal from '@/components/Modal'; 
import ProductForm from '@/components/ProductForm';
import { Product, Meta } from '@/types';
import { getProducts, createProduct, ProductCreationData } from '@/services/api';
import { AxiosError } from 'axios';

export default function HomePage() {
  const [products, setProducts] = useState<Product[]>([]);
  const [meta, setMeta] = useState<Meta | null>(null);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  
  const [isModalOpen, setIsModalOpen] = useState(false);

  const [currentPage, setCurrentPage] = useState<number>(0);
  const [appliedFilters, setAppliedFilters] = useState({
    search: '',
    minPrice: '',
    maxPrice: '',
  });

  const fetchProducts = useCallback(async () => {
    setIsLoading(true);
    setError(null);
    try {
      const params = new URLSearchParams();
      params.append('page', String(currentPage));
      params.append('size', '10');
      params.append('sort', 'name,asc');
      if (appliedFilters.search) params.append('search', appliedFilters.search);
      if (appliedFilters.minPrice) params.append('minPrice', appliedFilters.minPrice);
      if (appliedFilters.maxPrice) params.append('maxPrice', appliedFilters.maxPrice);

      const response = await getProducts(params);
      setProducts(response.data);
      setMeta(response.meta);
    } catch (err) {
      setError('Falha ao carregar os produtos. A API do backend está em execução?');
      console.error(err);
    } finally {
      setIsLoading(false);
    }
  }, [currentPage, appliedFilters]);

  useEffect(() => {
    fetchProducts();
  }, [fetchProducts]);
  
  const handleFilterChange = useCallback((newFilters: Partial<typeof appliedFilters>) => {
    setCurrentPage(0);
    setAppliedFilters(prevFilters => ({ ...prevFilters, ...newFilters }));
  }, []);

  const handleSaveProduct = async (productData: ProductCreationData) => {
    try {
        await createProduct(productData);
        setIsModalOpen(false);
        await fetchProducts(); // Usamos await para garantir que a lista é atualizada
    } catch (error: unknown) {
        console.error("Erro ao criar produto:", error);
        
        if (error instanceof AxiosError && error.response) {
            throw new Error(error.response.data.message || "Erro do servidor.");
        } else if (error instanceof Error) {
            throw new Error(error.message);
        } else {
            throw new Error("Erro desconhecido ao criar produto.");
        }
    }
  };

  return (
    <div className="flex h-screen bg-slate-50 text-slate-900">
      <Sidebar />
      <div className="flex-1 flex flex-col overflow-hidden">
        <Header />
        <main className="flex-1 overflow-y-auto p-8">
          <h2 className="text-3xl font-bold mb-8">Produtos</h2>
          <ProductControls onCreate={() => setIsModalOpen(true)} onFilterChange={handleFilterChange} />
          <ProductTable 
            products={products} 
            isLoading={isLoading} 
            error={error} 
          />
          <Pagination 
            meta={meta}
            onPageChange={setCurrentPage}
          />
        </main>
      </div>

      <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} title="Adicionar Novo Produto">
        <ProductForm onSave={handleSaveProduct} onCancel={() => setIsModalOpen(false)} />
      </Modal>
    </div>
  );
}