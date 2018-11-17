package com.noisepipe.server.repository;

import com.noisepipe.server.model.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, Long> {
  Page<Collection> findByUserUsername(String username, Pageable pageable);
}
