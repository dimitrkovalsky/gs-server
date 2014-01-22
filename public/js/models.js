/**
 * User: Dimitr
 * Date: 19.01.14
 * Time: 12:03
 */
(function () {

    window.GS = {
        Models: {},
        Views: {},
        Handlers: {},
        Collections: {}
    };

    window.template = function (id) {
        return _.template($('#' + id).html());
    };

    GS.Models.Status = Backbone.Model.extend({
        status: false,
        message: "",
        setSuccess: function () {
            this.set("status", true);
        },
        setFail: function () {
            this.set("status", false);
        },

        isSuccess: function () {
            return this.get('status');
        }
    });

    GS.Views.Status = Backbone.View.extend({
        initialize: function () {
            _.bindAll(this, "render"); // set context on current object
            this.model.on("change", this.render);
        },
        tagName: "div",

        render: function () {
            var clazz = this.model.get('status') ? "status-completed" : "status-fail";
            var msg = this.model.get('status') ? this.model.get('message') : "not " + this.model.get('message');
            this.$el.attr('class', clazz);
            this.$el.text(msg);
            return this;
        }
    });

    GS.Collections.Statuses = Backbone.Collection.extend({
        model: GS.Models.Status
    });

    GS.Views.Statuses = Backbone.View.extend({
        tagName: "div",
        render: function () {
            this.collection.each(this.addOne, this);
            return this;
        },

        addOne: function (element) {
            var statusView = new GS.Views.Status({model: element});
            this.$el.append(statusView.render().el);
        }
    });

    GS.Models.HistoryMessage = Backbone.Model.extend({
        request: true,
        message: "",
        info: false,
        error: false,

        isRequest: function () {
            return this.get('request');
        },

        isResponse: function () {
            return !this.isRequest;
        },

        isInfo: function () {
            return this.get('info');
        },

        isError: function () {
            return this.get('error');
        }

    });

    GS.Views.HistoryMessage = Backbone.View.extend({
        initialize: function () {
            _.bindAll(this, "render");
        },

        requestTemplate: _.template("<strong style='color: cadetblue'> Request </strong> =>> <%= message %>"),
        responseTemplate: _.template("<strong style='color: blueviolet'> Response </strong><<= <%= message %>"),
        infoTemplate: _.template("<strong style='color: #46a546'> INFO : </strong> <%= message %>"),
        errorTemplate: _.template("<strong style='color: #ff0000'> ERROR : </strong> <%= message %>"),

        tagName: "div",

        getTemplate: function () {
            var template;
            if (this.model.isInfo())
                template = this.infoTemplate;
            else if (this.model.isError())
                template = this.errorTemplate;
            else
                template = this.model.isRequest() ? this.requestTemplate : this.responseTemplate;
            return template;
        },

        render: function () {
            var template = this.getTemplate();
            var templateResult = template(this.model.toJSON());
            this.$el.html(templateResult);
            return this;
        }
    });

    GS.Collections.History = Backbone.Collection.extend({
        model: GS.Models.HistoryMessage,

        initialize: function () {
            _.bindAll(this, "addRequest", "addResponse", "addInfo", "addError");
        },

        addRequest: function (msg) {
            var request = new GS.Models.HistoryMessage({request: true, message: msg});
            this.add(request);
        },

        addResponse: function (msg) {
            var resp = new GS.Models.HistoryMessage({request: false, message: msg});
            this.add(resp)
        },

        addInfo: function (msg) {
            var resp = new GS.Models.HistoryMessage({request: false, message: msg, info: true});
            this.add(resp)
        },

        addError: function (msg) {
            var resp = new GS.Models.HistoryMessage({request: false, message: msg, error: true});
            this.add(resp)
        }
    });

    GS.Views.History = Backbone.View.extend({
        tagName: "div",

        initialize: function () {
            _.bindAll(this, "render", "addOne", "addedElement"); // set context on current object
            this.collection.on("add", this.addedElement);
            this.render();
        },

        addedElement: function (element) {
            this.addOne(element);
        },

        render: function () {
            this.collection.each(this.addOne, this);
            return this;
        },

        addOne: function (element) {
            var messageView = new GS.Views.HistoryMessage({model: element});
            this.$el.append(messageView.render().el);
        }
    });

    GS.Models.GenericRequest = Backbone.Model.extend({
        setSecurityToken: function (secToken) {
            this.set('securityToken ', secToken);
        },

        setRequestType: function (requestType) {
            this.set('requestType', requestType);
        },

        setRequestData: function (requestData) {
            this.set("requestData", requestData);
        }
    });

    GS.Models.Communicator = Backbone.Model.extend({
        socket: null,
        getSocket: function () {
            return this.get('socket');
        },
        initialize: function () {
            this.set('socket', new WebSocket("ws://localhost:8000/backend"));
            this.getSocket().onopen = function () {
                window.history.addInfo("Connection established");
                window.connection_status.setSuccess();
            };

            this.getSocket().onclose = function (event) {
                window.connection_status.setFail();
                window.auth_status.setFail();
                if (event.wasClean) {
                    window.history.addError('Connection close ok');
                } else {
                    window.history.addError('Connection fail');
                }
                window.history.addError('Code: ' + event.code + ' reason: ' + event.reason);
            };

            this.getSocket().onerror = function (error) {
                window.history.addError("Error " + error.message);
                window.connection_status.setFail();
                window.auth_status.setFail();
            };

            handleMessage:function (event) {
                var incomingMessage = event.data;
                window.history.addResponse(incomingMessage);
                window.interpreter.handleServerResponse(new GS.Models.ServerResponse(fromJson(incomingMessage)));
            }

            this.getSocket().onmessage = handleMessage();
        },

        toJson: function (object) {
            return JSON.stringify(object);
        },

        fromJson: function (encoded) {
            return JSON.parse(encoded);
        },

        send: function (request) {
            if (!window.connection_status.isSuccess()) {
                window.history.addError("Connection doesn't established");
                return;
            }
            if (window.securityToken) {
                request.setSecurityToken(window.securityToken)
            }
            var jsonRequest = this.toJson(request);
            window.history.addRequest(jsonRequest);
            this.getSocket().send(jsonRequest);
        }
    });

    GS.Models.ServerResponse = Backbone.Model.extend({
//        initialize: function () {
//            this.responseStatus = this.get('responseStatus');
//            this.requestType = this.get('requestType');
//            this.responseType = this.get('responseType');
//            this.responseData = this.get('responseData');
//            this.processingTime = this.get('pst');
//        },

        getResponseStatus: function () {
            return  this.get('responseStatus');
        },

        getRequestType: function () {
            return this.get('requestType');
        },
        getResponseType: function () {
            return this.get('responseType');
        },
        getResponseData: function () {
            return this.get('responseData');
        },
        getProcessingTime: function () {
            return this.processingTime;
        }
    })
}());