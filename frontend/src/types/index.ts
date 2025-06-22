export interface Product {
  id: number;
  name: string;
  description: string;
  stock: number;
  is_out_of_stock: boolean;
  price: number;
  finalPrice: number;
  discount: {
    type: string;
    value: number;
  } | null;
  hasCouponApplied: boolean;
}

export interface Meta {
  page: number;
  limit: number;
  totalItems: number;
  totalPages: number;
}