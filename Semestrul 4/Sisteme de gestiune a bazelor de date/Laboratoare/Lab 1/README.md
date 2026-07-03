# NBA SGBD

Aplicație Windows Forms dezvoltată în C# (.NET) pentru gestionarea echipelor de NBA și a jucătorilor acestora. 

## Cum se configurează baza de date
1. Asigurați-vă că aveți instalat **PostgreSQL** și **pgAdmin**.
2. Deschideți pgAdmin și conectați-vă la instanța locală.
3. Rulați scripturile pentru crearea tabelelor teams și players, cât și pentru popularea acestora cu date pentru teste.

## Cum se configurează conexiunea la baza de date
Aplicația folosește `DatabaseManager` pentru a realiza conexiunea. 
1. Deschideți fișierul `DatabaseConfig.cs` din Visual Studio.
2. Inlocuiti username-ul, parola si numele bazei de date cu cele corespunzatoare dumneavoastra.
3. Asigurați-vă că string-ul de conexiune cu numele `connectionString` indică spre serverul dumneavoastră local. 

## Cum se rulează aplicația
1. Deschideți soluția (.sln) în Visual Studio.
2. Din meniul de sus, asigurați-vă că proiectul este setat pe Debug sau Release.
3. Apăsați butonul Start (sau tasta F5) pentru a compila și a lansa aplicația.
4. În formularul principal (TeamPage), faceți click pe o echipă în tabel pentru a vedea jucatorii asociați în tabelul parinte. Folosiți butoanele Add/Update/Delete pentru operațiunile CRUD si Apply pentru aplicarea filtrelor.