package com.noisepipe.server.repository;

import com.noisepipe.server.model.Cue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CueRepository extends JpaRepository<Cue, Long> {
}
