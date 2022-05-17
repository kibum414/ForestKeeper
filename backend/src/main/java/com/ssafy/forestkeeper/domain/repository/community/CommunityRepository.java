package com.ssafy.forestkeeper.domain.repository.community;

import com.ssafy.forestkeeper.domain.dao.community.Community;
import com.ssafy.forestkeeper.domain.dao.mountain.Mountain;
import com.ssafy.forestkeeper.domain.dao.user.User;
import com.ssafy.forestkeeper.domain.enums.CommunityCode;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommunityRepository extends JpaRepository<Community, String> {

    Optional<Community> findByIdAndDelete(String communityId, boolean delete);

    Optional<List<Community>> findByMountainIdAndCommunityCodeAndDeleteOrderByCreateTimeDesc(String mountainId, CommunityCode communityCode, boolean delete, Pageable pageable);

    @Query("SELECT c FROM Community c WHERE c.mountain = :mountain AND c.communityCode = :communityCode AND c.delete = :delete AND (c.title LIKE %:title% OR c.description LIKE %:description%)")
    Optional<List<Community>> findByMountainAndCommunityCodeAndDeleteAndTitleContainingOrDescriptionContainingOrderByCreateTimeDesc(Mountain mountain, CommunityCode communityCode, boolean delete, String title, String description, Pageable pageable);

    Optional<List<Community>> findByMountainIdAndCommunityCodeAndDeleteAndTitleContainingOrderByCreateTimeDesc(String mountainId, CommunityCode communityCode, boolean delete, String title, Pageable pageable);

    Optional<List<Community>> findByMountainIdAndCommunityCodeAndDeleteAndDescriptionContainingOrderByCreateTimeDesc(String mountainId, CommunityCode communityCode, boolean delete, String description, Pageable pageable);

    Optional<List<Community>> findByMountainIdAndCommunityCodeAndDeleteAndUserOrderByCreateTimeDesc(String mountainId, CommunityCode communityCode, boolean delete, User user, Pageable pageable);

}
