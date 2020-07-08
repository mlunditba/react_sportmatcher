CREATE TABLE IF NOT EXISTS users(
  userid INTEGER IDENTITY PRIMARY KEY,
  username VARCHAR(100) UNIQUE NOT NULL,
  firstname VARCHAR(100) NOT NULL,
  lastname VARCHAR(100) NOT NULL,
  password VARCHAR(100) NOT NULL,
  role VARCHAR(50) NOT NULL,
  created_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS profile_pictures(
  profile_picture_id IDENTITY PRIMARY KEY,
  userid INTEGER UNIQUE NOT NULL,
  data BLOB NOT NULL,
  FOREIGN KEY (userid) REFERENCES users ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS clubs(
  clubid IDENTITY PRIMARY KEY,
  clubname VARCHAR(100) NOT NULL,
  location VARCHAR(500) NOT NULL,
  club_created_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS pitches(
  pitchid IDENTITY PRIMARY KEY,
  clubid INTEGER NOT NULL,
  pitchname VARCHAR(100) NOT NULL,
  sport VARCHAR(100) NOT NULL,
  pitch_created_at TIMESTAMP NOT NULL,
  FOREIGN KEY (clubid) REFERENCES clubs ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS events(
  eventid IDENTITY PRIMARY KEY,
  userid INTEGER NOT NULL,
  pitchid INTEGER NOT NULL,
  eventname VARCHAR(100) NOT NULL,
  description VARCHAR(500),
  max_participants INTEGER NOT NULL,
  inscription_ends_at TIMESTAMP,
  inscription_success BOOLEAN,
  starts_at TIMESTAMP NOT NULL,
  ends_at TIMESTAMP,
  event_created_at TIMESTAMP NOT NULL,
  FOREIGN KEY (userid) REFERENCES users ON DELETE CASCADE,
  FOREIGN KEY (pitchid) REFERENCES pitches ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS events_users(
  userid INTEGER NOT NULL,
  eventid INTEGER NOT NULL,
  teamid INTEGER, 
  vote INTEGER,
  FOREIGN KEY (userid) REFERENCES users ON DELETE CASCADE,
  FOREIGN KEY (teamid) REFERENCES tournament_teams ON DELETE CASCADE,
  PRIMARY KEY (userid, eventid)
);

CREATE TABLE IF NOT EXISTS user_comments(
  commentid IDENTITY PRIMARY KEY,
  commenter_id INTEGER NOT NULL,
  dest_userid INTEGER NOT NULL,
  comment VARCHAR(500) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  FOREIGN KEY (commenter_id) REFERENCES users ON DELETE CASCADE,
  FOREIGN KEY (dest_userid) REFERENCES users ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS club_comments(
  commentid IDENTITY PRIMARY KEY,
  commenter_id INTEGER NOT NULL,
  dest_clubid INTEGER NOT NULL,
  comment VARCHAR(500) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  FOREIGN KEY (commenter_id) REFERENCES users ON DELETE CASCADE,
  FOREIGN KEY (dest_clubid) REFERENCES users ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS tournaments(
  tournamentid IDENTITY PRIMARY KEY,
  tournamentname VARCHAR(100) NOT NULL,
  sport VARCHAR(100) NOT NULL,
  clubid INTEGER NOT NULL,
  max_teams INTEGER NOT NULL,
  team_size INTEGER NOT NULL,
  inscription_ends_at TIMESTAMP NOT NULL,
  inscription_success BOOLEAN NOT NULL,
  tournament_created_at TIMESTAMP NOT NULL,
  FOREIGN KEY (clubid) REFERENCES clubs ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS tournament_events(
  eventid IDENTITY PRIMARY KEY,
  userid INTEGER NOT NULL,
  pitchid INTEGER NOT NULL,
  eventname VARCHAR(100) NOT NULL,
  description VARCHAR(500),
  max_participants INTEGER NOT NULL,
  starts_at TIMESTAMP NOT NULL,
  ends_at TIMESTAMP,
  event_created_at TIMESTAMP NOT NULL,
  FOREIGN KEY (userid) REFERENCES users ON DELETE CASCADE,
  FOREIGN KEY (pitchid) REFERENCES pitches ON DELETE CASCADE,
  tournamentid INTEGER NOT NULL,
  round INTEGER NOT NULL,
  first_teamid INTEGER NOT NULL,
  second_teamid INTEGER NOT NULL,
  first_team_score INTEGER,
  second_team_score INTEGER,
  FOREIGN KEY (tournamentid) REFERENCES tournaments ON DELETE CASCADE,
  FOREIGN KEY (first_teamid) REFERENCES tournament_teams(teamid) ON DELETE CASCADE,
  FOREIGN KEY (second_teamid) REFERENCES tournament_teams(teamid) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS tournament_teams(
  teamid IDENTITY PRIMARY KEY,
  tournamentid INTEGER NOT NULL,
  teamname VARCHAR(100) NOT NULL,
  teamscore INTEGER NOT NULL,
  FOREIGN KEY (tournamentid) REFERENCES tournaments ON DELETE CASCADE
);

INSERT INTO users (userid, username, firstname, lastname, password, role, created_at) 
VALUES (1, 'user@name.com', 'first', 'last', '12345678', 'ROLE_USER', '2014-02-14 00:00:00');

INSERT INTO users (userid, username, firstname, lastname, password, role, created_at)
VALUES (2, 'other_user@name.com', 'second', 'fastlast', '87654321', 'ROLE_USER', '2015-02-14 00:00:00');

INSERT INTO users (userid, username, firstname, lastname, password, role, created_at)
VALUES (3, 'dummy@dummy.com', 'dummy', 'dummy', 'dummy', 'ROLE_USER', '2015-02-14 00:00:00');

INSERT INTO users (userid, username, firstname, lastname, password, role, created_at)
VALUES (4, 'admin@admin.admin', 'admin', 'admin', 'admin', 'ROLE_ADMIN', '2015-02-14 00:00:00');

INSERT INTO clubs (clubid, clubname, location, club_created_at) 
VALUES (1, 'club', 'location', '2010-01-15 10:01:40');

INSERT INTO pitches (pitchid, clubid, pitchname, sport, pitch_created_at) 
VALUES (1, 1, 'pitch', 'SOCCER', '2011-03-15 08:10:10');

INSERT INTO pitches (pitchid, clubid, pitchname, sport, pitch_created_at) 
VALUES (2, 1, 'pitch_2', 'SOCCER', '2011-03-15 08:10:10');

INSERT INTO events (eventid, userid, pitchid, eventname, description, max_participants, starts_at, ends_at, event_created_at, inscription_ends_at, inscription_success) 
VALUES (2, 1, 1, 'event', 'description', 2, '2030-05-20 10:00:00', '2030-05-20 11:00:00', '2019-05-15 11:00:00', '2030-05-18 11:00:00', false);

INSERT INTO events (eventid, userid, pitchid, eventname, description, max_participants, starts_at, ends_at, event_created_at, inscription_ends_at, inscription_success) 
VALUES (1, 1, 1, 'old_event', 'old_description', 2, '2019-01-20 10:00:00', '2019-01-20 11:00:00', '2019-01-15 11:00:00', '2019-01-18 11:00:00', false);

INSERT INTO events_users (userid, eventid) 
VALUES (1, 2);

INSERT INTO events_users (userid, eventid)
VALUES (2, 2);

INSERT INTO events_users (userid, eventid, vote)
VALUES (1, 1, -1);

INSERT INTO events_users (userid, eventid, vote)
VALUES (2, 1, -1);

INSERT INTO user_comments (commentid, commenter_id, dest_userid, comment, created_at)
VALUES (1, 1, 2, 'Comment', '2019-01-22 10:54:00');

INSERT INTO tournaments ( tournamentid,   tournamentname,   sport,    clubid, max_teams,  team_size,  inscription_ends_at,    inscription_success,  tournament_created_at) 
VALUES (                  1,              'tournament',     'SOCCER', 1,      4,          3,          '2030-01-20 11:00:00',  false,                '2030-01-15 11:00:00');

INSERT INTO tournament_teams  (teamid,  tournamentid,   teamname,   teamscore)
VALUES                        (1,       1,              'team_1',   0);

INSERT INTO tournament_teams  (teamid,  tournamentid,   teamname,   teamscore)
VALUES                        (2,       1,              'team_2',   0);

INSERT INTO tournament_teams  (teamid,  tournamentid,   teamname,   teamscore)
VALUES                        (3,       1,              'team_3',   0);

INSERT INTO tournament_teams  (teamid,  tournamentid,   teamname,   teamscore)
VALUES                        (4,       1,              'team_4',   0);

INSERT INTO tournament_events (eventid, userid, pitchid,  eventname,        max_participants, starts_at,              ends_at,                event_created_at,       tournamentid, round,  first_teamid, second_teamid,  first_team_score, second_team_score)
VALUES (                        3,      4,      1,        'tournament R1',  6,                '2019-01-22 11:00:00',  '2019-01-22 12:00:00',  '2019-01-20 11:00:00',  1,            1,      1,            3,              null,             null);

INSERT INTO tournament_events (eventid, userid, pitchid,  eventname,        max_participants, starts_at,              ends_at,                event_created_at,       tournamentid, round,  first_teamid, second_teamid,  first_team_score, second_team_score)
VALUES (                        4,      4,      2,        'tournament R1',  6,                '2019-01-22 11:00:00',  '2019-01-22 12:00:00',  '2019-01-20 11:00:00',  1,            1,      2,            4,              null,             null);

INSERT INTO tournament_events (eventid, userid, pitchid,  eventname,        max_participants, starts_at,              ends_at,                event_created_at,       tournamentid, round,  first_teamid, second_teamid,  first_team_score, second_team_score)
VALUES (                        5,      4,      1,        'tournament R2',  6,                '2019-01-29 11:00:00',  '2019-01-29 12:00:00',  '2019-01-20 11:00:00',  1,            2,      1,            4,              null,             null);

INSERT INTO tournament_events (eventid, userid, pitchid,  eventname,        max_participants, starts_at,              ends_at,                event_created_at,       tournamentid, round,  first_teamid, second_teamid,  first_team_score, second_team_score)
VALUES (                        6,      4,      2,        'tournament R2',  6,                '2019-01-29 11:00:00',  '2019-01-29 12:00:00',  '2019-01-20 11:00:00',  1,            2,      3,            2,              null,             null);

INSERT INTO tournament_events (eventid, userid, pitchid,  eventname,        max_participants, starts_at,              ends_at,                event_created_at,       tournamentid, round,  first_teamid, second_teamid,  first_team_score, second_team_score)
VALUES (                        7,      4,      1,        'tournament R3',  6,                '2019-02-05 11:00:00',  '2019-02-05 12:00:00',  '2019-01-20 11:00:00',  1,            3,      1,            2,              null,             null);

INSERT INTO tournament_events (eventid, userid, pitchid,  eventname,        max_participants, starts_at,              ends_at,                event_created_at,       tournamentid, round,  first_teamid, second_teamid,  first_team_score, second_team_score)
VALUES (                        8,      4,      2,        'tournament R3',  6,                '2019-02-05 11:00:00',  '2019-02-05 12:00:00',  '2019-01-20 11:00:00',  1,            3,      4,            3,              null,             null);

INSERT INTO events_users (userid, eventid, teamid) 
VALUES (2, 4, 4);

INSERT INTO events_users (userid, eventid, teamid) 
VALUES (2, 5, 4);

INSERT INTO events_users (userid, eventid, teamid) 
VALUES (2, 8, 4);
