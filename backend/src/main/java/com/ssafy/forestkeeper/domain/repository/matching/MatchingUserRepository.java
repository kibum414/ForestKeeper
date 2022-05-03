package com.ssafy.forestkeeper.domain.repository.matching;

import com.ssafy.forestkeeper.domain.dao.plogging.Matching;
import com.ssafy.forestkeeper.domain.dao.plogging.MatchingUser;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchingUserRepository extends JpaRepository<MatchingUser, String> {

    Optional<List<MatchingUser>> findByMatchingAndDelete(Matching matching, boolean delete);
    Optional<MatchingUser> findByMatchingAndUserId(Matching matching, String userId);
}
