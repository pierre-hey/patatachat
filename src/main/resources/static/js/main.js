'use strict';

var username = document.querySelector('#userName').innerHTML;
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');
var stompClient = null;

if (username) {
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnected, onError);
}

function fetchPreviousMessages() {
    fetch('/messages/all')
        .then(response => response.json())
        .then(messages => {
            messages.forEach(message => {
                mapMessageToHtmlElement(message);

            });
        })
        .catch(error => {
            console.error('Error fetching messages:', error);
        });
}

function onConnected() {
    // Subscribe to the Public Topic
    fetchPreviousMessages();
    stompClient.subscribe('/topic/public', onMessageReceived);
}


function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
    setTimeout(connectWebSocket, 2000); // Reconnexion après 5 secondes
}

function connectWebSocket() {
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnected, onError);
}

function sendMessage(event) {
    // Récupère le contenu du message du DOM, l'insère dans un dto pour traitement dans le back
    var messageContent = messageInput.value.trim();

    var temporaryId = generateTemporaryId();
    var currentDate = new Date().toISOString();



    // Mapping du message si le contenu et le client Stomp existent
    console.log(messageContent);
    if (messageContent && stompClient) {
        var UserMessageDto = {
            id: temporaryId,
            messageDateTime: currentDate, // Ajouter la date au message
            userName: username,
            content: messageInput.value
        };
        console.log(UserMessageDto);
        // Envoie le message vers le websocket écouté par le back
        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(UserMessageDto));
        messageInput.value = '';
    }
    event.preventDefault();
}


function onMessageReceived(payload) {
    // Parse le message reçu en JSON
    var message = JSON.parse(payload.body);

    mapMessageToHtmlElement(message)
}

function mapMessageToHtmlElement(message) {
    // Créer un élément fieldset et p pour afficher le message
    var htmlFieldSetElement = document.createElement('fieldset');
    var htmlParagraphElement = document.createElement('p');
    htmlFieldSetElement.classList.add('border', 'p-2');

    // Créer un élément legend pour afficher le nom de l'émetteur
    var htmlLegendElement = document.createElement('legend');
    htmlLegendElement.classList.add('float-none', 'w-auto', 'p-2', 'fs-6', 'fw-bold');

    // Convertir la chaîne de date en objet Date JavaScript
    var messageDate = new Date(message.messageDateTime);
    var formattedDate = messageDate.toLocaleString(); // Formater la date selon vos besoins

    // Créer le texte à insérer dans le html
    var usernameText = document.createTextNode(message.id + ' - ' + message.userName + ' - ' + formattedDate);
    var messageText = document.createTextNode(message.content);

    // Ajout du nom de l'user à son élément
    htmlLegendElement.appendChild(usernameText);
    htmlFieldSetElement.appendChild(htmlLegendElement);
    // Ajout du contenu du message dans le paragraphe
    htmlParagraphElement.appendChild(messageText);
    htmlFieldSetElement.appendChild(htmlParagraphElement);

    // Ajout de l'ensemble du fieldsSet dans la zone message du DOM
    messageArea.insertBefore(htmlFieldSetElement, messageArea.firstChild);

    // Utile si on veut que le scroll soit en bas quand on get la page
    // messageArea.scrollTop = messageArea.scrollHeight;
}


messageForm.addEventListener('submit', sendMessage, true)


function generateTemporaryId() {
    return "XX"
}
