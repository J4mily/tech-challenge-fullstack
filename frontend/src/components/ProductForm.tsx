"use client";

import React, { useState, useEffect } from "react";
import { ProductCreationData, ProductUpdateData } from "@/services/api";

interface ProductFormProps {
  onSave: (
    productData: ProductCreationData | ProductUpdateData
  ) => Promise<void>;
  onCancel: () => void;
  initialData?: Partial<ProductCreationData> | null;
  isEditing: boolean;
}

export default function ProductForm({
  onSave,
  onCancel,
  initialData = null,
  isEditing,
}: ProductFormProps) {
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [price, setPrice] = useState("");
  const [stock, setStock] = useState("");
  const [error, setError] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);

  // Efeito para preencher o formulário quando estiver no modo de edição
  useEffect(() => {
    if (isEditing && initialData) {
      setName(initialData.name || "");
      setDescription(initialData.description || "");
      setPrice(String(initialData.price || ""));
      setStock(String(initialData.stock || ""));
    }
  }, [initialData, isEditing]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");
    setIsSubmitting(true);

    const productData = {
      name,
      description: description || undefined, // Envia undefined se for vazio
      price: Number(price),
      stock: Number(stock),
    };

    try {
      await onSave(productData);
    } catch (err: unknown) {
      setError(
        err instanceof Error
          ? err.message
          : "Ocorreu um erro desconhecido ao salvar."
      );
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="bg-white p-6 rounded-lg shadow-sm">
      <div className="flex items-baseline border-b border-slate-200 pb-5 mb-6">
        <h3 className="text-base font-semibold leading-6 text-slate-900">
          Dados do produto |
        </h3>
        <p className="ml-2 text-sm text-slate-500 italic">
          Os campos com * são obrigatórios para o cadastro.
        </p>
      </div>

      <form onSubmit={handleSubmit} className="space-y-6">
        <div className="grid grid-cols-1 gap-x-6 gap-y-8 sm:grid-cols-2">
          <div>
            <label
              htmlFor="product-name"
              className="block text-sm font-medium leading-6 text-slate-900"
            >
              Nome do produto <span className="text-red-500">*</span>
            </label>
            <div className="mt-2">
              <input
                type="text"
                id="product-name"
                placeholder=" Informe o nome do produto"
                value={name}
                onChange={(e) => setName(e.target.value)}
                required
                className="block w-full rounded-md border-0 px-3 py-1.5 text-slate-900 shadow-sm ring-1 ring-inset ring-slate-300 placeholder:text-slate-400 focus:ring-2 focus:ring-inset focus:ring-slate-600 sm:text-sm sm:leading-6"
              />
            </div>
          </div>
          {/* O campo Categoria pode ser adicionado aqui no futuro se necessário */}
        </div>
        <div>
          <label
            htmlFor="product-description"
            className="block text-sm font-medium leading-6 text-slate-900"
          >
            Descrição <span className="text-red-500">*</span>
          </label>
          <div className="mt-2">
            <textarea
              id="product-description"
              placeholder=" Descrição detalhada do produto"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              rows={3}
              className="block w-full rounded-md border-0 px-3 py-1.5 text-slate-900 shadow-sm ring-1 ring-inset ring-slate-300 placeholder:text-slate-400 focus:ring-2 focus:ring-inset focus:ring-slate-600 sm:text-sm sm:leading-6"
            ></textarea>
          </div>
        </div>
        <div className="grid grid-cols-1 gap-x-6 gap-y-8 sm:grid-cols-2">
          <div>
            <label
              htmlFor="product-price"
              className="block text-sm font-medium leading-6 text-slate-900"
            >
              Preço <span className="text-red-500">*</span>
            </label>
            <div className="mt-2">
              <input
                type="number"
                step="0.01"
                min="0.01"
                id="product-price"
                placeholder=" R$ 0,00"
                value={price}
                onChange={(e) => setPrice(e.target.value)}
                required
                className="block w-full rounded-md border-0 px-3 py-1.5 text-slate-900 shadow-sm ring-1 ring-inset ring-slate-300 placeholder:text-slate-400 focus:ring-2 focus:ring-inset focus:ring-slate-600 sm:text-sm sm:leading-6"
              />
            </div>
          </div>
          <div>
            <label
              htmlFor="product-stock"
              className="block text-sm font-medium leading-6 text-slate-900"
            >
              Estoque <span className="text-red-500">*</span>
            </label>
            <div className="mt-2">
              <input
                type="number"
                min="0"
                id="product-stock"
                placeholder=" 0"
                value={stock}
                onChange={(e) => setStock(e.target.value)}
                required
                className="block w-full rounded-md border-0 px-3 py-1.5 text-slate-900 shadow-sm ring-1 ring-inset ring-slate-300 placeholder:text-slate-400 focus:ring-2 focus:ring-inset focus:ring-slate-600 sm:text-sm sm:leading-6"
              />
            </div>
          </div>
        </div>
        {error && <p className="text-red-500 text-sm mt-4">{error}</p>}
        <div className="mt-8 flex justify-end space-x-3">
          <button
            type="button"
            onClick={onCancel}
            className="bg-white py-2 px-4 border border-slate-300 rounded-md shadow-sm text-sm font-medium text-slate-700 hover:bg-slate-50"
          >
            Cancelar
          </button>
          <button
            type="submit"
            disabled={isSubmitting}
            className="inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-slate-800 hover:bg-slate-700 disabled:bg-slate-400"
          >
            {isSubmitting
              ? "Salvando..."
              : isEditing
              ? "Salvar Alterações"
              : "Cadastrar"}
          </button>
        </div>
      </form>
    </div>
  );
}
