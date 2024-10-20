package com.minsait.oum.mdc.coap.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.minsait.mdc.data.model.Call;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandOrder;
import com.minsait.oum.mdc.domain.EquipmentTask;
import com.minsait.oum.mdc.domain.Order;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Data
public class OnDemandCommand {

	@Value("${protocol.version}")
	private String protocolVersion;

	@Autowired
	private ApplicationContext context;

	private final Long MAGIC_TIME = 1600000000000L;

	public void handleOnDemandRequest(Call call) {

		Map<String, Object> communication = call.getCommunication();
		JSONArray properties = new JSONObject(communication).getJSONArray("properties");

		String ip = null;
		String port = null;
		String serialNumber = null;

		for (int i = 0; i < properties.length(); i++) {
			JSONObject obj = properties.getJSONObject(i);
			if (obj.getString("name").equals("IP")) {
				ip = obj.getString("value");
			} else if (obj.getString("name").equals("PORT")) {
				port = obj.getString("value");
			} else if (obj.getString("name").equals("_SERIAL_NUMBER")) {
				serialNumber = obj.getString("value");
			}
		}
		String ipPort = ip + ":" + port;
		Map<String, OnDemandOrder> mapOrders = new HashMap<>();

		for (EquipmentTask current : call.getTasks()) {
			
			List<Order> orders = current.getOrder();
			String eqSerialNumber = current.getProtocol().get("_SERIAL_NUMBER");

			for (Order currentOrder : orders) {

				if (mapOrders.containsKey(currentOrder.getName())) {
					OnDemandOrder onDemandOrder = mapOrders.get(currentOrder.getName());
					onDemandOrder.getDevices().add(eqSerialNumber);
					setParams(currentOrder, onDemandOrder);
					mapOrders.put(currentOrder.getName(), onDemandOrder);

				} else {

					OnDemandOrder onDemandOrder = new OnDemandOrder();
					onDemandOrder.setGatewaySerialNumber(serialNumber);
					onDemandOrder.getDevices().add(eqSerialNumber);
					setParams(currentOrder, onDemandOrder);
					mapOrders.put(currentOrder.getName(), onDemandOrder);
				}
			}
		}
		mapOrders.entrySet().stream().forEach(entry -> {
			String command = entry.getKey();
			OnDemandOrder orders = entry.getValue();
			String uuid = null;
			try {
				if (StringUtils.isNotBlank(protocolVersion)) {
					command = command + "_" + protocolVersion.toUpperCase();
				}

				AbstractOnDemand specificCommand = (AbstractOnDemand) context.getBean(command);

				if (call.getIdDC() == null) {
					uuid = UUID.randomUUID().toString();
				} else {
					log.info("[COAP client] ResquestId to send: " + call.getIdDC());
					uuid = call.getIdDC();
				}
				log.info("about to execute onDemand command {} with call ID {} using order {}", command, uuid, orders);
				specificCommand.processOnDemandCommand(uuid, command, orders, ipPort);
			} catch (Exception ex) {
				log.error("error executing onDemand command {} with call ID {} using order {}", command, uuid, orders);
				throw new UnsupportedOperationException(
						String.format("error processing command %s: %s", entry.getKey(), ex.getMessage()), ex);
			}
		});
	}

	private void setParams(Order currentOrder, OnDemandOrder onDemandOrder) {
		if (onDemandOrder.getParameters().size() == 0) {
			Map<String, String> parameters = new HashMap<String, String>();
			if (currentOrder.getOrdersParams() != null) {
				Set<Entry<String, String>> entrySet = currentOrder.getOrdersParams().entrySet();
				entrySet.stream().forEach(e -> {
					parameters.put(e.getKey(), e.getValue());
				});
			}
			onDemandOrder.setParameters(parameters);
		}
	}

	// For set interval, need to discuss later TODO
	private void setSchedulerConfig(Order currentOrder, OnDemandOrder onDemandOrder) {

		if (onDemandOrder.getSchedulerConfig().size() == 0) {
			Map<String, String> parameters = new HashMap<>();

			if (currentOrder.getOrdersParams() != null) {
				Set<Entry<String, String>> entrySet = currentOrder.getOrdersParams().entrySet();
				entrySet.stream().forEach(e -> {
					parameters.put(e.getKey(), e.getValue());
				});
			}
			onDemandOrder.setParameters(parameters);
		}
	}
}
