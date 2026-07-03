CREATE DATABASE GestiuneTrenuri
GO

USE GestiuneTrenuri
GO

CREATE TABLE TrainTypes(
id INT PRIMARY KEY IDENTITY,
description NVARCHAR(100)
);

CREATE TABLE Trains(
id INT PRIMARY KEY IDENTITY,
name NVARCHAR(30),
t_id INT NOT NULL,
CONSTRAINT fk_train_type
	FOREIGN KEY (t_id) REFERENCES TrainTypes(id)
);

CREATE TABLE Station(
id INT PRIMARY KEY IDENTITY,
name NVARCHAR(30)
);

CREATE TABLE Route(
id INT PRIMARY KEY IDENTITY,
name NVARCHAR(30),
train_id INT NOT NULL,
CONSTRAINT fk_train_route
	FOREIGN KEY (train_id) REFERENCES Trains(id)
);

CREATE TABLE RouteStation(
route_id INT NOT NULL,
station_id INT NOT NULL,
CONSTRAINT pk_route_station PRIMARY KEY (route_id,station_id),
CONSTRAINT fk_routestation_route
	FOREIGN KEY (route_id) REFERENCES Route(id),
CONSTRAINT fk_routestation_station
	FOREIGN KEY (station_id) REFERENCES Station(id),
arrive TIME NOT NULL,
leave TIME NOT NULL
);

INSERT INTO TrainTypes(description) VALUES
('desc1'),('desc2'),('desc3')

INSERT INTO Trains(name,t_id) VALUES
('nume1',1),('nume2',3),('nume3',1),
('nume4',3),('nume5',2),('nume6',2);

INSERT INTO Station(name) VALUES
('statia1'),('statia2'),('statia3');

INSERT INTO Route(name,train_id) VALUES
('ruta1',2),('ruta2',1),('ruta3',2),
('ruta4',3),('ruta5',4),('ruta6',4),
('ruta7',6),('ruta8',5),('ruta9',3);

INSERT INTO RouteStation(route_id,station_id,arrive,leave) VALUES
(1,2,'8:30','8:45'),(2,2,'3:30','3:45'),(3,2,'5:20','5:30'),
(5,3,'7:30','8:45'),(5,2,'1:00','2:00'),(4,3,'2:20','2:45'),
(6,1,'7:20','7:55'),(9,3,'3:30','3:45'),(5,1,'6:30','6:45'),
(7,3,'3:30','3:45'),(4,1,'8:00','8:35'),(1,1,'1:30','2:00');

SELECT COUNT(*) FROM RouteStation
WHERE route_id=1 AND station_id=2

GO
CREATE OR ALTER PROCEDURE AddStation
@routeId INT,
@stationId INT,
@arrive TIME,
@leave TIME
AS
BEGIN
IF ((SELECT COUNT(*) FROM RouteStation WHERE route_id = @routeId AND station_id = @stationId) = 0)
BEGIN
	INSERT INTO RouteStation(route_id,station_id,arrive,leave) VALUES
	(@routeId,@stationId,@arrive,@leave);
END

	Update RouteStation
	SET arrive = @arrive , leave = @leave
	WHERE route_id = @routeId AND station_id = @stationId 
END;
GO

SELECT * FROM RouteStation

EXEC AddStation 1,1,'2:00','3:00';
EXEC AddStation 6,2,'1:00','2:00';



CREATE OR ALTER View AllStationsRoute
AS
	SELECT r.name,COUNT(station_id) as nrStatii FROM RouteStation rs INNER JOIN Route r ON rs.route_id=r.id
	GROUP BY r.id,r.name
	HAVING COUNT(station_id)=(SELECT COUNT(*) FROM Station)
GO

SELECT * FROM AllStationsRoute
SELECT * FROM RouteStation