import type { Trip } from '../types';

const TripRow = ({ trip, onEdit, onDelete }: { trip: Trip, onEdit: (t: Trip) => void, onDelete: (id: number) => void }) => (
    <tr>
        <td>{trip.id}</td>
        <td>{trip.destination}</td>
        <td>{trip.date}</td>
        <td>{trip.hour}</td>
        <td>{trip.availableSeats}</td>
        <td className="actions-cell">
            <button className="btn btn-secondary btn-small" onClick={() => onEdit(trip)}>Edit</button>
            <button className="btn btn-danger btn-small" onClick={() => onDelete(trip.id!)}>Delete</button>
        </td>
    </tr>
);

export default TripRow;