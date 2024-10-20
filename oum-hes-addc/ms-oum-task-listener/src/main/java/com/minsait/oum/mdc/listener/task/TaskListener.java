package com.minsait.oum.mdc.listener.task;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.minsait.oum.mdc.domain.TaskVO;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TaskListener {

	@Autowired
	private TaskService service;

	@Autowired
	private Gson gson;
	
	@StreamListener(TaskStreams.INPUT)
	public void handlerTaskInput(@Payload String message) {
		log.info("Message input: " + message);

		try {
			PlatformTaskVO task = gson.fromJson(message, PlatformTaskVO.class);

			if (!task.getPid().equals("-1")) {

				Map<Integer, String> error = parseMessages((LinkedTreeMap<Integer, String>) task.getError());
				Map<Integer, String> debug = parseMessages((LinkedTreeMap<Integer, String>) task.getDebug());
				Map<Integer, String> info = parseMessages((LinkedTreeMap<Integer, String>) task.getInfo());

				service.updateCallTaskStatus(toTaskVO(error, debug, info, task));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
//			throw new UnsupportedOperationException(String.format("error handling tasks: %s", ex.getMessage()), ex);
		}

	}

	private TaskVO toTaskVO(Map<Integer, String> error, Map<Integer, String> debug, Map<Integer, String> info,
			PlatformTaskVO task) {
		TaskVO vo = new TaskVO();
		vo.setDebug(debug);
		vo.setError(error);
		vo.setInfo(info);
		vo.setMeterId(task.getMeterId());
		vo.setOrderId(task.getOrderId());
		vo.setPid(task.getPid());
		vo.setRequestId(task.getRequestId());
		vo.setTaskId(task.getTaskId());
		vo.setApplyToAllTasks(task.isApplyToAllTasks());
		return vo;
	}

	private Map<Integer, String> parseMessages(LinkedTreeMap<Integer, String> result) {
		Map<Integer, String> hashMap = new HashMap<Integer, String>();
		if (null != result && null != result.values()) {
			Object[] values = result.values().toArray();
			for (int i = 0; i < values.length; i++) {
				hashMap.put(i, (String) values[i]);
			}
		}
		return hashMap;
	}
}