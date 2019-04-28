package hello.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import hello.entity.MongoDemo;
import hello.repository.MongoDemoRepository;
import hello.service.MongoDemoService;

@Service
public class MongoDemoServiceImpl implements MongoDemoService{
	@Resource
	private MongoDemoRepository mongoDemoRepository;
	
	@Override
	public void saveOrUpdateDemo(MongoDemo demo) {
		mongoDemoRepository.save(demo);
	}
	
	@Override
	public List<MongoDemo> findAllDemos() {
		return mongoDemoRepository.findAll();
	}
	@Override
	public MongoDemo findDemoByName(String name) {
		return mongoDemoRepository.findByName(name);
	}
	@Override
	public void deleteDemoById(ObjectId id) {
		mongoDemoRepository.deleteById(id);
	}
	
}
