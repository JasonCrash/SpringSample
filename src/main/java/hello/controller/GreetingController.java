package hello.controller;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.Resource;

import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import hello.Greeting;
import hello.entity.Demo;
import hello.entity.MongoDemo;
import hello.service.DemoService;
import hello.service.MongoDemoService;


@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Resource
    private DemoService demoService;
    @Resource
    private MongoDemoService mongoDemoService;
    
    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
    	log.info("name: "+ name);
    	return new Greeting(counter.incrementAndGet(),
                            String.format(template, name));
    }
    
    @RequestMapping(value = "/save",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String saveDemo(@RequestBody  Demo demo) {
    	demoService.saveOrUpdateDemo(demo);
    	return "save " + demo.getName() + " success";
    }
    
    @RequestMapping(value = "/list",method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<Demo> getAllDemos() {
    	return demoService.getDemoList();
    }
    
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.DELETE, produces = "application/json")
    @ResponseBody
    public String deleteDemo(@PathVariable int id) {
    	demoService.deleteDemo(id);
    	return "delete " + id + " success";
    }
    
    
    @RequestMapping(value = "/mongo/save",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String saveMongoDemo(@RequestBody  MongoDemo demo) {
    	mongoDemoService.saveOrUpdateDemo(demo);
    	return "save " + demo.getName() + " success";
    }
    
    @RequestMapping(value = "/mongo/list",method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<MongoDemo> getAllMongoDemos() {
    	List<MongoDemo> list = mongoDemoService.findAllDemos();
    	return list;
    }
    
    @RequestMapping(value = "/mongo/delete/{id}",method = RequestMethod.DELETE, produces = "application/json")
    @ResponseBody
    public String deleteMongoDemo(@PathVariable String id) {
    	mongoDemoService.deleteDemoById(new ObjectId(id));
    	return "delete " + id + " success";
    }
}
