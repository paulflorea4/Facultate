CREATE TABLE IF NOT EXISTS contact_message_types (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  type_code TEXT NOT NULL UNIQUE,
  label TEXT NOT NULL,
  description TEXT,
  display_order INTEGER DEFAULT 0,
  created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT OR IGNORE INTO contact_message_types (type_code, label, description, display_order)
VALUES
  ('reservation', 'Informații despre rezervări', 'Întrebări legate de rezervări și disponibilitate', 1),
  ('facilities', 'Facilități și servicii', 'Informații despre facilitățile și serviciile hotelului', 2),
  ('complaint', 'Reclamație', 'Pentru a raporta o problemă sau insatisfacție', 3),
  ('suggestion', 'Sugestie', 'Sugestii pentru îmbunătățiri', 4),
  ('events', 'Evenimente corporate', 'Informații despre serviciile pentru evenimente corporate', 5),
  ('other', 'Altele', 'Alte tipuri de întrebări', 6);

CREATE INDEX IF NOT EXISTS idx_contact_message_types_code ON contact_message_types(type_code);