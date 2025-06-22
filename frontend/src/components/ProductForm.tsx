'use client';

import React, { useState } from 'react';
import { ProductCreationData } from '@/services/api';

interface ProductFormProps {
    onSave: (productData: ProductCreationData) => Promise<void>;
    onCancel: () => void;
    initialData?: Partial<ProductCreationData> | null;
}

export default function ProductForm({ onSave, onCancel, initialData = null }: ProductFormProps) {
    const [name, setName] = useState(initialData?.name || '');
    const [description, setDescription] = useState(initialData?.description || '');
    const [price, setPrice] = useState(initialData?.price || '');
    const [stock, setStock] = useState(initialData?.stock || '');
    const [error, setError] = useState('');

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');
        
        const productData = {
            name,
            description: description || '',
            price: Number(price),
            stock: Number(stock)
        };

        try {
            await onSave(productData);
        } catch (err: unknown) {
            if (err instanceof Error) {
                setError(err.message);
            } else {
                setError('Ocorreu um erro desconhecido ao salvar.');
            }
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <div className="space-y-4">
                <div>
                    <label htmlFor="product-name" className="block text-sm font-medium text-slate-700">Nome do Produto</label>
                    <input type="text" id="product-name" value={name} onChange={(e) => setName(e.target.value)} required className="mt-1 block w-full rounded-md border-slate-300 shadow-sm focus:border-amber-500 focus:ring-amber-500 sm:text-sm" />
                </div>
                <div>
                    <label htmlFor="product-description" className="block text-sm font-medium text-slate-700">Descrição</label>
                    <textarea id="product-description" value={description} onChange={(e) => setDescription(e.target.value)} rows={3} className="mt-1 block w-full rounded-md border-slate-300 shadow-sm focus:border-amber-500 focus:ring-amber-500 sm:text-sm"></textarea>
                </div>
                <div className="grid grid-cols-2 gap-4">
                    <div>
                        <label htmlFor="product-price" className="block text-sm font-medium text-slate-700">Preço (R$)</label>
                        <input type="number" step="0.01" min="0.01" id="product-price" value={price} onChange={(e) => setPrice(e.target.value)} required className="mt-1 block w-full rounded-md border-slate-300 shadow-sm focus:border-amber-500 focus:ring-amber-500 sm:text-sm" />
                    </div>
                    <div>
                        <label htmlFor="product-stock" className="block text-sm font-medium text-slate-700">Estoque</label>
                        <input type="number" min="0" id="product-stock" value={stock} onChange={(e) => setStock(e.target.value)} required className="mt-1 block w-full rounded-md border-slate-300 shadow-sm focus:border-amber-500 focus:ring-amber-500 sm:text-sm" />
                    </div>
                </div>
            </div>
            {error && <p className="text-red-500 text-sm mt-4">{error}</p>}
            <div className="mt-6 flex justify-end space-x-3">
                <button type="button" onClick={onCancel} className="bg-white py-2 px-4 border border-slate-300 rounded-md shadow-sm text-sm font-medium text-slate-700 hover:bg-slate-50 focus:outline-none">Cancelar</button>
                <button type="submit" className="inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-slate-800 hover:bg-slate-700 focus:outline-none">Salvar Produto</button>
            </div>
        </form>
    );
}