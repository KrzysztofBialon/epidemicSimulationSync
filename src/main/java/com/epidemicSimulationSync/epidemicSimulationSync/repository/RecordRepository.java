package com.epidemicSimulationSync.epidemicSimulationSync.repository;

import com.epidemicSimulationSync.epidemicSimulationSync.model.SimulationDay;
import com.epidemicSimulationSync.epidemicSimulationSync.model.SimulationRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface RecordRepository extends CrudRepository<SimulationRecord, String>
{
    SimulationRecord findByOwnerId(String id);
}
