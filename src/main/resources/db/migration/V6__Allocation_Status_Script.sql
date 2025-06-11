CREATE TABLE allocation_status(
    semester INTEGER PRIMARY KEY,
    status_code INTEGER NOT NULL,
    allocated_count INTEGER NOT NULL,
    unallocated_count INTEGER NOT NULL
);