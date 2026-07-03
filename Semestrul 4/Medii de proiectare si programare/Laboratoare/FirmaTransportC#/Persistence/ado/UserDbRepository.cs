using FirmaTransportC_.Model;
using FirmaTransportC_.Persistence.utils;
using log4net;
using System.Data;

namespace FirmaTransportC_.Persistence.ado
{
    public class UserDbRepository : IUserRepository
    {
        private static readonly ILog log = LogManager.GetLogger("UserDbRepository");

        IDictionary<String, string> props;

        public UserDbRepository(IDictionary<String, string> props)
        {
            log.Info("Creating UserDbRepository with properties: " + string.Join(", ", props));
            this.props = props;
        }

        public long Add(User entity)
        {
            log.InfoFormat("Adding new user: {0}", entity);

            using (IDbConnection connection = DbUtils.GetConnection(props))
            {
                try
                {
                    using (var command = connection.CreateCommand())
                    {
                        command.CommandText = "INSERT INTO users (username, password) VALUES (@username, @password); SELECT last_insert_rowid();";

                        var paramUsername = command.CreateParameter();
                        paramUsername.ParameterName = "@username";
                        paramUsername.Value = entity.Username;
                        command.Parameters.Add(paramUsername);

                        var paramPassword = command.CreateParameter();
                        paramPassword.ParameterName = "@password";
                        paramPassword.Value = PasswordUtils.Hash(entity.Password);
                        command.Parameters.Add(paramPassword);

                        var result = command.ExecuteScalar();

                        if (result != null)
                        {
                            long id = Convert.ToInt64(result);
                            entity.Id = id;

                            log.InfoFormat("User added successfully, id: {0}", id);
                            return id;
                        }
                    }
                }
                catch (Exception ex)
                {
                    log.Error("Error adding user", ex);
                }
            }

            return -1;
        }

        public void Delete(long id)
        {
            log.InfoFormat("Entering Delete with id: {0}", id);
            IDbConnection connection = DbUtils.GetConnection(props);

            try
            {
                int result;
                using (var command = connection.CreateCommand())
                {
                    command.CommandText = "DELETE FROM users WHERE id = @id";
                    var paramId = command.CreateParameter();
                    paramId.ParameterName = "@id";
                    paramId.Value = id;
                    command.Parameters.Add(paramId);

                    result = command.ExecuteNonQuery();
                }
                log.InfoFormat("Exiting Delete with result: {0}", result);
            }
            catch (Exception ex)
            {
                log.Error("Error in Delete", ex);
            }
            finally
            {
                connection.Close();
            }
        }

        public List<User> FindAll()
        {
            log.Info("Entering FindAll");
            IDbConnection connection = DbUtils.GetConnection(props);
            var users = new List<User>();

            try
            {
                using (var command = connection.CreateCommand())
                {
                    command.CommandText = "SELECT * FROM users";
                    using (var reader = command.ExecuteReader())
                    {
                        while (reader.Read())
                        {
                            users.Add(new User(
                                reader.GetInt64(0),
                                reader.GetString(1),
                                reader.GetString(2)
                            ));
                        }
                    }
                }
                log.InfoFormat("Exiting FindAll with {0} users", users.Count);
            }
            catch (Exception ex)
            {
                log.Error("Error in FindAll", ex);
            }
            finally
            {
                connection.Close();
            }

            return users;
        }

        public User FindOne(long id)
        {
            log.InfoFormat("Entering FindOne with id: {0}", id);
            IDbConnection connection = DbUtils.GetConnection(props);

            try
            {
                using (var command = connection.CreateCommand())
                {
                    command.CommandText = "SELECT * FROM users WHERE id = @id";
                    var paramId = command.CreateParameter();
                    paramId.ParameterName = "@id";
                    paramId.Value = id;
                    command.Parameters.Add(paramId);
                    using (var reader = command.ExecuteReader())
                    {
                        if (reader.Read())
                        {
                            var user = new User(
                                reader.GetInt64(0),
                                reader.GetString(1),
                                reader.GetString(2)
                            );
                            log.InfoFormat("Exiting FindOne with result {0}", user);
                            return user;
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                log.Error("Error in FindOne", ex);
            }
            finally
            {
                connection.Close();
            }
            return null;
        }

        public User? FindByUsername(string username)
        {
            log.InfoFormat("Entering FindByUsername with username: {0}", username);
            IDbConnection connection = DbUtils.GetConnection(props);

            try
            {
                using (var command = connection.CreateCommand())
                {
                    command.CommandText = "SELECT * FROM users WHERE username = @username LIMIT 1";

                    var paramUsername = command.CreateParameter();
                    paramUsername.ParameterName = "@username";
                    paramUsername.Value = username;
                    command.Parameters.Add(paramUsername);

                    using (var reader = command.ExecuteReader())
                    {
                        if (reader.Read())
                        {
                            var user = new User(
                                reader.GetInt64(0),
                                reader.GetString(1),
                                reader.GetString(2)
                            );
                            log.InfoFormat("Exiting FindByUsername with result: {0}", user);
                            return user;
                        }
                        return null;
                    }
                }
            }
            catch (Exception ex)
            {
                log.Error("Error in Login", ex);
                return null;
            }
            finally
            {
                connection.Close();
            }
        }

        public void Update(User entity)
        {
            log.InfoFormat("Entering Update with user: {0}", entity);
            IDbConnection connection = DbUtils.GetConnection(props);

            try
            {
                int result;
                using (var command = connection.CreateCommand())
                {
                    command.CommandText = "UPDATE users SET username = @username, password = @password WHERE id = @id";

                    var paramUsername = command.CreateParameter();
                    paramUsername.ParameterName = "@username";
                    paramUsername.Value = entity.Username;
                    command.Parameters.Add(paramUsername);

                    var paramPassword = command.CreateParameter();
                    paramPassword.ParameterName = "@password";
                    paramPassword.Value = entity.Password;
                    command.Parameters.Add(paramPassword);

                    var paramId = command.CreateParameter();
                    paramId.ParameterName = "@id";
                    paramId.Value = entity.Id;
                    command.Parameters.Add(paramId);

                    result = command.ExecuteNonQuery();
                }
                log.InfoFormat("Exiting Update with result: {0}", result);
            }
            catch (Exception ex)
            {
                log.Error("Error in Update", ex);
            }
            finally
            {
                connection.Close();
            }
        }
    }
}
