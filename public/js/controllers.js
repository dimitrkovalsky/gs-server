/**
 * User: Dimitr
 * Date: 21.01.14
 * Time: 21:24
 */

(function () {
    GS.Handlers.ResourceHandler = Backbone.Model.extend({

    });

    GS.Handlers.Interpreter = Backbone.Model.extend({
        handleServerResponse: function (response) {
            window.history.addInfo("[Interpreter] handle : " + response.toJSON());
        }
    })
}());