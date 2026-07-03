import type { Trip } from '../types';
import { API_BASE_URL } from './consts';

const getAuthHeaders = (): HeadersInit => {
    const token = localStorage.getItem('token');
    return token ? { 'Authorization': `Bearer ${token}`, 'Content-Type': 'application/json' } : { 'Content-Type': 'application/json' };
};

export const tripApi = {
    getAll: async (): Promise<Trip[]> => {
        const response = await fetch(API_BASE_URL);
        return response.json();
    },

    search: async (destination: string, date: string, hour: string): Promise<Trip[]> => {
        const url = new URL(`${API_BASE_URL}/search`);
        url.searchParams.append('destination', destination);
        url.searchParams.append('date', date);
        url.searchParams.append('hour', hour);
        
        const response = await fetch(url.toString());
        return response.json();
    },

    create: async (trip: Trip): Promise<number> => {
        const response = await fetch(API_BASE_URL, {
            method: 'POST',
            headers: getAuthHeaders(),
            body: JSON.stringify(trip)
        });
        if (!response.ok) throw new Error('Unauthorized or invalid resource creation');
        return response.json();
    },

    update: async (id: number, trip: Trip): Promise<void> => {
        const response = await fetch(`${API_BASE_URL}/${id}`, {
            method: 'PUT',
            headers: getAuthHeaders(),
            body: JSON.stringify(trip)
        });
        if (!response.ok) throw new Error('Update failed or unauthorized');
    },

    delete: async (id: number): Promise<void> => {
        const response = await fetch(`${API_BASE_URL}/${id}`, {
            method: 'DELETE',
            headers: getAuthHeaders()
        });
        if (!response.ok) throw new Error('Delete failed or unauthorized');
    }
};

export const authApi = {
    login: async (username: string, password: string): Promise<{ token: string }> => {
        const response = await fetch('http://localhost:8080/transport/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password })
        });
        if (!response.ok) throw new Error('Autentificare esuata');
        return response.json();
    }
};