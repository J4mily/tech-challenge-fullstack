import React from "react";


interface CancelButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  onClick: () => void;
  children?: React.ReactNode;
}

export default function CancelButton({
  onClick,
  children = "Cancelar", 
  ...props 
}: CancelButtonProps) {
  return (
    <button
      type="button" 
      onClick={onClick}
      className="bg-white py-2 px-4 border border-slate-300 rounded-md shadow-sm text-sm font-medium text-slate-700 hover:bg-slate-50 disabled:opacity-50"
      {...props} 
    >
      {children}
    </button>
  );
}