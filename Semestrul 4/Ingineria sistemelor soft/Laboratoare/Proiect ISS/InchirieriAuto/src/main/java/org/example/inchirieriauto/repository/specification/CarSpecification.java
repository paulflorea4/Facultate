package org.example.inchirieriauto.repository.specification;

import jakarta.persistence.criteria.*;
import org.example.inchirieriauto.model.Car;
import org.example.inchirieriauto.dto.CarFilterDTO;
import org.example.inchirieriauto.model.CarStatus;
import org.example.inchirieriauto.model.Rent;
import org.example.inchirieriauto.model.RentStatus;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;

public class CarSpecification {

    public static Specification<Car> getCarsByFilters(CarFilterDTO filter, boolean availableOnly) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();


            if (availableOnly) {
                predicates.add(cb.equal(root.get("status"), CarStatus.AVAILABLE));
            }

            if (filter.getBrand() != null && !filter.getBrand().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("brand")), "%" + filter.getBrand().toLowerCase() + "%"));
            }
            if (filter.getModel() != null && !filter.getModel().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("model")), "%" + filter.getModel().toLowerCase() + "%"));
            }
            if (filter.getMinPrice() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("pricePerDay"), filter.getMinPrice()));
            }
            if (filter.getMaxPrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("pricePerDay"), filter.getMaxPrice()));
            }
            if (filter.getYear() != null) {
                predicates.add(cb.equal(root.get("year"), filter.getYear()));
            }

            if (filter.getStartDate() != null && filter.getEndDate() != null) {
                Subquery<Integer> occupiedCarsSubquery = query.subquery(Integer.class);
                Root<Rent> rentRoot = occupiedCarsSubquery.from(Rent.class);
                occupiedCarsSubquery.select(rentRoot.get("car").get("id"));

                Timestamp startTs = Timestamp.valueOf(filter.getStartDate());
                Timestamp endTs = Timestamp.valueOf(filter.getEndDate());

                Predicate overlapDate = cb.and(
                        cb.lessThanOrEqualTo(rentRoot.get("startDate"), endTs),
                        cb.greaterThanOrEqualTo(rentRoot.get("endDate"), startTs)
                );
                Predicate notCanceled = cb.notEqual(rentRoot.get("status"), RentStatus.CANCELED);
                occupiedCarsSubquery.where(cb.and(overlapDate, notCanceled));

                predicates.add(cb.not(root.get("id").in(occupiedCarsSubquery)));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
