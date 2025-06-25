'use client';

import { Meta } from "@/types";

interface PaginationProps {
    meta: Meta | null;
    onPageChange: (page: number) => void;
}

export default function Pagination({ meta, onPageChange }: PaginationProps) {
    if (!meta || meta.totalPages <= 1) {
        return null; // Não mostra a paginação se não houver ou se for apenas uma página
    }

    const handlePrev = () => {
        if (meta.page > 0) {
            onPageChange(meta.page - 1);
        }
    };

    const handleNext = () => {
        if (meta.page < meta.totalPages - 1) {
            onPageChange(meta.page + 1);
        }
    };

    return (
        <div className="mt-6 flex items-center justify-between">
            <p className="text-sm text-slate-600">
                Página {meta.page + 1} de {meta.totalPages} (Total: {meta.totalItems} produtos)
            </p>
            <div className="flex space-x-2">
                <button 
                    onClick={handlePrev} 
                    disabled={meta.page === 0}
                    className="px-4 py-2 text-sm font-medium text-slate-700 bg-white border border-slate-300 rounded-md hover:bg-slate-50 disabled:opacity-50 disabled:cursor-not-allowed"
                >
                    Anterior
                </button>
                <button 
                    onClick={handleNext}
                    disabled={meta.page >= meta.totalPages - 1}
                    className="px-4 py-2 text-sm font-medium text-slate-700 bg-white border border-slate-300 rounded-md hover:bg-slate-50 disabled:opacity-50 disabled:cursor-not-allowed"
                >
                    Próximo
                </button>
            </div>
        </div>
    );
}