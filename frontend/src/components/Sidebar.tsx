"use client";

import { useState } from "react"; // 1. Importe o useState
import {
  LayoutDashboard,
  ShoppingBag,
  BarChart3,
  Settings,
} from "lucide-react";
import Image from "next/image";

export default function Sidebar() {
  const [isCollapsed, setIsCollapsed] = useState(false);

  const toggleSidebar = () => {
    setIsCollapsed(!isCollapsed);
  };

  return (
    <aside
      className={`flex-shrink-0 bg-white border-r border-slate-200 flex flex-col transition-all duration-300 ${
        isCollapsed ? "w-20" : "w-64"
      }`}
    >
      {!isCollapsed ? (
        <div className="h-16 flex items-center px-6 border-b border-slate-200">
          <button
            onClick={toggleSidebar}
            className="flex items-center justify-center w-full"
          >
            <Image
              src="/groupLogo.svg"
              alt="Logo do Grupo G"
              width={100}
              height={50}
              className="mr-2 transition-all duration-300"
            />
          </button>
        </div>
      ) : (
        <div className="h-16 flex items-center px-6 border-b border-slate-200">
          <button
            onClick={toggleSidebar}
            className="flex items-center justify-center w-full"
          >
            <Image
              src="/iconLogo.svg"
              alt="Logo do Grupo G"
              width={60}
              height={30}
              className="mr-2 transition-all duration-300"
            />
          </button>
        </div>
      )}
      <nav className="flex-1 px-4 py-4 space-y-2">
        <a
          href="#"
          className="flex items-center px-4 py-2 text-slate-700 rounded-lg hover:bg-slate-100"
        >
          <LayoutDashboard className="h-5 w-5" />
          {!isCollapsed && <span className="ml-3">Dashboard</span>}
        </a>
        <a
          href="#"
          className="flex items-center px-4 py-2 text-white bg-slate-900 rounded-lg"
        >
          <ShoppingBag className="h-5 w-5" />
          {!isCollapsed && <span className="ml-3">Produtos</span>}
        </a>
        <a
          href="#"
          className="flex items-center px-4 py-2 text-slate-700 rounded-lg hover:bg-slate-100"
        >
          <BarChart3 className="h-5 w-5" />
          {!isCollapsed && <span className="ml-3">Relatórios</span>}
        </a>
        <a
          href="#"
          className="flex items-center px-4 py-2 text-slate-700 rounded-lg hover:bg-slate-100"
        >
          <Settings className="h-5 w-5" />
          {!isCollapsed && <span className="ml-3">Administração</span>}
        </a>
      </nav>
      <div className="px-4 py-4 border-t border-slate-200">
        <a
          href="#"
          className="flex items-center gap-2 px-4 py-2 text-rose-500 rounded-xl hover:bg-rose-100 transition-colors duration-200"
        >
          <Image
            src="/btnExit.svg"
            alt="Ícone de Sair"
            width={20}
            height={20}
            className="w-5 h-5"
          />
          {!isCollapsed && <span className="font-medium text-base">Sair</span>}
        </a>
      </div>
    </aside>
  );
}
