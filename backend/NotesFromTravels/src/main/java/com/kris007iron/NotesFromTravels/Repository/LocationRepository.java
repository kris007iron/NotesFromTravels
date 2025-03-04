package com.kris007iron.NotesFromTravels.Repository;

import com.kris007iron.NotesFromTravels.Model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
}
