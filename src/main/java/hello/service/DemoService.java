package hello.service;

import java.util.List;

import hello.entity.Demo;

public interface DemoService {

	List<Demo> getDemoList();

	void saveOrUpdateDemo(Demo demo);

	void deleteDemo(long id);

}
