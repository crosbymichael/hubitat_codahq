/**
 *  Gledopto RGBW LED strip light controller
 *
 *  
 *
 *  Copyright 2018 Ben Rimmasch
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

//import com.hubitat.zigbee.DataType

private getEndpointId() {
  logDebug "endpoint id: ${device.endpointId}"
  new BigInteger(device.endpointId, 16).toString()
}

private getDimmableEndpointId() {
  new BigInteger("0A", 16).toString()
}

private getCOLOR_CONTROL_CLUSTER() { 0x0300 }
private getHUE_SATURATION_COMMAND() { 6 }
private getWHITE_CONTROL_CLUSTER() { 0x0006 }
private getWHITE_LEVEL_CONTROL_CLUSTER() { 0x0008 }

metadata {

  definition(name: "Gledopto RGBW LED Controller", namespace: "codahq-hubitat", author: "Ben Rimmasch") {
    capability "Switch Level"
    capability "Actuator"
    capability "Color Control"
    capability "Color Temperature"
    capability "Switch"
    capability "Configuration"
    capability "Polling"
    capability "Refresh"
    capability "Sensor"

    command "setAdjustedColor"
    command "startLoop"
    command "stopLoop"
    command "setLoopTime", [[name: "Set Loop Time*", type: "NUMBER", description: "Enter a value in seconds"]]
    command "setDirection"

    command "whiteOn"
    command "whiteOff"
    command "setWhiteLevel", [[name: "White Level*", type: "NUMBER", description: "Enter a value between 0 and 100"], [name: "Duration", type: "NUMBER", description: "Enter a value in ms"]]

    command "alert", [[name: "Alert*", type: "ENUM", description: "Pick an action", constraints: ["Blink", "Breathe", "Okay", "Stop"]]]
    command "toggle"

    // This is a new temporary counter to keep track of no responses
    attribute "unreachable", "number"
    attribute "colorMode", "string"
    attribute "colorName", "string"
    attribute "switchColor", "string"
    attribute "loopActive", "string"
    attribute "loopDirection", "string"
    attribute "loopTime", "number"
    attribute "alert", "string"

    fingerprint profileId: "C05E", inClusters: "0000,0003,0004,0005,0006,0008,0300,1000", outClusters: "0019"
  }

  // simulator metadata
  simulator {
    // status messages
    status "on": "on/off: 1"
    status "off": "on/off: 0"

    // reply messages
    reply "zcl on-off on": "on/off: 1"
    reply "zcl on-off off": "on/off: 0"
  }

  // UI tile definitions
  tiles(scale: 2){
    multiAttributeTile(name: "switch", type: "lighting", width: 6, height: 4, canChangeIcon: true) {
      tileAttribute("device.switchColor", key: "PRIMARY_CONTROL") {
        attributeState "off", label: '${currentValue}', action: "switch.on", icon: "st.switches.switch.off", backgroundColor: "#ffffff"
        attributeState "Red", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#ff0000"
        attributeState "Brick Red", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#ff3700"
        attributeState "Safety Orange", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#ff6F00"
        attributeState "Dark Orange", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#ff9900"
        attributeState "Amber", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#ffbf00"
        attributeState "Gold", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#ffe1000"
        attributeState "Yellow", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#ffff00"
        attributeState "Electric Lime", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#bfff00"
        attributeState "Lawn Green", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#88ff00"
        attributeState "Bright Green", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#48ff00"
        attributeState "Lime", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#00ff11"
        attributeState "Spring Green", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#00ff6a"
        attributeState "Turquoise", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#00ffd0"
        attributeState "Aqua", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#00ffff"
        attributeState "Sky Blue", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#00bfff"
        attributeState "Dodger Blue", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#007bff"
        attributeState "Navy Blue", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#0050ff"
        attributeState "Blue", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#0000ff"
        attributeState "Han Purple", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#3b00ff"
        attributeState "Electric Indigo", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#6600ff"
        attributeState "Electric Purple", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#b200ff"
        attributeState "Orchid Purple", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#e900ff"
        attributeState "Magenta", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#ff00dc"
        attributeState "Hot Pink", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#ff00aa"
        attributeState "Deep Pink", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#ff007b"
        attributeState "Raspberry", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#ff0061"
        attributeState "Crimson", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#ff003b"
        attributeState "White", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#79b821"
        attributeState "Color Loop", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#79b821"
      }
      tileAttribute("device.color", key: "COLOR_CONTROL") {
        attributeState "color", action: "setAdjustedColor"
      }
      tileAttribute("device.level", key: "SECONDARY_CONTROL") {
        attributeState "level", label: 'Level is ${currentValue}%'
      }
    }
    standardTile("refresh", "device.switch", height: 2, width: 2, inactiveLabel: false, decoration: "flat") {
      state "default", label: "", action: "refresh.refresh", icon: "st.secondary.refresh"
    }
    controlTile("levelSliderControl", "device.level", "slider", height: 1, width: 4, inactiveLabel: false) {
      state "level", action: "switch level.setLevel"
    }
    controlTile("colorTempSliderControl", "device.colorTemperature", "slider", height: 1, width: 4, inactiveLabel: false, range: "(2000..6500)") {
      state "colorTemperature", action: "color temperature.setColorTemperature"
    }
    valueTile("colorTemp", "device.colorTemperature", height: 1, width: 2, inactiveLabel: false, decoration: "flat") {
      state "colorTemperature", label: '${currentValue} K'
    }
    valueTile("colorName", "device.colorName", height: 1, width: 2, inactiveLabel: false, decoration: "flat") {
      state "colorName", label: '${currentValue}'
    }

    standardTile("ww", "device.ww", height: 1, width: 1, inactiveLabel: false, decoration: "flat", canChangeIcon: false) {
      state "off", label: "WW/W", action: "whiteOn", icon: "st.illuminance.illuminance.dark", backgroundColor: "#cccccc"
      state "on", label: "WW/W", action: "whiteOff", icon: "st.illuminance.illuminance.bright", backgroundColor: "#000000"
    }
    controlTile("wwSliderControl", "device.wwLevel", "slider", height: 1, width: 4, inactiveLabel: false) {
      state "wwLevel", action: "setWhiteLevel"
    }
    valueTile("wwValueTile", "device.wwLevel", height: 1, width: 1) {
      state "wwLevel", label: '${currentValue}%'
    }

    valueTile("colorMode", "device.colorMode", height: 2, width: 2, inactiveLabel: false, decoration: "flat") {
      state "colorMode", label: '${currentValue}'
    }
    standardTile("loop", "device.loopActive", height: 2, width: 2, inactiveLabel: false, decoration: "flat") {
      state "Active", label: '${currentValue}', action: "stopLoop", backgroundColor: "#79b821", nextState: "stoppingLoop"
      state "startingLoop", label: "Starting Loop", action: "stopLoop", backgroundColor: "#79b821", nextState: "stoppingLoop"
      state "Inactive", label: '${currentValue}', action: "startLoop", backgroundColor: "#ffffff", nextState: "startingLoop"
      state "stoppingLoop", label: "Stopping Loop", action: "startLoop", backgroundColor: "#ffffff", nextState: "startingLoop"
    }
    controlTile("loopTimeControl", "device.loopTime", "slider", height: 2, width: 4, range: "(1..60)", inactiveLabel: false) {
      state "loopTime", action: "setLoopTime"
    }
    standardTile("loopDir", "device.loopDirection", height: 2, width: 2, inactiveLabel: false, decoration: "flat") {
      state "default", label: '${currentValue}', action: "setDirection"
    }


    main(["switch"])
    details(["switch", "levelSliderControl", "colorName", "colorTempSliderControl", "colorTemp", "ww", "wwSliderControl", "wwValueTile", "colorMode", "loop", "refresh", "loopTimeControl", "loopDir"])
  }

  preferences {
    input name: "descriptionTextEnable", type: "bool", title: "Enable descriptionText logging", defaultValue: false
    input name: "logEnable", type: "bool", title: "Enable debug logging", defaultValue: false
    input name: "traceLogEnable", type: "bool", title: "Enable trace logging", defaultValue: false
  }
}

private logInfo(msg) {
  if (descriptionTextEnable) log.info msg
}

def logDebug(msg) {
  if (logEnable) log.debug msg
}

def logTrace(msg) {
  if (traceLogEnable) log.trace msg
}

def refresh() {

  def unreachable = device.currentValue("unreachable")
  if (unreachable == null) {
    sendEvent(name: 'unreachable', value: 1)
  }
  else {
    sendEvent(name: 'unreachable', value: unreachable + 1)
  }
  if (unreachable > 2) {
    sendEvent(name: "switch", value: "off")
    sendEvent(name: "switchColor", value: "off", displayed: false)
  }

  // Ping the device with color as to not get out of sync 
  return [
    "he rattr 0x${device.deviceNetworkId} ${endpointId} 6 0", "delay 500",
    "he rattr 0x${device.deviceNetworkId} ${endpointId} 8 0", "delay 500",
    "he rattr 0x${device.deviceNetworkId} ${endpointId} 0x0300 1", "delay 500",
    "he rattr 0x${device.deviceNetworkId} ${endpointId} 0x0300 0", "delay 500",
    "he rattr 0x${device.deviceNetworkId} ${endpointId} 0x0300 7", "delay 500",
    "he rattr 0x${device.deviceNetworkId} ${endpointId} 0x0300 8", "delay 500",
    "he wattr 0x${device.deviceNetworkId} ${endpointId} 8 0x10 0x21 {0015}"
  ]
}

def poll(){
  logDebug "Poll is calling refresh"
  refresh()
}

def configure(){
  logDebug "Initiating configuration reporting and binding"

  return [
    //These aren't implemented in hubitat so i'll do them manually as strings
    //zigbee.configSetup("6","0","0x10","0","60","{}"), "delay 1000",
    //Did I get it right?
    "he cr 0x${device.deviceNetworkId} 0x${device.endpointId} 0x0006 0 0x10 0 0x003C {}", "delay 1000",
    //zigbee.configSetup("8","0","0x20","5","600","{10}"), "delay 1500",
    "he cr 0x${device.deviceNetworkId} 0x${device.endpointId} 0x0008 0 0x20 5 0x0258 {10}", "delay 200",
    "zdo bind 0x${device.deviceNetworkId} ${endpointId} 1 6 {${device.zigbeeId}} {}", "delay 1000",
    "zdo bind 0x${device.deviceNetworkId} ${endpointId} 1 8 {${device.zigbeeId}} {}", "delay 1000",
    "zdo bind 0x${device.deviceNetworkId} ${endpointId} 1 0x0300 {${device.zigbeeId}} {}"
  ]
}

def updated() {
  state.counter = state.counter ? state.counter + 1 : 1
  if (state.counter == 1) {
    addChildWhiteChannel()
  }
}

def installed() {
  updated()
}

def addChildWhiteChannel() {
  logDebug "addChildWhiteChannel"
  try {
    //try to add the child device.  it may fail if the device handler is not present.  
    addChildDevice(
      "codahq-hubitat",
      "Gledopto RGBW LED White Channel Controller",
      "${device.deviceNetworkId}-1",
      [completedSetup: true, label: "${device.displayName} White Channel", isComponent: false, componentName: "whiteChannel", componentLabel: "White Channel"]
    )
  }
  catch (e) {
    logInfo "Adding child device for white channel failed!  Was the child device handler installed?"
  }
}

def notifyChildren(events) {
  def children = getChildDevices()
  children.each {
    child ->

      events.each {
      event ->

        logInfo "Notifying child ${child} of ${event}"

      if (event.name == "ww") {
        child.sendEvent(name: "switch", value: event.value)
      }
      else if (event.name == "wwLevel") {
        if (event.value == 0) {
          child.sendEvent(name: "switch", value: "off")
        }
        else if (child.currentValue("whiteChannel") == "off") {
          child.sendEvent(name: "switch", value: "on")
        }
        child.sendEvent(name: "level", value: event.value)
      }
    }
  }

}

// Parse incoming device messages to generate events
def parse(String description) {
  def events = []
  def cluster = zigbee.parse(description)

  if (cluster && cluster.sourceEndpoint == Short.parseShort(dimmableEndpointId)) {
    logDebug "Warm White: $description"
    logTrace "Cluster - $cluster"

    if (cluster.clusterId == WHITE_CONTROL_CLUSTER && cluster.data[0] == 1) {
      events += createEvent(name: "ww", value: "on")
    }
    else if (cluster.clusterId == WHITE_CONTROL_CLUSTER && cluster.data[0] == 0) {
      events += createEvent(name: "ww", value: "off")
    }
    else if (cluster.clusterId == WHITE_LEVEL_CONTROL_CLUSTER) {
      //don't do anything for now
    }
    notifyChildren(events)
  }
  else {
    logDebug "RGB: $description"
    sendEvent(name: "unreachable", value: 0)
    if (device.currentValue("loopActive") == "Active") {
    }
    else {
      if (description ?.startsWith("catchall:")) {
        if (description ?.endsWith("0100") || description ?.endsWith("1001") || description ?.matches("on/off\\s*:\\s*1")) {
          def result = createEvent(name: "switch", value: "on")
          sendEvent(name: "switchColor", value: (device.currentValue("colorMode") == "White" ? "White" : device.currentValue("colorName")), displayed: false)
          logDebug "Parse returned ${result?.descriptionText}"
          return result
        }
        else if (description ?.endsWith("0000") || description ?.endsWith("1000") || description ?.matches("on/off\\s*:\\s*0")) {
          if (!(description ?.startsWith("catchall: 0104 0300") || description ?.startsWith("catchall: 0104 0008"))) {
            def result = createEvent(name: "switch", value: "off")
            sendEvent(name: "switchColor", value: "off", displayed: false)
            logDebug "Parse returned ${result?.descriptionText}"
            return result
          }
        }
      }

      if (description ?.startsWith("read attr -")) {
        def descMap = parseDescriptionAsMap(description)
        logTrace "descMap : $descMap"
        if (descMap.cluster == "0300") {
          if (descMap.attrId == "0000") {  //Hue Attribute
            def hueValue = Math.round(convertHexToInt(descMap.value) / 255 * 100)
            logDebug "Hue value returned is $hueValue"
            def colorName = getColorName(hueValue)
            sendEvent(name: "colorName", value: colorName)
            if (device.currentValue("switch") == "on") {
              sendEvent(name: "switchColor", value: (device.currentValue("colorMode") == "White" ? "White" : device.currentValue("colorName")), displayed: false)
            }
            sendEvent(name: "hue", value: hueValue, displayed: false)
          }
          else if (descMap.attrId == "0001") { //Saturation Attribute
            def saturationValue = Math.round(convertHexToInt(descMap.value) / 255 * 100)
            logDebug "Saturation from refresh is $saturationValue"
            sendEvent(name: "saturation", value: saturationValue, displayed: false)
          }
          else if (descMap.attrId == "0007") {
            def tempInMired = convertHexToInt(descMap.value)
            def tempInKelvin = Math.round(1000000 / tempInMired)
            logDebug "Color temperature returned is $tempInKelvin"
            sendEvent(name: "colorTemperature", value: tempInKelvin)
          }
          else if (descMap.attrId == "0008") {
            def colorModeValue = (descMap.value == "02" ? "White" : "Color")
            logDebug "Color mode returned $colorModeValue"
            sendEvent(name: "colorMode", value: colorModeValue)
            if (device.currentValue("switch") == "on") {
              sendEvent(name: "switchColor", value: (descMap.value == "02" ? "White" : device.currentValue("colorName")), displayed: false)
            }
          }
        }
        else if (descMap.cluster == "0008") {
          def dimmerValue = Math.round(convertHexToInt(descMap.value) * 100 / 255)
          logDebug "dimmer value is $dimmerValue"
          sendEvent(name: "level", value: dimmerValue)
        }
      }
      else {
        def name = description ?.startsWith("on/off: ") ? "switch" : null
                def value = null
        if (name == "switch") {
          value = (description ?.endsWith(" 1") ? "on" : "off")
          logDebug value
          sendEvent(name: "switchColor", value: (value == "off" ? "off" : device.currentValue("colorName")), displayed: false)
        }
        else { value = null }
        def result = createEvent(name: name, value: value)
        logDebug "Parse returned ${result?.descriptionText}"
        return result
      }

    }
  }
  for (ev in events) {
    logDebug "Event - $ev.name: $ev.value"
    sendEvent(ev)
  }
}

def parseDescriptionAsMap(description) {
  (description - "read attr - ").split(",").inject([: ]) {
    map, param ->
      def nameAndValue = param.split(":")
    map += [(nameAndValue[0].trim()): nameAndValue[1].trim()]
  }
}

def on(onTime = null) {
  // just assume it works for now
  logInfo "Turning RGB on"
  logDebug "on()"
  sendEvent(name: "switch", value: "on")
  sendEvent(name: "switchColor", value: (device.currentValue("colorMode") == "White" ? "White" : device.currentValue("colorName")), displayed: false)

  if (onTime) {
    def newTime = onTime * 10
    def finalHex = swapEndianHex(hexF(newTime, 4))
    runIn(onTime, refresh)
    "st cmd 0x${device.deviceNetworkId} ${endpointId} 6 0x42 {00 ${finalHex} 0000}"
  }
  else {
    "st cmd 0x${device.deviceNetworkId} ${endpointId} 6 1 {}"
  }
}

def off() {
  // just assume it works for now
  logInfo "Turning RGB off"
  logDebug "off()"
  sendEvent(name: "loopActive", value: "Inactive")
  sendEvent(name: "switch", value: "off")
  sendEvent(name: "switchColor", value: "off", displayed: false)
  def cmds = []
  cmds << "st cmd 0x${device.deviceNetworkId} ${endpointId} 6 0 {}"
  //cmds << "st cmd 0x${device.deviceNetworkId} ${dimmableEndpointId} 6 0 {}"
  cmds
}

def toggle() {
  logInfo "Toggling RGB to ${device.currentValue("switch") == "on" ? "off" : "on"}"
  if (device.currentValue("switch") == "on") {
    sendEvent(name: "switch", value: "off")
    sendEvent(name: "switchColor", value: "off")
  }
  else {
    sendEvent(name: "switch", value: "on")
    sendEvent(name: "switchColor", value: (device.currentValue("colorMode") == "White" ? "White" : device.currentValue("colorName")), displayed: false)
  }
  "st cmd 0x${device.deviceNetworkId} ${endpointId} 6 2 {}"
}

def whiteOn(onTime = null) {
  logInfo "Turning white on"
  logDebug "whiteOn()"
  sendEvent(name: "ww", value: "on")
  if (onTime) {
    def newTime = onTime * 10
    def finalHex = swapEndianHex(hexF(newTime, 4))
    runIn(onTime, refresh)
    "st cmd 0x${device.deviceNetworkId} ${dimmableEndpointId} 6 0x42 {00 ${finalHex} 0000}"
  }
  else {
    "st cmd 0x${device.deviceNetworkId} ${dimmableEndpointId} 6 1 {}"
  }
}
def whiteOff() {
  logInfo "Turning white off"
  logDebug "whiteOff()"
  sendEvent(name: "ww", value: "off")
  "st cmd 0x${device.deviceNetworkId} ${dimmableEndpointId} 6 0 {}"
}

// adding duration to enable transition time adjustments
def setWhiteLevel(value, duration = 21) {
  logInfo "Setting white level to ${value}"
  logDebug "setWhiteLevel: ${value}"
  def transitionTime = swapEndianHex(hexF(duration, 4))


  def unreachable = device.currentValue("unreachable")
  logDebug "Unreachable ${unreachable}"
  if (unreachable == null) {
    sendEvent(name: 'unreachable', value: 1)
  }
  else {
    sendEvent(name: 'unreachable', value: unreachable + 1)
  }
  if (unreachable > 2) {
    sendEvent(name: "switch", value: "off")
    sendEvent(name: "switchColor", value: "off", displayed: false)
  }

  def cmds = []

  if (value == 0) {
    sendEvent(name: "ww", value: "off")
    cmds << "st cmd 0x${device.deviceNetworkId} ${dimmableEndpointId} 6 0 {}"
  }
  else if (device.currentValue("ww") == "off" && unreachable < 2) {
    sendEvent(name: "ww", value: "on")
  }

  def events = []
  events += createEvent(name: "wwLevel", value: value)
  notifyChildren(events)
  sendEvent(name: "wwLevel", value: value)

  def level = hex(value * 2.55)

  cmds << "st cmd 0x${device.deviceNetworkId} ${dimmableEndpointId} 8 4 {${level} ${transitionTime}}"

  logDebug cmds
  cmds
}


def alert(action) {
  def value = "00"
  def valid = true
  switch (action) {
    case "Blink":
      value = "00"
      break
    case "Breathe":
      value = "01"
      break
    case "Okay":
      value = "02"
      break
    case "Stop":
      value = "ff"
      break
    default:
      valid = false
      break
  }
  if (valid) {
    logDebug "Alert: ${action}, Value: ${value}"
    sendEvent(name: "alert", value: action)
    "st cmd 0x${device.deviceNetworkId} ${endpointId} 3 0x40 {${value} 00}"
  }
  else { logDebug "Invalid action" }
}

def setDirection() {
  def direction = (device.currentValue("loopDirection") == "Down" ? "Up" : "Down")
  logTrace direction
  sendEvent(name: "loopDirection", value: direction)
  if (device.currentValue("loopActive") == "Active") {
    def dirHex = (direction == "Down" ? "00" : "01")
    logTrace dirHex
    "st cmd 0x${device.deviceNetworkId} ${endpointId} 0x300 0x44 {02 01 ${dirHex} 0000 0000}"
  }
}

def setLoopTime(value) {
  sendEvent(name: "loopTime", value: value)
  if (device.currentValue("loopActive") == "Active") {
    def finTime = swapEndianHex(hexF(value, 4))
    "st cmd 0x${device.deviceNetworkId} ${endpointId} 0x300 0x44 {04 01 00 ${finTime} 0000}"
  }
}

def startLoop(Map params) {
  // direction either increments or decrements the hue value: "Up" will increment, "Down" will decrement
  def direction = (device.currentValue("loopDirection") != null ? (device.currentValue("loopDirection") == "Down" ? "00" : "01") : "00")
  logTrace direction
  if (params ?.direction != null) {
    direction = (params.direction == "Down" ? "00" : "01")
    sendEvent(name: "loopDirection", value: params.direction)
  }
  logTrace direction

  // time parameter is the time in seconds for a full loop
  def cycle = (device.currentValue("loopTime") != null ? device.currentValue("loopTime") : 2)
  logTrace cycle
  if (params ?.time != null) {
    cycle = params.time
    if (cycle >= 1 && cycle <= 60) { sendEvent(name: "loopTime", value: cycle) }
  }
  logTrace cycle
  def finTime = swapEndianHex(hexF(cycle, 4))
  logTrace finTime

  def cmds = []
  cmds << "st cmd 0x${device.deviceNetworkId} ${endpointId} 6 1 {}"
  cmds << "delay 200"

  sendEvent(name: "switchColor", value: "Color Loop", displayed: false)
  sendEvent(name: "loopActive", value: "Active")

  if (params ?.hue != null) {

    // start hue was specified, so convert to enhanced hue and start loop from there
    def sHue = Math.min(Math.round(params.hue * 255 / 100), 255)
    finHue = swapEndianHex(hexF(sHue, 4))
    logDebug "activating color loop from specified hue"
    cmds << "st cmd 0x${device.deviceNetworkId} ${endpointId} 0x300 0x44 {0F 01 ${direction} ${finTime} ${sHue}}"

  }
  else {

    // start hue was not specified, so start loop from current hue updating direction and time
    logDebug "activating color loop from current hue"
    cmds << "st cmd 0x${device.deviceNetworkId} ${endpointId} 0x300 0x44 {07 02 ${direction} ${finTime} 0000}"

  }
  cmds
}

def stopLoop() {

  logDebug "deactivating color loop"
  def cmds = [
    "st cmd 0x${device.deviceNetworkId} ${endpointId} 0x300 0x44 {01 00 00 0000 0000}", "delay 200",
    "st rattr 0x${device.deviceNetworkId} ${endpointId} 0x0300 0", "delay 200",
    "st rattr 0x${device.deviceNetworkId} ${endpointId} 0x0300 1", "delay 200",
    "st rattr 0x${device.deviceNetworkId} ${endpointId} 0x0300 7", "delay 200",
    "st rattr 0x${device.deviceNetworkId} ${endpointId} 0x0300 8", "delay 200",
    "st rattr 0x${device.deviceNetworkId} ${endpointId} 8 0"
  ]
  sendEvent(name: "loopActive", value: "Inactive")

  cmds

}

def setColorTemperature(value, duration = 32) {
  if (value < 101) {
    value = (value * 38) + 2700		//Calculation of mapping 0-100 to 2700-6500
  }

  def transitionTime = swapEndianHex(hexF(duration, 4))

  def tempInMired = Math.round(1000000 / value)
  def finalHex = swapEndianHex(hexF(tempInMired, 4))
  // def genericName = getGenericName(value)
  // logDebug "generic name is : $genericName"

  def cmds = []

  if (device.latestValue("switch") == "off") {
    cmds << "st cmd 0x${device.deviceNetworkId} ${endpointId} 6 1 {}"
    cmds << "delay 150"
  }
  sendEvent(name: "colorTemperature", value: value, displayed: false)
  sendEvent(name: "colorMode", value: "White")
  sendEvent(name: "switchColor", value: "White", displayed: false)
  // sendEvent(name: "colorName", value: genericName)

  cmds << "st cmd 0x${device.deviceNetworkId} ${endpointId} 0x0300 0x0a {${finalHex} ${transitionTime}}"

  cmds
}

def setHue(value, duration = 32) {
  def max = 0xfe
  def transitionTime = swapEndianHex(hexF(duration, 4))

  logTrace "here: setHue($value)"
  sendEvent(name: "hue", value: value)
  def scaledValue = Math.round(value * max / 100.0)
  def cmd = "st cmd 0x${device.deviceNetworkId} ${endpointId} 0x300 0x00 {${hex(scaledValue)} 00 ${transitionTime}}"
  logTrace "and here: ${cmd}"
  cmd
}

def setAdjustedColor(value, duration = 32) {
  logDebug "setAdjustedColor: ${value}"
  def adjusted = value + [: ]
  //adjusted.hue = adjustOutgoingHue(value.hue)
  //adjusted.hue = convertPercentToHue(adjusted.hue)
  adjusted.level = null // needed because color picker always sends 100
  setColor(adjusted, duration)
}



//endpoints
//0x0A, a ZLL Dimmable Light
//0x0B, a ZLL Extended Color Light
//0x0D, with device ID 0xE15E for the (client and server) ZLL Commissioning clusters - not sure if this one applies to GLEDOPTO RGBW - I don't know how to query device ID yet and I don't see teh value in graph
//Both dimmable light and extended color light endpoints support 16 groups and 16 scenes
/*
The Extended Color Light endpoint supports Hue/Saturation, Enhanced Hue/Saturation, X/Y, and Color Temperature. Both Color Mode and 
Enhanced Color Mode reflect the correct value. When setting (Enhanced) Hue/Saturation, the light updates Current X and Current Y; 
when setting X/Y, it updates (Enhanced) Hue and Saturation.

Color Temperature seems a world in itself. The lights don't report ctmin and ctmax. The RGBW reflects the colour temperature from 
153 to 500 fairly accurate. When setting Colour Temperature to 1 or 1000, the lights don't report the actual value. 

This information came from:
https://github.com/ebaauw/homebridge-hue/issues/244

Good code examples for a similar project here:
https://github.com/puzzle-star/SmartThings-IKEA-Tradfri-RGB
*/

def setColor(value, duration = 32){
  logDebug "setColor($value)"

  def transitionTime = swapEndianHex(hexF(duration, 4))

  def max = 0xfe
  if (value.hue == 0 && value.saturation == 0) {
    setColorTemperature(3500)
  }
  else if (value.red == 255 && value.blue == 185 && value.green == 255) {
    setColorTemperature(2700)
  }
  else {
    if (value.hex) {
      sendEvent(name: "color", value: value.hex, displayed: false)
    }

    def colorName = getColorName(value.hue)
    sendEvent(name: "colorName", value: colorName)
    sendEvent(name: "colorMode", value: "Color")
    sendEvent(name: "switchColor", value: device.currentValue("colorName"), displayed: false)

    logDebug "color name is : $colorName"
    sendEvent(name: "hue", value: value.hue, displayed: false)
    sendEvent(name: "saturation", value: value.saturation, displayed: false)
    def scaledHueValue = evenHex(Math.round(value.hue * max / 100.0))
    def scaledSatValue = evenHex(Math.round(value.saturation * max / 100.0))

    logDebug "value.hue: ${value.hue}"
    logDebug "value.saturation: ${value.saturation}"
    logDebug "scaledHueValue: ${scaledHueValue}"
    logDebug "scaledSatValue: ${scaledSatValue}"

    log.info "this ${Math.round(value.hue * max / 100)}"
    scaledHueValue = hubitat.helper.HexUtils.integerToHexString(Math.round(value.hue * max / 100) as Integer, 1)
    scaledSatValue = hubitat.helper.HexUtils.integerToHexString(Math.round(value.saturation * max / 100) as Integer, 1)

    logDebug "scaledHueValue: ${scaledHueValue}"
    logDebug "scaledSatValue: ${scaledSatValue}"

    logDebug "transitionTime: ${transitionTime}"

    def cmd = []
    if (value.switch != "off" && device.latestValue("switch") == "off") {
      cmd << "he cmd 0x${device.deviceNetworkId} ${endpointId} 6 1 {}"
      cmd << "delay 150"
    }

    //cmd << "st cmd 0x${device.deviceNetworkId} ${endpointId} 0x300 0x06 {${scaledHueValue} ${scaledSatValue} ${transitionTime}}"
    cmd << "he cmd 0x${device.deviceNetworkId} ${endpointId} 0x300 0x06 {${scaledHueValue} ${scaledSatValue} ${transitionTime}}"
    //cmd << zigbee.command(COLOR_CONTROL_CLUSTER, HUE_SATURATION_COMMAND, scaledHueValue, scaledSatValue, "0000")

    if (value.level) {
      state.levelValue = value.level
      sendEvent(name: "level", value: value.level)
      def level = hex(value.level * 254 / 100)
      cmd << "he cmd 0x${device.deviceNetworkId} ${endpointId} 8 4 {${level} 1500}"
    }

    if (value.switch == "off") {
      cmd << "delay 150"
      cmd << off()
    }

    cmd
  }
}

def setSaturation(value, duration = 32) {
  logTrace "setSaturation(${value})"
  def transitionTime = swapEndianHex(hexF(duration, 4))
  def max = 0xfe
  sendEvent(name: "saturation", value: value)
  def scaledValue = Math.round(value * max / 100.0)
  def cmd = "st cmd 0x${device.deviceNetworkId} ${endpointId} 0x300 0x03 {${hex(scaledValue)} ${transitionTime}}"
  logTrace cmd
  cmd
}



//input Hue Integer values; returns color name for saturation 100%
private getColorName(hueValue){
  if (hueValue > 360 || hueValue < 0)
    return

  hueValue = Math.round(hueValue / 100 * 360)

  logDebug "hue value is $hueValue"

  def colorName = "Color Mode"
  if (hueValue >= 0 && hueValue <= 4) {
    colorName = "Red"
  }
  else if (hueValue >= 5 && hueValue <= 21) {
    colorName = "Brick Red"
  }
  else if (hueValue >= 22 && hueValue <= 30) {
    colorName = "Safety Orange"
  }
  else if (hueValue >= 31 && hueValue <= 40) {
    colorName = "Dark Orange"
  }
  else if (hueValue >= 41 && hueValue <= 49) {
    colorName = "Amber"
  }
  else if (hueValue >= 50 && hueValue <= 56) {
    colorName = "Gold"
  }
  else if (hueValue >= 57 && hueValue <= 65) {
    colorName = "Yellow"
  }
  else if (hueValue >= 66 && hueValue <= 83) {
    colorName = "Electric Lime"
  }
  else if (hueValue >= 84 && hueValue <= 93) {
    colorName = "Lawn Green"
  }
  else if (hueValue >= 94 && hueValue <= 112) {
    colorName = "Bright Green"
  }
  else if (hueValue >= 113 && hueValue <= 135) {
    colorName = "Lime"
  }
  else if (hueValue >= 136 && hueValue <= 166) {
    colorName = "Spring Green"
  }
  else if (hueValue >= 167 && hueValue <= 171) {
    colorName = "Turquoise"
  }
  else if (hueValue >= 172 && hueValue <= 187) {
    colorName = "Aqua"
  }
  else if (hueValue >= 188 && hueValue <= 203) {
    colorName = "Sky Blue"
  }
  else if (hueValue >= 204 && hueValue <= 217) {
    colorName = "Dodger Blue"
  }
  else if (hueValue >= 218 && hueValue <= 223) {
    colorName = "Navy Blue"
  }
  else if (hueValue >= 224 && hueValue <= 251) {
    colorName = "Blue"
  }
  else if (hueValue >= 252 && hueValue <= 256) {
    colorName = "Han Purple"
  }
  else if (hueValue >= 257 && hueValue <= 274) {
    colorName = "Electric Indigo"
  }
  else if (hueValue >= 275 && hueValue <= 289) {
    colorName = "Electric Purple"
  }
  else if (hueValue >= 290 && hueValue <= 300) {
    colorName = "Orchid Purple"
  }
  else if (hueValue >= 301 && hueValue <= 315) {
    colorName = "Magenta"
  }
  else if (hueValue >= 316 && hueValue <= 326) {
    colorName = "Hot Pink"
  }
  else if (hueValue >= 327 && hueValue <= 335) {
    colorName = "Deep Pink"
  }
  else if (hueValue >= 336 && hueValue <= 339) {
    colorName = "Raspberry"
  }
  else if (hueValue >= 340 && hueValue <= 352) {
    colorName = "Crimson"
  }
  else if (hueValue >= 353 && hueValue <= 360) {
    colorName = "Red"
  }

  colorName
}

// adding duration to enable transition time adjustments
def setLevel(value, duration = 21) {
  logInfo "Setting RGB level to ${value}"
  logTrace "setLevel($value)"
  def transitionTime = swapEndianHex(hexF(duration, 4))


  def unreachable = device.currentValue("unreachable")
  logDebug unreachable
  if (unreachable == null) {
    sendEvent(name: 'unreachable', value: 1)
  }
  else {
    sendEvent(name: 'unreachable', value: unreachable + 1)
  }
  if (unreachable > 2) {
    sendEvent(name: "switch", value: "off")
    sendEvent(name: "switchColor", value: "off", displayed: false)
  }

  def cmds = []

  if (value == 0) {
    sendEvent(name: "switch", value: "off")
    sendEvent(name: "switchColor", value: "off", displayed: false)
    cmds << "st cmd 0x${device.deviceNetworkId} ${endpointId} 6 0 {}"
  }
  else if (device.currentValue("switch") == "off" && unreachable < 2) {
    sendEvent(name: "switch", value: "on")
    logDebug device.currentValue("colorMode")
    sendEvent(name: "switchColor", value: (device.currentValue("colorMode") == "White" ? "White" : device.currentValue("colorName")), displayed: false)
  }

  sendEvent(name: "level", value: value)
  def level = hex(value * 2.55)
  if (value == 1) { level = hex(1) }
  cmds << "st cmd 0x${device.deviceNetworkId} ${endpointId} 8 4 {${level} ${transitionTime}}"

  //logDebug cmds
  cmds
}



private hex(value, width = 2) {
  def s = new BigInteger(Math.round(value).toString()).toString(16)
  while (s.size() < width) {
    s = "0" + s
  }
  s
}

private evenHex(value){
  def s = new BigInteger(Math.round(value).toString()).toString(16)
  while (s.size() % 2 != 0) {
    s = "0" + s
  }
  s
}

//Need to reverse array of size 2
private byte[] reverseArray(byte[] array) {
  byte tmp;
  tmp = array[1];
  array[1] = array[0];
  array[0] = tmp;
  return array
}

private hexF(value, width) {
  def s = new BigInteger(Math.round(value).toString()).toString(16)
  while (s.size() < width) {
    s = "0" + s
  }
  s
}

private String swapEndianHex(String hex) {
  reverseArray(hex.decodeHex()).encodeHex()
}

private Integer convertHexToInt(hex) {
  Integer.parseInt(hex, 16)
}

def adjustOutgoingHue(percent) {
  def adjusted = percent
  if (percent > 31) {
    if (percent < 63.0) {
      adjusted = percent + (7 * (percent - 30) / 32)
    }
    else if (percent < 73.0) {
      adjusted = 69 + (5 * (percent - 62) / 10)
    }
    else {
      adjusted = percent + (2 * (100 - percent) / 28)
    }
  }
  log.info "percent: $percent, adjusted: $adjusted"
  adjusted
}

def convertPercentToHue(percent) {
  def adjusted = percent / 100 * 360
  log.info "percent: $percent, adjusted: $adjusted"
  adjusted
}
