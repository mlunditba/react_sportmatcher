CREATE TABLE IF NOT EXISTS users(
  userid SERIAL PRIMARY KEY,
  username VARCHAR(100) UNIQUE NOT NULL,
  firstname VARCHAR(100) NOT NULL,
  lastname VARCHAR(100) NOT NULL,
  password VARCHAR(100) NOT NULL,
  role VARCHAR(50) NOT NULL,
  created_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS profile_pictures(
  profile_picture_id SERIAL PRIMARY KEY,
  userid INTEGER UNIQUE NOT NULL,
  data BYTEA NOT NULL,
  FOREIGN KEY (userid) REFERENCES users ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS clubs(
  clubid SERIAL PRIMARY KEY,
  clubname VARCHAR(100) NOT NULL,
  location VARCHAR(500) NOT NULL,
  club_created_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS pitches(
  pitchid SERIAL PRIMARY KEY,
  clubid INTEGER NOT NULL,
  pitchname VARCHAR(100) NOT NULL,
  sport VARCHAR(100) NOT NULL,
  pitch_created_at TIMESTAMP NOT NULL,
  FOREIGN KEY (clubid) REFERENCES clubs ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS pitch_pictures(
  pitch_picture_id SERIAL PRIMARY KEY,
  pitchid INTEGER UNIQUE NOT NULL,
  data BYTEA NOT NULL,
  FOREIGN KEY (pitchid) REFERENCES pitches ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS events(
  eventid SERIAL PRIMARY KEY,
  userid INTEGER NOT NULL,
  pitchid INTEGER NOT NULL,
  eventname VARCHAR(100) NOT NULL,
  description VARCHAR(500),
  max_participants INTEGER NOT NULL,
  inscription_ends_at TIMESTAMP,
  inscription_success BOOLEAN DEFAULT FALSE,
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
  commentid SERIAL PRIMARY KEY,
  commenter_id INTEGER NOT NULL,
  dest_userid INTEGER NOT NULL,
  comment VARCHAR(500) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  FOREIGN KEY (commenter_id) REFERENCES users ON DELETE CASCADE,
  FOREIGN KEY (dest_userid) REFERENCES users ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS club_comments(
  commentid SERIAL PRIMARY KEY,
  commenter_id INTEGER NOT NULL,
  dest_clubid INTEGER NOT NULL,
  comment VARCHAR(500) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  FOREIGN KEY (commenter_id) REFERENCES users ON DELETE CASCADE,
  FOREIGN KEY (dest_clubid) REFERENCES users ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS tournaments(
  tournamentid SERIAL PRIMARY KEY,
  tournamentname VARCHAR(100) NOT NULL,
  sport VARCHAR(100) NOT NULL,
  clubid INTEGER NOT NULL,
  max_teams INTEGER NOT NULL,
  team_size INTEGER NOT NULL,
  inscription_ends_at TIMESTAMP NOT NULL,
  inscription_success BOOLEAN NOT NULL DEFAULT FALSE,
  tournament_created_at TIMESTAMP NOT NULL,
  FOREIGN KEY (clubid) REFERENCES clubs ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS tournament_events(
  eventid SERIAL PRIMARY KEY,
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
  teamid SERIAL PRIMARY KEY,
  tournamentid INTEGER NOT NULL,
  teamname VARCHAR(100) NOT NULL,
  teamscore INTEGER NOT NULL,
  FOREIGN KEY (tournamentid) REFERENCES tournaments ON DELETE CASCADE
);
