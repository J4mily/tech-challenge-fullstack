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
  has_coupon_applied: boolean;
  coupon_code: string | null | undefined;
}

export interface Meta {
  page: number;
  limit: number;
  totalItems: number;
  totalPages: number;
}

export interface Coupons {
    id: number;
    code: string;
    type: string;
    value: number;
    one_shot: boolean;
    valid_from: Date;
    valid_until: Date;
    created_at: Date;
}
export type ProductCreationData = Pick<Product, 'name' | 'description' | 'price' | 'stock'>;

export type ProductUpdateData = Partial<ProductCreationData>;