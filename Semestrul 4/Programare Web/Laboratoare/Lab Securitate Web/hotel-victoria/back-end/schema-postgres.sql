CREATE TABLE IF NOT EXISTS contact_message_types (
  id SERIAL PRIMARY KEY,
  type_code VARCHAR(50) NOT NULL UNIQUE,
  label VARCHAR(255) NOT NULL,
  description TEXT,
  display_order INT DEFAULT 0,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO contact_message_types (type_code, label, description, display_order)
VALUES
  ('reservation', 'Informații despre rezervări', 'Întrebări legate de rezervări și disponibilitate', 1),
  ('facilities', 'Facilități și servicii', 'Informații despre facilitățile și serviciile hotelului', 2),
  ('complaint', 'Reclamație', 'Pentru a raporta o problemă sau insatisfacție', 3),
  ('suggestion', 'Sugestie', 'Sugestii pentru îmbunătățiri', 4),
  ('events', 'Evenimente corporate', 'Informații despre serviciile pentru evenimente corporate', 5),
  ('other', 'Altele', 'Alte tipuri de întrebări', 6)
ON CONFLICT (type_code) DO NOTHING;

CREATE INDEX IF NOT EXISTS idx_contact_message_types_code ON contact_message_types(type_code);