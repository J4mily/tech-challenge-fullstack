'use client';

import { useState, useEffect } from 'react';
import Header from '@/components/Header';
import ProductControls from '@/components/ProductControls';
import ProductTable from '@/components/ProductTable';
import Sidebar from '@/components/Sidebar';
import { Product, Meta } from '@/types';
import { getProducts } from '@/services/api';

export default function HomePage() {
  const [products, setProducts] = useState<Product[]>([]);
  const [meta, setMeta] = useState<Meta | null>(null);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchInitialProducts = async () => {
      setIsLoading(true);
      setError(null);
      try {
        const params = new URLSearchParams({ page: '0', size: '10', sort: 'name,asc' });
        const response = await getProducts(params);
        setProducts(response.data);
        setMeta(response.meta);
      } catch (err) {
        setError('Falha ao carregar os produtos. A API do backend está em execução?');
        console.error(err);
      } finally {
        setIsLoading(false);
      }
    };

    fetchInitialProducts();
  }, []);

  return (
    <div className="flex h-screen bg-slate-50 text-slate-900">
      <Sidebar />
      <div className="flex-1 flex flex-col overflow-hidden">
        <Header />
        <main className="flex-1 overflow-y-auto p-8">
          <h2 className="text-3xl font-bold mb-8">Produtos</h2>
          <ProductControls />
          <ProductTable 
            products={products} 
            isLoading={isLoading} 
            error={error} 
          />
        </main>
      </div>
    </div>
  );
}