SELECT * FROM users u JOIN ducks d ON d.user_id=u.id;
SELECT * FROM users u JOIN persoane p ON p.user_id=u.id;
SELECT * FROM users WHERE type='Duck' AND id IN (SELECT user_id FROM ducks WHERE tip = 'FLYING')
SELECT * FROM flocks
SELECT * FROM friendships;
SELECT * FROM users u JOIN friendships f ON (u.id=f.user_id1 OR u.id=f.user_id2)
WHERE (f.user_id)
SELECT * FROM messages
SELECT * FROM users_messages
SELECT * FROM friend_requests
SELECT * FROM events
SELECT * FROM race_events
SELECT * FROM race_event_ducks
SELECT * FROM event_subscribers

INSERT INTO users (username,email,password,type) VALUES
('duckster','duckster@gmail.com','d111','Duck'),
('mallard','mallard@gmail.com','m222','Duck'),
('george','george@yahoo.com','g333','Persoana'),
('quacko','quacko@gmail.com','q444','Duck'),
('feather','feather@gmail.com','f555','Duck'),
('scooter','scooter@gmail.com','s666','Duck'),
('claudia','claudia@yahoo.com','c777','Persoana'),
('dragos','dragos@yahoo.com','d888','Persoana'),
('paddles','paddles@gmail.com','p999','Duck'),
('wingman','wingman@gmail.com','w101','Duck'),
('roxana','roxana@yahoo.com','r202','Persoana'),
('splasher','splasher@gmail.com','s303','Duck'),
('quiver','quiver@gmail.com','q404','Duck'),
('flapper','flapper@gmail.com','f505','Duck'),
('marius','marius@yahoo.com','m606','Persoana');

INSERT INTO persoane (user_id, nume, prenume, data_nasterii, ocupatie, nivel_empatie) VALUES
(15, 'Popescu', 'Andrei', '1995-03-12', 'Programator', 8.5),
(25, 'Ionescu', 'Mircea', '1990-07-21', 'Designer', 7.2),
(26, 'Vasilescu', 'Catalin', '1988-11-05', 'Inginer', 6.9),
(30, 'Marin', 'George', '1993-01-28', 'Profesor', 9.1),
(34, 'Dumitrescu', 'Claudia', '1996-04-17', 'Medic', 8.8),
(35, 'Stan', 'Dragos', '1992-09-02', 'Analist', 7.6),
(38, 'Serban', 'Roxana', '1998-06-14', 'Student', 8.2),
(42, 'Popa', 'Marius', '1987-12-30', 'Manager', 7.9);

INSERT INTO ducks (user_id, tip, viteza, rezistenta) VALUES
(8,  'SWIMMING', 12.5, 80.0),
(27, 'FLYING', 20.1, 60.0),
(28, 'SWIMMING', 11.3, 75.4),
(29, 'FLYING_AND_SWIMMING', 18.0, 82.2),
(31, 'SWIMMING', 13.0, 78.1),
(32, 'FLYING', 22.4, 65.5),
(33, 'FLYING_AND_SWIMMING', 17.8, 79.3),
(36, 'SWIMMING', 10.9, 72.0),
(37, 'FLYING', 23.2, 66.7),
(39, 'SWIMMING', 14.1, 81.9),
(40, 'FLYING_AND_SWIMMING', 19.6, 77.5),
(41, 'FLYING', 21.7, 64.2);

INSERT INTO friendships (user_id1, user_id2) VALUES
(8, 27),
(8, 28),
(15, 34),
(15, 38),
(25, 30),
(25, 35),
(26, 42),
(26, 38),
(27, 32),
(27, 37),
(28, 36),
(28, 39),
(29, 40),
(29, 41),
(30, 34),
(31, 33),
(32, 41),
(33, 39),
(35, 42),
(37, 40);

INSERT INTO messages (id, from_id, message, data) VALUES
(1, 8, 'Hello!', '2025-12-01'),     
(2, 15, 'How are you?', '2025-12-02'),
(3, 25, 'Good morning!', '2025-12-03'),
(4, 26, 'See you soon', '2025-12-04'),
(5, 27, 'Happy Holidays!', '2025-12-05');

INSERT INTO users_messages (to_id, message_id) VALUES
(15, 1),  
(25, 2), 
(26, 3), 
(27, 4), 
(8, 5);   

INSERT INTO messages (id,from_id,message,data) VALUES
(6,52, 'Hi there!','2025-12-02'),
(7,52, 'How are u doing?','2025-12-04');

INSERT INTO users_messages(to_id,message_id) VALUES
(15,6),
(27,7),
(8,6)

INSERT INTO friendships(user_id1,user_id2) VALUES
(15,52),(27,52),(8,52)

SELECT * FROM users
SELECT * FROM messages
SELECT * FROM users_messages

SELECT m.* FROM messages m
                JOIN users_messages um ON um.message_id = m.id
                WHERE (m.from_id = 8 AND um.to_id = 52) OR (m.from_id = 52 AND um.to_id = 8)
                ORDER BY m.data;