/**
 *  Monitor Max/Min Temperature Setting
 *
 *  Copyright 2014 Bob Sanford
 * 
 *  Visit Home Page for more information:
 *  http://goo.gl/i5yzuU
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
definition(
    name: "Monitor Max Heat/Min Cool Thermostat Settings",
    namespace: "midyear66",
    author: "Bob Sanford",
    description: "SmartApp to regulate max/min temperature setting of any connected thermostat",
    category: "Green Living",
    iconurl: "https://s3.amazonaws.com/smartthings-device-icons/thermostat/auto.png",
    iconX2Url: "https://s3.amazonaws.com/smartthings-device-icons/thermostat/auto@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartthings-device-icons/thermostat/auto@2x.png")


preferences {
	section("About") {
        paragraph "This SmartApp is a process that monitors the temperature setting " +
            "periodically for the selected thermostat.  If a user attempts to raise " +
            "the temperature above or below a selected value, the SmartApp will adjust " +
            "the setting to a designated Max/Min value.  This replaces telling my daughter; " +
            "Remember, a house does not warm up any faster if you set the temperature really high ;)!"
        paragraph "Version 1.0\nCopyright (c) 2014 ssetco.com"
    }
    
	section("Thermostat") {
		// TODO: put inputs here
        input "device", "capability.thermostat", title:"Select thermostat to be monitored", multiple:false, required:false
        input "min_cool", "number", title:"Set minimum cooling value", defaultValue:70
        input "max_heat", "number", title:"Set maximum heating value", defaultValue:74
        input "interval", "number", title:"Set monitor interval (in minutes)", defaultValue:5
	}
}

def installed() {
	log.debug "Installed with settings: ${settings}"

	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"
	
	unsubscribe()
	initialize()
}

def pollingTask() {
	def heatingSetpoint = settings.device.latestValue("heatingSetpoint")
    def coolingSetpoint = settings.device.latestValue("coolingSetpoint")
    DEBUG("Heating Set Point: ${heatingSetpoint}")
    DEBUG("Cooling Set Point: ${coolingSetpoint}")
    DEBUG("Heating Max Point: ${max_heat}")
    DEBUG("Cooling Min Point: ${min_cool}")
    
    	if (heatingSetpoint > max_heat){
    		DEBUG("Change heatingSetpoint to ${max_heat}")
            settings.device.setHeatingSetpoint(max_heat)
        }
    	if (coolingSetpoint < min_cool){
    		DEBUG("Change coolingSetpoint to ${min_cool}")
            settings.device.setCoolingSetpoint(min_cool)
        }
}

def initialize() {
	// TODO: subscribe to attributes, devices, locations, etc.
    def minutes = settings."interval".toInteger()
    if (minutes > 0) {
    	DEBUG("Scheduling monitor task to run every ${minutes} minutes.")
        def sched = "0 0/${minutes} * * * ?"
        schedule(sched, pollingTask)
    }
}

// TODO: implement event handlers
private def DEBUG(message) {
//    log.debug message
}
