using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;

using System;
using System.Collections.Generic;
using System.Text;
using Tema.Domain;

namespace Tema.Context
{
    public class MyContext : DbContext
    {
        public DbSet<Department> Departments { get; set; }
        public DbSet<Employee> Employees { get; set; }

        public static QueryCounterInterceptor Counter = new QueryCounterInterceptor();

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            var dbHost = Environment.GetEnvironmentVariable("DB_HOST") ?? "localhost";
            var dbName = Environment.GetEnvironmentVariable("DB_NAME") ?? "SGBD";
            var dbUser = Environment.GetEnvironmentVariable("DB_USER");
            var dbPass = Environment.GetEnvironmentVariable("DB_PASSWORD");

            string connectionString = $"Host={dbHost};Database={dbName};Username={dbUser};Password={dbPass};Maximum Pool Size=10;Minimum Pool Size=5;Timeout=30;Command Timeout=300;";

            optionsBuilder
                .UseLazyLoadingProxies()
                .UseNpgsql(connectionString)
                .AddInterceptors(Counter)
                .LogTo(message => System.Diagnostics.Debug.WriteLine(message), Microsoft.Extensions.Logging.LogLevel.Information)
                .EnableSensitiveDataLogging();
        }
    }
}
