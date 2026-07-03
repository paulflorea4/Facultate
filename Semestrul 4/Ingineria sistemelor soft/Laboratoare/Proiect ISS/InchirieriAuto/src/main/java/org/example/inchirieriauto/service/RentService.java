package org.example.inchirieriauto.service;

import jakarta.transaction.Transactional;
import org.example.inchirieriauto.dto.RentRequestDTO;
import org.example.inchirieriauto.dto.RentUpdateDTO;
import org.example.inchirieriauto.exception.BusinessRuleException;
import org.example.inchirieriauto.model.Car;
import org.example.inchirieriauto.model.Client;
import org.example.inchirieriauto.model.Rent;
import org.example.inchirieriauto.model.RentStatus;
import org.example.inchirieriauto.repository.RentRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RentService {
    private final RentRepository rentRepository;
    private final CarService carService;
    private final UserService userService;

    public RentService(RentRepository rentRepository,
                       CarService carService,
                       UserService userService) {
        this.rentRepository = rentRepository;
        this.carService = carService;
        this.userService = userService;
    }

    @Transactional
    public Rent createRent(RentRequestDTO requestDTO, String clientEmail) {
        validatePeriod(requestDTO.getStartDate(), requestDTO.getEndDate());

        Timestamp startTs = Timestamp.valueOf(requestDTO.getStartDate());
        Timestamp endTs = Timestamp.valueOf(requestDTO.getEndDate());
        boolean isAvailable = isCarAvailable(requestDTO.getCarId(), startTs, endTs, null);

        if (!isAvailable) {
            throw new BusinessRuleException("Mașina nu este disponibilă pentru perioada selectată.");
        }

        Car car = carService.getCarById(requestDTO.getCarId(), false);
        Client client = userService.requireClientByEmail(clientEmail);

        Rent rent = new Rent();
        rent.setCar(car);
        rent.setClient(client);
        rent.setStartDate(startTs);
        rent.setEndDate(endTs);

        rent.setStatus(RentStatus.PENDING);

        return rentRepository.save(rent);
    }

    public List<Rent> getRentHistory(String clientEmail) {
        Client client = userService.requireClientByEmail(clientEmail);
        return rentRepository.findByClientIdOrderByStartDateDesc(client.getId());
    }

    public Rent getRentForClient(String clientEmail, Integer rentId) {
        Client client = userService.requireClientByEmail(clientEmail);
        return rentRepository.findByIdAndClientEmail(rentId, client.getEmail())
                .orElseThrow(() -> new BusinessRuleException("Rezervarea nu a fost găsită."));
    }

    public Rent getEditableRentForClient(String clientEmail, Integer rentId) {
        Rent rent = getRentForClient(clientEmail, rentId);
        ensureEditableRent(rent);
        return rent;
    }

    @Transactional
    public Rent updatePendingRent(String clientEmail, RentUpdateDTO requestDTO) {
        Rent rent = getRentForClient(clientEmail, requestDTO.getRentId());
        ensureEditableRent(rent);
        validatePeriod(requestDTO.getStartDate(), requestDTO.getEndDate());

        Timestamp startTs = Timestamp.valueOf(requestDTO.getStartDate());
        Timestamp endTs = Timestamp.valueOf(requestDTO.getEndDate());

        if (!isCarAvailable(rent.getCar().getId(), startTs, endTs, rent.getId())) {
            throw new BusinessRuleException("Mașina nu este disponibilă pentru perioada selectată.");
        }

        rent.setStartDate(startTs);
        rent.setEndDate(endTs);
        return rentRepository.save(rent);
    }

    @Transactional
    public void cancelPendingRent(String clientEmail, Integer rentId) {
        Rent rent = getRentForClient(clientEmail, rentId);
        ensureEditableRent(rent);
        rent.setStatus(RentStatus.CANCELED);
        rentRepository.save(rent);
    }

    @Transactional
    public void cancelPendingRentsForCar(Integer carId) {
        List<Rent> rents = rentRepository.findByCarIdAndStatusAndStartDateAfter(
                carId,
                RentStatus.PENDING,
                Timestamp.valueOf(LocalDateTime.now())
        );

        for (Rent rent : rents) {
            rent.setStatus(RentStatus.CANCELED);
        }
        rentRepository.saveAll(rents);
    }

    public boolean hasActiveRentals(Integer carId) {
        List<Rent> confirmedRents = rentRepository.findByCarIdAndStatusAndStartDateAfter(
                carId,
                RentStatus.CONFIRMED,
                Timestamp.valueOf(LocalDateTime.now().minusDays(365))
        );
        
        LocalDateTime now = LocalDateTime.now();
        boolean hasOngoingConfirmed = confirmedRents.stream()
                .anyMatch(rent -> rent.getEndDate() != null && rent.getEndDate().after(Timestamp.valueOf(now)));
        
        if (hasOngoingConfirmed) {
            return true;
        }
        
        List<Rent> pendingRents = rentRepository.findByCarIdAndStatus(carId, RentStatus.PENDING);
        return !pendingRents.isEmpty();
    }

    public boolean isCarAvailable(Integer carId, Timestamp start, Timestamp end) {
        return isCarAvailable(carId, start, end, null);
    }

    public boolean isCarAvailable(Integer carId, Timestamp start, Timestamp end, Integer rentIdToExclude) {
        if (start == null || end == null || start.after(end)) {
            throw new BusinessRuleException("Perioada rezervării este invalidă.");
        }

        int count = rentIdToExclude == null
                ? rentRepository.countOverlappingReservations(carId, start, end)
                : rentRepository.countOverlappingReservationsExcludingRent(carId, rentIdToExclude, start, end);
        return count == 0;
    }

    private void ensureEditableRent(Rent rent) {
        if (rent.getStatus() != RentStatus.PENDING || rent.getStartDate() == null || !rent.getStartDate().after(Timestamp.valueOf(LocalDateTime.now()))) {
            throw new BusinessRuleException("Rezervarea poate fi modificată sau anulată doar dacă este activă și începe în viitor.");
        }
    }

    private void validatePeriod(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            throw new BusinessRuleException("Perioada rezervării este invalidă.");
        }
        LocalDateTime now = LocalDateTime.now();
        if (start.isBefore(now) || end.isBefore(now)) {
            throw new BusinessRuleException("Perioada rezervării nu poate fi în trecut.");
        }
        if (!start.isBefore(end)) {
            throw new BusinessRuleException("Data de ridicare trebuie să fie înaintea datei de returnare.");
        }
    }
}
