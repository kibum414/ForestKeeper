package com.ssafy.api.domain.repository.trashcan;

import com.ssafy.api.domain.dao.mountain.TrashCan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrashCanRepository extends JpaRepository<TrashCan, String>{
    Optional<List<TrashCan>> findByRegion(String region);
}
