-- Example data.sql to populate the database with initial values
INSERT INTO app_user (id, version, username, name, hashed_password) VALUES
                                                                        (1000, 0, 'user', 'User', '$2a$10$HL6ptCt/dcrqZcLESiMxmObFsJDnZgnPcHErFK.zXmL0m0i14Epza'),
                                                                        (1001, 0, 'admin', 'Admin', '$2a$10$AoqKsBmPAk1yhu8NZrQu2uDKMqJ70NUJrjyLFEaxmfx61nELUwygG');

-- Lisää roolit oikeaan tauluun
INSERT INTO user_roles (user_id, roles) VALUES
                                            (1000, 'USER'),
                                            (1001, 'ADMIN');
-- movies
INSERT INTO movie (id, version, title, director, release_year, genre, description) VALUES (1, 0, 'Mad Max: Fury Road', 'George Miller', 2015, 'Action', 'Post-apocalyptic action film set in a desert wasteland.');
INSERT INTO movie (id, version, title, director, release_year, genre, description) VALUES (2, 0, 'Die Hard', 'John McTiernan', 1988, 'Action', 'NYPD officer John McClane battles terrorists in a LA skyscraper.');
INSERT INTO movie (id, version, title, director, release_year, genre, description) VALUES (3, 0, 'John Wick', 'Chad Stahelski', 2014, 'Action', 'Retired hitman seeks vengeance after the killing of his dog.');
INSERT INTO movie (id, version, title, director, release_year, genre, description) VALUES (4, 0, 'The Matrix', 'Lana Wachowski, Lilly Wachowski', 1999, 'Action', 'A computer hacker learns about the true nature of reality.');
INSERT INTO movie (id, version, title, director, release_year, genre, description) VALUES (5, 0, 'Gladiator', 'Ridley Scott', 2000, 'Action', 'Roman general seeks revenge against the emperor who betrayed him.');
INSERT INTO movie (id, version, title, director, release_year, genre, description) VALUES (6, 0, 'Blade Runner 2049', 'Denis Villeneuve', 2017, 'Sci-Fi', 'A young blade runner uncovers a long-buried secret.');
INSERT INTO movie (id, version, title, director, release_year, genre, description) VALUES (7, 0, 'Interstellar', 'Christopher Nolan', 2014, 'Sci-Fi', 'Astronauts travel through a wormhole in search of a new home for humanity.');
INSERT INTO movie (id, version, title, director, release_year, genre, description) VALUES (8, 0, 'The Martian', 'Ridley Scott', 2015, 'Sci-Fi', 'An astronaut is left behind on Mars and must find a way to survive.');
INSERT INTO movie (id, version, title, director, release_year, genre, description) VALUES (9, 0, 'Arrival', 'Denis Villeneuve', 2016, 'Sci-Fi', 'A linguist works with the military to communicate with alien visitors.');
INSERT INTO movie (id, version, title, director, release_year, genre, description) VALUES (10, 0, 'District 9', 'Neill Blomkamp', 2009, 'Sci-Fi', 'Aliens become refugees in South Africa.');
INSERT INTO movie (id, version, title, director, release_year, genre, description) VALUES (11, 0, 'Superbad', 'Greg Mottola', 2007, 'Comedy', 'Two high school friends try to score alcohol for a party.');
INSERT INTO movie (id, version, title, director, release_year, genre, description) VALUES (12, 0, 'The Hangover', 'Todd Phillips', 2009, 'Comedy', 'A group of friends try to piece together a wild night in Vegas.');
INSERT INTO movie (id, version, title, director, release_year, genre, description) VALUES (13, 0, 'Bridesmaids', 'Paul Feig', 2011, 'Comedy', 'A woman competes with another bridesmaid for the title of maid of honor.');
INSERT INTO movie (id, version, title, director, release_year, genre, description) VALUES (14, 0, 'Airplane!', 'Jim Abrahams, David Zucker, Jerry Zucker', 1980, 'Comedy', 'A parody of disaster films with ridiculous humor.');
INSERT INTO movie (id, version, title, director, release_year, genre, description) VALUES (15, 0, 'Anchorman: The Legend of Ron Burgundy', 'Adam McKay', 2004, 'Comedy', 'A 1970s news anchor struggles with changing times.');
INSERT INTO movie (id, version, title, director, release_year, genre, description) VALUES (16, 0, 'The Shawshank Redemption', 'Frank Darabont', 1994, 'Drama', 'A man maintains hope while serving a life sentence in prison.');
INSERT INTO movie (id, version, title, director, release_year, genre, description) VALUES (17, 0, 'Forrest Gump', 'Robert Zemeckis', 1994, 'Drama', 'A simple man witnesses key moments in 20th-century American history.');
INSERT INTO movie (id, version, title, director, release_year, genre, description) VALUES (18, 0, 'The Godfather', 'Francis Ford Coppola', 1972, 'Drama', 'The aging patriarch of a crime dynasty transfers control to his son.');
INSERT INTO movie (id, version, title, director, release_year, genre, description) VALUES (19, 0, 'Schindler''s List', 'Steven Spielberg', 1993, 'Drama', 'A German businessman saves Jews during the Holocaust.');
INSERT INTO movie (id, version, title, director, release_year, genre, description) VALUES (20, 0, 'Fight Club', 'David Fincher', 1999, 'Drama', 'An insomniac office worker forms an underground fight club.');
INSERT INTO movie (id, version, title, director, release_year, genre, description) VALUES (21, 0, 'The Exorcist', 'William Friedkin', 1973, 'Horror', 'A mother seeks help for her possessed daughter.');
INSERT INTO movie (id, version, title, director, release_year, genre, description) VALUES (22, 0, 'Hereditary', 'Ari Aster', 2018, 'Horror', 'A family unravels after a tragic death reveals dark secrets.');
INSERT INTO movie (id, version, title, director, release_year, genre, description) VALUES (23, 0, 'The Shining', 'Stanley Kubrick', 1980, 'Horror', 'A writer''s descent into madness at a haunted hotel.');
INSERT INTO movie (id, version, title, director, release_year, genre, description) VALUES (24, 0, 'Get Out', 'Jordan Peele', 2017, 'Horror', 'A Black man uncovers disturbing secrets at his girlfriend''s family home.');
INSERT INTO movie (id, version, title, director, release_year, genre, description) VALUES (25, 0, 'A Quiet Place', 'John Krasinski', 2018, 'Horror', 'A family must live in silence to avoid monsters with ultra-sensitive hearing.');
INSERT INTO movie (id, version, title, director, release_year, genre, description) VALUES (26, 0, 'Spirited Away', 'Hayao Miyazaki', 2001, 'Animation', 'A girl enters a magical world and must work to free her parents.');
INSERT INTO movie (id, version, title, director, release_year, genre, description) VALUES (27, 0, 'Toy Story', 'John Lasseter', 1995, 'Animation', 'A cowboy doll feels threatened by a new spaceman toy.');
INSERT INTO movie (id, version, title, director, release_year, genre, description) VALUES (28, 0, 'The Lion King', 'Roger Allers, Rob Minkoff', 1994, 'Animation', 'A lion cub struggles to accept his destiny as king.');
INSERT INTO movie (id, version, title, director, release_year, genre, description) VALUES (29, 0, 'Spider-Man: Into the Spider-Verse', 'Bob Persichetti, Peter Ramsey, Rodney Rothman', 2018, 'Animation', 'Teen Miles Morales becomes Spider-Man and meets alternate versions of himself.');
INSERT INTO movie (id, version, title, director, release_year, genre, description) VALUES (30, 0, 'Wall-E', 'Andrew Stanton', 2008, 'Animation', 'A lonely robot cleans up an abandoned Earth and falls in love.');
INSERT INTO movie (id, version, title, director, release_year, genre, description) VALUES (100, 0, 'Parasite', 'Bong Joon-ho', 2019, 'Thriller', 'A poor family schemes to become employed by a wealthy household.');

-- Arvostelut (reviews)
INSERT INTO review (id, version, comment, rating, movie_id, reviewer_id) VALUES
                                                                             (1, 0, 'Upea visuaalinen teos ja jännittävä tarina!', 5, 1, 1000),
                                                                             (2, 0, 'Hyvä toimintaa, mutta juoni oli hieman heikko.', 3, 1, 1001),
                                                                             (3, 0, 'Klassikko, joka on edelleen yksi parhaista toimintaelokuvista!', 5, 2, 1000),
                                                                             (4, 0, 'John Wick on uusi aikakauden toimintasankari.', 4, 3, 1001),
                                                                             (5, 0, 'Mielenkiintoinen scifi-filosofinen tarina, joka tekee tiukkaa.', 4, 4, 1000),
                                                                             (6, 0, 'Visuaalisesti upea, mutta juoni oli hieman sekava.', 3, 6, 1001),
                                                                             (7, 0, 'Todella syvällinen ja liikuttava elokuva.', 5, 7, 1000),
                                                                             (8, 0, 'Hauska komedia, joka toimii edelleen hyvin.', 4, 11, 1001),
                                                                             (9, 0, 'Yksi kaikkien aikojen parhaista elokuvista.', 5, 16, 1000),
                                                                             (10, 0, 'Kauhua hienosti rakennettuna, ei turhaa veristä väkivaltaa.', 4, 21, 1001),
                                                                             (11, 0, 'Miyazakin mestariteos, kaunis ja syvällinen.', 5, 26, 1000),
                                                                             (12, 0, 'Täydellinen animaatio kaikille ikäluokille.', 5, 27, 1001),
                                                                             (13, 0, 'Todella uniikki ja tuore ote supersankareihin.', 5, 29, 1000),
                                                                             (14, 0, 'Hieno yhdistelmä trilleriä ja mustaa komediaa.', 5, 100, 1001),
                                                                             (15, 0, 'Gladiaattoritaistelut olivat upeita, mutta tarina kliseinen.', 3, 5, 1000);