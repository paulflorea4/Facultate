using Microsoft.Data.Sqlite;
using System.Data;

namespace FirmaTransportC_.Persistence.utils
{
    public static class DbUtils
    {
        private static IDbConnection instance = null;

        public static IDbConnection GetConnection(IDictionary<string, string> properties)
        {
            if (instance == null || instance.State == ConnectionState.Closed)
            {
                instance = getNewConnection(properties);
                instance.Open();
            }
            return instance;
        }

        private static IDbConnection getNewConnection(IDictionary<string, string> props)
        {
            String connectionString = props["ConnectionString"];
            return new SqliteConnection(connectionString);
        }
    }
}
