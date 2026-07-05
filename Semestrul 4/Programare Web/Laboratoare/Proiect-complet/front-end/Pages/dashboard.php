<?php
require_once '../../back-end/config.php';
require_once '../../back-end/nav-helper.php';
require_once '../../back-end/functions.php';

// Only admins can access this page
requireAdmin();
?>
<!DOCTYPE html>
<html lang="ro">

<head>
    <meta charset="UTF-8">
    <title>Dashboard - Hotel Victoria</title>
    <link rel="stylesheet" href="../Styles/style-responsive.css">
</head>

<body>
    <header>
        <h1>Dashboard - Administrare rezervări</h1>
    </header>
    <?php echo getNavigation(); ?>

    <main>
        <h2>Rezervări existente</h2>

        <div class="table-container">
            <table class="sortable-table" data-sortable-table="reservations-db">
                <thead>
                    <tr>
                        <th>Nume</th>
                        <th>Email</th>
                        <th>Nr. cameră</th>
                        <th>Tip cameră</th>
                        <th>Check-in</th>
                        <th>Check-out</th>
                        <th>Persoane</th>
                        <th>Status</th>
                        <th>Notițe</th>
                    </tr>
                </thead>
                <tbody>
                    <?php
                    // Load reservations from DB (join users and rooms)
                    $reservationsQuery = "SELECT r.id, r.check_in, r.check_out, r.guests, r.special_requests, r.status, r.created_at, u.full_name, u.email, rm.room_number, rm.room_type
                        FROM reservations r
                        LEFT JOIN users u ON r.user_id = u.id
                        LEFT JOIN rooms rm ON r.room_id = rm.id
                        ORDER BY r.created_at DESC";

                    $res = $conn->query($reservationsQuery);
                    if ($res) {
                        while ($row = $res->fetch_assoc()) {
                            $name = htmlspecialchars($row['full_name'] ?? 'Guest', ENT_QUOTES, 'UTF-8');
                            $email = htmlspecialchars($row['email'] ?? '', ENT_QUOTES, 'UTF-8');
                            $roomNumber = htmlspecialchars($row['room_number'] ?? '-', ENT_QUOTES, 'UTF-8');
                            $roomType = htmlspecialchars($row['room_type'] ?? '-', ENT_QUOTES, 'UTF-8');
                            $checkIn = htmlspecialchars($row['check_in'], ENT_QUOTES, 'UTF-8');
                            $checkOut = htmlspecialchars($row['check_out'], ENT_QUOTES, 'UTF-8');
                            $guests = (int) $row['guests'];
                            $status = htmlspecialchars($row['status'], ENT_QUOTES, 'UTF-8');
                            $notes = htmlspecialchars($row['special_requests'] ?? '', ENT_QUOTES, 'UTF-8');

                            echo "<tr>";
                            echo "<td>{$name}</td>";
                            echo "<td>{$email}</td>";
                            echo "<td>{$roomNumber}</td>";
                            echo "<td>{$roomType}</td>";
                            echo "<td>{$checkIn}</td>";
                            echo "<td>{$checkOut}</td>";
                            echo "<td>{$guests}</td>";
                            echo "<td>{$status}</td>";
                            echo "<td>{$notes}</td>";
                            echo "</tr>";
                        }
                        $res->free();
                    }
                    ?>
                </tbody>
            </table>
        </div>

        <h2>Comparație camere</h2>
        <p>Faceți click pe orice celulă din antetul vertical pentru a sorta coloanele tabelului.</p>

        <div class="table-container">
            <table class="vertical-sortable-table" data-vertical-table="room-comparison">
                <thead>
                    <tr>
                        <th class="sortable-header" data-sort-key="roomType">Tip cameră</th>
                        <th class="sortable-header" data-sort-key="occupied">Ocupate</th>
                        <th class="sortable-header" data-sort-key="available">Disponibile</th>
                        <th class="sortable-header" data-sort-key="maintenance">Întreținere</th>
                    </tr>
                </thead>
                <tbody></tbody>
            </table>
        </div>
    </main>

    <footer>
        <p>© 2026 Hotel Victoria</p>
    </footer>
    <script src="../Script/tables-data.js"></script>
    <script src="../Script/tables.js"></script>
</body>

</html>
