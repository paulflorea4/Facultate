import type { Trip } from '../types';
import TripRow from './TripRow';

interface ListProps {
    trips: Trip[];
    onEdit: (t: Trip) => void;
    onDelete: (id: number) => void;
}

const TripList: React.FC<ListProps> = ({ trips, onEdit, onDelete }) => (
    <section className="panel panel-list">
        <h4 className="panel-title">Curse disponibile</h4>
        <div className="table-wrap">
            <table className="trip-table">
                <thead>
                    <tr><th>ID</th><th>Destinație</th><th>Dată</th><th>Oră</th><th>Locuri</th><th>Acțiuni</th></tr>
                </thead>
                <tbody>
                    {trips.map(t => <TripRow key={t.id} trip={t} onEdit={onEdit} onDelete={onDelete} />)}
                </tbody>
            </table>
        </div>
    </section>
);

export default TripList;