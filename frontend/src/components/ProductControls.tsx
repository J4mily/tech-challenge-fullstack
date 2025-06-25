"use client";

import { useState, useEffect } from "react";
import { PlusCircle, Search, RotateCw } from "lucide-react";
import { useDebounce } from "@/hooks/useDebounce";

interface ProductControlsProps {
  onCreate: () => void;
  onFilterChange: (filters: {
    search?: string;
    minPrice?: string;
    maxPrice?: string;
  }) => void;
}

export default function ProductControls({
  onCreate,
  onFilterChange,
}: ProductControlsProps) {
  const [searchTerm, setSearchTerm] = useState("");
  const [minPrice, setMinPrice] = useState("");
  const [maxPrice, setMaxPrice] = useState("");
  const [isPriceFiltered, setIsPriceFiltered] = useState(false);

  const debouncedSearchTerm = useDebounce(searchTerm, 500);

  useEffect(() => {
    onFilterChange({ search: debouncedSearchTerm });
  }, [debouncedSearchTerm, onFilterChange]);

  const handleFilterClick = () => {
    onFilterChange({
      search: debouncedSearchTerm,
      minPrice: minPrice,
      maxPrice: maxPrice,
    });
    if (minPrice || maxPrice) {
      setIsPriceFiltered(true);
    }
  };

  const handleClearFilters = () => {
    setMinPrice("");
    setMaxPrice("");
    onFilterChange({
      search: debouncedSearchTerm,
      minPrice: "",
      maxPrice: "",
    });
    setIsPriceFiltered(false);
  };

  const handlePriceChange = (
    value: string,
    setter: (value: string) => void
  ) => {
    if (value === "" || Number(value) >= 0) {
      setter(value);
    }
  };

  return (
    <div className="bg-white p-4 rounded-lg shadow-sm mb-6 flex items-center space-x-4">
      <div className="flex items-center space-x-2">
        <input
          type="number"
          min="0"
          placeholder="R$ Mín."
          className="w-32 p-2 border border-slate-300 rounded-md text-sm"
          value={minPrice}
          onChange={(e) => handlePriceChange(e.target.value, setMinPrice)}
        />
        <input
          type="number"
          min="0"
          placeholder="R$ Máx."
          className="w-32 p-2 border border-slate-300 rounded-md text-sm"
          value={maxPrice}
          onChange={(e) => handlePriceChange(e.target.value, setMaxPrice)}
        />

        {isPriceFiltered ? (
          <button
            onClick={handleClearFilters}
            className="flex flex-row items-center justify-center w-[140px] h-10 gap-[14px] bg-white text-slate-700 text-sm font-normal rounded-md border border-slate-300 hover:bg-slate-50"
          >
            <RotateCw className="h-4 w-4" />
            Limpar filtros
          </button>
        ) : (
          <button
            onClick={handleFilterClick}
            className="flex items-center justify-center w-[72px] h-10 bg-slate-800 text-white text-sm font-semibold rounded-md hover:bg-slate-700"
          >
            Filtrar
          </button>
        )}
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
        className="flex items-center px-4 py-2 bg-slate-800 text-white text-sm font-semibold rounded-md hover:bg-slate-700"
      >
        <PlusCircle className="h-5 w-5 mr-2" />
        Criar Produto
      </button>
    </div>
  );
}
