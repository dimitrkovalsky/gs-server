
var Person = Backbone.Model.extend({
	defaults:{
		name : "Dima",
		age : 22,
		job : "Developer"
	},

	

	work: function(){
		return this.get("name") + " is working";
	}

});

var PersonView = Backbone.View.extend({

	template: _.template("<strong><%=name%></strong> ( <%= age %> )"),

	initialize: function(){
		console.log("Initialized : " + this.model);
	},

	tagName: "li",

	render:function(){
		this.$el.html(this.template(this.model.toJSON()));
	}

});

var person = new Person;
var personView = new PersonView({model:person});