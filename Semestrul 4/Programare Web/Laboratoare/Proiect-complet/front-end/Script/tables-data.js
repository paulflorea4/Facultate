window.HotelTablesData = {
    reservations: [
        {
            name: 'Maria Popescu',
            email: 'maria@email.com',
            roomType: 'Double',
            checkIn: '2026-03-20',
            checkOut: '2026-03-23',
            status: 'Confirmată'
        },
        {
            name: 'Ion Ionescu',
            email: 'ion@email.com',
            roomType: 'Single',
            checkIn: '2026-03-22',
            checkOut: '2026-03-24',
            status: 'În așteptare'
        },
        {
            name: 'Andreea Stan',
            email: 'andreea@email.com',
            roomType: 'Suite',
            checkIn: '2026-03-25',
            checkOut: '2026-03-28',
            status: 'Confirmată'
        },
        {
            name: 'Paul Marinescu',
            email: 'paul@email.com',
            roomType: 'Apartment',
            checkIn: '2026-04-01',
            checkOut: '2026-04-04',
            status: 'Anulată'
        }
    ],
    roomComparison: {
        headers: ['single', 'double', 'apartament', 'deluxe'],
        labels: {
            single: 'Single',
            double: 'Double',
            apartament: 'Apartament',
            deluxe: 'Deluxe'
        },
        rows: [
            {
                label: 'Preț / noapte',
                values: {
                    single: 260,
                    double: 300,
                    apartament: 450,
                    deluxe: 650
                },
                format: 'currency'
            },
            {
                label: 'Capacitate',
                values: {
                    single: 1,
                    double: 2,
                    apartament: 4,
                    deluxe: 4
                },
                format: 'number'
            },
            {
                label: 'Mic dejun inclus',
                values: {
                    single: 'Nu',
                    double: 'Nu',
                    apartament: 'Opțional',
                    deluxe: 'Da'
                },
                format: 'text'
            },
            {
                label: 'Balcon',
                values: {
                    single: 'Nu',
                    double: 'Da',
                    apartament: 'Da',
                    deluxe: 'Da'
                },
                format: 'text'
            }
        ]
    }
};
