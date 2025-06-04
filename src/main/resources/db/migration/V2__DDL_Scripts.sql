CREATE TABLE students (
    sid VARCHAR(12) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    program VARCHAR(10) NOT NULL,
    semester INTEGER NOT NULL
);

CREATE TABLE courses (
    cid VARCHAR(10) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    credits INTEGER NOT NULL,
    slot VARCHAR(4) NOT NULL
);

CREATE TABLE institute_reqs (
    program VARCHAR(10),
    semester INTEGER,
    category VARCHAR(10),
    course_cnt INTEGER NOT NULL,
    PRIMARY KEY (program, semester, category)
);

CREATE TABLE course_offerings (
    cid VARCHAR(10),
    program VARCHAR(10),
    semester INTEGER,
    category VARCHAR(10) NOT NULL,
    seats INTEGER NOT NULL,
    PRIMARY KEY (cid, program, semester),
    FOREIGN KEY (cid) REFERENCES courses(cid)
);

CREATE TABLE student_reqs (
    sid VARCHAR(12),
    category VARCHAR(10),
    course_cnt INTEGER NOT NULL,
    PRIMARY KEY (sid, category),
    FOREIGN KEY (sid) REFERENCES students(sid)
);

CREATE TABLE slot_prefs (
    sid VARCHAR(12),
    pref INTEGER,
    slot VARCHAR(4),
    PRIMARY KEY (sid, pref),
    FOREIGN KEY (sid) REFERENCES students(sid)
);

CREATE TABLE course_prefs (
    sid VARCHAR(12),
    slot VARCHAR(4),
    pref INTEGER,
    cid VARCHAR(10),
    PRIMARY KEY (sid, slot, pref),
    FOREIGN KEY (sid) REFERENCES students(sid),
    FOREIGN KEY (cid) REFERENCES courses(cid)
);

CREATE TABLE uploads(
    name VARCHAR(100) PRIMARY KEY,
    file BYTEA
);