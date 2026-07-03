CREATE DATABASE nba;

USE nba;

CREATE TABLE Team( 
    team_id INT PRIMARY KEY IDENTITY, 
    team_name NVARCHAR(100) NOT NULL, 
    city NVARCHAR(100) NOT NULL
);

CREATE TABLE Player( 
    player_id INT PRIMARY KEY IDENTITY,
    player_name NVARCHAR(100) NOT NULL, 
    team_id INT NOT NULL,
    CONSTRAINT fk_player_team 
        FOREIGN KEY (team_id) REFERENCES Team(team_id)
);

CREATE TABLE Coach( 
    coach_id INT PRIMARY KEY IDENTITY,
    coach_name NVARCHAR(100) NOT NULL,
    team_id INT NOT NULL,
    CONSTRAINT fk_coach_team 
        FOREIGN KEY (team_id) REFERENCES Team(team_id)
);

CREATE TABLE Sponsor(
    sponsor_id INT PRIMARY KEY IDENTITY,
    sponsor_name NVARCHAR(100) NOT NULL
);

CREATE TABLE Season(
    season_id INT PRIMARY KEY IDENTITY,
    year_start DATE NOT NULL,
    year_end DATE NOT NULL
);

CREATE TABLE Contract(
    contract_id INT PRIMARY KEY IDENTITY,
    player_id INT NOT NULL,
    salary INT DEFAULT 0,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    CONSTRAINT fk_contract_player 
        FOREIGN KEY (player_id) REFERENCES Player(player_id)
);

CREATE TABLE Game(
    game_id INT PRIMARY KEY IDENTITY,
    game_date DATE NOT NULL,
    home_team_id INT NOT NULL,
    away_team_id INT NOT NULL,
    home_score INT DEFAULT 0,
    away_score INT DEFAULT 0,
    CONSTRAINT fk_game_home_team 
        FOREIGN KEY (home_team_id) REFERENCES Team(team_id),
    CONSTRAINT fk_game_away_team 
        FOREIGN KEY (away_team_id) REFERENCES Team(team_id)
);

CREATE TABLE TeamSeason(
    team_id INT NOT NULL,
    season_id INT NOT NULL,
    wins INT DEFAULT 0,
    losses INT DEFAULT 0,
    CONSTRAINT pk_team_season PRIMARY KEY (team_id,season_id),
    CONSTRAINT fk_teamseason_team 
        FOREIGN KEY (team_id) REFERENCES Team(team_id),
    CONSTRAINT fk_teamseason_season 
        FOREIGN KEY (season_id) REFERENCES Season(season_id),
    CONSTRAINT check_team_season_games CHECK (wins + losses <= 82)
);

CREATE TABLE PlayerSeason(
    player_id INT NOT NULL,
    season_id INT NOT NULL,
    PPG FLOAT DEFAULT 0,
    APG FLOAT DEFAULT 0,
    RPG FLOAT DEFAULT 0,
    BPG FLOAT DEFAULT 0,
    SPG FLOAT DEFAULT 0,
    CONSTRAINT pk_player_season PRIMARY KEY (player_id,season_id),
    CONSTRAINT fk_playerseason_player 
        FOREIGN KEY (player_id) REFERENCES Player(player_id),
    CONSTRAINT fk_playerseason_season 
        FOREIGN KEY (season_id) REFERENCES Season(season_id)
);

CREATE TABLE TeamSponsor(
    team_id INT NOT NULL,
    sponsor_id INT NOT NULL,
    CONSTRAINT pk_team_sponsor PRIMARY KEY(team_id,sponsor_id),
    CONSTRAINT fk_teamsponsor_team 
        FOREIGN KEY (team_id) REFERENCES Team(team_id),
    CONSTRAINT fk_teamsponsor_sponsor 
        FOREIGN KEY (sponsor_id) REFERENCES Sponsor(sponsor_id)
);

CREATE TABLE Referee(
    ref_id INT PRIMARY KEY IDENTITY,
    ref_name NVARCHAR(100) NOT NULL
);

CREATE TABLE GameReferee(
    game_id INT NOT NULL,
    ref_id INT NOT NULL,
    CONSTRAINT pk_game_referee PRIMARY KEY(game_id,ref_id),
    CONSTRAINT fk_gameref_game 
        FOREIGN KEY (game_id) REFERENCES Game(game_id),
    CONSTRAINT fk_gameref_referee 
        FOREIGN KEY (ref_id) REFERENCES Referee(ref_id)
);

CREATE TABLE RuleUpdate ( 
    update_id INT PRIMARY KEY IDENTITY, 
    season_id INT NOT NULL,
    description NVARCHAR(300), 
    update_date DATE,
    CONSTRAINT fk_ruleupdate_season
        FOREIGN KEY (season_id) REFERENCES Season(season_id)
);

DROP TABLE GameReferee;
DROP TABLE RuleUpdate;
DROP TABLE PlayerSeason;
DROP TABLE TeamSeason;
DROP TABLE Contract;
DROP TABLE Game;
DROP TABLE Referee;
DROP TABLE TeamSponsor;
DROP TABLE Sponsor;
DROP TABLE Coach;
DROP TABLE Player;
DROP TABLE Season;
DROP TABLE Team;

SELECT team_name FROM Team UNION SELECT player_name FROM Player