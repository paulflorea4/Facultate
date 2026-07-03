CREATE DATABASE Brutarie
GO

USE Brutarie
GO

CREATE TABLE Brutarii(
id INT PRIMARY KEY IDENTITY,
denumire VARCHAR(30),
descriere VARCHAR(100),
capacitate INT,
program_coacere VARCHAR (30)
);

CREATE TABLE Faina(
id INT PRIMARY KEY IDENTITY,
denumire VARCHAR(30),
cantitate_disponibila INT,
descriere VARCHAR(100),
);

CREATE TABLE Copii(
id INT PRIMARY KEY IDENTITY,
nume VARCHAR(30),
prenume VARCHAR(30),
gen CHAR(1),
data_nasterii DATE
);

CREATE TABLE Paine(
id INT PRIMARY KEY IDENTITY,
denumire VARCHAR(30),
gramaj INT,
pret FLOAT,
id_brutarie INT FOREIGN KEY REFERENCES Brutarii(id),
id_copil INT FOREIGN KEY REFERENCES Copii(id)
);

CREATE TABLE PaineFaina(
id_paine INT FOREIGN KEY REFERENCES Paine(id),
id_faina INT FOREIGN KEY REFERENCES Faina(id),
cantitate_utilizata INT,
are_gluten BIT,
data_expirare DATE,
PRIMARY KEY (id_paine,id_faina)
);

INSERT INTO Brutarii(denumire,descriere,capacitate,program_coacere) VALUES
('nume1','desc1',10,'prog1'),('nume2','desc2',10,'prog2'),('nume3','desc3',10,'prog3')

INSERT INTO Faina(denumire,cantitate_disponibila,descriere) VALUES
('faina1',100,'desc1'),('faina2',100,'desc2'),('faina3',100,'desc3')

INSERT INTO Copii(nume,prenume,gen,data_nasterii) VALUES
('nume1','prenume1','m',GETDATE()),('nume2','prenume2','f',GETDATE()),('nume3','prenume3','f',GETDATE())

INSERT INTO Paine(denumire,gramaj,pret,id_brutarie,id_copil) VALUES
('paine1',700,5,2,3),('paine2',700,5,3,1),('paine3',700,5,1,2),('paine4',700,5,1,3)

INSERT INTO PaineFaina(id_paine,id_faina,cantitate_utilizata,are_gluten,data_expirare) VALUES
(1,2,20,1,'2026-1-17'),(1,3,40,1,'2026-1-23'),(2,1,50,1,'2026-1-23'),(2,3,20,1,'2026-1-18'),(3,3,20,1,'2026-1-19')
INSERT INTO PaineFaina VALUES (4,3,30,2,'2026-1-17')
INSERT INTO PaineFaina VALUES (2,2,20,1,'2026-1-17')

GO
CREATE OR ALTER PROCEDURE adauga_faina_paine(
    @id_paine INT,
    @id_faina INT,
    @cantitate INT,
    @are_gluten BIT,
    @data_expirare DATE
)
AS
BEGIN
    IF NOT EXISTS (SELECT * FROM Paine WHERE id = @id_paine)
    BEGIN
        PRINT 'Nu exista paine cu id ' + CAST(@id_paine AS VARCHAR(10))
        RETURN
    END

    IF NOT EXISTS (SELECT * FROM Faina WHERE id = @id_faina)
    BEGIN
        PRINT 'Nu exista faina cu id ' + CAST(@id_faina AS VARCHAR(10))
        RETURN
    END

    IF (SELECT cantitate_disponibila FROM Faina WHERE id = @id_faina) < @cantitate
    BEGIN
        PRINT 'Cantitate insuficienta de faina'
        RETURN
    END

    IF EXISTS (
        SELECT 1 FROM PaineFaina 
        WHERE id_paine = @id_paine AND id_faina = @id_faina
    )
    BEGIN
        UPDATE PaineFaina
        SET cantitate_utilizata = cantitate_utilizata + @cantitate,
            are_gluten = @are_gluten,
            data_expirare = @data_expirare
        WHERE id_paine = @id_paine AND id_faina = @id_faina;
    END
    ELSE
    BEGIN
        INSERT INTO PaineFaina(id_paine,id_faina,cantitate_utilizata,are_gluten,data_expirare)
        VALUES (@id_paine, @id_faina, @cantitate,@are_gluten,@data_expirare);
    END
    UPDATE Faina
        SET cantitate_disponibila = cantitate_disponibila - @cantitate
        WHERE id = @id_faina;
END;

EXEC adauga_faina_paine 1,2,30,0,'2026-1-19'

EXEC adauga_faina_paine 2,1,20,1,'2026-1-17'

EXEC adauga_faina_paine 4,1,20,1,'2026-1-17'

SELECT * FROM PaineFaina
SELECT * FROM Faina

GO
CREATE OR ALTER FUNCTION dbo.PainiCuAceeasiExpirareFainiDiferite()
RETURNS TABLE
AS
RETURN
(
    SELECT DISTINCT p.denumire
    FROM Paine p
    JOIN PaineFaina pf1 ON p.id = pf1.id_paine
    JOIN Faina f1 ON pf1.id_faina = f1.id
    JOIN PaineFaina pf2 ON p.id = pf2.id_paine
    JOIN Faina f2 ON pf2.id_faina = f2.id
    WHERE pf1.data_expirare = pf2.data_expirare
        AND f1.denumire <> f2.denumire
);
GO

SELECT * FROM dbo.PainiCuAceeasiExpirareFainiDiferite()


