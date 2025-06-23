"use client";

import React, { useState } from "react";
import { Percent, Tag } from "lucide-react";

interface DiscountFormProps {
  onApplyCoupon: (code: string) => Promise<void>;
  onApplyPercentage: (percentage: number) => Promise<void>;
  onRemoveDiscount: () => Promise<void>;
  onCancel: () => void;
  hasActiveDiscount: boolean;
}

export default function DiscountForm({
  onApplyCoupon,
  onApplyPercentage,
  onRemoveDiscount,
  onCancel,
  hasActiveDiscount,
}: DiscountFormProps) {
  const [activeTab, setActiveTab] = useState<"coupon" | "percent">("coupon");
  const [couponCode, setCouponCode] = useState("");
  const [percentage, setPercentage] = useState("");
  const [error, setError] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleCouponSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");
    setIsSubmitting(true);
    try {
      await onApplyCoupon(couponCode);
    } catch (err: unknown) {
      setError(
        err instanceof Error
          ? err.message
          : "Erro desconhecido ao aplicar cupom."
      );
    } finally {
      setIsSubmitting(false);
    }
  };

  const handlePercentageSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");
    setIsSubmitting(true);
    try {
      await onApplyPercentage(Number(percentage));
    } catch (err: unknown) {
      setError(
        err instanceof Error
          ? err.message
          : "Erro desconhecido ao aplicar percentual."
      );
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleRemoveClick = async () => {
    setError("");
    setIsSubmitting(true);
    try {
      await onRemoveDiscount();
    } catch (err: unknown) {
      setError(
        err instanceof Error
          ? err.message
          : "Erro desconhecido ao remover desconto."
      );
    } finally {
      setIsSubmitting(false);
    }
  };

  // Se o produto já tem um desconto, mostramos a opção de remover.
  if (hasActiveDiscount) {
    return (
      <div>
        <p className="text-sm text-slate-600 mb-4">
          Este produto já tem um desconto ativo. Deseja removê-lo?
        </p>
        {error && <p className="text-red-500 text-sm mb-4">{error}</p>}
        <div className="flex justify-end space-x-3 mt-6">
          <button
            type="button"
            onClick={onCancel}
            className="bg-white py-2 px-4 border border-slate-300 rounded-md shadow-sm text-sm font-medium text-slate-700 hover:bg-slate-50 disabled:opacity-50"
          >
            Cancelar
          </button>
          <button
            onClick={handleRemoveClick}
            disabled={isSubmitting}
            className="inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-red-600 hover:bg-red-700 disabled:bg-red-300"
          >
            {isSubmitting ? "Removendo..." : "Remover Desconto"}
          </button>
        </div>
      </div>
    );
  }

  // Se não tem desconto, mostramos as abas para aplicar um novo.
  return (
    <>
      <div className="grid grid-cols-2 gap-2 rounded-lg bg-slate-100 p-1 mb-6">
        <button
          onClick={() => setActiveTab("coupon")}
          className={`flex items-center gap-2 px-4 py-2 rounded-md border text-sm font-medium transition-colors duration-200 ${
            activeTab === "coupon"
              ? "bg-slate-900 text-white border-slate-900"
              : "text-slate-700 border-slate-300 hover:bg-slate-100"
          }`}
        >
          <Tag className="h-4 w-4 mr-2" />
          Código Cupom
        </button>
        <button
          onClick={() => setActiveTab("percent")}
          className={`flex items-center justify-center w-full py-2 px-4 text-sm font-medium rounded-md transition-colors ${
            activeTab === "percent"
              ? "bg-slate-900 text-white border-slate-900"
              : "text-slate-700 border-slate-300 hover:bg-slate-100"
          }`}
        >
          <Percent className="h-4 w-4 mr-2" />
          Percentual Direto
        </button>
      </div>

      {activeTab === "coupon" && (
        <form onSubmit={handleCouponSubmit}>
          <label
            htmlFor="coupon-code"
            className="block text-sm font-medium text-slate-700 mb-1"
          >
            Código do Cupom
          </label>
          <input
            type="text"
            id="coupon-code"
            placeholder="Digite o código do cupom"
            value={couponCode}
            onChange={(e) => setCouponCode(e.target.value.toUpperCase())}
            required
            className="block w-full rounded-md border-slate-300 shadow-sm focus:border-amber-500 focus:ring-amber-500 sm:text-sm"
          />
          <div className="mt-8 flex justify-end space-x-3">
            <button
              type="button"
              onClick={onCancel}
              className="bg-white py-2 px-4 border border-slate-300 rounded-md shadow-sm text-sm font-medium text-slate-700 hover:bg-slate-50 disabled:opacity-50"
            >
              Cancelar
            </button>
            <button
              type="submit"
              disabled={isSubmitting || !couponCode}
              className="inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-slate-800 hover:bg-slate-700 disabled:bg-slate-400"
            >
              {isSubmitting ? "Aplicando..." : "Aplicar"}
            </button>
          </div>
        </form>
      )}

      {activeTab === "percent" && (
        <form onSubmit={handlePercentageSubmit}>
          <label
            htmlFor="percentage"
            className="block text-sm font-medium text-slate-700 mb-1"
          >
            Percentual de desconto
          </label>
          <input
            type="number"
            step="0.01"
            min="1"
            max="80"
            id="percentage"
            placeholder="Ex: 10"
            value={percentage}
            onChange={(e) => setPercentage(e.target.value)}
            required
            className="block w-full rounded-md border-slate-300 shadow-sm focus:border-amber-500 focus:ring-amber-500 sm:text-sm"
          />
          <p className="text-xs text-slate-500 mt-1">
            Digite um valor entre 1 e 80.
          </p>
          <div className="mt-8 flex justify-end space-x-3">
            <button
              type="button"
              onClick={onCancel}
              className="bg-white py-2 px-4 border border-slate-300 rounded-md shadow-sm text-sm font-medium text-slate-700 hover:bg-slate-50 disabled:opacity-50"
            >
              Cancelar
            </button>
            <button
              type="submit"
              disabled={isSubmitting || !percentage}
              className="inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-slate-800 hover:bg-slate-700 disabled:bg-slate-400"
            >
              {isSubmitting ? "Aplicando..." : "Aplicar"}
            </button>
          </div>
        </form>
      )}
      {error && (
        <p className="text-red-500 text-sm mt-4 text-center">{error}</p>
      )}
    </>
  );
}
