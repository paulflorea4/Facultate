package transport.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import transport.model.Trip;
import transport.persistence.repository.TripRepository;
import transport.rest.websocket.WebSocketAuthHandler;

import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin(origins = {"http://localhost:5173","http://localhost:5174"})
@RequestMapping("/transport/trips")
public class TripController {
    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private WebSocketAuthHandler socketHandler;

    @GetMapping
    public List<Trip> findAll() {
        return tripRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trip> findById(@PathVariable Long id) {
        Trip trip =  tripRepository.findById(id);
        return trip != null ? ResponseEntity.ok(trip) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody Trip trip) {
        Long id = tripRepository.add(trip);
        socketHandler.broadcastNotification("{\"action\":\"CREATED\",\"id\":" + id + "}");
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Trip trip) {
        if(tripRepository.findById(id) == null){
            return ResponseEntity.notFound().build();
        }
        if(!Objects.equals(trip.getId(), id)){
            return ResponseEntity.badRequest().build();
        }
        trip.setId(id);
        tripRepository.update(trip);
        socketHandler.broadcastNotification("{\"action\":\"UPDATED\",\"id\":" + id + "}");
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if(tripRepository.findById(id) == null){
            return ResponseEntity.notFound().build();
        }
        tripRepository.delete(id);
        socketHandler.broadcastNotification("{\"action\":\"DELETED\",\"id\":" + id + "}");
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public List<Trip> searchByDestination(
            @RequestParam String destination,
            @RequestParam String date,
            @RequestParam String hour) {
        return tripRepository.findTripsByDestinationAndDepartureDate(destination, date, hour);
    }
}
