package com.minsait.mdc.util;

import com.minsait.oum.mdc.domain.RequestStatus;

public enum Status {
	BUILDING, READY, RUNNING, ERROR, FINISH_WITH_ERROR, FINISH_WARNING, FINISH_OK, CANCELLED, DO_FINISH;

	public static Status calculateStatusByCounters(Long countBuilding, Long countReady, Long countRunning,
			Long countCancelled, Long countError, Long countFinishedWithWarning, Long countFinishedWithError,
			Long countFinishedOk) {
		if (countCancelled > 0) {
			return CANCELLED;
		} else if (countRunning > 0) {
			return RUNNING;
		} else if (countError > 0) {
			return ERROR;
		} else if (countFinishedWithError > 0) {
			return FINISH_WITH_ERROR;
		} else if (countFinishedWithWarning > 0) {
			return FINISH_WARNING;
		} else if (countBuilding > 0) {
			return BUILDING;
		} else if (countReady > 0) {
			return READY;
		}
		return FINISH_OK;
	}

	public static Status from(RequestStatus reqStatus) {
		Status result = null;
		if (reqStatus.equals(RequestStatus.ERROR)) {
			result = Status.ERROR;
		} else if (reqStatus.equals(RequestStatus.FAIL)) {
			result = Status.FINISH_WITH_ERROR;
		} else if (reqStatus.equals(RequestStatus.OK)) {
			result = Status.FINISH_OK;
		}

		return result;
	}
}
