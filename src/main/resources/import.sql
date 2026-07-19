-- ==========================================
-- 1. GENERI
-- ==========================================
INSERT INTO genere (id, nome) VALUES (nextval('genere_seq'), 'Azione');
INSERT INTO genere (id, nome) VALUES (nextval('genere_seq'), 'GDR');
INSERT INTO genere (id, nome) VALUES (nextval('genere_seq'), 'Avventura');

-- ==========================================
-- 2. PIATTAFORME
-- ==========================================
INSERT INTO piattaforma (id, nome) VALUES (nextval('piattaforma_seq'), 'PlayStation 5');
INSERT INTO piattaforma (id, nome) VALUES (nextval('piattaforma_seq'), 'PC');
INSERT INTO piattaforma (id, nome) VALUES (nextval('piattaforma_seq'), 'Xbox Series X');

-- ==========================================
-- 3. SVILUPPATORI
-- ==========================================
INSERT INTO sviluppatore (id, nome) VALUES (nextval('sviluppatore_seq'), 'Naughty Dog');
INSERT INTO sviluppatore (id, nome) VALUES (nextval('sviluppatore_seq'), 'CD Projekt Red');
INSERT INTO sviluppatore (id, nome) VALUES (nextval('sviluppatore_seq'), 'FromSoftware');

-- ==========================================
-- 4. UTENTI
-- ==========================================
INSERT INTO utente (id, username, email, password, data_registrazione, ruolo) VALUES (nextval('utente_seq'), 'mario_rossi', 'mario@email.com', 'password123', '2026-07-01', 'USER');
INSERT INTO utente (id, username, email, password, data_registrazione, ruolo) VALUES (nextval('utente_seq'), 'luigi_verdi', 'luigi@email.com', 'qwerty', '2026-07-05', 'USER');

-- ==========================================
-- 5. VIDEOGIOCHI
-- ==========================================
INSERT INTO videogioco (id, anno_uscita, descrizione, titolo, url_copertina, sviluppatore_id) VALUES (nextval('videogioco_seq'), 2013, 'Un viaggio post-apocalittico emozionante.', 'The Last of Us', 'url_immagine_tlou.jpg', (SELECT id FROM sviluppatore WHERE nome = 'Naughty Dog'));
INSERT INTO videogioco (id, anno_uscita, descrizione, titolo, url_copertina, sviluppatore_id) VALUES (nextval('videogioco_seq'), 2015, 'Un cacciatore di mostri in un mondo fantasy incredibile.', 'The Witcher 3', 'url_immagine_witcher.jpg', (SELECT id FROM sviluppatore WHERE nome = 'CD Projekt Red'));
INSERT INTO videogioco (id, anno_uscita, descrizione, titolo, url_copertina, sviluppatore_id) VALUES (nextval('videogioco_seq'), 2022, 'Un gioco di azione e di ruolo molto impegnativo.', 'Elden Ring', 'url_immagine_elden.jpg', (SELECT id FROM sviluppatore WHERE nome = 'FromSoftware'));

-- ==========================================
-- 6. ASSOCIAZIONI	
-- ==========================================
INSERT INTO videogioco_generi (videogiochi_id, generi_id) VALUES ((SELECT id FROM videogioco WHERE titolo = 'The Last of Us'), (SELECT id FROM genere WHERE nome = 'Azione'));
INSERT INTO videogioco_generi (videogiochi_id, generi_id) VALUES ((SELECT id FROM videogioco WHERE titolo = 'The Last of Us'), (SELECT id FROM genere WHERE nome = 'Avventura'));
INSERT INTO videogioco_generi (videogiochi_id, generi_id) VALUES ((SELECT id FROM videogioco WHERE titolo = 'The Witcher 3'), (SELECT id FROM genere WHERE nome = 'GDR'));

INSERT INTO videogioco_piattaforme (videogiochi_id, piattaforme_id) VALUES ((SELECT id FROM videogioco WHERE titolo = 'The Last of Us'), (SELECT id FROM piattaforma WHERE nome = 'PlayStation 5'));
INSERT INTO videogioco_piattaforme (videogiochi_id, piattaforme_id) VALUES ((SELECT id FROM videogioco WHERE titolo = 'The Witcher 3'), (SELECT id FROM piattaforma WHERE nome = 'PlayStation 5'));
INSERT INTO videogioco_piattaforme (videogiochi_id, piattaforme_id) VALUES ((SELECT id FROM videogioco WHERE titolo = 'The Witcher 3'), (SELECT id FROM piattaforma WHERE nome = 'PC'));

-- ==========================================
-- 7. LIBRERIA UTENTI (ex RECENSIONI)
-- ==========================================
INSERT INTO videogioco_libreria (id, commento, data_aggiunta, voto, utente_id, videogioco_id) VALUES (nextval('videogioco_libreria_seq'), 'Un capolavoro assoluto, storia bellissima.', '2026-07-10', 10, (SELECT id FROM utente WHERE username = 'mario_rossi'), (SELECT id FROM videogioco WHERE titolo = 'The Last of Us'));
INSERT INTO videogioco_libreria (id, commento, data_aggiunta, voto, utente_id, videogioco_id) VALUES (nextval('videogioco_libreria_seq'), 'Troppo difficile per me, ma bel gioco.', '2026-07-12', 7, (SELECT id FROM utente WHERE username = 'luigi_verdi'), (SELECT id FROM videogioco WHERE titolo = 'Elden Ring'));
INSERT INTO videogioco_libreria (id, data_aggiunta, utente_id, videogioco_id) VALUES (nextval('videogioco_libreria_seq'), '2026-07-15', (SELECT id FROM utente WHERE username = 'mario_rossi'), (SELECT id FROM videogioco WHERE titolo = 'The Witcher 3'));