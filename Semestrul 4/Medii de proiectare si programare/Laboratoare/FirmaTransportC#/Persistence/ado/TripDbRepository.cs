using FirmaTransportC_.Model;
using FirmaTransportC_.Persistence.utils;
using FirmaTransportC_.Persistence;
using log4net;
using System;
using System.Collections.Generic;
using System.Data;
using System.Globalization;

namespace FirmaTransportC_.Persistence.ado
{
    public class TripDbRepository : ITripRepository
    {
        private static readonly ILog log = LogManager.GetLogger("TripDbRepository");
        private readonly IDictionary<string, string> props;

        public TripDbRepository(IDictionary<string, string> props)
        {
            log.Info("Initializing TripDbRepository with properties: " + string.Join(", ", props));
            this.props = props;
        }

        public long Add(Trip entity)
        {
            log.InfoFormat("Adding trip: {0}", entity);
            using var connection = DbUtils.GetConnection(props);
            try
            {
                using var command = connection.CreateCommand();
                command.CommandText = @"
                    INSERT INTO trips (destination, date, hour, available_seats) 
                    VALUES (@destination, @date, @hour, @seats);
                    SELECT last_insert_rowid();
                ";
                command.Parameters.Add(CreateParam(command, "@destination", entity.Destination));
                command.Parameters.Add(CreateParam(command, "@date", entity.Date));
                command.Parameters.Add(CreateParam(command, "@hour", entity.Hour));
                command.Parameters.Add(CreateParam(command, "@seats", entity.AvailableSeats));
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
                log.Error("Error adding trip", ex);
            }
            return -1;
        }

        public void Delete(long id)
        {
            log.InfoFormat("Deleting trip with id: {0}", id);
            using var connection = DbUtils.GetConnection(props);
            try
            {
                using var command = connection.CreateCommand();
                command.CommandText = "DELETE FROM trips WHERE id = @id";
                command.Parameters.Add(CreateParam(command, "@id", id));
                command.ExecuteNonQuery();
            }
            catch (Exception ex)
            {
                log.Error("Error deleting trip", ex);
            }
        }

        public void Update(Trip entity)
        {
            log.InfoFormat("Updating trip: {0}", entity);
            using var connection = DbUtils.GetConnection(props);
            try
            {
                using var command = connection.CreateCommand();
                command.CommandText = @"
                    UPDATE trips 
                    SET destination = @destination, date = @date, hour = @hour, available_seats = @seats 
                    WHERE id = @id
                ";
                command.Parameters.Add(CreateParam(command, "@destination", entity.Destination));
                command.Parameters.Add(CreateParam(command, "@date", entity.Date));
                command.Parameters.Add(CreateParam(command, "@hour", entity.Hour));
                command.Parameters.Add(CreateParam(command, "@seats", entity.AvailableSeats));
                command.Parameters.Add(CreateParam(command, "@id", entity.Id));
                command.ExecuteNonQuery();
            }
            catch (Exception ex)
            {
                log.Error("Error updating trip", ex);
            }
        }

        public Trip FindOne(long id)
        {
            log.InfoFormat("Finding trip by id: {0}", id);
            using var connection = DbUtils.GetConnection(props);
            try
            {
                using var command = connection.CreateCommand();
                command.CommandText = "SELECT * FROM trips WHERE id = @id";
                command.Parameters.Add(CreateParam(command, "@id", id));
                using var reader = command.ExecuteReader();
                if (reader.Read())
                    return ExtractTrip(reader);
            }
            catch (Exception ex)
            {
                log.Error("Error finding trip", ex);
            }
            return null;
        }

        public List<Trip> FindAll()
        {
            log.Info("Finding all trips");
            var trips = new List<Trip>();
            using var connection = DbUtils.GetConnection(props);
            try
            {
                using var command = connection.CreateCommand();
                command.CommandText = "SELECT * FROM trips";
                using var reader = command.ExecuteReader();
                while (reader.Read())
                    trips.Add(ExtractTrip(reader));
            }
            catch (Exception ex)
            {
                log.Error("Error finding trips", ex);
            }
            return trips;
        }

        public List<Trip> FindTripsByDestinationAndDepartureDate(string destination, string date, string hour)
        {
            log.InfoFormat("Finding trips by destination: {0}, hour: {1} and date: {2}", destination, hour, date);
            var trips = new List<Trip>();
            using var connection = DbUtils.GetConnection(props);
            try
            {
                using var command = connection.CreateCommand();
                command.CommandText = @"
                    SELECT * FROM trips
                    WHERE destination = @destination
                    AND date = @date
                    AND hour = @hour
                ";
                command.Parameters.Add(CreateParam(command, "@destination", destination));
                command.Parameters.Add(CreateParam(command, "@date", date));
                command.Parameters.Add(CreateParam(command, "@hour", hour));
                using var reader = command.ExecuteReader();
                while (reader.Read())
                    trips.Add(ExtractTrip(reader));
            }
            catch (Exception ex)
            {
                log.Error("Error finding filtered trips", ex);
            }
            return trips;
        }

        private Trip ExtractTrip(IDataReader reader)
        {
            long id = reader.GetInt64(0);
            string destination = reader.GetString(1);
            string date = reader.GetString(2);
            int availableSeats = reader.GetInt32(3);
            string hour = reader.GetString(4);
            return new Trip(id, destination, date, hour, availableSeats);
        }

        private IDbDataParameter CreateParam(IDbCommand cmd, string name, object value)
        {
            var p = cmd.CreateParameter();
            p.ParameterName = name;
            p.Value = value;
            return p;
        }
    }
}