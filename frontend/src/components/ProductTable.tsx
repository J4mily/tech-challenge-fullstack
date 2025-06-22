'use client';

import { Pencil, Trash2, DollarSign } from 'lucide-react';
import { Product } from '@/types';

// Atualizamos a interface para receber as novas funções
interface ProductTableProps {
  products: Product[];
  isLoading: boolean;
  error: string | null;
  onEdit: (id: number) => void;
  onDelete: (id: number) => void;
}

export default function ProductTable({ products, isLoading, error, onEdit, onDelete }: ProductTableProps) {
    
  if (isLoading) { return <div className="text-center py-10">A carregar produtos...</div>; }
  if (error) { return <div className="text-center py-10 text-red-500">{error}</div>; }
  if (!products || products.length === 0) { return <div className="text-center py-10 text-slate-500">Nenhum produto encontrado.</div>; }

  return (
    <div className="bg-white rounded-lg shadow-sm overflow-hidden">
      <table className="w-full">
        <thead className="bg-slate-50">
            <tr>
                <th className="p-4 text-left text-xs font-semibold text-slate-600 uppercase">Nome</th>
                <th className="p-4 text-left text-xs font-semibold text-slate-600 uppercase">Descrição</th>
                <th className="p-4 text-left text-xs font-semibold text-slate-600 uppercase">Preço</th>
                <th className="p-4 text-left text-xs font-semibold text-slate-600 uppercase">Estoque</th>
                <th className="p-4 text-left text-xs font-semibold text-slate-600 uppercase">Ações</th>
            </tr>
        </thead>
        <tbody className="divide-y divide-slate-200">
          {products.map((product) => (
            <tr key={product.id} className="hover:bg-slate-50">
              <td className="p-4 font-medium text-slate-800">{product.name}</td>
              <td className="p-4 text-slate-600 max-w-xs truncate">{product.description || ''}</td>
              <td className="p-4">
                  {product.discount ? (
                    <>
                      <div>R$ {product.finalPrice.toFixed(2)}</div>
                      <div className="text-xs text-green-600 line-through">R$ {product.price.toFixed(2)}</div>
                    </>
                  ) : (
                    <div>R$ {product.price.toFixed(2)}</div>
                  )}
              </td>
              <td className="p-4">
                {product.is_out_of_stock ? (
                  <span className="px-2 py-1 text-xs font-semibold text-red-800 bg-red-100 rounded-full">Esgotado</span>
                ) : (
                  <span>{product.stock}</span>
                )}
              </td>
              <td className="p-4">
                <div className="flex space-x-4">
                    {/* Adicionamos os eventos onClick aos nossos botões */}
                    <button onClick={() => onEdit(product.id)} title="Editar">
                        <Pencil className="h-5 w-5 text-slate-500 hover:text-amber-600 cursor-pointer" />
                    </button>
                    <button title="Aplicar Desconto">
                        <DollarSign className="h-5 w-5 text-slate-500 hover:text-green-600 cursor-pointer" />
                    </button>
                    <button onClick={() => onDelete(product.id)} title="Apagar">
                        <Trash2 className="h-5 w-5 text-slate-500 hover:text-red-600 cursor-pointer" />
                    </button>
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}