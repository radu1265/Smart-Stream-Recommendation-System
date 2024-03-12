# Smart-Stream-Recommendation-System

PROIECT POO

-Despre aplicație :
Proiectul conține implementări necesare pentru realizarea unui sistem de recomandări pentru utilizatori.
Am implementat un algoritm de recomandări pentru muzica/podcasts/audiobooks (streams) pe baza datelor
cunoscute despre cei care utilizează aplicația (users) și despre creatorii de stream-uri (streamers).

-Proiectul conține  :
	ProiectPOO.java (main);
    User.java;
	Streamer.java;
	Stream.java;
    Command.java;
	Factory.java;
	Singleton.java.

-ProiectPOO.java : conține metoda main, în care se apelează metoda build(args);

-User.java : {id, name, streams};

-Stream.java : {streamType, id, streamGenre, noOfStreams, streamerId, length, dateAdded, name};

-Streamer.java : {streamerType, id, name};

-Design Pattern-uri utilizate :
	Command (Comportamental) : 
		încapsulează o cerere ca obiect, parametrizează clienții cu diferite cereri și
		suportă operații ireversibile;
	Factory (Creațional) : 
		definește o interfață pentru crearea unui obiect, dar lasă subclasele să decidă în
		privința instanțierii;
	Singleton (Creațional) :
		se asigură că o clasă are doar o instanță, în timp ce oferă acces global către aceasta
		din orice loc al aplicației;	
	Iterator (Comportamental) :
		furnizează o modalitate de acces la elementele unui obiect agregat în mod
		secvențial, fără să îi expună implementarea;
	
	Singleton este implementat ca o bază de date unde sunt stocate 3 ArrayLists. În interiorul
acestei clase este implementat si Iterator, pe baza căruia este facilitată parcurgerea listelor.
