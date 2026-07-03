USE DB_NBA;

SELECT * FROM Player;
SELECT * FROM PlayerSeason;
SELECT * FROM Season;
SELECT * FROM TeamSeason;
SELECT * FROM Team;
SELECT * FROM Sponsor;
SELECT * FROM TeamSponsor;
SELECT * FROM Coach;
SELECT * FROM Game;
SELECT * FROM Contract;

-- m-n + WHERE
-- Numele,data sezonului,statisticile jucatorului care a avut cel putin 25 de puncte in primul sezon
SELECT p.player_name, s.year_start, s.year_end, ps.PPG, ps.APG, ps.RPG
FROM PlayerSeason ps 
INNER JOIN Player p ON ps.player_id = p.player_id
INNER JOIN Season s ON ps.season_id = s.season_id
WHERE s.season_id = 1 AND ps.PPG >= 25
ORDER BY ps.PPG DESC;

-- m-n + WHERE
-- Numele echipelor care sunt sponsorizate de Nike
SELECT t.team_name AS SponsoredByNike
FROM TeamSponsor ts 
INNER JOIN Team t ON t.team_id=ts.team_id
INNER JOIN Sponsor s ON ts.sponsor_id=s.sponsor_id
WHERE s.sponsor_name='Nike';

-- min. 3 TABELE + WHERE
-- Numele antrenorului si numele echipei corespunzatoare care au pierdut meciuri acasa
SELECT c.coach_name,t.team_name,g.home_score,g.away_score,g.game_date
FROM Coach c 
INNER JOIN Team t ON t.team_id=c.team_id
INNER JOIN Game g ON g.home_team_id=t.team_id
WHERE g.away_score>g.home_score;

-- min. 3 TABELE + WHERE
-- Meciurile jucate in luna aprilie cu fiecare echipa si antrenorul corespunzator
SELECT c1.coach_name,t1.team_name,g.home_score,g.away_score,t2.team_name,c2.coach_name,g.game_date
FROM Team t1 
INNER JOIN Game g ON t1.team_id=g.home_team_id 
INNER JOIN Team t2 ON t2.team_id=g.away_team_id
INNER JOIN Coach c1 ON c1.team_id=t1.team_id
INNER JOIN Coach c2 ON c2.team_id=t2.team_id
WHERE g.game_date LIKE '2024-04%';

-- min. 3 TABELE + GROUP BY + HAVING
-- Numele echipei cu antrenorul corespunzator care au media de salariu a jucatorilor de minim 35000000
SELECT c.coach_name,t.team_name,AVG(ct.salary) AS AverageSalaryOfPlayers
FROM Coach c 
INNER JOIN Team t ON c.team_id=t.team_id
INNER JOIN Player p ON t.team_id=p.team_id
INNER JOIN Contract ct ON ct.player_id=p.player_id
GROUP BY c.coach_name,t.team_name
HAVING AVG(ct.salary)>=35000000;

-- min. 3 TABELE + DISTINCT + GROUP BY + HAVING
-- Numele sponsorilor impreuna cu numarul de echipe pe care le sponsorizeaza cu media de salariu a jucatorilor de minim 30000000
SELECT s.sponsor_name, COUNT(DISTINCT t.team_id) AS SupportedTeams
FROM Sponsor s
INNER JOIN TeamSponsor ts ON s.sponsor_id = ts.sponsor_id
INNER JOIN Team t ON ts.team_id = t.team_id
INNER JOIN Player p ON t.team_id = p.team_id
INNER JOIN Contract c ON p.player_id = c.player_id
GROUP BY s.sponsor_name
HAVING AVG(c.salary) > 30000000;

-- min. 3 TABELE + GROUP BY
-- Numele echipei si al antrenorului corespunzator impreuna cu numarul de jucatori care joaca la echipa
SELECT t.team_name,c.coach_name,COUNT(p.player_id) AS TotalPlayers
FROM Team t 
INNER JOIN Player p ON t.team_id=p.team_id
INNER JOIN Coach c ON c.team_id=t.team_id
GROUP BY t.team_name,c.coach_name;

-- min. 3 TABELE + DISTINCT + WHERE
-- Numele jucatorului si echipei corespunzatoare care a avut sub 25 de puncte intr-un sezon
SELECT DISTINCT t.team_name,p.player_name
FROM PlayerSeason ps 
INNER JOIN Player p ON ps.player_id = p.player_id
INNER JOIN Season s ON ps.season_id = s.season_id
INNER JOIN Team t ON t.team_id=p.team_id
WHERE ps.PPG < 25;

-- min. 3 TABELE + WHERE + GROUP BY + HAVING
-- Numele echipei si a antrenorului care au avut minim 50 de castiguri in sezonul care incepe in 2024-10-01 
SELECT t.team_name, c.coach_name, ts.wins
FROM TeamSeason ts
INNER JOIN Team t ON ts.team_id = t.team_id
INNER JOIN Season s ON ts.season_id = s.season_id
INNER JOIN Coach c ON c.team_id=t.team_id
WHERE s.year_start = '2024-10-01'
GROUP BY t.team_name,c.coach_name,ts.wins
HAVING ts.wins > 50;

-- DISTINCT
-- Orasele distincte ale echipelor
SELECT DISTINCT city
FROM Team;
