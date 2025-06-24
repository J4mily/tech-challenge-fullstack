"use client";

import { useState, useEffect } from "react";
import Header from "@/components/Header";
import Sidebar from "@/components/Sidebar";
import Modal from "@/components/Modal";
import DiscountForm from "@/components/DiscountForm";
import ProductListView from "@/components/ProductListView";
import ProductFormView from "@/components/ProductFormView";
import { useProductManagement } from "@/hooks/useProductManagement";
import { useDiscountModal } from "@/hooks/useDiscountModal";
import { Product, ProductCreationData, ProductUpdateData } from "@/types";
import { Tag } from "lucide-react";

export default function HomePage() {
  const [view, setView] = useState<"list" | "form">("list");
  const [editingProduct, setEditingProduct] = useState<Product | null>(null);

  const {
    products,
    meta,
    isLoading,
    error,
    fetchProducts,
    handleFilterChange,
    setCurrentPage,
    saveProduct,
    deleteProductById,
    getProductById,
  } = useProductManagement();

  const {
    isDiscountModalOpen,
    productForDiscount,
    openDiscountModal,
    closeDiscountModal,
    handleApplyCoupon,
    handleApplyPercentage,
    handleRemoveDiscount,
  } = useDiscountModal({ onSuccess: fetchProducts });

  // Busca os produtos quando a view de lista for ativada
  useEffect(() => {
    if (view === "list") {
      fetchProducts();
    }
  }, [view, fetchProducts]);

  // --- Handlers de Navegação/Visão ---

  const handleCreateClick = () => {
    setEditingProduct(null);
    setView("form");
  };

  const handleEditClick = async (productId: number) => {
    try {
      const productToEdit = await getProductById(productId);
      setEditingProduct(productToEdit);
      setView("form");
    } catch (error) {
      console.error("Falha ao buscar produto para edição:", error);
    }
  };

  const handleCancelForm = () => {
    setEditingProduct(null);
    setView("list");
  };

  const handleSaveForm = async (
    formData: ProductCreationData | ProductUpdateData
  ) => {
    await saveProduct(formData, editingProduct ? editingProduct.id : null);
    setView("list");
    setEditingProduct(null);
  };

  return (
    <div className="flex h-screen bg-slate-50 text-slate-900">
      <Sidebar />
      <div className="flex-1 flex flex-col overflow-hidden">
        <Header />
        <main className="flex-1 overflow-y-auto p-8">
          {view === "list" ? (
            <ProductListView
              products={products}
              meta={meta}
              isLoading={isLoading}
              error={error}
              onFilterChange={handleFilterChange}
              onPageChange={setCurrentPage}
              onCreate={handleCreateClick}
              onEdit={handleEditClick}
              onDelete={deleteProductById}
              onApplyDiscount={openDiscountModal}
            />
          ) : (
            <ProductFormView
              onSave={handleSaveForm}
              onCancel={handleCancelForm}
              initialData={editingProduct}
            />
          )}
        </main>
      </div>

      <Modal
        isOpen={isDiscountModalOpen}
        onClose={closeDiscountModal}
        title={
          <div className="flex items-center">
            <Tag className="h-5 w-5 mr-2" />
            Aplicar Desconto
          </div>
        }
        description={
          productForDiscount?.discount
            ? null
            : "Escolha como aplicar o desconto ao produto"
        }
      >
        {productForDiscount && (
          <DiscountForm
            onApplyCoupon={handleApplyCoupon}
            onApplyPercentage={handleApplyPercentage}
            onRemoveDiscount={handleRemoveDiscount}
            onCancel={closeDiscountModal}
            activeDiscount={productForDiscount.discount}
            appliedCouponCode={productForDiscount.couponCode}
          />
        )}
      </Modal>
    </div>
  );
}
