CREATE DATABASE ParcDistractii;

USE ParcDistractii;

CREATE TABLE Sectiune(
idS INT PRIMARY KEY IDENTITY,
nume varchar(100) not null,
descriere varchar(100) not null
);

CREATE TABLE Atractie(
idA INT PRIMARY KEY IDENTITY,
nume varchar(100) not null,
descriere varchar(100) not null,
varstaMinima INT default 0,
idS INT FOREIGN KEY REFERENCES Sectiune(idS)
);

CREATE TABLE CategorieVizitatori(
idCV INT PRIMARY KEY IDENTITY
);

CREATE TABLE Vizitator(
idV INT PRIMARY KEY IDENTITY,
nume varchar(100) not null,
email varchar(100) not null,
idCV INT FOREIGN KEY REFERENCES CategorieVizitatori(idCV)
);

CREATE TABLE VizitatorAtractie(
idV INT FOREIGN KEY REFERENCES Vizitator(idV),
idA INT FOREIGN KEY REFERENCES Atractie(idA),
nota INT DEFAULT 1,
CONSTRAINT idVA PRIMARY KEY (idV,idA),
CONSTRAINT check_nota CHECK (nota>=1 AND nota<=10)
);

ALTER TABLE CategorieVizitatori
ADD nume varchar(100) not null;

INSERT INTO Sectiune(nume,descriere) VALUES
('Sectiune 1','Descriere 1'),
('Sectiune 2','Descriere 2');

SELECT * FROM Sectiune;

INSERT INTO Atractie(nume,descriere,varstaMinima,idS) VALUES
('Atractie 1','Descriere 1',5,1),
('Atractie 2','Descriere 2',7,2),
('Atractie 3','Descriere 3',3,2),
('Atractie 4','Descriere 4',9,1),
('Atractie 5','Descriere 5',15,1);

SELECT * FROM Atractie;

INSERT INTO Sectiune(nume,descriere) VALUES
('Sectiune 3','Descriere 1'),
('Sectiune 4','Descriere 4'),
('Sectiune 5','Descriere 2'),
('Sectiune 6','Descriere 3'),
('Sectiune 7','Descriere 2');

INSERT INTO Atractie(nume,descriere,varstaMinima,idS) VALUES
('Atractie 6','Descriere 3',5,3),
('Atractie 7','Descriere 4',7,5);

INSERT INTO Atractie(nume,descriere,varstaMinima,idS) VALUES
('Atractie 8','Descriere 3',5,7),
('Atractie 9','Descriere 4',7,6);

INSERT INTO CategorieVizitatori VALUES
('categorie 1'),
('categorie 2'),
('categorie 3'),
('categorie 4'),
('categorie 5'),
('categorie 6'),
('categorie 7');

SELECT * FROM CategorieVizitatori;

INSERT INTO Vizitator(nume,email,idCV) VALUES
('nume1','email1',1),
('nume2','email2',3),
('nume3','email3',2),
('nume4','email4',2),
('nume5','email5',3),
('nume6','email6',6),
('nume7','email7',7),
('nume8','email8',3),
('nume9','email9',7),
('nume10','email10',4);

SELECT * FROM Vizitator;

INSERT INTO VizitatorAtractie(idV,idA,nota) VALUES
(1,8,4),
(2,6,7),
(3,7,8),
(10,9,9),
(6,7,1),
(3,8,2),
(4,6,6),
(7,7,8),
(4,9,5),
(2,9,10);

SELECT * FROM VizitatorAtractie;

UPDATE Sectiune SET descriere='descriereNoua'
WHERE nume='Sectiune 1';

UPDATE Atractie SET descriere='descriereNoua'
WHERE nume='Atractie 7';

UPDATE CategorieVizitatori SET nume='categorieNoua'
WHERE nume='categorie 1';

UPDATE Vizitator SET nume='liviu',idCV=4
WHERE nume='nume1';

UPDATE VizitatorAtractie SET nota=5
WHERE idV=1 AND idA=8;

DELETE FROM VizitatorAtractie WHERE nota=5;

DELETE FROM Atractie WHERE nume='Atractie 2';