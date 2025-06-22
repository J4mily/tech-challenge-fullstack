'use client';

import { useState, useEffect, useCallback } from 'react';
import Header from '@/components/Header';
import ProductControls from '@/components/ProductControls';
import ProductTable from '@/components/ProductTable';
import Sidebar from '@/components/Sidebar';
import Pagination from '@/components/Pagination';
import { Product, Meta } from '@/types';
import { getProducts } from '@/services/api';

export default function HomePage() {
  const [products, setProducts] = useState<Product[]>([]);
  const [meta, setMeta] = useState<Meta | null>(null);
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);

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
  
  // useCallback para "memorizar" a função 
  const handleFilterChange = useCallback((newFilters: Partial<typeof appliedFilters>) => {
    setCurrentPage(0); // Sempre reseta para a primeira página ao aplicar um filtro
    setAppliedFilters(prevFilters => ({ ...prevFilters, ...newFilters }));
  }, []); // O array vazio de dependências significa que esta função nunca precisa de ser recriada.

  return (
    <div className="flex h-screen bg-slate-50 text-slate-900">
      <Sidebar />
      <div className="flex-1 flex flex-col overflow-hidden">
        <Header />
        <main className="flex-1 overflow-y-auto p-8">
          <h2 className="text-3xl font-bold mb-8">Produtos</h2>
          <ProductControls onFilterChange={handleFilterChange} />
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
    </div>
  );
}