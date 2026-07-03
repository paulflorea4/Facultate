package org.example.ducksocialnetworkui.repository;

import org.example.ducksocialnetworkui.domain.Duck;
import org.example.ducksocialnetworkui.flock.Flock;

public interface FlockRepository extends PagedRepository<Long, Flock<? extends Duck>> {
}
