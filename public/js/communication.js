/**
 * User: Dimitr
 * Date: 06.01.14
 * Time: 20:13
 */

function RequestObject() {
    this.requestType = 0;
    this.requestData = null;
    this.securityToken = null;

    this.setSecurityToken = function (secToken) {
        this.securityToken = secToken;
    };

    this.setRequestType = function (requestType) {
        this.requestType = requestType;
    };

    this.setRequestData = function (requestData) {
        this.requestData = requestData;
    };
}

var socket = new WebSocket("ws://localhost:8000/backend");
socket.onopen = function () {
    showMessage("Connection established");
};

socket.onclose = function (event) {
    if (event.wasClean) {
        showMessage('Connection close ok');
    } else {
        showMessage('Connection fail');
    }
    showMessage('Code: ' + event.code + ' reason: ' + event.reason);
};

socket.onerror = function (error) {
    showMessage("Error " + error.message);
};



function toJson(object) {
    return JSON.stringify(object);
}

function fromJson(encoded) {
    return JSON.parse(encoded);
}

var UserSession = Backbone.Model.extend({
    defaults: {
        securityToken: null
    },

    getToken: function () {
        return this.get("securityToken")
    }
});

var GenericRequest = Backbone.Model.extend({
    defaults: {
        requestType: 0,
        requestData: null,
        securityToken: null
    },

    setSecurityToken: function (secToken) {
        this.securityToken = secToken;
    },

    setRequestType: function (requestType) {
        this.requestType = requestType;
    },

    setRequestData: function (requestData) {
        this.set("requestData", requestData);
    },

    send: function () {
        return this.get("name") + " is working";
    }

});
