package org.example.inchirieriauto.repository;

import org.example.inchirieriauto.model.Rent;
import org.example.inchirieriauto.model.RentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface RentRepository extends JpaRepository<Rent, Integer> {
    List<Rent> findByClientIdOrderByStartDateDesc(Integer clientId);

    Optional<Rent> findByIdAndClientEmail(Integer id, String email);

    List<Rent> findByCarIdAndStatusAndStartDateAfter(Integer carId, RentStatus status, Timestamp startDate);

    List<Rent> findByStatus(RentStatus status);

    List<Rent> findByCarIdAndStatus(Integer carId, RentStatus status);

    @Query("SELECT COUNT(r) FROM Rent r WHERE r.car.id = :carId " +
            "AND r.status != 'CANCELED' " +
            "AND (:startDate <= r.endDate AND :endDate >= r.startDate)")
    int countOverlappingReservations(@Param("carId") Integer carId,
                                      @Param("startDate") Timestamp startDate,
                                      @Param("endDate") Timestamp endDate);

    @Query("SELECT COUNT(r) FROM Rent r WHERE r.car.id = :carId " +
            "AND r.status != 'CANCELED' " +
            "AND r.id <> :rentId " +
            "AND (:startDate <= r.endDate AND :endDate >= r.startDate)")
    int countOverlappingReservationsExcludingRent(@Param("carId") Integer carId,
                                                  @Param("rentId") Integer rentId,
                                                  @Param("startDate") Timestamp startDate,
                                                  @Param("endDate") Timestamp endDate);
}
