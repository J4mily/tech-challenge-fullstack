import axios from 'axios';
import { Product, Meta } from '@/types';

const API_BASE_URL = 'http://localhost:8080/api/v1';

const api = axios.create({
  baseURL: API_BASE_URL,
});

export interface PaginatedProductsResponse {
    data: Product[];
    meta: Meta;
}

export type ProductCreationData = {
    name: string;
    description?: string;
    price: number;
    stock: number;
}

// O tipo para atualização é parcial, pois podemos enviar apenas alguns campos.
export type ProductUpdateData = Partial<ProductCreationData>;

export const getProducts = async (params: URLSearchParams): Promise<PaginatedProductsResponse> => {
  const response = await api.get(`/products`, { params });
  return response.data;
};

export const getProductById = async (id: number): Promise<Product> => {
    const response = await api.get(`/products/${id}`);
    return response.data;
};

export const createProduct = async (productData: ProductCreationData): Promise<Product> => {
    const response = await api.post('/products', productData);
    return response.data;
};

export const updateProduct = async (id: number, productData: ProductUpdateData): Promise<Product> => {
    const response = await api.patch(`/products/${id}`, productData);
    return response.data;
};

export const deleteProduct = async (id: number): Promise<void> => {
    await api.delete(`/products/${id}`);
};
