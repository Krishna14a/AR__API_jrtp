package com.JRTP.repository;

import com.JRTP.entities.AppEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AppRepo extends JpaRepository<AppEntity,Long> {

    public List<AppEntity> fetchUserApps();

    @Query(value = "from AppEntity where userId =:userId")
    public List<AppEntity> fetchCwApps(Integer userId);

}
