/**
 * Created by Ram_Thirupathy on 8/18/2016.
 */
package com.ramkt.mobiAuto.scriptObjects;

import java.util.HashMap;

public class Events {
	public enum EVENTSKEY {
		VIDEO_PLAY_WAIT("VIDEO_PLAY_WAIT"),VIDEO_MONITOR_WAIT("VIDEO_MONITOR_WAIT"),VIDEO_MONITOR_ATTEMPT("VIDEO_MONITOR_ATTEMPT")
		,SCROLL_WAIT_TIME("SCROLL_WAIT_TIME"),SCROLL_Y_FROM_POSITION("SCROLL_Y_FROM_POSITION"),SCROLL_Y_TO_POSITION("SCROLL_Y_TO_POSITION");
		private EVENTSKEY(String name) {
		}
	}
	private HashMap<EVENTSKEY, Integer> eventConfiguration =new HashMap<EVENTSKEY, Integer>(); 
	public Events(){
	}

	public void setEventConfiguration(HashMap<EVENTSKEY, Integer> eventConfiguration) {
		this.eventConfiguration = eventConfiguration;
	}

	public int getConfigurationValue(EVENTSKEY name){
		return eventConfiguration.get(name);
	}

}
