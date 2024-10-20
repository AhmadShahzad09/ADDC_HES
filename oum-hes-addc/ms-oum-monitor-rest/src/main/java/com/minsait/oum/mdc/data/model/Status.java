package com.minsait.oum.mdc.data.model;

public enum Status {
	BUILDING, READY, RUNNING, ERROR, FINISH_WITH_ERROR, FINISH_WARNING, FINISH_OK, CANCELLED, DO_FINISH;

//	public static Status getStatusById(Integer id) {
//		switch (id) {
//		case 0:
//			return BUILDING;
//		case 1:
//			return READY;
//		case 2:
//			return RUNNING;
//		case 3:
//			return ERROR;
//		case 4:
//			return FINISH_WITH_ERROR;
//		case 5:
//			return FINISH_WARNING;
//		case 6:
//			return FINISH_OK;
//		case 7:
//			return CANCELLED;
//		case 8:
//			return DO_FINISH;
//		default:
//			break;
//		}
//		return null;
//	}
	
    public static Status calculateStatusByCounters(Long countBuilding, Long countReady, Long countRunning, Long countCancelled, Long countError, Long countFinishedWithWarning, Long countFinishedWithError, Long countFinishedOk) {
        if (countCancelled > 0) {
            return CANCELLED;
        }
        else if (countRunning > 0) {
            return RUNNING;
        }
        else if (countError > 0) {
            return ERROR;
        }
        else if (countFinishedWithError > 0) {
            return FINISH_WITH_ERROR;
        }
        else if (countFinishedWithWarning > 0) {
            return FINISH_WARNING;
        }
        else if (countBuilding > 0) {
            return BUILDING;
        }
        else if (countReady > 0) {
            return READY;
        }
        return FINISH_OK;
    }
	
	public boolean isVisible() {
		return (!FINISH_WARNING.equals(this) && !DO_FINISH.equals(this));
	}
    
}
