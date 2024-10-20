package com.minsait.oum.mdc.listener.task;

import com.minsait.mdc.data.model.Call;
import com.minsait.mdc.data.model.CallGroup;
import com.minsait.mdc.util.Status;
import com.minsait.oum.mdc.domain.EquipmentTask;
import com.minsait.oum.mdc.domain.Order;
import com.minsait.oum.mdc.domain.TaskVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TaskServiceImpl implements TaskService {

	@Autowired
	private MongoOperations mongoOps;

	public void updateCallTaskStatus(TaskVO taskVO) {
		log.debug("about to process a new call/task status update request with payload {}", taskVO);

		if (taskVO.getPid() == null || taskVO.getPid().isEmpty())
			throw new IllegalArgumentException("cannot update call/tasks: new TaskVO message received without PID");

		Call call = this.findCall(taskVO);
		call.setFinishDate(Calendar.getInstance().getTimeInMillis());

		boolean callUpdate = taskVO.getTaskId() == null;

		if (callUpdate) {
			// call update
			this.processCall(call, taskVO);
		} else {
			// task update
			this.processTask(call, taskVO);
		}
		// update finishtime in call group
		Update callGroupUpdate = new Update().set("finishDate", call.getFinishDate());
		this.mongoOps.findAndModify(new Query(Criteria.where("_id").is(call.getIdGroup())), callGroupUpdate, CallGroup.class);
	}

	private void processCall(Call call, TaskVO taskVO) {
		Status callStatus = this.resolveStatus(taskVO);
		String statusMsg = this.resolveStatusMsg(callStatus, taskVO);

		if (taskVO.isApplyToAllTasks())
			applyToAllTasks(call, taskVO, callStatus, task -> true, order -> true);

		// build set clause
		Update callUpdate = new Update().set("status", callStatus).set("generalError", statusMsg)
				.set("finishDate", call.getFinishDate());
		if (taskVO.isApplyToAllTasks())
			callUpdate = callUpdate.set("tasks", call.getTasks());

		// update call status and task if needed
		this.mongoOps.findAndModify(new Query(Criteria.where("_id").is(taskVO.getPid())), callUpdate, Call.class);

	}

	private void processTask(Call call, TaskVO taskVO) {
		boolean orderUpdate = taskVO.getOrderId() != null;
		long taskCount = call.getTasks().stream().count();

		Status taskStatus = this.resolveStatus(taskVO);
		String statusMsg = this.resolveStatusMsg(taskStatus, taskVO);

		if (orderUpdate) {
			// update order
			this.processOrder(call, taskVO);
		} else {

			EquipmentTask task = findTask(taskVO, call);
			task.setStatus(taskStatus);
			task.setFinishTime(call.getFinishDate());
			// apply status to all tasks and then update on DB
			applyToAllOrders(task, taskVO, taskStatus, order -> true);

			Update taskUpdate = new Update().set("tasks.$[taskFilter]", task)
					.filterArray(Criteria.where("taskFilter._id").is(taskVO.getTaskId()));
			this.mongoOps.findAndModify(new Query(Criteria.where("_id").is(taskVO.getPid())), taskUpdate, Call.class);

			// if call has just one task, update call
			if (taskCount == 1)
				this.updateCall(taskVO, taskStatus, statusMsg, call.getFinishDate());

		}

	}

	private void processOrder(Call call, TaskVO taskVO) {

		Status orderStatus = this.resolveStatus(taskVO);
		String statusMsg = this.resolveStatusMsg(orderStatus, taskVO);

		EquipmentTask task = this.findTask(taskVO, call);
		Order order = this.findOrder(taskVO, task);
		order.setFinishTime(call.getFinishDate());
		long orderCount = task.getOrder().stream().count();
		long taskCount = call.getTasks().stream().count();

		this.setOrderStatus(taskVO, orderStatus, order);

		Update orderUpdate = new Update().set("tasks.$[taskFilter].order.$[orderFilter]", order)
				.filterArray(Criteria.where("taskFilter._id").is(taskVO.getTaskId()))
				.filterArray(Criteria.where("orderFilter._id").is(taskVO.getOrderId()));

		this.mongoOps.findAndModify(new Query(Criteria.where("_id").is(taskVO.getPid())), orderUpdate, Call.class);

		// if task has just one order update task call status also
		if (orderCount == 1)
			updateTask(taskVO, orderStatus, call.getFinishDate());

		// if call has just one task update call status also
		if (taskCount == 1)
			updateCall(taskVO, orderStatus, statusMsg, call.getFinishDate());

	}

	private void updateTask(final TaskVO taskVO, final Status status, final long finishTime) {
		Update taskUpdate = new Update().set("tasks.$[taskFilter].status", status)
				.set("tasks.$[taskFilter].finishTime", finishTime)
				.filterArray(Criteria.where("taskFilter._id").is(taskVO.getTaskId()));
		this.mongoOps.findAndModify(new Query(Criteria.where("_id").is(taskVO.getPid())), taskUpdate, Call.class);
	}

	private void updateCall(final TaskVO taskVO, final Status status, final String statusMsg, final long finishTime) {
		Update callUpdate = new Update().set("status", status).set("finishDate",finishTime);
		if (!status.equals(Status.FINISH_OK))
			callUpdate = callUpdate.set("generalError", statusMsg);
		this.mongoOps.findAndModify(new Query(Criteria.where("_id").is(taskVO.getPid())), callUpdate, Call.class);
	}

	private EquipmentTask findTask(TaskVO taskVO, Call call) {
		return call.getTasks().stream().filter(callTask -> callTask.getId().equals(taskVO.getTaskId())).findFirst()
				.orElseThrow(() -> new UnsupportedOperationException(
						String.format("cannot update task: task with id %d not found in call %s", taskVO.getTaskId(),
								taskVO.getPid())));
	}

	private Order findOrder(TaskVO taskVO, EquipmentTask task) {
		return task.getOrder().stream().filter(order -> order.getId().equals(taskVO.getOrderId())).findFirst()
				.orElseThrow(() -> new UnsupportedOperationException(
						String.format("cannot update order: order with id %d not found in task with id %d from call %s",
								taskVO.getOrderId(), taskVO.getTaskId(), taskVO.getPid())));
	}

	private void applyToAllTasks(Call call, TaskVO taskVO, Status taskStatus, Predicate<EquipmentTask> taskPredicate,
			Predicate<Order> orderPredicate) {
		call.getTasks().stream()
				// filter task by predicate
				.filter(taskPredicate)
				// apply status to all task and
				.forEach(task -> {
					task.setFinishTime(call.getFinishDate());
					task.setStatus(taskStatus);
					task.getOrder().stream()
							// filter orders by predicate
							.filter(orderPredicate).forEach(order -> {
								order.setFinishTime(call.getFinishDate());
								setOrderStatus(taskVO, taskStatus, order);
							});
				});
	}

	private void applyToAllOrders(EquipmentTask task, TaskVO taskVO, Status taskStatus,
			Predicate<Order> orderPredicate) {
		task.getOrder().stream()
				// filter orders by predicate
				.filter(orderPredicate).forEach(order -> {
					order.setFinishTime(task.getFinishTime());
					setOrderStatus(taskVO, taskStatus, order);
				});

	}

	private void setOrderStatus(TaskVO taskVO, Status orderStatus, Order order) {
		order.setStatus(orderStatus);
		if (orderStatus.equals(Status.FINISH_OK)) {
			order.setInfo(taskVO.getInfo());

		} else if (orderStatus.equals(Status.FINISH_WITH_ERROR) || orderStatus.equals(Status.ERROR)) {
			order.setInfo(taskVO.getInfo());
			order.setError(taskVO.getError());
		}
	}

	private Call findCall(TaskVO taskVO) {
		List<Call> call = this.mongoOps.find(new Query(Criteria.where("_id").is(taskVO.getPid())), Call.class);

		if (call.isEmpty())
			throw new UnsupportedOperationException(
					String.format("cannot update call/tasks: cannot find task with '_id' %s", taskVO.getPid()));

		return call.get(0);
	}

	private Status resolveStatus(TaskVO taskVO) {
		Status result = Status.FINISH_OK;
		if (taskVO.getError() != null && !taskVO.getError().isEmpty())
			result = Status.FINISH_WITH_ERROR;

		return result;
	}

	private String resolveStatusMsg(Status status, TaskVO taskVO) {

		Function<Map<Integer, String>, String> mapMessageParser = mapMessage -> mapMessage.entrySet().stream()//
				.sorted((entry1, entry2) -> Integer.compare(entry1.getKey(), entry2.getKey()))//
				.map(Entry::getValue)//
				.collect(Collectors.joining(", "));//

		String result = null;
		if (status.equals(Status.FINISH_OK)) {
			result = mapMessageParser.apply(taskVO.getInfo());

		} else if (status.equals(Status.FINISH_WITH_ERROR) || status.equals(Status.ERROR)) {
			result = mapMessageParser.apply(taskVO.getError());

		}

		return result;
	}


}
