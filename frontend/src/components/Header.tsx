'use client';

export default function Header() {
    return (
        <header className="h-16 bg-white border-b border-slate-200 flex items-center justify-end px-6">
            <div className="flex items-center space-x-3">
                <span className="text-sm font-medium">Arthur Morgan</span>
                <div className="w-10 h-10 rounded-full bg-slate-200 flex items-center justify-center text-slate-600 font-bold">
                    AM
                </div>
            </div>
        </header>
    )
}