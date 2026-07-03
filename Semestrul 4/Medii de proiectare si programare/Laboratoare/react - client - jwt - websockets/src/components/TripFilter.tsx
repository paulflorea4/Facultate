import React, { useState } from 'react';

interface Props {
    onFilter: (dest: string, date: string, hour: string) => void;
    onReset: () => void;
}

const TripFilter: React.FC<Props> = ({ onFilter, onReset }) => {
    const [dest, setDest] = useState('');
    const [date, setDate] = useState('');
    const [hour, setHour] = useState('');

    const handleSubmit = (e: React.SyntheticEvent<HTMLFormElement>) => {
        e.preventDefault();
        onFilter(dest, date, hour);
    };

    return (
        <form className="panel panel-filter" onSubmit={handleSubmit}>
            <h4 className="panel-title">Filtrare/Căutare</h4>
            <div className="form-grid">
                <input placeholder="Destinație" value={dest} onChange={e => setDest(e.target.value)} required />
                <input type="date" value={date} onChange={e => setDate(e.target.value)} required />
                <input type="time" value={hour} onChange={e => setHour(e.target.value)} required />
            </div>
            <div className="button-row">
                <button type="submit" className="btn btn-primary">Caută</button>
                <button type="button" className="btn btn-ghost" onClick={() => { setDest(''); setDate(''); setHour(''); onReset(); }}>Reset</button>
            </div>
        </form>
    );
};

export default TripFilter;