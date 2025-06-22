'use client'; 

import { LayoutDashboard, ShoppingBag, BarChart3, Settings, LogOut } from 'lucide-react';

export default function Sidebar() {
    return (
        <aside className="w-64 flex-shrink-0 bg-white border-r border-slate-200 flex flex-col">
            <div className="h-16 flex items-center px-6 border-b border-slate-200">
                <h1 className="text-xl font-bold text-slate-800">grupo G</h1>
            </div>
            <nav className="flex-1 px-4 py-4 space-y-2">
                <a href="#" className="flex items-center px-4 py-2 text-slate-700 rounded-lg hover:bg-slate-100">
                    <LayoutDashboard className="h-5 w-5 mr-3" />
                    Dashboard
                </a>
                <a href="#" className="flex items-center px-4 py-2 text-white bg-slate-900 rounded-lg">
                    <ShoppingBag className="h-5 w-5 mr-3" />
                    Produtos
                </a>
                <a href="#" className="flex items-center px-4 py-2 text-slate-700 rounded-lg hover:bg-slate-100">
                    <BarChart3 className="h-5 w-5 mr-3" />
                    Relatórios
                </a>
                <a href="#" className="flex items-center px-4 py-2 text-slate-700 rounded-lg hover:bg-slate-100">
                    <Settings className="h-5 w-5 mr-3" />
                    Administração
                </a>
            </nav>
            <div className="px-4 py-4 border-t border-slate-200">
                <a href="#" className="flex items-center px-4 py-2 text-slate-700 rounded-lg hover:bg-slate-100">
                    <LogOut className="h-5 w-5 mr-3" />
                    Sair
                </a>
            </div>
        </aside>
    );
}