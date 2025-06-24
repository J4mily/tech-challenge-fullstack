"use client";

import { useState } from "react";
import { Product } from "@/types";
import {
  applyCouponDiscount,
  applyPercentageDiscount,
  removeDiscount,
} from "@/hooks/api";

interface UseDiscountModalProps {
  onSuccess: () => void; // Callback para atualizar a lista de produtos
}

export function useDiscountModal({ onSuccess }: UseDiscountModalProps) {
  const [isDiscountModalOpen, setIsDiscountModalOpen] = useState(false);
  const [productForDiscount, setProductForDiscount] = useState<Product | null>(null);

  const openDiscountModal = (product: Product) => {
    setProductForDiscount(product);
    setIsDiscountModalOpen(true);
  };

  const closeDiscountModal = () => {
    setIsDiscountModalOpen(false);
    setProductForDiscount(null);
  };

  const handleApplyCoupon = async (code: string) => {
    if (!productForDiscount) return;
    await applyCouponDiscount(productForDiscount.id, code);
    closeDiscountModal();
    onSuccess();
  };

  const handleApplyPercentage = async (percentage: number) => {
    if (!productForDiscount) return;
    await applyPercentageDiscount(productForDiscount.id, percentage);
    closeDiscountModal();
    onSuccess();
  };

  const handleRemoveDiscount = async () => {
    if (!productForDiscount) return;
    await removeDiscount(productForDiscount.id);
    closeDiscountModal();
    onSuccess();
  };

  return {
    isDiscountModalOpen,
    productForDiscount,
    openDiscountModal,
    closeDiscountModal,
    handleApplyCoupon,
    handleApplyPercentage,
    handleRemoveDiscount,
  };
}