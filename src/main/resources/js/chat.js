var socket;
var cUser;

function login() {

	var user = {
		username : document.getElementById("username").value,
		password : document.getElementById("password").value
	};

	var request = new XMLHttpRequest();
	request.open("POST", "http://localhost:9191/auth");
	request.setRequestHeader("Content-Type", "application/json");

	request.onreadystatechange = function() {

		if (request.readyState === XMLHttpRequest.DONE) {
			switch (request.status) {
			case 200:
				cUser = user.username;
				var accessToken = JSON.parse(request.responseText);
				openSocket(accessToken.token);
				break;

			case 403:
				cUser = null;
				document.getElementById("authentication-error").innerHTML = "Usuario/contraseña inválido.";
				break;

			default:
				document.getElementById("authentication-error").innerHTML = "Algo salio mal, reporte al administrador.";
			}
		}
	};

	request.send(JSON.stringify(user));
}


function openSocket(accessToken) {

	if (socket) {
		socket.close();
	}

	socket = new WebSocket("ws://localhost:9191/chat?access-token="
			+ accessToken);

	socket.onopen = function(event) {
		document.getElementById("authentication").style.display = "none";
		document.getElementById("users").style.display = "block";
		document.getElementById("message").focus();
		document.getElementById("chat").style.display = "block";
	};

	socket.onmessage = function(event) {

		if (typeof event.data === "string") {

			var messages = JSON.parse(event.data);
			switch (messages.type) {

			case "welcomeUser":
				displayConnectedUserMessage(messages.payload.username);
				break;

			case "textMessage":
				displayMessage(messages.payload.username,
						messages.payload.content);
				break;

			case "connectedUser":
				displayConnectedUserMessage(messages.payload.username);
				break;

			case "disconnectedUser":
				displayDisconnectedUserMessage(messages.payload.username);
				break;

			case "availableUsers":
				cleanAvailableUsers();
				for (var i = 0; i < messages.payload.usernames.length; i++) {
					addAvailableUsers(messages.payload.usernames[i]);
				}
				break;
			}
		}
	};
}

function sendMessage() {

	var text = document.getElementById("message").value;
	document.getElementById("message").value = "";

	var payload = {
		content : text
	};

	var messages = {
		type : "sendTextMessage"
	};

	messages.payload = payload;

	socket.send(JSON.stringify(messages));
}

function displayMessage(username, text) {

	var sentByCurrentUer = cUser === username;

	var message = document.createElement("div");
	message.setAttribute("class", sentByCurrentUer === true ? "message sent"
			: "message received");
	message.dataset.sender = username;

	var sender = document.createElement("span");
	sender.setAttribute("class", "sender");
	sender.appendChild(document
			.createTextNode(sentByCurrentUer === true ? "Tú" : username));
	message.appendChild(sender);

	var content = document.createElement("span");
	content.setAttribute("class", "content");
	content.appendChild(document.createTextNode(text));
	message.appendChild(content);

	var messages = document.getElementById("messages");
	var lastMessage = messages.lastChild;
	if (lastMessage && lastMessage.dataset.sender
			&& lastMessage.dataset.sender === username) {
		message.className += " same-sender-previous-message";
	}

	messages.appendChild(message);
	messages.scrollTop = messages.scrollHeight;
}

function displayConnectedUserMessage(username) {

	var sentByCurrentUer = cUser === username;

	var message = document.createElement("div");
	message.setAttribute("class", "message event");

	var text = sentByCurrentUer === true ? "Bienvenid@ " + username : username
			+ " se ha unido al chat";
	var content = document.createElement("span");
	content.setAttribute("class", "content");
	content.appendChild(document.createTextNode(text));
	message.appendChild(content);

	var messages = document.getElementById("messages");
	messages.appendChild(message);
}

function displayDisconnectedUserMessage(username) {

	var message = document.createElement("div");
	message.setAttribute("class", "message event");

	var text = username + " ha salido del chat";
	var content = document.createElement("span");
	content.setAttribute("class", "content");
	content.appendChild(document.createTextNode(text));
	message.appendChild(content);

	var messages = document.getElementById("messages");
	messages.appendChild(message);
}

function addAvailableUsers(username) {

	var contact = document.createElement("div");
	contact.setAttribute("class", "contact");

	var status = document.createElement("div");
	status.setAttribute("class", "status");
	contact.appendChild(status);

	var content = document.createElement("span");
	content.setAttribute("class", "name");
	content.appendChild(document.createTextNode(username));
	contact.appendChild(content);

	var users = document.getElementById("users");
	users.appendChild(contact);
}

function cleanAvailableUsers() {
	var users = document.getElementById("users");
	while (users.hasChildNodes()) {
		users.removeChild(users.lastChild);
	}
}



function signIn(){
	
}
