import { useEffect, useState } from 'react';
import TripFilter from './components/TripFilter';
import TripForm from './components/TripForm';
import TripList from './components/TripList';
import { tripApi, authApi } from './services/api';
import type { Trip } from './types';

const App: React.FC = () => {
    const [trips, setTrips] = useState<Trip[]>([]);
    const [editingTrip, setEditingTrip] = useState<Trip | null>(null);
    
    const [token, setToken] = useState<string | null>(localStorage.getItem('token'));
    const [username, setUsername] = useState<string>('');
    const [password, setPassword] = useState<string>('');

    const loadTrips = async (): Promise<void> => {
        try {
            const data = await tripApi.getAll();
            setTrips(data);
        } catch (error) {
            console.error('Eroare la incarcare date', error);
        }
    };

    useEffect(() => {
        void loadTrips();
    }, []);

    useEffect(() => {
        if (!token) return;

        const ws = new WebSocket(`ws://localhost:8080/ws-transport?token=${encodeURIComponent(token)}`);

        ws.onopen = () => {
            console.log('S-a stabilit conexiunea WebSocket ca Observator Autentificat.');
        };

        ws.onmessage = (event) => {
            try {
                const message = JSON.parse(event.data);
                console.log('Notificare primita de la server:', message);
                
                void loadTrips();
            } catch (err) {
                console.error('Eroare la procesarea mesajului WebSocket', err);
            }
        };

        ws.onclose = () => {
            console.log('Conexiunea WebSocket a fost inchisa.');
        };

        return () => {
            ws.close(); 
        };
    }, [token]);

    const handleSearch = async (destination: string, date: string, hour: string): Promise<void> => {
        try {
            const data = await tripApi.search(destination, date, hour);
            setTrips(data);
        } catch (error) {
            console.error('Eroare la filtrare', error);
        }
    };

    const handleSave = async (tripData: Trip): Promise<void> => {
        if (!token) {
            alert('Trebuie sa fii autentificat pentru a modifica resurse!');
            return;
        }
        try {
            if (editingTrip?.id !== undefined) {
                await tripApi.update(editingTrip.id, { ...tripData, id: editingTrip.id });
            } else {
                await tripApi.create(tripData);
            }
            setEditingTrip(null);
            await loadTrips();
        } catch (error) {
            console.error('Eroare la salvare', error);
            alert('Salvare esuata. S-ar putea ca sesiunea ta sa fi expirat.');
        }
    };

    const handleDelete = async (id: number): Promise<void> => {
        if (!token) {
            alert('Trebuie sa fii autentificat pentru a sterge resurse!');
            return;
        }
        if (window.confirm('Sigur doresti sa stergi aceasta calatorie?')) {
            try {
                await tripApi.delete(id);
                await loadTrips();
            } catch (error) {
                console.error('Eroare la stergere', error);
                alert('Stergere esuata. S-ar putea ca sesiunea ta sa fi expirat.');
            }
        }
    };

    const handleLogin = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            const data = await authApi.login(username, password);
            localStorage.setItem('token', data.token);
            setToken(data.token);
            setUsername('');
            setPassword('');
        } catch (error) {
            console.error('Login error', error);
            alert('Autentificare esuata! Username sau parola incorecte.');
        }
    };

    const handleLogout = () => {
        localStorage.removeItem('token');
        setToken(null);
    };

    return (
        <div className="app-shell">
            <header className="app-header" style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '10px 20px', background: '#f5f5f5', borderBottom: '1px solid #ddd' }}>
                <h1>Aplicatie Transport Calatorii</h1>
                
                <div className="auth-widget">
                    {token ? (
                        <div style={{ display: 'flex', alignItems: 'center', gap: '15px' }}>
                            <span style={{ color: '#2e7d32', fontWeight: 'bold', display: 'flex', alignItems: 'center', gap: '5px' }}>
                                <span style={{ width: '8px', height: '8px', borderRadius: '5px', background: '#2e7d32', display: 'inline-block' }}></span>
                                Observer Activ (Live Connected)
                            </span>
                            <button onClick={handleLogout} style={{ padding: '5px 10px', cursor: 'pointer' }}>Logout</button>
                        </div>
                    ) : (
                        <form onSubmit={handleLogin} style={{ display: 'flex', gap: '5px' }}>
                            <input type="text" placeholder="Utilizator" value={username} onChange={e => setUsername(e.target.value)} required style={{ padding: '5px' }} />
                            <input type="password" placeholder="Parola" value={password} onChange={e => setPassword(e.target.value)} required style={{ padding: '5px' }} />
                            <button type="submit" style={{ padding: '5px 10px', cursor: 'pointer' }}>Autentificare</button>
                        </form>
                    )}
                </div>
            </header>

            <TripFilter onFilter={handleSearch} onReset={loadTrips} />

            {token ? (
                <TripForm editingTrip={editingTrip} onSave={handleSave} onCancel={() => setEditingTrip(null)} />
            ) : (
                <div style={{ padding: '15px', background: '#fff3cd', color: '#856404', margin: '15px 0', borderRadius: '4px', textAlign: 'center', fontWeight: '5px' }}>
                    ⚠️ Autentifica-te folosind contul din baza de date pentru a putea adauga sau edita calatorii. (Vizitatorii anonimi pot doar vizualiza/filtra).
                </div>
            )}

            <TripList trips={trips} onEdit={setEditingTrip} onDelete={handleDelete} />
        </div>
    );
};

export default App;