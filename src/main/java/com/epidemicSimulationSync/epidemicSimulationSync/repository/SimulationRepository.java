package com.epidemicSimulationSync.epidemicSimulationSync.repository;

import com.epidemicSimulationSync.epidemicSimulationSync.model.SimulationSetUp;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.List;

@RepositoryRestResource
public interface SimulationRepository extends CrudRepository<SimulationSetUp, String>
{
    List<SimulationSetUp> findAllByN(@Param("name") String name);
    List<SimulationSetUp> findAllByR(@Param("r") double r);
    List<SimulationSetUp> findAllByI(@Param("i") int i);
    List<SimulationSetUp> findAllByM(@Param("m") double m);
    List<SimulationSetUp> findAllByTi(@Param("ti") double ti);
    List<SimulationSetUp> findAllByTm(@Param("tm") double tm);
    List<SimulationSetUp> findAllByTs(@Param("ts") double ts);
    List<SimulationSetUp> findByPBetween(@Param("from") int from, @Param("to") int to);
    List<SimulationSetUp> findByPBetweenAndTsIsBetween(@Param("from") int from, @Param("to") int to, @Param("from1") int from1, @Param("to1") int to1);
}
