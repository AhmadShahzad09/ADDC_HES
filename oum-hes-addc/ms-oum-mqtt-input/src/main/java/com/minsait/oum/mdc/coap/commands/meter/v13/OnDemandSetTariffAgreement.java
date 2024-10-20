package com.minsait.oum.mdc.coap.commands.meter.v13;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.minsait.mdc.util.AppConstants;
import com.minsait.oum.mdc.coap.commands.AbstractOnDemand;
import com.minsait.oum.mdc.coap.commands.bean.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service("OnDemandSetTariffAgreement_V13")
public class OnDemandSetTariffAgreement extends AbstractOnDemand {

	@Autowired
	private Gson gson;
	@Override
	protected String getCoapPath() {
		return AppConstants.OnDemand.COAP_PATH.SET_TARIFF_AGREEMENT;
	}

	@Override
	protected void customizeCommand(OnDemandCommandVO commandVO, OnDemandOrder orders) {
		commandVO.setCalendarName(orders.getParameters().get("CalendarName"));
		commandVO.setActivateCalendarTime(orders.getParameters().get("ActivateCalendarTime"));
		
		String calendarDataJson = orders.getParameters().get("CalendarData");
		
		JsonObject calendarDataJsonObject = gson.fromJson(calendarDataJson, JsonObject.class);

		JsonArray jsonBlocks = calendarDataJsonObject.get("seasonProfile").getAsJsonArray();
		List<SeasonProfile> seasonProfileList = new ArrayList<SeasonProfile>();
		for (JsonElement jsonBlock : jsonBlocks) {
			SeasonProfile seasonProfile = new SeasonProfile();
			JsonObject jsonSeasonProfileObject = gson.fromJson(jsonBlock.toString(), JsonObject.class);
			seasonProfile.setSeasonProfileId(jsonSeasonProfileObject.get("seasonProfileId").getAsLong());
			seasonProfile.setSeasonStart(jsonSeasonProfileObject.get("seasonStart").getAsString());
			seasonProfile.setWeekProfileId(jsonSeasonProfileObject.get("weekProfileId").getAsLong());
			seasonProfileList.add(seasonProfile);
		}
		commandVO.setSeasonProfile(seasonProfileList);


		JsonArray jsonWeekProfile = calendarDataJsonObject.get("weekProfile").getAsJsonArray();
		List<WeekProfile> weekProfileList = new ArrayList<WeekProfile>();
		for (JsonElement jsonProfile : jsonWeekProfile) {
			WeekProfile weekProfile = new WeekProfile();
			JsonObject jsonWeekProfileObject = gson.fromJson(jsonProfile.toString(), JsonObject.class);
			weekProfile.setWeekProfileId(jsonWeekProfileObject.get("weekProfileId").getAsLong());
			weekProfile.setMonday(jsonWeekProfileObject.get("monday").getAsLong());
			weekProfile.setTuesday(jsonWeekProfileObject.get("tuesday").getAsLong());
			weekProfile.setWednesday(jsonWeekProfileObject.get("wednesday").getAsLong());
			weekProfile.setThursday(jsonWeekProfileObject.get("thursday").getAsLong());
			weekProfile.setFriday(jsonWeekProfileObject.get("friday").getAsLong());
			weekProfile.setSaturday(jsonWeekProfileObject.get("saturday").getAsLong());
			weekProfile.setSunday(jsonWeekProfileObject.get("sunday").getAsLong());
			weekProfileList.add(weekProfile);
		}
		commandVO.setWeekProfile(weekProfileList);


		JsonArray jsondayProfile = calendarDataJsonObject.get("dayProfile").getAsJsonArray();
//		DayProfile.DayItems dayItems = new DayProfile.DayItems();
		List<DayProfile> dayProfileList = new ArrayList<DayProfile>();
		for (JsonElement dayProfiles : jsondayProfile) {
			DayProfile dayProfile = new DayProfile();
			JsonObject jsonDayProfileObject = gson.fromJson(dayProfiles.toString(), JsonObject.class);
			dayProfile.setDayId(jsonDayProfileObject.get("dayId").getAsLong());
			dayProfile.setQty(jsonDayProfileObject.get("qty").getAsLong());
			dayProfile.setDayItem(getDayItems(jsonDayProfileObject,orders));
			dayProfileList.add(dayProfile);
		}
		commandVO.setDayProfile(dayProfileList);

	}
	
	private List<DayProfile.DayItems>  getDayItems(JsonObject jsonObject,OnDemandOrder orders){
		DayProfile.DayItems dayItems;
		JsonArray jsonDayItemsProfile = gson.fromJson(jsonObject.get("dayItems"), JsonArray.class);
		List<DayProfile.DayItems> itemsList = new ArrayList<DayProfile.DayItems>();
		for (JsonElement dayItemsProfiles : jsonDayItemsProfile) {
			dayItems = new DayProfile.DayItems();
			JsonObject jsonDayItemsProfileObject = gson.fromJson(dayItemsProfiles.toString(), JsonObject.class);
			dayItems.setStartTime(jsonDayItemsProfileObject.get("startTime").getAsString());
			dayItems.setScriptLogicalName(jsonDayItemsProfileObject.get("scriptLogicalName").getAsString());
			dayItems.setScriptSelector(jsonDayItemsProfileObject.get("scriptSelector").getAsLong());
			itemsList.add(dayItems);
		}
		return itemsList;
	}
}
