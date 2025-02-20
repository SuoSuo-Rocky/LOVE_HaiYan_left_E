package org.wang.mapper;

import java.util.List;

import org.wang.entity.Animal;

public interface AnimalMapper {
	
	List<Animal> queryAllAnimalWithResMap();
	
	Animal queryAnimalByIdWithHashMap(int id);

}
