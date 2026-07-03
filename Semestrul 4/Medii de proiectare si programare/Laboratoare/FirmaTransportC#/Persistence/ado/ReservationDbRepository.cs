using FirmaTransportC_.Model;
using FirmaTransportC_.Persistence.utils;
using log4net;
using System.Data;

namespace FirmaTransportC_.Persistence.ado
{
    public class ReservationDbRepository : IReservationRepository
    {
        private static readonly ILog log = LogManager.GetLogger("ReservationDbRepository");
        IDictionary<string, string> props;

        public ReservationDbRepository(IDictionary<string, string> props)
        {
            log.Info("Creating ReservationDbRepository with properties: " + string.Join(", ", props));
            this.props = props;
        }

        private Reservation BuildReservation(IDataReader reader)
        {
            return new Reservation(
                    reader.GetInt64(0),
                    reader.GetString(1),
                    reader.GetInt64(2),
                    reader.GetInt32(3)
                );
        }

        public long Add(Reservation entity)
        {
            log.Info($"Adding reservation: {entity}");
            using var connection = DbUtils.GetConnection(props);

            try
            {
                using var command = connection.CreateCommand();
                command.CommandText =
                    "INSERT INTO reservations (client_name, trip_id, number_of_seats) " +
                    "VALUES (@client, @trip, @seats); SELECT last_insert_rowid();";

                command.Parameters.Add(CreateParam(command, "@client", entity.ClientName));

                command.Parameters.Add(CreateParam(command, "@trip", entity.TripId));

                command.Parameters.Add(CreateParam(command, "@seats", entity.NumberOfSeats));

                var id = Convert.ToInt64(command.ExecuteScalar());
                entity.Id = id;

                log.Info($"Inserted reservation with id {id}");
                return id;
            }
            catch (Exception ex)
            {
                log.Error("Error in Add", ex);
                return -1;
            }
        }

        public void Delete(long id)
        {
            log.Info($"Deleting reservation {id}");
            using var connection = DbUtils.GetConnection(props);

            try
            {
                using var command = connection.CreateCommand();
                command.CommandText = "DELETE FROM reservations WHERE id = @id";

                command.Parameters.Add(CreateParam(command, "@id", id));

                command.ExecuteNonQuery();
            }
            catch (Exception ex)
            {
                log.Error("Error in Delete", ex);
            }
        }

        public void Update(Reservation entity)
        {
            log.Info($"Updating reservation {entity}");
            using var connection = DbUtils.GetConnection(props);

            try
            {
                using var command = connection.CreateCommand();
                command.CommandText =
                    "UPDATE reservations SET client_name=@client, trip_id=@trip, number_of_seats=@seats WHERE id=@id";

                command.Parameters.Add(CreateParam(command, "@client", entity.ClientName));
                command.Parameters.Add(CreateParam(command, "@trip", entity.TripId));
                command.Parameters.Add(CreateParam(command, "@seats", entity.NumberOfSeats));
                command.Parameters.Add(CreateParam(command, "@id", entity.Id));

                command.ExecuteNonQuery();
            }
            catch (Exception ex)
            {
                log.Error("Error in Update", ex);
            }
        }

        public Reservation FindOne(long id)
        {
            log.Info($"Finding reservation {id}");

            using var connection = DbUtils.GetConnection(props);

            try
            {
                using var command = connection.CreateCommand();
                command.CommandText = "SELECT * FROM reservations WHERE id=@id";
                command.Parameters.Add(CreateParam(command, "@id", id));

                using var reader = command.ExecuteReader();
                if (reader.Read())
                    return BuildReservation(reader);
            }
            catch (Exception ex)
            {
                log.Error("Error in FindOne", ex);
            }

            return null;
        }

        public List<Reservation> FindAll()
        {
            log.Info("Finding all reservations");

            var list = new List<Reservation>();
            using var connection = DbUtils.GetConnection(props);

            try
            {
                using var command = connection.CreateCommand();
                command.CommandText = "SELECT * FROM reservations";

                using var reader = command.ExecuteReader();
                while (reader.Read())
                    list.Add(BuildReservation(reader));
            }
            catch (Exception ex)
            {
                log.Error("Error in FindAll", ex);
            }

            return list;
        }

        public List<Reservation> FindReservationsByClientName(string name)
        {
            log.Info($"Finding reservations for client name: {name}");

            var list = new List<Reservation>();
            using var connection = DbUtils.GetConnection(props);

            try
            {
                using var command = connection.CreateCommand();
                command.CommandText = "SELECT * FROM reservations WHERE client_name=@name";
                command.Parameters.Add(CreateParam(command, "@name", name));

                using var reader = command.ExecuteReader();
                while (reader.Read())
                    list.Add(BuildReservation(reader));
            }
            catch (Exception ex)
            {
                log.Error("Error in FindReservationsByClientName", ex);
            }

            return list;
        }

        private IDbDataParameter CreateParam(IDbCommand cmd, string name, object value)
        {
            var p = cmd.CreateParameter();
            p.ParameterName = name;
            p.Value = value;
            return p;
        }

        public void MakeReservationTransaction(Reservation reservation, Trip trip, ISeatRepository seatRepository, ITripRepository tripRepository)
        {
            log.Info($"Making reservation transaction for reservation: {reservation} and trip: {trip}");

            using var connection = DbUtils.GetConnection(props);
            IDbTransaction? transaction = null;

            try
            {
                transaction = connection.BeginTransaction();

                using var insertReservation = connection.CreateCommand();
                insertReservation.Transaction = transaction;
                insertReservation.CommandText =
                    "INSERT INTO reservations (client_name, trip_id, number_of_seats) " +
                    "VALUES (@client, @trip, @seats); SELECT last_insert_rowid();";
                insertReservation.Parameters.Add(CreateParam(insertReservation, "@client", reservation.ClientName));
                insertReservation.Parameters.Add(CreateParam(insertReservation, "@trip", reservation.TripId));
                insertReservation.Parameters.Add(CreateParam(insertReservation, "@seats", reservation.NumberOfSeats));

                var insertedId = insertReservation.ExecuteScalar();
                if (insertedId == null)
                    throw new Exception("Failed to insert reservation.");

                reservation.Id = Convert.ToInt64(insertedId);

                var availableSeatIds = new List<long>();
                using (var selectSeats = connection.CreateCommand())
                {
                    selectSeats.Transaction = transaction;
                    selectSeats.CommandText = "SELECT id FROM seats WHERE trip_id=@tripId AND reservation_id IS NULL ORDER BY number LIMIT @count";
                    selectSeats.Parameters.Add(CreateParam(selectSeats, "@tripId", trip.Id));
                    selectSeats.Parameters.Add(CreateParam(selectSeats, "@count", reservation.NumberOfSeats));

                    using var reader = selectSeats.ExecuteReader();
                    while (reader.Read())
                        availableSeatIds.Add(reader.GetInt64(0));
                }

                if (availableSeatIds.Count != reservation.NumberOfSeats)
                    throw new Exception("Not enough available seats for transaction.");

                foreach (var seatId in availableSeatIds)
                {
                    using var updateSeat = connection.CreateCommand();
                    updateSeat.Transaction = transaction;
                    updateSeat.CommandText = "UPDATE seats SET reservation_id=@reservationId, client_name=@clientName WHERE id=@id";
                    updateSeat.Parameters.Add(CreateParam(updateSeat, "@reservationId", reservation.Id));
                    updateSeat.Parameters.Add(CreateParam(updateSeat, "@clientName", reservation.ClientName));
                    updateSeat.Parameters.Add(CreateParam(updateSeat, "@id", seatId));
                    updateSeat.ExecuteNonQuery();
                }

                using var updateTrip = connection.CreateCommand();
                updateTrip.Transaction = transaction;
                updateTrip.CommandText = "UPDATE trips SET available_seats = available_seats - @count WHERE id=@id AND available_seats >= @count";
                updateTrip.Parameters.Add(CreateParam(updateTrip, "@count", reservation.NumberOfSeats));
                updateTrip.Parameters.Add(CreateParam(updateTrip, "@id", trip.Id));

                if (updateTrip.ExecuteNonQuery() == 0)
                    throw new Exception("Failed to update trip available seats.");

                trip.AvailableSeats -= reservation.NumberOfSeats;
                transaction.Commit();
            }
            catch (Exception ex)
            {
                try
                {
                    transaction?.Rollback();
                }
                catch (Exception rollbackEx)
                {
                    log.Error("Error rolling back MakeReservationTransaction", rollbackEx);
                }

                log.Error("Error in MakeReservationTransaction", ex);
            }
        }

        public void CancelReservationTransaction(Reservation reservation, ISeatRepository seatRepository, ITripRepository tripRepository)
        {
            log.Info($"Starting cancel reservation transaction for reservation: {reservation.Id}");

            using var connection = DbUtils.GetConnection(props);
            if (connection.State != ConnectionState.Open) connection.Open();

            using var transaction = connection.BeginTransaction();

            try
            {
                long tripId;
                int reservedSeats;

                using (var findReservation = connection.CreateCommand())
                {
                    findReservation.Transaction = transaction;
                    findReservation.CommandText = "SELECT trip_id, number_of_seats FROM reservations WHERE id = @id";
                    findReservation.Parameters.Add(CreateParam(findReservation, "@id", reservation.Id));

                    using (var reader = findReservation.ExecuteReader())
                    {
                        if (!reader.Read())
                        {
                            throw new Exception("Reservation not found");
                        }
                        tripId = reader.GetInt64(0);
                        reservedSeats = reader.GetInt32(1);
                    }
                }

                using (var releaseSeats = connection.CreateCommand())
                {
                    releaseSeats.Transaction = transaction;
                    releaseSeats.CommandText = "UPDATE seats SET reservation_id = NULL, client_name = NULL WHERE reservation_id = @resId";
                    releaseSeats.Parameters.Add(CreateParam(releaseSeats, "@resId", reservation.Id));
                    releaseSeats.ExecuteNonQuery();
                }

                using (var deleteReservation = connection.CreateCommand())
                {
                    deleteReservation.Transaction = transaction;
                    deleteReservation.CommandText = "DELETE FROM reservations WHERE id = @id";
                    deleteReservation.Parameters.Add(CreateParam(deleteReservation, "@id", reservation.Id));

                    int deleted = deleteReservation.ExecuteNonQuery();
                    if (deleted != 1)
                    {
                        throw new Exception("Reservation not found for delete");
                    }
                }

                using (var updateTrip = connection.CreateCommand())
                {
                    updateTrip.Transaction = transaction;
                    updateTrip.CommandText = "UPDATE trips SET available_seats = available_seats + @count WHERE id = @tripId";
                    updateTrip.Parameters.Add(CreateParam(updateTrip, "@count", reservedSeats));
                    updateTrip.Parameters.Add(CreateParam(updateTrip, "@tripId", tripId));

                    int updated = updateTrip.ExecuteNonQuery();
                    if (updated != 1)
                    {
                        throw new Exception("Trip seats could not be restored");
                    }
                }

                transaction.Commit();
                log.Info("Cancel reservation transaction committed successfully");
            }
            catch (Exception ex)
            {
                log.Error("Error in CancelReservationTransaction, rolling back", ex);
                try
                {
                    transaction.Rollback();
                }
                catch (Exception rollbackEx)
                {
                    log.Error("Error rolling back transaction", rollbackEx);
                }
            }
        }
    }
}