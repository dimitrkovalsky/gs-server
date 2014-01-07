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


function toJson(object) {
    return JSON.stringify(object);
}

function fromJson(encoded) {
    return JSON.parse(encoded);
}