import React, { useState, useEffect } from 'react';
import type { Trip } from '../types';

interface Props {
    editingTrip: Trip | null;
    onSave: (trip: Trip) => void;
    onCancel: () => void;
}

const TripForm: React.FC<Props> = ({ editingTrip, onSave, onCancel }) => {
    const [formData, setFormData] = useState<Trip>({
        destination: '', date: '', hour: '', availableSeats: 0
    });

    useEffect(() => {
        if (editingTrip) setFormData(editingTrip);
        else setFormData({ destination: '', date: '', hour: '', availableSeats: 0 });
    }, [editingTrip]);

    const handleSubmit = (e: React.SyntheticEvent<HTMLFormElement>) => {
        e.preventDefault();
        onSave(formData);
        setFormData({ destination: '', date: '', hour: '', availableSeats: 0 });
    };

    return (
        <form className="panel panel-form" onSubmit={handleSubmit}>
            <h4 className="panel-title">{editingTrip ? 'Editează' : 'Adaugă'} Călătorie</h4>
            <div className="form-grid">
                <input placeholder="Destinație" value={formData.destination}
                    onChange={e => setFormData({ ...formData, destination: e.target.value })} required />
                <input type="date" value={formData.date}
                    onChange={e => setFormData({ ...formData, date: e.target.value })} required />
                <input type="time" value={formData.hour}
                    onChange={e => setFormData({ ...formData, hour: e.target.value })} required />
                <input type="number" min={1} placeholder="Locuri" value={formData.availableSeats}
                    onChange={e => setFormData({ ...formData, availableSeats: Number(e.target.value) || 0 })} required />
            </div>
            <div className="button-row">
                <button type="submit" className="btn btn-primary">{editingTrip ? 'Actualizează' : 'Creează'}</button>
                {editingTrip && <button type="button" className="btn btn-ghost" onClick={onCancel}>Anulează</button>}
            </div>
        </form>
    );
};

export default TripForm;