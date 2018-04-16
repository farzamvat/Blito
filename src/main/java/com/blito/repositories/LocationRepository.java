package com.blito.repositories;

import com.blito.models.Location;
import com.blito.rest.viewmodels.LocationViewModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Farzam Vatanzadeh
 * 2/6/18
 * Mailto : farzam.vat@gmail.com
 **/

public interface LocationRepository extends JpaRepository<Location,Long> {
    Location findByName(String name);

    @Query(value = "SELECT\n" +
            "\t new com.blito.rest.viewmodels.LocationViewModel(l.profileId, (\n" +
            "    6371 * acos(\n" +
            "      cos( radians(:latitude) )\n" +
            "      * cos( radians( l.latitude ) )\n" +
            "      * cos( radians( l.longitude ) - radians(:longitude) )\n" +
            "      + sin( radians( :latitude) )\n" +
            "      * sin( radians( l.latitude ) )\n" +
            "    )\n" +
            "  ) as distance) \n" +
            "FROM location as l\n" +
            "where (\n" +
            "    6371 * acos(\n" +
            "      cos( radians(:latitude) )\n" +
            "      * cos( radians( l.latitude ) )\n" +
            "      * cos( radians( l.longitude ) - radians(:longitude) )\n" +
            "      + sin( radians( :latitude) )\n" +
            "      * sin( radians( l.latitude ) )\n" +
            "    )\n" +
            "  ) < :radius order by distance")
    List<LocationViewModel> findLocationsNearByRadius(
            @Param("latitude") Double latitude,
            @Param("longitude") Double longitude,
            @Param("radius") Double radius, Pageable pageable);
}
