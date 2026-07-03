USE nba

INSERT INTO Team (team_name, city) VALUES
('Los Angeles Lakers', 'Los Angeles'),
('Golden State Warriors', 'San Francisco'),
('Boston Celtics', 'Boston'),
('Miami Heat', 'Miami'),
('Chicago Bulls', 'Chicago'),
('Dallas Mavericks', 'Dallas'),
('Brooklyn Nets', 'Brooklyn'),
('Phoenix Suns', 'Phoenix'),
('Milwaukee Bucks', 'Milwaukee'),
('Denver Nuggets', 'Denver');

INSERT INTO Sponsor (sponsor_name) VALUES
('Nike'),
('Adidas'),
('Jordan Brand'),
('Tissot'),
('AT&T'),
('Red Bull'),
('Coca-Cola'),
('Amazon'),
('Google'),
('State Farm');

INSERT INTO Season (year_start, year_end) VALUES
('2010-10-01','2011-06-01'),
('2011-10-01','2012-06-01'),
('2012-10-01','2013-06-01'),
('2013-10-01','2014-06-01'),
('2014-10-01','2015-06-01'),
('2015-10-01','2016-06-01'),
('2016-10-01','2017-06-01'),
('2017-10-01','2018-06-01'),
('2018-10-01','2019-06-01'),
('2019-10-01','2020-06-01');

INSERT INTO Coach (coach_name, team_id) VALUES
('Darvin Ham', 1),
('Steve Kerr', 2),
('Joe Mazzulla', 3),
('Erik Spoelstra', 4),
('Billy Donovan', 5),
('Jason Kidd', 6),
('Jacque Vaughn', 7),
('Frank Vogel', 8),
('Adrian Griffin', 9),
('Michael Malone', 10),

('Assistant A - Lakers',1),
('Assistant A - Warriors',2),
('Assistant A - Celtics',3),
('Assistant A - Heat',4),
('Assistant A - Bulls',5),
('Assistant A - Mavericks',6),
('Assistant A - Nets',7),
('Assistant A - Suns',8),
('Assistant A - Bucks',9),
('Assistant A - Nuggets',10);

INSERT INTO Player (player_name, team_id) VALUES
('LeBron James',1),('Anthony Davis',1),('D’Angelo Russell',1),
('Stephen Curry',2),('Klay Thompson',2),('Draymond Green',2),
('Jayson Tatum',3),('Jaylen Brown',3),('Marcus Smart',3),
('Jimmy Butler',4),('Bam Adebayo',4),('Kyle Lowry',4),
('Zach LaVine',5),('Demar DeRozan',5),('Nikola Vučević',5),
('Luka Dončić',6),('Kyrie Irving',6),('Christian Wood',6),
('Kevin Durant',7),('Ben Simmons',7),('Mikal Bridges',7),
('Devin Booker',8),('Chris Paul',8),('Deandre Ayton',8),
('Giannis Antetokounmpo',9),('Khris Middleton',9),('Jrue Holiday',9),
('Nikola Jokić',10),('Jamal Murray',10),('Michael Porter Jr.',10);

INSERT INTO TeamSponsor(team_id, sponsor_id) VALUES
(1,1),(1,2),(1,3),
(2,1),(2,4),(2,5),
(3,6),(3,7),(3,8),
(4,3),(4,9),(4,10),
(5,2),(5,7),(5,10),
(6,6),(6,8),(6,9),
(7,1),(7,5),(7,7),
(8,4),(8,8),(8,10),
(9,3),(9,6),(9,9),
(10,2),(10,4),(10,5);

INSERT INTO Game(game_date, home_team_id, away_team_id, home_score, away_score) VALUES
('2010-11-01',1,2,102,99),
('2010-11-05',3,4,110,105),
('2011-01-10',5,6,98,92),
('2012-11-03',2,4,111,107),
('2013-01-20',6,8,115,112),
('2013-11-10',9,1,104,101),
('2014-12-12',10,3,99,94),
('2015-02-01',4,5,116,109),
('2016-12-20',3,7,101,98),
('2018-03-10',8,9,120,115),
('2011-02-23',1,3,108,101),
('2011-03-15',6,4,115,112),
('2012-11-12',2,5,105,98),
('2013-01-18',10,9,120,119),
('2014-02-22',8,7,118,110),
('2014-12-01',1,6,124,119),
('2015-03-18',4,9,109,105),
('2016-01-05',5,3,103,101),
('2017-03-29',7,10,111,107),
('2019-02-17',9,2,99,92);

INSERT INTO Referee(ref_name) VALUES
('Scott Foster'),
('Tony Brothers'),
('Zach Zarba'),
('Marc Davis'),
('James Capers'),
('Bill Kennedy'),
('Eric Lewis'),
('Kane Fitzgerald'),
('Lauren Holtkamp'),
('Ed Malloy');

INSERT INTO GameReferee(game_id, ref_id) VALUES
(1,1),(1,2),(1,3),
(2,2),(2,4),
(3,1),(3,6),
(4,3),(4,7),(4,8),
(5,4),(5,5),
(6,2),(6,9),
(7,1),(7,10),
(8,7),(8,8),(8,6),
(9,4),(9,5),
(10,3),(10,8),
(11,4),(11,9),
(12,3),(12,6),
(13,1),(13,10),
(14,7),(14,8),
(15,2),(15,9),
(16,4),(16,6),
(17,3),(17,7),
(18,2),(18,5),
(19,6),(19,7),
(20,9),(20,10);

INSERT INTO RuleUpdate(season_id, description, update_date) VALUES
(1,'Replay review expansion for buzzer-beaters','2010-12-01'),
(1,'Timeout reduction from 6 to 5 in final minutes','2011-02-01'),

(2,'Flopping warnings & escalating fines introduced','2012-01-15'),
(2,'Restricted area arc added under basket','2012-03-12'),

(3,'Clear path foul redefined','2013-02-20'),
(3,'Verticality rule updated for defenders','2013-04-10'),

(4,'Coach communication challenges pilot','2014-01-09'),
(4,'Instant replay center standardized league-wide','2014-03-25'),

(5,'Shot clock reset to 14 after offensive rebound','2015-01-20'),
(5,'Flagrant foul review procedures modified','2015-03-10'),

(6,'Coach challenge pilot expanded to preseason','2016-11-21'),
(6,'Continuation shooting motion clarification','2016-03-30'),

(7,'Transition take foul enforcement added','2017-01-08'),
(7,'Timeout structure simplified to reduce game length','2017-02-17'),

(8,'Landing space protection increased for shooters','2018-03-11'),
(8,'Goaltending review allowed on restricted plays','2018-04-05'),

(9,'Freedom of movement emphasis for off-ball contacts','2019-01-28'),
(9,'Travelling interpretation standardized (gather step)','2019-03-11'),

(10,'Coaches challenge fully implemented regular season','2020-01-08'),
(10,'Replay review limited to last 2 minutes only','2020-03-02');


INSERT INTO Contract(player_id, salary, start_date, end_date) VALUES
(1,39000000,'2018-07-01','2022-06-30'),
(2,35000000,'2019-07-01','2024-06-30'),
(4,45000000,'2017-07-01','2021-06-30'),
(5,18000000,'2019-07-01','2022-06-30'),
(7,28000000,'2020-07-01','2025-06-30'),
(10,36000000,'2018-07-01','2023-06-30'),
(13,21000000,'2019-07-01','2023-06-30'),
(16,43000000,'2021-07-01','2026-06-30'),
(22,29000000,'2020-07-01','2024-06-30'),
(25,45000000,'2019-07-01','2024-06-30');

INSERT INTO TeamSeason(team_id, season_id, wins, losses) VALUES
(1,1,57,25),
(2,1,55,27),
(3,2,48,34),
(4,2,50,32),
(5,3,45,37),
(6,3,33,49),
(7,4,42,40),
(8,4,60,22),
(9,5,56,26),
(10,5,49,33),
(1,2,51,31),
(1,3,45,37),
(2,2,57,25),
(2,3,58,24),
(3,4,55,27),
(4,5,53,29),
(6,6,49,33),
(8,7,61,21),
(9,8,60,22),
(10,8,55,27);

INSERT INTO PlayerSeason(player_id, season_id, PPG, APG, RPG, BPG, SPG) VALUES
(1,1,27.1,7.3,7.2,0.8,1.5),
(2,1,24.3,3.1,9.5,2.0,1.2),
(1,2,26.7,7.1,7.4,0.7,1.6),
(2,2,23.7,3.0,9.8,2.1,1.1),
(3,2,18.0,6.2,3.1,0.5,1.0),
(4,2,29.8,6.2,5.4,0.3,1.7),
(4,3,30.1,6.9,5.0,0.4,1.8),
(5,3,22.8,2.4,3.6,0.4,1.1),
(6,3,8.4,7.5,7.8,1.1,1.2),
(7,4,25.4,4.5,7.3,1.1,1.4),
(8,4,23.1,3.4,6.5,0.7,1.5),
(9,4,12.3,6.2,3.9,0.5,1.9),
(10,5,26.5,6.8,6.7,0.5,1.9),
(11,5,14.8,3.1,10.4,1.3,1.2),
(12,5,11.5,4.7,4.2,0.2,1.3),
(13,6,24.7,4.3,4.9,0.6,1.5),
(14,6,23.3,5.1,5.5,0.5,1.1),
(15,6,17.2,3.2,11.2,1.1,0.8),
(16,7,32.1,8.5,9.2,0.6,1.3),
(17,7,27.5,5.8,4.6,0.3,1.4),
(18,7,9.8,1.4,6.3,0.8,0.5),
(19,8,29.2,5.6,6.1,1.1,1.2),
(20,8,14.3,8.1,7.0,0.5,1.9),
(21,8,20.1,3.2,4.8,0.7,1.3),
(22,9,27.4,5.5,4.2,0.4,1.2),
(23,9,14.6,8.9,4.3,0.3,1.6),
(24,9,18.2,1.7,10.3,1.0,0.8),
(25,10,28.3,5.9,12.4,1.4,1.1),
(26,10,20.7,4.8,5.3,0.4,1.3),
(27,10,17.7,7.1,4.4,0.5,1.5),
(28,10,26.4,8.5,12.3,0.7,1.2),
(29,10,21.4,4.3,4.0,0.3,1.2),
(30,10,16.8,1.8,6.4,0.6,0.8);


SELECT * FROM Team;
SELECT * FROM Player;
SELECT * FROM Coach;
SELECT * FROM Sponsor;
SELECT * FROM Season;
SELECT * FROM Contract;
SELECT * FROM Game;
SELECT * FROM TeamSeason;
SELECT * FROM PlayerSeason;
SELECT * FROM TeamSponsor;
SELECT * FROM Referee;
SELECT * FROM GameReferee;
SELECT * FROM RuleUpdate;














