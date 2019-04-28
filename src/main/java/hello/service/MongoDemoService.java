package hello.service;

import java.util.List;

import org.bson.types.ObjectId;

import hello.entity.MongoDemo;

public interface MongoDemoService {

	void saveOrUpdateDemo(MongoDemo demo);

	MongoDemo findDemoByName(String name);

	List<MongoDemo> findAllDemos();

	void deleteDemoById(ObjectId id);
	
}
