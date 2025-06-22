'use client';

import { PlusCircle, Search } from 'lucide-react';

export default function ProductControls() {
    return (
        <div className="bg-white p-4 rounded-lg shadow-sm mb-6 flex items-center space-x-4">
            <div className="flex items-center space-x-2">
                <input type="text" placeholder="R$ 0,00" className="w-32 p-2 border border-slate-300 rounded-md text-sm" />
                <input type="text" placeholder="R$ 999,99" className="w-32 p-2 border border-slate-300 rounded-md text-sm" />
                <button className="px-4 py-2 bg-slate-800 text-white text-sm font-semibold rounded-md hover:bg-slate-700">Filtrar</button>
            </div>
            <div className="flex-1">
                <div className="relative">
                    <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-5 w-5 text-slate-400" />
                    <input type="text" placeholder="Buscar produto..." className="w-full p-2 pl-10 border border-slate-300 rounded-md text-sm" />
                </div>
            </div>
            <button className="flex items-center px-4 py-2 bg-slate-800 text-white text-sm font-semibold rounded-md hover:bg-slate-700">
                <PlusCircle className="h-5 w-5 mr-2" />
                Criar Produto
            </button>
        </div>
    );
}