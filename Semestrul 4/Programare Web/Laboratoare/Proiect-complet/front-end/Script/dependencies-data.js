window.HotelFormDependencies = {
    roomTypesByGuestCount: {
        '1': ['twin', 'standard'],
        '2': ['twin', 'standard'],
        '3': ['apartament'],
        '4': ['apartament', 'deluxe'],
        '5': ['apartament', 'deluxe']
    },
    roomLabels: {
        twin: 'Double Twin',
        standard: 'Double Standard',
        apartament: 'Apartament',
        deluxe: 'Apartament Deluxe'
    },
    roomFacilitiesByRoomType: {
        twin: ['wifi', 'tv', 'aer_conditionat', 'minibar'],
        standard: ['wifi', 'tv', 'aer_conditionat', 'minibar'],
        apartament: ['wifi', 'tv', 'aer_conditionat', 'minibar', 'seif', 'frigider'],
        deluxe: ['wifi', 'tv', 'aer_conditionat', 'minibar', 'seif', 'frigider', 'balcon', 'jacuzzi', 'mic_dejun_inclus']
    }
};
