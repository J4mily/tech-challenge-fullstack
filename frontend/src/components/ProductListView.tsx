"use client";

import ProductControls from "@/components/ProductControls";
import ProductTable from "@/components/ProductTable";
import Pagination from "@/components/Pagination";
import { Product, Meta } from "@/types";
import { ShoppingBag } from "lucide-react";

interface ProductListViewProps {
  products: Product[];
  meta: Meta | null;
  isLoading: boolean;
  error: string | null;
  onFilterChange: (filters: { search?: string; minPrice?: string; maxPrice?: string }) => void;
  onPageChange: (page: number) => void;
  onCreate: () => void;
  onEdit: (productId: number) => void;
  onDelete: (productId: number) => void;
  onApplyDiscount: (product: Product) => void;
}

export default function ProductListView({
  products,
  meta,
  isLoading,
  error,
  onFilterChange,
  onPageChange,
  onCreate,
  onEdit,
  onDelete,
  onApplyDiscount,
}: ProductListViewProps) {
  return (
    <>
      <div className="flex items-center gap-3 mb-8">
        <ShoppingBag className="h-8 w-8 text-slate-500" />
        <h2 className="text-3xl font-bold">Produtos</h2>
      </div>
      <ProductControls onCreate={onCreate} onFilterChange={onFilterChange} />
      <ProductTable
        products={products}
        isLoading={isLoading}
        error={error}
        onEdit={onEdit}
        onDelete={onDelete}
        onApplyDiscount={onApplyDiscount}
      />
      <Pagination meta={meta} onPageChange={onPageChange} />
    </>
  );
}