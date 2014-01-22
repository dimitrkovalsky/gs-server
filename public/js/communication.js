/**
 * User: Dimitr
 * Date: 06.01.14
 * Time: 20:13
 */

function initStatuses() {
    window.connection_status = new GS.Models.Status({message: "connected", status: false});
    window.auth_status = new GS.Models.Status({message: "authenticated", status: false});
    var statusesView = new GS.Views.Statuses({collection: new GS.Collections.Statuses([window.connection_status, window.auth_status])});
    $("#statuses").html(statusesView.render().el);
}

function initHistory() {
    window.history = new GS.Collections.History();
    $("#history").html(new GS.Views.History({collection: window.history}).render().el);
}


(function () {
    initStatuses();
    initHistory();
    window.interpreter = new GS.Handlers.Interpreter();
}());

function sendAuth() {
    if (!window.connection_status.isSuccess()) {
        window.history.addError("Connection doesn't established");
        return;
    }

    var request = new GS.Models.GenericRequest();
    request.setRequestType(RT_AUTHENTICATE);
    request.setRequestData({googleId: $('#google_id').val()});
    window.communicator.send(request);
}
