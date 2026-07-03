// Initializes table features after the DOM is ready.
(function ($) {
    $(function () {
        const data = window.HotelTablesData;
        const $reservationsTable = $('[data-sortable-table^="reservations"]');
        const $roomComparisonTable = $('[data-vertical-table="room-comparison"]');

        if ($reservationsTable.length && data && Array.isArray(data.reservations)) {
            initializeClassicTable($reservationsTable, data.reservations);
        }

        if ($roomComparisonTable.length && data && data.roomComparison) {
            initializeVerticalTable($roomComparisonTable, data.roomComparison);
        }
    });
}(jQuery));

// Sets up sorting and rendering for the classic reservations table.
function initializeClassicTable($table, rows) {
    const $tbody = $table.find('tbody');
    let $headers = $table.find('thead th[data-sort-key]');
    let sortState = { key: '', direction: 'asc' };

    // Renders visible rows in the reservations table body.
    function render(sortedRows) {
        $tbody.empty();

        // Creates a table row for each reservation record. Preserve column order.
        sortedRows.forEach(function (row) {
            const rowHtml = `
                <td>${row.name || ''}</td>
                <td>${row.email || ''}</td>
                <td>${row.roomNumber || ''}</td>
                <td>${row.roomType || ''}</td>
                <td>${formatDate(row.checkIn)}</td>
                <td>${formatDate(row.checkOut)}</td>
                <td>${row.guests != null ? row.guests : ''}</td>
                <td>${row.status || ''}</td>
                <td>${row.notes || ''}</td>
            `;

            $tbody.append($('<tr>').html(rowHtml));
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
        $headers.removeClass('is-sorted is-sorted-asc is-sorted-desc').removeAttr('aria-sort');

        // Finds the header associated with the active sort key.
        const $activeHeader = $headers.filter(`[data-sort-key="${key}"]`).first();

        if ($activeHeader.length) {
            $activeHeader
                .addClass(`is-sorted ${sortState.direction === 'asc' ? 'is-sorted-asc' : 'is-sorted-desc'}`)
                .attr('aria-sort', sortState.direction === 'asc' ? 'ascending' : 'descending');
        }

        render(sorted);
    }

    // If there are no data-sort-key headers (server-rendered table), infer keys from column order
    if (!$headers.length) {
        const inferredKeys = ['name', 'email', 'roomNumber', 'roomType', 'checkIn', 'checkOut', 'guests', 'status', 'notes'];

        // Extract existing DOM rows into a rows array
        const domRows = [];
        $tbody.find('tr').each(function () {
            const $cells = $(this).find('td');
            domRows.push({
                name: $cells.eq(0).text().trim(),
                email: $cells.eq(1).text().trim(),
                roomNumber: $cells.eq(2).text().trim(),
                roomType: $cells.eq(3).text().trim(),
                checkIn: $cells.eq(4).text().trim(),
                checkOut: $cells.eq(5).text().trim(),
                guests: parseInt($cells.eq(6).text().trim()) || 0,
                status: $cells.eq(7).text().trim(),
                notes: $cells.eq(8).text().trim()
            });
        });

        // Use extracted rows for sorting/rendering
        rows = domRows;

        // Ensure thead th elements have data-sort-key so UI updates work
        $table.find('thead th').each(function (i) {
            $(this).attr('data-sort-key', inferredKeys[i] || '');
            $(this).addClass('sortable-header');
        });

        $headers = $table.find('thead th[data-sort-key]');
    }

    // Attaches click handlers to trigger sorting by header.
    $headers.on('click', function () {
        // Sorts rows when a sortable header is clicked.
        sortRows($(this).data('sortKey'));
    });

    render(rows.slice());
}

// Sets up rendering and column sorting for the vertical comparison table.
function initializeVerticalTable($table, data) {
    const $thead = $table.find('thead');
    const $tbody = $table.find('tbody');
    let sortState = { rowKey: '', direction: 'asc', columnOrder: data.headers.slice() };

    // Renders vertical table headers, rows, and sorting handlers.
    function render() {
        const $headerRow = $thead.find('tr').first();
        $headerRow.html('<th class="vertical-header-cell">Atribut</th>');

        // Builds dynamic column headers in current sorted order.
        sortState.columnOrder.forEach(function (key) {
            $headerRow.append($('<th>', {
                text: data.labels[key],
                'data-column-key': key,
                class: 'vertical-column-header'
            }));
        });

        $tbody.empty();

        // Builds each attribute row for the vertical table body.
        data.rows.forEach(function (row) {
            const $tr = $('<tr>');
            const $rowHeader = $('<th>', {
                scope: 'row',
                text: row.label,
                class: 'vertical-row-header',
                'data-row-label': row.label,
                title: `Apasă pentru sortarea coloanelor după „${row.label}”`
            });

            $tr.append($rowHeader);

            // Fills row cells using the current column order.
            sortState.columnOrder.forEach(function (key) {
                $tr.append($('<td>', { text: formatVerticalCell(row, key) }));
            });

            $tbody.append($tr);
        });

        updateVerticalHeaderState($table, sortState.rowKey, sortState.direction);

        // Binds row-header click events for column sorting.
        $tbody.find('th[data-row-label]').off('click').on('click', function () {
            // Sorts columns based on the clicked row attribute.
            sortColumnsByRow($(this).data('rowLabel'));
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
        const row = data.rows.find(entry => entry.label === rowKey);
        return row ? normalizeValue(row.values[columnKey]) : '';
    }
}

// Normalizes values so sorting works for numbers, dates, and text.
function normalizeValue(value) {
    if (typeof value === 'number') return value;

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
    if (!value) return '';
    const parts = String(value).split('-');
    if (parts.length < 3) return value;
    const [year, month, day] = parts;
    return `${day}/${month}/${year}`;
}

// Formats vertical table cell values by row display type.
function formatVerticalCell(row, columnKey) {
    const value = row.values[columnKey];
    return row.format === 'currency' ? `${value} RON` : String(value);
}

// Updates visual and ARIA sorting state for vertical row headers.
function updateVerticalHeaderState($table, rowKey, direction) {
    const $rowHeaders = $table.find('tbody th[data-row-label]');

    // Clears existing sort markers from all row headers.
    $rowHeaders.removeClass('is-sorted is-sorted-asc is-sorted-desc').removeAttr('aria-sort');

    // Finds the currently active sorted row header.
    const $activeHeader = $rowHeaders.filter(`[data-row-label="${rowKey}"]`).first();

    if ($activeHeader.length) {
        $activeHeader
            .addClass(`is-sorted ${direction === 'asc' ? 'is-sorted-asc' : 'is-sorted-desc'}`)
            .attr('aria-sort', direction === 'asc' ? 'ascending' : 'descending');
    }
}