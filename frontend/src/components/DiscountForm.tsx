"use client";

import React, { useState } from "react";
import { Percent, Tag } from "lucide-react";
import Counpons from "./Coupons";
import axios from "axios";
import CancelButton from "@/components/CancelButton";

interface DiscountFormProps {
  onApplyCoupon: (code: string) => Promise<void>;
  onApplyPercentage: (percentage: number) => Promise<void>;
  onRemoveDiscount: () => Promise<void>;
  onCancel: () => void;
  activeDiscount: {
    value: number;
    type: string;
  } | null;
  appliedCouponCode?: string | null;
}

const getFriendlyErrorMessage = (error: unknown): string => {
  if (axios.isAxiosError(error) && error.response) {
    return error.response.data.message;
  }

  if (error instanceof Error) {
    return error.message;
  }
  return "Ocorreu um erro desconhecido. Verifique sua conexão e tente novamente.";
};

export default function DiscountForm({
  onApplyCoupon,
  onApplyPercentage,
  onRemoveDiscount,
  onCancel,
  activeDiscount,
  appliedCouponCode,
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
      setError(getFriendlyErrorMessage(err));
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
      setError(getFriendlyErrorMessage(err));
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
      setError(getFriendlyErrorMessage(err));
    } finally {
      setIsSubmitting(false);
    }
  };

  // Se o produto já tem um desconto, mostramos os detalhes e a opção de remover.
  if (activeDiscount) {
    const discountMessage = appliedCouponCode
      ? `Este produto tem o cupom "${appliedCouponCode}" aplicado, concedendo ${activeDiscount.value}% de desconto.`
      : `Este produto já tem um desconto de ${activeDiscount.value}% aplicado.`;

    return (
      <div>
        <p className="text-sm text-slate-600 mb-2">{discountMessage}</p>
        <p className="text-sm text-slate-600 mb-4">Deseja removê-lo?</p>

        {error && (
          <p className="text-red-500 text-sm mb-4 text-center">{error}</p>
        )}
        <div className="flex justify-end space-x-3 mt-6">
          <CancelButton onClick={onCancel}></CancelButton>
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
      <div className="grid grid-cols-2 gap-2 rounded-lg  p-1 mt-1">
        <button
          onClick={() => setActiveTab("coupon")}
          className={`flex items-center justify-center gap-2 px-4 py-2 rounded-md text-sm font-medium transition-colors duration-200 ${
            activeTab === "coupon"
              ? "bg-slate-900 text-white"
              : "bg-white text-slate-800 border border-slate-200 hover:bg-slate-50"
          }`}
        >
          <Tag className="h-6 w-4" />
          <span className="leading-none">Código Cupom</span>
        </button>

        <button
          onClick={() => setActiveTab("percent")}
          className={`flex items-center justify-center w-full py-2 px-4 text-sm font-medium rounded-md transition-colors duration-200 ${
            activeTab === "percent"
              ? "bg-slate-900 text-white"
              : "bg-white text-slate-800 border border-slate-200 hover:bg-slate-50"
          }`}
        >
          <Percent className="h-6 w-4 mr-2" />
          Percentual Direto
        </button>
      </div>
      <hr className="my-6 mx-auto w-3.9/4 border-t border-slate-200" />

      {activeTab === "coupon" && (
        <form onSubmit={handleCouponSubmit}>
          <label
            htmlFor="coupon-code"
            className="block text-sm font-semibold text-slate-900 mb-2"
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
            className="block w-full rounded-md border border-slate-200 bg-white placeholder-slate-400 text-slate-900 focus:border-slate-300 focus:ring-0 sm:text-sm px-4 py-2"
          />
          <Counpons setCouponCode={setCouponCode} />
          {error && (
            <p className="text-red-500 text-sm mt-4 text-center">{error}</p>
          )}
          <div className="mt-8 flex justify-end space-x-3">
            <CancelButton onClick={onCancel}></CancelButton>
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
            className="block text-sm font-semibold text-slate-900 mb-2"
          >
            Percentual de desconto
          </label>
          <input
            type="number"
            step="0.01"
            min="1"
            max="80"
            id="percentage"
            placeholder="Ex: 10%"
            value={percentage}
            onChange={(e) => setPercentage(e.target.value)}
            required
            className="block w-full rounded-md border border-slate-200 bg-white placeholder-slate-400 text-slate-900 focus:border-slate-300 focus:ring-0 sm:text-sm px-4 py-2"
          />
          <p className="text-xs text-slate-500 mt-1">
            Digite um valor entre 1% e 80%.
          </p>
          <div className="mt-8 flex justify-end space-x-3">
            <CancelButton onClick={onCancel}></CancelButton>
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
    </>
  );
}
