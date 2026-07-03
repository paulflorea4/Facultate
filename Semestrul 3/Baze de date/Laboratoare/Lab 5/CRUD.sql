USE nba
GO

--valideaza numele unei echipe
CREATE OR ALTER FUNCTION dbo.ValidateTeamName (@name NVARCHAR(100))
RETURNS BIT
AS
BEGIN
	IF(@name IS NOT NULL AND LEN(@name) >=3) RETURN 1;
	RETURN 0;
END;
GO

--valideaza orasul
CREATE OR ALTER FUNCTION dbo.ValidateCity (@city NVARCHAR(100))
RETURNS BIT
AS
BEGIN
    IF (@city IS NOT NULL AND LEN(@city) >= 4) RETURN 1;
    RETURN 0;
END;
GO

--valideaza data de inceput si sfarsit a sezonului
CREATE OR ALTER FUNCTION dbo.ValidateSeasonDates (@start DATE, @end DATE)
RETURNS BIT
AS
BEGIN
	IF (@start IS NOT NULL AND @end IS NOT NULL AND @start < @end) RETURN 1;
	RETURN 0;
END;
GO

--valideaza numarul de meciuri jucate
CREATE OR ALTER FUNCTION dbo.ValidateGames(@wins INT,@losses INT)
RETURNS BIT
AS
BEGIN
	IF (@wins IS NULL OR @losses IS NULL) RETURN 0;
	IF (@wins<0 OR @losses<0) RETURN 0;
	IF (@wins+@losses <= 82) RETURN 1;
	RETURN 0;
END;
GO

--INSERT pentru tabela Team
CREATE OR ALTER PROCEDURE dbo.INSERT_Team
	@team_name NVARCHAR(100),
	@city NVARCHAR(100),
    @flag BIT OUTPUT,
    @new_team_id INT OUTPUT
AS
BEGIN
	SET NOCOUNT ON;
	SET @flag = 0;
	SET @new_team_id = null;

	IF dbo.ValidateTeamName(@team_name) = 1 AND dbo.ValidateCity(@city) = 1
	BEGIN
		INSERT INTO Team(team_name,city)
		VALUES(@team_name,@city);

		SET @new_team_id = SCOPE_IDENTITY();
        SET @flag = 1;

		print 'INSERT operation successful for table Team'
	END
	ELSE print 'INSERT failed (name or city INVALID)'
END;
GO

--SELECT pentru tabela Team
CREATE OR ALTER PROCEDURE dbo.SELECT_Team
AS
BEGIN
	SET NOCOUNT ON;
	SELECT * FROM Team;
END;
GO

--UPDATE pentru tabela Team
CREATE OR ALTER PROCEDURE dbo.UPDATE_Team
	@team_id INT,
	@team_name NVARCHAR(100),
	@city NVARCHAR(100),
	@flag BIT OUTPUT
AS
BEGIN
	SET NOCOUNT ON;
	SET @flag = 0;

	IF EXISTS (SELECT * FROM Team WHERE team_id = @team_id) 
		AND dbo.ValidateTeamName(@team_name) = 1 AND dbo.ValidateCity(@city) = 1
	BEGIN
		UPDATE Team
		SET team_name = @team_name, city = @city
		WHERE team_id = @team_id

		IF @@ROWCOUNT > 0 SET @flag = 1;
		PRINT 'UPDATE operation successful for table Team'
	END
	ELSE PRINT 'UPDATE failed ( Id non-existent or INVALID name or city )'
END;
GO

--DELETE pentru tabela Team
CREATE OR ALTER PROCEDURE dbo.DELETE_Team
	@team_id INT,
	@flag BIT OUTPUT
AS
BEGIN
	IF EXISTS(SELECT * FROM Team WHERE team_id = @team_id)
	BEGIN
		DELETE FROM TeamSeason WHERE team_id = @team_id;
		DELETE FROM Team WHERE team_id = @team_id

		IF @@ROWCOUNT > 0 SET @flag = 1;
		IF @flag = 1 PRINT 'DELETE operation successful for table Team'
	END
	ELSE PRINT 'DELETE failed ( Id non-existent )'
END;
GO

--INSERT pentru tabela Season
CREATE OR ALTER PROCEDURE dbo.INSERT_Season
	@year_start DATE,
	@year_end DATE,
    @flag BIT OUTPUT,
    @new_season_id INT OUTPUT
AS
BEGIN
	SET NOCOUNT ON;
	SET @flag = 0;
	SET @new_season_id = NULL;

	IF dbo.ValidateSeasonDates(@year_start,@year_end) = 1
	BEGIN
		INSERT INTO Season(year_start,year_end)
		VALUES(@year_start,@year_end);

		SET @new_season_id = SCOPE_IDENTITY();
        SET @flag = 1;
	END
	ELSE PRINT 'INSERT failed ( dates INVALID )'
END;
GO

--SELECT pentru tabela Season
CREATE OR ALTER PROCEDURE dbo.SELECT_Season
AS
BEGIN
    SET NOCOUNT ON;
    SELECT * FROM Season;
END;
GO

--UPDATE pentru tabela Season
CREATE OR ALTER PROCEDURE dbo.UPDATE_Season
    @season_id INT,
    @year_start DATE,
    @year_end DATE,
    @flag BIT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    SET @flag = 0;

    IF EXISTS (SELECT * FROM Season WHERE season_id = @season_id)
       AND dbo.ValidateSeasonDates(@year_start, @year_end) = 1
    BEGIN
        UPDATE Season
        SET year_start = @year_start,year_end = @year_end
        WHERE season_id = @season_id;

        IF @@ROWCOUNT > 0 SET @flag = 1;
		IF @flag = 1 PRINT 'UPDATE operation successful for table Season'
    END
	ELSE PRINT 'UPDATE failed ( Id non-existent or INVALID dates )'
END;
GO

--DELETE pentru tabela Season
CREATE OR ALTER PROCEDURE dbo.DELETE_Season
    @season_id INT,
    @flag BIT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    SET @flag = 0;

    IF EXISTS (SELECT * FROM Season WHERE season_id = @season_id)
    BEGIN
        DELETE FROM TeamSeason WHERE season_id = @season_id;
        DELETE FROM Season WHERE season_id = @season_id;

        IF @@ROWCOUNT > 0 SET @flag = 1;
		IF @flag = 1 PRINT 'DELETE operation successful for table Team'
    END
	ELSE PRINT 'DELETE failed ( Id non-existent )'
END;
GO

--INSERT pentru tabela TeamSeason
CREATE OR ALTER PROCEDURE dbo.INSERT_TeamSeason
    @team_id INT,
    @season_id INT,
    @wins INT,
    @losses INT,
    @flag BIT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    SET @flag = 0;

    IF EXISTS (SELECT * FROM Team WHERE team_id = @team_id)
       AND EXISTS (SELECT * FROM Season WHERE season_id = @season_id)
       AND dbo.ValidateGames(@wins, @losses) = 1
       AND NOT EXISTS (SELECT * FROM TeamSeason WHERE team_id=@team_id AND season_id=@season_id)
    BEGIN
        INSERT INTO TeamSeason(team_id, season_id, wins, losses)
        VALUES (@team_id, @season_id, @wins, @losses);

        SET @flag = 1;
		PRINT 'INSERT operation successful for table TeamSeason'
    END
END;
GO

--SELECT pentru tabela TeamSeason
CREATE OR ALTER PROCEDURE dbo.SELECT_TeamSeason
AS
BEGIN
    SET NOCOUNT ON;

    SELECT
        TS.team_id, T.team_name, T.city,
        TS.season_id, S.year_start, S.year_end,
        TS.wins, TS.losses
    FROM TeamSeason TS
    INNER JOIN Team T ON T.team_id = TS.team_id
    INNER JOIN Season S ON S.season_id = TS.season_id;
END;
GO

--UPDATE pentru tabela TeamSeason
CREATE OR ALTER PROCEDURE dbo.UPDATE_TeamSeason
    @team_id INT,
    @season_id INT,
    @wins INT,
    @losses INT,
    @flag BIT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    SET @flag = 0;

    IF EXISTS (SELECT * FROM TeamSeason WHERE team_id=@team_id AND season_id=@season_id)
       AND dbo.ValidateGames(@wins, @losses) = 1
    BEGIN
        UPDATE TeamSeason
        SET wins = @wins,losses = @losses
        WHERE team_id = @team_id AND season_id = @season_id;

        IF @@ROWCOUNT > 0 SET @flag = 1;
        IF @flag = 1 PRINT 'UPDATE operation successful for table TeamSeason'
    END
    ELSE PRINT 'UPDATE failed ( id non-existent or INVALID game stats )'
END;
GO

--DELETE pentru tabela TeamSeason
CREATE OR ALTER PROCEDURE dbo.DELETE_TeamSeason
    @team_id INT,
    @season_id INT,
    @flag BIT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    SET @flag = 0;

    DELETE FROM TeamSeason
    WHERE team_id = @team_id
      AND season_id = @season_id;

    IF @@ROWCOUNT > 0 SET @flag = 1;
    IF @flag = 1 PRINT 'DELETE operation successful for table TeamSeason'
    ELSE PRINT 'DELETE failed ( non-existent data with given team_id and season_id )'
END;
GO

--Tabela de logare a modificarilor pentru tabela Team
CREATE TABLE dbo.Logging_Team (
    log_id INT IDENTITY PRIMARY KEY,
    team_id INT,
    team_name NVARCHAR(100),
    city NVARCHAR(100),
    operation_type NVARCHAR(10) CHECK (operation_type IN ('UPDATE','DELETE')),
    execution_date DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    sLogin NVARCHAR(128) NOT NULL DEFAULT SUSER_SNAME()
);
GO

--Tabela de logare a modificarilor pentru tabela Season
CREATE TABLE dbo.Logging_Season (
    log_id INT IDENTITY PRIMARY KEY,
    season_id INT,
    year_start DATE,
    year_end DATE,
    operation_type NVARCHAR(10) CHECK (operation_type IN ('UPDATE','DELETE')),
    execution_date DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    sLogin NVARCHAR(128) NOT NULL DEFAULT SUSER_SNAME()
);
GO

--Tabela de logare a modificarilor pentru tabela TeamSeason
CREATE TABLE dbo.Logging_TeamSeason (
    log_id INT IDENTITY PRIMARY KEY,
    team_id INT,
    season_id INT,
    wins INT,
    losses INT,
    operation_type NVARCHAR(10) CHECK (operation_type IN ('UPDATE','DELETE')),
    execution_date DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    sLogin NVARCHAR(128) NOT NULL DEFAULT SUSER_SNAME()
);
GO

--Trigger pentru operatiile de UPDATE si DELETE pentru tabela Team
CREATE OR ALTER TRIGGER dbo.Team_Log_UpdateDelete
ON dbo.Team
AFTER UPDATE, DELETE
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @op NVARCHAR(10) =
        CASE
            WHEN EXISTS (SELECT * FROM inserted) AND EXISTS (SELECT * FROM deleted) THEN 'UPDATE'
            WHEN EXISTS (SELECT * FROM deleted) THEN 'DELETE'
        END;

    IF @op IS NULL RETURN;

    INSERT INTO dbo.Logging_Team(team_id, team_name, city, operation_type)
    SELECT d.team_id, d.team_name, d.city, @op
    FROM deleted d;
END;
GO

--Trigger pentru operatiile de UPDATE si DELETE pentru tabela Season
CREATE OR ALTER TRIGGER dbo.Season_Log_UpdateDelete
ON dbo.Season
AFTER UPDATE, DELETE
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @op NVARCHAR(10) =
        CASE
            WHEN EXISTS (SELECT * FROM inserted) AND EXISTS (SELECT * FROM deleted) THEN 'UPDATE'
            WHEN EXISTS (SELECT * FROM deleted) THEN 'DELETE'
        END;

    IF @op IS NULL RETURN;

    INSERT INTO dbo.Logging_Season(season_id, year_start, year_end, operation_type)
    SELECT d.season_id, d.year_start, d.year_end, @op
    FROM deleted d;
END;
GO

--Trigger pentru operatiile de UPDATE si DELETE pentru tabela TeamSeason
CREATE OR ALTER TRIGGER dbo.TeamSeason_Log_UpdateDelete
ON dbo.TeamSeason
AFTER UPDATE, DELETE
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @op NVARCHAR(10) =
        CASE
            WHEN EXISTS (SELECT * FROM inserted) AND EXISTS (SELECT * FROM deleted) THEN 'UPDATE'
            WHEN EXISTS (SELECT * FROM deleted) THEN 'DELETE'
        END;

    IF @op IS NULL RETURN;

    INSERT INTO dbo.Logging_TeamSeason(team_id, season_id, wins, losses, operation_type)
    SELECT d.team_id, d.season_id, d.wins, d.losses, @op
    FROM deleted d;
END;
GO

--View cu performata echipelor in fiecare sezon
CREATE VIEW dbo.TeamSeasonPerformance
AS
SELECT
    t.team_id,
    t.team_name,
    t.city,
    s.season_id,
    s.year_start,
    s.year_end,
    ts.wins,
    ts.losses
FROM dbo.Team t
JOIN dbo.TeamSeason ts
    ON t.team_id = ts.team_id
JOIN dbo.Season s
    ON ts.season_id = s.season_id;
GO

--View cu echipe pe orase cu rezumatul sezonului
CREATE VIEW dbo.TeamsByCitySeason
AS
SELECT
    t.city,
    t.team_name,
    s.year_start,
    s.year_end,
    ts.wins,
    ts.losses,
    (ts.wins + ts.losses) AS games_played
FROM dbo.Team t
JOIN dbo.TeamSeason ts
    ON t.team_id = ts.team_id
JOIN dbo.Season s
    ON ts.season_id = s.season_id;
GO

SELECT team_name
FROM TeamSeasonPerformance
WHERE team_name = 'Los Angeles Lakers'
  AND year_start = '2010-10-01' AND wins>50;


SELECT *
FROM TeamsByCitySeason
WHERE city = 'Boston';

IF EXISTS (SELECT * FROM sys.indexes WHERE name = 'IX_Team_TeamName' AND object_id = OBJECT_ID('dbo.Team'))
    DROP INDEX IX_Team_TeamName ON dbo.Team;
GO
CREATE NONCLUSTERED INDEX IX_Team_TeamName ON dbo.Team(team_name);
GO

IF EXISTS (SELECT * FROM sys.indexes WHERE name = 'IX_Season_YearStart' AND object_id = OBJECT_ID('dbo.Season'))
    DROP INDEX IX_Season_YearStart ON dbo.Season;
GO
CREATE NONCLUSTERED INDEX IX_Season_YearStart ON dbo.Season(year_start);
GO

IF EXISTS (SELECT * FROM sys.indexes WHERE name = 'IX_TeamSeason_Team_Season' AND object_id = OBJECT_ID('dbo.TeamSeason'))
    DROP INDEX IX_TeamSeason_Team_Season ON dbo.TeamSeason;
GO
CREATE NONCLUSTERED INDEX IX_TeamSeason_Team_Season ON dbo.TeamSeason(team_id, season_id)
INCLUDE (wins, losses);
GO

SELECT * FROM Team
SELECT * FROM Season
SELECT * FROM TeamSeason

--scenariu rulare operatii CRUD
DECLARE @ok BIT, @tid INT, @sid INT;

EXEC dbo.INSERT_Team @team_name='Test', @city='test', @flag=@ok OUTPUT, @new_team_id=@tid OUTPUT;
SELECT @ok AS InsertTeam_OK, @tid AS NewTeamId;

EXEC dbo.INSERT_Season @year_start='2019-10-01', @year_end='2020-06-20', @flag=@ok OUTPUT, @new_season_id=@sid OUTPUT;
SELECT @ok AS InsertSeason_OK, @sid AS NewSeasonId;

EXEC dbo.INSERT_TeamSeason @team_id=@tid, @season_id=@sid, @wins=50, @losses=32, @flag=@ok OUTPUT;
SELECT @ok AS InsertTeamSeason_OK;

EXEC dbo.UPDATE_TeamSeason @team_id=@tid, @season_id=@sid, @wins=30, @losses=52, @flag=@ok OUTPUT;
SELECT @ok AS UpdateTeamSeason_OK;

EXEC dbo.DELETE_TeamSeason @team_id=@tid, @season_id=@sid, @flag=@ok OUTPUT;
SELECT @ok AS DeleteTeamSeason_OK;

SELECT * FROM dbo.Logging_TeamSeason ORDER BY log_id DESC;
GO