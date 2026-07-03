USE DB_NBA;

CREATE TABLE DB_Version(
versionNo INT PRIMARY KEY
);

INSERT INTO DB_Version(versionNo) VALUES (0);

--modifica tipul lui year_start din DATE in DATETIME
GO
CREATE OR ALTER PROCEDURE v1
AS
BEGIN
ALTER TABLE Season
ALTER COLUMN year_start DATETIME
print 'S-a modificat coloana year_start din Season ( DATE -> DATETIME)'
END;
GO

--modifica inapoi tipul lui year_start din DATETIME in DATE
GO
CREATE OR ALTER PROCEDURE v1b
AS
BEGIN
ALTER TABLE Season
ALTER COLUMN year_start DATE
print 'S-a modificat inapoi coloana year-start din Season ( DATETIME -> DATE)'
END;
GO

--adauga o constrangere default pentru data meciului astfel incat daca nu este introdusa sa primeasca automat data din momentul adaugarii in tabela
GO
CREATE OR ALTER PROCEDURE v2
AS
BEGIN
ALTER TABLE Game
ADD CONSTRAINT DF_GameDate DEFAULT GETDATE() FOR game_date
print 'S-a adaugat constrangere default pentru game_date din Game ( data setata implicit la data introducerii unui meci ) '
END;
GO

--sterge constrangerea default adaugata
GO
CREATE OR ALTER PROCEDURE v2b
AS
BEGIN
ALTER TABLE Game
DROP CONSTRAINT DF_GameDate
print 'S-a sters constrangerea default pentru game_date din Game'
END;
GO

--creeaza o noua tabela Arena
GO
CREATE OR ALTER PROCEDURE v3
AS
BEGIN
CREATE TABLE Arena(
arena_id INT PRIMARY KEY IDENTITY,
arena_name NVARCHAR(100) NOT NULL,
capacity INT DEFAULT 0
);
print 'S-a adaugat tabela noua Arena'
END;
GO

--sterge tabela Arena
GO
CREATE OR ALTER PROCEDURE v3b
AS
BEGIN
DROP TABLE Arena
print 'S-a sters tabela Arena'
END;
GO

SELECT * FROM Arena

--adauga un camp nou arena_id in tabela Game
GO
CREATE OR ALTER PROCEDURE v4
AS
BEGIN
ALTER TABLE Game
ADD arena_id INT
print 'S-a adaugat campul arena_id in tabela Game'
END;
GO

--sterge campul arena_id din tabela Game
GO
CREATE OR ALTER PROCEDURE v4b
AS
BEGIN
ALTER TABLE Game
DROP COLUMN arena_id
print 'S-a sters campul arena_id din tabela Game'
END;
GO

SELECT * FROM Game

--creeaza o constrangere de cheie straina pentru campul arena_id din Game si Arena
GO
CREATE OR ALTER PROCEDURE v5
AS
BEGIN
ALTER TABLE Game
ADD CONSTRAINT FK_Game_Arena FOREIGN KEY (arena_id) REFERENCES Arena(arena_id)
print 'S-a adaugat o constrangere de cheie straina pentru arena_id din Game si Arena'
END;
GO

--sterge constrangerea de cheie straina
GO
CREATE OR ALTER PROCEDURE v5b
AS
BEGIN
ALTER TABLE Game
DROP CONSTRAINT FK_Game_Arena
print 'S-a sters constrangerea de cheie straina pentru arena_id din Game si Arena'
END;
GO

--schimba versiunea bazei de date dupa valoarea parametrului de intrare
GO
CREATE OR ALTER PROCEDURE ChangeVersion 
@targetVersion INT
AS
BEGIN
	DECLARE @curentVersion INT;
	DECLARE @numeProcedura VARCHAR(50);

	IF @targetVersion<0 OR @targetVersion>5
	BEGIN
		PRINT 'Versiunea dorita este invalida!';
		RETURN;
	END;

	SELECT @curentVersion = MAX(versionNo) FROM DB_Version;

	IF @targetVersion = @curentVersion
	BEGIN
		PRINT 'Baza de date este deja la versiunea dorita.';
		RETURN;
	END;

	IF @targetVersion > @curentVersion
	BEGIN
		WHILE @curentVersion < @targetVersion
		BEGIN
			SET @curentVersion+=1;
			SET @numeProcedura = 'v' + CAST(@curentVersion as NVARCHAR(10));
			EXEC (@numeProcedura)
		END;
	END;
	ELSE
	BEGIN
		WHILE @curentVersion > @targetVersion
		BEGIN
			SET @numeProcedura = 'v' + CAST(@curentVersion as NVARCHAR(10))+'b';
			EXEC (@numeProcedura)
			SET @curentVersion-=1;
		END;
	END;

	UPDATE DB_Version
	SET versionNo=@curentVersion
END;

EXEC ChangeVersion 1
