"use client";

import ProductForm from "@/components/ProductForm";
import { Product, ProductCreationData, ProductUpdateData } from "@/types";
import { FilePenLine } from "lucide-react";

interface ProductFormViewProps {
  onSave: (formData: ProductCreationData | ProductUpdateData) => Promise<void>;
  onCancel: () => void;
  initialData: Product | null;
}

export default function ProductFormView({
  onSave,
  onCancel,
  initialData,
}: ProductFormViewProps) {
  const isEditing = !!initialData;

  return (
    <>
      <div className="flex items-center gap-3 mb-8">
        <FilePenLine className="h-9 w-9 text-slate-500" />
        <h2 className="text-3xl font-bold">
          {isEditing ? "Editar Produto" : "Cadastro de Produto"}
        </h2>
      </div>
      <ProductForm
        onSave={onSave}
        onCancel={onCancel}
        initialData={initialData}
        isEditing={isEditing}
      />
    </>
  );
}