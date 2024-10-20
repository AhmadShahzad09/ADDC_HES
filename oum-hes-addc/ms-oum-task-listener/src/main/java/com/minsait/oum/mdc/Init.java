//package com.minsait.oum.mdc;
//
//import java.math.BigInteger;
//import java.time.Instant;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.minsait.oum.mdc.entity.TelemetryCallMonitor;
//import com.minsait.oum.mdc.entity.TelemetryCallMonitorPK;
//import com.minsait.oum.mdc.entity.json.Call;
//import com.minsait.oum.mdc.entity.json.EquipmentTask;
//import com.minsait.oum.mdc.entity.json.Order;
//import com.minsait.oum.mdc.enums.Direction;
//import com.minsait.oum.mdc.enums.ExecutionType;
//import com.minsait.oum.mdc.enums.Status;
//import com.minsait.oum.mdc.repository.TelemetryCallMonitorRepository;
//import com.minsait.oum.mdc.service.TelemetryCallMonitorService;
//
//@Configuration
//public class Init {
//
//	@Autowired
//	private TelemetryCallMonitorRepository repo;
//
//	@Autowired
//	private TelemetryCallMonitorService service;
//
////	@PostConstruct
//	private void test2() {
//		long init = System.currentTimeMillis();
//		List<TelemetryCallMonitor> list = new ArrayList<TelemetryCallMonitor>();
//		for (int i = 0; i < 100000; i++) {
//			TelemetryCallMonitor entity = new TelemetryCallMonitor();
//			TelemetryCallMonitorPK pk = new TelemetryCallMonitorPK();
//			pk.setId(BigInteger.valueOf(i));
//			pk.setDatetime(new Date());
//			entity.setId(pk);
//			entity.setGroupname("name1");
////			entity.setExecutionType(ExecutionType.AUTOMATIC);
//			entity.setExecutionType(ExecutionType.MANUAL);
//			entity.setPriority(1);
//			entity.setUsr(13l);
//			entity.setDirection(Direction.OUT);
//			List<Call> calllist = new ArrayList<Call>();
//			Call call = new Call();
//			call.setPid(1l);
//			Map<String, Object> communication = new HashMap<String, Object>();
//			communication.put("ip", "182.12.12.1");
//			communication.put("port", 123);
//			call.setCommunication(communication);
//			call.setTasks(generateTasks());
//			calllist.add(call);
//			entity.setCall(calllist);
//			repo.save(entity);
//		}
//		long finish = System.currentTimeMillis();
//
//		System.out.println(finish - init + " ms");
////		repo2.saveAll(list);
//	}
//
//	private List<EquipmentTask> generateTasks() {
//		List<EquipmentTask> tasks = new ArrayList<EquipmentTask>();
//		for (int i = 0; i < 70; i++) {
//			tasks.add(generateTask(i));
//		}
//		return tasks;
//	}
//
//	private EquipmentTask generateTask(int i) {
//		EquipmentTask task = new EquipmentTask();
//		task.setDeviceName(Long.valueOf(i) + "name");
//		task.setId(Long.valueOf(i));
//		task.setOrder(generateOrders(i));
//		task.setFinishTime(Instant.now().toEpochMilli());
//		return task;
//	}
//
//	private List<Order> generateOrders(int i) {
//		List<Order> orders = new ArrayList<Order>();
//		Order order = new Order();
//		order.setId(Long.valueOf(i));
//		order.setName("LoadProfile");
//		order.setRequestId(1232l);
//		order.setStatus(Status.FINISH_OK);
//		orders.add(order);
//		return orders;
//	}
//
////	@PostConstruct
//	private void testJM() throws JsonProcessingException {
//
//		List<TelemetryCallMonitor> list = repo.findTelemetryCallMonitorsByOrderName("LoadProfile");
//
////		Gson gson = new Gson(); 
////		EquipmentTask task = gson.fromJson(json, EquipmentTask.class);
//		System.out.println(list.get(0).getCall());
//
//		long startTime = System.nanoTime();
//		List<EquipmentTask> taskList = service.findTaskByOrderName("LoadProfile");
//		long endTime = System.nanoTime() - startTime; // tiempo en que se ejecuta su método
//		System.out.println(endTime / 1e6 + " ms");
//		System.out.println(taskList);
//
//		long startTime2 = System.nanoTime();
//		List<EquipmentTask> taskList2 = service.findTaskByOrderNameSerialized("LoadProfile");
//		long endTime2 = System.nanoTime() - startTime2; // tiempo en que se ejecuta su método
//		System.out.println(endTime2 / 1e6 + " ms");
//
//	}
//
//}
