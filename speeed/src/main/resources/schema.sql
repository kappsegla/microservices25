CREATE TABLE IF NOT EXISTS users (
                                     id        BIGINT      PRIMARY KEY AUTO_INCREMENT,
                                     name      VARCHAR(255) NOT NULL UNIQUE,
                                     ip        VARCHAR(45)  NOT NULL,
                                     token     VARCHAR(36)  NOT NULL,
                                     count     INT          NOT NULL,
                                     version   INT          NOT NULL
);

CREATE TABLE IF NOT EXISTS global_stats (
                                            id          BIGINT      PRIMARY KEY,
                                            total_count INT         NOT NULL,
                                            version     INT         NOT NULL
);

INSERT IGNORE INTO global_stats (id, total_count)
VALUES (1, 0);
