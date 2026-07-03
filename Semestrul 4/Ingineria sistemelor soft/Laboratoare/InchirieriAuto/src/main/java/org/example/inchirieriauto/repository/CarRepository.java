package org.example.inchirieriauto.repository;

import org.example.inchirieriauto.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car,Integer>, JpaSpecificationExecutor<Car> {

	@Query("select c from Car c left join fetch c.features where c.id = :id")
	Optional<Car> findByIdWithFeatures(@Param("id") Integer id);

	List<Car> findByFeaturesId(Integer featureId);

}
