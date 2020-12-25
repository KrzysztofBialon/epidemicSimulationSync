package com.epidemicSimulationSync.epidemicSimulationSync.repository;

import com.epidemicSimulationSync.epidemicSimulationSync.model.SimulationSetUp;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface SimulationRepository extends PagingAndSortingRepository<SimulationSetUp, String>
{
    List<SimulationSetUp> findAllByN(@Param("name") String name);
}
