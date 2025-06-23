import { getCoupons } from "@/services/api";
import { Coupons } from "@/types";
import { useEffect, useState } from "react";

interface CouponsComponentProps {
  setCouponCode: React.Dispatch<React.SetStateAction<string>>;
}

export default function CouponsComponent({
  setCouponCode,
}: CouponsComponentProps) {
  const [coupons, setCoupons] = useState<Coupons[]>([]);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchCoupons = async () => {
      try {
        const data = await getCoupons();
        if (data.length === 0) {
          setError("Sem cupons disponíveis");
        }
        setCoupons(data);
      } catch (err) {
        setError("Erro ao carregar cupons." + err);
      }
    };
    fetchCoupons();
  }, []);

  return (
    <div className="p-4">
      {error === null ? (
        <>
          <p className="text-sm text-slate-600 mb-2">
            Cupons disponíveis para teste:
          </p>

          <div className="grid grid-cols-3 gap-2 mb-4">
            {coupons.map((coupon) => (
              <button
                type="button"
                onClick={() => setCouponCode(coupon.code)}
                key={coupon.code}
                className="px-4 py-2 bg-white text-slate-700 border border-slate-300 rounded-md text-sm font-medium hover:bg-slate-100"
              >
                {coupon.code} ({coupon.value}%)
              </button>
            ))}
          </div>
        </>
      ) : (
        <>
          <p className="text-red-600 text-sm">{error}</p>
        </>
      )}
    </div>
  );
}
