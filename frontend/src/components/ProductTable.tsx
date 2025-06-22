'use client';

import { Pencil, Trash2, DollarSign } from 'lucide-react';

export default function ProductTable() {
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
                    {/* Linha de Exemplo 1 */}
                    <tr className="hover:bg-slate-50">
                        <td className="p-4">Smartphone XYZ</td>
                        <td className="p-4 text-slate-600">Smartphone premium com...</td>
                        <td className="p-4">
                            <div>R$ 1799,99</div>
                            <div className="text-xs text-green-600 line-through">R$ 1999,99</div>
                        </td>
                        <td className="p-4">50</td>
                        <td className="p-4">
                            <div className="flex space-x-4">
                                <Pencil className="h-5 w-5 text-slate-500 hover:text-amber-600 cursor-pointer" />
                                <DollarSign className="h-5 w-5 text-slate-500 hover:text-green-600 cursor-pointer" />
                                <Trash2 className="h-5 w-5 text-slate-500 hover:text-red-600 cursor-pointer" />
                            </div>
                        </td>
                    </tr>
                    {/* Linha de Exemplo 2 (Esgotado) */}
                    <tr className="hover:bg-slate-50">
                        <td className="p-4">Camiseta Casual</td>
                        <td className="p-4 text-slate-600">Camisa polo casual masc...</td>
                        <td className="p-4">R$ 69,90</td>
                        <td className="p-4">
                            <span className="px-2 py-1 text-xs font-semibold text-red-800 bg-red-100 rounded-full">Esgotado</span>
                        </td>
                        <td className="p-4">
                            <div className="flex space-x-4">
                                <Pencil className="h-5 w-5 text-slate-500 hover:text-amber-600 cursor-pointer" />
                                <DollarSign className="h-5 w-5 text-slate-500 hover:text-green-600 cursor-pointer" />
                                <Trash2 className="h-5 w-5 text-slate-500 hover:text-red-600 cursor-pointer" />
                            </div>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    );
}