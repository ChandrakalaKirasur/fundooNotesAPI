package com.bridgelabz.fundoonotes.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundoonotes.entity.Label;
@Repository
public interface LabelRepository extends CrudRepository<Label,Long>{
	
}
