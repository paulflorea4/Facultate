// Initializes table features after the DOM is ready.
document.addEventListener('DOMContentLoaded', function () {
    const data = window.HotelTablesData;
    const reservationsTable = document.querySelector('[data-sortable-table="reservations"]');
    const roomComparisonTable = document.querySelector('[data-vertical-table="room-comparison"]');

    if (reservationsTable && data && Array.isArray(data.reservations)) {
        initializeClassicTable(reservationsTable, data.reservations);
    }

    if (roomComparisonTable && data && data.roomComparison) {
        initializeVerticalTable(roomComparisonTable, data.roomComparison);
    }
});

// Sets up sorting and rendering for the classic reservations table.
function initializeClassicTable(table, rows) {
    const tbody = table.querySelector('tbody');
    const headers = Array.from(table.querySelectorAll('thead th[data-sort-key]'));
    let sortState = { key: '', direction: 'asc' };

    // Renders visible rows in the reservations table body.
    function render(sortedRows) {
        tbody.innerHTML = '';

        // Creates a table row for each reservation record.
        sortedRows.forEach(function (row) {
            const tr = document.createElement('tr');
            tr.innerHTML = [
                '<td>' + row.name + '</td>',
                '<td>' + row.email + '</td>',
                '<td>' + row.roomType + '</td>',
                '<td>' + formatDate(row.checkIn) + '</td>',
                '<td>' + formatDate(row.checkOut) + '</td>',
                '<td>' + row.status + '</td>'
            ].join('');
            tbody.appendChild(tr);
        });
    }

    // Sorts table rows by a selected column key and updates UI state.
    function sortRows(key) {
        if (sortState.key === key) {
            sortState.direction = sortState.direction === 'asc' ? 'desc' : 'asc';
        } else {
            sortState.key = key;
            sortState.direction = 'asc';
        }

        // Compares row values while honoring current sort direction.
        const sorted = rows.slice().sort(function (left, right) {
            const leftValue = normalizeValue(left[key]);
            const rightValue = normalizeValue(right[key]);

            if (leftValue < rightValue) {
                return sortState.direction === 'asc' ? -1 : 1;
            }

            if (leftValue > rightValue) {
                return sortState.direction === 'asc' ? 1 : -1;
            }

            return 0;
        });

        // Clears sorting styles from all sortable headers.
        headers.forEach(function (header) {
            header.classList.remove('is-sorted', 'is-sorted-asc', 'is-sorted-desc');
            header.removeAttribute('aria-sort');
        });

        // Finds the header associated with the active sort key.
        const activeHeader = headers.find(function (header) {
            return header.dataset.sortKey === key;
        });

        if (activeHeader) {
            activeHeader.classList.add('is-sorted', sortState.direction === 'asc' ? 'is-sorted-asc' : 'is-sorted-desc');
            activeHeader.setAttribute('aria-sort', sortState.direction === 'asc' ? 'ascending' : 'descending');
        }

        render(sorted);
    }

    // Attaches click handlers to trigger sorting by header.
    headers.forEach(function (header) {
        // Sorts rows when a sortable header is clicked.
        header.addEventListener('click', function () {
            sortRows(header.dataset.sortKey);
        });
    });

    render(rows.slice());
}

// Sets up rendering and column sorting for the vertical comparison table.
function initializeVerticalTable(table, data) {
    const thead = table.querySelector('thead');
    const tbody = table.querySelector('tbody');
    let sortState = { rowKey: '', direction: 'asc', columnOrder: data.headers.slice() };

    // Renders vertical table headers, rows, and sorting handlers.
    function render() {
        const headerRow = thead.querySelector('tr');
        headerRow.innerHTML = '<th class="vertical-header-cell">Atribut</th>';

        // Builds dynamic column headers in current sorted order.
        sortState.columnOrder.forEach(function (key) {
            const th = document.createElement('th');
            th.textContent = data.labels[key];
            th.dataset.columnKey = key;
            th.className = 'vertical-column-header';
            headerRow.appendChild(th);
        });

        tbody.innerHTML = '';

        // Builds each attribute row for the vertical table body.
        data.rows.forEach(function (row) {
            const tr = document.createElement('tr');
            const rowHeader = document.createElement('th');
            rowHeader.scope = 'row';
            rowHeader.textContent = row.label;
            rowHeader.className = 'vertical-row-header';
            rowHeader.dataset.rowLabel = row.label;
            rowHeader.title = 'Apasă pentru sortarea coloanelor după „' + row.label + '”';
            tr.appendChild(rowHeader);

            // Fills row cells using the current column order.
            sortState.columnOrder.forEach(function (key) {
                const td = document.createElement('td');
                td.textContent = formatVerticalCell(row, key);
                tr.appendChild(td);
            });

            tbody.appendChild(tr);
        });

        updateVerticalHeaderState(table, sortState.rowKey, sortState.direction);

        // Binds row-header click events for column sorting.
        Array.from(tbody.querySelectorAll('th[data-row-label]')).forEach(function (header) {
            // Sorts columns based on the clicked row attribute.
            header.addEventListener('click', function () {
                sortColumnsByRow(header.dataset.rowLabel);
            });
        });
    }

    // Sorts columns using the values from a selected attribute row.
    function sortColumnsByRow(rowKey) {
        if (sortState.rowKey === rowKey) {
            sortState.direction = sortState.direction === 'asc' ? 'desc' : 'asc';
        } else {
            sortState.rowKey = rowKey;
            sortState.direction = 'asc';
        }

        // Compares column values extracted from the selected row.
        sortState.columnOrder = data.headers.slice().sort(function (left, right) {
            const leftValue = getComparableValue(left, rowKey);
            const rightValue = getComparableValue(right, rowKey);

            if (leftValue < rightValue) {
                return sortState.direction === 'asc' ? -1 : 1;
            }

            if (leftValue > rightValue) {
                return sortState.direction === 'asc' ? 1 : -1;
            }

            return 0;
        });

        render();
    }

    render();

    // Returns normalized comparable value for a column within a row.
    function getComparableValue(columnKey, rowKey) {
        // Locates the attribute row matching the requested row label.
        const row = data.rows.find(function (entry) {
            return entry.label === rowKey;
        });
        if (!row) {
            return '';
        }

        return normalizeValue(row.values[columnKey]);
    }
}

// Normalizes values so sorting works for numbers, dates, and text.
function normalizeValue(value) {
    if (typeof value === 'number') {
        return value;
    }

    if (typeof value === 'string') {
        const parsedDate = Date.parse(value);
        if (!Number.isNaN(parsedDate) && value.includes('-')) {
            return parsedDate;
        }

        return value.toLowerCase();
    }

    return value;
}

// Formats ISO date text into day/month/year.
function formatDate(value) {
    const parts = value.split('-');
    return parts[2] + '/' + parts[1] + '/' + parts[0];
}

// Formats vertical table cell values by row display type.
function formatVerticalCell(row, columnKey) {
    const value = row.values[columnKey];

    if (row.format === 'currency') {
        return value + ' RON';
    }

    return String(value);
}

// Updates visual and ARIA sorting state for vertical row headers.
function updateVerticalHeaderState(table, rowKey, direction) {
    const rowHeaders = Array.from(table.querySelectorAll('tbody th[data-row-label]'));

    // Clears existing sort markers from all row headers.
    rowHeaders.forEach(function (header) {
        header.classList.remove('is-sorted', 'is-sorted-asc', 'is-sorted-desc');
        header.removeAttribute('aria-sort');
    });

    // Finds the currently active sorted row header.
    const activeHeader = rowHeaders.find(function (header) {
        return header.dataset.rowLabel === rowKey;
    });

    if (activeHeader) {
        activeHeader.classList.add('is-sorted', direction === 'asc' ? 'is-sorted-asc' : 'is-sorted-desc');
        activeHeader.setAttribute('aria-sort', direction === 'asc' ? 'ascending' : 'descending');
    }
}
