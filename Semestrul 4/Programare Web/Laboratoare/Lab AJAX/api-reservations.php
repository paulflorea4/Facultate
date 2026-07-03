<?php
/**
 * API endpoint for reservation operations.
 *
 * Supported actions (via ?action=):
 *   paginate_json  – paginated reservations as JSON  (Req 1 & 3)
 *   paginate_xml   – paginated reservations as XML   (Req 2)
 *   get_user_reservations – list reservation IDs for the logged-in user (Req 5 & 6)
 *   get_reservation      – single reservation details by ID            (Req 5 & 6)
 *   update_reservation   – update editable fields of a reservation     (Req 5 & 6)
 */

require_once __DIR__ . '/config.php';
require_once __DIR__ . '/functions.php';

// Require login for every action
if (!isLoggedIn()) {
    http_response_code(401);
    header('Content-Type: application/json; charset=utf-8');
    echo json_encode(['error' => 'Unauthorized']);
    exit();
}

$action = isset($_GET['action']) ? $_GET['action'] : '';

switch ($action) {

    // ─── Paginate JSON (Req 1 & 3) ────────────────────────────────────
    case 'paginate_json':
        $limit  = isset($_GET['limit'])  ? max(1, (int) $_GET['limit'])  : 5;
        $offset = isset($_GET['offset']) ? max(0, (int) $_GET['offset']) : 0;

        // Total count
        $countResult = $conn->query("SELECT COUNT(*) AS total FROM contact_messages");
        $total = $countResult ? (int) $countResult->fetch_assoc()['total'] : 0;

        // Fetch page
        $query = "SELECT id, full_name, email, subject, message, attachment_mime_type, status, created_at
                  FROM contact_messages
                  ORDER BY created_at DESC
                  LIMIT ? OFFSET ?";
        $stmt = $conn->prepare($query);
        $stmt->bind_param('ii', $limit, $offset);
        $stmt->execute();
        $result = $stmt->get_result();

        $rows = [];
        while ($row = $result->fetch_assoc()) {
            $rows[] = $row;
        }
        $stmt->close();

        header('Content-Type: application/json; charset=utf-8');
        echo json_encode([
            'total'  => $total,
            'offset' => $offset,
            'limit'  => $limit,
            'data'   => $rows
        ]);
        break;

    // ─── Paginate XML (Req 2) ─────────────────────────────────────────
    case 'paginate_xml':
        $limit  = isset($_GET['limit'])  ? max(1, (int) $_GET['limit'])  : 5;
        $offset = isset($_GET['offset']) ? max(0, (int) $_GET['offset']) : 0;

        $countResult = $conn->query("SELECT COUNT(*) AS total FROM contact_messages");
        $total = $countResult ? (int) $countResult->fetch_assoc()['total'] : 0;

        $query = "SELECT id, full_name, email, subject, message, attachment_mime_type, status, created_at
                  FROM contact_messages
                  ORDER BY created_at DESC
                  LIMIT ? OFFSET ?";
        $stmt = $conn->prepare($query);
        $stmt->bind_param('ii', $limit, $offset);
        $stmt->execute();
        $result = $stmt->get_result();

        header('Content-Type: application/xml; charset=utf-8');

        $xml = new DOMDocument('1.0', 'UTF-8');
        $xml->formatOutput = true;

        $root = $xml->createElement('messages');
        $root->setAttribute('total', $total);
        $root->setAttribute('offset', $offset);
        $root->setAttribute('limit', $limit);
        $xml->appendChild($root);

        while ($row = $result->fetch_assoc()) {
            $item = $xml->createElement('message');
            foreach ($row as $key => $value) {
                $child = $xml->createElement($key);
                $child->appendChild($xml->createTextNode($value !== null ? $value : ''));
                $item->appendChild($child);
            }
            $root->appendChild($item);
        }
        $stmt->close();

        echo $xml->saveXML();
        break;

    // ─── Get user's reservation IDs (Req 5 & 6) ──────────────────────
    case 'get_user_reservations':
        $userId = (int) $_SESSION['user_id'];

        $query = "SELECT r.id, rm.room_number, r.check_in, r.check_out, r.status
                  FROM reservations r
                  LEFT JOIN rooms rm ON r.room_id = rm.id
                  WHERE r.user_id = ?
                  ORDER BY r.created_at DESC";
        $stmt = $conn->prepare($query);
        $stmt->bind_param('i', $userId);
        $stmt->execute();
        $result = $stmt->get_result();

        $rows = [];
        while ($row = $result->fetch_assoc()) {
            $rows[] = $row;
        }
        $stmt->close();

        header('Content-Type: application/json; charset=utf-8');
        echo json_encode(['data' => $rows]);
        break;

    // ─── Get single reservation (Req 5 & 6) ──────────────────────────
    case 'get_reservation':
        $reservationId = isset($_GET['id']) ? (int) $_GET['id'] : 0;

        if ($reservationId <= 0) {
            http_response_code(400);
            header('Content-Type: application/json; charset=utf-8');
            echo json_encode(['error' => 'ID invalid']);
            exit();
        }

        // Non-admin users can only view their own reservations
        $userId = (int) $_SESSION['user_id'];
        $role   = isset($_SESSION['role']) ? $_SESSION['role'] : 'client';

        if ($role === 'admin') {
            $query = "SELECT r.id, r.user_id, r.room_id, r.check_in, r.check_out, r.guests,
                             r.special_requests, r.status, r.created_at,
                             rm.room_number, rm.room_type
                      FROM reservations r
                      LEFT JOIN rooms rm ON r.room_id = rm.id
                      WHERE r.id = ?
                      LIMIT 1";
            $stmt = $conn->prepare($query);
            $stmt->bind_param('i', $reservationId);
        } else {
            $query = "SELECT r.id, r.user_id, r.room_id, r.check_in, r.check_out, r.guests,
                             r.special_requests, r.status, r.created_at,
                             rm.room_number, rm.room_type
                      FROM reservations r
                      LEFT JOIN rooms rm ON r.room_id = rm.id
                      WHERE r.id = ? AND r.user_id = ?
                      LIMIT 1";
            $stmt = $conn->prepare($query);
            $stmt->bind_param('ii', $reservationId, $userId);
        }

        $stmt->execute();
        $result = $stmt->get_result();
        $reservation = $result ? $result->fetch_assoc() : null;
        $stmt->close();

        if (!$reservation) {
            http_response_code(404);
            header('Content-Type: application/json; charset=utf-8');
            echo json_encode(['error' => 'Rezervarea nu a fost găsită']);
            exit();
        }

        header('Content-Type: application/json; charset=utf-8');
        echo json_encode(['data' => $reservation]);
        break;

    // ─── Update reservation (Req 5 & 6) ──────────────────────────────
    case 'update_reservation':
        if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
            http_response_code(405);
            header('Content-Type: application/json; charset=utf-8');
            echo json_encode(['error' => 'Method not allowed']);
            exit();
        }

        // Read JSON body
        $input = json_decode(file_get_contents('php://input'), true);
        if (!$input) {
            // Try form data
            $input = $_POST;
        }

        $reservationId  = isset($input['id'])               ? (int) $input['id']               : 0;
        $roomId         = isset($input['room_id'])           ? (int) $input['room_id']           : 0;
        $checkIn        = isset($input['check_in'])          ? trim($input['check_in'])          : '';
        $checkOut       = isset($input['check_out'])         ? trim($input['check_out'])         : '';
        $guests         = isset($input['guests'])            ? (int) $input['guests']            : 0;
        $specialRequests = isset($input['special_requests']) ? trim($input['special_requests'])  : '';

        if ($reservationId <= 0 || $roomId <= 0 || empty($checkIn) || empty($checkOut) || $guests <= 0) {
            http_response_code(400);
            header('Content-Type: application/json; charset=utf-8');
            echo json_encode(['error' => 'Date incomplete']);
            exit();
        }

        // Verify ownership for non-admin
        $userId = (int) $_SESSION['user_id'];
        $role   = isset($_SESSION['role']) ? $_SESSION['role'] : 'client';

        if ($role !== 'admin') {
            $checkStmt = $conn->prepare("SELECT id FROM reservations WHERE id = ? AND user_id = ?");
            $checkStmt->bind_param('ii', $reservationId, $userId);
            $checkStmt->execute();
            $checkResult = $checkStmt->get_result();
            if ($checkResult->num_rows === 0) {
                $checkStmt->close();
                http_response_code(403);
                header('Content-Type: application/json; charset=utf-8');
                echo json_encode(['error' => 'Nu aveți permisiunea de a modifica această rezervare']);
                exit();
            }
            $checkStmt->close();
        }

        // Validate room exists
        $roomCheck = getRoomById($roomId);
        if (!$roomCheck) {
            http_response_code(400);
            header('Content-Type: application/json; charset=utf-8');
            echo json_encode(['error' => 'Camera selectată nu există']);
            exit();
        }

        // Capacity check
        if (isset($roomCheck['capacity']) && $guests > (int) $roomCheck['capacity']) {
            http_response_code(400);
            header('Content-Type: application/json; charset=utf-8');
            echo json_encode(['error' => 'Numărul de persoane depășește capacitatea camerei']);
            exit();
        }

        // Date validation
        $checkInDate  = DateTime::createFromFormat('Y-m-d', $checkIn);
        $checkOutDate = DateTime::createFromFormat('Y-m-d', $checkOut);
        if (!$checkInDate || !$checkOutDate || $checkOutDate <= $checkInDate) {
            http_response_code(400);
            header('Content-Type: application/json; charset=utf-8');
            echo json_encode(['error' => 'Datele de check-in/check-out nu sunt valide']);
            exit();
        }

        // Availability check (exclude current reservation)
        $availQuery = "SELECT COUNT(*) AS cnt FROM reservations
                       WHERE room_id = ? AND id != ? AND check_in < ? AND check_out > ? AND status != 'cancelled'";
        $availStmt = $conn->prepare($availQuery);
        $availStmt->bind_param('iiss', $roomId, $reservationId, $checkOut, $checkIn);
        $availStmt->execute();
        $availResult = $availStmt->get_result();
        $availRow = $availResult ? $availResult->fetch_assoc() : null;
        $availStmt->close();

        if ($availRow && (int) $availRow['cnt'] > 0) {
            http_response_code(409);
            header('Content-Type: application/json; charset=utf-8');
            echo json_encode(['error' => 'Camera nu este disponibilă pentru datele selectate']);
            exit();
        }

        // Update
        $updateQuery = "UPDATE reservations SET room_id = ?, check_in = ?, check_out = ?, guests = ?, special_requests = ? WHERE id = ?";
        $updateStmt = $conn->prepare($updateQuery);
        $updateStmt->bind_param('issisi', $roomId, $checkIn, $checkOut, $guests, $specialRequests, $reservationId);

        if ($updateStmt->execute()) {
            $updateStmt->close();
            header('Content-Type: application/json; charset=utf-8');
            echo json_encode(['success' => true, 'message' => 'Rezervare actualizată cu succes']);
        } else {
            $updateStmt->close();
            http_response_code(500);
            header('Content-Type: application/json; charset=utf-8');
            echo json_encode(['error' => 'Eroare la actualizarea rezervării']);
        }
        break;

    // ─── Get rooms list (for edit form dropdowns) ─────────────────────
    case 'get_rooms':
        $rooms = getRooms();
        header('Content-Type: application/json; charset=utf-8');
        echo json_encode(['data' => $rooms]);
        break;

    default:
        http_response_code(400);
        header('Content-Type: application/json; charset=utf-8');
        echo json_encode(['error' => 'Acțiune necunoscută: ' . htmlspecialchars($action)]);
        break;
}
