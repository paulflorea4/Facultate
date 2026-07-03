CREATE DATABASE DB_NBA;

USE nba;
SELECT * FROM Team t
JOIN Player p ON p.team_id = t.team_id

CREATE INDEX idx1 ON Player(team_id)

CREATE TABLE Team( 
team_id INT PRIMARY KEY IDENTITY, 
team_name nvarchar(100) NOT NULL, 
city nvarchar(100) NOT NULL, 
);

CREATE TABLE Player( 
player_id INT PRIMARY KEY IDENTITY,
player_name nvarchar(100) NOT NULL, 
team_id INT FOREIGN KEY REFERENCES Team(team_id)
);

CREATE TABLE Coach( 
coach_id INT PRIMARY KEY IDENTITY,
coach_name nvarchar(100) NOT NULL,
team_id INT FOREIGN KEY REFERENCES Team(team_id) UNIQUE
);

CREATE TABLE Sponsor(
sponsor_id INT PRIMARY KEY IDENTITY,
sponsor_name nvarchar(100) NOT NULL
);

CREATE TABLE Season(
season_id INT PRIMARY KEY IDENTITY,
year_start DATE NOT NULL,
year_end DATE NOT NULL
);

CREATE TABLE Contract(
contract_id INT PRIMARY KEY IDENTITY,
player_id INT FOREIGN KEY REFERENCES Player(player_id),
salary INT DEFAULT 0,
start_date DATE NOT NULL,
end_date DATE NOT NULL
);

CREATE TABLE Game(
game_id INT PRIMARY KEY IDENTITY,
game_date DATE NOT NULL,
home_team_id INT FOREIGN KEY REFERENCES Team(team_id),
away_team_id INT FOREIGN KEY REFERENCES Team(team_id),
home_score INT DEFAULT 0,
away_score INT DEFAULT 0
);

CREATE TABLE TeamSeason(
team_id INT FOREIGN KEY REFERENCES Team(team_id),
season_id INT FOREIGN KEY REFERENCES Season(season_id),
wins INT DEFAULT 0,
losses INT DEFAULT 0,
CONSTRAINT pk_team_season PRIMARY KEY (team_id,season_id),
CONSTRAINT check_sum_82 CHECK (wins + losses <= 82)
);

CREATE TABLE PlayerSeason(
player_id INT FOREIGN KEY REFERENCES Player(player_id),
season_id INT FOREIGN KEY REFERENCES Season(season_id),
PPG FLOAT DEFAULT 0,
APG FLOAT DEFAULT 0,
RPG FLOAT DEFAULT 0,
BPG FLOAT DEFAULT 0,
SPG FLOAT DEFAULT 0,
CONSTRAINT pk_player_season PRIMARY KEY (player_id,season_id)
);

CREATE TABLE TeamSponsor(
team_id INT FOREIGN KEY REFERENCES Team(team_id),
sponsor_id INT FOREIGN KEY REFERENCES Sponsor(sponsor_id),
CONSTRAINT pk_team_sponsor PRIMARY KEY(team_id,sponsor_id)
);