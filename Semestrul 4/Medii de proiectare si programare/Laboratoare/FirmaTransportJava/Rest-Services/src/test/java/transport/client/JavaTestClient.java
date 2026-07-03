package transport.client;

import org.springframework.web.client.RestClient;
import transport.model.Trip;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;

public class JavaTestClient {
    public static void main(String[] args) {
        try {
            RestClient client = RestClient.builder()
                    .baseUrl("http://localhost:8080/transport/trips")
                    .requestInterceptor(new RequestStepInterceptor())
                    .build();

            System.out.println("--- Testing CREATE ---");
            Trip newTrip = new Trip(null, "Cluj", "2026-05-20", "12:00", 18);
            Long id = client.post().body(newTrip).retrieve().body(Long.class);
            System.out.println("Created Trip ID: " + id);

            System.out.println("\n--- Testing GET ALL ---");
            List<Trip> trips = client.get().retrieve().body(new ParameterizedTypeReference<>() {
            });
            trips.forEach(System.out::println);

            System.out.println("\n--- Testing FIND BY ID ---");
            Trip found = client.get().uri("/{id}", id).retrieve().body(Trip.class);
            System.out.println("Found: " + found);

            System.out.println("\n--- Testing FILTERED SEARCH ---");
            List<Trip> filtered = client.get()
                    .uri(uriBuilder -> uriBuilder.path("/search")
                            .queryParam("destination", "Cluj")
                            .queryParam("date", "2026-05-20")
                            .queryParam("hour", "12:00").build())
                    .retrieve().body(new ParameterizedTypeReference<>() {
                    });
            System.out.println("Filtered results: " + filtered.size());

            System.out.println("\n--- Testing UPDATE ---");
            newTrip.setAvailableSeats(10);
            client.put().uri("/{id}", id).body(newTrip).retrieve().toBodilessEntity();
            System.out.println("Update completed.");

            System.out.println("\n--- Testing DELETE ---");
            client.delete().uri("/{id}", id).retrieve().toBodilessEntity();
            System.out.println("Delete completed.");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}