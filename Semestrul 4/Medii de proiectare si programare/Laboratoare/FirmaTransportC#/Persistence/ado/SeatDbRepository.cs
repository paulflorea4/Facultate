using FirmaTransportC_.Model;
using FirmaTransportC_.Persistence.utils;
using FirmaTransportC_.Persistence;
using log4net;
using System;
using System.Collections.Generic;
using System.Data;

namespace FirmaTransportC_.Persistence.ado
{
    public class SeatDbRepository : ISeatRepository
    {
        private static readonly ILog log = LogManager.GetLogger("SeatDbRepository");
        private readonly IDictionary<string, string> props;

        public SeatDbRepository(IDictionary<string, string> props)
        {
            log.Info("Creating SeatDbRepository with properties: " + string.Join(", ", props));
            this.props = props;
        }

        public long Add(Seat entity)
        {
            log.InfoFormat("Adding seat: {0}", entity);
            using var connection = DbUtils.GetConnection(props);
            try
            {
                using var command = connection.CreateCommand();
                command.CommandText = "INSERT INTO seats (number, reservation_id, trip_id, clientName) VALUES (@number, @reservationId, @tripId, @clientName); SELECT last_insert_rowid();";
                command.Parameters.Add(CreateParam(command, "@number", entity.Number));
                command.Parameters.Add(CreateParam(command, "@reservationId", entity.ReservationId.HasValue ? entity.ReservationId.Value : DBNull.Value));
                command.Parameters.Add(CreateParam(command, "@tripId", entity.TripId));
                command.Parameters.Add(CreateParam(command, "@clientName", entity.ClientName ?? "-"));
                var result = command.ExecuteScalar();
                if (result != null)
                {
                    long id = Convert.ToInt64(result);
                    entity.Id = id;
                    return id;
                }
            }
            catch (Exception ex)
            {
                log.Error("Error adding seat", ex);
            }
            return -1;
        }

        public void Delete(long id)
        {
            log.InfoFormat("Deleting seat with id: {0}", id);
            using var connection = DbUtils.GetConnection(props);
            try
            {
                using var command = connection.CreateCommand();
                command.CommandText = "DELETE FROM seats WHERE id = @id";
                command.Parameters.Add(CreateParam(command, "@id", id));
                command.ExecuteNonQuery();
            }
            catch (Exception ex)
            {
                log.Error("Error deleting seat", ex);
            }
        }

        public void Update(Seat entity)
        {
            log.InfoFormat("Updating seat: {0}", entity);
            using var connection = DbUtils.GetConnection(props);
            try
            {
                using var command = connection.CreateCommand();
                command.CommandText = "UPDATE seats SET number=@number, reservation_id=@reservationId, trip_id=@tripId, client_name=@clientName WHERE id=@id";
                command.Parameters.Add(CreateParam(command, "@number", entity.Number));
                command.Parameters.Add(CreateParam(command, "@reservationId", entity.ReservationId.HasValue ? entity.ReservationId.Value : DBNull.Value));
                command.Parameters.Add(CreateParam(command, "@tripId", entity.TripId));
                command.Parameters.Add(CreateParam(command, "@clientName", entity.ClientName ?? "-"));
                command.Parameters.Add(CreateParam(command, "@id", entity.Id));
                command.ExecuteNonQuery();
            }
            catch (Exception ex)
            {
                log.Error("Error updating seat", ex);
            }
        }

        public Seat FindOne(long id)
        {
            log.InfoFormat("Finding seat with id: {0}", id);
            using var connection = DbUtils.GetConnection(props);
            try
            {
                using var command = connection.CreateCommand();
                command.CommandText = "SELECT * FROM seats WHERE id=@id";
                command.Parameters.Add(CreateParam(command, "@id", id));
                using var reader = command.ExecuteReader();
                if (reader.Read())
                    return ExtractSeat(reader);
            }
            catch (Exception ex)
            {
                log.Error("Error finding seat", ex);
            }
            return null;
        }

        public List<Seat> FindAll()
        {
            log.Info("Finding all seats");
            var seats = new List<Seat>();
            using var connection = DbUtils.GetConnection(props);
            try
            {
                using var command = connection.CreateCommand();
                command.CommandText = "SELECT * FROM seats";
                using var reader = command.ExecuteReader();
                while (reader.Read())
                    seats.Add(ExtractSeat(reader));
            }
            catch (Exception ex)
            {
                log.Error("Error finding seats", ex);
            }
            return seats;
        }

        public List<Seat> FindSeatsForTrip(long tripId)
        {
            log.InfoFormat("Finding seats for trip: {0}", tripId);
            var seats = new List<Seat>();
            using var connection = DbUtils.GetConnection(props);
            try
            {
                using var command = connection.CreateCommand();
                command.CommandText = "SELECT * FROM seats WHERE trip_id=@tripId";
                command.Parameters.Add(CreateParam(command, "@tripId", tripId));
                using var reader = command.ExecuteReader();
                while (reader.Read())
                    seats.Add(ExtractSeat(reader));
            }
            catch (Exception ex)
            {
                log.Error("Error finding seats for trip", ex);
            }
            return seats;
        }

        public List<Seat> FindSeatsForReservation(long reservationId)
        {
            log.InfoFormat("Finding seats for reservation: {0}", reservationId);
            var seats = new List<Seat>();
            using var connection = DbUtils.GetConnection(props);
            try
            {
                using var command = connection.CreateCommand();
                command.CommandText = "SELECT * FROM seats WHERE reservation_id=@reservationId";
                command.Parameters.Add(CreateParam(command, "@reservationId", reservationId));
                using var reader = command.ExecuteReader();
                while (reader.Read())
                    seats.Add(ExtractSeat(reader));
            }
            catch (Exception ex)
            {
                log.Error("Error finding seats for reservation", ex);
            }
            return seats;
        }

        public int CountAvailableSeatsForTrip(long tripId)
        {
            using var connection = DbUtils.GetConnection(props);
            try
            {
                using var command = connection.CreateCommand();
                command.CommandText = "SELECT COUNT(*) FROM seats WHERE trip_id=@tripId AND reservation_id IS NULL";
                command.Parameters.Add(CreateParam(command, "@tripId", tripId));
                return Convert.ToInt32(command.ExecuteScalar());
            }
            catch (Exception ex)
            {
                log.Error("Error counting available seats", ex);
            }
            return -1;
        }

        private Seat ExtractSeat(IDataReader reader)
        {
            long id = reader.GetInt64(0);
            int number = reader.GetInt32(1);
            long? reservationId = reader.IsDBNull(2) ? null : reader.GetInt64(2);
            long tripId = reader.GetInt64(3);
            string clientName = reader.IsDBNull(4) ? "-" : reader.GetString(4);
            return new Seat(id, number, reservationId, tripId, clientName);
        }

        private IDbDataParameter CreateParam(IDbCommand cmd, string name, object value)
        {
            var param = cmd.CreateParameter();
            param.ParameterName = name;
            param.Value = value;
            return param;
        }
    }
}