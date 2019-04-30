package hello.service;

import java.util.List;

import hello.entity.Demo;

public interface DemoService {

	List<Demo> getDemoList();

	void saveOrUpdateDemo(Demo demo);

	void deleteDemo(long id);

	void saveMoreDemo(List<Demo> demos);

	void saveOrUpdateDemo1(Demo demo) throws Exception;

	void saveOrUpdateDemo2(Demo demo) throws Exception;

	void saveOrUpdateDemo3(Demo demo);

}
