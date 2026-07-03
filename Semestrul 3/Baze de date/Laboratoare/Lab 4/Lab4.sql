USE nba;
GO

IF OBJECT_ID('dbo.RemovedRelationships','U') IS NULL
BEGIN
    CREATE TABLE dbo.RemovedRelationships (
        RemovedRelID INT IDENTITY PRIMARY KEY,
        LeftTableName NVARCHAR(128) NOT NULL,
        IdLeft BIGINT NULL,
        RightTableName NVARCHAR(128) NOT NULL,
        IdRight BIGINT NULL,
        Description NVARCHAR(400) NULL,
        ChangeDate DATETIME NOT NULL DEFAULT(GETDATE())
    );
END
GO

IF OBJECT_ID('proc_SeasonRuleUpdate_1N_to_N1','P') IS NOT NULL
    DROP PROCEDURE proc_SeasonRuleUpdate_1N_to_N1;
GO

CREATE PROCEDURE proc_SeasonRuleUpdate_1N_to_N1
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRAN;

    BEGIN TRY

        -- VALIDATION
        IF OBJECT_ID('Season','U') IS NULL THROW 50000, 'Table Season missing', 1;
        IF OBJECT_ID('RuleUpdate','U') IS NULL THROW 50000, 'Table RuleUpdate missing', 1;
        IF OBJECT_ID('RemovedRelationships','U') IS NULL THROW 50000, 'Table RemovedRelationships missing', 1;

        -- DROP FK
        IF EXISTS (SELECT 1 FROM sys.foreign_keys WHERE name = 'fk_ruleupdate_season')
            ALTER TABLE RuleUpdate DROP CONSTRAINT fk_ruleupdate_season;

        -- ADD update_id
        IF COL_LENGTH('Season','update_id') IS NULL
            ALTER TABLE Season ADD update_id INT;

        -- UPDATE Season.update_id
        DECLARE @sql1 NVARCHAR(MAX) = N'
            UPDATE Season
            SET update_id = (
                SELECT MAX(R.update_id)
                FROM RuleUpdate R
                WHERE R.season_id = Season.season_id
            );
        ';
        EXEC(@sql1);

        -- LOG REMOVED LINKS
        INSERT INTO RemovedRelationships
            (LeftTableName, IdLeft, RightTableName, IdRight, Description)
        SELECT 
            'Season', r.season_id, 'RuleUpdate', r.update_id,
            'Season–RuleUpdate 1:N -> N:1 (removed non-max)'
        FROM RuleUpdate r
        WHERE r.update_id NOT IN (
            SELECT MAX(R.update_id) FROM RuleUpdate R WHERE R.season_id = r.season_id
        );

        -- DROP season_id
        IF COL_LENGTH('RuleUpdate','season_id') IS NOT NULL
            ALTER TABLE RuleUpdate DROP COLUMN season_id;

        -- NEW FK
        ALTER TABLE Season
            ADD CONSTRAINT FK_Season_update_id FOREIGN KEY (update_id) REFERENCES RuleUpdate(update_id);

        COMMIT TRAN;
        PRINT 'Season - RuleUpdate changed from 1:N to N:1';
    END TRY
    BEGIN CATCH
        ROLLBACK TRAN;
        DECLARE @msg NVARCHAR(4000)=ERROR_MESSAGE();
        RAISERROR(@msg,16,1);
    END CATCH
END
GO


IF OBJECT_ID('proc_PlayerContract_1N_to_MN','P') IS NOT NULL
    DROP PROCEDURE proc_PlayerContract_1N_to_MN;
GO

CREATE PROCEDURE proc_PlayerContract_1N_to_MN
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRAN;

    BEGIN TRY

        -- VALIDATION
        IF OBJECT_ID('Player','U') IS NULL THROW 50000, 'Player missing',1;
        IF OBJECT_ID('Contract','U') IS NULL THROW 50000, 'Contract missing',1;
        IF OBJECT_ID('RemovedRelationships','U') IS NULL THROW 50000, 'RemovedRelationships missing',1;

        -- CREATE PlayerContract
        IF OBJECT_ID('PlayerContract','U') IS NULL
        BEGIN
            CREATE TABLE PlayerContract(
                player_id INT NOT NULL,
                contract_id INT NOT NULL,
                CONSTRAINT pk_player_contract PRIMARY KEY (player_id,contract_id),
                CONSTRAINT fk_playercontract_player  FOREIGN KEY (player_id) REFERENCES Player(player_id),
                CONSTRAINT fk_playercontract_contract FOREIGN KEY (contract_id) REFERENCES Contract(contract_id)
            );
        END

        -- INSERT M:N 
        INSERT INTO PlayerContract
        SELECT p.player_id, c.contract_id
        FROM Player p
        INNER JOIN Contract c ON p.player_id = c.player_id;

        -- DROP FK
        IF EXISTS (SELECT 1 FROM sys.foreign_keys WHERE name='fk_contract_player')
            ALTER TABLE Contract DROP CONSTRAINT fk_contract_player;

        -- DROP COLUMN
        IF COL_LENGTH('Contract','player_id') IS NOT NULL
        BEGIN
            DECLARE @sql2 NVARCHAR(MAX)='ALTER TABLE Contract DROP COLUMN player_id;';
            EXEC(@sql2);
        END

        COMMIT TRAN;
        PRINT 'Player - Contract changed from 1:N to M:N';

    END TRY
    BEGIN CATCH
        ROLLBACK TRAN;
        DECLARE @msg NVARCHAR(4000)=ERROR_MESSAGE();
        RAISERROR(@msg,16,1);
    END CATCH
END
GO


IF OBJECT_ID('proc_GameReferee_MN_to_1N','P') IS NOT NULL
    DROP PROCEDURE proc_GameReferee_MN_to_1N;
GO

CREATE PROCEDURE proc_GameReferee_MN_to_1N
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRAN;

    BEGIN TRY

        -- VALIDATION
        IF OBJECT_ID('Game','U') IS NULL THROW 50000,'Game missing',1;
        IF OBJECT_ID('GameReferee','U') IS NULL THROW 50000,'GameReferee missing',1;
        IF OBJECT_ID('Referee','U') IS NULL THROW 50000,'Referee missing',1;
        IF OBJECT_ID('RemovedRelationships','U') IS NULL THROW 50000,'RemovedRelationships missing',1;

        -- ADD ref_id
        IF COL_LENGTH('Game','ref_id') IS NULL
            ALTER TABLE Game ADD ref_id INT;

        -- ADD FK
        ALTER TABLE Game
            ADD CONSTRAINT fk_game_referee FOREIGN KEY (ref_id) REFERENCES Referee(ref_id);

        -- UPDATE ref_id
        DECLARE @sql3 NVARCHAR(MAX)=N'
            UPDATE Game
            SET ref_id = (SELECT MAX(ref_id) FROM GameReferee WHERE game_id=Game.game_id);
        ';
        EXEC(@sql3);

        -- LOG Removed relationships
        INSERT INTO RemovedRelationships
            (LeftTableName,IdLeft,RightTableName,IdRight,Description)
        SELECT 'Game',gr.game_id,'Referee',gr.ref_id,
               'Game–Referee M:N -> 1:N (removed non-max)'
        FROM GameReferee gr
        WHERE gr.ref_id NOT IN (
            SELECT MAX(ref_id) FROM GameReferee WHERE game_id=gr.game_id
        );

        -- DROP FKs
        IF EXISTS (SELECT 1 FROM sys.foreign_keys WHERE name='fk_gameref_game')
            ALTER TABLE GameReferee DROP CONSTRAINT fk_gameref_game;
        IF EXISTS (SELECT 1 FROM sys.foreign_keys WHERE name='fk_gameref_referee')
            ALTER TABLE GameReferee DROP CONSTRAINT fk_gameref_referee;

        -- DROP TABLE
        DROP TABLE GameReferee;

        COMMIT TRAN;
        PRINT 'Game - Referee changed from M:N to 1:N';

    END TRY
    BEGIN CATCH
        ROLLBACK TRAN;
        DECLARE @msg NVARCHAR(4000)=ERROR_MESSAGE();
        RAISERROR(@msg,16,1);
    END CATCH
END
GO


IF OBJECT_ID('proc_TeamCoach_1N_to_11','P') IS NOT NULL
    DROP PROCEDURE proc_TeamCoach_1N_to_11;
GO

CREATE PROCEDURE proc_TeamCoach_1N_to_11
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRAN;

    BEGIN TRY

        -- VALIDATION
        IF OBJECT_ID('Team','U') IS NULL THROW 50000,'Team missing',1;
        IF OBJECT_ID('Coach','U') IS NULL THROW 50000,'Coach missing',1;
        IF OBJECT_ID('RemovedRelationships','U') IS NULL THROW 50000,'RemovedRelationships missing',1;

        -- ADD coach_id
        IF COL_LENGTH('Team','coach_id') IS NULL
            ALTER TABLE Team ADD coach_id INT;

        -- ADD FK
        ALTER TABLE Team
            ADD CONSTRAINT fk_team_coach FOREIGN KEY (coach_id) REFERENCES Coach(coach_id);

        -- UPDATE coach_id (dynamic)
        DECLARE @sql4 NVARCHAR(MAX)=N'
            UPDATE Team
            SET coach_id = (SELECT MAX(coach_id) FROM Coach C WHERE C.team_id=Team.team_id);
        ';
        EXEC(@sql4);

        -- LOG removed coach rows
        INSERT INTO RemovedRelationships
            (LeftTableName,IdLeft,RightTableName,IdRight,Description)
        SELECT 'Team',c.team_id,'Coach',c.coach_id,
               'Team–Coach 1:N -> 1:1 (removed non-max coach)'
        FROM Coach c
        WHERE c.coach_id NOT IN (
            SELECT MAX(c2.coach_id) FROM Coach c2 WHERE c.team_id=c2.team_id
        );

        -- ADD unique constraint
        ALTER TABLE Team
            ADD CONSTRAINT fk_coach_unique UNIQUE (coach_id);

        -- DROP old FK
        IF EXISTS (SELECT 1 FROM sys.foreign_keys WHERE name='fk_coach_team')
            ALTER TABLE Coach DROP CONSTRAINT fk_coach_team;

        -- DROP team_id (dynamic)
        IF COL_LENGTH('Coach','team_id') IS NOT NULL
        BEGIN
            DECLARE @sql5 NVARCHAR(MAX)='ALTER TABLE Coach DROP COLUMN team_id;';
            EXEC(@sql5);
        END

        COMMIT TRAN;
        PRINT 'Team - Coach changed from 1:N to 1:1';
    END TRY
    BEGIN CATCH
        ROLLBACK TRAN;
        DECLARE @msg NVARCHAR(4000)=ERROR_MESSAGE();
        RAISERROR(@msg,16,1);
    END CATCH
END
GO


EXEC proc_SeasonRuleUpdate_1N_to_N1
SELECT * FROM Season
SELECT * FROM RuleUpdate

EXEC proc_PlayerContract_1N_to_MN
SELECT * FROM Player
SELECT * FROM PlayerContract
SELECT * FROM Contract

EXEC proc_GameReferee_MN_to_1N
SELECT * FROM Referee;
SELECT * FROM GameReferee;
SELECT * FROM Game;

EXEC proc_TeamCoach_1N_to_11
SELECT * FROM Team
SELECT * FROM Coach

SELECT * FROM RemovedRelationships



