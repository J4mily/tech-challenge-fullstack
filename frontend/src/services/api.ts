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

export const getProducts = async (params: URLSearchParams): Promise<PaginatedProductsResponse> => {
  const response = await api.get(`/products`, { params });
  return response.data;
};