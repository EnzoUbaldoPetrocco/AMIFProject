//librerie
const net = require('net');
const fs = require("fs");
const mime = require("mime");

//prima di occuparci della parte relativa al server propriamente detto
//voglio occuparmi di tutto quello che riguarda i dati degli utenti, scrittura ecc
//ossia creare delle classi che mi servano poi per gestire i dati
//---->OOD

//ad ogni utente sarà associato un username e una password per poterne verificare l'identità e risalire ai suoi dati
//ogni utente dovrà essere salvato in una lista o albero.
//ancora da decidere il modo migliore per salvare ciascun user per poter arrivare presto all'utente senza modificare troppo la complessità
//nella gestione del DB
//ora studio un attimo come implementare un albero B+





