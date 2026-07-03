using CSharpTestClient;
using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Net.Http.Json;
using System.Threading.Tasks;

class Program
{
    static async Task Main()
    {
        var client = new HttpClient(new LoggingHandler(new HttpClientHandler()))
        {
            BaseAddress = new Uri("http://localhost:8080/")
        };

        try
        {
            Console.WriteLine("--- Testing CREATE ---");

            var trip = new
            {
                destination = "Sibiu",
                date = "2026-06-10",
                hour = "08:00",
                availableSeats = 25
            };

            var postRes = await client.PostAsJsonAsync("transport/trips", trip);
            postRes.EnsureSuccessStatusCode();

            var id = (await postRes.Content.ReadAsStringAsync()).Trim();

            Console.WriteLine($"Created Trip ID: {id}");

            Console.WriteLine("\n--- Testing GET ALL ---");
            var all = await client.GetFromJsonAsync<List<object>>("transport/trips");
            Console.WriteLine($"Found {all?.Count ?? 0} trips.");

            Console.WriteLine("\n--- Testing FIND BY ID ---");
            var found = await client.GetAsync($"transport/trips/{id}");
            Console.WriteLine($"Found Status: {found.StatusCode}");
            Console.WriteLine($"Found Body: {await found.Content.ReadAsStringAsync()}");

            Console.WriteLine("\n--- Testing UPDATE ---");
            var updatedTrip = new
            {
                destination = "Sibiu",
                date = "2026-06-10",
                hour = "09:30",
                availableSeats = 5
            };

            var putRes = await client.PutAsJsonAsync($"transport/trips/{id}", updatedTrip);
            Console.WriteLine($"Update Status: {putRes.StatusCode}");

            Console.WriteLine("\n--- Testing DELETE ---");
            var delRes = await client.DeleteAsync($"transport/trips/{id}");
            Console.WriteLine($"Delete Status: {delRes.StatusCode}");
        }
        catch (Exception ex)
        {
            Console.WriteLine($"\n[ERROR] Something went wrong: {ex.Message}");
            if (ex.InnerException != null)
            {
                Console.WriteLine($"Inner Exception: {ex.InnerException.Message}");
            }
        }
    }
}