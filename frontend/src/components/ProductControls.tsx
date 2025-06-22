'use client';

import { useState, useEffect } from 'react';
import { PlusCircle, Search } from 'lucide-react';
import { useDebounce } from '@/hooks/useDebounce';

interface ProductControlsProps {
  onCreate: () => void;
  onFilterChange: (filters: { search?: string; minPrice?: string; maxPrice?: string; }) => void;
}

export default function ProductControls({ onCreate, onFilterChange }: ProductControlsProps) {
  const [searchTerm, setSearchTerm] = useState('');
  const [minPrice, setMinPrice] = useState('');
  const [maxPrice, setMaxPrice] = useState('');
  
  const debouncedSearchTerm = useDebounce(searchTerm, 500);

  useEffect(() => {
    onFilterChange({ search: debouncedSearchTerm });
  }, [debouncedSearchTerm, onFilterChange]);

  const handleFilterClick = () => {
    onFilterChange({ 
      minPrice: minPrice, 
      maxPrice: maxPrice 
    });
  };
  
  return (
    <div className="bg-white p-4 rounded-lg shadow-sm mb-6 flex items-center space-x-4">
      <div className="flex items-center space-x-2">
          <input 
              type="number" 
              placeholder="R$ Mín." 
              className="w-32 p-2 border border-slate-300 rounded-md text-sm"
              value={minPrice}
              onChange={(e) => setMinPrice(e.target.value)}
          />
          <input 
              type="number" 
              placeholder="R$ Máx." 
              className="w-32 p-2 border border-slate-300 rounded-md text-sm"
              value={maxPrice}
              onChange={(e) => setMaxPrice(e.target.value)}
          />
          <button 
              onClick={handleFilterClick}
              className="px-4 py-2 bg-slate-800 text-white text-sm font-semibold rounded-md hover:bg-slate-700">
              Filtrar
          </button>
      </div>
      
      <div className="flex-1">
          <div className="relative">
              <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-5 w-5 text-slate-400" />
              <input 
                  type="text" 
                  placeholder="Buscar produto..." 
                  className="w-full p-2 pl-10 border border-slate-300 rounded-md text-sm"
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
              />
          </div>
      </div>
      
      <button 
        onClick={onCreate}
        className="flex items-center px-4 py-2 bg-slate-800 text-white text-sm font-semibold rounded-md hover:bg-slate-700">
          <PlusCircle className="h-5 w-5 mr-2" />
          Criar Produto
      </button>
    </div>
  );
}
