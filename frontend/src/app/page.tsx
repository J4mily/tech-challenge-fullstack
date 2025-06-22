import Header from '../components/Header';
import ProductControls from '../components/ProductControls';
import ProductTable from '../components/ProductTable';
import Sidebar from '../components/Sidebar';

export default function HomePage() {
  return (
    <div className="flex h-screen bg-slate-50 text-slate-900">
      <Sidebar />
      <div className="flex-1 flex flex-col overflow-hidden">
        <Header />
        <main className="flex-1 overflow-y-auto p-8">
          <h2 className="text-3xl font-bold mb-8">Produtos</h2>
          <ProductControls />
          <ProductTable />
        </main>
      </div>
    </div>
  );
}
