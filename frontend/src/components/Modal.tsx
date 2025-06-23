"use client";

import React, { ReactNode } from "react";

interface ModalProps {
  isOpen: boolean;
  onClose: () => void;
  title: ReactNode;
  children: React.ReactNode;
  description: ReactNode;
}

export default function Modal({
  isOpen,
  title,
  children,
  description,
}: ModalProps) {
  if (!isOpen) {
    return null;
  }

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-white/10 backdrop-blur-sm">
      <div className="bg-white rounded-lg shadow-xl w-full max-w-lg relative animate-fade-in-up z-50">
        <header className="px-6 pt-6">
          <h2 className="flex items-center text-lg font-semibold text-slate-900">
            {title}
          </h2>
          <p className="text-sm text-slate-500 mt-1">{description}</p>
        </header>

        <div className="px-6 pt-4 pb-6">{children}</div>
        
      </div>
    </div>
  );
}
